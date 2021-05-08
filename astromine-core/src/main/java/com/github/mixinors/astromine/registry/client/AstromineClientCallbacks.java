/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.client.render.sky.MarsSkyProperties;
import com.github.mixinors.astromine.client.render.sky.MoonSkyProperties;
import com.github.mixinors.astromine.client.render.sky.SpaceSkyProperties;
import com.github.mixinors.astromine.client.render.sky.VulcanSkyProperties;
import com.github.mixinors.astromine.common.callback.SkyPropertiesCallback;
import com.github.mixinors.astromine.common.entity.PrimitiveRocketEntity;
import com.github.mixinors.astromine.common.item.HolographicConnectorItem;
import com.github.mixinors.astromine.common.item.SpaceSuitItem;
import com.github.mixinors.astromine.common.network.type.EnergyNetworkType;
import com.github.mixinors.astromine.registry.AstromineDimensions;
import com.github.mixinors.astromine.registry.AstromineEntityTypes;
import com.github.mixinors.astromine.registry.AstromineItems;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.item.base.EnergyVolumeItem;
import com.github.mixinors.astromine.common.item.base.FluidVolumeItem;
import com.github.mixinors.astromine.common.utilities.NumberUtilities;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import java.util.UUID;

public class AstromineClientCallbacks {
	public static void initialize() {
		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof FluidVolumeItem) {
				FluidComponent fluidComponent = FluidComponent.get(stack);

				if (fluidComponent != null) {
					fluidComponent.forEachIndexed((slot, volume) -> {
						tooltip.add(new LiteralText(slot + " - " + NumberUtilities.shorten(volume.getAmount().doubleValue(), "") + "/" + NumberUtilities.shorten(volume.getSize().doubleValue(), "") + " " + new TranslatableText(String.format("block.%s.%s", volume.getFluidId().getNamespace(), volume.getFluidId().getPath())).getString()).formatted(Formatting.GRAY));
					});
				}
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof EnergyVolumeItem) {
				EnergyHandler handler = Energy.of(stack);
				tooltip.add(Math.min(tooltip.size(), 1), new LiteralText(NumberUtilities.shorten(handler.getEnergy(), "") + "/" + NumberUtilities.shorten(((EnergyVolumeItem) stack.getItem()).getMaxStoredPower(), "")).formatted(Formatting.GRAY));
			}
		});
		
		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if (stack.getItem() instanceof HolographicConnectorItem) {
				Pair<RegistryKey<World>, BlockPos> pair = ((HolographicConnectorItem) stack.getItem()).readBlock(stack);
				if (pair != null) {
					tooltip.add(Text.of(null));
					tooltip.add(new TranslatableText("text.astromine.selected.dimension.pos", pair.getLeft().getValue(), pair.getRight().getX(), pair.getRight().getY(), pair.getRight().getZ()).formatted(Formatting.GRAY));
				}
			}
		}));
		
		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof EnergyNetworkType.NodeSpeedProvider) {
				tooltip.add(new TranslatableText("text.astromine.tooltip.cable.speed", ((EnergyNetworkType.NodeSpeedProvider) ((BlockItem) stack.getItem()).getBlock()).getNodeSpeed()).formatted(Formatting.GRAY));
			}
		}));
		
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDimensions.MOON_ID, new MoonSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDimensions.MARS_ID, new MarsSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDimensions.VULCAN_ID, new VulcanSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDimensions.GLACIOS_ID, new SpaceSkyProperties()));
		
		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if (stack.getItem() instanceof SpaceSuitItem) {
				if (stack.getItem() == AstromineItems.SPACE_SUIT_CHESTPLATE) {
					FluidComponent fluidComponent = FluidComponent.get(stack);
					
					fluidComponent.forEachIndexed((slot, volume) -> {
						tooltip.add(new LiteralText(volume.getAmount().toString() + " | " + new TranslatableText(volume.getFluid().getDefaultState().getBlockState().getBlock().getTranslationKey()).getString()).formatted(Formatting.GRAY));
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
				PrimitiveRocketEntity rocketEntity = AstromineEntityTypes.PRIMITIVE_ROCKET.create(MinecraftClient.getInstance().world);
				
				rocketEntity.setUuid(uuid);
				rocketEntity.setEntityId(id);
				rocketEntity.updatePosition(x, y, z);
				rocketEntity.updateTrackedPosition(x, y, z);
				
				MinecraftClient.getInstance().world.addEntity(id, rocketEntity);
			});
		});
	}
}
