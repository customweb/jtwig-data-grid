package com.customweb.jtwig.grid.tag;

import com.customweb.grid.util.UrlEncodedQueryString;
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
			JtwigResource resource = this.retrieveResource(context, "grid/filter");
			return new Compiled(context.parse(resource).compile(context), this.getAttributeCollection());
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
