package com.customweb.jtwig.grid.model;

import com.customweb.jtwig.grid.addon.GridAddon;
import com.customweb.jtwig.lib.template.IResourceResolver;

public abstract class AbstractResourceResolver implements IResourceResolver {

	public final void register() {
		GridAddon.getResourceHandler().addResolver(this);
	}

}
