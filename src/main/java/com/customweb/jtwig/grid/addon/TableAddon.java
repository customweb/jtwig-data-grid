package com.customweb.jtwig.grid.addon;

import org.jtwig.Environment;
import org.jtwig.loader.Loader;

import com.customweb.jtwig.grid.tag.TableTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;

public class TableAddon extends AttributeAddon<TableTag> {

	public TableAddon(Loader.Resource resource, Environment environment) {
		super(resource, environment);
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