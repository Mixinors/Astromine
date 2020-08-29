package com.github.chainmailstudios.astromine.technologies.datagen;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.RecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineRecipeGenerators;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.recipe.PressingRecipeGenerator;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.recipe.TrituratingRecipeGenerator;

import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.ASTEROID_CLUSTER;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.ASTEROID_ORE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.DUST;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.FRAGMENT;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.GEM;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.INGOT;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.METEOR_CLUSTER;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.METEOR_ORE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.NUGGET;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.ORE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.PLATES;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.TINY_DUST;


public class AstromineTechnologiesRecipeGenerators extends AstromineRecipeGenerators {
	public final RecipeGenerator ORE_TO_DUSTS = register(new TrituratingRecipeGenerator(ORE, DUST, 2, 30, 270));
	public final RecipeGenerator ASTEROID_ORE_TO_DUSTS = register(new TrituratingRecipeGenerator(ASTEROID_ORE, DUST, 2, 30, 270));
	public final RecipeGenerator METEOR_ORE_TO_TINY_DUSTS = register(new TrituratingRecipeGenerator(METEOR_ORE, TINY_DUST, 6, 30, 270));
	public final RecipeGenerator ASTEROID_CLUSTER_TO_DUSTS = register(new TrituratingRecipeGenerator(ASTEROID_CLUSTER, DUST, 2, 30, 270));
	public final RecipeGenerator METEOR_CLUSTER_TO_TINY_DUSTS = register(new TrituratingRecipeGenerator(METEOR_CLUSTER, TINY_DUST, 3, 30, 270));
	public final RecipeGenerator INGOT_TO_DUST = register(new TrituratingRecipeGenerator(INGOT, DUST, 10, 340));
	public final RecipeGenerator GEM_TO_DUST = register(new TrituratingRecipeGenerator(GEM, DUST, 10, 340));
	public final RecipeGenerator NUGGET_TO_TINY_DUST = register(new TrituratingRecipeGenerator(NUGGET, TINY_DUST, 5, 340));
	public final RecipeGenerator FRAGMENT_TO_TINY_DUST = register(new TrituratingRecipeGenerator(FRAGMENT, TINY_DUST, 5, 340));

	public final RecipeGenerator INGOT_TO_PLATES_PRESSING = register(new PressingRecipeGenerator(INGOT, PLATES, 60, 384));
}
