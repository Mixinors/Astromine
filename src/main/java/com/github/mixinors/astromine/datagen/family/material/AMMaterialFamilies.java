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

package com.github.mixinors.astromine.datagen.family.material;

import com.github.mixinors.astromine.datagen.family.material.family.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.family.MaterialFamily.MaterialType;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.github.mixinors.astromine.registry.common.AMItems;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class AMMaterialFamilies {
	private static final Map<Item, MaterialFamily> BASE_ITEMS_TO_FAMILIES = new HashMap<>();
	
	// Vanilla Gems
	
	public static final MaterialFamily DIAMOND = register("diamond", Items.DIAMOND, MaterialType.GEM)
			.block(Blocks.DIAMOND_BLOCK)
			.nugget(AMItems.DIAMOND_FRAGMENT.get())
			.overworldOre(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE)
			.asteroidOre(AMBlocks.ASTEROID_DIAMOND_ORE.get(), AMItems.ASTEROID_DIAMOND_ORE_CLUSTER.get())
			.moonOre(AMBlocks.MOON_DIAMOND_ORE.get(), Items.DIAMOND)
			.darkMoonOre(AMBlocks.DARK_MOON_DIAMOND_ORE.get(), Items.DIAMOND)
			.dust(AMItems.DIAMOND_DUST.get(), AMItems.DIAMOND_TINY_DUST.get())
			.tools(Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_SWORD, Items.DIAMOND_HOE)
			.armor(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS)
			.horseArmor(Items.DIAMOND_HORSE_ARMOR)
			.validForBeacon()
			.miningLevel(MiningLevels.IRON)
			.oreSmeltingExperience(1.0F)
			.moltenFluid(AMFluids.MOLTEN_DIAMOND, 3.8F)
			.build();
	
	public static final MaterialFamily EMERALD = register("emerald", Items.EMERALD, MaterialType.GEM)
			.block(Blocks.EMERALD_BLOCK)
			.nugget(AMItems.EMERALD_FRAGMENT.get())
			.overworldOre(Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE)
			.asteroidOre(AMBlocks.ASTEROID_EMERALD_ORE.get(), AMItems.ASTEROID_EMERALD_ORE_CLUSTER.get())
			.moonOre(AMBlocks.MOON_EMERALD_ORE.get(), Items.EMERALD)
			.darkMoonOre(AMBlocks.DARK_MOON_EMERALD_ORE.get(), Items.EMERALD)
			.dust(AMItems.EMERALD_DUST.get(), AMItems.EMERALD_TINY_DUST.get())
			.validForBeacon()
			.miningLevel(MiningLevels.IRON)
			.oreSmeltingExperience(1.0F)
			.moltenFluid(AMFluids.MOLTEN_EMERALD, 2.8F)
			.build();
	
	public static final MaterialFamily QUARTZ = register("quartz", Items.QUARTZ, MaterialType.GEM)
			.block2x2(Blocks.QUARTZ_BLOCK)
			.nugget(AMItems.QUARTZ_FRAGMENT.get())
			.netherOre(Blocks.NETHER_QUARTZ_ORE)
			.dust(AMItems.QUARTZ_DUST.get(), AMItems.QUARTZ_TINY_DUST.get())
			.oreSmeltingExperience(0.2F)
			.moltenFluid(AMFluids.MOLTEN_QUARTZ, 1.75F)
			.build();
	
	public static final MaterialFamily AMETHYST = register("amethyst", Items.AMETHYST_SHARD, MaterialType.GEM)
			.block2x2(Blocks.AMETHYST_BLOCK)
			.nugget(AMItems.AMETHYST_FRAGMENT.get())
			.dust(AMItems.AMETHYST_DUST.get(), AMItems.AMETHYST_TINY_DUST.get())
			.moltenFluid(AMFluids.MOLTEN_AMETHYST, 2.0F)
			.build();
	
	// Vanilla Ingots
	
	public static final MaterialFamily IRON = register("iron", Items.IRON_INGOT, MaterialType.INGOT)
			.block(Blocks.IRON_BLOCK)
			.nugget(Items.IRON_NUGGET)
			.overworldOre(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE)
			.rawOre(Items.RAW_IRON, Blocks.RAW_IRON_BLOCK)
			.asteroidOre(AMBlocks.ASTEROID_IRON_ORE.get(), AMItems.ASTEROID_IRON_ORE_CLUSTER.get())
			.moonOre(AMBlocks.MOON_IRON_ORE.get(), Items.RAW_IRON)
			.darkMoonOre(AMBlocks.DARK_MOON_IRON_ORE.get(), Items.RAW_IRON)
			.dust(AMItems.IRON_DUST.get(), AMItems.IRON_TINY_DUST.get())
			.tools(Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_SWORD, Items.IRON_HOE)
			.armor(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS)
			.horseArmor(Items.IRON_HORSE_ARMOR)
			.gear(AMItems.IRON_GEAR.get())
			.plate(AMItems.IRON_PLATE.get())
			.validForBeacon()
			.miningLevel(MiningLevels.STONE)
			.moltenFluid(AMFluids.MOLTEN_IRON)
			.build();
	
	public static final MaterialFamily GOLD = register("gold", Items.GOLD_INGOT, MaterialType.INGOT)
			.block(Blocks.GOLD_BLOCK)
			.nugget(Items.GOLD_NUGGET)
			.overworldOre(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE)
			.rawOre(Items.RAW_GOLD, Blocks.RAW_GOLD_BLOCK)
			.netherOre(Blocks.NETHER_GOLD_ORE)
			.asteroidOre(AMBlocks.ASTEROID_GOLD_ORE.get(), AMItems.ASTEROID_GOLD_ORE_CLUSTER.get())
			.moonOre(AMBlocks.MOON_GOLD_ORE.get(), Items.RAW_GOLD)
			.darkMoonOre(AMBlocks.DARK_MOON_GOLD_ORE.get(), Items.RAW_GOLD)
			.dust(AMItems.GOLD_DUST.get(), AMItems.GOLD_TINY_DUST.get())
			.tools(Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_SWORD, Items.GOLDEN_HOE)
			.armor(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS)
			.horseArmor(Items.GOLDEN_HORSE_ARMOR)
			.gear(AMItems.GOLD_GEAR.get())
			.plate(AMItems.GOLD_PLATE.get())
			.wire(AMItems.GOLD_WIRE.get())
			.apple(Items.GOLDEN_APPLE)
			.piglinLoved()
			.validForBeacon()
			.miningLevel(MiningLevels.IRON)
			.oreSmeltingExperience(1.0F)
			.moltenFluid(AMFluids.MOLTEN_GOLD, 0.85F).build();
	
	public static final MaterialFamily COPPER = register("copper", Items.COPPER_INGOT, MaterialType.INGOT)
			.block(Blocks.COPPER_BLOCK)
			.nugget(AMItems.COPPER_NUGGET.get())
			.overworldOre(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE)
			.rawOre(Items.RAW_COPPER, Blocks.RAW_COPPER_BLOCK)
			.asteroidOre(AMBlocks.ASTEROID_COPPER_ORE.get(), AMItems.ASTEROID_COPPER_ORE_CLUSTER.get())
			.moonOre(AMBlocks.MOON_COPPER_ORE.get(), Items.RAW_COPPER)
			.darkMoonOre(AMBlocks.DARK_MOON_COPPER_ORE.get(), Items.RAW_COPPER)
			.dust(AMItems.COPPER_DUST.get(), AMItems.COPPER_TINY_DUST.get())
			.gear(AMItems.COPPER_GEAR.get())
			.plate(AMItems.COPPER_PLATE.get())
			.wire(AMItems.COPPER_WIRE.get())
			.miningLevel(MiningLevels.STONE)
			.moltenFluid(AMFluids.MOLTEN_COPPER, 0.875F)
			.build();
	
	// Vanilla Misc
	
	public static final MaterialFamily COAL = register("coal", Items.COAL, MaterialType.MISC)
			.block(Blocks.COAL_BLOCK)
			.overworldOre(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE)
			.asteroidOre(AMBlocks.ASTEROID_COAL_ORE.get(), AMItems.ASTEROID_COAL_ORE_CLUSTER.get())
			.moonOre(AMBlocks.MOON_COAL_ORE.get(), Items.COAL)
			.darkMoonOre(AMBlocks.DARK_MOON_COAL_ORE.get(), Items.COAL)
			.dust(AMItems.COAL_DUST.get(), AMItems.COAL_TINY_DUST.get())
			.oreSmeltingExperience(0.1F)
			.build();
	
	public static final MaterialFamily REDSTONE = register("redstone", Items.REDSTONE, MaterialType.DUST)
			.block(Blocks.REDSTONE_BLOCK)
			.overworldOre(Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE)
			.asteroidOre(AMBlocks.ASTEROID_REDSTONE_ORE.get(), AMItems.ASTEROID_REDSTONE_ORE_CLUSTER.get())
			.moonOreGem(AMBlocks.MOON_REDSTONE_ORE.get())
			.darkMoonOreGem(AMBlocks.DARK_MOON_REDSTONE_ORE.get())
			.tinyDust(AMItems.REDSTONE_TINY_DUST.get())
			.miningLevel(MiningLevels.IRON)
			.build();
	
	public static final MaterialFamily LAPIS = register("lapis", Items.LAPIS_LAZULI, MaterialType.MISC)
			.block(Blocks.LAPIS_BLOCK)
			.overworldOre(Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE)
			.asteroidOre(AMBlocks.ASTEROID_LAPIS_ORE.get(), AMItems.ASTEROID_LAPIS_ORE_CLUSTER.get())
			.moonOreGem(AMBlocks.MOON_LAPIS_ORE.get())
			.darkMoonOreGem(AMBlocks.DARK_MOON_LAPIS_ORE.get())
			.dust(AMItems.LAPIS_DUST.get(), AMItems.LAPIS_TINY_DUST.get())
			.alias("lapis_lazuli")
			.baseTagPathOverride("lapis")
			.miningLevel(MiningLevels.STONE)
			.oreSmeltingExperience(0.2F)
			.moltenFluid(AMFluids.MOLTEN_LAPIS, 1.6F)
			.build();
	
	public static final MaterialFamily RAW_NETHERITE = register("raw_netherite", Items.NETHERITE_SCRAP, MaterialType.MISC)
			.netherOre(Blocks.ANCIENT_DEBRIS)
			.dust(AMItems.RAW_NETHERITE_DUST.get(), AMItems.RAW_NETHERITE_TINY_DUST.get())
			.alias("netherite_scrap")
			.miningLevel(MiningLevels.DIAMOND)
			.oreSmeltingExperience(2.0F)
			.build();
	
	public static final MaterialFamily CHARCOAL = register("charcoal", Items.CHARCOAL, MaterialType.MISC)
			.dust(AMItems.CHARCOAL_DUST.get(), AMItems.CHARCOAL_TINY_DUST.get())
			.build();
	
	public static final MaterialFamily GLOWSTONE = register("glowstone", Items.GLOWSTONE_DUST, MaterialType.DUST)
			.block2x2(Blocks.GLOWSTONE)
			.tinyDust(AMItems.GLOWSTONE_TINY_DUST.get())
			.build();
	
	public static final MaterialFamily GUNPOWDER = register("gunpowder", Items.GUNPOWDER, MaterialType.DUST)
			.build();
	
	public static final MaterialFamily SLIME = register("slime", Items.SLIME_BALL, MaterialType.BALL)
			.block(Blocks.SLIME_BLOCK)
			.build();
	
	// Vanilla Alloys
	
	public static final MaterialFamily NETHERITE = register("netherite", Items.NETHERITE_INGOT, MaterialType.INGOT)
			.block(Blocks.NETHERITE_BLOCK).nugget(AMItems.NETHERITE_NUGGET.get())
			.dust(AMItems.NETHERITE_DUST.get(), AMItems.NETHERITE_TINY_DUST.get())
			.tools(Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_SWORD, Items.NETHERITE_HOE)
			.armor(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS)
			.gear(AMItems.NETHERITE_GEAR.get())
			.plate(AMItems.NETHERITE_PLATE.get())
			.smithingBase(DIAMOND)
			.alloyIngredients(GOLD, 4, RAW_NETHERITE, 4, 1, 100, 100)
			.validForBeacon()
			.miningLevel(MiningLevels.DIAMOND)
			.moltenFluid(AMFluids.MOLTEN_NETHERITE, 2.0F)
			.build();
	
	// Astromine Gems
	
	public static final MaterialFamily ASTERITE = register("asterite", AMItems.ASTERITE.get(), MaterialType.GEM)
			.block(AMBlocks.ASTERITE_BLOCK.get())
			.nugget(AMItems.ASTERITE_FRAGMENT.get())
			.asteroidOre(AMBlocks.ASTEROID_ASTERITE_ORE.get(), AMItems.ASTEROID_ASTERITE_ORE_CLUSTER.get())
			.dust(AMItems.ASTERITE_DUST.get(), AMItems.ASTERITE_TINY_DUST.get())
			.tools(AMItems.ASTERITE_PICKAXE.get(), AMItems.ASTERITE_AXE.get(), AMItems.ASTERITE_SHOVEL.get(), AMItems.ASTERITE_SWORD.get(), AMItems.ASTERITE_HOE.get())
			.armor(AMItems.ASTERITE_HELMET.get(), AMItems.ASTERITE_CHESTPLATE.get(), AMItems.ASTERITE_LEGGINGS.get(), AMItems.ASTERITE_BOOTS.get())
			.validForBeacon()
			.miningLevel(MiningLevels.NETHERITE)
			.oreSmeltingExperience(2.0F)
			.moltenFluid(AMFluids.MOLTEN_ASTERITE, 14).build();
	
	public static final MaterialFamily GALAXIUM = register("galaxium", AMItems.GALAXIUM.get(), MaterialType.GEM)
			.block(AMBlocks.GALAXIUM_BLOCK.get())
			.nugget(AMItems.GALAXIUM_FRAGMENT.get())
			.asteroidOre(AMBlocks.ASTEROID_GALAXIUM_ORE.get(), AMItems.ASTEROID_GALAXIUM_ORE_CLUSTER.get())
			.dust(AMItems.GALAXIUM_DUST.get(), AMItems.GALAXIUM_TINY_DUST.get())
			.tools(AMItems.GALAXIUM_PICKAXE.get(), AMItems.GALAXIUM_AXE.get(), AMItems.GALAXIUM_SHOVEL.get(), AMItems.GALAXIUM_SWORD.get(), AMItems.GALAXIUM_HOE.get())
			.armor(AMItems.GALAXIUM_HELMET.get(), AMItems.GALAXIUM_CHESTPLATE.get(), AMItems.GALAXIUM_LEGGINGS.get(), AMItems.GALAXIUM_BOOTS.get())
			.validForBeacon()
			.miningLevel(5)
			.oreSmeltingExperience(3.0F)
			.moltenFluid(AMFluids.MOLTEN_GALAXIUM, 18)
			.build();
	
	// Astromine Overworld Ingots
	
	public static final MaterialFamily TIN = register("tin", AMItems.TIN_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.TIN_BLOCK.get())
			.nugget(AMItems.TIN_NUGGET.get())
			.overworldOre(AMBlocks.TIN_ORE.get(), AMBlocks.DEEPSLATE_TIN_ORE.get())
			.rawOre(AMItems.RAW_TIN.get(), AMBlocks.RAW_TIN_BLOCK.get())
			.asteroidOre(AMBlocks.ASTEROID_TIN_ORE.get(), AMItems.ASTEROID_TIN_ORE_CLUSTER.get())
			.moonOre(AMBlocks.MOON_TIN_ORE.get(), AMItems.RAW_TIN.get())
			.darkMoonOre(AMBlocks.DARK_MOON_TIN_ORE.get(), AMItems.RAW_TIN.get())
			.dust(AMItems.TIN_DUST.get(), AMItems.TIN_TINY_DUST.get())
			.gear(AMItems.TIN_GEAR.get())
			.plate(AMItems.TIN_PLATE.get())
			.wire(AMItems.TIN_WIRE.get())
			.miningLevel(MiningLevels.STONE)
			.moltenFluid(AMFluids.MOLTEN_TIN, 0.6F)
			.build();
	
	public static final MaterialFamily SILVER = register("silver", AMItems.SILVER_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.SILVER_BLOCK.get())
			.nugget(AMItems.SILVER_NUGGET.get())
			.overworldOre(AMBlocks.SILVER_ORE.get(), AMBlocks.DEEPSLATE_SILVER_ORE.get())
			.rawOre(AMItems.RAW_SILVER.get(), AMBlocks.RAW_SILVER_BLOCK.get())
			.asteroidOre(AMBlocks.ASTEROID_SILVER_ORE.get(), AMItems.ASTEROID_SILVER_ORE_CLUSTER.get())
			.moonOre(AMBlocks.MOON_SILVER_ORE.get(), AMItems.RAW_SILVER.get())
			.darkMoonOre(AMBlocks.DARK_MOON_SILVER_ORE.get(), AMItems.RAW_SILVER.get())
			.dust(AMItems.SILVER_DUST.get(), AMItems.SILVER_TINY_DUST.get())
			.gear(AMItems.SILVER_GEAR.get())
			.plate(AMItems.SILVER_PLATE.get())
			.wire(AMItems.SILVER_WIRE.get())
			.validForBeacon()
			.miningLevel(MiningLevels.IRON)
			.oreSmeltingExperience(0.8F)
			.moltenFluid(AMFluids.MOLTEN_SILVER, 0.8F)
			.build();
	
	public static final MaterialFamily LEAD = register("lead", AMItems.LEAD_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.LEAD_BLOCK.get())
			.nugget(AMItems.LEAD_NUGGET.get())
			.overworldOre(AMBlocks.LEAD_ORE.get(), AMBlocks.DEEPSLATE_LEAD_ORE.get())
			.rawOre(AMItems.RAW_LEAD.get(), AMBlocks.RAW_LEAD_BLOCK.get())
			.asteroidOre(AMBlocks.ASTEROID_LEAD_ORE.get(), AMItems.ASTEROID_LEAD_ORE_CLUSTER.get())
			.moonOre(AMBlocks.MOON_LEAD_ORE.get(), AMItems.RAW_LEAD.get())
			.darkMoonOre(AMBlocks.DARK_MOON_LEAD_ORE.get(), AMItems.RAW_LEAD.get())
			.dust(AMItems.LEAD_DUST.get(), AMItems.LEAD_TINY_DUST.get())
			.gear(AMItems.LEAD_GEAR.get())
			.plate(AMItems.LEAD_PLATE.get())
			.apple(AMItems.LEAD_APPLE.get())
			.miningLevel(MiningLevels.IRON)
			.oreSmeltingExperience(0.8F)
			.moltenFluid(AMFluids.MOLTEN_LEAD, 0.775F).build();
	
	// Astromine Overworld Alloys
	
	public static final MaterialFamily STEEL = register("steel", AMItems.STEEL_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.STEEL_BLOCK.get())
			.nugget(AMItems.STEEL_NUGGET.get())
			.dust(AMItems.STEEL_DUST.get(), AMItems.STEEL_TINY_DUST.get())
			.tools(AMItems.STEEL_PICKAXE.get(), AMItems.STEEL_AXE.get(), AMItems.STEEL_SHOVEL.get(), AMItems.STEEL_SWORD.get(), AMItems.STEEL_HOE.get())
			.armor(AMItems.STEEL_HELMET.get(), AMItems.STEEL_CHESTPLATE.get(), AMItems.STEEL_LEGGINGS.get(), AMItems.STEEL_BOOTS.get())
			.gear(AMItems.STEEL_GEAR.get())
			.plate(AMItems.STEEL_PLATE.get())
			.alloyIngredients(IRON, 1, COAL, 2, 1, 250, 900)
			.alloyIngredients(IRON, 1, CHARCOAL, 2, 1, 250, 900)
			.validForBeacon()
			.miningLevel(MiningLevels.IRON)
			.moltenFluid(AMFluids.MOLTEN_STEEL, 1.1F)
			.build();
	
	public static final MaterialFamily BRONZE = register("bronze", AMItems.BRONZE_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.BRONZE_BLOCK.get())
			.nugget(AMItems.BRONZE_NUGGET.get())
			.dust(AMItems.BRONZE_DUST.get(), AMItems.BRONZE_TINY_DUST.get())
			.tools(AMItems.BRONZE_PICKAXE.get(), AMItems.BRONZE_AXE.get(), AMItems.BRONZE_SHOVEL.get(), AMItems.BRONZE_SWORD.get(), AMItems.BRONZE_HOE.get())
			.armor(AMItems.BRONZE_HELMET.get(), AMItems.BRONZE_CHESTPLATE.get(), AMItems.BRONZE_LEGGINGS.get(), AMItems.BRONZE_BOOTS.get())
			.gear(AMItems.BRONZE_GEAR.get())
			.plate(AMItems.BRONZE_PLATE.get())
			.alloyIngredients(COPPER, 3, TIN, 1, 200, 800)
			.validForBeacon()
			.miningLevel(MiningLevels.IRON)
			.moltenFluid(AMFluids.MOLTEN_BRONZE, 0.9F)
			.build();
	
	public static final MaterialFamily ELECTRUM = register("electrum", AMItems.ELECTRUM_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.ELECTRUM_BLOCK.get())
			.nugget(AMItems.ELECTRUM_NUGGET.get())
			.dust(AMItems.ELECTRUM_DUST.get(), AMItems.ELECTRUM_TINY_DUST.get())
			.gear(AMItems.ELECTRUM_GEAR.get())
			.plate(AMItems.ELECTRUM_PLATE.get())
			.wire(AMItems.ELECTRUM_WIRE.get())
			.alloyIngredients(GOLD, SILVER, 180, 750)
			.validForBeacon()
			.miningLevel(MiningLevels.IRON)
			.moltenFluid(AMFluids.MOLTEN_ELECTRUM, 1.1F)
			.build();
	
	public static final MaterialFamily FOOLS_GOLD = register("fools_gold", AMItems.FOOLS_GOLD_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.FOOLS_GOLD_BLOCK.get())
			.nugget(AMItems.FOOLS_GOLD_NUGGET.get())
			.dust(AMItems.FOOLS_GOLD_DUST.get(), AMItems.FOOLS_GOLD_TINY_DUST.get())
			.armor(AMItems.FOOLS_GOLD_HELMET.get(), AMItems.FOOLS_GOLD_CHESTPLATE.get(), AMItems.FOOLS_GOLD_LEGGINGS.get(), AMItems.FOOLS_GOLD_BOOTS.get())
			.gear(AMItems.FOOLS_GOLD_GEAR.get())
			.plate(AMItems.FOOLS_GOLD_PLATE.get())
			.apple(AMItems.FOOLS_GOLD_APPLE.get())
			.alloyIngredients(IRON, GUNPOWDER, 1, 150, 600)
			.piglinLoved()
			.alias("pyrite")
			.miningLevel(MiningLevels.STONE)
			.moltenFluid(AMFluids.MOLTEN_FOOLS_GOLD).build();
	
	// Astromine Space Ingots
	
	public static final MaterialFamily METITE = register("metite", AMItems.METITE_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.METITE_BLOCK.get())
			.nugget(AMItems.METITE_NUGGET.get())
			.meteorOre(AMBlocks.METEOR_METITE_ORE.get(), AMItems.METEOR_METITE_ORE_CLUSTER.get())
			.asteroidOre(AMBlocks.ASTEROID_METITE_ORE.get(), AMItems.ASTEROID_METITE_ORE_CLUSTER.get())
			.dust(AMItems.METITE_DUST.get(), AMItems.METITE_TINY_DUST.get())
			.tools(AMItems.METITE_PICKAXE.get(), AMItems.METITE_AXE.get(), AMItems.METITE_SHOVEL.get(), AMItems.METITE_SWORD.get(), AMItems.METITE_HOE.get())
			.armor(AMItems.METITE_HELMET.get(), AMItems.METITE_CHESTPLATE.get(), AMItems.METITE_LEGGINGS.get(), AMItems.METITE_BOOTS.get())
			.gear(AMItems.METITE_GEAR.get())
			.plate(AMItems.METITE_PLATE.get())
			.validForBeacon()
			.miningLevel(MiningLevels.NETHERITE).oreSmeltingExperience(0.5F)
			.moltenFluid(AMFluids.MOLTEN_METITE, 2.5F)
			.build();
	
	public static final MaterialFamily STELLUM = register("stellum", AMItems.STELLUM_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.STELLUM_BLOCK.get())
			.nugget(AMItems.STELLUM_NUGGET.get())
			.asteroidOre(AMBlocks.ASTEROID_STELLUM_ORE.get(), AMItems.ASTEROID_STELLUM_ORE_CLUSTER.get())
			.dust(AMItems.STELLUM_DUST.get(), AMItems.STELLUM_TINY_DUST.get())
			.tools(AMItems.STELLUM_PICKAXE.get(), AMItems.STELLUM_AXE.get(), AMItems.STELLUM_SHOVEL.get(), AMItems.STELLUM_SWORD.get(), AMItems.STELLUM_HOE.get())
			.armor(AMItems.STELLUM_HELMET.get(), AMItems.STELLUM_CHESTPLATE.get(), AMItems.STELLUM_LEGGINGS.get(), AMItems.STELLUM_BOOTS.get())
			.gear(AMItems.STELLUM_GEAR.get())
			.plate(AMItems.STELLUM_PLATE.get())
			.validForBeacon()
			.miningLevel(MiningLevels.NETHERITE)
			.oreSmeltingExperience(1.5F)
			.moltenFluid(AMFluids.MOLTEN_STELLUM, 8)
			.build();
	
	// Astromine Space Alloys
	
	public static final MaterialFamily UNIVITE = register("univite", AMItems.UNIVITE_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.UNIVITE_BLOCK.get())
			.nugget(AMItems.UNIVITE_NUGGET.get())
			.dust(AMItems.UNIVITE_DUST.get(), AMItems.UNIVITE_TINY_DUST.get())
			.tools(AMItems.UNIVITE_PICKAXE.get(), AMItems.UNIVITE_AXE.get(), AMItems.UNIVITE_SHOVEL.get(), AMItems.UNIVITE_SWORD.get(), AMItems.UNIVITE_HOE.get())
			.armor(AMItems.UNIVITE_HELMET.get(), AMItems.UNIVITE_CHESTPLATE.get(), AMItems.UNIVITE_LEGGINGS.get(), AMItems.UNIVITE_BOOTS.get())
			.gear(AMItems.UNIVITE_GEAR.get())
			.plate(AMItems.UNIVITE_PLATE.get())
			.smithingBase(GALAXIUM)
			.validForBeacon()
			.miningLevel(6)
			.moltenFluid(AMFluids.MOLTEN_UNIVITE, 20)
			.build();
	
	public static final MaterialFamily METEORIC_STEEL = register("meteoric_steel", AMItems.METEORIC_STEEL_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.METEORIC_STEEL_BLOCK.get())
			.nugget(AMItems.METEORIC_STEEL_NUGGET.get())
			.dust(AMItems.METEORIC_STEEL_DUST.get(), AMItems.METEORIC_STEEL_TINY_DUST.get())
			.tools(AMItems.METEORIC_STEEL_PICKAXE.get(), AMItems.METEORIC_STEEL_AXE.get(), AMItems.METEORIC_STEEL_SHOVEL.get(), AMItems.METEORIC_STEEL_SWORD.get(), AMItems.METEORIC_STEEL_HOE.get())
			.armor(AMItems.METEORIC_STEEL_HELMET.get(), AMItems.METEORIC_STEEL_CHESTPLATE.get(), AMItems.METEORIC_STEEL_LEGGINGS.get(), AMItems.METEORIC_STEEL_BOOTS.get())
			.gear(AMItems.METEORIC_STEEL_GEAR.get())
			.plate(AMItems.METEORIC_STEEL_PLATE.get())
			.alloyIngredients(METITE, 2, STEEL, 1, 300, 1000)
			.validForBeacon()
			.miningLevel(MiningLevels.NETHERITE)
			.moltenFluid(AMFluids.MOLTEN_METEORIC_STEEL, 2.6F)
			.build();

	// Astromine Moon Ingots

	public static final MaterialFamily LUNUM = register("lunum", AMItems.LUNUM_INGOT.get(), MaterialType.INGOT)
			.block(AMBlocks.LUNUM_BLOCK.get())
			.nugget(AMItems.LUNUM_NUGGET.get())
			.moonOre(AMBlocks.MOON_LUNUM_ORE.get(), AMBlocks.DARK_MOON_LUNUM_ORE.get())
			.rawOre(AMItems.RAW_LUNUM.get(), AMBlocks.RAW_LUNUM_BLOCK.get())
			.dust(AMItems.LUNUM_DUST.get(), AMItems.LUNUM_TINY_DUST.get())
			.tools(AMItems.LUNUM_PICKAXE.get(), AMItems.LUNUM_AXE.get(), AMItems.LUNUM_SHOVEL.get(), AMItems.LUNUM_SWORD.get(), AMItems.LUNUM_HOE.get())
			.armor(AMItems.LUNUM_HELMET.get(), AMItems.LUNUM_CHESTPLATE.get(), AMItems.LUNUM_LEGGINGS.get(), AMItems.LUNUM_BOOTS.get())
			.gear(AMItems.LUNUM_GEAR.get())
			.plate(AMItems.LUNUM_PLATE.get())
			.validForBeacon()
			.miningLevel(MiningLevels.STONE)
			.moltenFluid(AMFluids.MOLTEN_LUNUM)
			.build();
	
	// Astromine Misc
	
	public static final MaterialFamily SPACE_SLIME = register("space_slime", AMItems.SPACE_SLIME_BALL.get(), MaterialType.BALL)
			.block(AMBlocks.SPACE_SLIME_BLOCK.get())
			.noGenerateModels()
			.noGenerateHarvestTags()
			.alias("slime")
			.build();
	
	
	public static MaterialFamily.Builder register(String name, ItemConvertible baseItem, MaterialType materialType) {
		var builder = new MaterialFamily.Builder(name, baseItem.asItem(), materialType);
		
		var materialFamily = BASE_ITEMS_TO_FAMILIES.put(baseItem.asItem(), builder.build());
		
		if (materialFamily != null) {
			throw new IllegalStateException("Duplicate family definition for " + Registry.ITEM.getId(baseItem.asItem()));
		}
		
		return builder;
	}
	
	public static Stream<MaterialFamily> getFamilies() {
		return BASE_ITEMS_TO_FAMILIES.values().stream().sorted();
	}
	
	public static void init() {
	
	}
}
