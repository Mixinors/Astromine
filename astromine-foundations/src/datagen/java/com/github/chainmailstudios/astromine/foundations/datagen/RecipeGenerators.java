package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe.*;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.HashSet;
import java.util.Set;

import static com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType.*;

public class RecipeGenerators {
	private static final Set<RecipeGenerator> GENERATORS = new HashSet<>();

	public static final RecipeGenerator INGOT_TO_BLOCK = register(new Crafting3x3RecipeGenerator(INGOT, BLOCK));
	public static final RecipeGenerator GEM_TO_BLOCK = register(new Crafting3x3RecipeGenerator(GEM, BLOCK));
	public static final RecipeGenerator NUGGET_TO_INGOT = register(new Crafting3x3RecipeGenerator(NUGGET, INGOT));
	public static final RecipeGenerator FRAGMENT_TO_GEM = register(new Crafting3x3RecipeGenerator(FRAGMENT, GEM));
	public static final RecipeGenerator TINY_DUST_TO_DUST = register(new Crafting3x3RecipeGenerator(TINY_DUST, DUST));

	public static final RecipeGenerator BLOCK_TO_INGOTS = register(new ShapelessCraftingRecipeGenerator(BLOCK, INGOT, 9));
	public static final RecipeGenerator BLOCK_TO_GEMS = register(new ShapelessCraftingRecipeGenerator(BLOCK, GEM, 9));
	public static final RecipeGenerator INGOT_TO_NUGGETS = register(new ShapelessCraftingRecipeGenerator(INGOT, NUGGET, 9));
	public static final RecipeGenerator GEM_TO_FRAGMENTS = register(new ShapelessCraftingRecipeGenerator(GEM, FRAGMENT, 9));
	public static final RecipeGenerator DUST_TO_TINY_DUSTS = register(new ShapelessCraftingRecipeGenerator(DUST, TINY_DUST, 9));

	public static final RecipeGenerator ORE_TO_DUSTS = register(new TrituratingRecipeGenerator(ORE, DUST, 2, 30, 270));
	public static final RecipeGenerator ASTEROID_ORE_TO_DUSTS = register(new TrituratingRecipeGenerator(ASTEROID_ORE, DUST, 2, 30, 270));
	public static final RecipeGenerator METEOR_ORE_TO_TINY_DUSTS = register(new TrituratingRecipeGenerator(METEOR_ORE, TINY_DUST, 6, 30, 270));
	public static final RecipeGenerator ASTEROID_CLUSTER_TO_DUSTS = register(new TrituratingRecipeGenerator(ASTEROID_CLUSTER, DUST, 2, 30, 270));
	public static final RecipeGenerator METEOR_CLUSTER_TO_TINY_DUSTS = register(new TrituratingRecipeGenerator(METEOR_CLUSTER, TINY_DUST, 6, 30, 270));
	public static final RecipeGenerator INGOT_TO_DUST = register(new TrituratingRecipeGenerator(INGOT, DUST, 10, 340));
	public static final RecipeGenerator GEM_TO_DUST = register(new TrituratingRecipeGenerator(GEM, DUST, 10, 340));
	public static final RecipeGenerator NUGGET_TO_TINY_DUST = register(new TrituratingRecipeGenerator(NUGGET, TINY_DUST, 5, 340));
	public static final RecipeGenerator FRAGMENT_TO_TINY_DUST = register(new TrituratingRecipeGenerator(FRAGMENT, TINY_DUST, 5, 340));

	public static final RecipeGenerator INGOT_TO_PLATES_PRESSING = register(new PressingRecipeGenerator(INGOT, PLATES, 60, 384));

