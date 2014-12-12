package com.customweb.jtwig.grid.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import com.customweb.grid.util.Property;
import com.customweb.grid.util.UrlEncodedQueryString;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.DynamicAttribute;
import com.customweb.jtwig.lib.attribute.model.VariableAttribute;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;
import com.customweb.jtwig.lib.attribute.model.definition.EmptyAttributeDefinition;
import com.customweb.jtwig.lib.attribute.model.definition.NamedAttributeDefinition;
import com.customweb.jtwig.lib.attribute.model.definition.VariableAttributeDefinition;
import com.customweb.jtwig.lib.model.ObjectExtractor;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.util.ObjectExtractor.ExtractException;

public class ColumnTag extends AbstractGridTag<ColumnTag> {

	public static final String RENDER_TYPE_VARIABLE_NAME = ColumnTag.class.getName() + ".renderType";

	public enum RenderType {
		HEADER, FILTER, CONTENT
	};

	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new NamedAttributeDefinition("title", false));
		attributeDefinitions.add(new NamedAttributeDefinition("fieldName", false));
		attributeDefinitions.add(new EmptyAttributeDefinition("sortable"));
		attributeDefinitions.add(new EmptyAttributeDefinition("filterable"));
		attributeDefinitions.add(new VariableAttributeDefinition("filterOptions", false));
		attributeDefinitions.add(new NamedAttributeDefinition("filterOperator", false));
		attributeDefinitions.add(new NamedAttributeDefinition("labelForTrue", false));
		attributeDefinitions.add(new NamedAttributeDefinition("labelForFalse", false));
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		try {
			JtwigResource resource = this.retrieveResource(context, "grid/column");
			JtwigResource headerResource = this.retrieveResource(context, "grid/column/header");
			JtwigResource filterResource = this.retrieveResource(context, "grid/column/filter");
			return new Compiled(context.parse(resource).compile(context), context.parse(headerResource).compile(context), context.parse(
					filterResource).compile(context), super.compile(context), this.getAttributeCollection());
		} catch (ParseException | ResourceException e) {
			throw new CompileException(e);
		}
	}

	private class Compiled extends AbstractGridTag<ColumnTag>.Compiled {
		private Renderable headerBlock;
		private Renderable filterBlock;

		protected Compiled(Renderable block, Renderable headerBlock, Renderable filterBlock, Renderable content,
				AttributeCollection attributeCollection) {
			super(block, content, attributeCollection);
			this.headerBlock = headerBlock;
			this.filterBlock = filterBlock;
		}

		public Renderable getHeaderBlock() {
			return headerBlock;
		}

		public Renderable getFilterBlock() {
			return filterBlock;
		}
		
		public boolean isInTableContext(RenderContext context) {
			return context.map(TableTag.TABLE_CONTEXT_VARIABLE_NAME).equals(Boolean.TRUE);
		}
		
		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with("column", new Data(this.renderContentAsString(context), context, this.getAttributeCollection()));
		}

		@Override
		public void render(RenderContext context) throws RenderException {
			if (!this.isInTableContext(context)) {
				throw new RuntimeException("The 'column' tag can only be used inside a valid 'table' tag.");
			}
			
			RenderType renderType = (RenderType) context.map(RENDER_TYPE_VARIABLE_NAME);
			switch (renderType) {
			case FILTER:
				this.renderFilter(context);
				break;
			case HEADER:
				this.renderHeader(context);
				break;
			default:
				super.render(context);
				break;
			}
		}

		public void renderHeader(RenderContext context) throws RenderException {
			this.getAttributeCollection().render(context);
			
			context = this.isolatedModel(context);
			context.with("columnHeader", new HeaderData(context, this.getAttributeCollection()));
			this.getHeaderBlock().render(context);
		}

		public void renderFilter(RenderContext context) throws RenderException {
			this.getAttributeCollection().render(context);
			
			context = this.isolatedModel(context);
			context.with("columnFilter", new FilterData(context, this.getAttributeCollection()));
			this.getFilterBlock().render(context);
		}
	}
	
	abstract public class AbstractData extends AbstractGridTag<ColumnTag>.Data {
		protected AbstractData(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
		}

		protected String getFieldName() {
			return this.getAttributeValue("fieldName", null);
		}
	}
	
	public class Data extends AbstractData {
		private String content;
		
		protected Data(String content, RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
			this.content = content;
		}
		
		public String getContent() throws ExtractException {
			if (this.content != null && !this.content.isEmpty()) {
				return this.content;
			} else {
				Object target = this.getContext().map(TableTag.ROW_MODEL_VARIABLE_NAME);
				if (this.getFieldName() != null) {
					return ObjectUtils.nullSafeToString(ObjectExtractor.extract(target, this.getFieldName()));
				} else {
					return "";
				}
			}
		}
		
		@Override
		public Collection<DynamicAttribute> getDynamicAttributes() {
			List<DynamicAttribute> attributes = new ArrayList<DynamicAttribute>();
			for (DynamicAttribute attribute : super.getDynamicAttributes()) {
				if (attribute.getKey().startsWith("body-")) {
					attributes.add(new DynamicAttribute(attribute.getKey().replace("body-", ""), attribute.getValue()));
				} else if (!attribute.getKey().startsWith("header-") && !attribute.getKey().startsWith("filter-")) {
					attributes.add(attribute);
				}
			}
			return attributes;
		}
	}

	public class HeaderData extends AbstractData {
		protected HeaderData(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
		}
		
		public String getTitle() {
			if (this.getAttributeCollection().hasAttribute("title")) {
				return this.getAttributeValue("title");
			} else if (this.getAttributeCollection().hasAttribute("fieldName")) {
				return this.getAttributeValue("fieldName");
			} else {
				return "";
			}
		}
		
		public boolean isSortable() {
			return this.getAttributeCollection().hasAttribute("sortable");
		}
		
		public String getOrderingClass() {
			String order = this.getGrid().getFilter().getOrderBy(this.getFieldName());
			if (order != null && order.equalsIgnoreCase("ASC")) {
				return "sorting-asc";
			} else if (order != null && order.equalsIgnoreCase("DESC")) {
				return "sorting-desc";
			} else {
				return "no-sorting";
			}
		}
		
		public String getSortingUrl() {
			String order = this.getGrid().getFilter().getOrderBy(this.getFieldName());
			if (order != null && order.equalsIgnoreCase("ASC")) {
				return this.getGrid().getSortingUrl(getFieldName(), false, true);
			} else if (order != null && order.equalsIgnoreCase("DESC")) {
				return this.getGrid().getSortingUrl(getFieldName(), false, false);
			} else {
				return this.getGrid().getSortingUrl(getFieldName(), true, false);
			}
		}
		
		@Override
		public Collection<DynamicAttribute> getDynamicAttributes() {
			List<DynamicAttribute> attributes = new ArrayList<DynamicAttribute>();
			for (DynamicAttribute attribute : super.getDynamicAttributes()) {
				if (attribute.getKey().startsWith("header-")) {
					attributes.add(new DynamicAttribute(attribute.getKey().replace("header-", ""), attribute.getValue()));
				} else if (!attribute.getKey().startsWith("body-") && !attribute.getKey().startsWith("filter-")) {
					attributes.add(attribute);
				}
			}
			return attributes;
		}
	}

	public class FilterData extends AbstractData {
		protected FilterData(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
		}
		
		public boolean isFilterable() {
			return this.getAttributeCollection().hasAttribute("filterable");
		}
		
		public String getType() {
			Class<?> columnType = Property.getPropertyDataType(this.getGrid().getDomainClass(), this.getFieldName());
			if (boolean.class.isAssignableFrom(columnType)) {
				return "select";
			} else if (this.getOptions() != null) {
				return "select";
			}
			else {
				return null;
			}
		}
		
		@SuppressWarnings("unchecked")
		public Map<String, String> getOptions() {
			Class<?> columnType = Property.getPropertyDataType(this.getGrid().getDomainClass(), this.getFieldName());
			if (boolean.class.isAssignableFrom(columnType)) {
				Map<String, String> values = new HashMap<String, String>();
				values.put("", "");
				values.put("true", getLabelForTrue());
				values.put("false", getLabelForFalse());
				return values;
			} else if (this.getAttributeCollection().hasAttribute("filterOptions")) {
				return (Map<String, String>) this.getAttributeCollection().getAttribute("filterOptions", VariableAttribute.class).getVariable();
			} else {
				return null;
			}
		}
		
		public String getValue() {
			String inputFieldName = this.getGrid().getFieldFilterParameterName(this.getFieldName());
			UrlEncodedQueryString query = UrlEncodedQueryString.parse(this.getGrid().getCurrentUrl().getQuery());
			if (query.contains(inputFieldName)) {
				return query.get(inputFieldName);
			} else {
				return "";
			}
		}
		
		public String getName() {
			return this.getGrid().getFieldFilterParameterName(this.getFieldName());
		}
		
		public String getOperator() {
			return this.getAttributeValue("filterOperator", null);
		}
		
		public String getOperatorName() {
			return this.getGrid().getFilterOperatorName(this.getFieldName());
		}
		
		private String getLabelForTrue() {
			return this.getAttributeValue("labelForTrue", "True");
		}
		
		private String getLabelForFalse() {
			return this.getAttributeValue("labelForFalse", "False");
		}
		
		@Override
		public Collection<DynamicAttribute> getDynamicAttributes() {
			List<DynamicAttribute> attributes = new ArrayList<DynamicAttribute>();
			for (DynamicAttribute attribute : super.getDynamicAttributes()) {
				if (attribute.getKey().startsWith("filter-")) {
					attributes.add(new DynamicAttribute(attribute.getKey().replace("filter-", ""), attribute.getValue()));
				} else if (!attribute.getKey().startsWith("body-") && !attribute.getKey().startsWith("header-")) {
					attributes.add(attribute);
				}
			}
			return attributes;
		}
	}

}
