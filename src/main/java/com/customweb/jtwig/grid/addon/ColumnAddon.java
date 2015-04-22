package com.customweb.jtwig.grid.addon;

import org.jtwig.Environment;
import org.jtwig.loader.Loader;

import com.customweb.jtwig.grid.tag.ColumnTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;

public class ColumnAddon extends AttributeAddon<ColumnTag> {

	public ColumnAddon(Loader.Resource resource, Environment environment) {
		super(resource, environment);
	}

	@Override
	protected java.lang.String keyword() {
		return "grid:column";
	}

	@Override
	public ColumnTag instance() {
		return new ColumnTag();
	}

}