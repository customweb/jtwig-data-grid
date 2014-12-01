package com.customweb.jtwig.grid.addon;

import com.customweb.jtwig.grid.tag.SearchTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class SearchAddon extends AttributeAddon<SearchTag> {

	public SearchAddon(JtwigResource resource, ParserConfiguration configuration) {
		super(resource, configuration);
	}

	@Override
	protected java.lang.String keyword() {
		return "grid:search";
	}

	@Override
	public SearchTag instance() {
		return new SearchTag();
	}

}