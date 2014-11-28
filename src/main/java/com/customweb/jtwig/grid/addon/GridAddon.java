package com.customweb.jtwig.grid.addon;

import com.customweb.jtwig.grid.tag.GridTag;
import com.customweb.jtwig.lib.attribute.AttributeAddon;
import com.customweb.jtwig.lib.path.PathHandler;
import com.customweb.jtwig.lib.template.ResourceHandler;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;


public class GridAddon extends AttributeAddon<GridTag> {
	
	private static ResourceHandler resourceHandler = new ResourceHandler();
	
	private static PathHandler pathHandler = new PathHandler();
	
	public static ResourceHandler getResourceHandler() {
		return resourceHandler;
	}
	
	public static PathHandler getPathHandler() {
		return pathHandler;
	}
	
	public static void addons(JtwigConfiguration config) {
		config.parse().addons().withAddon(GridAddon.class);
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