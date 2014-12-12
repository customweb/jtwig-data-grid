package com.customweb.jtwig.grid.tag;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.customweb.grid.Grid;
import com.customweb.jtwig.lib.attribute.model.AbstractAttributeTag;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;
import com.customweb.jtwig.lib.attribute.model.definition.EmptyAttributeDefinition;
import com.customweb.jtwig.lib.attribute.model.definition.NamedAttributeDefinition;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

public class TableTag extends AbstractAttributeTag<TableTag> {
	
	public static final String TABLE_CONTEXT_VARIABLE_NAME = TableTag.class.getName() + ".context";
	
	public static final String ROW_MODEL_VARIABLE_NAME = TableTag.class.getName() + ".rowModel";
	
	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new NamedAttributeDefinition("var", true));
		attributeDefinitions.add(new EmptyAttributeDefinition("showFilterRow"));
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		try {
			JtwigResource resource = this.retrieveResource(context, "grid/table");
			return new Compiled(context.parse(resource).compile(context), super.compile(context), this.getAttributeCollection());
		} catch (ParseException | ResourceException e) {
			throw new CompileException(e);
		}
	}

	private class Compiled extends AbstractAttributeTag<TableTag>.Compiled {
		protected Compiled(Renderable block, Renderable content, AttributeCollection attributeCollection) {
			super(block, content, attributeCollection);
		}
		
		public boolean isInGridContext(RenderContext context) {
			return context.map(GridTag.GRID_CONTEXT_VARIABLE_NAME).equals(Boolean.TRUE);
		}

		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with(TABLE_CONTEXT_VARIABLE_NAME, true);
			
			context.with("table", new Data(this.getContent(), context, this.getAttributeCollection()));
		}
		
		@Override
		public void render(RenderContext context) throws RenderException {
			if (!this.isInGridContext(context)) {
				throw new RuntimeException("The 'table' tag can only be used inside a valid 'grid' tag.");
			}
			
			super.render(context);
		}
	}
	
	public class Data extends AbstractAttributeTag<TableTag>.Data {
		private Grid<?> grid;
		private Renderable content;
		
		protected Data(Renderable content, RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
			this.content = content;
		}
		
		public List<String> getRows() throws RenderException {
			List<String> rows = new ArrayList<String>();
			for (Object row : this.getGrid().getResultList()) {
				RenderContext context = this.getContext().isolatedModel();
				context.with(this.getAttributeValue("var"), row);
				context.with(ROW_MODEL_VARIABLE_NAME, row);
				rows.add(this.renderContentAsString(context, ColumnTag.RenderType.CONTENT));
			}
			return rows;
		}
		
		public String getHeaderContent() throws RenderException {
			return this.renderContentAsString(this.getContext(), ColumnTag.RenderType.HEADER);
		}
		
		public boolean isShowFilterRow() {
			return this.getAttributeCollection().hasAttribute("showFilterRow");
		}
		
		public String getFilterContent() throws RenderException {
			return this.renderContentAsString(this.getContext(), ColumnTag.RenderType.FILTER);
		}
		
		private Grid<?> getGrid() {
			if (this.grid == null) {
				this.grid = (Grid<?>) this.getContext().map((String) this.getContext().map(GridTag.MODEL_ATTRIBUTE_VARIABLE_NAME));
			}
			return this.grid;
		}
		
		private String renderContentAsString(RenderContext context, ColumnTag.RenderType renderType) throws RenderException {
			ByteArrayOutputStream contentRenderStream = new ByteArrayOutputStream();
			context = context.newRenderContext(contentRenderStream);
			context.with(ColumnTag.RENDER_TYPE_VARIABLE_NAME, renderType);
			this.content.render(context);
			return contentRenderStream.toString();
		}
	}
	
}
