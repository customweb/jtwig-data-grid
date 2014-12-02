package com.customweb.jtwig.grid.tag;

import com.customweb.jtwig.grid.addon.GridAddon;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

public class SubmitTag extends AbstractGridTag<SubmitTag> {
	
	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.getDynamicAttributeDefinition().addDisallowedKeys("type");
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		try {
			JtwigResource resource = GridAddon.getResourceHandler().resolve("submit");
			return new Compiled(context.parse(resource).compile(context), super.compile(context), this.getAttributeCollection());
		} catch (ParseException | ResourceException e) {
			throw new CompileException(e);
		}
	}

	private class Compiled extends AbstractGridTag<SubmitTag>.Compiled {
		protected Compiled(Renderable block, Renderable content, AttributeCollection attributeCollection) {
			super(block, content, attributeCollection);
		}
		
		public boolean isInGridContext(RenderContext context) {
			return context.map(GridTag.GRID_CONTEXT_VARIABLE_NAME).equals(Boolean.TRUE);
		}

		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with("submit", new Data(this.renderContentAsString(context), context, this.getAttributeCollection()));
		}
		
		@Override
		public void render(RenderContext context) throws RenderException {
			if (!this.isInGridContext(context)) {
				throw new RuntimeException("The 'submit' tag can only be used inside a valid 'grid' tag.");
			}
			
			super.render(context);
		}
	}
	
	public class Data extends AbstractGridTag<SubmitTag>.Data {
		private String content;
		
		protected Data(String content, RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
			this.content = content;
		}
		
		public String getContent() {
			return this.content;
		}
	}
	
}
