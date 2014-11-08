package com.customweb.jtwig.grid;

import org.parboiled.Rule;

import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.addons.AddonModel;
import com.lyncode.jtwig.content.model.compilable.Block;
import com.lyncode.jtwig.expressions.model.Constant;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public class GridAddon extends Addon {
	public GridAddon(JtwigResource resource, ParserConfiguration configuration) {
		super(resource, configuration);
	}

	@Override
	public Rule startRule() {
		return Optional(
				Sequence(
						Sequence(
								basicParser().identifier(),
								expressionParser().push(new Constant<>(match())),
								basicParser().spacing()
				        ),
						basicParser().spacing(),
						push(new Grid(this.popAsString()))
				)
			);
	}

	@Override
	public AddonModel<Grid> instance() {
		throw new UnsupportedOperationException();
	}

	protected String keyword() {
		return "grid";
	}

	@Override
	public String beginKeyword() {
		return keyword();
	}

	@Override
	public String endKeyword() {
		return "end" + keyword();
	}
	
	@SuppressWarnings("rawtypes")
	String popAsString() {
		return (String)((Constant)expressionParser().pop(0)).getValue();
	}

}