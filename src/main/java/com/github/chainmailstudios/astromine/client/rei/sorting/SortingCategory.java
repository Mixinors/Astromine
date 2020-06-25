package com.github.chainmailstudios.astromine.client.rei.sorting;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.text.DecimalFormat;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SortingCategory implements RecipeCategory<SortingDisplay> {
	@Override
	public Identifier getIdentifier() {
		return AstromineREIPlugin.SORTING;
	}
	
	@Override
	public String getCategoryName() {
		return I18n.translate("category.astromine.sorting");
	}
	
	@Override
	public EntryStack getLogo() {
		return EntryStack.create(AstromineBlocks.SORTER);
	}
	
	@Override
	public List<Widget> setupDisplay(SortingDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getY() + 10);
		DecimalFormat df = new DecimalFormat("###.##");
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createLabel(new Point(bounds.x + 5, bounds.y + 5),
				new TranslatableText("category.astromine.sorting.energy", FluidUtilities.rawFraction(display.getEnergyRequired()))).noShadow().leftAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5),
				new TranslatableText("category.astromine.sorting.time", df.format(display.getTimeRequired() / 20d))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 8)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 9)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9)).entries(display.getOutputEntries()).disableBackground().markOutput());
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 49;
	}
}
