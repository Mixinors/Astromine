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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;

public class AMTagKeys {
	public static final String COMMON_TAG_NAMESPACE = "c";
	
	public static final ResourceLocation IS_MOON_ID = createCommonTagId("is_moon");
	
	public static final ResourceLocation INFINIBURN_SPACE_ID = createCommonTagId("infiniburn_space");
	public static final ResourceLocation YELLOW_SANDSTONES_ID = createCommonTagId("yellow_sandstones");
	public static final ResourceLocation RED_SANDSTONES_ID = createCommonTagId("red_sandstones");
	public static final ResourceLocation SANDSTONES_ID = createCommonTagId("sandstones");
	public static final ResourceLocation QUARTZ_BLOCKS_ID = createCommonTagId("quartz_blocks");
	public static final ResourceLocation UNWAXED_COPPER_BLOCKS_ID = createCommonTagId("unwaxed_copper_blocks");
	public static final ResourceLocation WAXED_COPPER_BLOCKS_ID = createCommonTagId("waxed_copper_blocks");
	public static final ResourceLocation COPPER_BLOCKS_ID = createCommonTagId("copper_blocks");
	public static final ResourceLocation UNWAXED_CUT_COPPER_ID = createCommonTagId("unwaxed_cut_copper");
	public static final ResourceLocation WAXED_CUT_COPPER_ID = createCommonTagId("waxed_cut_copper");
	public static final ResourceLocation CUT_COPPER_ID = createCommonTagId("cut_copper");
	public static final ResourceLocation PURPUR_BLOCKS_ID = createCommonTagId("purpur_blocks");
	public static final ResourceLocation MUSHROOMS_ID = createCommonTagId("mushrooms");
	public static final ResourceLocation MUSHROOM_BLOCKS_ID = createCommonTagId("mushroom_blocks");
	public static final ResourceLocation NETHER_FUNGI_ID = createCommonTagId("nether_fungi");
	public static final ResourceLocation NETHER_ROOTS_ID = createCommonTagId("nether_roots");
	public static final ResourceLocation NETHER_VINES_ID = createCommonTagId("nether_vines");
	public static final ResourceLocation PUMPKINS_ID = createCommonTagId("pumpkins");
	public static final ResourceLocation GOURDS_ID = createCommonTagId("gourds");
	public static final ResourceLocation WEEPING_VINES_ID = createCommonTagId("weeping_vines");
	public static final ResourceLocation TWISTING_VINES_ID = createCommonTagId("twisting_vines");
	
	public static final ResourceLocation MOON_STONE_ID = createCommonTagId("moon_stone");
	public static final ResourceLocation DARK_MOON_STONE_ID = createCommonTagId("dark_moon_stone");
	
	public static class BiomeTags {
		public static final TagKey<Biome> IS_MOON = createBiomeTag(IS_MOON_ID);
	}
	
	public static class BlockTags {
		public static final TagKey<Block> INFINIBURN_SPACE = createBlockTag(INFINIBURN_SPACE_ID);
		public static final TagKey<Block> YELLOW_SANDSTONES = createBlockTag(YELLOW_SANDSTONES_ID);
		public static final TagKey<Block> RED_SANDSTONES = createBlockTag(RED_SANDSTONES_ID);
		public static final TagKey<Block> SANDSTONES = createBlockTag(SANDSTONES_ID);
		public static final TagKey<Block> QUARTZ_BLOCKS = createBlockTag(QUARTZ_BLOCKS_ID);
		public static final TagKey<Block> UNWAXED_COPPER_BLOCKS = createBlockTag(UNWAXED_COPPER_BLOCKS_ID);
		public static final TagKey<Block> WAXED_COPPER_BLOCKS = createBlockTag(WAXED_COPPER_BLOCKS_ID);
		public static final TagKey<Block> COPPER_BLOCKS = createBlockTag(COPPER_BLOCKS_ID);
		public static final TagKey<Block> UNWAXED_CUT_COPPER = createBlockTag(UNWAXED_CUT_COPPER_ID);
		public static final TagKey<Block> WAXED_CUT_COPPER = createBlockTag(WAXED_CUT_COPPER_ID);
		public static final TagKey<Block> CUT_COPPER = createBlockTag(CUT_COPPER_ID);
		public static final TagKey<Block> PURPUR_BLOCKS = createBlockTag(PURPUR_BLOCKS_ID);
		public static final TagKey<Block> MUSHROOMS = createBlockTag(MUSHROOMS_ID);
		public static final TagKey<Block> MUSHROOM_BLOCKS = createBlockTag(MUSHROOM_BLOCKS_ID);
		public static final TagKey<Block> NETHER_FUNGI = createBlockTag(NETHER_FUNGI_ID);
		public static final TagKey<Block> NETHER_ROOTS = createBlockTag(NETHER_ROOTS_ID);
		public static final TagKey<Block> NETHER_VINES = createBlockTag(NETHER_VINES_ID);
		public static final TagKey<Block> PUMPKINS = createBlockTag(PUMPKINS_ID);
		public static final TagKey<Block> GOURDS = createBlockTag(GOURDS_ID);
		public static final TagKey<Block> WEEPING_VINES = createBlockTag(WEEPING_VINES_ID);
		public static final TagKey<Block> TWISTING_VINES = createBlockTag(TWISTING_VINES_ID);
		
