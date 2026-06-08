/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.base.BlockWithEntity;
import com.github.mixinors.astromine.datagen.AMDatagenLists;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.AMMaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.family.MaterialFamily;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMProperties;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.WallSide;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AMModelProvider implements DataProvider {
	private final PackOutput.PathProvider blockStatePathProvider;
	private final PackOutput.PathProvider modelPathProvider;
	private Consumer<BlockStateGenerator> blockStateOutput;
	private BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
	private Set<Item> delegatedItems;
	
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
	
	public static final Map<Block, ResourceLocation> CABLE_CENTER_MODELS = Map.of(
			AMBlocks.PRIMITIVE_ENERGY_CABLE.get(), AMCommon.id("block/primitive_energy_cable_center"),
			AMBlocks.BASIC_ENERGY_CABLE.get(), AMCommon.id("block/basic_energy_cable_center"),
			AMBlocks.ADVANCED_ENERGY_CABLE.get(), AMCommon.id("block/advanced_energy_cable_center"),
			AMBlocks.ELITE_ENERGY_CABLE.get(), AMCommon.id("block/elite_energy_cable_center"),
			AMBlocks.FLUID_PIPE.get(), AMCommon.id("block/fluid_pipe_center"),
			AMBlocks.ITEM_CONDUIT.get(), AMCommon.id("block/item_conduit_center")
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
			
			AMItems.LOW_CAPACITY_ROCKET_FUEL_TANK.get(),
			AMItems.MEDIUM_CAPACITY_ROCKET_FUEL_TANK.get(),
			AMItems.HIGH_CAPACITY_ROCKET_FUEL_TANK.get(),
			
			AMItems.LOW_DURABILITY_ROCKET_HULL.get(),
			AMItems.MEDIUM_DURABILITY_ROCKET_HULL.get(),
			AMItems.HIGH_DURABILITY_ROCKET_HULL.get(),
			
			AMItems.STANDING_ROCKET_LANDING_MECHANISM.get(),
			AMItems.PERCHING_ROCKET_LANDING_MECHANISM.get(),
			AMItems.HOVERING_ROCKET_LANDING_MECHANISM.get(),
			
			AMItems.ROCKET_LIFE_SUPPORT.get(),
			
			AMItems.LOW_TEMPERATURE_ROCKET_SHIELDING.get(),
			AMItems.HIGH_TEMPERATURE_ROCKET_SHIELDING.get(),
			
			AMItems.LOW_EFFICIENCY_ROCKET_THRUSTER.get(),
			AMItems.MEDIUM_EFFICIENCY_ROCKET_THRUSTER.get(),
			AMItems.HIGH_EFFICIENCY_ROCKET_THRUSTER.get(),
			
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
	
	public AMModelProvider(PackOutput output) {
		this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
		this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		var blockStates = Maps.<Block, BlockStateGenerator>newHashMap();
		this.blockStateOutput = generator -> {
			var previous = blockStates.put(generator.getBlock(), generator);
			
			if (previous != null) {
				throw new IllegalStateException("Duplicate blockstate definition for " + generator.getBlock());
			}
		};
		var models = Maps.<ResourceLocation, Supplier<JsonElement>>newHashMap();
		this.modelOutput = (id, supplier) -> {
			var previous = models.put(id, supplier);
			
			if (previous != null) {
				throw new IllegalStateException("Duplicate model definition for " + id);
			}
		};
		this.delegatedItems = Sets.<Item>newHashSet();
		
		generateBlockStateModels(new BlockModelGenerators(this.blockStateOutput, this.modelOutput, this.delegatedItems::add));
		generateItemModels();
		
		return CompletableFuture.allOf(
				saveCollection(output, blockStates, block -> this.blockStatePathProvider.json(block.builtInRegistryHolder().key().location())),
				saveCollection(output, models, this.modelPathProvider::json)
		);
	}
	
	private static <T> CompletableFuture<?> saveCollection(CachedOutput output, Map<T, ? extends Supplier<JsonElement>> values, Function<T, Path> pathFactory) {
		return CompletableFuture.allOf(values.entrySet().stream().map(entry -> DataProvider.saveStable(output, entry.getValue().get(), pathFactory.apply(entry.getKey()))).toArray(CompletableFuture[]::new));
	}
	
	@Override
	public String getName() {
		return "Astromine Model Definitions";
	}
	
	public void generateFlatItem(Item item, ModelTemplate template) {
		template.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.modelOutput);
	}
	
	public void delegateItemModel(Block block, ResourceLocation model) {
		var item = block.asItem();
		
		if (!this.delegatedItems.add(item)) {
			return;
		}
		
		this.modelOutput.accept(ModelLocationUtils.getModelLocation(item), new DelegatedModel(model));
	}
	
	public void registerSimpleBlock(Block block, ResourceLocation model) {
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, model)));
	}
	
	public void registerSimpleCubeAll(Block block) {
		var model = isDynamic(block) ? ModelTemplates.CUBE_ALL.create(block, prefixedCube(block, "static_"), this.modelOutput) : TexturedModel.CUBE.create(block, this.modelOutput);

		if (isDynamic(block)) {
			var dynamicModel = ModelTemplates.CUBE_ALL.createWithSuffix(block, "_dynamic", prefixedCube(block, "dynamic_"), this.modelOutput);
			registerDynamicBlock(block, model, dynamicModel);
		} else {
			registerSimpleBlock(block, model);
		}

		delegateItemModel(block, model);
	}
	
	public void registerCubeColumn(Block cubeColumn, Block endTexture) {
		var texture = TextureMapping.column(TextureMapping.getBlockTexture(cubeColumn), TextureMapping.getBlockTexture(endTexture));
		var identifier = ModelTemplates.CUBE_COLUMN.create(cubeColumn, texture, this.modelOutput);
		registerSimpleBlock(cubeColumn, identifier);
	}
	
	public void registerAsteroidOre(Block asteroidOre) {
		registerCubeColumn(asteroidOre, AMBlocks.ASTEROID_STONE.get());
	}
	
	public void registerMeteorOre(Block meteorOre) {
		registerCubeColumn(meteorOre, AMBlocks.METEOR_STONE.get());
	}
	
	public void registerCauldron(Block cauldron) {
		registerSimpleBlock(cauldron, ModelTemplates.CAULDRON_FULL.create(cauldron, TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.WATER, "_still")), this.modelOutput));
	}
	
	public static ModelTemplate model(ResourceLocation parent, String variant, TextureSlot... requiredTextures) {
		return new ModelTemplate(Optional.of(parent), Optional.of(variant), requiredTextures);
	}
	
	public static ModelTemplate model(ResourceLocation parent, TextureSlot... requiredTextures) {
		return new ModelTemplate(Optional.of(parent), Optional.empty(), requiredTextures);
	}
	
	private static ModelTemplate blockModel(ResourceLocation parent, TextureSlot... requiredTextures) {
		return model(getBlockFolderId(parent), requiredTextures);
	}
	
	private static ModelTemplate blockModel(ResourceLocation parent, String variant, TextureSlot... requiredTextures) {
		return model(getBlockFolderId(parent), variant, requiredTextures);
	}
	
	private static ModelTemplate itemModel(ResourceLocation parent, TextureSlot... requiredTextures) {
		return model(getItemFolderId(parent), requiredTextures);
	}
	
	public static ResourceLocation getBlockFolderId(ResourceLocation id) {
		return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
	}
	
	public static ResourceLocation getItemFolderId(ResourceLocation id) {
		return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + id.getPath());
	}
	
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		AMBlockFamilies.getFamilies().filter(BlockFamily::shouldGenerateModel).forEach(this::registerBlockFamily);
		
		SIMPLE_CUBE_ALL.forEach((block) -> {
			blockStateModelGenerator.createTrivialCube(block);
			delegateItemModel(block, ModelLocationUtils.getModelLocation(block));
		});
		
		SIMPLE_STATE.forEach((block) -> {
			registerSimpleBlock(block, ModelLocationUtils.getModelLocation(block));
			delegateItemModel(block, ModelLocationUtils.getModelLocation(block));
		});
		
		JUST_STATE.forEach((block) -> registerSimpleBlock(block, ModelLocationUtils.getModelLocation(block)));
		
		CUBE_BOTTOM_TOP.forEach((block) -> {
			blockStateModelGenerator.createTrivialBlock(block, TexturedModel.CUBE_TOP_BOTTOM);
			delegateItemModel(block, ModelLocationUtils.getModelLocation(block));
		});
		
		CABLE_CENTER_MODELS.forEach(this::registerCable);
		registerSidingOverlayModels();

		AMDatagenLists.FluidLists.FLUIDS.forEach((fluid) -> {
			registerSimpleBlock(fluid.getBlock(), ModelLocationUtils.getModelLocation(Blocks.WATER));
		});
		
		AMDatagenLists.BlockLists.MACHINES.forEach((block) -> {
			registerMachine(block);
			delegateItemModel(block, ModelLocationUtils.getModelLocation(block));
		});
		
		AMMaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateModels).forEach((family) -> {
			family.getBlockVariants().forEach(((variant, block) -> {
				if (family.shouldGenerateModel(variant)) {
					variant.getModelRegistrar().accept(this, block);
					delegateItemModel(block, ModelLocationUtils.getModelLocation(block));
				}
			}));
		});
	}
	
	private void registerCable(Block block, ResourceLocation centerModel) {
		registerSimpleBlock(block, ModelLocationUtils.getModelLocation(block));
		
		this.modelOutput.accept(ModelLocationUtils.getModelLocation(block), () -> {
			var json = new JsonObject();
			
			json.addProperty("loader", AMCommon.id("cable").toString());
			json.addProperty("center", centerModel.toString());
			json.addProperty("side", AMCommon.id("block/cable_side").toString());
			json.addProperty("connector", AMCommon.id("block/cable_connector").toString());
			json.addProperty("insert_connector", AMCommon.id("block/cable_connector_insert").toString());
			json.addProperty("extract_connector", AMCommon.id("block/cable_connector_extract").toString());
			json.addProperty("insert_extract_connector", AMCommon.id("block/cable_connector_insert_extract").toString());
			
			return json;
		});
		
		delegateItemModel(block, centerModel);
	}

	private void registerBlockFamily(BlockFamily family) {
		var baseBlock = family.getBaseBlock();
		var dynamic = isDynamic(baseBlock);
		var baseTexture = dynamic ? prefixedCube(baseBlock, "static_") : TextureMapping.cube(baseBlock);
		var baseModel = ModelTemplates.CUBE_ALL.create(baseBlock, baseTexture, this.modelOutput);
		var dynamicBaseModel = dynamic ? ModelTemplates.CUBE_ALL.createWithSuffix(baseBlock, "_dynamic", prefixedCube(baseBlock, "dynamic_"), this.modelOutput) : null;

		if (dynamic) {
			registerDynamicBlock(baseBlock, baseModel, dynamicBaseModel);
		} else {
			registerSimpleBlock(baseBlock, baseModel);
		}
		delegateItemModel(baseBlock, baseModel);

		family.getVariants().forEach((variant, block) -> {
			switch (variant) {
				case SLAB -> {
					if (isDynamic(block)) {
						registerDynamicSlab(block, baseBlock, baseModel, dynamicBaseModel);
					} else {
						registerSlab(block, baseTexture, baseModel);
					}
				}
				case STAIRS -> {
					if (isDynamic(block)) {
						registerDynamicStairs(block, baseBlock);
					} else {
						registerStairs(block, baseTexture);
					}
				}
				case WALL -> {
					if (isDynamic(block)) {
						registerDynamicWall(block, baseBlock);
					} else {
						registerWall(block, baseTexture);
					}
				}
				default -> {
				}
			}
		});
	}

	private void registerDynamicBlock(Block block, ResourceLocation staticModel, ResourceLocation dynamicModel) {
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(AMProperties.DYNAMIC)
				.select(false, Variant.variant().with(VariantProperties.MODEL, staticModel))
				.select(true, Variant.variant().with(VariantProperties.MODEL, dynamicModel))));
	}

	private void registerSlab(Block slab, TextureMapping texture, ResourceLocation fullBlockModel) {
		var bottomModel = ModelTemplates.SLAB_BOTTOM.create(slab, texture, this.modelOutput);
		var topModel = ModelTemplates.SLAB_TOP.create(slab, texture, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(slab).with(PropertyDispatch.property(BlockStateProperties.SLAB_TYPE)
				.select(SlabType.BOTTOM, Variant.variant().with(VariantProperties.MODEL, bottomModel))
				.select(SlabType.TOP, Variant.variant().with(VariantProperties.MODEL, topModel))
				.select(SlabType.DOUBLE, Variant.variant().with(VariantProperties.MODEL, fullBlockModel))));
		delegateItemModel(slab, bottomModel);
	}

	private void registerDynamicSlab(Block slab, Block textureBlock, ResourceLocation staticFullBlockModel, ResourceLocation dynamicFullBlockModel) {
		var staticTexture = prefixedCube(textureBlock, "static_");
		var dynamicTexture = prefixedCube(textureBlock, "dynamic_");
		var staticBottomModel = ModelTemplates.SLAB_BOTTOM.create(slab, staticTexture, this.modelOutput);
		var staticTopModel = ModelTemplates.SLAB_TOP.create(slab, staticTexture, this.modelOutput);
		var dynamicBottomModel = ModelTemplates.SLAB_BOTTOM.createWithSuffix(slab, "_dynamic", dynamicTexture, this.modelOutput);
		var dynamicTopModel = ModelTemplates.SLAB_TOP.createWithSuffix(slab, "_top_dynamic", dynamicTexture, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(slab).with(PropertyDispatch.properties(BlockStateProperties.SLAB_TYPE, AMProperties.DYNAMIC)
				.select(SlabType.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, staticBottomModel))
				.select(SlabType.TOP, false, Variant.variant().with(VariantProperties.MODEL, staticTopModel))
				.select(SlabType.DOUBLE, false, Variant.variant().with(VariantProperties.MODEL, staticFullBlockModel))
				.select(SlabType.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, dynamicBottomModel))
				.select(SlabType.TOP, true, Variant.variant().with(VariantProperties.MODEL, dynamicTopModel))
				.select(SlabType.DOUBLE, true, Variant.variant().with(VariantProperties.MODEL, dynamicFullBlockModel))));
		delegateItemModel(slab, staticBottomModel);
	}

	private void registerStairs(Block stairs, TextureMapping texture) {
		var innerModel = ModelTemplates.STAIRS_INNER.create(stairs, texture, this.modelOutput);
		var straightModel = ModelTemplates.STAIRS_STRAIGHT.create(stairs, texture, this.modelOutput);
		var outerModel = ModelTemplates.STAIRS_OUTER.create(stairs, texture, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(stairs).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE)
				.generate((facing, half, shape) -> createStairsVariant(facing, half, shape, innerModel, straightModel, outerModel))));
		delegateItemModel(stairs, straightModel);
	}

	private void registerDynamicStairs(Block stairs, Block textureBlock) {
		var staticTexture = prefixedCube(textureBlock, "static_");
		var dynamicTexture = prefixedCube(textureBlock, "dynamic_");
		var staticInnerModel = ModelTemplates.STAIRS_INNER.create(stairs, staticTexture, this.modelOutput);
		var staticStraightModel = ModelTemplates.STAIRS_STRAIGHT.create(stairs, staticTexture, this.modelOutput);
		var staticOuterModel = ModelTemplates.STAIRS_OUTER.create(stairs, staticTexture, this.modelOutput);
		var dynamicInnerModel = ModelTemplates.STAIRS_INNER.createWithSuffix(stairs, "_inner_dynamic", dynamicTexture, this.modelOutput);
		var dynamicStraightModel = ModelTemplates.STAIRS_STRAIGHT.createWithSuffix(stairs, "_dynamic", dynamicTexture, this.modelOutput);
		var dynamicOuterModel = ModelTemplates.STAIRS_OUTER.createWithSuffix(stairs, "_outer_dynamic", dynamicTexture, this.modelOutput);

		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(stairs).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE, AMProperties.DYNAMIC)
				.generate((facing, half, shape, dynamic) -> dynamic
						? createStairsVariant(facing, half, shape, dynamicInnerModel, dynamicStraightModel, dynamicOuterModel)
						: createStairsVariant(facing, half, shape, staticInnerModel, staticStraightModel, staticOuterModel))));
		delegateItemModel(stairs, staticStraightModel);
	}

	private static Variant createStairsVariant(Direction facing, Half half, StairsShape shape, ResourceLocation innerModel, ResourceLocation straightModel, ResourceLocation outerModel) {
		var variant = Variant.variant();
		var model = switch (shape) {
			case INNER_LEFT, INNER_RIGHT -> innerModel;
			case OUTER_LEFT, OUTER_RIGHT -> outerModel;
			default -> straightModel;
		};

		variant.with(VariantProperties.MODEL, model);

		var yRotation = switch (facing) {
			case SOUTH -> VariantProperties.Rotation.R90;
			case WEST -> VariantProperties.Rotation.R180;
			case NORTH -> VariantProperties.Rotation.R270;
			default -> VariantProperties.Rotation.R0;
		};

		if (shape == StairsShape.INNER_RIGHT || shape == StairsShape.OUTER_RIGHT) {
			yRotation = rotateClockwise(yRotation);
		}

		if (half == Half.TOP) {
			variant.with(VariantProperties.X_ROT, VariantProperties.Rotation.R180);
		}

		if (yRotation != VariantProperties.Rotation.R0) {
			variant.with(VariantProperties.Y_ROT, yRotation);
			variant.with(VariantProperties.UV_LOCK, true);
		}

		return variant;
	}

	private static VariantProperties.Rotation rotateClockwise(VariantProperties.Rotation rotation) {
		return switch (rotation) {
			case R0 -> VariantProperties.Rotation.R90;
			case R90 -> VariantProperties.Rotation.R180;
			case R180 -> VariantProperties.Rotation.R270;
			case R270 -> VariantProperties.Rotation.R0;
		};
	}

	private void registerWall(Block wall, TextureMapping texture) {
		var postModel = ModelTemplates.WALL_POST.create(wall, texture, this.modelOutput);
		var lowSideModel = ModelTemplates.WALL_LOW_SIDE.create(wall, texture, this.modelOutput);
		var tallSideModel = ModelTemplates.WALL_TALL_SIDE.create(wall, texture, this.modelOutput);
		var inventoryModel = ModelTemplates.WALL_INVENTORY.create(wall, texture, this.modelOutput);

		this.blockStateOutput.accept(MultiPartGenerator.multiPart(wall)
				.with(Condition.condition().term(BlockStateProperties.UP, true), Variant.variant().with(VariantProperties.MODEL, postModel))
				.with(Condition.condition().term(BlockStateProperties.NORTH_WALL, WallSide.LOW), wallSide(lowSideModel, VariantProperties.Rotation.R0))
				.with(Condition.condition().term(BlockStateProperties.EAST_WALL, WallSide.LOW), wallSide(lowSideModel, VariantProperties.Rotation.R90))
				.with(Condition.condition().term(BlockStateProperties.SOUTH_WALL, WallSide.LOW), wallSide(lowSideModel, VariantProperties.Rotation.R180))
				.with(Condition.condition().term(BlockStateProperties.WEST_WALL, WallSide.LOW), wallSide(lowSideModel, VariantProperties.Rotation.R270))
				.with(Condition.condition().term(BlockStateProperties.NORTH_WALL, WallSide.TALL), wallSide(tallSideModel, VariantProperties.Rotation.R0))
				.with(Condition.condition().term(BlockStateProperties.EAST_WALL, WallSide.TALL), wallSide(tallSideModel, VariantProperties.Rotation.R90))
				.with(Condition.condition().term(BlockStateProperties.SOUTH_WALL, WallSide.TALL), wallSide(tallSideModel, VariantProperties.Rotation.R180))
				.with(Condition.condition().term(BlockStateProperties.WEST_WALL, WallSide.TALL), wallSide(tallSideModel, VariantProperties.Rotation.R270)));
		delegateItemModel(wall, inventoryModel);
	}

	private void registerDynamicWall(Block wall, Block textureBlock) {
		var staticTexture = prefixedCube(textureBlock, "static_");
		var dynamicTexture = prefixedCube(textureBlock, "dynamic_");
		var staticPostModel = ModelTemplates.WALL_POST.create(wall, staticTexture, this.modelOutput);
		var staticLowSideModel = ModelTemplates.WALL_LOW_SIDE.create(wall, staticTexture, this.modelOutput);
		var staticTallSideModel = ModelTemplates.WALL_TALL_SIDE.create(wall, staticTexture, this.modelOutput);
		var staticInventoryModel = ModelTemplates.WALL_INVENTORY.create(wall, staticTexture, this.modelOutput);
		var dynamicPostModel = ModelTemplates.WALL_POST.createWithSuffix(wall, "_post_dynamic", dynamicTexture, this.modelOutput);
		var dynamicLowSideModel = ModelTemplates.WALL_LOW_SIDE.createWithSuffix(wall, "_side_dynamic", dynamicTexture, this.modelOutput);
		var dynamicTallSideModel = ModelTemplates.WALL_TALL_SIDE.createWithSuffix(wall, "_side_tall_dynamic", dynamicTexture, this.modelOutput);

		this.blockStateOutput.accept(MultiPartGenerator.multiPart(wall)
				.with(Condition.condition().term(AMProperties.DYNAMIC, false).term(BlockStateProperties.UP, true), Variant.variant().with(VariantProperties.MODEL, staticPostModel))
				.with(Condition.condition().term(AMProperties.DYNAMIC, false).term(BlockStateProperties.NORTH_WALL, WallSide.LOW), wallSide(staticLowSideModel, VariantProperties.Rotation.R0))
				.with(Condition.condition().term(AMProperties.DYNAMIC, false).term(BlockStateProperties.EAST_WALL, WallSide.LOW), wallSide(staticLowSideModel, VariantProperties.Rotation.R90))
				.with(Condition.condition().term(AMProperties.DYNAMIC, false).term(BlockStateProperties.SOUTH_WALL, WallSide.LOW), wallSide(staticLowSideModel, VariantProperties.Rotation.R180))
				.with(Condition.condition().term(AMProperties.DYNAMIC, false).term(BlockStateProperties.WEST_WALL, WallSide.LOW), wallSide(staticLowSideModel, VariantProperties.Rotation.R270))
				.with(Condition.condition().term(AMProperties.DYNAMIC, false).term(BlockStateProperties.NORTH_WALL, WallSide.TALL), wallSide(staticTallSideModel, VariantProperties.Rotation.R0))
				.with(Condition.condition().term(AMProperties.DYNAMIC, false).term(BlockStateProperties.EAST_WALL, WallSide.TALL), wallSide(staticTallSideModel, VariantProperties.Rotation.R90))
				.with(Condition.condition().term(AMProperties.DYNAMIC, false).term(BlockStateProperties.SOUTH_WALL, WallSide.TALL), wallSide(staticTallSideModel, VariantProperties.Rotation.R180))
				.with(Condition.condition().term(AMProperties.DYNAMIC, false).term(BlockStateProperties.WEST_WALL, WallSide.TALL), wallSide(staticTallSideModel, VariantProperties.Rotation.R270))
				.with(Condition.condition().term(AMProperties.DYNAMIC, true).term(BlockStateProperties.UP, true), Variant.variant().with(VariantProperties.MODEL, dynamicPostModel))
				.with(Condition.condition().term(AMProperties.DYNAMIC, true).term(BlockStateProperties.NORTH_WALL, WallSide.LOW), wallSide(dynamicLowSideModel, VariantProperties.Rotation.R0))
				.with(Condition.condition().term(AMProperties.DYNAMIC, true).term(BlockStateProperties.EAST_WALL, WallSide.LOW), wallSide(dynamicLowSideModel, VariantProperties.Rotation.R90))
				.with(Condition.condition().term(AMProperties.DYNAMIC, true).term(BlockStateProperties.SOUTH_WALL, WallSide.LOW), wallSide(dynamicLowSideModel, VariantProperties.Rotation.R180))
				.with(Condition.condition().term(AMProperties.DYNAMIC, true).term(BlockStateProperties.WEST_WALL, WallSide.LOW), wallSide(dynamicLowSideModel, VariantProperties.Rotation.R270))
				.with(Condition.condition().term(AMProperties.DYNAMIC, true).term(BlockStateProperties.NORTH_WALL, WallSide.TALL), wallSide(dynamicTallSideModel, VariantProperties.Rotation.R0))
				.with(Condition.condition().term(AMProperties.DYNAMIC, true).term(BlockStateProperties.EAST_WALL, WallSide.TALL), wallSide(dynamicTallSideModel, VariantProperties.Rotation.R90))
				.with(Condition.condition().term(AMProperties.DYNAMIC, true).term(BlockStateProperties.SOUTH_WALL, WallSide.TALL), wallSide(dynamicTallSideModel, VariantProperties.Rotation.R180))
				.with(Condition.condition().term(AMProperties.DYNAMIC, true).term(BlockStateProperties.WEST_WALL, WallSide.TALL), wallSide(dynamicTallSideModel, VariantProperties.Rotation.R270)));
		delegateItemModel(wall, staticInventoryModel);
	}

	private static boolean isDynamic(Block block) {
		return block.defaultBlockState().hasProperty(AMProperties.DYNAMIC);
	}

	private static TextureMapping prefixedCube(Block block, String prefix) {
		var blockId = BuiltInRegistries.BLOCK.getKey(block);
		var texture = ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), "block/" + prefix + blockId.getPath());
		return new TextureMapping()
				.put(TextureSlot.ALL, texture)
				.put(TextureSlot.BOTTOM, texture)
				.put(TextureSlot.TOP, texture)
				.put(TextureSlot.SIDE, texture)
				.put(TextureSlot.WALL, texture)
				.put(TextureSlot.PARTICLE, texture);
	}

	private static Variant wallSide(ResourceLocation model, VariantProperties.Rotation yRotation) {
		var variant = Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.UV_LOCK, true);

		if (yRotation != VariantProperties.Rotation.R0) {
			variant.with(VariantProperties.Y_ROT, yRotation);
		}

		return variant;
	}
	
	public void generateItemModels() {
		AMMaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateModels).forEach((family) -> {
			family.getItemVariants().forEach(((variant, item) -> {
				if (family.shouldGenerateModel(variant)) {
					variant.getModelRegistrar().accept(this, item);
				}
			}));
		});
		
		AMDatagenLists.FluidLists.FLUIDS.forEach((fluid) -> generateFlatItem(fluid.getBucket(), ModelTemplates.FLAT_ITEM));
		
		GENERATED.forEach((item) -> generateFlatItem(item, ModelTemplates.FLAT_ITEM));
		
		HANDHELD.forEach((item) -> generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM));
	}
	
	public void registerMachine(Block machine) {
		var inactiveId = createMachineModel(machine, "");
		var activeId = createMachineModel(machine, "_active");
		
		var inactiveVariant = Variant.variant().with(VariantProperties.MODEL, inactiveId);
		var activeVariant = Variant.variant().with(VariantProperties.MODEL, activeId);
		var northVariant = Variant.variant();
		var eastVariant = Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
		var southVariant = Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
		var westVariant = Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
		
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(machine).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockWithEntity.ACTIVE)
																																	  .generate((facing, active) -> {
																																		  var facingVariant = switch (facing) {
																																			  case EAST -> eastVariant;
																																			  case SOUTH -> southVariant;
																																			  case WEST -> westVariant;
																																			  default -> northVariant;
																																		  };
																																		  return Variant.merge(facingVariant, active ? activeVariant : inactiveVariant);
																																	  })));
	}
	
	private ResourceLocation createMachineModel(Block machine, String suffix) {
		var modelId = ModelLocationUtils.getModelLocation(machine, suffix);
		var baseModelId = ResourceLocation.fromNamespaceAndPath(modelId.getNamespace(), modelId.getPath() + "_base");
		var textureSuffix = machine == AMBlocks.ROCKET_CONTROLLER.get() && suffix.equals("_active") ? "" : suffix;

		this.modelOutput.accept(baseModelId, () -> {
			var json = new JsonObject();
			json.addProperty("parent", AMCommon.id("block/machine").toString());

			var textures = new JsonObject();
			textures.addProperty("top", TextureMapping.getBlockTexture(machine, "_top" + textureSuffix).toString());
			textures.addProperty("bottom", TextureMapping.getBlockTexture(machine, "_bottom" + textureSuffix).toString());
			textures.addProperty("left", TextureMapping.getBlockTexture(machine, "_left" + textureSuffix).toString());
			textures.addProperty("right", TextureMapping.getBlockTexture(machine, "_right" + textureSuffix).toString());
			textures.addProperty("front", TextureMapping.getBlockTexture(machine, "_front" + textureSuffix).toString());
			textures.addProperty("back", TextureMapping.getBlockTexture(machine, "_back" + textureSuffix).toString());
			textures.addProperty("particle", TextureMapping.getBlockTexture(machine, "_top" + textureSuffix).toString());
			
			json.add("textures", textures);
			
			return json;
		});

		this.modelOutput.accept(modelId, () -> {
			var json = new JsonObject();
			json.addProperty("loader", AMCommon.id("machine").toString());
			json.addProperty("base", baseModelId.toString());

			return json;
		});

		return modelId;
	}

	private void registerSidingOverlayModels() {
		createSidingOverlayModel("siding_overlay_insert", "insert", 4.0F, 4.0F, 12.0F, 12.0F);
		createSidingOverlayModel("siding_overlay_extract", "extract", 4.0F, 4.0F, 12.0F, 12.0F);
		createSidingOverlayModel("siding_overlay_insert_extract", "insert_extract", 4.0F, 4.0F, 12.0F, 12.0F);
		createSidingOverlayModel("siding_overlay_item_insert", "insert", 2.0F, 9.0F, 8.0F, 15.0F);
		createSidingOverlayModel("siding_overlay_item_extract", "extract", 2.0F, 9.0F, 8.0F, 15.0F);
		createSidingOverlayModel("siding_overlay_item_insert_extract", "insert_extract", 2.0F, 9.0F, 8.0F, 15.0F);
		createSidingOverlayModel("siding_overlay_fluid_insert", "insert", 8.0F, 1.0F, 14.0F, 7.0F);
		createSidingOverlayModel("siding_overlay_fluid_extract", "extract", 8.0F, 1.0F, 14.0F, 7.0F);
		createSidingOverlayModel("siding_overlay_fluid_insert_extract", "insert_extract", 8.0F, 1.0F, 14.0F, 7.0F);
	}

	private void createSidingOverlayModel(String path, String texture, float minX, float minY, float maxX, float maxY) {
		this.modelOutput.accept(AMCommon.id("block/" + path), () -> {
			var json = new JsonObject();
			json.addProperty("render_type", "minecraft:cutout");

			var textureId = AMCommon.id("block/siding/" + texture).toString();
			var textures = new JsonObject();
			textures.addProperty("siding", textureId);
			textures.addProperty("particle", textureId);
			json.add("textures", textures);

			var elements = new JsonArray();
			var element = new JsonObject();
			element.add("from", vector(minX, minY, -0.02F));
			element.add("to", vector(maxX, maxY, 0.0F));
			element.addProperty("shade", false);

			var faces = new JsonObject();
			var north = new JsonObject();
			north.add("uv", vector(0.0F, 0.0F, 16.0F, 16.0F));
			north.addProperty("texture", "#siding");
			faces.add("north", north);
			element.add("faces", faces);
			elements.add(element);
			json.add("elements", elements);

			return json;
		});
	}

	private static JsonArray vector(float... values) {
		var array = new JsonArray();

		for (var value : values) {
			array.add(value);
		}

		return array;
	}
}
