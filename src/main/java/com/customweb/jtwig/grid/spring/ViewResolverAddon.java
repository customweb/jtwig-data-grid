package com.customweb.jtwig.grid.spring;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.customweb.jtwig.grid.addon.GridAddon;
import com.lyncode.jtwig.mvc.JtwigViewResolver;

@Component
public final class ViewResolverAddon {
	@Autowired
	private JtwigViewResolver viewResolver;

	@PostConstruct
    public void register() {
		viewResolver.setCached(false);
		GridAddon.addons(viewResolver.configuration());
	}
}
