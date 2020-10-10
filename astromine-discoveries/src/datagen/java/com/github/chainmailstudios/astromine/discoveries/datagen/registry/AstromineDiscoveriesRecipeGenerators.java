package com.github.chainmailstudios.astromine.discoveries.datagen.registry;

import net.minecraft.recipe.Ingredient;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.RecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.SlabCraftingRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.StairsCraftingRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.StonecuttingRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.WallCraftingRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.BlastingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.SmeltingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineRecipeGenerators;
import com.github.chainmailstudios.astromine.discoveries.datagen.generators.recipe.TrituratingRecipeGenerator;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;

import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.ASTEROID_CLUSTER;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.ASTEROID_ORE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.DUST;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.GEM;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.INGOT;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.MISC_RESOURCE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.MOON_ORE;


public class AstromineDiscoveriesRecipeGenerators extends AstromineRecipeGenerators {
	public final RecipeGenerator ASTEROID_ORE_TO_INGOT = register(new SmeltingSetRecipeGenerator(ASTEROID_ORE, INGOT));
	public final RecipeGenerator ASTEROID_ORE_TO_GEM = register(new SmeltingSetRecipeGenerator(ASTEROID_ORE, GEM));
	public final RecipeGenerator ASTEROID_ORE_TO_MISC = register(new SmeltingSetRecipeGenerator(ASTEROID_ORE, MISC_RESOURCE));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_INGOT = register(new SmeltingSetRecipeGenerator(ASTEROID_CLUSTER, INGOT));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_GEM = register(new SmeltingSetRecipeGenerator(ASTEROID_CLUSTER, GEM));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_MISC = register(new SmeltingSetRecipeGenerator(ASTEROID_CLUSTER, MISC_RESOURCE));

	public final RecipeGenerator MOON_ORE_TO_INGOT = register(new SmeltingSetRecipeGenerator(MOON_ORE, INGOT));
	public final RecipeGenerator MOON_ORE_TO_GEM = register(new SmeltingSetRecipeGenerator(MOON_ORE, GEM));
	public final RecipeGenerator MOON_ORE_TO_MISC = register(new SmeltingSetRecipeGenerator(MOON_ORE, MISC_RESOURCE));

