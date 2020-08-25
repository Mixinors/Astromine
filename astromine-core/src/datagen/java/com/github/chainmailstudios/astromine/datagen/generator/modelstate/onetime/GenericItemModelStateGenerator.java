package com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime;

import net.minecraft.item.Item;

import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

import java.util.Arrays;
import java.util.List;

public class GenericItemModelStateGenerator implements OneTimeModelStateGenerator {
	protected final List<Item> items;

	public GenericItemModelStateGenerator(Item... items) {
		this.items = Arrays.asList(items);
	}

	@Override
	public String getGeneratorName() {
		return "generic_item_modelstate";
	}

	@Override
	public void generate(ModelStateData data) {
		items.forEach(data::addGeneratedItemModel);
	}
}
