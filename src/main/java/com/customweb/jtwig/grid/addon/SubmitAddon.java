package com.customweb.jtwig.grid.addon;

import com.customweb.jtwig.grid.tag.SubmitTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class SubmitAddon extends AttributeAddon<SubmitTag> {

	public SubmitAddon(JtwigResource resource, ParserConfiguration configuration) {
		super(resource, configuration);
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