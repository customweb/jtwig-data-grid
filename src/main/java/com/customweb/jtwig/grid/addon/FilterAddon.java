package com.customweb.jtwig.grid.addon;

import com.customweb.jtwig.grid.tag.FilterTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class FilterAddon extends AttributeAddon<FilterTag> {

	public FilterAddon(JtwigResource resource, ParserConfiguration configuration) {
		super(resource, configuration);
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