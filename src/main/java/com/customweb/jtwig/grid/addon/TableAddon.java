package com.customweb.jtwig.grid.addon;

import com.customweb.jtwig.grid.tag.TableTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class TableAddon extends AttributeAddon<TableTag> {

	public TableAddon(JtwigResource resource, ParserConfiguration configuration) {
		super(resource, configuration);
	}

	@Override
	protected java.lang.String keyword() {
		return "grid:table";
	}

	@Override
	public TableTag instance() {
		return new TableTag();
	}

}