		public static final TagKey<Block> MOON_STONE = createBlockTag(MOON_STONE_ID);
		public static final TagKey<Block> DARK_MOON_STONE = createBlockTag(DARK_MOON_STONE_ID);
		
		public static final TagKey<Block> DRILL_MINEABLE = createBlockTag("mineable/drill");
	}
	
	public static class ItemTags {
		public static final TagKey<Item> YELLOW_SANDSTONES = createItemTag(YELLOW_SANDSTONES_ID);
		public static final TagKey<Item> RED_SANDSTONES = createItemTag(RED_SANDSTONES_ID);
		public static final TagKey<Item> SANDSTONES = createItemTag(SANDSTONES_ID);
		public static final TagKey<Item> QUARTZ_BLOCKS = createItemTag(QUARTZ_BLOCKS_ID);
		public static final TagKey<Item> UNWAXED_COPPER_BLOCKS = createItemTag(UNWAXED_COPPER_BLOCKS_ID);
		public static final TagKey<Item> WAXED_COPPER_BLOCKS = createItemTag(WAXED_COPPER_BLOCKS_ID);
		public static final TagKey<Item> COPPER_BLOCKS = createItemTag(COPPER_BLOCKS_ID);
		public static final TagKey<Item> UNWAXED_CUT_COPPER = createItemTag(UNWAXED_CUT_COPPER_ID);
		public static final TagKey<Item> WAXED_CUT_COPPER = createItemTag(WAXED_CUT_COPPER_ID);
		public static final TagKey<Item> CUT_COPPER = createItemTag(CUT_COPPER_ID);
		public static final TagKey<Item> PURPUR_BLOCKS = createItemTag(PURPUR_BLOCKS_ID);
		public static final TagKey<Item> MUSHROOMS = createItemTag(MUSHROOMS_ID);
		public static final TagKey<Item> MUSHROOM_BLOCKS = createItemTag(MUSHROOM_BLOCKS_ID);
		public static final TagKey<Item> NETHER_FUNGI = createItemTag(NETHER_FUNGI_ID);
		public static final TagKey<Item> NETHER_ROOTS = createItemTag(NETHER_ROOTS_ID);
		public static final TagKey<Item> NETHER_VINES = createItemTag(NETHER_VINES_ID);
		public static final TagKey<Item> PUMPKINS = createItemTag(PUMPKINS_ID);
		public static final TagKey<Item> GOURDS = createItemTag(GOURDS_ID);
		public static final TagKey<Item> WEEPING_VINES = createItemTag(WEEPING_VINES_ID);
		public static final TagKey<Item> TWISTING_VINES = createItemTag(TWISTING_VINES_ID);
		
		public static final TagKey<Item> MAKES_ONE_BIOFUEL = createItemTag("makes_one_biofuel");
		public static final TagKey<Item> MAKES_TWO_BIOFUEL = createItemTag("makes_two_biofuel");
		public static final TagKey<Item> MAKES_FOUR_BIOFUEL = createItemTag("makes_four_biofuel");
		public static final TagKey<Item> MAKES_NINE_BIOFUEL = createItemTag("makes_nine_biofuel");
		
		public static final TagKey<Item> BIOFUEL = createCommonItemTag("biofuel");
		
		public static final TagKey<Item> TRICKS_PIGLINS = createItemTag("tricks_piglins");
	}
	
	public static class FluidTags {
		public static final TagKey<Fluid> OXYGEN = createCommonFluidTag("oxygen");
		
