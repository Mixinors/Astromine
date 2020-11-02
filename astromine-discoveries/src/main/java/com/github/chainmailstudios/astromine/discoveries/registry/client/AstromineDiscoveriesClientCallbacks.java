/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.discoveries.registry.client;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.chainmailstudios.astromine.common.callback.SkyPropertiesCallback;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.MarsSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.MoonSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.SpaceSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.VulcanSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.common.entity.PrimitiveRocketEntity;
import com.github.chainmailstudios.astromine.discoveries.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesDimensions;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesEntityTypes;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientCallbacks;

import java.util.UUID;

public class AstromineDiscoveriesClientCallbacks extends AstromineClientCallbacks {
	public static void initialize() {
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.EARTH_SPACE_ID, new SpaceSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.MOON_ID, new MoonSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.MARS_ID, new MarsSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.VULCAN_ID, new VulcanSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.GLACIOS_ID, new SpaceSkyProperties()));

		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if (stack.getItem() instanceof SpaceSuitItem) {
				if (stack.getItem() == AstromineDiscoveriesItems.SPACE_SUIT_CHESTPLATE) {
					FluidComponent fluidComponent = FluidComponent.get(stack);

					fluidComponent.forEach((entry) -> {
						int slot = entry.getKey();

						FluidVolume volume = entry.getValue();

						tooltip.add(new LiteralText(volume.getAmount().toFractionalString() + " | " + new TranslatableText(volume.getFluid().getDefaultState().getBlockState().getBlock().getTranslationKey()).getString()).formatted(Formatting.GRAY));
					});
				}
			}
		}));

		ClientSidePacketRegistry.INSTANCE.register(PrimitiveRocketEntity.PRIMITIVE_ROCKET_SPAWN, (context, buffer) -> {
			double x = buffer.readDouble();
			double y = buffer.readDouble();
			double z = buffer.readDouble();
			UUID uuid = buffer.readUuid();
			int id = buffer.readInt();

			context.getTaskQueue().execute(() -> {
				PrimitiveRocketEntity rocketEntity = AstromineDiscoveriesEntityTypes.PRIMITIVE_ROCKET.create(MinecraftClient.getInstance().world);

				rocketEntity.setUuid(uuid);
				rocketEntity.setEntityId(id);
				rocketEntity.updatePosition(x, y, z);
				rocketEntity.updateTrackedPosition(x, y, z);

				MinecraftClient.getInstance().world.addEntity(id, rocketEntity);
			});
		});
	}
}
