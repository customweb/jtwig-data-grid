package com.customweb.jtwig.grid.addon;

import com.customweb.jtwig.grid.tag.LimitTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class LimitAddon extends AttributeAddon<LimitTag> {

	public LimitAddon(JtwigResource resource, ParserConfiguration configuration) {
		super(resource, configuration);
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