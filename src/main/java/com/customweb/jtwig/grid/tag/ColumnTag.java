package com.customweb.jtwig.grid.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.ObjectUtils;

import com.customweb.grid.util.Property;
import com.customweb.grid.util.UrlEncodedQueryString;
import com.customweb.jtwig.grid.addon.GridAddon;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.VariableAttribute;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;
import com.customweb.jtwig.lib.attribute.model.definition.EmptyAttributeDefinition;
import com.customweb.jtwig.lib.attribute.model.definition.NamedAttributeDefinition;
import com.customweb.jtwig.lib.attribute.model.definition.VariableAttributeDefinition;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

public class ColumnTag extends AbstractGridTag<ColumnTag> {

	public static final String RENDER_TYPE_VARIABLE_NAME = ColumnTag.class.getName() + ".renderType";

	public enum RenderType {
		HEADER, FILTER, CONTENT
	};

	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new NamedAttributeDefinition("title", false));
		attributeDefinitions.add(new NamedAttributeDefinition("fieldname", false));
		attributeDefinitions.add(new EmptyAttributeDefinition("sortable"));
		attributeDefinitions.add(new EmptyAttributeDefinition("filterable"));
		attributeDefinitions.add(new VariableAttributeDefinition("filteroptions", false));
		attributeDefinitions.add(new NamedAttributeDefinition("filteroperator", false));
		attributeDefinitions.add(new NamedAttributeDefinition("labelfortrue", false));
		attributeDefinitions.add(new NamedAttributeDefinition("labelforfalse", false));
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		try {
			JtwigResource resource = GridAddon.getResourceHandler().resolve("column");
			JtwigResource headerResource = GridAddon.getResourceHandler().resolve("column/header");
			JtwigResource filterResource = GridAddon.getResourceHandler().resolve("column/filter");
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
		
		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with("column", new Data(this.renderContentAsString(context), context, this.getAttributeCollection()));
		}

		@Override
		public void render(RenderContext context) throws RenderException {
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
			context = context.isolatedModel();
			context.with("columnHeader", new HeaderData(context, this.getAttributeCollection()));
			this.getHeaderBlock().render(context);
		}

		public void renderFilter(RenderContext context) throws RenderException {
			context = context.isolatedModel();
			context.with("columnFilter", new FilterData(context, this.getAttributeCollection()));
			this.getFilterBlock().render(context);
		}
	}
	
	public class Data extends AbstractGridTag<ColumnTag>.Data {
		private String content;
		
		protected Data(String content, RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
			this.content = content;
		}
		
		public String getContent() {
			if (this.content != null && !this.content.isEmpty()) {
				return this.content;
			} else {
				Object target = this.getContext().map(TableTag.ROW_MODEL_VARIABLE_NAME);
				if (this.getFieldName() != null) {
					BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);
					return ObjectUtils.nullSafeToString(beanWrapper.getPropertyValue(this.getFieldName()));
				} else {
					return "";
				}
			}
		}
		
		private String getFieldName() {
			if (this.getAttributeCollection().hasAttribute("fieldname")) {
				return this.getAttributeValue("fieldname");
			} else {
				return null;
			}
		}
	}

	public class HeaderData extends AbstractGridTag<ColumnTag>.Data {
		protected HeaderData(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
		}
		
		public String getTitle() {
			if (this.getAttributeCollection().hasAttribute("title")) {
				return this.getAttributeValue("title");
			} else if (this.getAttributeCollection().hasAttribute("fieldname")) {
				return this.getAttributeValue("fieldname");
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
				return "ascending-sorting";
			} else if (order != null && order.equalsIgnoreCase("DESC")) {
				return "descending-sorting";
			}
			return "no-sorting";
		}
		
		public String getSortingUrlAsc() {
			return this.getGrid().getSortingUrl(getFieldName(), true, false);
		}
		
		public String getSortingUrlDesc() {
			return this.getGrid().getSortingUrl(getFieldName(), false, true);
		}
		
		public String getSortingUrlReset() {
			return this.getGrid().getSortingUrl(getFieldName(), false, false);
		}
		
		private String getFieldName() {
			if (this.getAttributeCollection().hasAttribute("fieldname")) {
				return this.getAttributeValue("fieldname");
			} else {
				return "";
			}
		}
	}

	public class FilterData extends AbstractGridTag<ColumnTag>.Data {
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
			} else if (this.getAttributeCollection().hasAttribute("filteroptions")) {
				return (Map<String, String>) this.getAttributeCollection().getAttribute("filteroptions", VariableAttribute.class).getVariable(this.getContext());
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
			if (this.getAttributeCollection().hasAttribute("filteroperator")) {
				return this.getAttributeValue("filteroperator");
			} else {
				return null;
			}
		}
		
		public String getOperatorName() {
			return this.getGrid().getFilterOperatorName(this.getFieldName());
		}
		
		private String getFieldName() {
			if (this.getAttributeCollection().hasAttribute("fieldname")) {
				return this.getAttributeValue("fieldname");
			} else {
				return "";
			}
		}
		
		private String getLabelForTrue() {
			if (this.getAttributeCollection().hasAttribute("labelfortrue")) {
				return this.getAttributeValue("labelfortrue");
			} else {
				return "True";
			}
		}
		
		private String getLabelForFalse() {
			if (this.getAttributeCollection().hasAttribute("labelforfalse")) {
				return this.getAttributeValue("labelforfalse");
			} else {
				return "False";
			}
		}
	}

}
