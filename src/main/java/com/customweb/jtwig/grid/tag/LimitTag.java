package com.customweb.jtwig.grid.tag;

import java.util.ArrayList;
import java.util.List;

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
import com.customweb.jtwig.lib.attribute.model.definition.NamedAttributeDefinition;

public class LimitTag extends AbstractGridTag<LimitTag> {

	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new NamedAttributeDefinition("steps", true));
		attributeDefinitions.getDynamicAttributeDefinition().addDisallowedKeys("name");
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		this.getAttributeCollection().compile(context);
		try {
			Loader.Resource resource = this.retrieveResource(context, "grid/limit");
			return new Compiled(context.environment().parse(resource).compile(context), this.getAttributeCollection());
		} catch (ParseException | ResourceException e) {
			throw new CompileException(e);
		}
	}

	private class Compiled extends AbstractGridTag<LimitTag>.Compiled {
		protected Compiled(Renderable block, AttributeCollection attributeCollection) {
			super(block, null, attributeCollection);
		}
		
		public boolean isInGridContext(RenderContext context) {
			return context.map(GridTag.GRID_CONTEXT_VARIABLE_NAME).equals(Boolean.TRUE);
		}

		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with("limit", new Data(context, this.getAttributeCollection()));
		}
		
		@Override
		public void render(RenderContext context) throws RenderException {
			if (!this.isInGridContext(context)) {
				throw new RuntimeException("The 'limit' tag can only be used inside a valid 'grid' tag.");
			}
			
			super.render(context);
		}
	}
	
	public class Data extends AbstractGridTag<LimitTag>.Data {
		protected Data(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
			
			this.extendDynamicAttribute("class", "ajax-event");
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
