package com.customweb.grid.jtwig.model;

import java.io.IOException;

import com.lyncode.jtwig.addons.AddonModel;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.render.RenderContext;

public class Grid extends AddonModel<Grid> {
	
	private final String gridVariableName;
	
	public Grid(String gridVariableName) {
		this.gridVariableName = gridVariableName;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		return new Compiled(super.compile(context));
	}
	
	public String getGridVariableName() {
		return this.gridVariableName;
	}

	private class Compiled implements Renderable {
		private final Renderable content;

		private Compiled(Renderable content) {
			this.content = content;
		}

		@Override
		public void render(RenderContext context) throws RenderException {
			
			// Prevent access on grid variables outside
			context = context.isolatedModel();
			context.with("other", "sample");
			
			try {
				context.write("<div>".getBytes());
				this.content.render(context);
				context.write("</div>".getBytes());
			} catch (IOException e) {
			}
			
			
		}
	}

}
