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

package com.github.mixinors.astromine.gametest;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.entity.HoloBridgeProjectorBlockEntity;
import com.github.mixinors.astromine.common.block.entity.cable.CableBlockEntity;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.AlloySmelterBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.ElectricFurnaceBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.ElectrolyzerBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.FluidMixerBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.MelterBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.PresserBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.RefineryBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.SolidifierBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.TrituratorBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.WireMillBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.generator.FluidGeneratorBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.generator.SolidGeneratorBlockEntity;
import com.github.mixinors.astromine.common.block.entity.storage.TankBlockEntity;
import com.github.mixinors.astromine.common.block.entity.utility.PumpBlockEntity;
import com.github.mixinors.astromine.common.block.network.CableBlock;
import com.github.mixinors.astromine.common.component.world.NetworksComponent;
import com.github.mixinors.astromine.common.item.utility.MachineUpgradeKitItem;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.recipe.AlloySmeltingRecipe;
import com.github.mixinors.astromine.common.recipe.ElectrolyzingRecipe;
import com.github.mixinors.astromine.common.recipe.FluidGeneratingRecipe;
import com.github.mixinors.astromine.common.recipe.FluidMixingRecipe;
import com.github.mixinors.astromine.common.recipe.MeltingRecipe;
import com.github.mixinors.astromine.common.recipe.PressingRecipe;
import com.github.mixinors.astromine.common.recipe.RefiningRecipe;
import com.github.mixinors.astromine.common.recipe.SolidifyingRecipe;
import com.github.mixinors.astromine.common.recipe.TrituratingRecipe;
import com.github.mixinors.astromine.common.recipe.WireMillingRecipe;
import com.github.mixinors.astromine.common.transfer.RedstoneType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.NetworkUtils;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMBiomes;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.github.mixinors.astromine.registry.common.AMItemGroups;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMNetworkTypes;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.IItemHandler;

@GameTestHolder(AMCommon.MOD_ID)
@PrefixGameTestTemplate(false)
public final class AMGameTests {
	private static final String TEMPLATE = "empty";
	
	private AMGameTests() {
	}
	
	@GameTest(template = TEMPLATE)
	public static void registrationsLoad(GameTestHelper helper) {
		helper.assertTrue(BuiltInRegistries.BLOCK.getKey(AMBlocks.PRIMITIVE_TANK.get()).equals(AMBlocks.PRIMITIVE_TANK.getId()), "primitive tank block should be registered");
		helper.assertTrue(BuiltInRegistries.ITEM.getKey(AMItems.PRIMITIVE_BATTERY.get()).equals(AMItems.PRIMITIVE_BATTERY.getId()), "primitive battery item should be registered");
		helper.assertTrue(BuiltInRegistries.FLUID.getKey(AMFluids.OIL.getSource()).equals(AMFluids.OIL.still().getId()), "oil source fluid should be registered");
		helper.assertTrue(AMBlockEntityTypes.PRIMITIVE_TANK.get() != null, "primitive tank block entity type should be registered");
		helper.assertTrue(AMItemGroups.isDisplayable(new ItemStack(AMBlocks.PRIMITIVE_TANK.get().asItem())), "primitive tank should be displayable in the creative tab");
		helper.assertTrue(!AMItemGroups.isDisplayable(new ItemStack(AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.get().asItem())), "invisible holo bridge block should not be displayable in the creative tab");
		
		var stationControllerPos = new BlockPos(0, 0, 0);
		helper.setBlock(stationControllerPos, AMBlocks.STATION_CONTROLLER.get());
		helper.assertTrue(helper.getBlockEntity(stationControllerPos).getType() == AMBlockEntityTypes.STATION_CONTROLLER.get(), "station controller should use station block entity type");
		
		var server = helper.getLevel().getServer();
		helper.assertTrue(BuiltInRegistries.BIOME_SOURCE.containsKey(AMCommon.id("moon_orbit")), "moon orbit biome source should be registered");
		helper.assertTrue(BuiltInRegistries.CHUNK_GENERATOR.containsKey(AMCommon.id("moon_orbit")), "moon orbit chunk generator should be registered");

		var dimensionTypes = server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE);
		var moonOrbitType = dimensionTypes.getHolderOrThrow(AMWorlds.MOON_ORBIT_DIMENSION_TYPE_KEY);
		helper.assertTrue(AMWorlds.isVacuum(moonOrbitType), "moon orbit should be tagged as a vacuum dimension");

		assertNoPrecipitation(helper, AMBiomes.ASTEROID_BELT_KEY);
		assertNoPrecipitation(helper, AMBiomes.MOON_LIGHT_SIDE_KEY);
		assertNoPrecipitation(helper, AMBiomes.MOON_DARK_SIDE_KEY);
		assertNoPrecipitation(helper, AMBiomes.MOON_CRATER_FIELD_KEY);
		assertNoPrecipitation(helper, AMBiomes.ROCKET_KEY);
		
