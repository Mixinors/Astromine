/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.datagen.provider;

import java.util.Optional;
import java.util.Set;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.base.BlockWithEntity;
import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMItems;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.*;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockStateDefinitionProvider;

public class AMModelProvider extends FabricBlockStateDefinitionProvider {
	/**
	 * Blocks with a single cube model, with the same texture on all sides
	 */
	public static final Set<Block> SIMPLE_CUBE_ALL = Set.of(
			AMBlocks.BLAZING_ASTEROID_STONE.get()
	);

	/**
	 * Blocks with a single model, where the block model itself isn't data generated
	 */
	public static final Set<Block> SIMPLE_STATE = Set.of(
			AMBlocks.SPACE_SLIME_BLOCK.get()
	);

	/**
	 * Blocks with a single model, where neither the block model nor the item model are data generated
	 */
	public static final Set<Block> JUST_STATE = Set.of(
			AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.get()
	);

	public static final Set<Block> CUBE_BOTTOM_TOP = Set.of(
			AMBlocks.NUCLEAR_WARHEAD.get()
	);

	/**
	 * Items with the GENERATED model
	 */
	public static final Set<Item> GENERATED = Set.of(
			AMItems.ENERGY.get(),
			AMItems.FLUID.get(),
			AMItems.ITEM.get(),

			AMItems.MANUAL.get(),

			AMItems.SPACE_SLIME_SPAWN_EGG.get(),

			AMItems.SPACE_SLIME_BALL.get(),

			AMItems.BIOFUEL.get(),

			AMItems.MACHINE_CHASSIS.get(),

			AMItems.BASIC_MACHINE_UPGRADE_KIT.get(),
			AMItems.ADVANCED_MACHINE_UPGRADE_KIT.get(),
			AMItems.ELITE_MACHINE_UPGRADE_KIT.get(),

			AMItems.PRIMITIVE_PLATING.get(),
			AMItems.BASIC_PLATING.get(),
			AMItems.ADVANCED_PLATING.get(),
			AMItems.ELITE_PLATING.get(),

			AMItems.PORTABLE_TANK.get(),
			AMItems.LARGE_PORTABLE_TANK.get(),

			AMItems.PRIMITIVE_CIRCUIT.get(),
			AMItems.BASIC_CIRCUIT.get(),
			AMItems.ADVANCED_CIRCUIT.get(),
			AMItems.ELITE_CIRCUIT.get(),

			AMItems.PRIMITIVE_BATTERY.get(),
			AMItems.BASIC_BATTERY.get(),
			AMItems.ADVANCED_BATTERY.get(),
			AMItems.ELITE_BATTERY.get(),
			AMItems.CREATIVE_BATTERY.get(),

			AMItems.PRIMITIVE_BATTERY_PACK.get(),
			AMItems.BASIC_BATTERY_PACK.get(),
			AMItems.ADVANCED_BATTERY_PACK.get(),
			AMItems.ELITE_BATTERY_PACK.get(),
			AMItems.CREATIVE_BATTERY_PACK.get(),

			AMItems.PRIMITIVE_ROCKET_FUEL_TANK.get(),
			AMItems.PRIMITIVE_ROCKET_PLATING.get(),
			AMItems.PRIMITIVE_ROCKET_HULL.get(),
			AMItems.PRIMITIVE_ROCKET_BOOSTER.get(),

			AMItems.SPACE_SUIT_HELMET.get(),
			AMItems.SPACE_SUIT_CHESTPLATE.get(),
			AMItems.SPACE_SUIT_LEGGINGS.get(),
			AMItems.SPACE_SUIT_BOOTS.get()
	);

