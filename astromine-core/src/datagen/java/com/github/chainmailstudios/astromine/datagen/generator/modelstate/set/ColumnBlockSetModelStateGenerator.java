package com.github.chainmailstudios.astromine.datagen.generator.modelstate.set;

import net.minecraft.data.client.model.Models;
import net.minecraft.data.client.model.Texture;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

public class ColumnBlockSetModelStateGenerator extends GenericBlockSetModelStateGenerator {
	private final Identifier endTexture;

	public ColumnBlockSetModelStateGenerator(MaterialItemType type, Identifier endTexture) {
		super(type);
		this.endTexture = endTexture;
	}

	@Override
	public void generate(ModelStateData data, MaterialSet set) {
		Texture texture = Texture.sideEnd(Texture.getId(getBlock(set)), endTexture);
		Identifier identifier = Models.CUBE_COLUMN.upload(getBlock(set), texture, data::addModel);
		data.addState(getBlock(set), ModelStateData.createSingletonBlockState(getBlock(set), identifier));
		data.addSimpleBlockItemModel(getBlock(set));
	}

	@Override
	public String getGeneratorName() {
		return type.getName() + "_column_block_set_modelstate";
	}
}