		public static final TagKey<Fluid> INDUSTRIAL_FLUIDS = createFluidTag("industrial_fluids");
		public static final TagKey<Fluid> MOLTEN_FLUIDS = createFluidTag("molten_fluids");
		public static final TagKey<Fluid> ROCKET_FUELS = createFluidTag("rocket_fuels");
	}
	
	public static class EntityTypeTags {
		public static final TagKey<EntityType<?>> FISH = createCommonEntityTypeTag("fish");
		public static final TagKey<EntityType<?>> SQUIDS = createCommonEntityTypeTag("squids");
		public static final TagKey<EntityType<?>> GUARDIANS = createCommonEntityTypeTag("guardians");
		public static final TagKey<EntityType<?>> SKELETONS = createCommonEntityTypeTag("skeletons");
		public static final TagKey<EntityType<?>> ZOMBIES = createCommonEntityTypeTag("zombies");
		public static final TagKey<EntityType<?>> SPACE_SLIMES = createCommonEntityTypeTag("space_slimes");
		
		public static final TagKey<EntityType<?>> DOES_NOT_BREATHE = createEntityTypeTag("does_not_breathe");
		public static final TagKey<EntityType<?>> CAN_BREATHE_WATER = createEntityTypeTag("can_breathe_water");
		public static final TagKey<EntityType<?>> CAN_BREATHE_LAVA = createEntityTypeTag("can_breathe_lava");
		public static final TagKey<EntityType<?>> CANNOT_BREATHE_OXYGEN = createEntityTypeTag("cannot_breathe_oxygen");
		
		public static final TagKey<EntityType<?>> IGNORES_DIMENSIONAL_LAYERS = createEntityTypeTag("ignore_dimensional_layers");
	}
	
	public static class DimensionTypeTags {
		public static final TagKey<DimensionType> IS_VACUUM = createDimensionTypeTag("is_space");
	}
	
	public static ResourceLocation createCommonTagId(String path) {
		return ResourceLocation.fromNamespaceAndPath(COMMON_TAG_NAMESPACE, path);
	}
	
	public static TagKey<Biome> createBiomeTag(ResourceLocation id) {
		return TagKey.create(Registries.BIOME, id);
	}
	
	public static TagKey<Block> createBlockTag(ResourceLocation id) {
		return TagKey.create(Registries.BLOCK, id);
	}
	
	public static TagKey<Block> createBlockTag(String path) {
		return createBlockTag(AMCommon.id(path));
	}
	
	public static TagKey<Block> createCommonBlockTag(String path) {
		return createBlockTag(createCommonTagId(path));
	}
	
	public static TagKey<Item> createItemTag(ResourceLocation id) {
		return TagKey.create(Registries.ITEM, id);
	}
	
	public static TagKey<Item> createItemTag(String path) {
		return createItemTag(AMCommon.id(path));
	}
	
	public static TagKey<Item> createCommonItemTag(String path) {
		return createItemTag(createCommonTagId(path));
	}
	
	public static TagKey<Fluid> createFluidTag(ResourceLocation id) {
		return TagKey.create(Registries.FLUID, id);
	}
	
	public static TagKey<Fluid> createFluidTag(String path) {
		return createFluidTag(AMCommon.id(path));
	}
	
	public static TagKey<Fluid> createCommonFluidTag(String path) {
		return createFluidTag(createCommonTagId(path));
	}
	
	public static TagKey<EntityType<?>> createEntityTypeTag(ResourceLocation id) {
		return TagKey.create(Registries.ENTITY_TYPE, id);
	}
	
	public static TagKey<EntityType<?>> createEntityTypeTag(String path) {
		return createEntityTypeTag(AMCommon.id(path));
	}
	
	public static TagKey<EntityType<?>> createCommonEntityTypeTag(String path) {
		return createEntityTypeTag(createCommonTagId(path));
	}
	
	public static TagKey<DimensionType> createDimensionTypeTag(ResourceLocation id) {
		return TagKey.create(Registries.DIMENSION_TYPE, id);
	}
	
	public static TagKey<DimensionType> createDimensionTypeTag(String path) {
		return createDimensionTypeTag(AMCommon.id(path));
	}
	
	public static TagKey<DimensionType> createCommonDimensionTypeTag(String path) {
		return createDimensionTypeTag(createCommonTagId(path));
	}
}
