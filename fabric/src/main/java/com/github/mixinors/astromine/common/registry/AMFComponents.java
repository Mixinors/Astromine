package com.github.mixinors.astromine.common.registry;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.component.base.*;
import com.github.mixinors.astromine.common.entity.base.*;
import com.github.mixinors.astromine.common.item.base.FluidVolumeItem;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
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

public class AMFComponents implements WorldComponentInitializer, ChunkComponentInitializer, ItemComponentInitializer, EntityComponentInitializer, BlockComponentInitializer {
	public static final ComponentKey<AMFComponent.ServerTicking> NETWORK = ComponentRegistry.getOrCreate(AMCommon.id("network"), AMFComponent.ServerTicking.class);
	public static final ComponentKey<AMFComponent.ServerTicking> ATMOSPHERE = ComponentRegistry.getOrCreate(AMCommon.id("atmosphere"), AMFComponent.ServerTicking.class);
	public static final ComponentKey<AMFComponent> BRIDGE = ComponentRegistry.getOrCreate(AMCommon.id("bridge"), AMFComponent.class);
	
	public static final ComponentKey<AMFComponent> ITEM = ComponentRegistry.getOrCreate(AMCommon.id("item"), AMFComponent.class);
	public static final ComponentKey<AMFComponent> FLUID = ComponentRegistry.getOrCreate(AMCommon.id("fluid"), AMFComponent.class);
	public static final ComponentKey<AMFComponent> ENERGY = ComponentRegistry.getOrCreate(AMCommon.id("energy"), AMFComponent.class);
	
	public static final ComponentKey<AMFComponent> TRANSFER = ComponentRegistry.getOrCreate(AMCommon.id("transfer"), AMFComponent.class);
	public static final ComponentKey<AMFComponent> REDSTONE = ComponentRegistry.getOrCreate(AMCommon.id("redstone"), AMFComponent.class);
	
	public static final ComponentKey<AMFComponent> OXYGEN = ComponentRegistry.getOrCreate(AMCommon.id("oxygen"), AMFComponent.class);
	
	public static void init() {
	
	}
	
	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {}
	
	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(ATMOSPHERE, (c) -> new AMFComponent.ServerTicking(AtmosphereComponent.of(c)));
	}
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, OXYGEN, (e) -> new AMFComponent(OxygenComponent.of(e)));
		
		registry.registerFor(ComponentFluidItemEntity.class, FLUID, (e) -> new AMFComponent(e.createFluidComponent()));
		registry.registerFor(ComponentFluidItemEntity.class, ITEM, (e) -> new AMFComponent(e.createItemComponent()));
		
		registry.registerFor(ComponentEnergyItemEntity.class, ITEM, (e) -> new AMFComponent(e.createItemComponent()));
		registry.registerFor(ComponentEnergyItemEntity.class, ENERGY, (e) -> new AMFComponent(e.createEnergyComponent()));
		
		registry.registerFor(ComponentItemEntity.class, ITEM, (e) -> new AMFComponent(e.createItemComponent()));
		
		registry.registerFor(ComponentFluidEntity.class, FLUID, (e) -> new AMFComponent(e.createFluidComponent()));
		
		registry.registerFor(ComponentEnergyEntity.class, ENERGY, (e) -> new AMFComponent(e.createEnergyComponent()));
	}
	
	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
		registry.registerFor(
				(i) -> i instanceof FluidVolumeItem,
				FLUID,
				(s) -> new AMFComponent(FluidComponent.of(FluidVolume.of(((FluidVolumeItem) s.getItem()).getSize(), Fluids.EMPTY)))
		);
		
		registry.registerFor(
				(i) -> i == AMItems.SPACE_SUIT_CHESTPLATE.get(),
				FLUID,
				(s) -> new AMFComponent(FluidComponent.of(FluidVolume.of(AMConfig.get().spaceSuitFluid, Fluids.EMPTY)))
		);
	}
	
	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(NETWORK, (w) -> new AMFComponent.ServerTicking(NetworkComponent.of(w)));
		registry.register(BRIDGE, (w) -> new AMFComponent(BridgeComponent.of(w)));
	}
}
