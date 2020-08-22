package com.github.chainmailstudios.astromine.transportations.common.registry.client;

import com.github.chainmailstudios.astromine.common.item.AstromineBlockItem;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientMiscellaneous;
import com.github.chainmailstudios.astromine.common.network.type.EnergyNetworkType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class AstromineTransportationsClientMiscellaneous extends AstromineClientMiscellaneous {
	public static void initialize() {
		AstromineBlockItem.APPENDERS.add((item, tooltip) -> {
			if (item.getBlock() instanceof EnergyNetworkType.EnergyNodeSpeedProvider) {
				tooltip.add(new TranslatableText("text.astromine.tooltip.cable.speed", ((EnergyNetworkType.EnergyNodeSpeedProvider) item.getBlock()).getNodeSpeed()).formatted(Formatting.GRAY));
				tooltip.add(new LiteralText(" "));
			}
		});
	}
}
