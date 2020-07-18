package com.github.chainmailstudios.astromine.client.rei.electricsmelting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.text.TranslatableText;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.plugin.cooking.DefaultCookingCategory;
import me.shedaniel.rei.plugin.cooking.DefaultCookingDisplay;

import com.google.common.collect.Lists;
import java.text.DecimalFormat;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ElectricSmeltingCategory extends DefaultCookingCategory {
	public ElectricSmeltingCategory() {
		super(AstromineREIPlugin.ELECTRIC_SMELTING, EntryStack.create(AstromineBlocks.ELECTRIC_SMELTER), "category.astromine.electric_smelting");
	}

	@Override
	public List<Widget> setupDisplay(DefaultCookingDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 27);
		double cookingTime = display.getCookingTime();
		DecimalFormat df = new DecimalFormat("###.##");
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 19)));
		if (display instanceof ElectricSmeltingDisplay)
			widgets.addAll(AstromineREIPlugin.createEnergyDisplay(new Rectangle(bounds.getX() + 10, bounds.getCenterY() - 23, 12, 48),
					((ElectricSmeltingDisplay) display).getEnergyRequired(), false, (long) (cookingTime / 10 * 500)));
		widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5),
				new TranslatableText("category.astromine.cooking.time", df.format(cookingTime / 150.0))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 18)).animationDurationTicks(cookingTime / 3));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 19)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 19)).entries(display.getOutputEntries()).disableBackground().markOutput());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 66;
	}
}
