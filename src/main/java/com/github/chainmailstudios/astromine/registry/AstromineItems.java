package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.*;
import com.github.chainmailstudios.astromine.common.item.weapon.variant.Weaponry;
import com.github.chainmailstudios.astromine.common.item.weapon.variant.ammo.Ammunition;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;

public class AstromineItems {
	public static final Item.Settings BASIC_SETTINGS = new Item.Settings().group(AstromineItemGroups.ASTROMINE);

	public static final UncoloredSpawnEggItem SPACE_SLIME_SPAWN_EGG = register("space_slime_spawn_egg", new UncoloredSpawnEggItem(
			AstromineEntities.SPACE_SLIME,
			new Item.Settings().group(AstromineItemGroups.ASTROMINE)
	));

	public static final SuperSpaceSlimeShooterItem SUPER_SPACE_SLIME_SHOOTER = register("super_space_slime_shooter", new SuperSpaceSlimeShooterItem(new Item.Settings().group(AstromineItemGroups.ASTROMINE)));
	public static final Item SPACE_SLIME_BALL = register("space_slime_ball", new Item(new Item.Settings().group(AstromineItemGroups.ASTROMINE)));

	// Realistic weaponry
	public static final Item SCAR_H = register("scar_h", new Weaponry.ScarH());
	public static final Item BARRET_M98B = register("barret_m98b", new Weaponry.BarretM98B());

	// Realistic ammunition
	public static final Item NATO_7_62_X_51_MM = register("nato_7_62x51mm", new Ammunition.Nato762x51mm());
	public static final Item LAPUA_8_6_X_70_MM = register("lapua_8_6x70mm", new Ammunition.Lapua86x70mm());

	// Realistic tooling
	public static final Item FIRE_EXTINGUISHER = register("fire_extinguisher", new FireExtinguisher());

	// Materials - Fragments
	public static final Item ASTERITE_FRAGMENT = register("asterite_fragment", new Item(BASIC_SETTINGS));
	public static final Item GALAXIUM_FRAGMENT = register("galaxium_fragment", new Item(BASIC_SETTINGS));

	// Materials - Ingots
	public static final Item METITE_INGOT = register("metite_ingot", new Item(BASIC_SETTINGS));
	public static final Item STELLUM_INGOT = register("stellum_ingot", new Item(BASIC_SETTINGS));
	public static final Item UNIVITE_INGOT = register("univite_ingot", new Item(BASIC_SETTINGS));

	// Materials - Clusters
	public static final Item ASTEROID_METITE_CLUSTER = register("asteroid_metite_cluster", new Item(BASIC_SETTINGS));
	public static final Item METEOR_METITE_CLUSTER = register("meteor_metite_cluster", new Item(BASIC_SETTINGS));
	public static final Item ASTEROID_COAL_CLUSTER = register("asteroid_coal_cluster", new Item(BASIC_SETTINGS));
	public static final Item ASTEROID_IRON_CLUSTER = register("asteroid_iron_cluster", new Item(BASIC_SETTINGS));
	public static final Item ASTEROID_GOLD_CLUSTER = register("asteroid_gold_cluster", new Item(BASIC_SETTINGS));
	public static final Item ASTEROID_REDSTONE_CLUSTER = register("asteroid_redstone_cluster", new Item(BASIC_SETTINGS));
	public static final Item ASTEROID_LAPIS_CLUSTER = register("asteroid_lapis_cluster", new Item(BASIC_SETTINGS));
	public static final Item ASTEROID_DIAMOND_CLUSTER = register("asteroid_diamond_cluster", new Item(BASIC_SETTINGS));
	public static final Item ASTEROID_EMERALD_CLUSTER = register("asteroid_emerald_cluster", new Item(BASIC_SETTINGS));

	// Tools
	public static final Item HOLOGRAPHIC_CONNECTOR = register("holographic_connector", new HolographicConnector(new Item.Settings().group(AstromineItemGroups.ASTROMINE).maxCount(1)));

