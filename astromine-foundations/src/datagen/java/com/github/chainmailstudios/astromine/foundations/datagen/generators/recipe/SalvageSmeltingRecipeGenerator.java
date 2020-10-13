package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.MultiSmeltingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.*;

import java.util.Arrays;
import java.util.List;

public class SalvageSmeltingRecipeGenerator extends MultiSmeltingSetRecipeGenerator {
	public static final List<MaterialItemType> SALVAGEABLE = Arrays.asList(
			PICKAXE, AXE, SHOVEL, SWORD, HOE,
			HAMMER, EXCAVATOR, MINING_TOOL, MATTOCK,
			HELMET, CHESTPLATE, LEGGINGS, BOOTS
	);

	public SalvageSmeltingRecipeGenerator(MaterialItemType output) {
		super(SALVAGEABLE, output);
	}
}
