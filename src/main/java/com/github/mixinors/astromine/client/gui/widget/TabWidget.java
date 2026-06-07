/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import com.github.mixinors.astromine.client.gui.GuiRenderers;
import com.github.mixinors.astromine.client.gui.GuiSprites;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TabWidget extends Widget {
	private final List<TabPage> pages = new ArrayList<>();
	private int selected;
	
	public TabWidget(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public TabPage addTab(Supplier<ItemStack> icon, Supplier<Component> title) {
		var page = new TabPage(icon, title);
		pages.add(page);
		return page;
	}
	
	public TabPage selectedPage() {
		if (pages.isEmpty()) {
			return null;
		}
		
		if (selected >= pages.size()) {
			selected = 0;
		}
		
		return pages.get(selected);
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		if (hidden) {
			return;
		}
		
		GuiRenderers.panel(graphics, context.left() + x, context.top() + y + GuiSprites.TAB_HEIGHT, width, height - GuiSprites.TAB_HEIGHT);
		
		for (var tab = 0; tab < pages.size(); ++tab) {
			var selectedTab = tab == selected;
			var tabX = context.left() + x + tab * GuiSprites.TAB_WIDTH;
			var tabY = context.top() + y + (selectedTab ? 0 : 2);
			var texture = tabTexture(tab, pages.size(), selectedTab);
			var textureHeight = selectedTab ? 29 : 26;
			graphics.blit(texture, tabX, tabY, 0.0F, 0.0F, 25, textureHeight, 25, textureHeight);
			graphics.renderItem(pages.get(tab).icon().get(), tabX + 5, context.top() + y + 7);
		}
		
		var page = selectedPage();
		
		if (page != null) {
			var pageContext = context.offset(x, y);
			
			for (var child : page.children()) {
				child.render(graphics, pageContext);
			}
		}
	}
	
	@Override
	public void renderTooltip(GuiGraphics graphics, WidgetContext context) {
		for (var tab = 0; tab < pages.size(); ++tab) {
			if (context.mouseX() >= context.left() + x + tab * GuiSprites.TAB_WIDTH && context.mouseX() < context.left() + x + tab * GuiSprites.TAB_WIDTH + GuiSprites.TAB_WIDTH && context.mouseY() >= context.top() + y && context.mouseY() < context.top() + y + GuiSprites.TAB_HEIGHT) {
				graphics.renderComponentTooltip(context.font(), List.of(pages.get(tab).title().get()), context.mouseX(), context.mouseY());
				return;
			}
		}
		
		var page = selectedPage();
		
		if (page != null) {
			var pageContext = context.offset(x, y);
			
			for (var i = page.children().size() - 1; i >= 0; --i) {
				page.children().get(i).renderTooltip(graphics, pageContext);
			}
		}
	}
	
	@Override
	public boolean mouseClicked(WidgetContext context, double mouseX, double mouseY, int button) {
		for (var tab = 0; tab < pages.size(); ++tab) {
			if (mouseX >= context.left() + x + tab * GuiSprites.TAB_WIDTH && mouseX < context.left() + x + tab * GuiSprites.TAB_WIDTH + GuiSprites.TAB_WIDTH && mouseY >= context.top() + y && mouseY < context.top() + y + GuiSprites.TAB_HEIGHT) {
				selected = tab;
				return true;
			}
		}
		
		var page = selectedPage();
		
		if (page != null) {
			var pageContext = context.offset(x, y);
			
			for (var i = page.children().size() - 1; i >= 0; --i) {
				if (page.children().get(i).mouseClicked(pageContext, mouseX, mouseY, button)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private static ResourceLocation tabTexture(int tab, int tabCount, boolean selected) {
		if (selected) {
			if (tab == 0) {
				return GuiSprites.TAB_LEFT_ACTIVE;
			}
			
			return tab == tabCount - 1 ? GuiSprites.TAB_RIGHT_ACTIVE : GuiSprites.TAB_MIDDLE_ACTIVE;
		}
		
		if (tab == 0) {
			return GuiSprites.TAB_LEFT_INACTIVE;
		}
		
		return tab == tabCount - 1 ? GuiSprites.TAB_RIGHT_INACTIVE : GuiSprites.TAB_MIDDLE_INACTIVE;
	}
}
