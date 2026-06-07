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
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class SpaceSuitArmorItem extends ArmorItem {
	public SpaceSuitArmorItem(Holder<ArmorMaterial> material, Type type, Properties settings) {
		super(material, type, settings);
	}
	
	public static class Chestplate extends SpaceSuitArmorItem implements FluidStorageItem, EnergyStorageItem {
		private final long fluidCapacity;
		private final long energyCapacity;
		
		public Chestplate(Holder<ArmorMaterial> material, Type type, Properties settings, long fluidCapacity, long energyCapacity) {
			super(material, type, settings);
			
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
		public int getBarWidth(ItemStack stack) {
			if (getFluidCapacity() == 0) {
				return 0;
			}
			
			var totalAmount = getStoredFluid(stack).getAmount();
			var totalCapacity = Math.max(1L, getFluidCapacity());
			
			return (int) (13.0F * ((float) totalAmount / (float) totalCapacity));
		}
		
		@Override
		public boolean isBarVisible(ItemStack stack) {
			return true;
		}
		
		@Override
		public int getBarColor(ItemStack stack) {
			return 0x3DC5D6;
		}
	}
}
