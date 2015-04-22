package com.customweb.jtwig.grid.addon;

import org.jtwig.Environment;
import org.jtwig.loader.Loader;

import com.customweb.jtwig.grid.tag.SubmitTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;

public class SubmitAddon extends AttributeAddon<SubmitTag> {

	public SubmitAddon(Loader.Resource resource, Environment environment) {
		super(resource, environment);
	}

	@Override
	protected java.lang.String keyword() {
		return "grid:submit";
	}

	@Override
	public SubmitTag instance() {
		return new SubmitTag();
	}

}