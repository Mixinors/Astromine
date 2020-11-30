package com.github.chainmailstudios.astromine.datagen.generator.modelstate.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;

public class ColumnBlockSetModelStateGenerator extends GenericBlockSetModelStateGenerator {
	private final ResourceLocation endTexture;

	public ColumnBlockSetModelStateGenerator(MaterialItemType type, ResourceLocation endTexture) {
		super(type);
		this.endTexture = endTexture;
	}

	@Override
	public void generate(ModelStateData data, MaterialSet set) {
		TextureMapping texture = TextureMapping.column(TextureMapping.getBlockTexture(getBlock(set)), endTexture);
		ResourceLocation identifier = ModelTemplates.CUBE_COLUMN.create(getBlock(set), texture, data::addModel);
		data.addState(getBlock(set), ModelStateData.createSingletonBlockState(getBlock(set), identifier));
		data.addSimpleBlockItemModel(getBlock(set));
	}

	@Override
	public String getGeneratorName() {
		return type.getName() + "_column_block_set_modelstate";
	}
}
