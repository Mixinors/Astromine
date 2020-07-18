package com.github.chainmailstudios.astromine.client.rei.generating;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.google.common.collect.Lists;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class AbstractEnergyGeneratingCategory<T extends AbstractEnergyGeneratingDisplay> implements RecipeCategory<T> {
	@Override
	public List<Widget> setupDisplay(T recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		Rectangle innerBounds = new Rectangle(bounds.getCenterX() - 55, bounds.y, 110, bounds.height);
		widgets.add(Widgets.createRecipeBase(innerBounds));
		widgets.addAll(AstromineREIPlugin.createEnergyDisplay(new Rectangle(innerBounds.getMaxX() - 32, innerBounds.getCenterY() - 23, 12, 48),
				recipeDisplay.getEnergyGeneratedPerTick(), true, 5000));
		return widgets;
	}
}
