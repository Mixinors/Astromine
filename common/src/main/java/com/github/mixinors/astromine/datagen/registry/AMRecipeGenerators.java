/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.datagen.registry;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.generator.recipe.RecipeGenerator;
import com.github.mixinors.astromine.datagen.generator.recipe.onetime.*;
import com.github.mixinors.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import com.github.mixinors.astromine.datagen.generator.recipe.set.*;
import com.github.mixinors.astromine.datagen.generator.recipe.set.base.SetRecipeGenerator;
import com.github.mixinors.astromine.datagen.generator.recipe.*;
import com.github.mixinors.astromine.datagen.generator.recipe.set.PressingSetRecipeGenerator;
import com.github.mixinors.astromine.datagen.generator.recipe.set.TrituratingSetRecipeGenerator;
import com.github.mixinors.astromine.datagen.generator.recipe.set.WireMillingSetRecipeGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMItems;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static com.github.mixinors.astromine.datagen.material.MaterialItemType.*;

public class AMRecipeGenerators {
	private final List<SetRecipeGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeRecipeGenerator> ONE_TIME_GENERATORS = new ArrayList<>();
	
	public final RecipeGenerator INGOT_TO_BLOCK = register(new Crafting3x3SetRecipeGenerator(INGOT, BLOCK));
	public final RecipeGenerator GEM_TO_BLOCK = register(new Crafting3x3SetRecipeGenerator(GEM, BLOCK));
	public final RecipeGenerator MISC_TO_BLOCK = register(new Crafting3x3SetRecipeGenerator(MISC_RESOURCE, BLOCK));
	public final RecipeGenerator NUGGET_TO_INGOT = register(new Crafting3x3SetRecipeGenerator(NUGGET, INGOT));
	public final RecipeGenerator FRAGMENT_TO_GEM = register(new Crafting3x3SetRecipeGenerator(FRAGMENT, GEM));
	public final RecipeGenerator TINY_DUST_TO_DUST = register(new Crafting3x3SetRecipeGenerator(TINY_DUST, DUST));
	
	public final RecipeGenerator INGOT_TO_BLOCK_2x2 = register(new Crafting2x2SetRecipeGenerator(INGOT, BLOCK_2x2));
	public final RecipeGenerator GEM_TO_BLOCK_2x2 = register(new Crafting2x2SetRecipeGenerator(GEM, BLOCK_2x2));
	public final RecipeGenerator MISC_TO_BLOCK_2x2 = register(new Crafting2x2SetRecipeGenerator(MISC_RESOURCE, BLOCK_2x2));
	
	public final RecipeGenerator BLOCK_TO_INGOTS = register(new ShapelessCraftingSetRecipeGenerator(BLOCK, INGOT, 9));
	public final RecipeGenerator BLOCK_TO_GEMS = register(new ShapelessCraftingSetRecipeGenerator(BLOCK, GEM, 9));
	public final RecipeGenerator BLOCK_TO_MISC = register(new ShapelessCraftingSetRecipeGenerator(BLOCK, MISC_RESOURCE, 9));
	public final RecipeGenerator INGOT_TO_NUGGETS = register(new ShapelessCraftingSetRecipeGenerator(INGOT, NUGGET, 9));
	public final RecipeGenerator GEM_TO_FRAGMENTS = register(new ShapelessCraftingSetRecipeGenerator(GEM, FRAGMENT, 9));
	public final RecipeGenerator DUST_TO_TINY_DUSTS = register(new ShapelessCraftingSetRecipeGenerator(DUST, TINY_DUST, 9));
	
