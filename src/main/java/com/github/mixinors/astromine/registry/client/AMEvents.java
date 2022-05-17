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
import com.github.mixinors.astromine.client.render.sky.SpaceDimensionEffects;
import com.github.mixinors.astromine.common.block.network.EnergyCableBlock;
import com.github.mixinors.astromine.common.component.entity.OxygenComponent;
import com.github.mixinors.astromine.common.event.DimensionEffectsEvents;
import com.github.mixinors.astromine.common.item.armor.SpaceSuitArmorItem;
import com.github.mixinors.astromine.common.item.utility.HolographicConnectorItem;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidItemStorage;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtils;
import dev.vini2003.hammer.core.api.client.util.InstanceUtils;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.util.FluidTextUtils;
import dev.vini2003.hammer.core.api.common.util.TextUtils;
import dev.vini2003.hammer.gui.api.client.event.InGameHudEvents;
import dev.vini2003.hammer.gui.api.common.widget.bar.HudBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.ImageBarWidget;
import dev.vini2003.hammer.gui.energy.api.common.util.EnergyTextUtils;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;

public class AMEvents {
	public static void init() {
		var spaceSuitEnergyBar = new ImageBarWidget(
				() -> {
					var client = InstanceUtils.getClient();
					
					if (client != null && client.player != null) {
						var chestStack = client.player.getEquippedStack(EquipmentSlot.CHEST);
						
						if (chestStack.getItem() instanceof SpaceSuitArmorItem.Chestplate) {
							var energyStorage = EnergyStorage.ITEM.find(chestStack, ContainerItemContext.withInitial(chestStack));
							
							if (energyStorage != null) {
								return (float) energyStorage.getCapacity();
							}
						}
					}
					
					return 100.0F;
				},
				() -> {
					var client = InstanceUtils.getClient();
					
					if (client != null && client.player != null) {
						var chestStack = client.player.getEquippedStack(EquipmentSlot.CHEST);
						
						if (chestStack.getItem() instanceof SpaceSuitArmorItem.Chestplate) {
							var energyStorage = EnergyStorage.ITEM.find(chestStack, ContainerItemContext.withInitial(chestStack));
							
							if (energyStorage != null) {
								return (float) energyStorage.getAmount();
							}
						}
					}
					
					return 0.0F;
				}
		);
		
		spaceSuitEnergyBar.setVertical(true);
		
		spaceSuitEnergyBar.setSmooth(false);
		
		spaceSuitEnergyBar.setForegroundTexture(new ImageTexture(AMCommon.id("textures/widget/vertical_energy_bar_foreground.png")));
		spaceSuitEnergyBar.setBackgroundTexture(new ImageTexture(AMCommon.id("textures/widget/vertical_energy_bar_background.png")));
		
		var spaceSuitOxygenBar = new ImageBarWidget(
				() -> {
					var client = InstanceUtils.getClient();
					
					if (client != null && client.player != null) {
						var chestStack = client.player.getEquippedStack(EquipmentSlot.CHEST);
						
						if (chestStack.getItem() instanceof SpaceSuitArmorItem.Chestplate) {
							var fluidStorages = FluidStorage.ITEM.find(chestStack, ContainerItemContext.withInitial(chestStack));
							
							if (fluidStorages instanceof SimpleFluidItemStorage fluidStorage) {
								return (float) fluidStorage.getCapacity();
							}
						}
					}
					
					return 100.0F;
				},
				() -> {
					var client = InstanceUtils.getClient();
					
					if (client != null && client.player != null) {
						var chestStack = client.player.getEquippedStack(EquipmentSlot.CHEST);
						
						if (chestStack.getItem() instanceof SpaceSuitArmorItem.Chestplate) {
							var fluidStorages = FluidStorage.ITEM.find(chestStack, ContainerItemContext.withInitial(chestStack));
							
							if (fluidStorages instanceof SimpleFluidItemStorage fluidStorage) {
								return (float) fluidStorage.getAmount();
							}
						}
					}
					
					return 0.0F;
				}
		);
		
		spaceSuitOxygenBar.setVertical(true);
		
		spaceSuitOxygenBar.setSmooth(false);
		
		spaceSuitOxygenBar.setForegroundTexture(new ImageTexture(AMCommon.id("textures/widget/vertical_oxygen_bar_foreground.png")));
		spaceSuitOxygenBar.setBackgroundTexture(new ImageTexture(AMCommon.id("textures/widget/vertical_oxygen_bar_background.png")));
		
		
		InGameHudEvents.RENDER.register(((matrices, provider, hud, collection) -> {
			var client = InstanceUtils.getClient();
			
			if (client != null && client.player != null) {
				var chestStack = client.player.getEquippedStack(EquipmentSlot.CHEST);
				
				var hidden = !(chestStack.getItem() instanceof SpaceSuitArmorItem.Chestplate);
				
				spaceSuitEnergyBar.setHidden(hidden);
				spaceSuitOxygenBar.setHidden(hidden);
				
				if (!hidden) {
					var itemRenderer = DrawingUtils.getItemRenderer();
					
					itemRenderer.renderGuiItemIcon(new ItemStack(AMItems.ENERGY.get()), (int) spaceSuitEnergyBar.getX() - 4, (int) (spaceSuitEnergyBar.getY() + 81.0F + 1.0F));
					itemRenderer.renderGuiItemIcon(new ItemStack(AMItems.FLUID.get()), (int) spaceSuitOxygenBar.getX() - 4, (int) (spaceSuitOxygenBar.getY() + 81.0F + 1.0F));
				}
			}
			
			var scaledHeight = client.getWindow().getScaledHeight();
			
			var energyBarPos = new Position(1.0F + 4.0F, scaledHeight / 2.0F - 81.0F / 2.0F);
			var oxygenBarPos = energyBarPos.plus(new Position(9.0F + 1.0F, 0.0F));
			
			spaceSuitEnergyBar.setPosition(energyBarPos);
			spaceSuitOxygenBar.setPosition(oxygenBarPos);
			
			spaceSuitEnergyBar.setSize(new Size(9.0F, 81.0F));
			spaceSuitOxygenBar.setSize(new Size(9.0F, 81.0F));
		}));
		
		InGameHudEvents.INIT.register((hud, collection) -> {
			collection.add(spaceSuitEnergyBar);
			collection.add(spaceSuitOxygenBar);
			
			var bar = new HudBarWidget(
					HudBarWidget.Side.RIGHT,
					HudBarWidget.Type.CONTINUOS,
					() -> {
						var client = InstanceUtils.getClient();
						
						if (client != null && client.player != null) {
							var component = OxygenComponent.get(client.player);
							
							if (component != null) {
								return (float) component.getMaximumOxygen();
							}
						}
						
						return 100.0F;
					},
					() -> {
						var client = InstanceUtils.getClient();
						
						if (client != null && client.player != null) {
							var component = OxygenComponent.get(client.player);
							
							if (component != null) {
								return (float) component.getOxygen();
							}
						}
						
						return 0.0F;
					}
			);
			
			bar.setHorizontal(true);
			
			bar.setShow(() -> {
				var client = InstanceUtils.getClient();
				
				if (client != null && client.player != null && !client.player.isCreative() && !client.player.isSpectator() && AMWorlds.isSpace(client.player.world.method_40134())) {
					var component = OxygenComponent.get(client.player);
					
					if (component != null) {
						return component.getOxygen() != component.getMaximumOxygen();
					}
				}
				
				return false;
			});
			
			bar.setSmooth(false);
			bar.setInvert(true);
			
			bar.setForegroundTexture(new ImageTexture(AMCommon.id("textures/widget/horizontal_oxygen_bar_foreground.png")));
			bar.setBackgroundTexture(new ImageTexture(AMCommon.id("textures/widget/horizontal_oxygen_bar_background.png")));
			
			collection.add(bar);
		});
		
		ClientTooltipEvent.ITEM.register(((stack, tooltips, context) -> {
			var item = stack.getItem();
			
			var id = Registry.ITEM.getId(item);
			
			if (id.getNamespace().equals(AMCommon.MOD_ID)) {
				var empty = tooltips.stream().filter(text -> text.asString().isEmpty()).findFirst().orElse(null);
				
				var index = empty == null ? tooltips.size() : tooltips.indexOf(empty) + 1;
				
				var fluidStorages = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
				
				if (fluidStorages != null) {
					var emptyTooltip = new ArrayList<Text>();
					
					try (var transaction = Transaction.openOuter()) {
						for (var storage : fluidStorages.iterable(transaction)) {
							if (storage.isResourceBlank()) {
								emptyTooltip.add(new TranslatableText("text.astromine.fluid").formatted(Formatting.BLUE));
								emptyTooltip.add(new TranslatableText("text.astromine.empty").formatted(Formatting.GRAY));
							} else {
								if (context.isAdvanced()) {
									tooltips.addAll(index, FluidTextUtils.getDetailedStorageTooltips(storage));
								} else {
									tooltips.addAll(index, FluidTextUtils.getShortenedStorageTooltips(storage));
								}
							}
						}
						
						transaction.abort();
					}
					
					tooltips.addAll(index, emptyTooltip);
				}
				
				var energyStorages = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
				
				if (energyStorages != null) {
					if (context.isAdvanced()) {
						tooltips.addAll(index, EnergyTextUtils.getDetailedTooltips(energyStorages));
					} else {
						tooltips.addAll(index, EnergyTextUtils.getShortenedTooltips(energyStorages));
					}
				}
			}
		}));
		
		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof HolographicConnectorItem holographicConnectorItem) {
				var pair = holographicConnectorItem.fromStack(stack);
				
				if (pair != null) {
					var key = pair.registryKey();
					var pos = pair.blockPos();
					
					tooltip.add(TextUtils.EMPTY);
					tooltip.add(new TranslatableText("text.astromine.selected.dimension.blockPos", key, pos.getX(), pos.getY(), pos.getZ()).formatted(Formatting.GRAY));
				}
			}
		});
		
		ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
			if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof EnergyCableBlock cableBlock) {
				tooltip.add(new TranslatableText("text.astromine.tooltip.cable.speed", cableBlock.getNetworkType().getTransferRate()).styled(style -> style.withColor(EnergyTextUtils.COLOR_OVERRIDE.toRGB())));
			}
		});
		
		DimensionEffectsEvents.INIT.register((properties) -> properties.put(AMWorlds.EARTH_ORBIT_ID, new SpaceDimensionEffects()));
	}
}
