package com.github.chainmailstudios.astromine.foundations.client.rei.infusing;

import com.github.chainmailstudios.astromine.foundations.client.rei.AstromineFoundationsRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class InfusingCategory implements RecipeCategory<InfusingDisplay> {
	@Override
	public Identifier getIdentifier() {
		return AstromineFoundationsRoughlyEnoughItemsPlugin.INFUSING;
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("category.astromine.infusing");
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(AstromineFoundationsBlocks.ALTAR);
	}

	@Override
	public List<Widget> setupDisplay(InfusingDisplay display, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createCategoryBase(bounds));

		float radius = 50f;
		float degrees = 360f / display.getInputEntries().size();
		System.out.println(degrees);
		for (int i = 0; i < display.getInputEntries().size(); i++) {
			List<EntryStack> stacks = display.getInputEntries().get(i);
			int x = (int) (radius * 1.05f * MathHelper.cos(degrees * i * 0.0174532925F));
			int y = (int) (radius * MathHelper.sin(degrees * i * 0.0174532925F));
			widgets.add(Widgets.createSlot(new Point(bounds.x + 65 + x, bounds.y + 54 + y + 15)).entry(EntryStack.create(AstromineFoundationsBlocks.ITEM_DISPLAYER)).noFavoritesInteractable().noInteractable().disableHighlight().disableTooltips().disableBackground());
			widgets.add(Widgets.createSlot(new Point(bounds.x + 65 + x, bounds.y + 54 + y)).entries(stacks).disableBackground().markInput());
		}

		widgets.add(Widgets.createSlot(new Point(bounds.x + 65, bounds.y + 54 + 15)).entry(EntryStack.create(AstromineFoundationsBlocks.ALTAR)).noFavoritesInteractable().noInteractable().disableHighlight().disableTooltips().disableBackground());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 65, bounds.y + 54)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}

	public int getDisplayHeight() {
		return 140;
	}
}
