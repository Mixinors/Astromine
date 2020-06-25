package com.github.chainmailstudios.astromine.client.rei.generating;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;

import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class AbstractEnergyGeneratingCategory<T extends AbstractEnergyGeneratingDisplay> implements RecipeCategory<T> {
	@Override
	public List<Widget> setupDisplay(T recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		Rectangle innerBounds = new Rectangle(bounds.getCenterX() - 55, bounds.y, 110, bounds.height);
		widgets.add(Widgets.createRecipeBase(innerBounds));
		widgets.addAll(AstromineREIPlugin.createEnergyDisplay(new Rectangle(innerBounds.getMaxX() - 32, innerBounds.getCenterY() - 28, 12, 48),
				recipeDisplay.getEnergyGeneratedPerTick(), 5000));
		widgets.add(Widgets.createLabel(new Point(innerBounds.getX() + 5, innerBounds.getMaxY() - 12),
				new TranslatableText("category.astromine.generating.energy", FluidUtilities.rawFraction(recipeDisplay.getEnergyGeneratedPerTick())))
				.tooltipLine(I18n.translate("category.astromine.generating.energy", recipeDisplay.getEnergyGeneratedPerTick().toPrettyString()))
				.noShadow().leftAligned().color(0xFF404040, 0xFFBBBBBB));
		return widgets;
	}
}