	public static final Item METITE_PICKAXE = register("metite_pickaxe", new PickaxeItem(AstromineToolMaterials.METITE, 1, -2.8f, BASIC_SETTINGS));
	public static final Item METITE_AXE = register("metite_axe", new AxeItem(AstromineToolMaterials.METITE, 5f, -3.0f, BASIC_SETTINGS));
	public static final Item METITE_SHOVEL = register("metite_shovel", new ShovelItem(AstromineToolMaterials.METITE, 1.5f, -3.0f, BASIC_SETTINGS));
	public static final Item METITE_HOE = register("metite_hoe", new HoeItem(AstromineToolMaterials.METITE, -5, 0f, BASIC_SETTINGS));
	public static final Item METITE_SWORD = register("metite_sword", new SwordItem(AstromineToolMaterials.METITE, 3, -2.4f, BASIC_SETTINGS));

	public static final Item ASTERITE_PICKAXE = register("asterite_pickaxe", new PickaxeItem(AstromineToolMaterials.ASTERITE, 1, -2.8f, BASIC_SETTINGS));
	public static final Item ASTERITE_AXE = register("asterite_axe", new AxeItem(AstromineToolMaterials.ASTERITE, 5f, -3.0f, BASIC_SETTINGS));
	public static final Item ASTERITE_SHOVEL = register("asterite_shovel", new ShovelItem(AstromineToolMaterials.ASTERITE, 1.5f, -3.0f, BASIC_SETTINGS));
	public static final Item ASTERITE_HOE = register("asterite_hoe", new HoeItem(AstromineToolMaterials.ASTERITE, -5, 0f, BASIC_SETTINGS));
	public static final Item ASTERITE_SWORD = register("asterite_sword", new SwordItem(AstromineToolMaterials.ASTERITE, 3, -2.4f, BASIC_SETTINGS));

	public static final Item STELLUM_PICKAXE = register("stellum_pickaxe", new PickaxeItem(AstromineToolMaterials.STELLUM, 1, -2.8f, BASIC_SETTINGS));
	public static final Item STELLUM_AXE = register("stellum_axe", new AxeItem(AstromineToolMaterials.STELLUM, 5f, -3.0f, BASIC_SETTINGS));
	public static final Item STELLUM_SHOVEL = register("stellum_shovel", new ShovelItem(AstromineToolMaterials.STELLUM, 1.5f, -3.0f, BASIC_SETTINGS));
	public static final Item STELLUM_HOE = register("stellum_hoe", new HoeItem(AstromineToolMaterials.STELLUM, -6, 0f, BASIC_SETTINGS));
	public static final Item STELLUM_SWORD = register("stellum_sword", new SwordItem(AstromineToolMaterials.STELLUM, 3, -2.4f, BASIC_SETTINGS));

	public static final Item GALAXIUM_PICKAXE = register("galaxium_pickaxe", new PickaxeItem(AstromineToolMaterials.GALAXIUM, 1, -2.8f, BASIC_SETTINGS));
	public static final Item GALAXIUM_AXE = register("galaxium_axe", new AxeItem(AstromineToolMaterials.GALAXIUM, 5f, -3.0f, BASIC_SETTINGS));
	public static final Item GALAXIUM_SHOVEL = register("galaxium_shovel", new ShovelItem(AstromineToolMaterials.GALAXIUM, 1.5f, -3.0f, BASIC_SETTINGS));
	public static final Item GALAXIUM_HOE = register("galaxium_hoe", new HoeItem(AstromineToolMaterials.GALAXIUM, -6, 0f, BASIC_SETTINGS));
	public static final Item GALAXIUM_SWORD = register("galaxium_sword", new SwordItem(AstromineToolMaterials.GALAXIUM, 3, -2.4f, BASIC_SETTINGS));

	public static final Item UNIVITE_PICKAXE = register("univite_pickaxe", new PickaxeItem(AstromineToolMaterials.UNIVITE, 1, -2.8f, BASIC_SETTINGS));
	public static final Item UNIVITE_AXE = register("univite_axe", new AxeItem(AstromineToolMaterials.UNIVITE, 5f, -3.0f, BASIC_SETTINGS));
	public static final Item UNIVITE_SHOVEL = register("univite_shovel", new ShovelItem(AstromineToolMaterials.UNIVITE, 1.5f, -3.0f, BASIC_SETTINGS));
	public static final Item UNIVITE_HOE = register("univite_hoe", new HoeItem(AstromineToolMaterials.UNIVITE, -6, 0f, BASIC_SETTINGS));
	public static final Item UNIVITE_SWORD = register("univite_sword", new SwordItem(AstromineToolMaterials.UNIVITE, 3, -2.4f, BASIC_SETTINGS));