		helper.succeed();
	}

	@GameTest(template = TEMPLATE)
	public static void upgradeKitTierRulesLoad(GameTestHelper helper) {
		var item = AMItems.BASIC_MACHINE_UPGRADE_KIT.get();

		helper.assertTrue(item instanceof MachineUpgradeKitItem, "basic upgrade kit should use machine upgrade behavior");

		var kit = (MachineUpgradeKitItem) item;

		helper.assertTrue(kit.isValidFor(Tier.PRIMITIVE), "basic upgrade kit should upgrade primitive machines");
		helper.assertTrue(kit.isObsoleteFor(Tier.BASIC), "basic upgrade kit should reject basic machines with feedback");
		helper.assertTrue(kit.isObsoleteFor(Tier.ADVANCED), "basic upgrade kit should reject advanced machines with feedback");
		helper.assertTrue(kit.isObsoleteFor(Tier.ELITE), "basic upgrade kit should reject elite machines with feedback");
		helper.assertTrue(kit.isObsoleteFor(Tier.CREATIVE), "basic upgrade kit should reject creative machines with feedback");
		helper.succeed();
	}

	@GameTest(template = TEMPLATE)
	public static void holographicBridgeProjectorsBuildBridge(GameTestHelper helper) {
		var parentPos = new BlockPos(0, 0, 0);
		var childPos = new BlockPos(4, 0, 0);
		var bridgePos = new BlockPos(1, 1, 0);

		helper.setBlock(parentPos, AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.EAST));
		helper.setBlock(childPos, AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.WEST));
		helper.setBlock(bridgePos, Blocks.AIR);
		helper.setBlock(new BlockPos(2, 1, 0), Blocks.AIR);
		helper.setBlock(new BlockPos(3, 1, 0), Blocks.AIR);

		var parent = helper.getBlockEntity(parentPos);
		var child = helper.getBlockEntity(childPos);

		helper.assertTrue(parent instanceof HoloBridgeProjectorBlockEntity, "expected parent holographic bridge projector");
		helper.assertTrue(child instanceof HoloBridgeProjectorBlockEntity, "expected child holographic bridge projector");

		var parentProjector = (HoloBridgeProjectorBlockEntity) parent;
		var childProjector = (HoloBridgeProjectorBlockEntity) child;

		helper.assertTrue(parentProjector.attemptToBuildBridge(childProjector), "holographic bridge projectors should have a clear path");

		parentProjector.setChild(childProjector);
		parentProjector.buildBridge();

		helper.assertTrue(parentProjector.segments != null && !parentProjector.segments.isEmpty(), "holographic bridge should build render segments");
		helper.assertBlockPresent(AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.get(), bridgePos);
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE)
	public static void energyStorageTransfers(GameTestHelper helper) {
		var source = new LongEnergyStorage(1000L, 250L, 125L, 500L);
		var destination = new LongEnergyStorage(1000L, 100L, 100L);
		
		helper.assertValueEqual(source.insert(100L, true), 100L, "simulated energy insertion");
		helper.assertValueEqual(source.getAmount(), 500L, "source energy after simulated insertion");
		helper.assertValueEqual(source.extract(200L, false), 125L, "extracted energy should respect max extraction");
		helper.assertValueEqual(source.getAmount(), 375L, "source energy after extraction");
		helper.assertValueEqual(LongEnergyStorage.move(source, destination, 200L), 100L, "moved energy should respect destination insertion");
		helper.assertValueEqual(source.getAmount(), 275L, "source energy after move");
		helper.assertValueEqual(destination.getAmount(), 100L, "destination energy after move");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE)
	public static void itemStorageTransfers(GameTestHelper helper) {
		var storage = new SimpleItemStorage(2)
				.insertPredicate(($, $$) -> true)
				.extractPredicate(($, $$) -> true);
		var input = new ItemStack(Items.COBBLESTONE, 48);
		
		var remainder = storage.insertItem(0, input, false);
		
		helper.assertTrue(remainder.isEmpty(), "item insertion should accept the full stack");
		helper.assertValueEqual(storage.getStackInSlot(0).getCount(), 48, "stored item count after insertion");
		
		var extracted = storage.extractItem(0, 17, false);
		
		helper.assertValueEqual(extracted.getCount(), 17, "extracted item count");
		helper.assertValueEqual(storage.getStackInSlot(0).getCount(), 31, "stored item count after extraction");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE)
	public static void fluidStorageTransfers(GameTestHelper helper) {
		var storage = new SimpleFluidStorage(1, FluidType.BUCKET_VOLUME * 2L)
				.insertPredicate(($, $$) -> true)
				.extractPredicate(($, $$) -> true);
		var water = new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
		
		helper.assertValueEqual(storage.fill(water, IFluidHandler.FluidAction.EXECUTE), FluidType.BUCKET_VOLUME, "filled fluid amount");
		helper.assertValueEqual(storage.getFluidInTank(0).getAmount(), FluidType.BUCKET_VOLUME, "stored fluid amount after fill");
		
		var drained = storage.drain(new FluidStack(Fluids.WATER, 400), IFluidHandler.FluidAction.EXECUTE);
		
		helper.assertValueEqual(drained.getAmount(), 400, "drained fluid amount");
		helper.assertTrue(FluidStack.isSameFluidSameComponents(drained, new FluidStack(Fluids.WATER, 400)), "drained fluid should be water");
		helper.assertValueEqual(storage.getFluidInTank(0).getAmount(), 600, "stored fluid amount after drain");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE)
	public static void itemEnergyAndFluidCapabilitiesLoad(GameTestHelper helper) {
		var battery = new ItemStack(AMItems.PRIMITIVE_BATTERY.get());
		var energy = battery.getCapability(Capabilities.EnergyStorage.ITEM);
		
		helper.assertTrue(energy != null, "primitive battery should expose NeoForge energy capability");
		helper.assertTrue(energy.receiveEnergy(1, false) > 0, "primitive battery should receive energy");
		
		var portableTank = new ItemStack(AMItems.PORTABLE_TANK.get());
		var fluid = portableTank.getCapability(Capabilities.FluidHandler.ITEM);
		
		helper.assertTrue(fluid != null, "portable tank should expose NeoForge fluid capability");
		helper.assertTrue(fluid.fill(new FluidStack(Fluids.WATER, 1), IFluidHandler.FluidAction.EXECUTE) > 0, "portable tank should receive fluid");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE)
	public static void placedStorageCapabilitiesLoad(GameTestHelper helper) {
		var capacitorPos = new BlockPos(0, 0, 0);
		var tankPos = new BlockPos(1, 0, 0);
		var bufferPos = new BlockPos(2, 0, 0);
		
		helper.setBlock(capacitorPos, AMBlocks.PRIMITIVE_CAPACITOR.get());
		helper.setBlock(tankPos, AMBlocks.PRIMITIVE_TANK.get());
		helper.setBlock(bufferPos, AMBlocks.PRIMITIVE_BUFFER.get());
		
		var capacitorEnergy = helper.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, helper.absolutePos(capacitorPos), null);
		var tankFluid = helper.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, helper.absolutePos(tankPos), null);
		var tankItems = helper.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, helper.absolutePos(tankPos), null);
		var bufferItems = helper.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, helper.absolutePos(bufferPos), null);
		
		helper.assertTrue(capacitorEnergy != null, "placed capacitor should expose energy capability");
		helper.assertTrue(tankFluid != null, "placed tank should expose fluid capability");
		helper.assertTrue(tankItems != null, "placed tank should expose item capability");
		helper.assertTrue(bufferItems != null, "placed buffer should expose item capability");
		
		assertEnergyReceives(helper, capacitorEnergy);
		helper.assertTrue(tankFluid.fill(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE) > 0, "placed tank should receive water");
		helper.assertTrue(tankItems.insertItem(0, new ItemStack(Items.WATER_BUCKET), true).isEmpty(), "placed tank should accept filled fluid containers");
		helper.assertTrue(bufferItems.getSlots() > 0, "placed buffer item capability should report slots");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE)
	public static void logisticsNetworksTransferStorageContents(GameTestHelper helper) {
		var itemSourcePos = new BlockPos(0, 0, 0);
		var itemDestinationPos = new BlockPos(1, 0, 0);
		var fluidSourcePos = new BlockPos(2, 0, 0);
		var fluidDestinationPos = new BlockPos(3, 0, 0);
		var energySourcePos = new BlockPos(4, 0, 0);
		var energyDestinationPos = new BlockPos(5, 0, 0);
		
		helper.setBlock(itemSourcePos, AMBlocks.PRIMITIVE_BUFFER.get());
		helper.setBlock(itemDestinationPos, AMBlocks.PRIMITIVE_BUFFER.get());
		helper.setBlock(fluidSourcePos, AMBlocks.PRIMITIVE_TANK.get());
		helper.setBlock(fluidDestinationPos, AMBlocks.PRIMITIVE_TANK.get());
		helper.setBlock(energySourcePos, AMBlocks.PRIMITIVE_CAPACITOR.get());
		helper.setBlock(energyDestinationPos, AMBlocks.PRIMITIVE_CAPACITOR.get());
		
		var itemSource = getItemHandler(helper, itemSourcePos);
		var itemDestination = getItemHandler(helper, itemDestinationPos);
		var fluidSource = getFluidHandler(helper, fluidSourcePos);
		var fluidDestination = getFluidHandler(helper, fluidDestinationPos);
		var energySource = getEnergyStorage(helper, energySourcePos);
		var energyDestination = getEnergyStorage(helper, energyDestinationPos);
		
		helper.assertTrue(itemSource.insertItem(0, new ItemStack(Items.COBBLESTONE), false).isEmpty(), "item source should accept cobblestone");
		helper.assertTrue(fluidSource.fill(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE) > 0, "fluid source should accept water");
		helper.assertTrue(energySource.receiveEnergy(100, false) > 0, "energy source should accept energy");
		
		var itemNetwork = new Network<IItemHandler>(helper.getLevel(), AMNetworkTypes.ITEM);
		itemNetwork.getMembers().add(new Network.Member(helper.absolutePos(itemSourcePos), null, StorageSiding.EXTRACT));
		itemNetwork.getMembers().add(new Network.Member(helper.absolutePos(itemDestinationPos), null, StorageSiding.INSERT));
		itemNetwork.tick();
		
		var fluidNetwork = new Network<IFluidHandler>(helper.getLevel(), AMNetworkTypes.FLUID);
		fluidNetwork.getMembers().add(new Network.Member(helper.absolutePos(fluidSourcePos), null, StorageSiding.EXTRACT));
		fluidNetwork.getMembers().add(new Network.Member(helper.absolutePos(fluidDestinationPos), null, StorageSiding.INSERT));
		fluidNetwork.tick();
		
		var energyNetwork = new Network<IEnergyStorage>(helper.getLevel(), AMNetworkTypes.PRIMITIVE_ENERGY);
		energyNetwork.getMembers().add(new Network.Member(helper.absolutePos(energySourcePos), null, StorageSiding.EXTRACT));
		energyNetwork.getMembers().add(new Network.Member(helper.absolutePos(energyDestinationPos), null, StorageSiding.INSERT));
		var sourceEnergyBeforeTransfer = energySource.getEnergyStored();
		var destinationEnergyBeforeTransfer = energyDestination.getEnergyStored();
		energyNetwork.tick();
		
		helper.assertTrue(itemSource.getStackInSlot(0).isEmpty(), "item source should be drained by item network");
		helper.assertTrue(itemDestination.getStackInSlot(0).is(Items.COBBLESTONE), "item destination should receive cobblestone");
		helper.assertTrue(fluidSource.getFluidInTank(0).isEmpty(), "fluid source should be drained by fluid network");
		helper.assertTrue(FluidStack.isSameFluidSameComponents(fluidDestination.getFluidInTank(0), new FluidStack(Fluids.WATER, fluidDestination.getFluidInTank(0).getAmount())), "fluid destination should receive water");
		helper.assertTrue(
				energySource.getEnergyStored() < sourceEnergyBeforeTransfer,
				"energy source should be drained by energy network: " + sourceEnergyBeforeTransfer + " -> " + energySource.getEnergyStored()
		);
		helper.assertTrue(
				energyDestination.getEnergyStored() > destinationEnergyBeforeTransfer,
				"energy destination should receive energy: " + destinationEnergyBeforeTransfer + " -> " + energyDestination.getEnergyStored()
		);
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE)
	public static void placedItemConduitTracesAndTransfers(GameTestHelper helper) {
		var sourcePos = new BlockPos(0, 0, 0);
		var conduitPos = new BlockPos(1, 0, 0);
		var destinationPos = new BlockPos(2, 0, 0);
		
		helper.setBlock(sourcePos, AMBlocks.PRIMITIVE_BUFFER.get());
		helper.setBlock(conduitPos, AMBlocks.ITEM_CONDUIT.get());
		helper.setBlock(destinationPos, AMBlocks.PRIMITIVE_BUFFER.get());
		
		configureItemSiding(helper, sourcePos, Direction.EAST, StorageSiding.EXTRACT);
		configureItemSiding(helper, destinationPos, Direction.WEST, StorageSiding.INSERT);
		configureCable(helper, conduitPos, Direction.WEST, StorageSiding.EXTRACT);
		configureCable(helper, conduitPos, Direction.EAST, StorageSiding.INSERT);
		NetworkUtils.trace(AMNetworkTypes.ITEM, helper.getLevel(), helper.absolutePos(conduitPos));
		
		var component = NetworksComponent.get(helper.getLevel());
		helper.assertTrue(component != null, "level should expose networks component");
		
		var network = component.get(AMNetworkTypes.ITEM, helper.absolutePos(conduitPos));
		helper.assertTrue(network != null, "item conduit should trace an item network");
		helper.assertValueEqual(network.getNodes().size(), 1, "traced item network node count");
		helper.assertValueEqual(network.getMembers().size(), 2, "traced item network member count");
		
		var source = getItemHandler(helper, sourcePos);
		var destination = getItemHandler(helper, destinationPos);
		
		helper.assertTrue(source.insertItem(0, new ItemStack(Items.COBBLESTONE), false).isEmpty(), "source buffer should accept cobblestone");
		
		component.tick();
		
		helper.assertTrue(source.getStackInSlot(0).isEmpty(), "source buffer should be drained through placed conduit");
		helper.assertTrue(destination.getStackInSlot(0).is(Items.COBBLESTONE), "destination buffer should receive cobblestone through placed conduit");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE)
	public static void placedFluidPipeTracesAndTransfers(GameTestHelper helper) {
		var sourcePos = new BlockPos(0, 0, 0);
		var pipePos = new BlockPos(1, 0, 0);
		var destinationPos = new BlockPos(2, 0, 0);
		
		helper.setBlock(sourcePos, AMBlocks.PRIMITIVE_TANK.get());
		helper.setBlock(pipePos, AMBlocks.FLUID_PIPE.get());
		helper.setBlock(destinationPos, AMBlocks.PRIMITIVE_TANK.get());
		
		configureFluidSiding(helper, sourcePos, Direction.EAST, StorageSiding.EXTRACT);
		configureFluidSiding(helper, destinationPos, Direction.WEST, StorageSiding.INSERT);
		configureCable(helper, pipePos, Direction.WEST, StorageSiding.EXTRACT);
		configureCable(helper, pipePos, Direction.EAST, StorageSiding.INSERT);
		NetworkUtils.trace(AMNetworkTypes.FLUID, helper.getLevel(), helper.absolutePos(pipePos));
		
		var source = getFluidHandler(helper, sourcePos);
		var destination = getFluidHandler(helper, destinationPos);
		
		helper.assertValueEqual(source.fill(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE), FluidType.BUCKET_VOLUME, "source tank water insertion");
		
		var component = NetworksComponent.get(helper.getLevel());
		helper.assertTrue(component != null, "level should expose networks component");
		component.tick();
		
		helper.assertTrue(source.getFluidInTank(0).getAmount() < FluidType.BUCKET_VOLUME, "source tank should be drained through placed fluid pipe");
		assertFluid(helper, destination, 0, Fluids.WATER, 1, "destination tank should receive water through placed fluid pipe");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 120)
	public static void directTankFeedsFluidGeneratorAfterLateSidingChange(GameTestHelper helper) {
		var tankPos = new BlockPos(0, 0, 0);
		var generatorPos = new BlockPos(1, 0, 0);
		
		helper.setBlock(tankPos, AMBlocks.PRIMITIVE_TANK.get());
		helper.setBlock(generatorPos, AMBlocks.PRIMITIVE_FLUID_GENERATOR.get());
		
		var generatorCache = BlockCapabilityCache.create(
				Capabilities.FluidHandler.BLOCK,
				(ServerLevel) helper.getLevel(),
				helper.absolutePos(generatorPos),
				Direction.WEST
		);
		
		helper.assertTrue(generatorCache.getCapability() == null, "generator west fluid capability should initially be unavailable");
		
		var tank = getFluidHandler(helper, tankPos);
		
		helper.assertValueEqual(tank.fill(new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE), FluidType.BUCKET_VOLUME, "tank lava insertion");
		
		configureFluidSiding(helper, tankPos, Direction.EAST, StorageSiding.INSERT_EXTRACT);
		configureFluidSiding(helper, generatorPos, Direction.WEST, StorageSiding.INSERT);
		
		helper.assertTrue(generatorCache.getCapability() != null, "generator west fluid capability should refresh after siding change");
		
		var generator = getFluidHandler(helper, generatorPos);
		
		helper.succeedWhen(() -> {
			assertFluid(helper, generator, FluidGeneratorBlockEntity.INPUT_SLOT, Fluids.LAVA, 1, "tank should feed lava into adjacent fluid generator without re-placement");
			helper.assertTrue(tank.getFluidInTank(0).getAmount() < FluidType.BUCKET_VOLUME, "tank should lose lava to adjacent fluid generator");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 120)
	public static void fluidPipeRetracesWhenSidingChangesAfterInitialTrace(GameTestHelper helper) {
		var tankPos = new BlockPos(0, 0, 0);
		var pipePos = new BlockPos(1, 0, 0);
		var generatorPos = new BlockPos(2, 0, 0);
		
		helper.setBlock(tankPos, AMBlocks.PRIMITIVE_TANK.get());
		helper.setBlock(pipePos, AMBlocks.FLUID_PIPE.get());
		helper.setBlock(generatorPos, AMBlocks.PRIMITIVE_FLUID_GENERATOR.get());
		
		configureCable(helper, pipePos, Direction.WEST, StorageSiding.EXTRACT);
		configureCable(helper, pipePos, Direction.EAST, StorageSiding.INSERT);
		NetworkUtils.trace(AMNetworkTypes.FLUID, helper.getLevel(), helper.absolutePos(pipePos));
		
		var component = NetworksComponent.get(helper.getLevel());
		helper.assertTrue(component != null, "level should expose networks component");
		
		var initiallyTraced = component.get(AMNetworkTypes.FLUID, helper.absolutePos(pipePos));
		helper.assertTrue(initiallyTraced != null, "fluid pipe should trace an initial network");
		helper.assertValueEqual(initiallyTraced.getMembers().size(), 0, "initial network should not use null-side fallback members");
		
		var tank = getFluidHandler(helper, tankPos);
		var generator = getFluidHandler(helper, generatorPos);
		
		helper.assertValueEqual(tank.fill(new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE), FluidType.BUCKET_VOLUME, "tank lava insertion");
		
		configureFluidSiding(helper, tankPos, Direction.EAST, StorageSiding.INSERT_EXTRACT);
		configureFluidSiding(helper, generatorPos, Direction.WEST, StorageSiding.INSERT);
		
		helper.succeedWhen(() -> {
			var retraced = component.get(AMNetworkTypes.FLUID, helper.absolutePos(pipePos));
			
			helper.assertTrue(retraced != null, "fluid pipe should still have a traced network");
			helper.assertValueEqual(retraced.getMembers().size(), 2, "fluid pipe should retrace members after siding changes");
			assertFluid(helper, generator, FluidGeneratorBlockEntity.INPUT_SLOT, Fluids.LAVA, 1, "fluid pipe should feed lava into generator after late siding changes");
			helper.assertTrue(tank.getFluidInTank(0).getAmount() < FluidType.BUCKET_VOLUME, "tank should lose lava through fluid pipe");
		});
	}
	
	@GameTest(template = TEMPLATE)
	public static void placedEnergyCableTracesAndTransfers(GameTestHelper helper) {
		var sourcePos = new BlockPos(0, 0, 0);
		var cablePos = new BlockPos(1, 0, 0);
		var destinationPos = new BlockPos(2, 0, 0);
		
		helper.setBlock(sourcePos, AMBlocks.PRIMITIVE_CAPACITOR.get());
		helper.setBlock(cablePos, AMBlocks.PRIMITIVE_ENERGY_CABLE.get());
		helper.setBlock(destinationPos, AMBlocks.PRIMITIVE_CAPACITOR.get());
		
		configureCable(helper, cablePos, Direction.WEST, StorageSiding.EXTRACT);
		configureCable(helper, cablePos, Direction.EAST, StorageSiding.INSERT);
		NetworkUtils.trace(AMNetworkTypes.PRIMITIVE_ENERGY, helper.getLevel(), helper.absolutePos(cablePos));
		
		var source = getEnergyStorage(helper, sourcePos);
		var destination = getEnergyStorage(helper, destinationPos);
		
		helper.assertTrue(source.receiveEnergy(1000, false) > 0, "source capacitor should accept energy");
		
		var sourceBefore = source.getEnergyStored();
		var destinationBefore = destination.getEnergyStored();
		
		var component = NetworksComponent.get(helper.getLevel());
		helper.assertTrue(component != null, "level should expose networks component");
		component.tick();
		
		helper.assertTrue(source.getEnergyStored() < sourceBefore, "source capacitor should be drained through placed energy cable");
		helper.assertTrue(destination.getEnergyStored() > destinationBefore, "destination capacitor should receive energy through placed energy cable");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 400)
	public static void placedItemConduitFeedsProcessingMachine(GameTestHelper helper) {
		var sourcePos = new BlockPos(0, 0, 0);
		var conduitPos = new BlockPos(1, 0, 0);
		var furnacePos = new BlockPos(2, 0, 0);
		
		helper.setBlock(sourcePos, AMBlocks.PRIMITIVE_BUFFER.get());
		helper.setBlock(conduitPos, AMBlocks.ITEM_CONDUIT.get());
		helper.setBlock(furnacePos, AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get());
		
		configureItemSiding(helper, sourcePos, Direction.EAST, StorageSiding.EXTRACT);
		configureItemSiding(helper, furnacePos, Direction.WEST, StorageSiding.INSERT);
		configureCable(helper, conduitPos, Direction.WEST, StorageSiding.EXTRACT);
		configureCable(helper, conduitPos, Direction.EAST, StorageSiding.INSERT);
		NetworkUtils.trace(AMNetworkTypes.ITEM, helper.getLevel(), helper.absolutePos(conduitPos));
		
		var source = getItemHandler(helper, sourcePos);
		var furnaceItems = getItemHandler(helper, furnacePos);
		var furnaceEnergy = getEnergyStorage(helper, furnacePos);
		
		helper.assertTrue(source.insertItem(0, new ItemStack(Items.RAW_IRON), false).isEmpty(), "source buffer should accept raw iron");
		
		for (var i = 0; i < 16 && furnaceEnergy.getEnergyStored() < 1000; ++i) {
			furnaceEnergy.receiveEnergy(1000, false);
		}
		
		var component = NetworksComponent.get(helper.getLevel());
		helper.assertTrue(component != null, "level should expose networks component");
		component.tick();
		
		helper.assertTrue(source.getStackInSlot(0).isEmpty(), "source buffer should be drained into machine through placed conduit");
		helper.assertTrue(furnaceItems.getStackInSlot(0).is(Items.RAW_IRON), "furnace should receive raw iron through placed conduit");
		
		helper.succeedWhen(() -> {
			helper.assertTrue(furnaceItems.getStackInSlot(0).isEmpty(), "electric furnace should consume conduit-fed input");
			helper.assertTrue(furnaceItems.getStackInSlot(1).is(Items.IRON_INGOT), "electric furnace should process conduit-fed raw iron");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 400)
	public static void utilityBlockPlacerPlacesStoredBlock(GameTestHelper helper) {
		var placerPos = new BlockPos(0, 0, 0);
		var targetPos = new BlockPos(1, 0, 0);
		
		helper.setBlock(placerPos, AMBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.EAST));
		
		var items = getItemHandler(helper, placerPos);
		var energy = getEnergyStorage(helper, placerPos);
		
		helper.assertTrue(items.insertItem(0, new ItemStack(Items.STONE), false).isEmpty(), "block placer should accept placeable block item");
		chargeEnergy(helper, energy, 1000);
		
		helper.succeedWhen(() -> {
			helper.assertBlockPresent(Blocks.STONE, targetPos);
			helper.assertTrue(items.getStackInSlot(0).isEmpty(), "block placer should consume the placed block item");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 400)
	public static void utilityBlockBreakerBreaksAndStoresDrops(GameTestHelper helper) {
		var breakerPos = new BlockPos(0, 0, 0);
		var targetPos = new BlockPos(1, 0, 0);
		
		helper.setBlock(breakerPos, AMBlocks.BLOCK_BREAKER.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.EAST));
		helper.setBlock(targetPos, Blocks.STONE);
		
		var items = getItemHandler(helper, breakerPos);
		var energy = getEnergyStorage(helper, breakerPos);
		
		chargeEnergy(helper, energy, 1000);
		
		helper.succeedWhen(() -> {
			helper.assertBlockPresent(Blocks.AIR, targetPos);
			helper.assertTrue(items.getStackInSlot(0).is(Items.COBBLESTONE) || items.getStackInSlot(0).is(Items.STONE), "block breaker should store target block drops");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 400)
	public static void utilityFluidCollectorCollectsSourceFluid(GameTestHelper helper) {
		var collectorPos = new BlockPos(0, 0, 0);
		var targetPos = new BlockPos(1, 0, 0);
		
		helper.setBlock(collectorPos, AMBlocks.FLUID_EXTRACTOR.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.EAST));
		helper.setBlock(targetPos, Blocks.WATER);
		
		var fluids = getFluidHandler(helper, collectorPos);
		var energy = getEnergyStorage(helper, collectorPos);
		
		chargeEnergy(helper, energy, 1000);
		
		helper.succeedWhen(() -> {
			helper.assertBlockPresent(Blocks.AIR, targetPos);
			assertFluid(helper, fluids, 0, Fluids.WATER, FluidType.BUCKET_VOLUME, "fluid collector should store collected water");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 400)
	public static void utilityFluidPlacerPlacesStoredFluid(GameTestHelper helper) {
		var placerPos = new BlockPos(0, 0, 0);
		var targetPos = new BlockPos(1, 0, 0);
		
		helper.setBlock(placerPos, AMBlocks.FLUID_INSERTER.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.EAST));
		
		var fluids = getFluidHandler(helper, placerPos);
		var energy = getEnergyStorage(helper, placerPos);
		
		helper.assertValueEqual(fluids.fill(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE), FluidType.BUCKET_VOLUME, "fluid placer water insertion");
		chargeEnergy(helper, energy, 1000);
		
		helper.succeedWhen(() -> {
			helper.assertBlockPresent(Blocks.WATER, targetPos);
			helper.assertTrue(fluids.getFluidInTank(0).isEmpty(), "fluid placer should consume placed water");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 400)
	public static void utilityPumpCollectsFluidBelowDepth(GameTestHelper helper) {
		var pumpPos = new BlockPos(0, 1, 0);
		var sourcePos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pumpPos, AMBlocks.PUMP.get());
		helper.setBlock(sourcePos, Blocks.WATER);
		
		var pump = helper.getBlockEntity(pumpPos);
		helper.assertTrue(pump instanceof PumpBlockEntity, "expected pump block entity");
		((PumpBlockEntity) pump).depth = 20.0D;
		
		var fluids = getFluidHandler(helper, pumpPos);
		var energy = getEnergyStorage(helper, pumpPos);
		
		chargeEnergy(helper, energy, 1000);
		
		helper.succeedWhen(() -> {
			helper.assertBlockPresent(Blocks.AIR, sourcePos);
			assertFluid(helper, fluids, 0, Fluids.WATER, FluidType.BUCKET_VOLUME, "pump should store pumped water");
		});
	}
	
	@GameTest(template = TEMPLATE)
	public static void utilityDrainDestroysInsertedFluid(GameTestHelper helper) {
		var drainPos = new BlockPos(0, 0, 0);
		
		helper.setBlock(drainPos, AMBlocks.DRAIN.get());
		
		var fluids = getFluidHandler(helper, drainPos);
		
		helper.assertValueEqual(fluids.fill(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE), FluidType.BUCKET_VOLUME, "drain water insertion");
		
		helper.succeedWhen(() -> helper.assertTrue(fluids.getFluidInTank(0).isEmpty(), "drain should destroy inserted water"));
	}
	
	@GameTest(template = TEMPLATE)
	public static void extendedBlockEntityPersistenceRoundTripsStorageAndState(GameTestHelper helper) {
		var capacitorPos = new BlockPos(0, 0, 0);
		var bufferPos = new BlockPos(1, 0, 0);
		var tankPos = new BlockPos(2, 0, 0);
		var furnacePos = new BlockPos(3, 0, 0);
		
		helper.setBlock(capacitorPos, AMBlocks.PRIMITIVE_CAPACITOR.get());
		helper.setBlock(bufferPos, AMBlocks.PRIMITIVE_BUFFER.get());
		helper.setBlock(tankPos, AMBlocks.PRIMITIVE_TANK.get());
		helper.setBlock(furnacePos, AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get());
		
		var capacitor = getEnergyStorage(helper, capacitorPos);
		var buffer = getItemHandler(helper, bufferPos);
		var tank = getFluidHandler(helper, tankPos);
		var tankBlockEntity = helper.getBlockEntity(tankPos);
		var furnaceBlockEntity = getExtendedBlockEntity(helper, furnacePos);
		
		helper.assertTrue(capacitor.receiveEnergy(250, false) > 0, "capacitor should accept persisted energy");
		helper.assertTrue(buffer.insertItem(0, new ItemStack(Items.COBBLESTONE, 17), false).isEmpty(), "buffer should accept persisted items");
		helper.assertValueEqual(tank.fill(new FluidStack(Fluids.WATER, 750), IFluidHandler.FluidAction.EXECUTE), 750, "tank should accept persisted water");
		helper.assertTrue(tankBlockEntity instanceof TankBlockEntity, "expected tank block entity");
		((TankBlockEntity) tankBlockEntity).setFilter(new FluidStack(Fluids.WATER, 1));
		
		furnaceBlockEntity.progress = 3.0D;
		furnaceBlockEntity.limit = 8.0D;
		furnaceBlockEntity.setRedstoneControl(RedstoneType.WORK_WHEN_ON);
		configureItemSiding(helper, furnacePos, Direction.WEST, StorageSiding.INSERT);
		
		var capacitorCopy = roundTripBlockEntity(helper, capacitorPos, ExtendedBlockEntity.class);
		var bufferCopy = roundTripBlockEntity(helper, bufferPos, ExtendedBlockEntity.class);
		var tankCopy = roundTripBlockEntity(helper, tankPos, TankBlockEntity.class);
		var furnaceCopy = roundTripBlockEntity(helper, furnacePos, ExtendedBlockEntity.class);
		
		helper.assertValueEqual(capacitorCopy.getEnergyStorage().getAmount(), LongEnergyStorage.getAmount(capacitor), "capacitor energy should persist");
		helper.assertValueEqual(bufferCopy.getItemStorage().getItem(0).getCount(), 17, "buffer item count should persist");
		assertFluidStorage(helper, tankCopy.getFluidStorage(), 0, Fluids.WATER, 750, "tank fluid should persist");
		helper.assertTrue(FluidStack.isSameFluidSameComponents(tankCopy.getFilter(), new FluidStack(Fluids.WATER, 1)), "tank filter should persist");
		helper.assertValueEqual((int) furnaceCopy.progress, 3, "machine progress should persist");
		helper.assertValueEqual((int) furnaceCopy.limit, 8, "machine progress limit should persist");
		helper.assertTrue(furnaceCopy.getRedstoneType() == RedstoneType.WORK_WHEN_ON, "machine redstone mode should persist");
		helper.assertTrue(furnaceCopy.getItemStorage().getSidings()[Direction.WEST.ordinal()] == StorageSiding.INSERT, "machine item siding should persist");
		helper.succeed();
	}

	@GameTest(template = TEMPLATE)
	public static void droppedMachineStackRestoresSavedContentsOnPlacement(GameTestHelper helper) {
		var capacitorSourcePos = new BlockPos(0, 0, 0);
		var capacitorTargetPos = new BlockPos(0, 0, 1);
		var bufferSourcePos = new BlockPos(1, 0, 0);
		var bufferTargetPos = new BlockPos(1, 0, 1);
		var tankSourcePos = new BlockPos(2, 0, 0);
		var tankTargetPos = new BlockPos(2, 0, 1);

		helper.setBlock(capacitorSourcePos, AMBlocks.PRIMITIVE_CAPACITOR.get());
		helper.setBlock(bufferSourcePos, AMBlocks.PRIMITIVE_BUFFER.get());
		helper.setBlock(tankSourcePos, AMBlocks.PRIMITIVE_TANK.get());

		helper.assertTrue(getEnergyStorage(helper, capacitorSourcePos).receiveEnergy(250, false) > 0, "capacitor should accept dropped energy");
		helper.assertTrue(getItemHandler(helper, bufferSourcePos).insertItem(0, new ItemStack(Items.COBBLESTONE, 17), false).isEmpty(), "buffer should accept dropped items");
		helper.assertValueEqual(getFluidHandler(helper, tankSourcePos).fill(new FluidStack(Fluids.WATER, 750), IFluidHandler.FluidAction.EXECUTE), 750, "tank should accept dropped water");

		var capacitorStack = createDroppedMachineStack(helper, capacitorSourcePos, AMBlocks.PRIMITIVE_CAPACITOR.get());
		var bufferStack = createDroppedMachineStack(helper, bufferSourcePos, AMBlocks.PRIMITIVE_BUFFER.get());
		var tankStack = createDroppedMachineStack(helper, tankSourcePos, AMBlocks.PRIMITIVE_TANK.get());

		placeDroppedMachineStack(helper, capacitorTargetPos, AMBlocks.PRIMITIVE_CAPACITOR.get(), capacitorStack);
		placeDroppedMachineStack(helper, bufferTargetPos, AMBlocks.PRIMITIVE_BUFFER.get(), bufferStack);
		placeDroppedMachineStack(helper, tankTargetPos, AMBlocks.PRIMITIVE_TANK.get(), tankStack);

		helper.assertTrue(getEnergyStorage(helper, capacitorTargetPos).getEnergyStored() >= 250, "placed capacitor should restore dropped energy");
		helper.assertValueEqual(getItemHandler(helper, bufferTargetPos).getStackInSlot(0).getCount(), 17, "placed buffer should restore dropped item count");
		assertFluid(helper, getFluidHandler(helper, tankTargetPos), 0, Fluids.WATER, 750, "placed tank should restore dropped water");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE)
	public static void cablePersistenceRoundTripsConnections(GameTestHelper helper) {
		var cablePos = new BlockPos(0, 0, 0);
		
		helper.setBlock(cablePos, AMBlocks.FLUID_PIPE.get());
		
		var cable = helper.getBlockEntity(cablePos);
		helper.assertTrue(cable instanceof CableBlockEntity, "expected cable block entity");
		
		var connections = ((CableBlockEntity) cable).getConnections();
		connections.setSide(Direction.EAST, true);
		connections.setConnection(Direction.EAST, true);
		connections.setInsert(Direction.WEST, true);
		connections.setExtract(Direction.NORTH, true);
		connections.setInsertExtract(Direction.UP, true);
		
		var copy = roundTripBlockEntity(helper, cablePos, CableBlockEntity.class);
		var copyConnections = copy.getConnections();
		
		helper.assertTrue(copyConnections.hasSide(Direction.EAST), "cable side should persist");
		helper.assertTrue(copyConnections.hasConnector(Direction.EAST), "cable connector should persist");
		helper.assertTrue(copyConnections.isInsert(Direction.WEST), "cable insert mode should persist");
		helper.assertTrue(copyConnections.isExtract(Direction.NORTH), "cable extract mode should persist");
		helper.assertTrue(copyConnections.isInsertExtract(Direction.UP), "cable insert-extract mode should persist");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 400)
	public static void machineProcessingSmeltsItems(GameTestHelper helper) {
		var furnacePos = new BlockPos(0, 0, 0);
		
		helper.setBlock(furnacePos, AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get());
		
		var items = getItemHandler(helper, furnacePos);
		var energy = getEnergyStorage(helper, furnacePos);
		
		helper.assertTrue(items.insertItem(ElectricFurnaceBlockEntity.INPUT_SLOT, new ItemStack(Items.RAW_IRON), false).isEmpty(), "electric furnace should accept smeltable input");
		chargeEnergy(helper, energy, 1000);
		
		helper.succeedWhen(() -> {
			helper.assertTrue(items.getStackInSlot(ElectricFurnaceBlockEntity.INPUT_SLOT).isEmpty(), "electric furnace should consume input");
			helper.assertTrue(items.getStackInSlot(ElectricFurnaceBlockEntity.OUTPUT_SLOT).is(Items.IRON_INGOT), "electric furnace should produce iron ingot");
		});
	}
	
	@GameTest(template = TEMPLATE)
	public static void machineRecipesLoad(GameTestHelper helper) {
		assertRecipeTypeLoaded(helper, RecipeType.SMELTING, "vanilla smelting");
		assertRecipeTypeLoaded(helper, AlloySmeltingRecipe.Type.INSTANCE, "alloy smelting");
		assertRecipeTypeLoaded(helper, PressingRecipe.Type.INSTANCE, "pressing");
		assertRecipeTypeLoaded(helper, TrituratingRecipe.Type.INSTANCE, "triturating");
		assertRecipeTypeLoaded(helper, WireMillingRecipe.Type.INSTANCE, "wire milling");
		assertRecipeTypeLoaded(helper, MeltingRecipe.Type.INSTANCE, "melting");
		assertRecipeTypeLoaded(helper, SolidifyingRecipe.Type.INSTANCE, "solidifying");
		assertRecipeTypeLoaded(helper, RefiningRecipe.Type.INSTANCE, "refining");
		assertRecipeTypeLoaded(helper, ElectrolyzingRecipe.Type.INSTANCE, "electrolyzing");
		assertRecipeTypeLoaded(helper, FluidMixingRecipe.Type.INSTANCE, "fluid mixing");
		assertRecipeTypeLoaded(helper, FluidGeneratingRecipe.Type.INSTANCE, "fluid generating");
		helper.succeed();
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 500)
	public static void machineProcessingAlloySmelterProducesSteel(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_ALLOY_SMELTER.get());
		
		var items = getItemHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		
		helper.assertTrue(items.insertItem(AlloySmelterBlockEntity.INPUT_SLOT_1, new ItemStack(Items.IRON_INGOT), false).isEmpty(), "alloy smelter should accept iron");
		helper.assertTrue(items.insertItem(AlloySmelterBlockEntity.INPUT_SLOT_2, new ItemStack(Items.COAL, 2), false).isEmpty(), "alloy smelter should accept coal");
		chargeEnergy(helper, energy, 900);
		
		helper.succeedWhen(() -> {
			helper.assertTrue(items.getStackInSlot(AlloySmelterBlockEntity.INPUT_SLOT_1).isEmpty(), "alloy smelter should consume iron");
			helper.assertTrue(items.getStackInSlot(AlloySmelterBlockEntity.INPUT_SLOT_2).isEmpty(), "alloy smelter should consume coal");
			helper.assertTrue(items.getStackInSlot(AlloySmelterBlockEntity.OUTPUT_SLOT).is(AMItems.STEEL_INGOT.get()), "alloy smelter should produce steel");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 400)
	public static void machineProcessingPresserProducesCopperPlate(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_PRESSER.get());
		
		var items = getItemHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		
		helper.assertTrue(items.insertItem(PresserBlockEntity.INPUT_SLOT, new ItemStack(Items.COPPER_INGOT), false).isEmpty(), "presser should accept copper ingot");
		chargeEnergy(helper, energy, 340);
		
		helper.succeedWhen(() -> {
			helper.assertTrue(items.getStackInSlot(PresserBlockEntity.INPUT_SLOT).isEmpty(), "presser should consume copper ingot");
			helper.assertTrue(items.getStackInSlot(PresserBlockEntity.OUTPUT_SLOT).is(AMItems.COPPER_PLATE.get()), "presser should produce copper plate");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 500)
	public static void machineProcessingTrituratorProducesGravel(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_TRITURATOR.get());
		
		var items = getItemHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		
		helper.assertTrue(items.insertItem(TrituratorBlockEntity.INPUT_SLOT, new ItemStack(Items.COBBLESTONE), false).isEmpty(), "triturator should accept cobblestone");
		chargeEnergy(helper, energy, 440);
		
		helper.succeedWhen(() -> {
			helper.assertTrue(items.getStackInSlot(TrituratorBlockEntity.INPUT_SLOT).isEmpty(), "triturator should consume cobblestone");
			helper.assertTrue(items.getStackInSlot(TrituratorBlockEntity.OUTPUT_SLOT).is(Items.GRAVEL), "triturator should produce gravel");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 400)
	public static void machineProcessingWireMillProducesCopperWire(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_WIRE_MILL.get());
		
		var items = getItemHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		
		helper.assertTrue(items.insertItem(WireMillBlockEntity.INPUT_SLOT, new ItemStack(Items.COPPER_INGOT), false).isEmpty(), "wire mill should accept copper ingot");
		chargeEnergy(helper, energy, 340);
		
		helper.succeedWhen(() -> {
			helper.assertTrue(items.getStackInSlot(WireMillBlockEntity.INPUT_SLOT).isEmpty(), "wire mill should consume copper ingot");
			helper.assertTrue(items.getStackInSlot(WireMillBlockEntity.OUTPUT_SLOT).is(AMItems.COPPER_WIRE.get()), "wire mill should produce copper wire");
			helper.assertValueEqual(items.getStackInSlot(WireMillBlockEntity.OUTPUT_SLOT).getCount(), 3, "wire mill copper wire count");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 500)
	public static void machineProcessingMelterProducesMoltenIron(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_MELTER.get());
		
		var items = getItemHandler(helper, pos);
		var fluids = getFluidHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		
		helper.assertTrue(items.insertItem(MelterBlockEntity.ITEM_INPUT_SLOT, new ItemStack(Items.IRON_INGOT), false).isEmpty(), "melter should accept iron ingot");
		chargeEnergy(helper, energy, 1600);
		
		helper.succeedWhen(() -> {
			helper.assertTrue(items.getStackInSlot(MelterBlockEntity.ITEM_INPUT_SLOT).isEmpty(), "melter should consume iron ingot");
			assertFluid(helper, fluids, MelterBlockEntity.FLUID_OUTPUT_SLOT, AMFluids.MOLTEN_IRON.getSource(), 111, "melter should produce molten iron");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 500)
	public static void machineProcessingSolidifierProducesIronIngot(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_SOLIDIFIER.get());
		
		var items = getItemHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		var blockEntity = getExtendedBlockEntity(helper, pos);
		
		fillFluidSlot(helper, blockEntity, SolidifierBlockEntity.FLUID_INPUT_SLOT, AMFluids.MOLTEN_IRON.getSource(), 111);
		chargeEnergy(helper, energy, 1600);
		
		helper.succeedWhen(() -> {
			helper.assertTrue(blockEntity.getFluidStorage().getStorage(SolidifierBlockEntity.FLUID_INPUT_SLOT).isResourceBlank(), "solidifier should consume molten iron");
			helper.assertTrue(items.getStackInSlot(SolidifierBlockEntity.ITEM_OUTPUT_SLOT).is(Items.IRON_INGOT), "solidifier should produce iron ingot");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 200)
	public static void machineProcessingRefineryProducesFuel(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_REFINERY.get());
		
		var fluids = getFluidHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		
		helper.assertValueEqual(fluids.fill(new FluidStack(AMFluids.OIL.getSource(), 810), IFluidHandler.FluidAction.EXECUTE), 810, "refinery oil insertion");
		chargeEnergy(helper, energy, 32);
		
		helper.succeedWhen(() -> assertFluid(helper, fluids, RefineryBlockEntity.OUTPUT_SLOT, AMFluids.FUEL.getSource(), 405, "refinery should produce fuel"));
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 200)
	public static void machineProcessingElectrolyzerProducesGases(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_ELECTROLYZER.get());
		
		var fluids = getFluidHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		
		helper.assertValueEqual(fluids.fill(new FluidStack(Fluids.WATER, 2250), IFluidHandler.FluidAction.EXECUTE), 2250, "electrolyzer water insertion");
		chargeEnergy(helper, energy, 24);
		
		helper.succeedWhen(() -> {
			assertFluid(helper, fluids, ElectrolyzerBlockEntity.OUTPUT_SLOT_1, AMFluids.OXYGEN.getSource(), 750, "electrolyzer should produce oxygen");
			assertFluid(helper, fluids, ElectrolyzerBlockEntity.OUTPUT_SLOT_2, AMFluids.HYDROGEN.getSource(), 1500, "electrolyzer should produce hydrogen");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 200)
	public static void machineProcessingFluidMixerProducesWater(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_FLUID_MIXER.get());
		
		var fluids = getFluidHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		var blockEntity = getExtendedBlockEntity(helper, pos);
		
		fillFluidSlot(helper, blockEntity, FluidMixerBlockEntity.INPUT_SLOT_1, AMFluids.OXYGEN.getSource(), 750);
		fillFluidSlot(helper, blockEntity, FluidMixerBlockEntity.INPUT_SLOT_2, AMFluids.HYDROGEN.getSource(), 1500);
		chargeEnergy(helper, energy, 24);
		
		helper.succeedWhen(() -> assertFluid(helper, fluids, FluidMixerBlockEntity.OUTPUT_SLOT, Fluids.WATER, 2250, "fluid mixer should produce water"));
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 200)
	public static void machineProcessingSolidGeneratorProducesEnergy(GameTestHelper helper) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_SOLID_GENERATOR.get());
		
		var items = getItemHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		
		helper.assertTrue(items.insertItem(SolidGeneratorBlockEntity.INPUT_SLOT, new ItemStack(Items.COAL), false).isEmpty(), "solid generator should accept coal");
		
		helper.succeedWhen(() -> {
			helper.assertTrue(energy.getEnergyStored() > 0, "solid generator should produce energy");
			helper.assertTrue(items.getStackInSlot(SolidGeneratorBlockEntity.INPUT_SLOT).isEmpty(), "solid generator should consume coal");
		});
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 200)
	public static void machineProcessingFluidGeneratorProducesEnergy(GameTestHelper helper) {
		assertFluidGeneratorFuel(helper, AMFluids.OIL.getSource(), "oil");
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 200)
	public static void machineProcessingFluidGeneratorUsesFuel(GameTestHelper helper) {
		assertFluidGeneratorFuel(helper, AMFluids.FUEL.getSource(), "fuel");
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 200)
	public static void machineProcessingFluidGeneratorUsesLava(GameTestHelper helper) {
		assertFluidGeneratorFuel(helper, Fluids.LAVA, "lava");
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 200)
	public static void machineProcessingEliteFluidGeneratorExportsLavaEnergy(GameTestHelper helper) {
		assertEliteFluidGeneratorExportsEnergy(helper, Fluids.LAVA, "lava");
	}
	
	@GameTest(template = TEMPLATE, timeoutTicks = 200)
	public static void machineProcessingEliteFluidGeneratorExportsFuelEnergy(GameTestHelper helper) {
		assertEliteFluidGeneratorExportsEnergy(helper, AMFluids.FUEL.getSource(), "fuel");
	}
	
	private static void assertEliteFluidGeneratorExportsEnergy(GameTestHelper helper, Fluid fuel, String fuelName) {
		var generatorPos = new BlockPos(0, 0, 0);
		var capacitorPos = new BlockPos(1, 0, 0);
		
		helper.setBlock(generatorPos, AMBlocks.ELITE_FLUID_GENERATOR.get());
		helper.setBlock(capacitorPos, AMBlocks.ELITE_CAPACITOR.get());
		
		var generatorFluids = getFluidHandler(helper, generatorPos);
		var capacitorEnergy = getEnergyStorage(helper, capacitorPos);
		
		helper.assertValueEqual(generatorFluids.fill(new FluidStack(fuel, 1000), IFluidHandler.FluidAction.EXECUTE), 1000, "elite fluid generator " + fuelName + " insertion");
		
		helper.succeedWhen(() -> {
			helper.assertTrue(capacitorEnergy.getEnergyStored() > 0, "elite fluid generator should export " + fuelName + " energy to adjacent capacitor");
			helper.assertTrue(generatorFluids.getFluidInTank(FluidGeneratorBlockEntity.INPUT_SLOT).getAmount() < 1000, "elite fluid generator should consume " + fuelName);
		});
	}
	
	private static void assertFluidGeneratorFuel(GameTestHelper helper, Fluid fuel, String fuelName) {
		var pos = new BlockPos(0, 0, 0);
		
		helper.setBlock(pos, AMBlocks.PRIMITIVE_FLUID_GENERATOR.get());
		
		var fluids = getFluidHandler(helper, pos);
		var energy = getEnergyStorage(helper, pos);
		
		helper.assertValueEqual(fluids.fill(new FluidStack(fuel, 81), IFluidHandler.FluidAction.EXECUTE), 81, "fluid generator " + fuelName + " insertion");
		
		helper.succeedWhen(() -> {
			helper.assertTrue(energy.getEnergyStored() > 0, "fluid generator should produce energy from " + fuelName);
			helper.assertTrue(fluids.getFluidInTank(FluidGeneratorBlockEntity.INPUT_SLOT).isEmpty(), "fluid generator should consume " + fuelName);
		});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void assertRecipeTypeLoaded(GameTestHelper helper, RecipeType type, String name) {
		helper.assertTrue(!helper.getLevel().getRecipeManager().getAllRecipesFor(type).isEmpty(), name + " recipes should load");
	}
	
	private static void assertEnergyReceives(GameTestHelper helper, IEnergyStorage energyStorage) {
		helper.assertTrue(energyStorage.getMaxEnergyStored() > 0, "energy storage should have capacity");
		helper.assertTrue(energyStorage.receiveEnergy(1, false) > 0, "energy storage should receive energy");
	}
	
	private static void chargeEnergy(GameTestHelper helper, IEnergyStorage energyStorage, int minimumEnergy) {
		for (var i = 0; i < 128 && energyStorage.getEnergyStored() < minimumEnergy; ++i) {
			var received = energyStorage.receiveEnergy(Integer.MAX_VALUE, false);
			
			if (received <= 0) {
				break;
			}
		}
		
		helper.assertTrue(energyStorage.getEnergyStored() >= minimumEnergy, "machine energy storage should charge to at least " + minimumEnergy);
	}
	
	private static void assertFluid(GameTestHelper helper, IFluidHandler storage, int tank, Fluid fluid, int minimumAmount, String message) {
		var stack = storage.getFluidInTank(tank);
		
		helper.assertTrue(FluidStack.isSameFluidSameComponents(stack, new FluidStack(fluid, minimumAmount)), message + ": " + stack);
		helper.assertTrue(stack.getAmount() >= minimumAmount, message + " amount: " + stack.getAmount());
	}
	
	private static void assertFluidStorage(GameTestHelper helper, SimpleFluidStorage storage, int tank, Fluid fluid, int minimumAmount, String message) {
		helper.assertTrue(storage != null, "expected fluid storage");
		assertFluid(helper, storage, tank, fluid, minimumAmount, message);
	}
	
	private static void fillFluidSlot(GameTestHelper helper, ExtendedBlockEntity blockEntity, int slot, Fluid fluid, int amount) {
		var fluidStorage = blockEntity.getFluidStorage();
		
		helper.assertTrue(fluidStorage != null, "expected fluid storage on " + blockEntity.getBlockPos());
		helper.assertValueEqual(fluidStorage.getStorage(slot).insert(new FluidStack(fluid, amount), amount, false, false), (long) amount, "fluid slot insertion");
	}
	
	private static <T extends BlockEntity> T roundTripBlockEntity(GameTestHelper helper, BlockPos pos, Class<T> expectedType) {
		var source = helper.getLevel().getBlockEntity(helper.absolutePos(pos));
		
		helper.assertTrue(source != null, "expected source block entity at " + pos);
		
		var nbt = source.saveWithId(helper.getLevel().registryAccess());
		var copy = source.getType().create(source.getBlockPos(), source.getBlockState());
		
		helper.assertTrue(copy != null, "expected copy block entity at " + pos);
		
		copy.loadWithComponents(nbt, helper.getLevel().registryAccess());
		
		helper.assertTrue(expectedType.isInstance(copy), "expected copied block entity type " + expectedType.getSimpleName());
		
		return expectedType.cast(copy);
	}

	private static ItemStack createDroppedMachineStack(GameTestHelper helper, BlockPos pos, Block block) {
		var blockEntity = helper.getLevel().getBlockEntity(helper.absolutePos(pos));

		helper.assertTrue(blockEntity != null, "expected dropped block entity at " + pos);

		var customTag = new CompoundTag();
		customTag.put("BlockEntityTag", blockEntity.saveCustomOnly(helper.getLevel().registryAccess()));

		var stack = new ItemStack(block.asItem());
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(customTag));

		return stack;
	}

	private static void placeDroppedMachineStack(GameTestHelper helper, BlockPos pos, Block block, ItemStack stack) {
		helper.setBlock(pos, block);

		var absolutePos = helper.absolutePos(pos);
		var state = helper.getLevel().getBlockState(absolutePos);

		block.setPlacedBy(helper.getLevel(), absolutePos, state, null, stack);
	}
	
	private static ExtendedBlockEntity getExtendedBlockEntity(GameTestHelper helper, BlockPos pos) {
		var blockEntity = helper.getLevel().getBlockEntity(helper.absolutePos(pos));
		
		helper.assertTrue(blockEntity instanceof ExtendedBlockEntity, "expected extended block entity at " + pos);
		
		return (ExtendedBlockEntity) blockEntity;
	}
	
	private static IItemHandler getItemHandler(GameTestHelper helper, BlockPos pos) {
		var storage = helper.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, helper.absolutePos(pos), null);
		
		helper.assertTrue(storage != null, "expected item handler at " + pos);
		
		return storage;
	}
	
	private static IFluidHandler getFluidHandler(GameTestHelper helper, BlockPos pos) {
		var storage = helper.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, helper.absolutePos(pos), null);
		
		helper.assertTrue(storage != null, "expected fluid handler at " + pos);
		
		return storage;
	}
	
	private static IEnergyStorage getEnergyStorage(GameTestHelper helper, BlockPos pos) {
		var storage = helper.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, helper.absolutePos(pos), null);
		
		helper.assertTrue(storage != null, "expected energy storage at " + pos);
		
		return storage;
	}

	private static void assertNoPrecipitation(GameTestHelper helper, ResourceKey<Biome> key) {
		var biomes = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
		var biome = biomes.getHolderOrThrow(key).value();

		helper.assertTrue(!biome.hasPrecipitation(), key.location() + " should not allow precipitation");
		helper.assertTrue(biome.getPrecipitationAt(BlockPos.ZERO) == Biome.Precipitation.NONE, key.location() + " should report no precipitation at block positions");
	}
	
	private static void configureCable(GameTestHelper helper, BlockPos pos, Direction direction, StorageSiding siding) {
		var absolutePos = helper.absolutePos(pos);
		var block = helper.getLevel().getBlockState(absolutePos).getBlock();
		var blockEntity = helper.getLevel().getBlockEntity(absolutePos);
		
		helper.assertTrue(block instanceof CableBlock, "expected cable block at " + pos);
		helper.assertTrue(blockEntity instanceof CableBlockEntity, "expected cable block entity at " + pos);
		
		((CableBlock) block).updateConnections(helper.getLevel(), absolutePos);
		
		var connections = ((CableBlockEntity) blockEntity).getConnections();
		
		connections.setInsert(direction, siding == StorageSiding.INSERT);
		connections.setExtract(direction, siding == StorageSiding.EXTRACT);
		connections.setInsertExtract(direction, siding == StorageSiding.INSERT_EXTRACT);
		((CableBlockEntity) blockEntity).syncData();
	}
	
	private static void configureItemSiding(GameTestHelper helper, BlockPos pos, Direction direction, StorageSiding siding) {
		var blockEntity = helper.getLevel().getBlockEntity(helper.absolutePos(pos));
		
		helper.assertTrue(blockEntity instanceof ExtendedBlockEntity, "expected extended block entity at " + pos);
		
		var extendedBlockEntity = (ExtendedBlockEntity) blockEntity;
		var storage = extendedBlockEntity.getItemStorage();
		
		helper.assertTrue(storage != null, "expected item storage at " + pos);
		
		extendedBlockEntity.setStorageSiding(StorageType.ITEM, direction, siding);
	}
	
	private static void configureFluidSiding(GameTestHelper helper, BlockPos pos, Direction direction, StorageSiding siding) {
		var blockEntity = helper.getLevel().getBlockEntity(helper.absolutePos(pos));
		
		helper.assertTrue(blockEntity instanceof ExtendedBlockEntity, "expected extended block entity at " + pos);
		
		var extendedBlockEntity = (ExtendedBlockEntity) blockEntity;
		var storage = extendedBlockEntity.getFluidStorage();
		
		helper.assertTrue(storage != null, "expected fluid storage at " + pos);
		
		extendedBlockEntity.setStorageSiding(StorageType.FLUID, direction, siding);
	}
}
