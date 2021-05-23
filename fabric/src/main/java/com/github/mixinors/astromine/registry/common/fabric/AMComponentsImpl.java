package com.github.mixinors.astromine.registry.common.fabric;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.component.base.*;
import com.github.mixinors.astromine.common.entity.base.*;
import com.github.mixinors.astromine.common.item.base.EnergyVolumeItem;
import com.github.mixinors.astromine.common.item.base.FluidVolumeItem;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.compat.cardinalcomponents.common.component.base.CCAComponent;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.registry.common.AMItems;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;

public class AMComponentsImpl implements WorldComponentInitializer, ChunkComponentInitializer, ItemComponentInitializer, EntityComponentInitializer, BlockComponentInitializer {
	public static ComponentKey<CCAComponent.ServerTicking> NETWORK = ComponentRegistry.getOrCreate(AMCommon.id("network"), CCAComponent.ServerTicking.class);
	public static ComponentKey<CCAComponent.ServerTicking> ATMOSPHERE = ComponentRegistry.getOrCreate(AMCommon.id("atmosphere"), CCAComponent.ServerTicking.class);
	public static ComponentKey<CCAComponent> BRIDGE = ComponentRegistry.getOrCreate(AMCommon.id("bridge"), CCAComponent.class);
	
	public static ComponentKey<CCAComponent> ITEM = ComponentRegistry.getOrCreate(AMCommon.id("item"), CCAComponent.class);
	public static ComponentKey<CCAComponent> FLUID = ComponentRegistry.getOrCreate(AMCommon.id("fluid"), CCAComponent.class);
	public static ComponentKey<CCAComponent> ENERGY = ComponentRegistry.getOrCreate(AMCommon.id("energy"), CCAComponent.class);
	
	public static ComponentKey<CCAComponent> TRANSFER = ComponentRegistry.getOrCreate(AMCommon.id("transfer"), CCAComponent.class);
	public static ComponentKey<CCAComponent> REDSTONE = ComponentRegistry.getOrCreate(AMCommon.id("redstone"), CCAComponent.class);
	
	public static ComponentKey<CCAComponent> OXYGEN = ComponentRegistry.getOrCreate(AMCommon.id("oxygen"), CCAComponent.class);
	
	public static void postInit() {}
	
	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {}
	
	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(ATMOSPHERE, (c) -> new CCAComponent.ServerTicking(AtmosphereComponent.of(c)));
	}
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, OXYGEN, (e) -> new CCAComponent(OxygenComponent.of(e)));
		
		registry.registerFor(ComponentFluidItemEntity.class, FLUID, (e) -> new CCAComponent(e.createFluidComponent()));
		registry.registerFor(ComponentFluidItemEntity.class, ITEM, (e) -> new CCAComponent(e.createItemComponent()));
		
		registry.registerFor(ComponentEnergyItemEntity.class, ITEM, (e) -> new CCAComponent(e.createItemComponent()));
		registry.registerFor(ComponentEnergyItemEntity.class, ENERGY, (e) -> new CCAComponent(e.createEnergyComponent()));
		
		registry.registerFor(ComponentItemEntity.class, ITEM, (e) -> new CCAComponent(e.createItemComponent()));
		
		registry.registerFor(ComponentFluidEntity.class, FLUID, (e) -> new CCAComponent(e.createFluidComponent()));
		
		registry.registerFor(ComponentEnergyEntity.class, ENERGY, (e) -> new CCAComponent(e.createEnergyComponent()));
	}
	
	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
		registry.registerFor(
				(i) -> i instanceof FluidVolumeItem,
				FLUID,
				(s) -> new CCAComponent(FluidComponent.of(FluidVolume.of(((FluidVolumeItem) s.getItem()).getSize(), Fluids.EMPTY)))
		);
		
		registry.registerFor(
				(i) -> i instanceof EnergyVolumeItem,
				ENERGY,
				(s) -> new CCAComponent(EnergyComponent.of(((FluidVolumeItem) s.getItem()).getSize()))
		);
		
		registry.registerFor(
				(i) -> i == AMItems.SPACE_SUIT_CHESTPLATE.get(),
				FLUID,
				(s) -> new CCAComponent(FluidComponent.of(FluidVolume.of(AMConfig.get().spaceSuitFluid, Fluids.EMPTY)))
		);
	}
	
	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(NETWORK, (w) -> new CCAComponent.ServerTicking(NetworkComponent.of(w)));
		registry.register(BRIDGE, (w) -> new CCAComponent(BridgeComponent.of(w)));
	}
}
