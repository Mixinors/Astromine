package com.github.chainmailstudios.astromine.technologies.datagen.generators.modelstate;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.base.BlockWithEntity;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericBlockModelStateGenerator;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

public class MachineModelStateGenerator extends GenericBlockModelStateGenerator {
	private static final ModelTemplate MACHINE = getModel("cube",
		TextureSlot.NORTH,
		TextureSlot.SOUTH,
		TextureSlot.EAST,
		TextureSlot.WEST,
		TextureSlot.UP,
		TextureSlot.DOWN,
		TextureSlot.PARTICLE);

	public MachineModelStateGenerator(Block... blocks) {
		super(blocks);
	}

	public static PropertyDispatch getActiveMap(ResourceLocation active, ResourceLocation inactive) {
		return PropertyDispatch.property(BlockWithEntity.ACTIVE)
			.select(Boolean.TRUE, Variant.variant().with(VariantProperties.MODEL, active))
			.select(Boolean.FALSE, Variant.variant().with(VariantProperties.MODEL, inactive));
	}

	private static ModelTemplate getModel(String parent, TextureSlot... requiredTextures) {
		return new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "block/" + parent)), Optional.empty(), requiredTextures);
	}

	private static String getPathNoTier(Block block) {
		ResourceLocation id = Registry.BLOCK.getKey(block);
		// astromine:primitive_alloy_smelter

		String path = id.getPath();
		// primitive_alloy_smelter

		path = path
			.replace("primitive_", "")
			.replace("basic_", "")
			.replace("advanced_", "")
			.replace("elite_", "")
			.replace("creative_", "");
		// alloy_smelter

		return path;
	}

	public static PropertyDispatch facingMap() {
		return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
			.select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
			.select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
			.select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
			.select(Direction.NORTH, Variant.variant());
	}

	private static String getPath(Block block) {
		ResourceLocation id = Registry.BLOCK.getKey(block);
		// astromine:primitive_alloy_smelter

		return id.getPath();

	}

	public TextureMapping getTexture(Block block, boolean active) {
		return new TextureMapping()
			.put(TextureSlot.SOUTH, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") + getPath(block) + "_back"))
			.put(TextureSlot.NORTH, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") + getPath(block) + "_front"))
			.put(TextureSlot.WEST, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") + getPath(block) + "_right"))
			.put(TextureSlot.EAST, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") + getPath(block) + "_left"))
			.put(TextureSlot.UP, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") + getPath(block) + "_top"))
			.put(TextureSlot.DOWN, AstromineCommon.identifier("block/" + getPathNoTier(block) + "/" + (active ? "active/" : "inactive/") + getPath(block) + "_bottom"))
			.put(TextureSlot.PARTICLE, AstromineCommon.identifier("block/machine/generic_machine_top"));
	}

	@Override
	public String getGeneratorName() {
		return "machine_modelstate";
	}

	@Override
	public void generate(ModelStateData data) {
		blocks.forEach((block) -> {
			TextureMapping activeTexture = getTexture(block, true);

			ResourceLocation active = MACHINE.createWithSuffix(block, "_active", activeTexture, data::addModel);

			TextureMapping inactiveTexture = getTexture(block, false);

			ResourceLocation inactive = MACHINE.createWithSuffix(block, "_inactive", inactiveTexture, data::addModel);

			data.addState(block, MultiVariantGenerator.multiVariant(block)
				.with(facingMap())
				.with(getActiveMap(active, inactive)));

			data.addSimpleItemModel(Item.BY_BLOCK.get(block), ModelLocationUtils.getModelLocation(block, "_inactive"));
		});
	}
}
