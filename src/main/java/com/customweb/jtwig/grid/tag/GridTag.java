package com.customweb.jtwig.grid.tag;

import java.util.HashMap;
import java.util.Map;

import com.customweb.grid.Grid;
import com.customweb.grid.filter.OrderBy;
import com.customweb.jtwig.grid.addon.GridAddon;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;
import com.customweb.jtwig.lib.attribute.model.definition.EmptyAttributeDefinition;
import com.customweb.jtwig.lib.attribute.model.definition.VariableAttributeDefinition;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

public class GridTag extends AbstractGridTag<GridTag> {

	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new VariableAttributeDefinition("model", false));
		attributeDefinitions.add(new EmptyAttributeDefinition("ajax"));
		attributeDefinitions.getDynamicAttributeDefinition().addDisallowedKeys("id");
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		try {
			JtwigResource resource = GridAddon.getResourceHandler().resolve("grid");
			return new Compiled(context.parse(resource).compile(context), super.compile(context), this.getAttributeCollection());
		} catch (ParseException | ResourceException e) {
			throw new CompileException(e);
		}
	}

	private class Compiled extends AbstractGridTag<GridTag>.Compiled {
		protected Compiled(Renderable block, Renderable content, AttributeCollection attributeCollection) {
			super(block, content, attributeCollection);
		}

		public String getModelAttribute() {
			return this.getAttributeValue("model", DEFAULT_MODEL_ATTRIBUTE_NAME);
		}

		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with(MODEL_ATTRIBUTE_VARIABLE_NAME, this.getModelAttribute());

			context.with("grid", new Data(this.renderContentAsString(context), context, this.getAttributeCollection()));
		}
	}

	public class Data extends AbstractGridTag<GridTag>.Data {
		private Map<String, String> hiddenFields;
		private String content;

		protected Data(String content, RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
			this.content = content;
		}

		public boolean isAjax() {
			return this.getAttributeCollection().hasAttribute("ajax");
		}

		public String getId() {
			return this.getGrid().getGridId();
		}

		public String getUrl() {
			return this.getGrid().getCurrentUrl().getPath();
		}

		public Map<String, String> getHiddenFields() {
			if (this.hiddenFields == null) {
				this.hiddenFields = new HashMap<String, String>();
				int pageNumber = this.getGrid().getFilter().getPageNumber();

				this.hiddenFields.put(Grid.getPageNumberParameter(this.getGrid().getGridId()), new Integer(pageNumber).toString());

				for (OrderBy orderBy : this.getGrid().getFilter().getOrderBys()) {
					this.hiddenFields.put(this.getGrid().getOrderByParameterName(orderBy.getFieldName()), orderBy.getSorting());
				}
			}
			return this.hiddenFields;
		}

		public String getContent() {
			return this.content;
		}
	}

}
