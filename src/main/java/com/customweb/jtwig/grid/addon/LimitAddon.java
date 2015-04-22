package com.customweb.jtwig.grid.addon;

import org.jtwig.Environment;
import org.jtwig.loader.Loader;

import com.customweb.jtwig.grid.tag.LimitTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;

public class LimitAddon extends AttributeAddon<LimitTag> {

	public LimitAddon(Loader.Resource resource, Environment environment) {
		super(resource, environment);
	}

	@Override
	protected java.lang.String keyword() {
		return "grid:limit";
	}

	@Override
	public LimitTag instance() {
		return new LimitTag();
	}

}