	public static final RecipeGenerator INGOT_TO_PICKAXE = register(new ToolCraftingRecipeGenerators.PickaxeCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_PICKAXE = register(new ToolCraftingRecipeGenerators.PickaxeCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_PICKAXE = register(new ToolCraftingRecipeGenerators.PickaxeCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_AXE = register(new ToolCraftingRecipeGenerators.AxeCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_AXE = register(new ToolCraftingRecipeGenerators.AxeCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_AXE = register(new ToolCraftingRecipeGenerators.AxeCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_SHOVEL = register(new ToolCraftingRecipeGenerators.ShovelCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_SHOVEL = register(new ToolCraftingRecipeGenerators.ShovelCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_SHOVEL = register(new ToolCraftingRecipeGenerators.ShovelCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_SWORD = register(new ToolCraftingRecipeGenerators.SwordCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_SWORD = register(new ToolCraftingRecipeGenerators.SwordCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_SWORD = register(new ToolCraftingRecipeGenerators.SwordCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_HOE = register(new ToolCraftingRecipeGenerators.HoeCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_HOE = register(new ToolCraftingRecipeGenerators.HoeCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_HOE = register(new ToolCraftingRecipeGenerators.HoeCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_MATTOCK = register(new ToolCraftingRecipeGenerators.MattockCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_MATTOCK = register(new ToolCraftingRecipeGenerators.MattockCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_MATTOCK = register(new ToolCraftingRecipeGenerators.MattockCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_MINING_TOOL = register(new ToolCraftingRecipeGenerators.MiningToolCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_MINING_TOOL = register(new ToolCraftingRecipeGenerators.MiningToolCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_MINING_TOOL = register(new ToolCraftingRecipeGenerators.MiningToolCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_HAMMER = register(new ToolCraftingRecipeGenerators.HammerCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_HAMMER = register(new ToolCraftingRecipeGenerators.HammerCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_HAMMER = register(new ToolCraftingRecipeGenerators.HammerCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_EXCAVATOR = register(new ToolCraftingRecipeGenerators.ExcavatorCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_EXCAVATOR = register(new ToolCraftingRecipeGenerators.ExcavatorCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_EXCAVATOR = register(new ToolCraftingRecipeGenerators.ExcavatorCraftingRecipeGenerator(MISC_RESOURCE));

	public static final RecipeGenerator INGOT_TO_HELMET = register(new ArmorCraftingRecipeGenerators.HelmetCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_HELMET = register(new ArmorCraftingRecipeGenerators.HelmetCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_HELMET = register(new ArmorCraftingRecipeGenerators.HelmetCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_CHESTPLATE = register(new ArmorCraftingRecipeGenerators.ChestplateCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_CHESTPLATE = register(new ArmorCraftingRecipeGenerators.ChestplateCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_CHESTPLATE = register(new ArmorCraftingRecipeGenerators.ChestplateCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_LEGGINGS = register(new ArmorCraftingRecipeGenerators.LeggingsCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_LEGGINGS = register(new ArmorCraftingRecipeGenerators.LeggingsCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_LEGGINGS = register(new ArmorCraftingRecipeGenerators.LeggingsCraftingRecipeGenerator(MISC_RESOURCE));
	public static final RecipeGenerator INGOT_TO_BOOTS = register(new ArmorCraftingRecipeGenerators.BootsCraftingRecipeGenerator(INGOT));
	public static final RecipeGenerator GEM_TO_BOOTS = register(new ArmorCraftingRecipeGenerators.BootsCraftingRecipeGenerator(GEM));
	public static final RecipeGenerator MISC_TO_BOOTS = register(new ArmorCraftingRecipeGenerators.BootsCraftingRecipeGenerator(MISC_RESOURCE));

	public static final RecipeGenerator ORE_TO_INGOT = register(new SmeltingRecipeGenerator(ORE, INGOT));
	public static final RecipeGenerator ORE_TO_GEM = register(new SmeltingRecipeGenerator(ORE, GEM));
	public static final RecipeGenerator METEOR_ORE_TO_NUGGET = register(new SmeltingRecipeGenerator(METEOR_ORE, NUGGET));
	public static final RecipeGenerator METEOR_ORE_TO_FRAGMENT = register(new SmeltingRecipeGenerator(METEOR_ORE, FRAGMENT));
	public static final RecipeGenerator ASTEROID_ORE_TO_INGOT = register(new SmeltingRecipeGenerator(ASTEROID_ORE, INGOT));
	public static final RecipeGenerator ASTEROID_ORE_TO_GEM = register(new SmeltingRecipeGenerator(ASTEROID_ORE, GEM));
	public static final RecipeGenerator DUST_TO_INGOT = register(new SmeltingRecipeGenerator(DUST, INGOT));
	public static final RecipeGenerator DUST_TO_GEM = register(new SmeltingRecipeGenerator(DUST, GEM));
	public static final RecipeGenerator ASTEROID_CLUSTER_TO_INGOT = register(new SmeltingRecipeGenerator(ASTEROID_CLUSTER, INGOT));
	public static final RecipeGenerator ASTEROID_CLUSTER_TO_GEM = register(new SmeltingRecipeGenerator(ASTEROID_CLUSTER, GEM));
	public static final RecipeGenerator METEOR_CLUSTER_TO_NUGGET = register(new SmeltingRecipeGenerator(METEOR_CLUSTER, NUGGET));
	public static final RecipeGenerator METEOR_CLUSTER_TO_FRAGMENT = register(new SmeltingRecipeGenerator(METEOR_CLUSTER, FRAGMENT));
	public static final RecipeGenerator TINY_DUST_TO_NUGGET = register(new SmeltingRecipeGenerator(TINY_DUST, NUGGET));
	public static final RecipeGenerator TINY_DUST_TO_FRAGMENT = register(new SmeltingRecipeGenerator(TINY_DUST, FRAGMENT));

	public static final RecipeGenerator ORE_TO_INGOT_BLASTING = register(new BlastingRecipeGenerator(ORE, INGOT));
	public static final RecipeGenerator ORE_TO_GEM_BLASTING = register(new BlastingRecipeGenerator(ORE, GEM));
	public static final RecipeGenerator METEOR_ORE_TO_NUGGET_BLASTING = register(new BlastingRecipeGenerator(METEOR_ORE, NUGGET));
	public static final RecipeGenerator METEOR_ORE_TO_FRAGMENT_BLASTING = register(new BlastingRecipeGenerator(METEOR_ORE, FRAGMENT));
	public static final RecipeGenerator ASTEROID_ORE_TO_INGOT_BLASTING = register(new BlastingRecipeGenerator(ASTEROID_ORE, INGOT));
	public static final RecipeGenerator ASTEROID_ORE_TO_GEM_BLASTING = register(new BlastingRecipeGenerator(ASTEROID_ORE, GEM));
	public static final RecipeGenerator DUST_TO_INGOT_BLASTING = register(new BlastingRecipeGenerator(DUST, INGOT));
	public static final RecipeGenerator DUST_TO_GEM_BLASTING = register(new BlastingRecipeGenerator(DUST, GEM));
	public static final RecipeGenerator ASTEROID_CLUSTER_TO_INGOT_BLASTING = register(new BlastingRecipeGenerator(ASTEROID_CLUSTER, INGOT));
	public static final RecipeGenerator ASTEROID_CLUSTER_TO_GEM_BLASTING = register(new BlastingRecipeGenerator(ASTEROID_CLUSTER, GEM));
	public static final RecipeGenerator METEOR_CLUSTER_TO_NUGGET_BLASTING = register(new BlastingRecipeGenerator(METEOR_CLUSTER, NUGGET));
	public static final RecipeGenerator METEOR_CLUSTER_TO_FRAGMENT_BLASTING = register(new BlastingRecipeGenerator(METEOR_CLUSTER, FRAGMENT));
	public static final RecipeGenerator TINY_DUST_TO_NUGGET_BLASTING = register(new BlastingRecipeGenerator(TINY_DUST, NUGGET));
	public static final RecipeGenerator TINY_DUST_TO_FRAGMENT_BLASTING = register(new BlastingRecipeGenerator(TINY_DUST, FRAGMENT));

	public static final RecipeGenerator INGOT_TO_PLATES_CRAFTING = register(new ShapedCraftingRecipeGenerator(INGOT, PLATES, "#", "#"));
	public static final RecipeGenerator INGOT_TO_GEAR_CRAFTING = register(new ShapedCraftingRecipeGenerator(INGOT, GEAR, " # ", "# #", " # "));

	public static final RecipeGenerator PICKAXE_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(PICKAXE, INGOT));
	public static final RecipeGenerator PICKAXE_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(PICKAXE, GEM));
	public static final RecipeGenerator AXE_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(AXE, INGOT));
	public static final RecipeGenerator AXE_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(AXE, GEM));
	public static final RecipeGenerator SHOVEL_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(SHOVEL, INGOT));
	public static final RecipeGenerator SHOVEL_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(SHOVEL, GEM));
	public static final RecipeGenerator SWORD_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(SWORD, INGOT));
	public static final RecipeGenerator SWORD_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(SWORD, GEM));
	public static final RecipeGenerator HOE_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(HOE, INGOT));
	public static final RecipeGenerator HOE_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(HOE, GEM));
	public static final RecipeGenerator MATTOCK_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(MATTOCK, INGOT));
	public static final RecipeGenerator MATTOCK_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(MATTOCK, GEM));
	public static final RecipeGenerator MINING_TOOL_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(MINING_TOOL, INGOT));
	public static final RecipeGenerator MINING_TOOL_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(MINING_TOOL, GEM));
	public static final RecipeGenerator HAMMER_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(HAMMER, INGOT));
	public static final RecipeGenerator HAMMER_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(HAMMER, GEM));
	public static final RecipeGenerator EXCAVATOR_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(EXCAVATOR, INGOT));
	public static final RecipeGenerator EXCAVATOR_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(EXCAVATOR, GEM));

	public static final RecipeGenerator HELMET_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(HELMET, INGOT));
	public static final RecipeGenerator HELMET_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(HELMET, GEM));
	public static final RecipeGenerator CHESTPLATE_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(CHESTPLATE, INGOT));
	public static final RecipeGenerator CHESTPLATE_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(CHESTPLATE, GEM));
	public static final RecipeGenerator LEGGINGS_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(LEGGINGS, INGOT));
	public static final RecipeGenerator LEGGINGS_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(LEGGINGS, GEM));
	public static final RecipeGenerator BOOTS_SMITHING_FROM_INGOT = register(new SmithingRecipeGenerator(BOOTS, INGOT));
	public static final RecipeGenerator BOOTS_SMITHING_FROM_GEM = register(new SmithingRecipeGenerator(BOOTS, GEM));

	public static RecipeGenerator register(RecipeGenerator generator) {
		GENERATORS.add(generator);
		return generator;
	}

	public static void generateRecipes(RecipeData recipes, MaterialSet set) {
		GENERATORS.forEach((generator) -> {
			try {
				if(set.shouldGenerate(generator)) {
					generator.generateRecipe(recipes, set);
					System.out.println("generated recipe "+generator.getRecipeId(set));
				}
			} catch(Exception e) {
				System.out.println("oh fuck recipe bronked for " + set.getName()+" with generator "+generator.getString());
				System.out.println(e.getMessage());
			}
		});
	}

	public static void initialize() {

	}
}
