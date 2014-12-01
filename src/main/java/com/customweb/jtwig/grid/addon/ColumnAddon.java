package com.customweb.jtwig.grid.addon;

import com.customweb.jtwig.grid.tag.ColumnTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class ColumnAddon extends AttributeAddon<ColumnTag> {

	public ColumnAddon(JtwigResource resource, ParserConfiguration configuration) {
		super(resource, configuration);
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