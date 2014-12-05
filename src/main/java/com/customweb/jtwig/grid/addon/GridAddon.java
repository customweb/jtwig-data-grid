package com.customweb.jtwig.grid.addon;

import com.customweb.jtwig.grid.model.DefaultResourceResolver;
import com.customweb.jtwig.grid.tag.GridTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;
import com.customweb.jtwig.lib.template.ResourceHandler;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;


public class GridAddon extends AttributeAddon<GridTag> {
	
	private static ResourceHandler resourceHandler = new ResourceHandler().addResolver(new DefaultResourceResolver());
	
	public static ResourceHandler getResourceHandler() {
		return resourceHandler;
	}

	public static void addons(JtwigConfiguration config) {
		config.parse().addons().withAddon(GridAddon.class);
		config.parse().addons().withAddon(TableAddon.class);
		config.parse().addons().withAddon(ColumnAddon.class);
		config.parse().addons().withAddon(PagerAddon.class);
		config.parse().addons().withAddon(LimitAddon.class);
		config.parse().addons().withAddon(FilterAddon.class);
		config.parse().addons().withAddon(SubmitAddon.class);
	}

	public GridAddon(JtwigResource resource, ParserConfiguration configuration) {
		super(resource, configuration);
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