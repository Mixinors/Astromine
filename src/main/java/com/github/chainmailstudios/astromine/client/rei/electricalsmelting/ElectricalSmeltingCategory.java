package com.github.chainmailstudios.astromine.client.rei.electricalsmelting;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.plugin.cooking.DefaultCookingCategory;
import me.shedaniel.rei.plugin.cooking.DefaultCookingDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.TranslatableText;

import java.text.DecimalFormat;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ElectricalSmeltingCategory extends DefaultCookingCategory {
	public ElectricalSmeltingCategory() {
		super(AstromineREIPlugin.ELECTRICAL_SMELTING, EntryStack.create(AstromineBlocks.ELECTRICAL_SMELTER), "category.astromine.electrical_smelting");
	}

	@Override
	public List<Widget> setupDisplay(DefaultCookingDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
		double cookingTime = display.getCookingTime();
		DecimalFormat df = new DecimalFormat("###.##");
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
		if (display instanceof ElectricalSmeltingDisplay)
			widgets.add(Widgets.createLabel(new Point(bounds.x + 5, bounds.y + 5),
					new TranslatableText("category.astromine.cooking.energy", FluidUtilities.rawFraction(((ElectricalSmeltingDisplay) display).getEnergyRequired()))).noShadow().leftAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5),
				new TranslatableText("category.astromine.cooking.time", df.format(cookingTime / 200d))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 8)).animationDurationTicks(cookingTime / 10));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 9)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9)).entries(display.getOutputEntries()).disableBackground().markOutput());
		return widgets;
	}
}
