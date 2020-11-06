package com.github.chainmailstudios.astromine.technologies.datagen;

import net.fabricmc.fabric.api.tag.TagRegistry;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.RecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineRecipeGenerators;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.recipe.onetime.TrituratingRecipeGenerator;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.recipe.set.PressingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.recipe.set.TrituratingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.technologies.datagen.generators.recipe.set.WireMillingSetRecipeGenerator;

import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.BLOCK;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.BLOCK_2x2;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.DUST;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.FRAGMENT;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.GEM;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.INGOT;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.METEOR_CLUSTER;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.METEOR_ORE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.MISC_RESOURCE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.NUGGET;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.ORE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.PLATE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.TINY_DUST;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.WIRE;


public class AstromineTechnologiesRecipeGenerators extends AstromineRecipeGenerators {
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

	public final RecipeGenerator GRAVEL_FROM_COBBLESTONE = register(new TrituratingRecipeGenerator("gravel_from_triturating_cobblestone", Ingredient.ofItems(Blocks.COBBLESTONE), Blocks.GRAVEL, 40, 120));
	public final RecipeGenerator SAND_FROM_GRAVEL = register(new TrituratingRecipeGenerator("sand_from_triturating_gravel", Ingredient.ofItems(Blocks.GRAVEL), Blocks.SAND, 40, 120));
	public final RecipeGenerator SOUL_SAND_FROM_SOIL = register(new TrituratingRecipeGenerator("soul_sand_from_soil", Ingredient.ofItems(Blocks.SOUL_SOIL), Blocks.SOUL_SAND, 40, 120));

	public final RecipeGenerator WHITE_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("white_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.WHITE_CONCRETE), Blocks.WHITE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator ORANGE_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("orange_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.ORANGE_CONCRETE), Blocks.ORANGE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator MAGENTA_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("magenta_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.MAGENTA_CONCRETE), Blocks.MAGENTA_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator LIGHT_BLUE_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("light_blue_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.LIGHT_BLUE_CONCRETE), Blocks.LIGHT_BLUE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator YELLOW_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("yellow_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.YELLOW_CONCRETE), Blocks.YELLOW_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator LIME_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("lime_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.LIME_CONCRETE), Blocks.LIME_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator PINK_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("pink_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.PINK_CONCRETE), Blocks.PINK_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator GRAY_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("gray_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.GRAY_CONCRETE), Blocks.GRAY_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator LIGHT_GRAY_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("light_gray_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.LIGHT_GRAY_CONCRETE), Blocks.LIGHT_GRAY_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator CYAN_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("cyan_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.CYAN_CONCRETE), Blocks.CYAN_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator PURPLE_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("purple_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.PURPLE_CONCRETE), Blocks.PURPLE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator BLUE_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("blue_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.BLUE_CONCRETE), Blocks.BLUE_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator BROWN_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("brown_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.BROWN_CONCRETE), Blocks.BROWN_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator GREEN_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("green_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.GREEN_CONCRETE), Blocks.GREEN_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator RED_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("red_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.RED_CONCRETE), Blocks.RED_CONCRETE_POWDER, 20, 50));
	public final RecipeGenerator BLACK_CONCRETE_POWDER_FROM_CONCRETE = register(new TrituratingRecipeGenerator("black_concrete_powder_from_triturating_concrete", Ingredient.ofItems(Blocks.BLACK_CONCRETE), Blocks.BLACK_CONCRETE_POWDER, 20, 50));

	public final RecipeGenerator SUGAR_FROM_SUGAR_CANE = register(new TrituratingRecipeGenerator("sugar_from_triturating_sugar_cane", Ingredient.ofItems(Items.SUGAR_CANE), Items.SUGAR, 2, 20, 50));
	public final RecipeGenerator BLAZE_POWDER_FROM_ROD = register(new TrituratingRecipeGenerator("blaze_powder_from_triturating_rod", Ingredient.ofItems(Items.BLAZE_ROD), Items.BLAZE_POWDER, 4, 30, 60));
	public final RecipeGenerator BONE_MEAL_FROM_BONE = register(new TrituratingRecipeGenerator("bone_meal_from_triturating_bone", Ingredient.ofItems(Items.BONE), Items.BONE_MEAL, 5, 20, 50));

	public final RecipeGenerator INGOT_TO_PLATES_PRESSING = register(new PressingSetRecipeGenerator(INGOT, PLATE, 80, 340));

	public final RecipeGenerator INGOT_TO_WIRES_WIREMILLING = register(new WireMillingSetRecipeGenerator(INGOT, WIRE, 3, 80, 340));
}
