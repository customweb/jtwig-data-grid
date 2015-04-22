package com.customweb.jtwig.grid.tag;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;
import org.jtwig.render.RenderContext;

import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;

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
			Loader.Resource resource = this.retrieveResource(context, "grid/submit");
			return new Compiled(context.environment().parse(resource).compile(context), super.compile(context), this.getAttributeCollection());
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
