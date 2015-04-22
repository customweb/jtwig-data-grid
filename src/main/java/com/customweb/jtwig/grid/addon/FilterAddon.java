package com.customweb.jtwig.grid.addon;

import org.jtwig.Environment;
import org.jtwig.loader.Loader;

import com.customweb.jtwig.grid.tag.FilterTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;

public class FilterAddon extends AttributeAddon<FilterTag> {

	public FilterAddon(Loader.Resource resource, Environment environment) {
		super(resource, environment);
	}

	@Override
	protected java.lang.String keyword() {
		return "grid:filter";
	}

	@Override
	public FilterTag instance() {
		return new FilterTag();
	}

}