package com.github.chainmailstudios.astromine.registry.client;

import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingEnergyTieredBlockWithEntity;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.item.AstromineBlockItem;

public class AstromineClientMiscellaneous {
	public static void initialize() {
		AstromineBlockItem.APPENDERS.add((item, tooltip) -> {
			if (item.getBlock() instanceof WrenchableHorizontalFacingEnergyTieredBlockWithEntity) {
				tooltip.add(new TranslatableText("text.astromine.tooltip.speed", Fraction.DECIMAL_FORMAT.format(((WrenchableHorizontalFacingEnergyTieredBlockWithEntity) item.getBlock()).getMachineSpeed())).formatted(Formatting.GRAY));
				tooltip.add(new LiteralText(" "));
			}
		});
	}
}