	// Armor
	public static final Item METITE_HELMET = register("metite_helmet", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.HEAD, BASIC_SETTINGS));
	public static final Item METITE_CHESTPLATE = register("metite_chestplate", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.CHEST, BASIC_SETTINGS));
	public static final Item METITE_LEGGINGS = register("metite_leggings", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.LEGS, BASIC_SETTINGS));
	public static final Item METITE_BOOTS = register("metite_boots", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.FEET, BASIC_SETTINGS));

	public static final Item ASTERITE_HELMET = register("asterite_helmet", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.HEAD, BASIC_SETTINGS));
	public static final Item ASTERITE_CHESTPLATE = register("asterite_chestplate", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.CHEST, BASIC_SETTINGS));
	public static final Item ASTERITE_LEGGINGS = register("asterite_leggings", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.LEGS, BASIC_SETTINGS));
	public static final Item ASTERITE_BOOTS = register("asterite_boots", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.FEET, BASIC_SETTINGS));

	public static final Item STELLUM_HELMET = register("stellum_helmet", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.HEAD, BASIC_SETTINGS));
	public static final Item STELLUM_CHESTPLATE = register("stellum_chestplate", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.CHEST, BASIC_SETTINGS));
	public static final Item STELLUM_LEGGINGS = register("stellum_leggings", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.LEGS, BASIC_SETTINGS));
	public static final Item STELLUM_BOOTS = register("stellum_boots", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.FEET, BASIC_SETTINGS));

	public static final Item GALAXIUM_HELMET = register("galaxium_helmet", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.HEAD, BASIC_SETTINGS));
	public static final Item GALAXIUM_CHESTPLATE = register("galaxium_chestplate", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.CHEST, BASIC_SETTINGS));
	public static final Item GALAXIUM_LEGGINGS = register("galaxium_leggings", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.LEGS, BASIC_SETTINGS));
	public static final Item GALAXIUM_BOOTS = register("galaxium_boots", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.FEET, BASIC_SETTINGS));

	public static final Item UNIVITE_HELMET = register("univite_helmet", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.HEAD, BASIC_SETTINGS));
	public static final Item UNIVITE_CHESTPLATE = register("univite_chestplate", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.CHEST, BASIC_SETTINGS));
	public static final Item UNIVITE_LEGGINGS = register("univite_leggings", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.LEGS, BASIC_SETTINGS));
	public static final Item UNIVITE_BOOTS = register("univite_boots", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.FEET, BASIC_SETTINGS));

	public static final Item SPACE_SUIT_HELMET = register("space_suit_helmet", new ArmorItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.HEAD, BASIC_SETTINGS));
	public static final Item SPACE_SUIT_CHESTPLATE = register("space_suit_chestplate", new ArmorItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.CHEST, BASIC_SETTINGS));
	public static final Item SPACE_SUIT_LEGGINGS = register("space_suit_leggings", new ArmorItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.LEGS, BASIC_SETTINGS));
	public static final Item SPACE_SUIT_BOOTS = register("space_suit_boots", new ArmorItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.FEET, BASIC_SETTINGS));

	public static final Item YEAST = register("yeast", new Item(new Item.Settings()));

	public static void initialize() {
		Iterator var1 = UncoloredSpawnEggItem.getAll().iterator();

		while(var1.hasNext()) {
			UncoloredSpawnEggItem spawnEggItem = (UncoloredSpawnEggItem)var1.next();
			DispenserBlock.registerBehavior(spawnEggItem, new ItemDispenserBehavior() {
				public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
					Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
					EntityType<?> entityType = ((UncoloredSpawnEggItem)stack.getItem()).getEntityType(stack.getTag());
					entityType.spawnFromItemStack(pointer.getWorld(), stack, null, pointer.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
					stack.decrement(1);
					return stack;
				}
			});
		}

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
