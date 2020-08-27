package com.github.chainmailstudios.astromine.technologies.registry.client;

import com.github.chainmailstudios.astromine.common.callback.TooltipCallback;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientCallbacks;
import com.github.chainmailstudios.astromine.technologies.common.item.HolographicConnectorItem;
import com.github.chainmailstudios.astromine.technologies.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesItems;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class AstromineTechnologiesClientCallbacks extends AstromineClientCallbacks {
	public static void initialize() {
		TooltipCallback.EVENT.register(((stack, world, tooltip, context) -> {
			if (stack.getItem() instanceof HolographicConnectorItem) {
				Pair<RegistryKey<World>, BlockPos> pair = ((HolographicConnectorItem) stack.getItem()).readBlock(stack);
				if (pair != null) {
					tooltip.add(Text.of(null));
					tooltip.add(new TranslatableText("text.astromine.selected.dimension.pos", pair.getLeft().getValue(), pair.getRight().getX(), pair.getRight().getY(), pair.getRight().getZ()).formatted(Formatting.GRAY));
				}
			}
		}));

		TooltipCallback.EVENT.register(((stack, world, tooltip, context) -> {
			if (stack.getItem() instanceof SpaceSuitItem) {
				if (stack.getItem() == AstromineTechnologiesItems.SPACE_SUIT_CHESTPLATE) {
					FluidInventoryComponent fluidComponent = ComponentProvider.fromItemStack(stack).getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

					fluidComponent.getContents().forEach((key, value) -> {
						tooltip.add(new LiteralText(value.getFraction().toFractionalString() + " | " + new TranslatableText(value.getFluid().getDefaultState().getBlockState().getBlock().getTranslationKey()).getString()).formatted(Formatting.GRAY));
					});
				}
			}
		}));
	}
}
