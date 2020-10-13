package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.MultiBlastingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.MultiSmeltingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

import java.util.Arrays;
import java.util.List;

import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.AXE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.BOOTS;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.CHESTPLATE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.EXCAVATOR;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.HAMMER;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.HELMET;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.HOE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.LEGGINGS;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.MATTOCK;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.MINING_TOOL;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.PICKAXE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.SHOVEL;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.SWORD;

public class SalvageBlastingRecipeGenerator extends MultiBlastingSetRecipeGenerator {
	public static final List<MaterialItemType> SALVAGEABLE = Arrays.asList(
			PICKAXE, AXE, SHOVEL, SWORD, HOE,
			HAMMER, EXCAVATOR, MINING_TOOL, MATTOCK,
			HELMET, CHESTPLATE, LEGGINGS, BOOTS
	);

	public SalvageBlastingRecipeGenerator(MaterialItemType output) {
		super(SALVAGEABLE, output);
	}
}