	public final RecipeGenerator ASTEROID_ORE_TO_INGOT_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_ORE, INGOT));
	public final RecipeGenerator ASTEROID_ORE_TO_GEM_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_ORE, GEM));
	public final RecipeGenerator ASTEROID_ORE_TO_MISC_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_ORE, MISC_RESOURCE));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_INGOT_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_CLUSTER, INGOT));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_GEM_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_CLUSTER, GEM));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_MISC_BLASTING = register(new BlastingSetRecipeGenerator(ASTEROID_CLUSTER, MISC_RESOURCE));

	public final RecipeGenerator MOON_ORE_TO_INGOT_BLASTING = register(new BlastingSetRecipeGenerator(MOON_ORE, INGOT));
	public final RecipeGenerator MOON_ORE_TO_GEM_BLASTING = register(new BlastingSetRecipeGenerator(MOON_ORE, GEM));
	public final RecipeGenerator MOON_ORE_TO_MISC_BLASTING = register(new BlastingSetRecipeGenerator(MOON_ORE, MISC_RESOURCE));

	public final RecipeGenerator ASTEROID_ORE_TO_DUSTS = register(new TrituratingRecipeGenerator(ASTEROID_ORE, DUST, 2, 90, 340));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_DUSTS = register(new TrituratingRecipeGenerator(ASTEROID_CLUSTER, DUST, 2, 90, 340));

	public final RecipeGenerator MOON_ORE_TO_DUSTS = register(new TrituratingRecipeGenerator(MOON_ORE, DUST, 2, 90, 340));

	public final RecipeGenerator ASTEROID_STONE_SLAB = register(new SlabCraftingRecipeGenerator(AstromineDiscoveriesBlocks.ASTEROID_STONE_SLAB, Ingredient.ofItems(AstromineDiscoveriesBlocks.ASTEROID_STONE)));
	public final RecipeGenerator MOON_STONE_SLAB = register(new SlabCraftingRecipeGenerator(AstromineDiscoveriesBlocks.MOON_STONE_SLAB, Ingredient.ofItems(AstromineDiscoveriesBlocks.MOON_STONE)));
	public final RecipeGenerator MARTIAN_STONE_SLAB = register(new SlabCraftingRecipeGenerator(AstromineDiscoveriesBlocks.MARTIAN_STONE_SLAB, Ingredient.ofItems(AstromineDiscoveriesBlocks.MARTIAN_STONE)));
	public final RecipeGenerator VULCAN_STONE_SLAB = register(new SlabCraftingRecipeGenerator(AstromineDiscoveriesBlocks.VULCAN_STONE_SLAB, Ingredient.ofItems(AstromineDiscoveriesBlocks.VULCAN_STONE)));

	public final RecipeGenerator ASTEROID_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AstromineDiscoveriesBlocks.ASTEROID_STONE_STAIRS, Ingredient.ofItems(AstromineDiscoveriesBlocks.ASTEROID_STONE)));
	public final RecipeGenerator MOON_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AstromineDiscoveriesBlocks.MOON_STONE_STAIRS, Ingredient.ofItems(AstromineDiscoveriesBlocks.MOON_STONE)));
	public final RecipeGenerator MARTIAN_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AstromineDiscoveriesBlocks.MARTIAN_STONE_STAIRS, Ingredient.ofItems(AstromineDiscoveriesBlocks.MARTIAN_STONE)));
	public final RecipeGenerator VULCAN_STONE_STAIRS = register(new StairsCraftingRecipeGenerator(AstromineDiscoveriesBlocks.VULCAN_STONE_STAIRS, Ingredient.ofItems(AstromineDiscoveriesBlocks.VULCAN_STONE)));

	public final RecipeGenerator ASTEROID_STONE_WALL = register(new WallCraftingRecipeGenerator(AstromineDiscoveriesBlocks.ASTEROID_STONE_WALL, Ingredient.ofItems(AstromineDiscoveriesBlocks.ASTEROID_STONE)));
	public final RecipeGenerator MOON_STONE_WALL = register(new WallCraftingRecipeGenerator(AstromineDiscoveriesBlocks.MOON_STONE_WALL, Ingredient.ofItems(AstromineDiscoveriesBlocks.MOON_STONE)));
	public final RecipeGenerator MARTIAN_STONE_WALL = register(new WallCraftingRecipeGenerator(AstromineDiscoveriesBlocks.MARTIAN_STONE_WALL, Ingredient.ofItems(AstromineDiscoveriesBlocks.MARTIAN_STONE)));
	public final RecipeGenerator VULCAN_STONE_WALL = register(new WallCraftingRecipeGenerator(AstromineDiscoveriesBlocks.VULCAN_STONE_WALL, Ingredient.ofItems(AstromineDiscoveriesBlocks.VULCAN_STONE)));

	public final RecipeGenerator ASTEROID_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.ASTEROID_STONE_SLAB, Ingredient.ofItems(AstromineDiscoveriesBlocks.ASTEROID_STONE), 2));
	public final RecipeGenerator MOON_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.MOON_STONE_SLAB, Ingredient.ofItems(AstromineDiscoveriesBlocks.MOON_STONE), 2));
	public final RecipeGenerator MARTIAN_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.MARTIAN_STONE_SLAB, Ingredient.ofItems(AstromineDiscoveriesBlocks.MARTIAN_STONE), 2));
	public final RecipeGenerator VULCAN_STONE_SLAB_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.VULCAN_STONE_SLAB, Ingredient.ofItems(AstromineDiscoveriesBlocks.VULCAN_STONE), 2));

	public final RecipeGenerator ASTEROID_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.ASTEROID_STONE_STAIRS, Ingredient.ofItems(AstromineDiscoveriesBlocks.ASTEROID_STONE)));
	public final RecipeGenerator MOON_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.MOON_STONE_STAIRS, Ingredient.ofItems(AstromineDiscoveriesBlocks.MOON_STONE)));
	public final RecipeGenerator MARTIAN_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.MARTIAN_STONE_STAIRS, Ingredient.ofItems(AstromineDiscoveriesBlocks.MARTIAN_STONE)));
	public final RecipeGenerator VULCAN_STONE_STAIRS_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.VULCAN_STONE_STAIRS, Ingredient.ofItems(AstromineDiscoveriesBlocks.VULCAN_STONE)));

	public final RecipeGenerator ASTEROID_STONE_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.ASTEROID_STONE_WALL, Ingredient.ofItems(AstromineDiscoveriesBlocks.ASTEROID_STONE)));
	public final RecipeGenerator MOON_STONE_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.MOON_STONE_WALL, Ingredient.ofItems(AstromineDiscoveriesBlocks.MOON_STONE)));
	public final RecipeGenerator MARTIAN_STONE_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.MARTIAN_STONE_WALL, Ingredient.ofItems(AstromineDiscoveriesBlocks.MARTIAN_STONE)));
	public final RecipeGenerator VULCAN_STONE_WALL_STONECUTTING = register(new StonecuttingRecipeGenerator(AstromineDiscoveriesBlocks.VULCAN_STONE_WALL, Ingredient.ofItems(AstromineDiscoveriesBlocks.VULCAN_STONE)));

	public static void initialize() {

	}
}
