package com.github.chainmailstudios.astromine.discoveries.datagen.registry;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.RecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.BlastingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.SmeltingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineRecipeGenerators;

import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.ASTEROID_CLUSTER;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.ASTEROID_ORE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.GEM;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.INGOT;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.MISC_RESOURCE;


public class AstromineDiscoveriesRecipeGenerators extends AstromineRecipeGenerators {
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
}
