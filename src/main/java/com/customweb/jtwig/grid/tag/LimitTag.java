package com.customweb.jtwig.grid.tag;

import java.util.ArrayList;
import java.util.List;

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

public class LimitTag extends AbstractGridTag<LimitTag> {

	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new NamedAttributeDefinition("steps", true));
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		try {
			JtwigResource resource = GridAddon.getResourceHandler().resolve("limit");
			return new Compiled(context.parse(resource).compile(context), this.getAttributeCollection());
		} catch (ParseException | ResourceException e) {
			throw new CompileException(e);
		}
	}

	private class Compiled extends AbstractGridTag<LimitTag>.Compiled {
		protected Compiled(Renderable block, AttributeCollection attributeCollection) {
			super(block, null, attributeCollection);
		}

		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with("limit", new Data(context, this.getAttributeCollection()));
		}
	}
	
	public class Data extends AbstractGridTag<LimitTag>.Data {
		protected Data(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
		}
		
		public String getResultsPerPageParameterName() {
			return this.getGrid().getResultsPerPageParameterName();
		}
		
		public List<Integer> getSteps() {
			List<Integer> steps = new ArrayList<Integer>();
			String[] itemsStrings = this.getAttributeValue("steps").split(",");
			for (String step : itemsStrings) {
				steps.add(Integer.parseInt(step));
			}
			return steps;
		}
		
		public int getNumberOfResulsPerPage() {
			return this.getGrid().getFilter().getResultsPerPage();
		}
	}
	
}
