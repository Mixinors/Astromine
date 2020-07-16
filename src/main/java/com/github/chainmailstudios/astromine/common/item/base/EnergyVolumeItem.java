package com.github.chainmailstudios.astromine.common.item.base;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class EnergyVolumeItem extends BaseVolumeItem {
	public EnergyVolumeItem(Settings settings) {
		super(settings);
	}

	public static EnergyVolumeItem of(Settings settings, Fraction size) {
		EnergyVolumeItem item = new EnergyVolumeItem(settings);
		item.size = size;
		return item;
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		EnergyInventoryComponent fluidComponent = ComponentProvider.fromItemStack(stack).getComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);

		fluidComponent.getContents().forEach((key, value) -> {
			tooltip.add(new LiteralText("- " + value.getFraction().toDecimalString() + " | " + new TranslatableText("text.astromine.energy").getString()));
		});
	}
}
