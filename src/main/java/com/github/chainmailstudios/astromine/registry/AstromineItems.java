package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.material.AstromineArmorMaterials;
import com.github.chainmailstudios.astromine.common.material.AstromineToolMaterials;
import com.github.chainmailstudios.astromine.common.weapon.variant.Weaponry;
import com.github.chainmailstudios.astromine.common.weapon.variant.ammo.Ammunition;
import com.github.chainmailstudios.astromine.tool.FireExtinguisher;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineItems {
	// Weaponry
	public static final Item SCAR_H = register("scar_h", new Weaponry.ScarH());
	public static final Item BARRET_M98B = register("barret_m98b", new Weaponry.BarretM98B());

	// Ammunition
	public static final Item NATO_7_62_X_51_MM = register("nato_7_62x51mm", new Ammunition.Nato762x51mm());
	public static final Item LAPUA_8_6_X_70_MM = register("lapua_8_6x70mm", new Ammunition.Lapua86x70mm());

	// Tooling
	public static final Item FIRE_EXTINGUISHER = register("fire_extinguisher", new FireExtinguisher());

	public static final Item.Settings BASIC_SETTINGS = new Item.Settings().group(AstromineItemGroups.ASTROMINE);

	// Materials
	public static Item ASTERITE_FRAGMENT = new Item(BASIC_SETTINGS);
	public static Item METITE_CLUSTER = new Item(BASIC_SETTINGS);
	public static Item METITE_INGOT = new Item(BASIC_SETTINGS);
	public static Item STELLUM_INGOT = new Item(BASIC_SETTINGS);
	public static Item GALAXIUM_FRAGMENT = new Item(BASIC_SETTINGS);
	public static Item UNIVITE_INGOT = new Item(BASIC_SETTINGS);

	// Tools
	public static Item ASTERITE_PICKAXE = new PickaxeItem(AstromineToolMaterials.ASTERITE, 1, -2.8f, BASIC_SETTINGS);
	public static Item ASTERITE_AXE = new AxeItem(AstromineToolMaterials.ASTERITE, 5f, -3.0f, BASIC_SETTINGS);
	public static Item ASTERITE_SHOVEL = new ShovelItem(AstromineToolMaterials.ASTERITE, 1.5f, -3.0f, BASIC_SETTINGS);
	public static Item ASTERITE_HOE = new HoeItem(AstromineToolMaterials.ASTERITE, -5, 0f, BASIC_SETTINGS);
	public static Item ASTERITE_SWORD = new SwordItem(AstromineToolMaterials.ASTERITE, 3, -2.4f, BASIC_SETTINGS);

	public static Item STELLUM_PICKAXE = new PickaxeItem(AstromineToolMaterials.STELLUM, 1, -2.8f, BASIC_SETTINGS);
	public static Item STELLUM_AXE = new AxeItem(AstromineToolMaterials.STELLUM, 5f, -3.0f, BASIC_SETTINGS);
	public static Item STELLUM_SHOVEL = new ShovelItem(AstromineToolMaterials.STELLUM, 1.5f, -3.0f, BASIC_SETTINGS);
	public static Item STELLUM_HOE = new HoeItem(AstromineToolMaterials.STELLUM, -6, 0f, BASIC_SETTINGS);
	public static Item STELLUM_SWORD = new SwordItem(AstromineToolMaterials.STELLUM, 3, -2.4f, BASIC_SETTINGS);

	public static Item GALAXIUM_PICKAXE = new PickaxeItem(AstromineToolMaterials.GALAXIUM, 1, -2.8f, BASIC_SETTINGS);
	public static Item GALAXIUM_AXE = new AxeItem(AstromineToolMaterials.GALAXIUM, 5f, -3.0f, BASIC_SETTINGS);
	public static Item GALAXIUM_SHOVEL = new ShovelItem(AstromineToolMaterials.GALAXIUM, 1.5f, -3.0f, BASIC_SETTINGS);
	public static Item GALAXIUM_HOE = new HoeItem(AstromineToolMaterials.GALAXIUM, -6, 0f, BASIC_SETTINGS);
	public static Item GALAXIUM_SWORD = new SwordItem(AstromineToolMaterials.GALAXIUM, 3, -2.4f, BASIC_SETTINGS);

	public static Item UNIVITE_PICKAXE = new PickaxeItem(AstromineToolMaterials.UNIVITE, 1, -2.8f, BASIC_SETTINGS);
	public static Item UNIVITE_AXE = new AxeItem(AstromineToolMaterials.UNIVITE, 5f, -3.0f, BASIC_SETTINGS);
	public static Item UNIVITE_SHOVEL = new ShovelItem(AstromineToolMaterials.UNIVITE, 1.5f, -3.0f, BASIC_SETTINGS);
	public static Item UNIVITE_HOE = new HoeItem(AstromineToolMaterials.UNIVITE, -6, 0f, BASIC_SETTINGS);
	public static Item UNIVITE_SWORD = new SwordItem(AstromineToolMaterials.UNIVITE, 3, -2.4f, BASIC_SETTINGS);

	// Armor
	public static Item ASTERITE_HELMET = new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.HEAD, BASIC_SETTINGS);
	public static Item ASTERITE_CHESTPLATE = new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.CHEST, BASIC_SETTINGS);
	public static Item ASTERITE_LEGGINGS = new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.LEGS, BASIC_SETTINGS);
	public static Item ASTERITE_BOOTS = new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.FEET, BASIC_SETTINGS);

	public static Item STELLUM_HELMET = new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.HEAD, BASIC_SETTINGS);
	public static Item STELLUM_CHESTPLATE = new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.CHEST, BASIC_SETTINGS);
	public static Item STELLUM_LEGGINGS = new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.LEGS, BASIC_SETTINGS);
	public static Item STELLUM_BOOTS = new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.FEET, BASIC_SETTINGS);

	public static Item GALAXIUM_HELMET = new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.HEAD, BASIC_SETTINGS);
	public static Item GALAXIUM_CHESTPLATE = new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.CHEST, BASIC_SETTINGS);
	public static Item GALAXIUM_LEGGINGS = new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.LEGS, BASIC_SETTINGS);
	public static Item GALAXIUM_BOOTS = new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.FEET, BASIC_SETTINGS);

	public static Item UNIVITE_HELMET = new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.HEAD, BASIC_SETTINGS);
	public static Item UNIVITE_CHESTPLATE = new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.CHEST, BASIC_SETTINGS);
	public static Item UNIVITE_LEGGINGS = new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.LEGS, BASIC_SETTINGS);
	public static Item UNIVITE_BOOTS = new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.FEET, BASIC_SETTINGS);

	public static Item SPACE_HELMET = new ArmorItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.HEAD, BASIC_SETTINGS);
	public static Item SPACE_SUIT_CHEST = new ArmorItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.CHEST, BASIC_SETTINGS);
	public static Item SPACE_SUIT_PANTS = new ArmorItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.LEGS, BASIC_SETTINGS);
	public static Item SPACE_BOOTS = new ArmorItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.FEET, BASIC_SETTINGS);

	public static void initialize() {
		ASTERITE_FRAGMENT = register("asterite_fragment", ASTERITE_FRAGMENT);
		METITE_CLUSTER = register("metite_cluster", METITE_CLUSTER);
		METITE_INGOT = register("metite_ingot", METITE_INGOT);
		STELLUM_INGOT = register("stellum_ingot", STELLUM_INGOT);
		GALAXIUM_FRAGMENT = register("galaxium_fragment", GALAXIUM_FRAGMENT);
		UNIVITE_INGOT = register("univite_ingot", UNIVITE_INGOT);

		ASTERITE_PICKAXE = register("asterite_pickaxe", ASTERITE_PICKAXE);
		ASTERITE_AXE = register("asterite_axe", ASTERITE_AXE);
		ASTERITE_SHOVEL = register("asterite_shovel", ASTERITE_SHOVEL);
		ASTERITE_HOE = register("asterite_hoe", ASTERITE_HOE);
		ASTERITE_SWORD = register("asterite_sword", ASTERITE_SWORD);

		STELLUM_PICKAXE = register("stellum_pickaxe", STELLUM_PICKAXE);
		STELLUM_AXE = register("stellum_axe", STELLUM_AXE);
		STELLUM_SHOVEL = register("stellum_shovel", STELLUM_SHOVEL);
		STELLUM_HOE = register("stellum_hoe", STELLUM_HOE);
		STELLUM_SWORD = register("stellum_sword", STELLUM_SWORD);

		GALAXIUM_PICKAXE = register("galaxium_pickaxe", GALAXIUM_PICKAXE);
		GALAXIUM_AXE = register("galaxium_axe", GALAXIUM_AXE);
		GALAXIUM_SHOVEL = register("galaxium_shovel", GALAXIUM_SHOVEL);
		GALAXIUM_HOE = register("galaxium_hoe", GALAXIUM_HOE);
		GALAXIUM_SWORD = register("galaxium_sword", GALAXIUM_SWORD);

		UNIVITE_PICKAXE = register("univite_pickaxe", UNIVITE_PICKAXE);
		UNIVITE_AXE = register("univite_axe", UNIVITE_AXE);
		UNIVITE_SHOVEL = register("univite_shovel", UNIVITE_SHOVEL);
		UNIVITE_HOE = register("univite_hoe", UNIVITE_HOE);
		UNIVITE_SWORD = register("univite_sword", UNIVITE_SWORD);

		ASTERITE_HELMET = register("asterite_helmet", ASTERITE_HELMET);
		ASTERITE_CHESTPLATE = register("asterite_chestplate", ASTERITE_CHESTPLATE);
		ASTERITE_LEGGINGS = register("asterite_leggings", ASTERITE_LEGGINGS);
		ASTERITE_BOOTS = register("asterite_boots", ASTERITE_BOOTS);

		STELLUM_HELMET = register("stellum_helmet", STELLUM_HELMET);
		STELLUM_CHESTPLATE = register("stellum_chestplate", STELLUM_CHESTPLATE);
		STELLUM_LEGGINGS = register("stellum_leggings", STELLUM_LEGGINGS);
		STELLUM_BOOTS = register("stellum_boots", STELLUM_BOOTS);

		GALAXIUM_HELMET = register("galaxium_helmet", GALAXIUM_HELMET);
		GALAXIUM_CHESTPLATE = register("galaxium_chestplate", GALAXIUM_CHESTPLATE);
		GALAXIUM_LEGGINGS = register("galaxium_leggings", GALAXIUM_LEGGINGS);
		GALAXIUM_BOOTS = register("galaxium_boots", GALAXIUM_BOOTS);

		UNIVITE_HELMET = register("univite_helmet", UNIVITE_HELMET);
		UNIVITE_CHESTPLATE = register("univite_chestplate", UNIVITE_CHESTPLATE);
		UNIVITE_LEGGINGS = register("univite_leggings", UNIVITE_LEGGINGS);
		UNIVITE_BOOTS = register("univite_boots", UNIVITE_BOOTS);

		SPACE_HELMET = register("space_helmet", SPACE_HELMET);
		SPACE_SUIT_CHEST = register("space_suit_chest", SPACE_SUIT_CHEST);
		SPACE_SUIT_PANTS = register("space_suit_pants", SPACE_SUIT_PANTS);
		SPACE_BOOTS = register("space_boots", SPACE_BOOTS);
	}

	/**
	 * @param name Name of item instance to be registered
	 * @param item Item instance to be registered
	 * @return Item instanced registered
	 */
	public static <T extends Item> T register(String name, T item) {
		return register(AstromineCommon.identifier(name), item);
	}

	/**
	 * @param name Identifier of item instance to be registered
	 * @param item Item instance to be registered
	 * @return Item instance registered
	 */
	public static <T extends Item> T register(Identifier name, T item) {
		return Registry.register(Registry.ITEM, name, item);
	}
}
