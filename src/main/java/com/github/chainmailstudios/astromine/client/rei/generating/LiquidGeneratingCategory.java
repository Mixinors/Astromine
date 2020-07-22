package com.github.chainmailstudios.astromine.client.rei.generating;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class LiquidGeneratingCategory extends AbstractEnergyGeneratingCategory<LiquidGeneratingDisplay> {
	@Override
	public Identifier getIdentifier() {
		return AstromineREIPlugin.LIQUID_GENERATING;
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("category.astromine.liquid_generating");
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(AstromineBlocks.ADVANCED_LIQUID_GENERATOR);
	}

	@Override
	public List<Widget> setupDisplay(LiquidGeneratingDisplay recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = super.setupDisplay(recipeDisplay, bounds);
		Rectangle innerBounds = new Rectangle(bounds.getCenterX() - 55, bounds.y, 110, bounds.height);
		widgets.addAll(AstromineREIPlugin.createFluidDisplay(new Rectangle(innerBounds.getX() + 24, innerBounds.getCenterY() - 23, 12, 48),
				EntryStack.create(recipeDisplay.getFluid()), recipeDisplay.getAmount(), false, 5000));
		widgets.add(Widgets.createArrow(new Point(innerBounds.getX() + 45, innerBounds.getY() + 26)));
		return widgets;
	}
}
