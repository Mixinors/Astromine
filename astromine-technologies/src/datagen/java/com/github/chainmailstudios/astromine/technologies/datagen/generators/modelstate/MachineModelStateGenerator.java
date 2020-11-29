package com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate;

import net.minecraft.block.Block;
import net.minecraft.data.client.model.BlockStateVariant;
import net.minecraft.data.client.model.BlockStateVariantMap;
import net.minecraft.data.client.model.Model;
import net.minecraft.data.client.model.ModelIds;
import net.minecraft.data.client.model.Texture;
import net.minecraft.data.client.model.TextureKey;
import net.minecraft.data.client.model.VariantSettings;
import net.minecraft.data.client.model.VariantsBlockStateSupplier;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.base.BlockWithEntity;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericBlockModelStateGenerator;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

import java.util.Optional;

public class MachineModelStateGenerator extends GenericBlockModelStateGenerator {
	private static final Model MACHINE = getModel("cube", TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST, TextureKey.UP, TextureKey.DOWN, TextureKey.PARTICLE);

	public MachineModelStateGenerator(Block... blocks) {
		super(blocks);
	}

	public static BlockStateVariantMap getActiveMap(ResourceLocation active, ResourceLocation inactive) {
		return BlockStateVariantMap.create(BlockWithEntity.ACTIVE).register(Boolean.TRUE, BlockStateVariant.create().put(VariantSettings.MODEL, active)).register(Boolean.FALSE, BlockStateVariant.create().put(VariantSettings.MODEL, inactive));
	}

	private static Model getModel(String parent, TextureKey... requiredTextures) {
		return new Model(Optional.of(new ResourceLocation("minecraft", "block/" + parent)), Optional.empty(), requiredTextures);
	}

	private static String getPathNoTier(Block block) {
		ResourceLocation id = Registry.BLOCK.getId(block);
		// astromine:primitive_alloy_smelter

		String path = id.getPath();
		// primitive_alloy_smelter

		path = path
				.replace("primitive_",  "")
				.replace("basic_", "")
				.replace("advanced_", "")
				.replace("elite_", "")
				.replace("creative_", "");
		// alloy_smelter

		return path;
	}

	public static BlockStateVariantMap facingMap() {
		return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING).register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R90)).register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R180)).register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R270)).register(Direction.NORTH, BlockStateVariant.create());
	}

	private static String getPath(Block block) {
		ResourceLocation id = Registry.BLOCK.getId(block);
		// astromine:primitive_alloy_smelter

		return id.getPath();

	}

	public Texture getTexture(Block block, boolean active) {
		return new Texture()
				.put(TextureKey.SOUTH, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") +  getPath(block) + "_back"))
				.put(TextureKey.NORTH, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") +  getPath(block) + "_front"))
				.put(TextureKey.WEST, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") +  getPath(block) + "_right"))
				.put(TextureKey.EAST, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") +  getPath(block) + "_left"))
				.put(TextureKey.UP, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") +  getPath(block) + "_top"))
				.put(TextureKey.DOWN, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") +  getPath(block) + "_bottom"))
				.put(TextureKey.PARTICLE, AstromineCommon.identifier("block/machine/generic_machine_top"));
	}

	@Override
	public String getGeneratorName() {
		return "machine_modelstate";
	}

	@Override
	public void generate(ModelStateData data) {
		blocks.forEach((block) -> {
			Texture activeTexture = getTexture(block, true);

			ResourceLocation active = MACHINE.upload(block, "_active", activeTexture, data::addModel);

			Texture inactiveTexture = getTexture(block, false);

			ResourceLocation inactive = MACHINE.upload(block, "_inactive", inactiveTexture, data::addModel);

			data.addState(block, VariantsBlockStateSupplier.create(block).coordinate(facingMap()).coordinate(getActiveMap(active, inactive)));

			data.addSimpleItemModel(Item.BLOCK_ITEMS.get(block), ModelIds.getBlockSubModelId(block, "_inactive"));
		});
	}
}
