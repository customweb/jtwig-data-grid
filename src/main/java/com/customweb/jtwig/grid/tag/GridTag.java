package com.customweb.jtwig.grid.tag;

import com.customweb.jtwig.grid.addon.GridAddon;
import com.customweb.jtwig.lib.attribute.model.AbstractAttributeTag;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;
import com.customweb.jtwig.lib.attribute.model.definition.VariableAttributeDefinition;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

public class GridTag extends AbstractAttributeTag<GridTag> {
	
	private static final String DEFAULT_MODEL_ATTRIBUTE_NAME = "gridModel";
	
	private static final String MODEL_ATTRIBUTE = "modelAttribute";
	
	public static final String MODEL_ATTRIBUTE_VARIABLE_NAME = GridTag.class.getName() + "." + MODEL_ATTRIBUTE;
	
	public static final String GRID_ID_ATTRIBUTE_NAME = GridTag.class.getName() + ".id";

	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new VariableAttributeDefinition("model", false));
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

	private class Compiled extends AbstractAttributeTag<GridTag>.Compiled {
		protected Compiled(Renderable block, Renderable content, AttributeCollection attributeCollection) {
			super(block, content, attributeCollection);
		}

		public String getId() {
			return this.getModelAttribute();
		}

		public String getModelAttribute() {
			if (this.getAttributeCollection().hasAttribute("model")) {
				return this.getAttributeValue("model");
			} else {
				return DEFAULT_MODEL_ATTRIBUTE_NAME;
			}
		}

		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with(MODEL_ATTRIBUTE_VARIABLE_NAME, this.getModelAttribute());
			context.with(GRID_ID_ATTRIBUTE_NAME, this.getId());
			
			context.with("grid", new Data(this.getId(), this.renderContentAsString(context), context, this.getAttributeCollection()));
		}
	}
	
	public class Data extends AbstractAttributeTag<GridTag>.Data {
		private String id;
		private String content;
		
		protected Data(String id, String content, RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
			this.id = id;
			this.content = content;
		}
		
		public String getId() {
			return this.id;
		}
		
		public String getContent() {
			return this.content;
		}
	}
	
}
