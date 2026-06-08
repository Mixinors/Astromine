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

package com.github.mixinors.astromine.datagen.provider;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.AMDatagenLists;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.AMMaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.family.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.family.MaterialFamily.MaterialType;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.datagen.recipe.*;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AMRecipeProvider extends RecipeProvider {
	private static final int TINY_DUST_TIME = 20;
	private static final int TINY_DUST_ENERGY = 200;
	private static final int SALVAGE_TIME = 40;
	private static final int SALVAGE_ENERGY = 400;
	private static final int MATERIAL_PROCESSING_TIME = 80;
	private static final int MATERIAL_PROCESSING_ENERGY = 1600;
	private static final int CLUSTER_PROCESSING_TIME = 120;
	private static final int CLUSTER_PROCESSING_ENERGY = 2400;
	private static final int ORE_PROCESSING_TIME = 200;
	private static final int ORE_PROCESSING_ENERGY = 4000;
	private static final int BLOCK_2X2_PROCESSING_TIME = 320;
	private static final int BLOCK_2X2_PROCESSING_ENERGY = 6400;
	private static final int BLOCK_3X3_PROCESSING_TIME = 720;
	private static final int BLOCK_3X3_PROCESSING_ENERGY = 14400;
	private static final int SIMPLE_GRINDING_TIME = 80;
	private static final int SIMPLE_GRINDING_ENERGY = 800;
	private static final int HEAVY_GRINDING_TIME = 160;
	private static final int HEAVY_GRINDING_ENERGY = 1600;
	private static final int STONE_GRINDING_TIME = 200;
	private static final int STONE_GRINDING_ENERGY = 1600;
	private static final int BIOFUEL_GRINDING_BASE_TIME = 40;
	private static final int BIOFUEL_GRINDING_TIME_PER_OUTPUT = 20;
	private static final int BIOFUEL_GRINDING_ENERGY_PER_OUTPUT = 200;
	private static final int BIOFUEL_TO_BIOMASS_AMOUNT = 100;
	private static final int BIOFUEL_TO_BIOMASS_TIME = 80;
	private static final int BIOFUEL_TO_BIOMASS_ENERGY = 800;
	
	public static final Map<ItemVariant, TagOfferer> EQUIPMENT_OFFERERS = ImmutableMap.of(
			ItemVariant.HELMET, AMRecipeProvider::offerHelmetRecipe,
			ItemVariant.CHESTPLATE, AMRecipeProvider::offerChestplateRecipe,
			ItemVariant.LEGGINGS, AMRecipeProvider::offerLeggingsRecipe,
			ItemVariant.BOOTS, AMRecipeProvider::offerBootsRecipe,
			ItemVariant.PICKAXE, AMRecipeProvider::offerPickaxeRecipe,
			ItemVariant.AXE, AMRecipeProvider::offerAxeRecipe,
			ItemVariant.SHOVEL, AMRecipeProvider::offerShovelRecipe,
			ItemVariant.SWORD, AMRecipeProvider::offerSwordRecipe,
			ItemVariant.HOE, AMRecipeProvider::offerHoeRecipe
	);
	
	public static final Map<ItemVariant, TagOfferer> MISC_OFFERERS = ImmutableMap.of(
			ItemVariant.GEAR, AMRecipeProvider::offerGearRecipe,
			ItemVariant.PLATE, AMRecipeProvider::offerPlateRecipe,
			ItemVariant.APPLE, AMRecipeProvider::offerMaterialAppleRecipe
	);
	
	private static final Map<BlockFamily.Variant, BasicOfferer> STONECUTTING_OFFERERS = ImmutableMap.of(
			BlockFamily.Variant.STAIRS, AMRecipeProvider::offerStonecuttingRecipe,
			BlockFamily.Variant.SLAB, (exporter, output, input) -> offerStonecuttingRecipe(exporter, output, input, 2),
			BlockFamily.Variant.WALL, AMRecipeProvider::offerStonecuttingRecipe,
			BlockFamily.Variant.POLISHED, AMRecipeProvider::offerStonecuttingRecipe
	);
	
	public static final Map<Block, Block> REGULAR_TO_SMOOTH = ImmutableMap.of(
			AMBlocks.METEOR_STONE.get(), AMBlocks.SMOOTH_METEOR_STONE.get(),
			AMBlocks.ASTEROID_STONE.get(), AMBlocks.SMOOTH_ASTEROID_STONE.get(),
			AMBlocks.MOON_STONE.get(), AMBlocks.SMOOTH_MOON_STONE.get(),
			AMBlocks.DARK_MOON_STONE.get(), AMBlocks.SMOOTH_DARK_MOON_STONE.get()
	);
	
	public static final Map<BlockFamily, BlockFamily> SMOOTH_TO_POLISHED = ImmutableMap.of(
			AMBlockFamilies.SMOOTH_METEOR_STONE, AMBlockFamilies.POLISHED_METEOR_STONE,
			AMBlockFamilies.SMOOTH_ASTEROID_STONE, AMBlockFamilies.POLISHED_ASTEROID_STONE,
			AMBlockFamilies.SMOOTH_MOON_STONE, AMBlockFamilies.POLISHED_MOON_STONE,
			AMBlockFamilies.SMOOTH_DARK_MOON_STONE, AMBlockFamilies.POLISHED_DARK_MOON_STONE
	);
	
	public static final Map<BlockFamily, BlockFamily> POLISHED_TO_BRICK = ImmutableMap.of(
			AMBlockFamilies.POLISHED_METEOR_STONE, AMBlockFamilies.METEOR_STONE_BRICK,
			AMBlockFamilies.POLISHED_ASTEROID_STONE, AMBlockFamilies.ASTEROID_STONE_BRICK,
			AMBlockFamilies.POLISHED_MOON_STONE, AMBlockFamilies.MOON_STONE_BRICK,
			AMBlockFamilies.POLISHED_DARK_MOON_STONE, AMBlockFamilies.DARK_MOON_STONE_BRICK
	);
	
	public static final Map<BlockFamily, BlockFamily> SMOOTH_TO_BRICK = ImmutableMap.of(
			AMBlockFamilies.SMOOTH_METEOR_STONE, AMBlockFamilies.METEOR_STONE_BRICK,
			AMBlockFamilies.SMOOTH_ASTEROID_STONE, AMBlockFamilies.ASTEROID_STONE_BRICK,
			AMBlockFamilies.SMOOTH_MOON_STONE, AMBlockFamilies.MOON_STONE_BRICK,
			AMBlockFamilies.SMOOTH_DARK_MOON_STONE, AMBlockFamilies.DARK_MOON_STONE_BRICK
	);
	
	public static final Set<Map<BlockFamily, BlockFamily>> STONECUT_FAMILIES = ImmutableSet.of(
			SMOOTH_TO_POLISHED,
			POLISHED_TO_BRICK,
			SMOOTH_TO_BRICK
	);
	
	public static final Map<BlockFamily, BlockFamily> TRITURATED_BLOCK_FAMILIES = ImmutableMap.<BlockFamily, BlockFamily>builder()
			.put(BlockFamilies.STONE_BRICK, BlockFamilies.STONE)
			.put(BlockFamilies.DEEPSLATE_BRICKS, BlockFamilies.POLISHED_DEEPSLATE)
			.put(BlockFamilies.POLISHED_BLACKSTONE_BRICKS, BlockFamilies.POLISHED_BLACKSTONE)
			.put(AMBlockFamilies.ASTEROID_STONE_BRICK, AMBlockFamilies.POLISHED_ASTEROID_STONE)
			.put(AMBlockFamilies.METEOR_STONE_BRICK, AMBlockFamilies.POLISHED_METEOR_STONE)
			.put(AMBlockFamilies.MOON_STONE_BRICK, AMBlockFamilies.POLISHED_MOON_STONE)
			.put(AMBlockFamilies.DARK_MOON_STONE_BRICK, AMBlockFamilies.POLISHED_DARK_MOON_STONE)
			.put(BlockFamilies.POLISHED_BLACKSTONE, BlockFamilies.BLACKSTONE)
			.put(BlockFamilies.POLISHED_ANDESITE, BlockFamilies.ANDESITE)
			.put(BlockFamilies.POLISHED_DIORITE, BlockFamilies.DIORITE)
			.put(BlockFamilies.POLISHED_GRANITE, BlockFamilies.GRANITE)
			.put(BlockFamilies.POLISHED_DEEPSLATE, BlockFamilies.COBBLED_DEEPSLATE)
			.put(AMBlockFamilies.POLISHED_ASTEROID_STONE, AMBlockFamilies.ASTEROID_STONE)
			.put(AMBlockFamilies.POLISHED_METEOR_STONE, AMBlockFamilies.METEOR_STONE)
			.put(AMBlockFamilies.POLISHED_MOON_STONE, AMBlockFamilies.MOON_STONE)
			.put(AMBlockFamilies.POLISHED_DARK_MOON_STONE, AMBlockFamilies.DARK_MOON_STONE)
			.put(BlockFamilies.STONE, BlockFamilies.COBBLESTONE)
			.put(BlockFamilies.DEEPSLATE, BlockFamilies.COBBLED_DEEPSLATE)
			.put(AMBlockFamilies.SMOOTH_ASTEROID_STONE, AMBlockFamilies.ASTEROID_STONE)
			.put(AMBlockFamilies.SMOOTH_METEOR_STONE, AMBlockFamilies.METEOR_STONE)
			.put(AMBlockFamilies.SMOOTH_MOON_STONE, AMBlockFamilies.MOON_STONE)
			.put(AMBlockFamilies.SMOOTH_DARK_MOON_STONE, AMBlockFamilies.DARK_MOON_STONE)
			.build();
	
	public static final Map<Block, ItemLike> CONCRETE_TO_CONCRETE_POWDER = ImmutableMap.<Block, ItemLike>builder()
			.put(Blocks.WHITE_CONCRETE, Blocks.WHITE_CONCRETE_POWDER)
			.put(Blocks.ORANGE_CONCRETE, Blocks.ORANGE_CONCRETE_POWDER)
			.put(Blocks.MAGENTA_CONCRETE, Blocks.MAGENTA_CONCRETE_POWDER)
			.put(Blocks.LIGHT_BLUE_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE_POWDER)
			.put(Blocks.YELLOW_CONCRETE, Blocks.YELLOW_CONCRETE_POWDER)
			.put(Blocks.LIME_CONCRETE, Blocks.LIME_CONCRETE_POWDER)
			.put(Blocks.PINK_CONCRETE, Blocks.PINK_CONCRETE_POWDER)
			.put(Blocks.GRAY_CONCRETE, Blocks.GRAY_CONCRETE_POWDER)
			.put(Blocks.LIGHT_GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE_POWDER)
			.put(Blocks.CYAN_CONCRETE, Blocks.CYAN_CONCRETE_POWDER)
			.put(Blocks.PURPLE_CONCRETE, Blocks.PURPLE_CONCRETE_POWDER)
			.put(Blocks.BLUE_CONCRETE, Blocks.BLUE_CONCRETE_POWDER)
			.put(Blocks.BROWN_CONCRETE, Blocks.BROWN_CONCRETE_POWDER)
			.put(Blocks.GREEN_CONCRETE, Blocks.GREEN_CONCRETE_POWDER)
			.put(Blocks.RED_CONCRETE, Blocks.RED_CONCRETE_POWDER)
			.put(Blocks.BLACK_CONCRETE, Blocks.BLACK_CONCRETE_POWDER)
			.build();
	
	public static final Map<Block, ItemLike> TRITURATED_BLOCKS_1_TO_1_CHEAP = ImmutableMap.<Block, ItemLike>builder()
			.putAll(CONCRETE_TO_CONCRETE_POWDER)
			.put(Blocks.END_STONE_BRICKS, Blocks.END_STONE)
			.put(Blocks.CHISELED_DEEPSLATE, Blocks.COBBLED_DEEPSLATE)
			.put(Blocks.SMOOTH_STONE, Blocks.STONE)
			.put(Blocks.SMOOTH_STONE_SLAB, Blocks.STONE_SLAB)
			.put(Blocks.SOUL_SOIL, Blocks.SOUL_SAND)
			.build();
	
	public static final Map<Block, ItemLike> TRITURATED_BLOCKS_1_TO_4 = ImmutableMap.of(
			Blocks.MAGMA_BLOCK, Items.MAGMA_CREAM,
			Blocks.BROWN_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM,
			Blocks.RED_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM,
			Blocks.BRICKS, Items.BRICK,
			Blocks.NETHER_BRICKS, Items.NETHER_BRICK,
			Blocks.PRISMARINE, Items.PRISMARINE_SHARD,
			Blocks.HONEYCOMB_BLOCK, Items.HONEYCOMB
	);
	
	public static final Map<Block, ItemLike> TRITURATED_BLOCKS_1_TO_2 = ImmutableMap.of(
			Blocks.BRICK_SLAB, Items.BRICK,
			Blocks.NETHER_BRICK_SLAB, Items.NETHER_BRICK,
			Blocks.PRISMARINE_SLAB, Items.PRISMARINE_SHARD
	);
	
	public static final Map<Block, ItemLike> TRITURATED_BLOCKS_1_TO_9 = ImmutableMap.of(
			Blocks.BONE_BLOCK, Items.BONE_MEAL,
			Blocks.NETHER_WART_BLOCK, Items.NETHER_WART,
			Blocks.HAY_BLOCK, Items.WHEAT,
			Blocks.PRISMARINE_BRICKS, Items.PRISMARINE_SHARD
	);
	
	public static final Map<Block, ItemLike> TRITURATED_BLOCKS_1_TO_1_EXPENSIVE = ImmutableMap.of(
			Blocks.COBBLESTONE, Blocks.GRAVEL,
			Blocks.GRAVEL, Blocks.SAND
	);
	
	public static final Map<Map<Block, ItemLike>, Integer> TRITURATED_BLOCKS_CHEAP = ImmutableMap.of(
			TRITURATED_BLOCKS_1_TO_1_CHEAP, 1,
			TRITURATED_BLOCKS_1_TO_2, 2,
			TRITURATED_BLOCKS_1_TO_4, 4,
			TRITURATED_BLOCKS_1_TO_9, 9
	);
	
	public static final Map<TagKey<Item>, Integer> BIOFUEL_TAGS = ImmutableMap.of(
			AMTagKeys.ItemTags.MAKES_ONE_BIOFUEL, 1,
			AMTagKeys.ItemTags.MAKES_TWO_BIOFUEL, 2,
			AMTagKeys.ItemTags.MAKES_FOUR_BIOFUEL, 4,
			AMTagKeys.ItemTags.MAKES_NINE_BIOFUEL, 9
	);
	
	public AMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}
	
	public static String convertBetween(ItemLike to, String from) {
		return RecipeProvider.getSimpleRecipeName(to) + "_from_" + from;
	}
	
	public static String convertBetween(ItemLike to, TagKey<Item> from) {
		return getRecipeName(to) + "_from_" + getRecipeName(from);
	}
	
	public static String convertBetween(ItemLike to, String process, ItemLike from) {
		return RecipeProvider.getSimpleRecipeName(to) + "_from_" + process + "_" + RecipeProvider.getSimpleRecipeName(from);
	}
	
	public static String convertBetween(ItemLike to, String process, String from) {
		return RecipeProvider.getSimpleRecipeName(to) + "_from_" + process + "_" + from;
	}
	
	public static String convertBetween(ItemLike to, String process, TagKey<Item> from) {
		return getRecipeName(to) + "_from_" + process + "_" + getRecipeName(from);
	}
	
	public static String getRecipeName(TagKey<Item> tag) {
		return tag.location().getPath();
	}
	
	public static String getRecipeName(ItemLike item) {
		return RecipeProvider.getSimpleRecipeName(item);
	}
	
	public static void offerStonecuttingRecipe(RecipeOutput exporter, ItemLike output, ItemLike input) {
		stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, output, input);
	}
	
	public static void offerStonecuttingRecipe(RecipeOutput exporter, ItemLike output, ItemLike input, int count) {
		stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, output, input, count);
	}
	
	public static void offerReversibleCompactingRecipesWithInputItemGroup(RecipeOutput exporter, ItemLike input, ItemLike compacted) {
		offerReversibleCompactingRecipesWithInputItemGroup(exporter, input, compacted, getRecipeName(compacted));
	}
	
	public static void offerReversibleCompactingRecipesWithCompactedItemGroup(RecipeOutput exporter, ItemLike input, ItemLike compacted) {
		offerReversibleCompactingRecipesWithInputItemGroup(exporter, input, compacted, getRecipeName(compacted));
	}
	
	public static void offerReversibleCompactingRecipesWithInputItemGroup(RecipeOutput exporter, ItemLike input, ItemLike compacted, String from) {
		offerReversibleCompactingRecipesWithCompactingRecipeGroup(exporter, input, compacted, convertBetween(input, from), getRecipeName(input));
	}
	
	public static void offerReversibleCompactingRecipesWithCompactedItemGroup(RecipeOutput exporter, ItemLike input, ItemLike compacted, String from) {
		offerReversibleCompactingRecipesWithCompactingRecipeGroup(exporter, input, compacted, convertBetween(compacted, from), getRecipeName(compacted));
	}
	
	public static void offerReversibleCompactingRecipesWithCompactingRecipeGroup(RecipeOutput exporter, ItemLike input, ItemLike compacted, String compactingRecipeName, String compactingRecipeGroup) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, compacted)
				.define('#', input)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.group(compactingRecipeGroup)
				.unlockedBy("has_" + getRecipeName(input), RecipeProvider.has(input))
				.save(exporter, compactingRecipeName);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, input, 9)
				.requires(compacted)
				.group(getRecipeName(input))
				.unlockedBy("has_" + getRecipeName(compacted), RecipeProvider.has(compacted))
				.save(exporter, convertBetween(input, compactingRecipeGroup));
	}
	
	public static void offerSmeltingAndBlasting(RecipeOutput exporter, ItemLike input, ItemLike output, float experience) {
		offerSmeltingAndBlasting(exporter, ImmutableList.of(input), output, experience);
	}
	
	public static void offerSmeltingAndBlasting(RecipeOutput exporter, List<ItemLike> input, ItemLike output, float experience) {
		oreSmelting(exporter, input, RecipeCategory.MISC, output, experience, 200, getRecipeName(output));
		oreBlasting(exporter, input, RecipeCategory.MISC, output, experience, 100, getRecipeName(output));
	}
	
	public static void offerSmeltingAndBlasting(RecipeOutput exporter, TagKey<Item> input, ItemLike output, float experience) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, experience, 200).unlockedBy("has_" + getRecipeName(input), RecipeProvider.has(input)).save(exporter, convertBetween(output, "smelting", input));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC, output, experience, 100).unlockedBy("has_" + getRecipeName(input), RecipeProvider.has(input)).save(exporter, convertBetween(output, "blasting", input));
	}
	
	public static void offerHelmetRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).pattern("XXX").pattern("X X"), input).save(exporter);
	}
	
	public static void offerChestplateRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).pattern("X X").pattern("XXX").pattern("XXX"), input).save(exporter);
	}
	
	public static void offerLeggingsRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).pattern("XXX").pattern("X X").pattern("X X"), input).save(exporter);
	}
	
	public static void offerBootsRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).pattern("X X").pattern("X X"), input).save(exporter);
	}
	
	public static void offerPickaxeRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).define('S', Items.STICK).pattern("XXX").pattern(" S ").pattern(" S "), input).save(exporter);
	}
	
	public static void offerAxeRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).define('S', Items.STICK).pattern("XX").pattern("XS").pattern(" S"), input).save(exporter);
	}
	
	public static void offerShovelRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).define('S', Items.STICK).pattern("X").pattern("S").pattern("S"), input).save(exporter);
	}
	
	public static void offerSwordRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).define('S', Items.STICK).pattern("X").pattern("X").pattern("S"), input).save(exporter);
	}
	
	public static void offerHoeRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).define('S', Items.STICK).pattern("XX").pattern(" S").pattern(" S"), input).save(exporter);
	}
	
	public static void offerGearRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 2).define('X', input).pattern(" X ").pattern("X X").pattern(" X "), input).save(exporter);
	}
	
	public static void offerPlateRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).pattern("X").pattern("X"), input).save(exporter);
	}
	
	public static void offerMaterialAppleRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('X', input).define('A', Items.APPLE).pattern("XXX").pattern("XAX").pattern("XXX"), input).save(exporter);
	}
	
	public static void offer2x2CompactingRecipe(RecipeOutput exporter, ItemLike input, ItemLike output) {
		withCriterion(create2x2CompactingRecipe(input, output), input).save(exporter);
	}
	
	public static void offer2x2CompactingRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(create2x2CompactingRecipe(input, output), input).save(exporter);
	}
	
	public static ShapedRecipeBuilder create2x2CompactingRecipe(ItemLike input, ItemLike output) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('#', input).pattern("##").pattern("##");
	}
	
	public static ShapedRecipeBuilder create2x2CompactingRecipe(TagKey<Item> input, ItemLike output) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('#', input).pattern("##").pattern("##");
	}
	
	public static void offerBricksRecipe(RecipeOutput exporter, ItemLike input, ItemLike output) {
		withCriterion(createBricksRecipe(input, output), input).save(exporter);
	}
	
	public static void offerBricksRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(createBricksRecipe(input, output), input).save(exporter);
	}
	
	public static ShapedRecipeBuilder createBricksRecipe(ItemLike input, ItemLike output) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 4).define('#', input).pattern("##").pattern("##");
	}
	
	public static ShapedRecipeBuilder createBricksRecipe(TagKey<Item> input, ItemLike output) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 4).define('#', input).pattern("##").pattern("##");
	}
	
	public static void offerCompactingRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(createCompactingRecipe(input, output), input).save(exporter);
	}
	
	public static ShapedRecipeBuilder createCompactingRecipe(ItemLike input, ItemLike output) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('#', input).pattern("###").pattern("###").pattern("###");
	}
	
	public static ShapedRecipeBuilder createCompactingRecipe(TagKey<Item> input, ItemLike output) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('#', input).pattern("###").pattern("###").pattern("###");
	}
	
	public static void offerReverseCompactingRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		withCriterion(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 9).requires(input), input).save(exporter);
	}
	
	public static void offerCompactingRecipeWithFullName(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		offerWithName(exporter, withCriterion(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output).define('#', input).pattern("###").pattern("###").pattern("###"), input), convertBetween(output, input));
	}
	
	public static void offerReverseCompactingRecipeWithFullName(RecipeOutput exporter, TagKey<Item> input, ItemLike output) {
		offerWithName(exporter, withCriterion(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 9).requires(input), input), convertBetween(output, input));
	}
	
	public static void offerSmoothingRecipe(RecipeOutput exporter, ItemLike input, ItemLike output) {
		withCriterion(SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, 0.1f, 200), input).save(exporter);
	}
	
	public static void offerSmithingRecipe(RecipeOutput exporter, Item input, Item addition, Item output) {
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(input), Ingredient.of(addition), RecipeCategory.COMBAT, output).unlocks("has_" + getRecipeName(addition), RecipeProvider.has(addition)).save(exporter, convertBetween(output, "smithing", input));
	}
	
	public static TrituratingRecipeJsonFactory createTrituratingRecipe(TagKey<Item> input, ItemLike output, int outputCount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createTriturating(Ingredient.of(input), output, outputCount, processingTime, energy);
	}
	
	public static TrituratingRecipeJsonFactory createTrituratingRecipe(ItemLike input, ItemLike output, int outputCount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createTriturating(Ingredient.of(input), output, outputCount, processingTime, energy);
	}
	
	public static void offerTrituratingRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output, int outputCount, int processingTime, int energy) {
		createTrituratingRecipe(input, output, outputCount, processingTime, energy).save(exporter, convertBetween(output, "triturating", input));
	}
	
	public static void offerTrituratingRecipe(RecipeOutput exporter, ItemLike input, ItemLike output, int outputCount, int processingTime, int energy) {
		createTrituratingRecipe(input, output, outputCount, processingTime, energy).save(exporter, convertBetween(output, "triturating", input));
	}
	
	public static PressingRecipeJsonFactory createPressingRecipe(TagKey<Item> input, ItemLike output, int outputCount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createPressing(Ingredient.of(input), output, outputCount, processingTime, energy);
	}
	
	public static PressingRecipeJsonFactory createPressingRecipe(ItemLike input, ItemLike output, int outputCount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createPressing(Ingredient.of(input), output, outputCount, processingTime, energy);
	}
	
	public static void offerPressingRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output, int outputCount, int processingTime, int energy) {
		createPressingRecipe(input, output, outputCount, processingTime, energy).save(exporter, convertBetween(output, "pressing", input));
	}
	
	public static void offerPressingRecipe(RecipeOutput exporter, ItemLike input, ItemLike output, int outputCount, int processingTime, int energy) {
		createPressingRecipe(input, output, outputCount, processingTime, energy).save(exporter, convertBetween(output, "pressing", input));
	}
	
	public static WireMillingRecipeJsonFactory createWireMillingRecipe(TagKey<Item> input, ItemLike output, int outputCount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createWireMilling(Ingredient.of(input), output, outputCount, processingTime, energy);
	}
	
	public static WireMillingRecipeJsonFactory createWireMillingRecipe(ItemLike input, ItemLike output, int outputCount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createWireMilling(Ingredient.of(input), output, outputCount, processingTime, energy);
	}
	
	public static void offerWireMillingRecipe(RecipeOutput exporter, TagKey<Item> input, ItemLike output, int outputCount, int processingTime, int energy) {
		createWireMillingRecipe(input, output, outputCount, processingTime, energy).save(exporter, convertBetween(output, "wire_milling", input));
	}
	
	public static void offerWireMillingRecipe(RecipeOutput exporter, ItemLike input, ItemLike output, int outputCount, int processingTime, int energy) {
		createWireMillingRecipe(input, output, outputCount, processingTime, energy).save(exporter, convertBetween(output, "wire_milling", input));
	}
	
	public static AlloySmeltingRecipeJsonFactory createAlloySmeltingRecipe(TagKey<Item> firstInput, int firstCount, TagKey<Item> secondInput, int secondCount, ItemLike output, int outputCount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createAlloySmelting(Ingredient.of(firstInput), firstCount, Ingredient.of(secondInput), secondCount, output, outputCount, processingTime, energy);
	}
	
	public static AlloySmeltingRecipeJsonFactory createAlloySmeltingRecipe(ItemLike firstInput, int firstCount, ItemLike secondInput, int secondCount, ItemLike output, int outputCount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createAlloySmelting(Ingredient.of(firstInput), firstCount, Ingredient.of(secondInput), secondCount, output, outputCount, processingTime, energy);
	}
	
	public static void offerAlloySmeltingRecipe(RecipeOutput exporter, TagKey<Item> firstInput, int firstCount, TagKey<Item> secondInput, int secondCount, ItemLike output, int outputCount, int processingTime, int energy) {
		createAlloySmeltingRecipe(firstInput, firstCount, secondInput, secondCount, output, outputCount, processingTime, energy).save(exporter, convertBetween(output, "alloy_smelting", getRecipeName(firstInput) + "_and_" + getRecipeName(secondInput)));
	}
	
	public static void offerAlloySmeltingRecipe(RecipeOutput exporter, ItemLike firstInput, int firstCount, ItemLike secondInput, int secondCount, ItemLike output, int outputCount, int processingTime, int energy) {
		createAlloySmeltingRecipe(firstInput, firstCount, secondInput, secondCount, output, outputCount, processingTime, energy).save(exporter, convertBetween(output, "alloy_smelting", getRecipeName(firstInput) + "_and_" + getRecipeName(secondInput)));
	}
	
	public static MeltingRecipeJsonFactory createMeltingRecipe(TagKey<Item> input, Fluid output, long outputAmount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createMelting(Ingredient.of(input), output, outputAmount, processingTime, energy);
	}
	
	public static MeltingRecipeJsonFactory createMeltingRecipe(ItemLike input, Fluid output, long outputAmount, int processingTime, int energy) {
		return MachineRecipeJsonFactory.createMelting(Ingredient.of(input), output, outputAmount, processingTime, energy);
	}
	
	public static void offerMeltingRecipe(RecipeOutput exporter, TagKey<Item> input, Fluid output, long outputAmount, int processingTime, int energy) {
		createMeltingRecipe(input, output, outputAmount, processingTime, energy).save(exporter, BuiltInRegistries.FLUID.getKey(output).getPath() + "_from_melting_" + getRecipeName(input));
	}
	
	public static void offerMeltingRecipe(RecipeOutput exporter, ItemLike input, Fluid output, long outputAmount, int processingTime, int energy) {
		createMeltingRecipe(input, output, outputAmount, processingTime, energy).save(exporter, BuiltInRegistries.FLUID.getKey(output).getPath() + "_from_melting_" + getRecipeName(input));
	}
	
	public static RecipeBuilder withCriterion(RecipeBuilder factory, ItemLike input) {
		return factory.unlockedBy("has_" + getRecipeName(input), RecipeProvider.has(input));
	}
	
	public static RecipeBuilder withCriterion(RecipeBuilder factory, TagKey<Item> input) {
		return factory.unlockedBy("has_" + getRecipeName(input), RecipeProvider.has(input));
	}
	
	public static void offerWithName(RecipeOutput exporter, RecipeBuilder factory, String name) {
		factory.save(exporter, AMCommon.id(name));
	}
	
	public static void generateFamily(RecipeOutput exporter, BlockFamily family) {
		RecipeProvider.generateRecipes(exporter, family, FeatureFlagSet.of(FeatureFlags.VANILLA));
		STONECUTTING_OFFERERS.forEach((variant, offerer) -> {
			if (family.getVariants().containsKey(variant)) {
				offerer.accept(exporter, family.get(variant), family.getBaseBlock());
			}
		});
	}
	
	@Override
	protected void buildRecipes(RecipeOutput exporter) {
		AMBlockFamilies.getFamilies().filter(BlockFamily::shouldGenerateRecipe).forEach(family -> generateFamily(exporter, family));
		
		STONECUT_FAMILIES.forEach(map -> map.forEach((originalFamily, cutFamily) -> {
			offerStonecuttingRecipe(exporter, cutFamily.getBaseBlock(), originalFamily.getBaseBlock());
			STONECUTTING_OFFERERS.forEach((variant, offerer) -> {
				if (cutFamily.getVariants().containsKey(variant)) {
					offerer.accept(exporter, cutFamily.get(variant), originalFamily.getBaseBlock());
				}
			});
			originalFamily.getVariants().forEach((variant, block) -> {
				if (cutFamily.getVariants().containsKey(variant)) {
					offerStonecuttingRecipe(exporter, cutFamily.get(variant), block);
				}
			});
		}));
		
		POLISHED_TO_BRICK.forEach((polishedFamily, brickFamily) -> offerBricksRecipe(exporter, polishedFamily.getBaseBlock(), brickFamily.getBaseBlock()));
		
		REGULAR_TO_SMOOTH.forEach((regular, smooth) -> offerSmoothingRecipe(exporter, regular, smooth));
		
		AMMaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateRecipes).forEach((family) -> {
			AMCommon.LOGGER.info("Offering recipes for " + family.getName());
			if (family.shouldGenerateRecipe(BlockVariant.BLOCK)) {
				if (family.isBlock2x2()) {
					AMCommon.LOGGER.info("Offering 2x2 compacting recipe for base -> block");
					offer2x2CompactingRecipe(exporter, family.getBaseTag(), family.getVariant(BlockVariant.BLOCK));
				} else {
					AMCommon.LOGGER.info("Offering compacting and reverse compacting recipe for base <-> block");
					offerCompactingRecipe(exporter, family.getBaseTag(), family.getVariant(BlockVariant.BLOCK));
					offerReverseCompactingRecipeWithFullName(exporter, family.getItemTag(BlockVariant.BLOCK), family.getBaseItem());
				}
			}
			if (family.shouldGenerateRecipe(ItemVariant.NUGGET, family.getBaseVariant())) {
				AMCommon.LOGGER.info("Offering compacting and reverse compacting recipe for base <-> nugget");
				offerCompactingRecipeWithFullName(exporter, family.getTag(ItemVariant.NUGGET), family.getBaseItem());
				offerReverseCompactingRecipe(exporter, family.getBaseTag(), family.getVariant(ItemVariant.NUGGET));
			}
			if (family.shouldGenerateRecipe(ItemVariant.TINY_DUST, ItemVariant.DUST)) {
				AMCommon.LOGGER.info("Offering compacting and reverse compacting recipe for dust <-> tiny dust");
				offerCompactingRecipeWithFullName(exporter, family.getTag(ItemVariant.TINY_DUST), family.getVariant(ItemVariant.DUST));
				offerReverseCompactingRecipe(exporter, family.getTag(ItemVariant.DUST), family.getVariant(ItemVariant.TINY_DUST));
			}
			if (family.shouldGenerateRecipe(ItemVariant.RAW_ORE, BlockVariant.RAW_ORE_BLOCK)) {
				AMCommon.LOGGER.info("Offering compacting and reverse compacting recipe for raw ore <-> raw ore block");
				offerCompactingRecipe(exporter, family.getTag(ItemVariant.RAW_ORE), family.getVariant(BlockVariant.RAW_ORE_BLOCK));
				offerReverseCompactingRecipeWithFullName(exporter, family.getItemTag(BlockVariant.RAW_ORE_BLOCK), family.getVariant(ItemVariant.RAW_ORE));
			}
			if (family.hasAnyBlockVariants(AMDatagenLists.BlockVariantLists.ORE_VARIANTS)) {
				AMCommon.LOGGER.info("Offering smelting and blasting for ores -> base");
				if (family.areAnyBlockVariantsVanilla(AMDatagenLists.BlockVariantLists.ORE_VARIANTS)) {
					AMDatagenLists.BlockVariantLists.ORE_VARIANTS.stream().filter(family::isVariantAstromine).forEach((variant) -> {
						offerSmeltingAndBlasting(exporter, family.getItemTag(variant), family.getBaseItem(), family.getOreSmeltingExperience());
					});
				} else {
					offerSmeltingAndBlasting(exporter, family.getItemTag("ores"), family.getBaseItem(), family.getOreSmeltingExperience());
				}
			}
			if (family.hasAnyItemVariants(AMDatagenLists.ItemVariantLists.CLUSTER_VARIANTS)) {
				AMCommon.LOGGER.info("Offering smelting and blasting for clusters -> base");
				offerSmeltingAndBlasting(exporter, family.getItemTag("clusters"), family.getBaseItem(), family.getOreSmeltingExperience());
			}
			if (family.shouldGenerateRecipe(ItemVariant.RAW_ORE, family.getBaseVariant())) {
				AMCommon.LOGGER.info("Offering smelting and blasting for raw ores -> base");
				offerSmeltingAndBlasting(exporter, family.getTag(ItemVariant.RAW_ORE), family.getBaseItem(), family.getOreSmeltingExperience());
			}
			if (!family.getType().equals(MaterialType.MISC) && !family.getType().equals(MaterialType.DUST)) {
				if (family.shouldGenerateRecipe(ItemVariant.DUST, family.getBaseVariant())) {
					AMCommon.LOGGER.info("Offering smelting and blasting for dust -> base");
					offerSmeltingAndBlasting(exporter, family.getTag(ItemVariant.DUST), family.getBaseItem(), 0.0f);
				}
				if (family.shouldGenerateRecipe(ItemVariant.TINY_DUST, ItemVariant.NUGGET)) {
					AMCommon.LOGGER.info("Offering smelting and blasting for tiny dust -> nugget");
					offerSmeltingAndBlasting(exporter, family.getTag(ItemVariant.TINY_DUST), family.getVariant(ItemVariant.NUGGET), 0.0f);
				}
			}
			if (family.hasAnyItemVariants(AMDatagenLists.ItemVariantLists.EQUIPMENT_VARIANTS)) {
				if (family.hasVariant(ItemVariant.NUGGET)) {
					AMCommon.LOGGER.info("Offering smelting and blasting for salvagables -> nugget");
				}
				offerSmeltingAndBlasting(exporter, family.getItemTag("salvageables"), family.getVariant(ItemVariant.NUGGET), 0.1f);
				if (family.hasVariant(ItemVariant.TINY_DUST)) {
					AMCommon.LOGGER.info("Offering triturating for salvagables -> tiny dusts");
				}
				offerTrituratingRecipe(exporter, family.getItemTag("salvageables"), family.getVariant(ItemVariant.TINY_DUST), 2, SALVAGE_TIME, SALVAGE_ENERGY);
			}
			if (family.usesSmithing()) {
				AMCommon.LOGGER.info("Offering smithing recipes for equipment");
				var smithingBase = family.getSmithingBase().orElse(AMMaterialFamilies.DIAMOND);
				AMDatagenLists.ItemVariantLists.EQUIPMENT_VARIANTS.forEach((variant) -> {
					if (family.shouldGenerateRecipe(variant)) {
						offerSmithingRecipe(exporter, smithingBase.getVariant(variant), family.getBaseItem(), family.getVariant(variant));
					}
				});
			} else {
				AMCommon.LOGGER.info("Offering crafting recipes for equipment");
				EQUIPMENT_OFFERERS.forEach((variant, offerer) -> {
					if (family.shouldGenerateRecipe(variant)) {
						offerer.accept(exporter, family.getBaseTag(), family.getVariant(variant));
					}
				});
			}
			MISC_OFFERERS.forEach((variant, offerer) -> {
				if (family.shouldGenerateRecipe(variant)) {
					AMCommon.LOGGER.info("Offering recipe for base -> " + variant.getName());
					offerer.accept(exporter, family.getBaseTag(), family.getVariant(variant));
				}
			});
			
			if (family.hasVariant(ItemVariant.DUST)) {
				if (!family.getType().equals(MaterialType.DUST)) {
					AMCommon.LOGGER.info("Offering triturating for base -> dust");
					offerTrituratingRecipe(exporter, family.getBaseTag(), family.getVariant(ItemVariant.DUST), 1, MATERIAL_PROCESSING_TIME, MATERIAL_PROCESSING_ENERGY);
				}
				if (family.hasVariant(BlockVariant.BLOCK)) {
					AMCommon.LOGGER.info("Offering triturating for block -> dusts");
					var outputCount = family.isBlock2x2() ? 4 : 9;
					var time = family.isBlock2x2() ? BLOCK_2X2_PROCESSING_TIME : BLOCK_3X3_PROCESSING_TIME;
					var energy = family.isBlock2x2() ? BLOCK_2X2_PROCESSING_ENERGY : BLOCK_3X3_PROCESSING_ENERGY;
					if (family.shouldGenerateTags()) {
						offerTrituratingRecipe(exporter, family.getItemTag(BlockVariant.BLOCK), family.getVariant(ItemVariant.DUST), outputCount, time, energy);
					} else {
						offerTrituratingRecipe(exporter, family.getVariant(BlockVariant.BLOCK), family.getVariant(ItemVariant.DUST), outputCount, time, energy);
					}
				}
				if (family.hasVariant(ItemVariant.RAW_ORE)) {
					AMCommon.LOGGER.info("Offering triturating for raw ore -> dusts");
					offerTrituratingRecipe(exporter, family.getTag(ItemVariant.RAW_ORE), family.getVariant(ItemVariant.DUST), 2, CLUSTER_PROCESSING_TIME, CLUSTER_PROCESSING_ENERGY);
					offerTrituratingRecipe(exporter, family.getItemTag(BlockVariant.RAW_ORE_BLOCK), family.getVariant(ItemVariant.DUST), 18, BLOCK_3X3_PROCESSING_TIME * 2, BLOCK_3X3_PROCESSING_ENERGY * 2);
				} else {
					if (family.hasAnyBlockVariants(AMDatagenLists.BlockVariantLists.ORE_VARIANTS)) {
						AMCommon.LOGGER.info("Offering triturating for ores -> dusts");
						offerTrituratingRecipe(exporter, family.getItemTag("ores"), family.getVariant(ItemVariant.DUST), 2, ORE_PROCESSING_TIME, ORE_PROCESSING_ENERGY);
					}
					if (family.hasAnyItemVariants(AMDatagenLists.ItemVariantLists.CLUSTER_VARIANTS)) {
						AMCommon.LOGGER.info("Offering triturating for clusters -> dusts");
						offerTrituratingRecipe(exporter, family.getItemTag("clusters"), family.getVariant(ItemVariant.DUST), 2, CLUSTER_PROCESSING_TIME, CLUSTER_PROCESSING_ENERGY);
					}
				}
			}
			
			if (family.hasVariant(ItemVariant.RAW_ORE)) {
				if (family.hasAnyBlockVariants(AMDatagenLists.BlockVariantLists.ORE_VARIANTS)) {
					AMCommon.LOGGER.info("Offering triturating for ores -> raw ores");
					offerTrituratingRecipe(exporter, family.getItemTag("ores"), family.getVariant(ItemVariant.RAW_ORE), 2, ORE_PROCESSING_TIME, ORE_PROCESSING_ENERGY);
				}
				if (family.hasAnyItemVariants(AMDatagenLists.ItemVariantLists.CLUSTER_VARIANTS)) {
					AMCommon.LOGGER.info("Offering triturating for clusters -> raw ores");
					offerTrituratingRecipe(exporter, family.getItemTag("clusters"), family.getVariant(ItemVariant.RAW_ORE), 2, CLUSTER_PROCESSING_TIME, CLUSTER_PROCESSING_ENERGY);
				}
			}
			
			if (family.shouldGenerateRecipe(ItemVariant.NUGGET, ItemVariant.TINY_DUST)) {
				AMCommon.LOGGER.info("Offering triturating for nugget -> tiny dust");
				offerTrituratingRecipe(exporter, family.getTag(ItemVariant.NUGGET), family.getVariant(ItemVariant.TINY_DUST), 1, TINY_DUST_TIME, TINY_DUST_ENERGY);
			}
			
			if (family.shouldGenerateRecipe(ItemVariant.PLATE)) {
				AMCommon.LOGGER.info("Offering pressing for base -> plate");
				offerPressingRecipe(exporter, family.getBaseTag(), family.getVariant(ItemVariant.PLATE), 1, MATERIAL_PROCESSING_TIME, MATERIAL_PROCESSING_ENERGY);
				if (family.hasVariant(BlockVariant.BLOCK)) {
					AMCommon.LOGGER.info("Offering pressing for block -> plates");
					offerPressingRecipe(exporter, family.getItemTag(BlockVariant.BLOCK), family.getVariant(ItemVariant.PLATE), family.isBlock2x2() ? 4 : 9, family.isBlock2x2() ? BLOCK_2X2_PROCESSING_TIME : BLOCK_3X3_PROCESSING_TIME, family.isBlock2x2() ? BLOCK_2X2_PROCESSING_ENERGY : BLOCK_3X3_PROCESSING_ENERGY);
				}
			}
			
			if (family.shouldGenerateRecipe(ItemVariant.WIRE)) {
				AMCommon.LOGGER.info("Offering wire milling for base -> wires");
				offerWireMillingRecipe(exporter, family.getBaseTag(), family.getVariant(ItemVariant.WIRE), 3, MATERIAL_PROCESSING_TIME, MATERIAL_PROCESSING_ENERGY);
			}
			
			if (family.isAlloy()) {
				family.getAlloyInfos().forEach((alloyInfo -> {
					var first = alloyInfo.firstIngredient();
					var second = alloyInfo.secondIngredient();
					AMCommon.LOGGER.info("Offering alloy smelting for base + base -> base");
					offerAlloySmeltingRecipe(exporter, first.getBaseTag(), alloyInfo.firstCount(), second.getBaseTag(), alloyInfo.secondCount(), family.getBaseItem(), alloyInfo.outputCount(), alloyInfo.time(), alloyInfo.energy());
					if (family.hasVariant(ItemVariant.NUGGET) && first.hasVariant(ItemVariant.NUGGET) && second.hasVariant(ItemVariant.NUGGET)) {
						AMCommon.LOGGER.info("Offering alloy smelting for nugget + nugget -> nugget");
						offerAlloySmeltingRecipe(exporter, alloyInfo.firstIngredient().getTag(ItemVariant.NUGGET), alloyInfo.firstCount(), alloyInfo.secondIngredient().getTag(ItemVariant.NUGGET), alloyInfo.secondCount(), family.getVariant(ItemVariant.NUGGET), alloyInfo.outputCount(), alloyInfo.time() / 8, alloyInfo.energy() / 8);
					}
					if (family.hasVariant(ItemVariant.DUST) && first.hasVariant(ItemVariant.DUST) && second.hasVariant(ItemVariant.DUST)) {
						AMCommon.LOGGER.info("Offering alloy smelting for dust + dust -> dust");
						offerAlloySmeltingRecipe(exporter, alloyInfo.firstIngredient().getTag(ItemVariant.DUST), alloyInfo.firstCount(), alloyInfo.secondIngredient().getTag(ItemVariant.DUST), alloyInfo.secondCount(), family.getVariant(ItemVariant.DUST), alloyInfo.outputCount(), alloyInfo.time(), alloyInfo.energy());
					}
					if (family.hasVariant(ItemVariant.TINY_DUST) && first.hasVariant(ItemVariant.TINY_DUST) && second.hasVariant(ItemVariant.TINY_DUST)) {
						AMCommon.LOGGER.info("Offering alloy smelting for tiny dust + tiny dust -> tiny dust");
						offerAlloySmeltingRecipe(exporter, alloyInfo.firstIngredient().getTag(ItemVariant.TINY_DUST), alloyInfo.firstCount(), alloyInfo.secondIngredient().getTag(ItemVariant.TINY_DUST), alloyInfo.secondCount(), family.getVariant(ItemVariant.TINY_DUST), alloyInfo.outputCount(), alloyInfo.time() / 8, alloyInfo.energy() / 8);
					}
				}));
			}
			
			if (family.hasMoltenFluid()) {
				family.getItemVariants().forEach((variant, item) -> {
					AMCommon.LOGGER.info("Offering melting for " + variant.getName() + " to molten fluid");
					offerMeltingRecipe(exporter, family.getTag(variant), family.getMoltenFluid().get().getSource(), variant.getMeltedFluidAmount(family), variant.getMeltingTime(family), variant.getMeltingEnergy(family));
				});
				family.getBlockVariants().forEach((variant, block) -> {
					AMCommon.LOGGER.info("Offering melting for " + variant.getName() + " to molten fluid");
					offerMeltingRecipe(exporter, family.getItemTag(variant), family.getMoltenFluid().get().getSource(), variant.getMeltedFluidAmount(family), variant.getMeltingTime(family), variant.getMeltingEnergy(family));
				});
			}
			
			AMCommon.LOGGER.info("Finished offering recipes for " + family.getName());
		});
		
		offerTrituratingRecipe(exporter, Items.BLAZE_ROD, Items.BLAZE_POWDER, 4, MATERIAL_PROCESSING_TIME, MATERIAL_PROCESSING_ENERGY);
		offerTrituratingRecipe(exporter, Items.BONE, Items.BONE_MEAL, 4, MATERIAL_PROCESSING_TIME, MATERIAL_PROCESSING_ENERGY);
		offerTrituratingRecipe(exporter, Items.SUGAR_CANE, Items.SUGAR, 2, MATERIAL_PROCESSING_TIME, MATERIAL_PROCESSING_ENERGY);
		offerTrituratingRecipe(exporter, AMTagKeys.ItemTags.YELLOW_SANDSTONES, Blocks.SAND, 4, STONE_GRINDING_TIME, STONE_GRINDING_ENERGY);
		offerTrituratingRecipe(exporter, AMTagKeys.ItemTags.RED_SANDSTONES, Blocks.RED_SAND, 4, STONE_GRINDING_TIME, STONE_GRINDING_ENERGY);
		offerTrituratingRecipe(exporter, AMTagKeys.ItemTags.CUT_COPPER, AMItems.COPPER_DUST.get(), 1, STONE_GRINDING_TIME, STONE_GRINDING_ENERGY);
		offerTrituratingRecipe(exporter, AMTagKeys.ItemTags.PURPUR_BLOCKS, Items.POPPED_CHORUS_FRUIT, 4, SIMPLE_GRINDING_TIME, SIMPLE_GRINDING_ENERGY);
		offerTrituratingRecipe(exporter, net.minecraft.tags.ItemTags.WOOL, Items.STRING, 4, SIMPLE_GRINDING_TIME, SIMPLE_GRINDING_ENERGY);
		
		TRITURATED_BLOCK_FAMILIES.forEach((inputFamily, outputFamily) -> {
			inputFamily.getVariants().forEach((variant, block) -> {
				if (outputFamily.getVariants().containsKey(variant)) {
					offerTrituratingRecipe(exporter, block, outputFamily.get(variant), 1, SIMPLE_GRINDING_TIME, SIMPLE_GRINDING_ENERGY);
				}
			});
		});
		
		TRITURATED_BLOCKS_CHEAP.forEach((map, count) -> {
			map.forEach((input, output) -> {
				offerTrituratingRecipe(exporter, input, output, count, SIMPLE_GRINDING_TIME, SIMPLE_GRINDING_ENERGY);
			});
		});
		
		TRITURATED_BLOCKS_1_TO_1_EXPENSIVE.forEach((input, output) -> {
			offerTrituratingRecipe(exporter, input, output, 1, HEAVY_GRINDING_TIME, HEAVY_GRINDING_ENERGY);
		});
		
		BIOFUEL_TAGS.forEach((tag, count) -> {
			offerTrituratingRecipe(exporter, tag, AMItems.BIOFUEL.get(), count, BIOFUEL_GRINDING_BASE_TIME + (BIOFUEL_GRINDING_TIME_PER_OUTPUT * count), BIOFUEL_GRINDING_ENERGY_PER_OUTPUT * count);
		});
		
		offerMeltingRecipe(exporter, AMTagKeys.ItemTags.BIOFUEL, AMFluids.BIOMASS.getSource(), BIOFUEL_TO_BIOMASS_AMOUNT, BIOFUEL_TO_BIOMASS_TIME, BIOFUEL_TO_BIOMASS_ENERGY);
	}
	
	@FunctionalInterface
	public interface BasicOfferer extends TriConsumer<RecipeOutput, ItemLike, ItemLike> {
	}
	
	@FunctionalInterface
	public interface TagOfferer extends TriConsumer<RecipeOutput, TagKey<Item>, ItemLike> {
	}
}