	/**
	 * Items with the HANDHELD model
	 */
	public static final Set<Item> HANDHELD = Set.of(
			AMItems.BLADES.get(),

			AMItems.PRIMITIVE_DRILL.get(),
			AMItems.BASIC_DRILL.get(),
			AMItems.ADVANCED_DRILL.get(),
			AMItems.ELITE_DRILL.get(),

			AMItems.DRILL_HEAD.get(),

			AMItems.PRIMITIVE_DRILL_BASE.get(),
			AMItems.BASIC_DRILL_BASE.get(),
			AMItems.ADVANCED_DRILL_BASE.get(),
			AMItems.ELITE_DRILL_BASE.get(),

			AMItems.HOLOGRAPHIC_CONNECTOR.get()
	);

	public static final TextureKey LEFT = TextureKey.of("left", TextureKey.EAST);
	public static final TextureKey RIGHT = TextureKey.of("right", TextureKey.WEST);

	public static final Model MACHINE = blockModel(AMCommon.id("machine"), TextureKey.TOP, TextureKey.BOTTOM, LEFT, RIGHT, TextureKey.FRONT, TextureKey.BACK);
	public static final Model MACHINE_ACTIVE = blockModel(AMCommon.id("machine_active"), "_active", TextureKey.TOP, TextureKey.BOTTOM, LEFT, RIGHT, TextureKey.FRONT, TextureKey.BACK);

