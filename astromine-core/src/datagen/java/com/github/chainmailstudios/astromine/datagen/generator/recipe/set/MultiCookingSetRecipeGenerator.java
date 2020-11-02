package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import net.minecraft.item.Item;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.CookingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

import java.util.ArrayList;
import java.util.Collection;

public abstract class MultiCookingSetRecipeGenerator extends CookingSetRecipeGenerator {
	private final Collection<MaterialItemType> inputs;

	public MultiCookingSetRecipeGenerator(Collection<MaterialItemType> inputs, MaterialItemType output, int time, float experience) {
		super(inputs.stream().findFirst().get(), output, time, experience);
		this.inputs = inputs;
	}

	public Collection<MaterialItemType> getInputs() {
		return inputs;
	}

	public Collection<Item> getInputs(MaterialSet set) {
		ArrayList<Item> items = new ArrayList<>();
		for (MaterialItemType type : getInputs()) {
			if (shouldGenerate(set, type)) items.add(set.getItem(type));
		}
		return items;
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		boolean hasInput = false;
		for (MaterialItemType type : getInputs()) {
			if (set.hasType(type)) hasInput = true;
		}
		return hasInput && set.hasType(output);
	}

	public boolean shouldGenerate(MaterialSet set, MaterialItemType type) {
		return set.hasType(output) && set.hasType(type) && !(set.isFromVanilla(type) && set.isFromVanilla(output));
	}
}
