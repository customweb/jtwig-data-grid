package com.customweb.jtwig.grid.tag;

import java.util.ArrayList;
import java.util.List;

import com.customweb.jtwig.lib.attribute.model.AttributeCollection;
import com.customweb.jtwig.lib.attribute.model.definition.AttributeDefinitionCollection;
import com.customweb.jtwig.lib.attribute.model.definition.EmptyAttributeDefinition;
import com.customweb.jtwig.lib.attribute.model.definition.NamedAttributeDefinition;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

public class PagerTag extends AbstractGridTag<PagerTag> {

	@Override
	public AttributeDefinitionCollection getAttributeDefinitions() {
		AttributeDefinitionCollection attributeDefinitions = super.getAttributeDefinitions();
		attributeDefinitions.add(new NamedAttributeDefinition("maxPageItems", false));
		attributeDefinitions.add(new EmptyAttributeDefinition("showPreviousButton"));
		attributeDefinitions.add(new EmptyAttributeDefinition("showNextButton"));
		attributeDefinitions.add(new EmptyAttributeDefinition("showFirstButton"));
		attributeDefinitions.add(new EmptyAttributeDefinition("showLastButton"));
		return attributeDefinitions;
	}

	@Override
	public Renderable compile(CompileContext context) throws CompileException {
		this.getAttributeCollection().compile(context);
		try {
			JtwigResource resource = this.retrieveResource(context, "grid/pager");
			return new Compiled(context.parse(resource).compile(context), this.getAttributeCollection());
		} catch (ParseException | ResourceException e) {
			throw new CompileException(e);
		}
	}

	private class Compiled extends AbstractGridTag<PagerTag>.Compiled {
		protected Compiled(Renderable block, AttributeCollection attributeCollection) {
			super(block, null, attributeCollection);
		}
		
		public boolean isInGridContext(RenderContext context) {
			return context.map(GridTag.GRID_CONTEXT_VARIABLE_NAME).equals(Boolean.TRUE);
		}

		@Override
		public void prepareContext(RenderContext context) throws RenderException {
			context.with("pager", new Data(context, this.getAttributeCollection()));
		}
		
		@Override
		public void render(RenderContext context) throws RenderException {
			if (!this.isInGridContext(context)) {
				throw new RuntimeException("The 'pager' tag can only be used inside a valid 'grid' tag.");
			}
			
			super.render(context);
		}
	}
	
	public class Data extends AbstractGridTag<PagerTag>.Data {
		protected Data(RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
		}
		
		public boolean showFirstButton() {
			return this.getAttributeCollection().hasAttribute("showFirstButton");
		}
		
		public boolean showLastButton() {
			return this.getAttributeCollection().hasAttribute("showLastButton");
		}
		
		public boolean showPreviousButton() {
			return this.getAttributeCollection().hasAttribute("showPreviousButton");
		}
		
		public boolean showNextButton() {
			return this.getAttributeCollection().hasAttribute("showNextButton");
		}
		
		public boolean isOnFirstPage() {
			return this.getGrid().getCurrentPage() == 0;
		}
		
		public boolean isOnLastPage() {
			return getGrid().getCurrentPage() == this.getMaxPageNumber();
		}
		
		public String getFirstPageUrl() {
			return getGrid().getPageUrl(0);
		}
		
		public String getLastPageUrl() {
			return getGrid().getPageUrl(this.getMaxPageNumber());
		}
		
		public String getPreviousPageUrl() {
			return getGrid().getPageUrl(Math.max(getGrid().getCurrentPage() - 1, 0));
		}
		
		public String getNextPageUrl() {
			return getGrid().getPageUrl(Math.min(getGrid().getCurrentPage() + 1, this.getMaxPageNumber()));
		}
		
		public List<PageData> getPages() {
			List<PageData> pages = new ArrayList<PageData>();
			if (this.getMaxPageItems() >= getGrid().getNumberOfPages()) {
				for (int i = 0; i < getGrid().getNumberOfPages(); i++) {
					pages.add(new PageData(i, this.getContext(), this.getAttributeCollection()));
				}
			} else {
				int start = Math.max(getGrid().getCurrentPage() - this.getMaxPageItems()/2, 0);
				int end = getGrid().getCurrentPage() + this.getMaxPageItems()/2;
				
				if (end >= getGrid().getNumberOfPages()) {
					start = start - (end - getGrid().getNumberOfPages() + 1);
				}
				for (int i = start; i < start + this.getMaxPageItems(); i++) {
					pages.add(new PageData(i, this.getContext(), this.getAttributeCollection()));
				}
			}
			return pages;
		}
		
		private int getMaxPageItems() {
			return Integer.parseInt(this.getAttributeValue("maxPageItems", "9"));
		}
		
		private int getMaxPageNumber() {
			return getGrid().getNumberOfPages() - 1;
		}
	}
	
	public class PageData extends AbstractGridTag<PagerTag>.Data {
		private int pageNumber;
		
		protected PageData(int pageNumber, RenderContext context, AttributeCollection attributeCollection) {
			super(context, attributeCollection);
			this.pageNumber = pageNumber;
		}
		
		public int getPageNumber() {
			return this.pageNumber;
		}

		public boolean isCurrent() {
			return getGrid().getCurrentPage() == this.getPageNumber();
		}
		
		public String getUrl() {
			return getGrid().getPageUrl(this.getPageNumber());
		}
		
		public String getLabel() {
			return new Integer(this.getPageNumber() + 1).toString();
		}
	}
	
}
