package com.customweb.jtwig.grid;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.customweb.grid.jtwig.GridAddon;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;

public class GridTest {

	@Test
	public void test() throws ParseException, CompileException, RenderException {
		
		JtwigConfiguration config = new JtwigConfiguration();
		config.parse().addons().withAddon(GridAddon.class);
		
		JtwigTemplate template = new JtwigTemplate(new ClasspathJtwigResource("classpath:/views/default.twig.html"), config);
		
		JtwigModelMap map = new JtwigModelMap();
		
		Object grid = new Object();
		map.add("test", grid);
		
		
        String result = template.output(map);
        
        System.out.println(result);
        
	}

}
