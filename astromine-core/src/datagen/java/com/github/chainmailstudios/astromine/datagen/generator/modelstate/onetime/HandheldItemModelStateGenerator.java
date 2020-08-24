package com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime;

import net.minecraft.item.Item;

import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

import java.util.Arrays;
import java.util.List;

public class HandheldItemModelStateGenerator extends GenericItemModelStateGenerator {
	public HandheldItemModelStateGenerator(Item... items) {
		super(items);
	}

	@Override
	public String getGeneratorName() {
		return "handheld_item_modelstate";
	}

	@Override
	public void generate(ModelStateData data) {
		items.forEach(data::addHandheldItemModel);
	}
}
