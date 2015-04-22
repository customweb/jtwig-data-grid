package com.customweb.jtwig.grid.addon;

import org.jtwig.Environment;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.loader.Loader;

import com.customweb.jtwig.grid.tag.GridTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;


public class GridAddon extends AttributeAddon<GridTag> {
	
	public static void addons(JtwigConfiguration configuration) {
		configuration.getAddonParserList().withAddon(GridAddon.class);
		configuration.getAddonParserList().withAddon(TableAddon.class);
		configuration.getAddonParserList().withAddon(ColumnAddon.class);
		configuration.getAddonParserList().withAddon(PagerAddon.class);
		configuration.getAddonParserList().withAddon(LimitAddon.class);
		configuration.getAddonParserList().withAddon(FilterAddon.class);
		configuration.getAddonParserList().withAddon(SubmitAddon.class);
	}

	public GridAddon(Loader.Resource resource, Environment environment) {
		super(resource, environment);
	}

	@Override
	protected java.lang.String keyword() {
		return "grid:grid";
	}

	@Override
	public GridTag instance() {
		return new GridTag();
	}

}