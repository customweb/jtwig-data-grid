package com.customweb.jtwig.grid.addon;

import org.jtwig.Environment;
import org.jtwig.loader.Loader;

import com.customweb.jtwig.grid.tag.PagerTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;

public class PagerAddon extends AttributeAddon<PagerTag> {

	public PagerAddon(Loader.Resource resource, Environment environment) {
		super(resource, environment);
	}

	@Override
	protected java.lang.String keyword() {
		return "grid:pager";
	}

	@Override
	public PagerTag instance() {
		return new PagerTag();
	}

}