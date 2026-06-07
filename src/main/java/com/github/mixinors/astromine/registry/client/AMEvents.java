/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.render.effects.SpaceDimensionEffects;
import com.github.mixinors.astromine.common.block.network.EnergyCableBlock;
import com.github.mixinors.astromine.common.item.utility.HolographicConnectorItem;
import com.github.mixinors.astromine.common.transfer.storage.EnergyStorageItem;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;
import com.github.mixinors.astromine.registry.common.AMFluids;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class AMEvents {
	public static void init(IEventBus modBus) {
		modBus.addListener(AMEvents::registerDimensionEffects);
		modBus.addListener(AMEvents::registerClientExtensions);
		NeoForge.EVENT_BUS.addListener(AMEvents::updateTickDelta);
		NeoForge.EVENT_BUS.addListener(AMEvents::appendTooltips);
	}
	
	private static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
		event.register(AMCommon.id("space"), new SpaceDimensionEffects());
	}
	
	private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		for (var fluid : AMFluids.entries()) {
			var tintColor = fluid.getSource().getTintColor();
			
			event.registerFluidType(new IClientFluidTypeExtensions() {
				private static final ResourceLocation STILL_TEXTURE = ResourceLocation.withDefaultNamespace("block/water_still");
				private static final ResourceLocation FLOWING_TEXTURE = ResourceLocation.withDefaultNamespace("block/water_flow");
				
				@Override
				public ResourceLocation getStillTexture() {
					return STILL_TEXTURE;
				}
				
				@Override
				public ResourceLocation getFlowingTexture() {
					return FLOWING_TEXTURE;
				}
				
				@Override
				public int getTintColor() {
					return tintColor;
				}
			}, fluid.getType());
		}
	}
	
	private static void updateTickDelta(RenderGuiEvent.Post event) {
		AMValues.TICK_DELTA = event.getPartialTick().getGameTimeDeltaPartialTick(false);
	}
	
	private static void appendTooltips(ItemTooltipEvent event) {
		var stack = event.getItemStack();
		var tooltips = event.getToolTip();
		var item = stack.getItem();
		var id = BuiltInRegistries.ITEM.getKey(item);
		
		if (id.getNamespace().equals(AMCommon.MOD_ID)) {
			var energyStorage = findEnergyStorage(stack);
			
			if (energyStorage != null) {
				tooltips.add(Component.translatable(
						"text.astromine.tooltip.compound_energy_value",
						LongEnergyStorage.getAmount(energyStorage),
						LongEnergyStorage.getCapacity(energyStorage)
				).withStyle(ChatFormatting.GOLD));
			}
		}
		
		if (item instanceof HolographicConnectorItem holographicConnectorItem) {
			var selection = holographicConnectorItem.fromStack(stack);
			
			if (selection != null) {
				var key = selection.registryKey().location();
				var pos = selection.blockPos();
				
				tooltips.add(Component.empty());
				tooltips.add(Component.translatable("text.astromine.selected.dimension.blockPos", key, pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.GRAY));
			}
		}
		
		if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof EnergyCableBlock cableBlock) {
			tooltips.add(Component.translatable("text.astromine.tooltip.cable.speed", cableBlock.getNetworkType().getTransferRate()).withStyle(style -> style.withColor(0xFFCC33)));
		}
	}
	
	private static IEnergyStorage findEnergyStorage(ItemStack stack) {
		var capabilityStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
		
		if (capabilityStorage != null) {
			return capabilityStorage;
		}
		
		if (stack.getItem() instanceof EnergyStorageItem energyStorageItem) {
			return energyStorageItem.createEnergyStorage(stack);
		}
		
		return null;
	}
}
