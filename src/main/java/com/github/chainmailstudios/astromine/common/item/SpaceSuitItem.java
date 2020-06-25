package com.github.chainmailstudios.astromine.common.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;

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