	public final RecipeGenerator INGOT_TO_PICKAXE = register(new ToolCraftingRecipeGenerators.PickaxeCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_PICKAXE = register(new ToolCraftingRecipeGenerators.PickaxeCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_PICKAXE = register(new ToolCraftingRecipeGenerators.PickaxeCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_AXE = register(new ToolCraftingRecipeGenerators.AxeCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_AXE = register(new ToolCraftingRecipeGenerators.AxeCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_AXE = register(new ToolCraftingRecipeGenerators.AxeCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_SHOVEL = register(new ToolCraftingRecipeGenerators.ShovelCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_SHOVEL = register(new ToolCraftingRecipeGenerators.ShovelCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_SHOVEL = register(new ToolCraftingRecipeGenerators.ShovelCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_SWORD = register(new ToolCraftingRecipeGenerators.SwordCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_SWORD = register(new ToolCraftingRecipeGenerators.SwordCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_SWORD = register(new ToolCraftingRecipeGenerators.SwordCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_HOE = register(new ToolCraftingRecipeGenerators.HoeCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_HOE = register(new ToolCraftingRecipeGenerators.HoeCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_HOE = register(new ToolCraftingRecipeGenerators.HoeCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_MATTOCK = register(new ToolCraftingRecipeGenerators.MattockCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_MATTOCK = register(new ToolCraftingRecipeGenerators.MattockCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_MATTOCK = register(new ToolCraftingRecipeGenerators.MattockCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_MINING_TOOL = register(new ToolCraftingRecipeGenerators.MiningToolCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_MINING_TOOL = register(new ToolCraftingRecipeGenerators.MiningToolCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_MINING_TOOL = register(new ToolCraftingRecipeGenerators.MiningToolCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_HAMMER = register(new ToolCraftingRecipeGenerators.HammerCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_HAMMER = register(new ToolCraftingRecipeGenerators.HammerCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_HAMMER = register(new ToolCraftingRecipeGenerators.HammerCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_EXCAVATOR = register(new ToolCraftingRecipeGenerators.ExcavatorCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_EXCAVATOR = register(new ToolCraftingRecipeGenerators.ExcavatorCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_EXCAVATOR = register(new ToolCraftingRecipeGenerators.ExcavatorCraftingRecipeGenerator(MISC_RESOURCE));
	
	public final RecipeGenerator INGOT_TO_HELMET = register(new ArmorCraftingRecipeGenerators.HelmetCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_HELMET = register(new ArmorCraftingRecipeGenerators.HelmetCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_HELMET = register(new ArmorCraftingRecipeGenerators.HelmetCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_CHESTPLATE = register(new ArmorCraftingRecipeGenerators.ChestplateCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_CHESTPLATE = register(new ArmorCraftingRecipeGenerators.ChestplateCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_CHESTPLATE = register(new ArmorCraftingRecipeGenerators.ChestplateCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_LEGGINGS = register(new ArmorCraftingRecipeGenerators.LeggingsCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_LEGGINGS = register(new ArmorCraftingRecipeGenerators.LeggingsCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_LEGGINGS = register(new ArmorCraftingRecipeGenerators.LeggingsCraftingRecipeGenerator(MISC_RESOURCE));
	public final RecipeGenerator INGOT_TO_BOOTS = register(new ArmorCraftingRecipeGenerators.BootsCraftingRecipeGenerator(INGOT));
	public final RecipeGenerator GEM_TO_BOOTS = register(new ArmorCraftingRecipeGenerators.BootsCraftingRecipeGenerator(GEM));
	public final RecipeGenerator MISC_TO_BOOTS = register(new ArmorCraftingRecipeGenerators.BootsCraftingRecipeGenerator(MISC_RESOURCE));
	
	public final RecipeGenerator ORE_TO_INGOT = register(new SmeltingSetRecipeGenerator(ORE, INGOT));
	public final RecipeGenerator ORE_TO_GEM = register(new SmeltingSetRecipeGenerator(ORE, GEM));
	public final RecipeGenerator ORE_TO_MISC = register(new SmeltingSetRecipeGenerator(ORE, MISC_RESOURCE));
	public final RecipeGenerator METEOR_ORE_TO_NUGGET = register(new SmeltingSetRecipeGenerator(METEOR_ORE, NUGGET));
	public final RecipeGenerator METEOR_ORE_TO_FRAGMENT = register(new SmeltingSetRecipeGenerator(METEOR_ORE, FRAGMENT));
	public final RecipeGenerator DUST_TO_INGOT = register(new SmeltingSetRecipeGenerator(DUST, INGOT));
	public final RecipeGenerator DUST_TO_GEM = register(new SmeltingSetRecipeGenerator(DUST, GEM));
	public final RecipeGenerator METEOR_CLUSTER_TO_NUGGET = register(new SmeltingSetRecipeGenerator(METEOR_CLUSTER, NUGGET));
	public final RecipeGenerator METEOR_CLUSTER_TO_FRAGMENT = register(new SmeltingSetRecipeGenerator(METEOR_CLUSTER, FRAGMENT));
	public final RecipeGenerator TINY_DUST_TO_NUGGET = register(new SmeltingSetRecipeGenerator(TINY_DUST, NUGGET));
	public final RecipeGenerator TINY_DUST_TO_FRAGMENT = register(new SmeltingSetRecipeGenerator(TINY_DUST, FRAGMENT));
	
	public final RecipeGenerator ORE_TO_INGOT_BLASTING = register(new BlastingSetRecipeGenerator(ORE, INGOT));
	public final RecipeGenerator ORE_TO_GEM_BLASTING = register(new BlastingSetRecipeGenerator(ORE, GEM));
	public final RecipeGenerator ORE_TO_MISC_BLASTING = register(new BlastingSetRecipeGenerator(ORE, MISC_RESOURCE));
	public final RecipeGenerator METEOR_ORE_TO_NUGGET_BLASTING = register(new BlastingSetRecipeGenerator(METEOR_ORE, NUGGET));
	public final RecipeGenerator METEOR_ORE_TO_FRAGMENT_BLASTING = register(new BlastingSetRecipeGenerator(METEOR_ORE, FRAGMENT));
	public final RecipeGenerator DUST_TO_INGOT_BLASTING = register(new BlastingSetRecipeGenerator(DUST, INGOT));
	public final RecipeGenerator DUST_TO_GEM_BLASTING = register(new BlastingSetRecipeGenerator(DUST, GEM));
	public final RecipeGenerator METEOR_CLUSTER_TO_NUGGET_BLASTING = register(new BlastingSetRecipeGenerator(METEOR_CLUSTER, NUGGET));
	public final RecipeGenerator METEOR_CLUSTER_TO_FRAGMENT_BLASTING = register(new BlastingSetRecipeGenerator(METEOR_CLUSTER, FRAGMENT));
	public final RecipeGenerator TINY_DUST_TO_NUGGET_BLASTING = register(new BlastingSetRecipeGenerator(TINY_DUST, NUGGET));
	public final RecipeGenerator TINY_DUST_TO_FRAGMENT_BLASTING = register(new BlastingSetRecipeGenerator(TINY_DUST, FRAGMENT));
	
	public final RecipeGenerator INGOT_TO_PLATES_CRAFTING = register(new ShapedCraftingSetRecipeGenerator(INGOT, PLATE, "#", "#"));
	public final RecipeGenerator INGOT_TO_GEAR_CRAFTING = register(new ShapedCraftingSetRecipeGenerator(INGOT, GEAR, 2, " # ", "# #", " # "));
	
	public final RecipeGenerator PLATE_TO_WIRE_CRAFTING = register(new WireFromPlateRecipeGenerator(PLATE, Ingredient.fromTag(FabricToolTags.SHEARS), WIRE, 1));
	
	public final RecipeGenerator PICKAXE_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(PICKAXE, INGOT));
	public final RecipeGenerator PICKAXE_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(PICKAXE, GEM));
	public final RecipeGenerator AXE_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(AXE, INGOT));
	public final RecipeGenerator AXE_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(AXE, GEM));
	public final RecipeGenerator SHOVEL_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(SHOVEL, INGOT));
	public final RecipeGenerator SHOVEL_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(SHOVEL, GEM));
	public final RecipeGenerator SWORD_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(SWORD, INGOT));
	public final RecipeGenerator SWORD_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(SWORD, GEM));
	public final RecipeGenerator HOE_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(HOE, INGOT));
	public final RecipeGenerator HOE_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(HOE, GEM));
	public final RecipeGenerator MATTOCK_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(MATTOCK, INGOT));
	public final RecipeGenerator MATTOCK_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(MATTOCK, GEM));
	public final RecipeGenerator MINING_TOOL_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(MINING_TOOL, INGOT));
	public final RecipeGenerator MINING_TOOL_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(MINING_TOOL, GEM));
	public final RecipeGenerator HAMMER_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(HAMMER, INGOT));
	public final RecipeGenerator HAMMER_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(HAMMER, GEM));
	public final RecipeGenerator EXCAVATOR_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(EXCAVATOR, INGOT));
	public final RecipeGenerator EXCAVATOR_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(EXCAVATOR, GEM));
	
	public final RecipeGenerator HELMET_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(HELMET, INGOT));
	public final RecipeGenerator HELMET_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(HELMET, GEM));
	public final RecipeGenerator CHESTPLATE_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(CHESTPLATE, INGOT));
	public final RecipeGenerator CHESTPLATE_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(CHESTPLATE, GEM));
	public final RecipeGenerator LEGGINGS_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(LEGGINGS, INGOT));
	public final RecipeGenerator LEGGINGS_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(LEGGINGS, GEM));
	public final RecipeGenerator BOOTS_SMITHING_FROM_INGOT = register(new SmithingSetRecipeGenerator(BOOTS, INGOT));
	public final RecipeGenerator BOOTS_SMITHING_FROM_GEM = register(new SmithingSetRecipeGenerator(BOOTS, GEM));
	
	public final RecipeGenerator SALVAGE_SMELTING_TO_NUGGET = register(new SalvageSmeltingRecipeGenerator(NUGGET));
	public final RecipeGenerator SALVAGE_SMELTING_TO_FRAGMENT = register(new SalvageSmeltingRecipeGenerator(FRAGMENT));
	public final RecipeGenerator SALVAGE_BLASTING_TO_NUGGET = register(new SalvageBlastingRecipeGenerator(NUGGET));
	public final RecipeGenerator SALVAGE_BLASTING_TO_FRAGMENT = register(new SalvageBlastingRecipeGenerator(FRAGMENT));
	
	public final RecipeGenerator APPLE = register(new AppleCraftingRecipeGenerator());
	
	public final RecipeGenerator SMOOTH_METEOR_STONE = register(new SmeltingRecipeGenerator(Ingredient.ofItems(AMBlocks.METEOR_STONE.get()), AMBlocks.SMOOTH_METEOR_STONE.get()));
	
	public final RecipeGenerator POLISHED_METEOR_STONE = register(new Crafting2x2RecipeGenerator(AMBlocks.POLISHED_METEOR_STONE.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get()), 4));
	public final RecipeGenerator METEOR_STONE_BRICKS = register(new Crafting2x2RecipeGenerator(AMBlocks.METEOR_STONE_BRICKS.get(), Ingredient.ofItems(AMBlocks.POLISHED_METEOR_STONE.get()), 4));
	
	public final RecipeGenerator POLISHED_METEOR_STONE_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_METEOR_STONE.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get())));
	public final RecipeGenerator METEOR_STONE_BRICKS_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICKS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get()), "smooth"));
	public final RecipeGenerator METEOR_STONE_BRICKS_FROM_POLISHED_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICKS.get(), Ingredient.ofItems(AMBlocks.POLISHED_METEOR_STONE.get()), "polished"));
	
	public final RecipeGenerator METEOR_STONE_SLAB = register(new SlabCraftingRecipeGenerator(AMBlocks.METEOR_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE.get())));
	public final RecipeGenerator METEOR_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AMBlocks.METEOR_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE.get())));
	public final RecipeGenerator METEOR_STONE_WALL = register(new WallCraftingRecipeGenerator(AMBlocks.METEOR_STONE_WALL.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE.get())));
	
	public final RecipeGenerator SMOOTH_METEOR_STONE_SLAB = register(new SlabCraftingRecipeGenerator(AMBlocks.SMOOTH_METEOR_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get())));
	public final RecipeGenerator SMOOTH_METEOR_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AMBlocks.SMOOTH_METEOR_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get())));
	public final RecipeGenerator SMOOTH_METEOR_STONE_WALL = register(new WallCraftingRecipeGenerator(AMBlocks.SMOOTH_METEOR_STONE_WALL.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get())));
	
	public final RecipeGenerator POLISHED_METEOR_STONE_SLAB = register(new SlabCraftingRecipeGenerator(AMBlocks.POLISHED_METEOR_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.POLISHED_METEOR_STONE.get())));
	public final RecipeGenerator POLISHED_METEOR_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AMBlocks.POLISHED_METEOR_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.POLISHED_METEOR_STONE.get())));
	
	public final RecipeGenerator METEOR_STONE_BRICK_SLAB = register(new SlabCraftingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_SLAB.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE_BRICKS.get())));
	public final RecipeGenerator METEOR_STONE_BRICK_STAIRS = register(new StairsCraftingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_STAIRS.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE_BRICKS.get())));
	public final RecipeGenerator METEOR_STONE_BRICK_WALL = register(new WallCraftingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_WALL.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE_BRICKS.get())));
	
	public final RecipeGenerator METEOR_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE.get()), 2));
	public final RecipeGenerator METEOR_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE.get())));
	public final RecipeGenerator METEOR_STONE_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_WALL.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE.get())));
	
	public final RecipeGenerator SMOOTH_METEOR_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.SMOOTH_METEOR_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get()), 2));
	public final RecipeGenerator SMOOTH_METEOR_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.SMOOTH_METEOR_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get())));
	public final RecipeGenerator SMOOTH_METEOR_STONE_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.SMOOTH_METEOR_STONE_WALL.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get())));
	
	public final RecipeGenerator POLISHED_METEOR_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_METEOR_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.POLISHED_METEOR_STONE.get()), 2));
	public final RecipeGenerator POLISHED_METEOR_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_METEOR_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.POLISHED_METEOR_STONE.get())));
	
	public final RecipeGenerator POLISHED_METEOR_STONE_SLAB_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_METEOR_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get()), 2, "smooth"));
	public final RecipeGenerator POLISHED_METEOR_STONE_STAIRS_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_METEOR_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get()), "smooth"));
	
	public final RecipeGenerator METEOR_STONE_BRICK_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_SLAB.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE_BRICKS.get()), 2));
	public final RecipeGenerator METEOR_STONE_BRICK_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_STAIRS.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE_BRICKS.get())));
	public final RecipeGenerator METEOR_STONE_BRICK_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_WALL.get(), Ingredient.ofItems(AMBlocks.METEOR_STONE_BRICKS.get())));
	
	public final RecipeGenerator METEOR_STONE_BRICK_SLAB_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_SLAB.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get()), 2, "smooth"));
	public final RecipeGenerator METEOR_STONE_BRICK_STAIRS_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_STAIRS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get()), "smooth"));
	public final RecipeGenerator METEOR_STONE_BRICK_WALL_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_WALL.get(), Ingredient.ofItems(AMBlocks.SMOOTH_METEOR_STONE.get()), "smooth"));
	
	public final RecipeGenerator METEOR_STONE_BRICK_SLAB_FROM_POLISHED_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_SLAB.get(), Ingredient.ofItems(AMBlocks.POLISHED_METEOR_STONE.get()), 2, "polished"));
	public final RecipeGenerator METEOR_STONE_BRICK_STAIRS_FROM_POLISHED_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_STAIRS.get(), Ingredient.ofItems(AMBlocks.POLISHED_METEOR_STONE.get()), "polished"));
	public final RecipeGenerator METEOR_STONE_BRICK_WALL_FROM_POLISHED_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.METEOR_STONE_BRICK_WALL.get(), Ingredient.ofItems(AMBlocks.POLISHED_METEOR_STONE.get()), "polished"));
	
	public final RecipeGenerator ASTEROID_ORE_TO_INGOT = register(new SmeltingSetRecipeGenerator(ASTEROID_ORE, INGOT));
	public final RecipeGenerator ASTEROID_ORE_TO_GEM = register(new SmeltingSetRecipeGenerator(ASTEROID_ORE, GEM));
	public final RecipeGenerator ASTEROID_ORE_TO_MISC = register(new SmeltingSetRecipeGenerator(ASTEROID_ORE, MISC_RESOURCE));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_INGOT = register(new SmeltingSetRecipeGenerator(ASTEROID_CLUSTER, INGOT));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_GEM = register(new SmeltingSetRecipeGenerator(ASTEROID_CLUSTER, GEM));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_MISC = register(new SmeltingSetRecipeGenerator(ASTEROID_CLUSTER, MISC_RESOURCE));
	
	
	public final RecipeGenerator ASTEROID_ORE_TO_INGOT_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_ORE, INGOT));
	public final RecipeGenerator ASTEROID_ORE_TO_GEM_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_ORE, GEM));
	public final RecipeGenerator ASTEROID_ORE_TO_MISC_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_ORE, MISC_RESOURCE));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_INGOT_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_CLUSTER, INGOT));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_GEM_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_CLUSTER, GEM));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_MISC_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_CLUSTER, MISC_RESOURCE));
	
	
	public final RecipeGenerator ASTEROID_ORE_TO_DUSTS = register(new com.github.mixinors.astromine.datagen.generator.recipe.TrituratingRecipeGenerator(ASTEROID_ORE, DUST, 2, 90, 340));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_DUSTS = register(new com.github.mixinors.astromine.datagen.generator.recipe.TrituratingRecipeGenerator(ASTEROID_CLUSTER, DUST, 2, 90, 340));
	
	
	public final RecipeGenerator SMOOTH_ASTEROID_STONE = register(new SmeltingRecipeGenerator(Ingredient.ofItems(AMBlocks.ASTEROID_STONE.get()), AMBlocks.SMOOTH_ASTEROID_STONE.get()));
	
	public final RecipeGenerator POLISHED_ASTEROID_STONE = register(new Crafting2x2RecipeGenerator(AMBlocks.POLISHED_ASTEROID_STONE.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get()), 4));
	
	public final RecipeGenerator ASTEROID_STONE_BRICKS = register(new Crafting2x2RecipeGenerator(AMBlocks.ASTEROID_STONE_BRICKS.get(), Ingredient.ofItems(AMBlocks.POLISHED_ASTEROID_STONE.get()), 4));
	
	public final RecipeGenerator ASTEROID_STONE_SLAB = register(new SlabCraftingRecipeGenerator(AMBlocks.ASTEROID_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE.get())));
	
	public final RecipeGenerator ASTEROID_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AMBlocks.ASTEROID_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE.get())));
	
	public final RecipeGenerator SMOOTH_ASTEROID_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AMBlocks.SMOOTH_ASTEROID_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get())));
	
	public final RecipeGenerator POLISHED_ASTEROID_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AMBlocks.POLISHED_ASTEROID_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.POLISHED_ASTEROID_STONE.get())));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_STAIRS = register(new StairsCraftingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_STAIRS.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE_BRICKS.get())));
	
	public final RecipeGenerator ASTEROID_STONE_WALL = register(new WallCraftingRecipeGenerator(AMBlocks.ASTEROID_STONE_WALL.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE.get())));
	
	public final RecipeGenerator SMOOTH_ASTEROID_STONE_WALL = register(new WallCraftingRecipeGenerator(AMBlocks.SMOOTH_ASTEROID_STONE_WALL.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get())));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_WALL = register(new WallCraftingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_WALL.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE_BRICKS.get())));
	
	public final RecipeGenerator POLISHED_ASTEROID_STONE_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_ASTEROID_STONE.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get())));
	
	public final RecipeGenerator ASTEROID_STONE_BRICKS_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICKS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get()), "smooth"));
	
	public final RecipeGenerator ASTEROID_STONE_BRICKS_FROM_POLISHED_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICKS.get(), Ingredient.ofItems(AMBlocks.POLISHED_ASTEROID_STONE.get()), "polished"));
	
	public final RecipeGenerator ASTEROID_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE.get()), 2));
	
	public final RecipeGenerator SMOOTH_ASTEROID_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.SMOOTH_ASTEROID_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get()), 2));
	
	public final RecipeGenerator POLISHED_ASTEROID_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_ASTEROID_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.POLISHED_ASTEROID_STONE.get()), 2));
	
	public final RecipeGenerator POLISHED_ASTEROID_STONE_SLAB_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_ASTEROID_STONE_SLAB.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get()), 2, "smooth"));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_SLAB.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE_BRICKS.get()), 2));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_SLAB_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_SLAB.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get()), 2, "smooth"));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_SLAB_FROM_POLISHED_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_SLAB.get(), Ingredient.ofItems(AMBlocks.POLISHED_ASTEROID_STONE.get()), 2, "polished"));
	
	public final RecipeGenerator ASTEROID_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE.get())));
	
	public final RecipeGenerator SMOOTH_ASTEROID_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.SMOOTH_ASTEROID_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get())));
	
	public final RecipeGenerator POLISHED_ASTEROID_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_ASTEROID_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.POLISHED_ASTEROID_STONE.get())));
	
	public final RecipeGenerator POLISHED_ASTEROID_STONE_STAIRS_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.POLISHED_ASTEROID_STONE_STAIRS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get()), "smooth"));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_STAIRS.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE_BRICKS.get())));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_STAIRS_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_STAIRS.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get()), "smooth"));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_STAIRS_FROM_POLISHED_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_STAIRS.get(), Ingredient.ofItems(AMBlocks.POLISHED_ASTEROID_STONE.get()), "polished"));
	
	public final RecipeGenerator ASTEROID_STONE_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_WALL.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE.get())));
	
	public final RecipeGenerator SMOOTH_ASTEROID_STONE_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.SMOOTH_ASTEROID_STONE_WALL.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get())));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_WALL.get(), Ingredient.ofItems(AMBlocks.ASTEROID_STONE_BRICKS.get())));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_WALL_FROM_SMOOTH_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_WALL.get(), Ingredient.ofItems(AMBlocks.SMOOTH_ASTEROID_STONE.get()), "smooth"));
	
	public final RecipeGenerator ASTEROID_STONE_BRICK_WALL_FROM_POLISHED_STONECUTTING = register(new StonecuttingRecipeGenerator(AMBlocks.ASTEROID_STONE_BRICK_WALL.get(), Ingredient.ofItems(AMBlocks.POLISHED_ASTEROID_STONE.get()), "polished"));
	
	public final RecipeGenerator SPACE_SLIME_BLOCK = register(new Crafting3x3RecipeGenerator(AMBlocks.SPACE_SLIME_BLOCK.get(), Ingredient.ofItems(AMItems.SPACE_SLIME_BALL.get())));
	
	public final RecipeGenerator BLOCK_TO_DUSTS = register(new TrituratingSetRecipeGenerator(BLOCK, DUST, 9, 240, 540));
	public final RecipeGenerator BLOCK_2x2_TO_DUSTS = register(new TrituratingSetRecipeGenerator(BLOCK_2x2, DUST, 4, 160, 480));
	public final RecipeGenerator BLOCK_2x2_TO_MISC = register(new TrituratingSetRecipeGenerator(BLOCK_2x2, MISC_RESOURCE, 4, 120, 340));
	public final RecipeGenerator ORE_TO_DUSTS = register(new TrituratingSetRecipeGenerator(ORE, DUST, 2, 180, 340));
	public final RecipeGenerator METEOR_ORE_TO_TINY_DUSTS = register(new TrituratingSetRecipeGenerator(METEOR_ORE, TINY_DUST, 6, 180, 340));
	public final RecipeGenerator METEOR_CLUSTER_TO_TINY_DUSTS = register(new TrituratingSetRecipeGenerator(METEOR_CLUSTER, TINY_DUST, 3, 180, 340));
	public final RecipeGenerator INGOT_TO_DUST = register(new TrituratingSetRecipeGenerator(INGOT, DUST, 60, 270));
	public final RecipeGenerator GEM_TO_DUST = register(new TrituratingSetRecipeGenerator(GEM, DUST, 60, 270));
	public final RecipeGenerator NUGGET_TO_TINY_DUST = register(new TrituratingSetRecipeGenerator(NUGGET, TINY_DUST, 30, 200));
	public final RecipeGenerator FRAGMENT_TO_TINY_DUST = register(new TrituratingSetRecipeGenerator(FRAGMENT, TINY_DUST, 30, 200));
	
	public final RecipeGenerator GRAVEL_FROM_COBBLESTONE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("gravel_from_triturating_cobblestone", Ingredient.ofItems(Blocks.COBBLESTONE), Blocks.GRAVEL, 40, 120));
	public final RecipeGenerator SAND_FROM_GRAVEL = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("sand_from_triturating_gravel", Ingredient.ofItems(Blocks.GRAVEL), Blocks.SAND, 40, 120));
	public final RecipeGenerator SOUL_SAND_FROM_SOIL = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("soul_sand_from_soil", Ingredient.ofItems(Blocks.SOUL_SOIL), Blocks.SOUL_SAND, 40, 120));
	
	public final RecipeGenerator WHITE_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("white_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.WHITE_CONCRETE), Blocks.WHITE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator ORANGE_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("orange_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.ORANGE_CONCRETE), Blocks.ORANGE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator MAGENTA_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("magenta_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.MAGENTA_CONCRETE), Blocks.MAGENTA_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator LIGHT_BLUE_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("light_blue_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.LIGHT_BLUE_CONCRETE), Blocks.LIGHT_BLUE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator YELLOW_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("yellow_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.YELLOW_CONCRETE), Blocks.YELLOW_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator LIME_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("lime_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.LIME_CONCRETE), Blocks.LIME_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator PINK_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("pink_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.PINK_CONCRETE), Blocks.PINK_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator GRAY_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("gray_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.GRAY_CONCRETE), Blocks.GRAY_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator LIGHT_GRAY_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("light_gray_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.LIGHT_GRAY_CONCRETE), Blocks.LIGHT_GRAY_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator CYAN_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("cyan_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.CYAN_CONCRETE), Blocks.CYAN_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator PURPLE_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("purple_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.PURPLE_CONCRETE), Blocks.PURPLE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator BLUE_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("blue_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.BLUE_CONCRETE), Blocks.BLUE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator BROWN_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("brown_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.BROWN_CONCRETE), Blocks.BROWN_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator GREEN_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("green_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.GREEN_CONCRETE), Blocks.GREEN_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator RED_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("red_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.RED_CONCRETE), Blocks.RED_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator BLACK_CONCRETE_POWDER_FROM_CONCRETE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("black_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.BLACK_CONCRETE), Blocks.BLACK_CONCRETE_POWDER, 20, 50));
	
	public final RecipeGenerator SUGAR_FROM_SUGAR_CANE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("sugar_from_triturating_sugar_cane", Ingredient.ofItems(Items.SUGAR_CANE), Items.SUGAR, 2, 20, 50));
	public final RecipeGenerator BLAZE_POWDER_FROM_ROD = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("blaze_powder_from_triturating_rod", Ingredient.ofItems(Items.BLAZE_ROD), Items.BLAZE_POWDER, 4, 30, 60));
	public final RecipeGenerator BONE_MEAL_FROM_BONE = register(new com.github.mixinors.astromine.datagen.generator.recipe.onetime.TrituratingRecipeGenerator("bone_meal_from_triturating_bone", Ingredient.ofItems(Items.BONE), Items.BONE_MEAL, 5, 20, 50));
	
	public final RecipeGenerator INGOT_TO_PLATES_PRESSING = register(new PressingSetRecipeGenerator(INGOT, PLATE, 80, 340));
	
	public final RecipeGenerator INGOT_TO_WIRES_WIREMILLING = register(new WireMillingSetRecipeGenerator(INGOT, WIRE, 3, 80, 340));

	public SetRecipeGenerator register(SetRecipeGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeRecipeGenerator register(OneTimeRecipeGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateRecipes(RecipeData recipes) {
		AMMaterialSets.getMaterialSets().forEach((set) -> generateSetRecipes(recipes, set));
		generateOneTimeRecipes(recipes);
	}

	private void generateSetRecipes(RecipeData recipes, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(recipes, set);
					AMCommon.LOGGER.info("Recipe generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AMCommon.LOGGER.error("Recipe generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AMCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeRecipes(RecipeData recipes) {
		ONE_TIME_GENERATORS.forEach((generator) -> {
			try {
				generator.generate(recipes);
			} catch (Exception exception) {
				AMCommon.LOGGER.error("Recipe generation failed, with generator " + generator.getGeneratorName() + ".");
				AMCommon.LOGGER.error(exception.getMessage());
			}
		});
	}
}
