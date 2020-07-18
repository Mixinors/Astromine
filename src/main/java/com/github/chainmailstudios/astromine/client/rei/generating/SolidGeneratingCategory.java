package com.github.chainmailstudios.astromine.client.rei.generating;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;

import java.text.DecimalFormat;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SolidGeneratingCategory extends AbstractEnergyGeneratingCategory<SolidGeneratingDisplay> {
	@Override
	public Identifier getIdentifier() {
		return AstromineREIPlugin.SOLID_GENERATING;
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("category.astromine.solid_generating");
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(AstromineBlocks.SOLID_GENERATOR);
	}

	@Override
	public List<Widget> setupDisplay(SolidGeneratingDisplay recipeDisplay, Rectangle bounds) {
		DecimalFormat df = new DecimalFormat("###.##");
		List<Widget> widgets = super.setupDisplay(recipeDisplay, bounds);
		Rectangle innerBounds = new Rectangle(bounds.getCenterX() - 55, bounds.y, 110, bounds.height);
		widgets.add(Widgets.createSlot(new Point(innerBounds.getX() + 20, innerBounds.getY() + 26)).entries(recipeDisplay.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createLabel(new Point(innerBounds.x + 5, innerBounds.y + 5),
				new TranslatableText("category.astromine.cooking.time", df.format(recipeDisplay.getTime()))).noShadow().leftAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createArrow(new Point(innerBounds.getX() + 45, innerBounds.getY() + 26)));
		return widgets;
	}
}
