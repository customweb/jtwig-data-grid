package com.customweb.jtwig.grid.tag;

import org.jtwig.content.api.Renderable;
import org.jtwig.render.RenderContext;

import com.customweb.grid.Grid;
import com.customweb.jtwig.lib.attribute.model.AbstractAttributeTag;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.DynamicAttribute;

abstract public class AbstractGridTag<T extends AbstractGridTag<T>> extends AbstractAttributeTag<T> {
	public static final String DEFAULT_MODEL_ATTRIBUTE_NAME = "gridModel";

	public static final String MODEL_ATTRIBUTE = "modelAttribute";

	public static final String MODEL_ATTRIBUTE_VARIABLE_NAME = GridTag.class.getName() + "." + MODEL_ATTRIBUTE;

	abstract protected class Compiled extends AbstractAttributeTag<GridTag>.Compiled {
		protected Compiled(Renderable block, Renderable content, AttributeCollection attributeCollection) {
			super(block, content, attributeCollection);
		}
	}

	abstract public class Data extends AbstractAttributeTag<GridTag>.Data {
		private Grid<?> grid;

		protected Data(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
		}

		protected final Grid<?> getGrid() {
			if (this.grid == null) {
				this.grid = (Grid<?>) this.getContext().map((String) this.getContext().map(MODEL_ATTRIBUTE_VARIABLE_NAME));
			}
			return this.grid;
		}

		protected final void extendDynamicAttribute(String key, String value) {
			if (this.getAttributeCollection().hasAttribute(key)) {
				value = this.getAttributeValue(key) + " " + value;
			}
			this.getAttributeCollection().addAttribute(new DynamicAttribute(key, value));
		}
	}
}
