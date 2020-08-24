package com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate;

import net.minecraft.block.Block;
import net.minecraft.data.client.model.BlockStateVariant;
import net.minecraft.data.client.model.BlockStateVariantMap;
import net.minecraft.data.client.model.ModelIds;
import net.minecraft.data.client.model.Models;
import net.minecraft.data.client.model.Texture;
import net.minecraft.data.client.model.TextureKey;
import net.minecraft.data.client.model.VariantSettings;
import net.minecraft.data.client.model.VariantsBlockStateSupplier;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.base.BlockWithEntity;
import com.github.chainmailstudios.astromine.common.utilities.type.MachineType;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericBlockModelStateGenerator;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

public class MachineModelStateGenerator extends TieredFacingModelStateGenerator {

	public MachineModelStateGenerator(MachineType type, Block... blocks) {
		super(type, blocks);
	}

	public Texture getTexture(Block block, boolean active) {
		return (new Texture()).put(TextureKey.SIDE, getSubId(getTextureId(type), "_side")).put(TextureKey.FRONT, Texture.getSubId(block, "_front_"+(active?"active":"inactive"))).put(TextureKey.TOP, getSubId(getTextureId(type), "_top")).put(TextureKey.BOTTOM, getSubId(getTextureId(type), "_bottom"));
	}

	@Override
	public String getGeneratorName() {
		return "machine_modelstate";
	}

	@Override
	public void generate(ModelStateData data) {
		blocks.forEach((block) -> {
			Texture activeTexture = getTexture(block, true);
			Identifier active = Models.ORIENTABLE_WITH_BOTTOM.upload(block, "_active", activeTexture, data::addModel);
			Texture inactiveTexture = getTexture(block, false);
			Identifier inactive = Models.ORIENTABLE_WITH_BOTTOM.upload(block, "_inactive", inactiveTexture, data::addModel);
			data.addState(block, VariantsBlockStateSupplier.create(block).coordinate(facingMap()).coordinate(activeMap(active, inactive)));
			data.addSimpleItemModel(Item.BLOCK_ITEMS.get(block), ModelIds.getBlockSubModelId(block, "_inactive"));
		});
	}

	public static BlockStateVariantMap activeMap(Identifier active, Identifier inactive) {
		return BlockStateVariantMap.create(BlockWithEntity.ACTIVE).register(Boolean.TRUE, BlockStateVariant.create().put(VariantSettings.MODEL, active)).register(Boolean.FALSE, BlockStateVariant.create().put(VariantSettings.MODEL, inactive));
	}
}
