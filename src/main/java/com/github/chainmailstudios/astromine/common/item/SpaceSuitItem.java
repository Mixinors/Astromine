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

package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class SpaceSuitItem extends ArmorItem {
	public SpaceSuitItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
		super(material, slot, settings);
	}

	public static ItemStack getHelmet(DefaultedList<ItemStack> stacks) {
		return stacks.get(0);
	}

	public static ItemStack getChestplate(DefaultedList<ItemStack> stacks) {
		return stacks.get(1);
	}

	public static ItemStack getLeggings(DefaultedList<ItemStack> stacks) {
		return stacks.get(2);
	}

	public static ItemStack getBoots(DefaultedList<ItemStack> stacks) {
		return stacks.get(3);
	}

	public static boolean hasFullArmor(DefaultedList<ItemStack> stacks) {
		return stacks.stream().allMatch(stack -> stack.getItem() instanceof SpaceSuitItem);
	}

	public static FluidVolume readVolume(DefaultedList<ItemStack> stacks) {
		return FluidVolume.fromTag(getChestplate(stacks).getOrCreateTag().getCompound("gas_volume"));
	}

	public static void writeVolume(DefaultedList<ItemStack> stacks, FluidVolume volume) {
		getChestplate(stacks).getOrCreateTag().put("gas_volume", volume.toTag(new CompoundTag()));
	}
}
