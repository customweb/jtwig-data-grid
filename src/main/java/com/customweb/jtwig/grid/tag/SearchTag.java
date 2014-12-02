package com.customweb.jtwig.grid.tag;

import com.customweb.jtwig.grid.addon.GridAddon;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;
import com.customweb.jtwig.lib.attribute.model.definition.NamedAttributeDefinition;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

public class SearchTag extends AbstractGridTag<SearchTag> {

	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new NamedAttributeDefinition("columnName", true));
		attributeDefinitions.getDynamicAttributeDefinition().addDisallowedKeys("name", "type");
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		try {
			JtwigResource resource = GridAddon.getResourceHandler().resolve("search");
			return new Compiled(context.parse(resource).compile(context), this.getAttributeCollection());
		} catch (ParseException | ResourceException e) {
			throw new CompileException(e);
		}
	}

	private class Compiled extends AbstractGridTag<SearchTag>.Compiled {
		protected Compiled(Renderable block, AttributeCollection attributeCollection) {
			super(block, null, attributeCollection);
		}

		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with("search", new Data(context, this.getAttributeCollection()));
		}
	}
	
	public class Data extends AbstractGridTag<SearchTag>.Data {
		protected Data(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
		}
		
		public String getParameterName() {
			return this.getGrid().getFieldFilterParameterName(this.getAttributeValue("columnName"));
		}
	}
	
}
