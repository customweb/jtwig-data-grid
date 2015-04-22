package com.customweb.jtwig.grid.spring;

import javax.annotation.PostConstruct;

import org.jtwig.cache.impl.NoCacheSystem;
import org.jtwig.mvc.JtwigViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.customweb.jtwig.grid.addon.GridAddon;

@Component
public final class ViewResolverAddon {
	@Autowired
	private JtwigViewResolver viewResolver;

	@PostConstruct
    public void register() {
		viewResolver.setCacheSystem(new NoCacheSystem());
		GridAddon.addons(viewResolver.getEnvironment().getConfiguration());
	}
}
