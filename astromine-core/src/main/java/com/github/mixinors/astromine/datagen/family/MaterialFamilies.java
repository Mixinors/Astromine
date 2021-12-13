package com.github.mixinors.astromine.datagen.family;

import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.Maps;

import com.github.mixinors.astromine.datagen.family.MaterialFamily.MaterialType;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMItems;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public class MaterialFamilies {
	private static final Map<Item, MaterialFamily> BASE_ITEMS_TO_FAMILIES = Maps.newHashMap();

	// Vanilla Gems

	public static final MaterialFamily DIAMOND = register(Items.DIAMOND, MaterialType.GEM)
			.block(Blocks.DIAMOND_BLOCK).nugget(AMItems.DIAMOND_FRAGMENT.get())
			.ore(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE)
			.asteroidOre(AMBlocks.ASTEROID_DIAMOND_ORE.get(), AMItems.ASTEROID_DIAMOND_CLUSTER.get())
			.dust(AMItems.DIAMOND_DUST.get(), AMItems.DIAMOND_TINY_DUST.get())
			.tools(Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_SWORD, Items.DIAMOND_HOE)
			.armor(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS).horseArmor(Items.DIAMOND_HORSE_ARMOR)
			.build();

	public static final MaterialFamily EMERALD = register(Items.EMERALD, MaterialType.GEM)
			.block(Blocks.EMERALD_BLOCK).nugget(AMItems.EMERALD_FRAGMENT.get())
			.ore(Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE)
			.asteroidOre(AMBlocks.ASTEROID_EMERALD_ORE.get(), AMItems.ASTEROID_EMERALD_CLUSTER.get())
			.dust(AMItems.EMERALD_DUST.get(), AMItems.EMERALD_TINY_DUST.get()).build();

	public static final MaterialFamily QUARTZ = register(Items.QUARTZ, MaterialType.GEM)
			.block2x2(Blocks.QUARTZ_BLOCK).nugget(AMItems.QUARTZ_FRAGMENT.get())
			.netherOre(Blocks.NETHER_QUARTZ_ORE)
			.dust(AMItems.QUARTZ_DUST.get(), AMItems.QUARTZ_TINY_DUST.get()).build();

	// Vanilla Ingots

	public static final MaterialFamily IRON = register(Items.IRON_INGOT, MaterialType.INGOT)
			.block(Blocks.IRON_BLOCK).nugget(Items.IRON_NUGGET)
			.ore(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE).rawOre(Items.RAW_IRON, Blocks.RAW_IRON_BLOCK)
			.asteroidOre(AMBlocks.ASTEROID_IRON_ORE.get(), AMItems.ASTEROID_IRON_CLUSTER.get())
			.dust(AMItems.IRON_DUST.get(), AMItems.IRON_TINY_DUST.get())
			.tools(Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_SWORD, Items.IRON_HOE)
			.armor(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS).horseArmor(Items.IRON_HORSE_ARMOR)
			.gear(AMItems.IRON_GEAR.get()).plate(AMItems.IRON_PLATE.get()).build();

	public static final MaterialFamily GOLD = register(Items.GOLD_INGOT, MaterialType.INGOT)
			.block(Blocks.GOLD_BLOCK).nugget(Items.GOLD_NUGGET)
			.ore(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE).rawOre(Items.RAW_GOLD, Blocks.RAW_GOLD_BLOCK).netherOre(Blocks.NETHER_GOLD_ORE)
			.asteroidOre(AMBlocks.ASTEROID_GOLD_ORE.get(), AMItems.ASTEROID_GOLD_CLUSTER.get())
			.dust(AMItems.GOLD_DUST.get(), AMItems.GOLD_TINY_DUST.get())
			.tools(Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_SWORD, Items.GOLDEN_HOE)
			.armor(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS).horseArmor(Items.GOLDEN_HORSE_ARMOR)
			.gear(AMItems.GOLD_GEAR.get()).plate(AMItems.GOLD_PLATE.get())
			.wire(AMItems.GOLD_WIRE.get())
			.apple(Items.GOLDEN_APPLE)
			.piglinLoved().build();

	public static final MaterialFamily COPPER = register(Items.COPPER_INGOT, MaterialType.INGOT)
			.block(Blocks.COPPER_BLOCK).nugget(AMItems.COPPER_NUGGET.get())
			.ore(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE).rawOre(Items.RAW_COPPER, Blocks.RAW_COPPER_BLOCK)
			.asteroidOre(AMBlocks.ASTEROID_COPPER_ORE.get(), AMItems.ASTEROID_COPPER_CLUSTER.get())
			.dust(AMItems.COPPER_DUST.get(), AMItems.COPPER_TINY_DUST.get())
			.gear(AMItems.COPPER_GEAR.get()).plate(AMItems.COPPER_PLATE.get())
			.wire(AMItems.COPPER_WIRE.get()).build();

	public static final MaterialFamily NETHERITE = register(Items.NETHERITE_INGOT, MaterialType.INGOT)
			.block(Blocks.NETHERITE_BLOCK).nugget(AMItems.NETHERITE_NUGGET.get())
			.dust(AMItems.NETHERITE_DUST.get(), AMItems.NETHERITE_TINY_DUST.get())
			.tools(Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_SWORD, Items.NETHERITE_HOE)
			.armor(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS)
			.gear(AMItems.NETHERITE_GEAR.get()).plate(AMItems.NETHERITE_PLATE.get())
			.smithingBase(DIAMOND).build();

	// Vanilla Misc

	public static final MaterialFamily COAL = register(Items.COAL, MaterialType.MISC)
			.block(Blocks.COAL_BLOCK)
			.ore(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE)
			.asteroidOre(AMBlocks.ASTEROID_COAL_ORE.get(), AMItems.ASTEROID_COAL_CLUSTER.get())
			.dust(AMItems.COAL_DUST.get(), AMItems.COAL_TINY_DUST.get()).build();

	public static final MaterialFamily REDSTONE = register(Items.REDSTONE, MaterialType.DUST)
			.block(Blocks.REDSTONE_BLOCK)
			.ore(Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE)
			.asteroidOre(AMBlocks.ASTEROID_REDSTONE_ORE.get(), AMItems.ASTEROID_REDSTONE_CLUSTER.get())
			.tinyDust(AMItems.REDSTONE_TINY_DUST.get()).build();

	public static final MaterialFamily LAPIS = register(Items.LAPIS_LAZULI, MaterialType.MISC)
			.block(Blocks.LAPIS_BLOCK)
			.ore(Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE)
			.asteroidOre(AMBlocks.ASTEROID_LAPIS_ORE.get(), AMItems.ASTEROID_LAPIS_CLUSTER.get())
			.dust(AMItems.LAPIS_DUST.get(), AMItems.LAPIS_TINY_DUST.get()).build();

	public static final MaterialFamily RAW_NETHERITE = register(Items.NETHERITE_SCRAP, MaterialType.MISC)
			.netherOre(Blocks.ANCIENT_DEBRIS)
			.dust(AMItems.RAW_NETHERITE_DUST.get(), AMItems.RAW_NETHERITE_TINY_DUST.get()).build();

	public static final MaterialFamily CHARCOAL = register(Items.CHARCOAL, MaterialType.MISC)
			.dust(AMItems.CHARCOAL_DUST.get(), AMItems.CHARCOAL_TINY_DUST.get()).build();

	public static final MaterialFamily GLOWSTONE = register(Items.GLOWSTONE_DUST, MaterialType.DUST)
			.block2x2(Blocks.GLOWSTONE)
			.tinyDust(AMItems.GLOWSTONE_TINY_DUST.get()).build();

	public static final MaterialFamily GUNPOWDER = register(Items.GUNPOWDER, MaterialType.DUST).build();

	// Astromine Gems

	public static final MaterialFamily ASTERITE = register(AMItems.ASTERITE.get(), MaterialType.GEM)
			.block(AMBlocks.ASTERITE_BLOCK.get()).nugget(AMItems.ASTERITE_FRAGMENT.get())
			.asteroidOre(AMBlocks.ASTEROID_ASTERITE_ORE.get(), AMItems.ASTEROID_ASTERITE_CLUSTER.get())
			.dust(AMItems.ASTERITE_DUST.get(), AMItems.ASTERITE_TINY_DUST.get())
			.tools(AMItems.ASTERITE_PICKAXE.get(), AMItems.ASTERITE_AXE.get(), AMItems.ASTERITE_SHOVEL.get(), AMItems.ASTERITE_SWORD.get(), AMItems.ASTERITE_HOE.get())
			.armor(AMItems.ASTERITE_HELMET.get(), AMItems.ASTERITE_CHESTPLATE.get(), AMItems.ASTERITE_LEGGINGS.get(), AMItems.ASTERITE_BOOTS.get()).build();

	public static final MaterialFamily GALAXIUM = register(AMItems.GALAXIUM.get(), MaterialType.GEM)
			.block(AMBlocks.GALAXIUM_BLOCK.get()).nugget(AMItems.GALAXIUM_FRAGMENT.get())
			.asteroidOre(AMBlocks.ASTEROID_GALAXIUM_ORE.get(), AMItems.ASTEROID_GALAXIUM_CLUSTER.get())
			.dust(AMItems.GALAXIUM_DUST.get(), AMItems.GALAXIUM_TINY_DUST.get())
			.tools(AMItems.GALAXIUM_PICKAXE.get(), AMItems.GALAXIUM_AXE.get(), AMItems.GALAXIUM_SHOVEL.get(), AMItems.GALAXIUM_SWORD.get(), AMItems.GALAXIUM_HOE.get())
			.armor(AMItems.GALAXIUM_HELMET.get(), AMItems.GALAXIUM_CHESTPLATE.get(), AMItems.GALAXIUM_LEGGINGS.get(), AMItems.GALAXIUM_BOOTS.get()).build();

	// Astromine Ingots

	public static final MaterialFamily TIN = register(AMItems.TIN_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.TIN_BLOCK.get()).nugget(AMItems.TIN_NUGGET.get())
			.ore(AMBlocks.TIN_ORE.get(), AMBlocks.DEEPSLATE_TIN_ORE.get()).rawOre(AMItems.RAW_TIN.get(), AMBlocks.RAW_TIN_BLOCK.get())
			.asteroidOre(AMBlocks.ASTEROID_TIN_ORE.get(), AMItems.ASTEROID_TIN_CLUSTER.get())
			.dust(AMItems.TIN_DUST.get(), AMItems.TIN_TINY_DUST.get())
			.gear(AMItems.TIN_GEAR.get()).plate(AMItems.TIN_PLATE.get())
			.wire(AMItems.TIN_WIRE.get()).build();

	public static final MaterialFamily SILVER = register(AMItems.SILVER_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.SILVER_BLOCK.get()).nugget(AMItems.SILVER_NUGGET.get())
			.ore(AMBlocks.SILVER_ORE.get(), AMBlocks.DEEPSLATE_SILVER_ORE.get()).rawOre(AMItems.RAW_SILVER.get(), AMBlocks.RAW_SILVER_BLOCK.get())
			.asteroidOre(AMBlocks.ASTEROID_SILVER_ORE.get(), AMItems.ASTEROID_SILVER_CLUSTER.get())
			.dust(AMItems.SILVER_DUST.get(), AMItems.SILVER_TINY_DUST.get())
			.gear(AMItems.SILVER_GEAR.get()).plate(AMItems.SILVER_PLATE.get())
			.wire(AMItems.SILVER_WIRE.get()).build();

	public static final MaterialFamily LEAD = register(AMItems.LEAD_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.LEAD_BLOCK.get()).nugget(AMItems.LEAD_NUGGET.get())
			.ore(AMBlocks.LEAD_ORE.get(), AMBlocks.DEEPSLATE_LEAD_ORE.get()).rawOre(AMItems.RAW_LEAD.get(), AMBlocks.RAW_LEAD_BLOCK.get())
			.asteroidOre(AMBlocks.ASTEROID_LEAD_ORE.get(), AMItems.ASTEROID_LEAD_CLUSTER.get())
			.dust(AMItems.LEAD_DUST.get(), AMItems.LEAD_TINY_DUST.get())
			.gear(AMItems.LEAD_GEAR.get()).plate(AMItems.LEAD_PLATE.get())
			.apple(AMItems.LEAD_APPLE.get()).build();

	public static final MaterialFamily STEEL = register(AMItems.STEEL_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.STEEL_BLOCK.get()).nugget(AMItems.STEEL_NUGGET.get())
			.dust(AMItems.STEEL_DUST.get(), AMItems.STEEL_TINY_DUST.get())
			.tools(AMItems.STEEL_PICKAXE.get(), AMItems.STEEL_AXE.get(), AMItems.STEEL_SHOVEL.get(), AMItems.STEEL_SWORD.get(), AMItems.STEEL_HOE.get())
			.armor(AMItems.STEEL_HELMET.get(), AMItems.STEEL_CHESTPLATE.get(), AMItems.STEEL_LEGGINGS.get(), AMItems.STEEL_BOOTS.get())
			.gear(AMItems.STEEL_GEAR.get()).plate(AMItems.STEEL_PLATE.get())
			.alloyIngredients(IRON, COAL, 1).alloyIngredients(IRON, CHARCOAL, 1).build();

	public static final MaterialFamily BRONZE = register(AMItems.BRONZE_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.BRONZE_BLOCK.get()).nugget(AMItems.BRONZE_NUGGET.get())
			.dust(AMItems.BRONZE_DUST.get(), AMItems.BRONZE_TINY_DUST.get())
			.tools(AMItems.BRONZE_PICKAXE.get(), AMItems.BRONZE_AXE.get(), AMItems.BRONZE_SHOVEL.get(), AMItems.BRONZE_SWORD.get(), AMItems.BRONZE_HOE.get())
			.armor(AMItems.BRONZE_HELMET.get(), AMItems.BRONZE_CHESTPLATE.get(), AMItems.BRONZE_LEGGINGS.get(), AMItems.BRONZE_BOOTS.get())
			.gear(AMItems.BRONZE_GEAR.get()).plate(AMItems.BRONZE_PLATE.get())
			.alloyIngredients(COPPER, 3, TIN, 1).build();

	public static final MaterialFamily ELECTRUM = register(AMItems.ELECTRUM_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.ELECTRUM_BLOCK.get()).nugget(AMItems.ELECTRUM_NUGGET.get())
			.dust(AMItems.ELECTRUM_DUST.get(), AMItems.ELECTRUM_TINY_DUST.get())
			.gear(AMItems.ELECTRUM_GEAR.get()).plate(AMItems.ELECTRUM_PLATE.get())
			.wire(AMItems.ELECTRUM_WIRE.get())
			.alloyIngredients(GOLD, SILVER).build();

	public static final MaterialFamily FOOLS_GOLD = register(AMItems.FOOLS_GOLD_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.FOOLS_GOLD_BLOCK.get()).nugget(AMItems.FOOLS_GOLD_NUGGET.get())
			.dust(AMItems.FOOLS_GOLD_DUST.get(), AMItems.FOOLS_GOLD_TINY_DUST.get())
			.armor(AMItems.FOOLS_GOLD_HELMET.get(), AMItems.FOOLS_GOLD_CHESTPLATE.get(), AMItems.FOOLS_GOLD_LEGGINGS.get(), AMItems.FOOLS_GOLD_BOOTS.get())
			.gear(AMItems.FOOLS_GOLD_GEAR.get()).plate(AMItems.FOOLS_GOLD_PLATE.get())
			.apple(AMItems.FOOLS_GOLD_APPLE.get())
			.alloyIngredients(IRON, GUNPOWDER, 1).build();

	public static final MaterialFamily METITE = register(AMItems.METITE_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.METITE_BLOCK.get()).nugget(AMItems.METITE_NUGGET.get())
			.meteorOre(AMBlocks.METEOR_METITE_ORE.get(), AMItems.METEOR_METITE_CLUSTER.get())
			.asteroidOre(AMBlocks.ASTEROID_METITE_ORE.get(), AMItems.ASTEROID_METITE_CLUSTER.get())
			.dust(AMItems.METITE_DUST.get(), AMItems.METITE_TINY_DUST.get())
			.tools(AMItems.METITE_PICKAXE.get(), AMItems.METITE_AXE.get(), AMItems.METITE_SHOVEL.get(), AMItems.METITE_SWORD.get(), AMItems.METITE_HOE.get())
			.armor(AMItems.METITE_HELMET.get(), AMItems.METITE_CHESTPLATE.get(), AMItems.METITE_LEGGINGS.get(), AMItems.METITE_BOOTS.get())
			.gear(AMItems.METITE_GEAR.get()).plate(AMItems.METITE_PLATE.get()).build();

	public static final MaterialFamily STELLUM = register(AMItems.STELLUM_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.STELLUM_BLOCK.get()).nugget(AMItems.STELLUM_NUGGET.get())
			.asteroidOre(AMBlocks.ASTEROID_STELLUM_ORE.get(), AMItems.ASTEROID_STELLUM_CLUSTER.get())
			.dust(AMItems.STELLUM_DUST.get(), AMItems.STELLUM_TINY_DUST.get())
			.tools(AMItems.STELLUM_PICKAXE.get(), AMItems.STELLUM_AXE.get(), AMItems.STELLUM_SHOVEL.get(), AMItems.STELLUM_SWORD.get(), AMItems.STELLUM_HOE.get())
			.armor(AMItems.STELLUM_HELMET.get(), AMItems.STELLUM_CHESTPLATE.get(), AMItems.STELLUM_LEGGINGS.get(), AMItems.STELLUM_BOOTS.get())
			.gear(AMItems.STELLUM_GEAR.get()).plate(AMItems.STELLUM_PLATE.get()).build();

	public static final MaterialFamily UNIVITE = register(AMItems.UNIVITE_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.UNIVITE_BLOCK.get()).nugget(AMItems.UNIVITE_NUGGET.get())
			.dust(AMItems.UNIVITE_DUST.get(), AMItems.UNIVITE_TINY_DUST.get())
			.tools(AMItems.UNIVITE_PICKAXE.get(), AMItems.UNIVITE_AXE.get(), AMItems.UNIVITE_SHOVEL.get(), AMItems.UNIVITE_SWORD.get(), AMItems.UNIVITE_HOE.get())
			.armor(AMItems.UNIVITE_HELMET.get(), AMItems.UNIVITE_CHESTPLATE.get(), AMItems.UNIVITE_LEGGINGS.get(), AMItems.UNIVITE_BOOTS.get())
			.gear(AMItems.UNIVITE_GEAR.get()).plate(AMItems.UNIVITE_PLATE.get())
			.smithingBase(GALAXIUM).build();

	public static final MaterialFamily METEORIC_STEEL = register(AMItems.METEORIC_STEEL_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.METEORIC_STEEL_BLOCK.get()).nugget(AMItems.METEORIC_STEEL_NUGGET.get())
			.dust(AMItems.METEORIC_STEEL_DUST.get(), AMItems.METEORIC_STEEL_TINY_DUST.get())
			.tools(AMItems.METEORIC_STEEL_PICKAXE.get(), AMItems.METEORIC_STEEL_AXE.get(), AMItems.METEORIC_STEEL_SHOVEL.get(), AMItems.METEORIC_STEEL_SWORD.get(), AMItems.METEORIC_STEEL_HOE.get())
			.armor(AMItems.METEORIC_STEEL_HELMET.get(), AMItems.METEORIC_STEEL_CHESTPLATE.get(), AMItems.METEORIC_STEEL_LEGGINGS.get(), AMItems.METEORIC_STEEL_BOOTS.get())
			.gear(AMItems.METEORIC_STEEL_GEAR.get()).plate(AMItems.METEORIC_STEEL_PLATE.get())
			.alloyIngredients(METITE, STEEL).build();

	public static MaterialFamily.Builder register(Item baseItem, MaterialType materialType) {
		MaterialFamily.Builder builder = new MaterialFamily.Builder(baseItem, materialType);
		MaterialFamily materialFamily = BASE_ITEMS_TO_FAMILIES.put(baseItem, builder.build());
		if (materialFamily != null) {
			throw new IllegalStateException("Duplicate family definition for " + Registry.ITEM.getId(baseItem));
		}
		return builder;
	}

	public static Stream<MaterialFamily> getFamilies() {
		return BASE_ITEMS_TO_FAMILIES.values().stream();
	}

	public static void init() {

	}
}
