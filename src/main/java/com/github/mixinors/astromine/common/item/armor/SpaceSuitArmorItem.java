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

package com.github.mixinors.astromine.common.item.armor;

import com.github.mixinors.astromine.common.transfer.storage.EnergyStorageItem;
import com.github.mixinors.astromine.common.transfer.storage.FluidStorageItem;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

public class SpaceSuitArmorItem extends ArmorItem {
	public SpaceSuitArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
		super(material, slot, settings);
	}
	
	public static class Chestplate extends SpaceSuitArmorItem implements FluidStorageItem, EnergyStorageItem {
		private final long fluidCapacity;
		private final long energyCapacity;
		
		public Chestplate(ArmorMaterial material, EquipmentSlot slot, Settings settings, long fluidCapacity, long energyCapacity) {
			super(material, slot, settings);
			
			this.fluidCapacity = fluidCapacity;
			this.energyCapacity = energyCapacity;
		}
		
		@Override
		public long getFluidCapacity() {
			return fluidCapacity;
		}
		
		@Override
		public long getEnergyCapacity() {
			return energyCapacity;
		}
		
		@Override
		public int getItemBarStep(ItemStack stack) {
			if (getFluidCapacity() == 0) {
				return 0;
			}
			
			var fluidStorages = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
			
			var totalAmount = 0L;
			var totalCapacity = 0L;
			
			try (var transaction = Transaction.openOuter()) {
				for (var fluidStorage : fluidStorages) {
					totalAmount += fluidStorage.getAmount();
					totalCapacity += fluidStorage.getCapacity();
				}
				
				transaction.abort();
			}
			
			totalCapacity = totalCapacity == 0L ? 1L : totalCapacity;
			
			return (int) (13.0F * ((float) totalAmount / (float) totalCapacity));
		}
		
		@Override
		public boolean isItemBarVisible(ItemStack stack) {
			return true;
		}
		
		@Override
		public int getItemBarColor(ItemStack stack) {
			return 0x3DC5D6;
		}
	}
}
