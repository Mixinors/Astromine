package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.item.*;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.item.weapon.AmmunitionItem;
import com.github.chainmailstudios.astromine.common.item.weapon.variant.Weaponry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class AstromineItems {
	// Spawn eggs
	public static final Item SPACE_SLIME_SPAWN_EGG = register("space_slime_spawn_egg", new UncoloredSpawnEggItem(AstromineEntityTypes.SPACE_SLIME, getBasicSettings()));
	public static final Item ROCKET = register("rocket", new UncoloredSpawnEggItem(AstromineEntityTypes.ROCKET, getBasicSettings()));

	// Misc materials
	public static final Item SPACE_SLIME_BALL = register("space_slime_ball", new Item(getBasicSettings()));
	public static final Item YEAST = register("yeast", new Item(getBasicSettings()));

	// Realistic weaponry
	public static final Item SCAR_H = register("scar_h", new Weaponry.ScarH(getBasicSettings().fireproof().maxCount(1)));
	public static final Item BARRET_M98B = register("barret_m98b", new Weaponry.BarretM98B(getBasicSettings().fireproof().maxCount(1)));

	// Fantasy weaponry
	public static final Item SUPER_SPACE_SLIME_SHOOTER = register("super_space_slime_shooter", new SuperSpaceSlimeShooterItem(getBasicSettings()));

	// Realistic ammunition
	public static final Item NATO_7_62_X_51_MM = register("nato_7_62x51mm", new AmmunitionItem(getBasicSettings().fireproof().maxCount(1).maxDamage(32)));
	public static final Item LAPUA_8_6_X_70_MM = register("lapua_8_6x70mm", new AmmunitionItem(getBasicSettings().fireproof().maxCount(1).maxDamage(6)));

	// Realistic tooling
	public static final Item FIRE_EXTINGUISHER = register("fire_extinguisher", new FireExtinguisherItem(getBasicSettings().maxCount(1)));

	// Materials - Fragments
	public static final Item ASTERITE_FRAGMENT = register("asterite_fragment", new Item(getBasicSettings()));
	public static final Item GALAXIUM_FRAGMENT = register("galaxium_fragment", new Item(getBasicSettings()));
	public static final Item DIAMOND_FRAGMENT = register("diamond_fragment", new Item(getBasicSettings()));
	public static final Item EMERALD_FRAGMENT = register("emerald_fragment", new Item(getBasicSettings()));

	// Materials - Gems
	public static final Item ASTERITE = register("asterite", new Item(getBasicSettings()));
	public static final Item GALAXIUM = register("galaxium", new Item(getBasicSettings()));

	public static final Item ENERGY = register("energy", new Item(getBasicSettings()));
	public static final Item FLUID = register("fluid", new Item(getBasicSettings()));
	public static final Item ITEM = register("item", new Item(getBasicSettings()));

	// Materials - Nuggets
	public static final Item METITE_NUGGET = register("metite_nugget", new Item(getBasicSettings()));
	public static final Item STELLUM_NUGGET = register("stellum_nugget", new Item(getBasicSettings()));
	public static final Item UNIVITE_NUGGET = register("univite_nugget", new Item(getBasicSettings()));
	public static final Item NETHERITE_NUGGET = register("netherite_nugget", new Item(getBasicSettings().fireproof()));

	// Materials - Ingots
	public static final Item METITE_INGOT = register("metite_ingot", new Item(getBasicSettings()));
	public static final Item STELLUM_INGOT = register("stellum_ingot", new Item(getBasicSettings().fireproof()));
	public static final Item UNIVITE_INGOT = register("univite_ingot", new Item(getBasicSettings().fireproof()));

	// Materials - Clusters
	public static final Item METEOR_METITE_CLUSTER = register("meteor_metite_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_METITE_CLUSTER = register("asteroid_metite_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_ASTERITE_CLUSTER = register("asteroid_asterite_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_STELLUM_CLUSTER = register("asteroid_stellum_cluster", new Item(getBasicSettings().fireproof()));
	public static final Item ASTEROID_GALAXIUM_CLUSTER = register("asteroid_galaxium_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_COAL_CLUSTER = register("asteroid_coal_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_IRON_CLUSTER = register("asteroid_iron_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_GOLD_CLUSTER = register("asteroid_gold_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_REDSTONE_CLUSTER = register("asteroid_redstone_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_LAPIS_CLUSTER = register("asteroid_lapis_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_DIAMOND_CLUSTER = register("asteroid_diamond_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_EMERALD_CLUSTER = register("asteroid_emerald_cluster", new Item(getBasicSettings()));

	// Materials - Dusts
	public static final Item METITE_DUST = register("metite_dust", new Item(getBasicSettings()));
	public static final Item ASTERITE_DUST = register("asterite_dust", new Item(getBasicSettings()));
	public static final Item STELLUM_DUST = register("stellum_dust", new Item(getBasicSettings().fireproof()));
	public static final Item GALAXIUM_DUST = register("galaxium_dust", new Item(getBasicSettings()));
	public static final Item UNIVITE_DUST = register("univite_dust", new Item(getBasicSettings().fireproof()));
	public static final Item IRON_DUST = register("iron_dust", new Item(getBasicSettings()));
	public static final Item GOLD_DUST = register("gold_dust", new Item(getBasicSettings()));
	public static final Item LAPIS_DUST = register("lapis_dust", new Item(getBasicSettings()));
	public static final Item DIAMOND_DUST = register("diamond_dust", new Item(getBasicSettings()));
	public static final Item EMERALD_DUST = register("emerald_dust", new Item(getBasicSettings()));
	public static final Item NETHERITE_DUST = register("netherite_dust", new Item(getBasicSettings().fireproof()));

	// Materials - Plates
	public static final Item METITE_PLATES = register("metite_plates", new Item(getBasicSettings()));
	public static final Item STELLUM_PLATES = register("stellum_plates", new Item(getBasicSettings().fireproof()));
	public static final Item UNIVITE_PLATES = register("univite_plates", new Item(getBasicSettings().fireproof()));
	public static final Item IRON_PLATES = register("iron_plates", new Item(getBasicSettings()));
	public static final Item GOLD_PLATES = register("gold_plates", new Item(getBasicSettings()));
	public static final Item NETHERITE_PLATES = register("netherite_plates", new Item(getBasicSettings().fireproof()));

	// Materials - Gears
	public static final Item METITE_GEAR = register("metite_gear", new Item(getBasicSettings()));
	public static final Item STELLUM_GEAR = register("stellum_gear", new Item(getBasicSettings().fireproof()));
	public static final Item UNIVITE_GEAR = register("univite_gear", new Item(getBasicSettings().fireproof()));
	public static final Item IRON_GEAR = register("iron_gear", new Item(getBasicSettings()));
	public static final Item GOLD_GEAR = register("gold_gear", new Item(getBasicSettings()));
	public static final Item NETHERITE_GEAR = register("netherite_gear", new Item(getBasicSettings().fireproof()));

	// Circuits
	public static final Item BASIC_CIRCUIT = register("basic_circuit", new Item(getBasicSettings()));
	public static final Item ADVANCED_CIRCUIT = register("advanced_circuit", new Item(getBasicSettings()));
	public static final Item ELITE_CIRCUIT = register("elite_circuit", new Item(getBasicSettings()));

	// Containers
	public static final Item MACHINE_CHASSIS = register("machine_chassis", new Item(getBasicSettings()));
	public static final Item GRAPHITE_SHEET = register("graphite_sheet", new Item(getBasicSettings()));
	public static final Item GAS_CANISTER = register("gas_canister", FluidVolumeItem.of(getBasicSettings(), Fraction.of(8, 1)));
	public static final Item PRESSURIZED_GAS_CANISTER = register("pressurized_gas_canister", FluidVolumeItem.of(getBasicSettings(), Fraction.of(32, 1)));
	public static final Item BASIC_BATTERY = register("basic_battery", EnergyVolumeItem.of(getBasicSettings(), 9000));
	public static final Item ADVANCED_BATTERY = register("advanced_battery", EnergyVolumeItem.of(getBasicSettings(), 24000));
	public static final Item ELITE_BATTERY = register("elite_battery", EnergyVolumeItem.of(getBasicSettings(), 64000));
	public static final Item CREATIVE_BATTERY = register("creative_battery", EnergyVolumeItem.of(getBasicSettings(), 128000));

	// Tools
	public static final Item HOLOGRAPHIC_CONNECTOR = register("holographic_connector", new HolographicConnectorItem(getBasicSettings().maxCount(1)));

	public static final Item METITE_PICKAXE = register("metite_pickaxe", new PickaxeItem(AstromineToolMaterials.METITE, 1, -2.8f, getBasicSettings()));
	public static final Item METITE_AXE = register("metite_axe", new AxeItem(AstromineToolMaterials.METITE, 5f, -3.0f, getBasicSettings()));
	public static final Item METITE_SHOVEL = register("metite_shovel", new ShovelItem(AstromineToolMaterials.METITE, 1.5f, -3.0f, getBasicSettings()));
	public static final Item METITE_HOE = register("metite_hoe", new HoeItem(AstromineToolMaterials.METITE, -5, 0f, getBasicSettings()));
	public static final Item METITE_SWORD = register("metite_sword", new SwordItem(AstromineToolMaterials.METITE, 3, -2.4f, getBasicSettings()));

	public static final Item ASTERITE_PICKAXE = register("asterite_pickaxe", new PickaxeItem(AstromineToolMaterials.ASTERITE, 1, -2.8f, getBasicSettings()));
	public static final Item ASTERITE_AXE = register("asterite_axe", new AxeItem(AstromineToolMaterials.ASTERITE, 5f, -3.0f, getBasicSettings()));
	public static final Item ASTERITE_SHOVEL = register("asterite_shovel", new ShovelItem(AstromineToolMaterials.ASTERITE, 1.5f, -3.0f, getBasicSettings()));
	public static final Item ASTERITE_HOE = register("asterite_hoe", new HoeItem(AstromineToolMaterials.ASTERITE, -5, 0f, getBasicSettings()));
	public static final Item ASTERITE_SWORD = register("asterite_sword", new SwordItem(AstromineToolMaterials.ASTERITE, 3, -2.4f, getBasicSettings()));

	public static final Item STELLUM_PICKAXE = register("stellum_pickaxe", new PickaxeItem(AstromineToolMaterials.STELLUM, 1, -2.8f, getBasicSettings().fireproof()));
	public static final Item STELLUM_AXE = register("stellum_axe", new AxeItem(AstromineToolMaterials.STELLUM, 5f, -3.0f, getBasicSettings().fireproof()));
	public static final Item STELLUM_SHOVEL = register("stellum_shovel", new ShovelItem(AstromineToolMaterials.STELLUM, 1.5f, -3.0f, getBasicSettings().fireproof()));
	public static final Item STELLUM_HOE = register("stellum_hoe", new HoeItem(AstromineToolMaterials.STELLUM, -6, 0f, getBasicSettings().fireproof()));
	public static final Item STELLUM_SWORD = register("stellum_sword", new SwordItem(AstromineToolMaterials.STELLUM, 3, -2.4f, getBasicSettings().fireproof()));

	public static final Item GALAXIUM_PICKAXE = register("galaxium_pickaxe", new PickaxeItem(AstromineToolMaterials.GALAXIUM, 1, -2.8f, getBasicSettings()));
	public static final Item GALAXIUM_AXE = register("galaxium_axe", new AxeItem(AstromineToolMaterials.GALAXIUM, 5f, -3.0f, getBasicSettings()));
	public static final Item GALAXIUM_SHOVEL = register("galaxium_shovel", new ShovelItem(AstromineToolMaterials.GALAXIUM, 1.5f, -3.0f, getBasicSettings()));
	public static final Item GALAXIUM_HOE = register("galaxium_hoe", new HoeItem(AstromineToolMaterials.GALAXIUM, -6, 0f, getBasicSettings()));
	public static final Item GALAXIUM_SWORD = register("galaxium_sword", new SwordItem(AstromineToolMaterials.GALAXIUM, 3, -2.4f, getBasicSettings()));

	public static final Item UNIVITE_PICKAXE = register("univite_pickaxe", new PickaxeItem(AstromineToolMaterials.UNIVITE, 1, -2.8f, getBasicSettings().fireproof()));
	public static final Item UNIVITE_AXE = register("univite_axe", new AxeItem(AstromineToolMaterials.UNIVITE, 5f, -3.0f, getBasicSettings().fireproof()));
	public static final Item UNIVITE_SHOVEL = register("univite_shovel", new ShovelItem(AstromineToolMaterials.UNIVITE, 1.5f, -3.0f, getBasicSettings().fireproof()));
	public static final Item UNIVITE_HOE = register("univite_hoe", new HoeItem(AstromineToolMaterials.UNIVITE, -6, 0f, getBasicSettings().fireproof()));
	public static final Item UNIVITE_SWORD = register("univite_sword", new SwordItem(AstromineToolMaterials.UNIVITE, 3, -2.4f, getBasicSettings().fireproof()));

	// Armor
	public static final Item METITE_HELMET = register("metite_helmet", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item METITE_CHESTPLATE = register("metite_chestplate", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item METITE_LEGGINGS = register("metite_leggings", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item METITE_BOOTS = register("metite_boots", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item ASTERITE_HELMET = register("asterite_helmet", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item ASTERITE_CHESTPLATE = register("asterite_chestplate", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item ASTERITE_LEGGINGS = register("asterite_leggings", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item ASTERITE_BOOTS = register("asterite_boots", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item STELLUM_HELMET = register("stellum_helmet", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.HEAD, getBasicSettings().fireproof()));
	public static final Item STELLUM_CHESTPLATE = register("stellum_chestplate", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.CHEST, getBasicSettings().fireproof()));
	public static final Item STELLUM_LEGGINGS = register("stellum_leggings", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.LEGS, getBasicSettings().fireproof()));
	public static final Item STELLUM_BOOTS = register("stellum_boots", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.FEET, getBasicSettings().fireproof()));

	public static final Item GALAXIUM_HELMET = register("galaxium_helmet", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item GALAXIUM_CHESTPLATE = register("galaxium_chestplate", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item GALAXIUM_LEGGINGS = register("galaxium_leggings", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item GALAXIUM_BOOTS = register("galaxium_boots", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item UNIVITE_HELMET = register("univite_helmet", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.HEAD, getBasicSettings().fireproof()));
	public static final Item UNIVITE_CHESTPLATE = register("univite_chestplate", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.CHEST, getBasicSettings().fireproof()));
	public static final Item UNIVITE_LEGGINGS = register("univite_leggings", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.LEGS, getBasicSettings().fireproof()));
	public static final Item UNIVITE_BOOTS = register("univite_boots", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.FEET, getBasicSettings().fireproof()));

	public static final Item SPACE_SUIT_HELMET = register("space_suit_helmet", new SpaceSuitItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item SPACE_SUIT_CHESTPLATE = register("space_suit_chestplate", new SpaceSuitItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item SPACE_SUIT_LEGGINGS = register("space_suit_leggings", new SpaceSuitItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item SPACE_SUIT_BOOTS = register("space_suit_boots", new SpaceSuitItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.FEET, getBasicSettings()));

	public static void initialize() {
		for (UncoloredSpawnEggItem spawnEggItem : UncoloredSpawnEggItem.getAll()) {
			DispenserBlock.registerBehavior(spawnEggItem, new ItemDispenserBehavior() {
				@Override
				public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
					Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
					EntityType<?> entityType = ((UncoloredSpawnEggItem) stack.getItem()).getEntityType(stack.getTag());
					entityType.spawnFromItemStack(pointer.getWorld(), stack, null, pointer.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
					stack.decrement(1);
					return stack;
				}
			});
		}

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Registry.register(Registry.ITEM, AstromineCommon.identifier("meteor_spawner"), new MeteorSpawnerDevItem(new Item.Settings()));
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

	public static Item.Settings getBasicSettings() {
		return new Item.Settings().group(AstromineItemGroups.ASTROMINE);
	}
}
