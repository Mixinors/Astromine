package com.github.mixinors.astromine.datagen.model;

import java.util.Set;

import com.github.mixinors.astromine.datagen.family.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.MaterialFamily;
import com.github.mixinors.astromine.registry.common.AMBlocks;

import net.minecraft.block.Block;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.ModelIds;
import net.minecraft.data.client.model.Models;
import net.minecraft.data.client.model.Texture;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockStateDefinitionProvider;

public class AMModelProvider extends FabricBlockStateDefinitionProvider {
	public static final Set<Block> SIMPLE_CUBE_ALL = Set.of(
			AMBlocks.BLAZING_ASTEROID_STONE.get()
	);

	public AMModelProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		for(Block block:SIMPLE_CUBE_ALL) {
			blockStateModelGenerator.registerSimpleCubeAll(block);
		}

		MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateModels).forEach((family) -> {
			family.getBlockVariants().forEach(((variant, block) -> {
				if(family.shouldGenerateModel(variant)) {
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
				if(family.shouldGenerateModel(variant)) variant.getModelRegistrar().accept(itemModelGenerator, item);
			}));
		});
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
}
