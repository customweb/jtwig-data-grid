package com.customweb.jtwig.grid.addon;

import com.customweb.jtwig.grid.tag.PagerTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class PagerAddon extends AttributeAddon<PagerTag> {

	public PagerAddon(JtwigResource resource, ParserConfiguration configuration) {
		super(resource, configuration);
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