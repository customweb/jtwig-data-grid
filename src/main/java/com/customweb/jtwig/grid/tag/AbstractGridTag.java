package com.customweb.jtwig.grid.tag;

import com.customweb.grid.Grid;
import com.customweb.jtwig.lib.attribute.model.AbstractAttributeTag;
import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.render.RenderContext;

abstract public class AbstractGridTag<T extends AbstractGridTag<T>> extends AbstractAttributeTag<T> {
	public static final String DEFAULT_MODEL_ATTRIBUTE_NAME = "gridModel";

	public static final String MODEL_ATTRIBUTE = "modelAttribute";

	public static final String MODEL_ATTRIBUTE_VARIABLE_NAME = GridTag.class.getName() + "." + MODEL_ATTRIBUTE;

	// public static final String NESTED_PATH_VARIABLE_NAME =
	// AbstractDataBoundFormElementTag.NESTED_PATH_VARIABLE_NAME;

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
	}
}
