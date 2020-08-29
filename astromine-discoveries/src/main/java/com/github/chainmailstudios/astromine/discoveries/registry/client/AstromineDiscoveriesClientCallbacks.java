package com.github.chainmailstudios.astromine.discoveries.registry.client;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.chainmailstudios.astromine.common.callback.SkyPropertiesCallback;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.MarsSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.MoonSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.SpaceSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.VulcanSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.common.entity.RocketEntity;
import com.github.chainmailstudios.astromine.discoveries.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesDimensions;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesEntityTypes;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientCallbacks;
import nerdhub.cardinal.components.api.component.ComponentProvider;

import java.util.UUID;

public class AstromineDiscoveriesClientCallbacks extends AstromineClientCallbacks {
	public static void initialize() {
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.EARTH_SPACE_ID, new SpaceSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.MOON_ID, new MoonSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.MARS_ID, new MarsSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.VULCAN_ID, new VulcanSkyProperties()));

		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if (stack.getItem() instanceof SpaceSuitItem) {
				if (stack.getItem() == AstromineDiscoveriesItems.SPACE_SUIT_CHESTPLATE) {
					FluidInventoryComponent fluidComponent = ComponentProvider.fromItemStack(stack).getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

					fluidComponent.getContents().forEach((key, value) -> {
						tooltip.add(new LiteralText(value.getFraction().toFractionalString() + " | " + new TranslatableText(value.getFluid().getDefaultState().getBlockState().getBlock().getTranslationKey()).getString()).formatted(Formatting.GRAY));
					});
				}
			}
		}));

		ClientSidePacketRegistry.INSTANCE.register(RocketEntity.ROCKET_SPAWN, (context, buffer) -> {
			double x = buffer.readDouble();
			double y = buffer.readDouble();
			double z = buffer.readDouble();
			UUID uuid = buffer.readUuid();
			int id = buffer.readInt();

			context.getTaskQueue().execute(() -> {
				RocketEntity rocketEntity = AstromineDiscoveriesEntityTypes.ROCKET.create(MinecraftClient.getInstance().world);

				rocketEntity.setUuid(uuid);
				rocketEntity.setEntityId(id);
				rocketEntity.updatePosition(x, y, z);
				rocketEntity.updateTrackedPosition(x, y, z);

				MinecraftClient.getInstance().world.addEntity(id, rocketEntity);
			});
		});
	}
}
