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

public class TieredFacingModelStateGenerator extends GenericBlockModelStateGenerator {
	protected final MachineType type;

	public TieredFacingModelStateGenerator(MachineType type, Block... blocks) {
		super(blocks);
		this.type = type;
	}

	public Texture getTexture(Block block) {
		return (new Texture()).put(TextureKey.SIDE, getSubId(getTextureId(type), "_side")).put(TextureKey.FRONT, Texture.getSubId(block, "_front")).put(TextureKey.TOP, getSubId(getTextureId(type), "_top")).put(TextureKey.BOTTOM, getSubId(getTextureId(type), "_bottom"));
	}

	public static Identifier getTextureId(MachineType type) {
		switch(type) {
			case PRIMITIVE:
				return AstromineCommon.identifier("primitive_machine");
			case BASIC:
				return AstromineCommon.identifier("basic_machine");
			case ELITE:
				return AstromineCommon.identifier("elite_machine");
			default:
				return AstromineCommon.identifier("advanced_machine");
		}
	}

	public static Identifier getSubId(Identifier main, String suffix) {
		return new Identifier(main.getNamespace(), "block/" + main.getPath() + suffix);
	}

	@Override
	public void generate(ModelStateData data) {
		blocks.forEach((block) -> {
			Texture texture = getTexture(block);
			Identifier identifier = Models.ORIENTABLE_WITH_BOTTOM.upload(block, texture, data::addModel);
			data.addState(block, VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, identifier)).coordinate(facingMap()));
			data.addSimpleBlockItemModel(block);
		});
	}

	public static BlockStateVariantMap facingMap() {
		return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING).register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R90)).register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R180)).register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R270)).register(Direction.NORTH, BlockStateVariant.create());
	}
}