	public AMModelProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	public static void registerCubeColumn(BlockStateModelGenerator blockStateModelGenerator, Block cubeColumn, Block endTexture) {
		Texture texture = Texture.sideEnd(Texture.getId(cubeColumn), Texture.getId(endTexture));
		Identifier identifier = Models.CUBE_COLUMN.upload(cubeColumn, texture, blockStateModelGenerator.modelCollector);
		blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(cubeColumn, identifier));
	}

	public static void registerAsteroidOre(BlockStateModelGenerator blockStateModelGenerator, Block asteroidOre) {
		registerCubeColumn(blockStateModelGenerator, asteroidOre, AMBlocks.ASTEROID_STONE.get());
	}

	public static void registerMeteorOre(BlockStateModelGenerator blockStateModelGenerator, Block meteorOre) {
		registerCubeColumn(blockStateModelGenerator, meteorOre, AMBlocks.METEOR_STONE.get());
	}

	public static void registerCauldron(BlockStateModelGenerator blockStateModelGenerator, Block cauldron) {
		blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(cauldron, Models.TEMPLATE_CAULDRON_FULL.upload(cauldron, Texture.cauldron(Texture.getSubId(Blocks.WATER, "_still")), blockStateModelGenerator.modelCollector)));
	}

	public static Model model(Identifier parent, String variant, TextureKey... requiredTextures) {
		return new Model(Optional.of(parent), Optional.of(variant), requiredTextures);
	}

	public static Model model(Identifier parent, TextureKey... requiredTextures) {
		return new Model(Optional.of(parent), Optional.empty(), requiredTextures);
	}

	private static Model blockModel(Identifier parent, TextureKey... requiredTextures) {
		return model(getBlockFolderId(parent), requiredTextures);
	}

	private static Model blockModel(Identifier parent, String variant, TextureKey... requiredTextures) {
		return model(getBlockFolderId(parent), variant, requiredTextures);
	}

	private static Model itemModel(Identifier parent, TextureKey... requiredTextures) {
		return model(getItemFolderId(parent), requiredTextures);
	}

	public static Identifier getBlockFolderId(Identifier id) {
		return new Identifier(id.getNamespace(), "block/" + id.getPath());
	}

	public static Identifier getItemFolderId(Identifier id) {
		return new Identifier(id.getNamespace(), "item/" + id.getPath());
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		BlockFamilies.getFamilies().filter(AMBlockFamilies::isAstromineFamily).filter(BlockFamily::shouldGenerateModels).forEach(family -> {
			blockStateModelGenerator.registerCubeAllModelTexturePool(family.getBaseBlock()).family(family);
			blockStateModelGenerator.registerParentedItemModel(family.getBaseBlock(), ModelIds.getBlockModelId(family.getBaseBlock()));
		});

		SIMPLE_CUBE_ALL.forEach((block) -> {
			blockStateModelGenerator.registerSimpleCubeAll(block);
			blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
		});

		SIMPLE_STATE.forEach((block) -> {
			blockStateModelGenerator.registerSimpleState(block);
			blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
		});

		JUST_STATE.forEach(blockStateModelGenerator::registerSimpleState);

		CUBE_BOTTOM_TOP.forEach((block) -> {
			blockStateModelGenerator.registerSingleton(block, TexturedModel.CUBE_BOTTOM_TOP);
			blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
		});

		AMDatagen.FLUIDS.forEach((fluid) -> {
			blockStateModelGenerator.registerStateWithModelReference(fluid.getBlock(), Blocks.WATER);
			registerCauldron(blockStateModelGenerator, fluid.getCauldron());
		});

		AMDatagen.MACHINES.forEach((block) -> {
			registerMachine(blockStateModelGenerator, block);
			blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
		});

		MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateModels).forEach((family) -> {
			family.getBlockVariants().forEach(((variant, block) -> {
				if (family.shouldGenerateModel(variant)) {
					variant.getModelRegistrar().accept(blockStateModelGenerator, block);
					blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
				}
			}));
		});
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateModels).forEach((family) -> {
			family.getItemVariants().forEach(((variant, item) -> {
				if (family.shouldGenerateModel(variant)) variant.getModelRegistrar().accept(itemModelGenerator, item);
			}));
		});

		AMDatagen.FLUIDS.forEach((fluid) -> itemModelGenerator.register(fluid.getBucketItem(), Models.GENERATED));

		GENERATED.forEach((item) -> itemModelGenerator.register(item, Models.GENERATED));

		HANDHELD.forEach((item) -> itemModelGenerator.register(item, Models.HANDHELD));
	}

	public static void registerMachine(BlockStateModelGenerator blockStateModelGenerator, Block machine) {
		Texture inactiveTexture = new Texture().put(TextureKey.TOP, Texture.getSubId(machine, "_top")).put(TextureKey.BOTTOM, Texture.getSubId(machine, "_bottom")).put(LEFT, Texture.getSubId(machine, "_left")).put(RIGHT, Texture.getSubId(machine, "_right")).put(TextureKey.FRONT, Texture.getSubId(machine, "_front")).put(TextureKey.BACK, Texture.getSubId(machine, "_back"));
		Texture activeTexture = new Texture().put(TextureKey.TOP, Texture.getSubId(machine, "_top_active")).put(TextureKey.BOTTOM, Texture.getSubId(machine, "_bottom_active")).put(LEFT, Texture.getSubId(machine, "_left_active")).put(RIGHT, Texture.getSubId(machine, "_right_active")).put(TextureKey.FRONT, Texture.getSubId(machine, "_front_active")).put(TextureKey.BACK, Texture.getSubId(machine, "_back_active"));
		Identifier inactiveId = MACHINE.upload(machine, inactiveTexture, blockStateModelGenerator.modelCollector);
		Identifier activeId = MACHINE_ACTIVE.upload(machine, activeTexture, blockStateModelGenerator.modelCollector);

		BlockStateVariant inactiveVariant = BlockStateVariant.create().put(VariantSettings.MODEL, inactiveId);
		BlockStateVariant activeVariant = BlockStateVariant.create().put(VariantSettings.MODEL, activeId);
		BlockStateVariant northVariant = BlockStateVariant.create();
		BlockStateVariant eastVariant = BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R90);
		BlockStateVariant southVariant = BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R180);
		BlockStateVariant westVariant = BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R270);

		blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(machine).coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, BlockWithEntity.ACTIVE)
				.register((facing, active) -> {
					BlockStateVariant facingVariant = switch (facing) {
						case EAST -> eastVariant;
						case SOUTH -> southVariant;
						case WEST -> westVariant;
						default -> northVariant;
					};
					return BlockStateVariant.union(facingVariant, active ? activeVariant : inactiveVariant);
				})));
	}
}
