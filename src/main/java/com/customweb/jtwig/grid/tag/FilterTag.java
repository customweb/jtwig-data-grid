package com.customweb.jtwig.grid.tag;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;
import org.jtwig.render.RenderContext;

import com.customweb.grid.util.UrlEncodedQueryString;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;
import com.customweb.jtwig.lib.attribute.model.definition.EmptyAttributeDefinition;
import com.customweb.jtwig.lib.attribute.model.definition.NamedAttributeDefinition;

public class FilterTag extends AbstractGridTag<FilterTag> {

	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new NamedAttributeDefinition("fieldName", true));
		attributeDefinitions.add(new NamedAttributeDefinition("defaultOperator", false));
		attributeDefinitions.add(new EmptyAttributeDefinition("showOperator"));
		attributeDefinitions.getDynamicAttributeDefinition().addDisallowedKeys("name", "value");
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		this.getAttributeCollection().compile(context);
		try {
			Loader.Resource resource = this.retrieveResource(context, "grid/filter");
			return new Compiled(context.environment().parse(resource).compile(context), this.getAttributeCollection());
		} catch (ParseException | ResourceException e) {
			throw new CompileException(e);
		}
	}

	private class Compiled extends AbstractGridTag<FilterTag>.Compiled {
		protected Compiled(Renderable block, AttributeCollection attributeCollection) {
			super(block, null, attributeCollection);
		}
		
		public boolean isInGridContext(RenderContext context) {
			return context.map(GridTag.GRID_CONTEXT_VARIABLE_NAME).equals(Boolean.TRUE);
		}

		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with("columnFilter", new Data(context, this.getAttributeCollection()));
		}
		
		@Override
		public void render(RenderContext context) throws RenderException {
			if (!this.isInGridContext(context)) {
				throw new RuntimeException("The 'filter' tag can only be used inside a valid 'grid' tag.");
			}
			
			super.render(context);
		}
	}
	
	public class Data extends AbstractGridTag<FilterTag>.Data {
		protected Data(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
		}
		
		public String getParameterName() {
			return this.getGrid().getFieldFilterParameterName(this.getFieldName());
		}
		
		public String getValue() {
			String inputFieldName = this.getGrid().getFieldFilterParameterName(this.getFieldName());
			UrlEncodedQueryString query = UrlEncodedQueryString.parse(this.getGrid().getCurrentUrl().getQuery());
			if (query.contains(inputFieldName)) {
				return query.get(inputFieldName);
			}
			return "";
		}
		
		public boolean hasCustomOperator() {
			return this.isShowOperator() || !this.getDefaultOperator().isEmpty();
		}
		
		public boolean isShowOperator() {
			return this.getAttributeCollection().hasAttribute("showOperator");
		}
		
		public String getOperatorName() {
			return this.getGrid().getFilterOperatorName(this.getFieldName());
		}
		
		public String getOperatorValue() {
			String operatorName = this.getGrid().getFilterOperatorName(this.getFieldName());		
			UrlEncodedQueryString query = UrlEncodedQueryString.parse(this.getGrid().getCurrentUrl().getQuery());
			if (query.contains(operatorName)) {
				return query.get(operatorName);
			}
			return getDefaultOperator();
		}
		
		private String getDefaultOperator() {
			return this.getAttributeValue("defaultOperator", "");
		}
		
		private String getFieldName() {
			return this.getAttributeValue("fieldName", "");
		}
	}
	
}
