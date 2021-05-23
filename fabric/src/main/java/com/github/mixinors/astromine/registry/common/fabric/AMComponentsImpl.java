package com.github.mixinors.astromine.registry.common.fabric;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.component.Component;
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
	public static ComponentKey<CCANetworkComponent> NETWORK = ComponentRegistry.getOrCreate(AMCommon.id("network"), CCANetworkComponent.class);
	public static ComponentKey<CCAAtmosphereComponent> ATMOSPHERE = ComponentRegistry.getOrCreate(AMCommon.id("atmosphere"), CCAAtmosphereComponent.class);
	public static ComponentKey<CCABridgeComponent> BRIDGE = ComponentRegistry.getOrCreate(AMCommon.id("bridge"), CCABridgeComponent.class);
	
	public static ComponentKey<CCAItemComponent> ITEM = ComponentRegistry.getOrCreate(AMCommon.id("item"), CCAItemComponent.class);
	public static ComponentKey<CCAFluidComponent> FLUID = ComponentRegistry.getOrCreate(AMCommon.id("fluid"), CCAFluidComponent.class);
	public static ComponentKey<CCAEnergyComponent> ENERGY = ComponentRegistry.getOrCreate(AMCommon.id("energy"), CCAEnergyComponent.class);
	
	public static ComponentKey<CCATransferComponent> TRANSFER = ComponentRegistry.getOrCreate(AMCommon.id("transfer"), CCATransferComponent.class);
	public static ComponentKey<CCARedstoneComponent> REDSTONE = ComponentRegistry.getOrCreate(AMCommon.id("redstone"), CCARedstoneComponent.class);
	
	public static ComponentKey<CCAOxygenComponent> OXYGEN = ComponentRegistry.getOrCreate(AMCommon.id("oxygen"), CCAOxygenComponent.class);
	
	public static class CCANetworkComponent extends CCAComponent.ServerTicking {
		public CCANetworkComponent(Component.ServerTicking component) {
			super(component);
		}
	}
	
	public static class CCAAtmosphereComponent extends CCAComponent.ServerTicking {
		public CCAAtmosphereComponent(Component.ServerTicking component) {
			super(component);
		}
	}
	
	public static class CCABridgeComponent extends CCAComponent {
		public CCABridgeComponent(Component component) {
			super(component);
		}
	}
	
	public static class CCAItemComponent extends CCAComponent {
		public CCAItemComponent(Component component) {
			super(component);
		}
	}
	
	public static class CCAEnergyComponent extends CCAComponent {
		public CCAEnergyComponent(Component component) {
			super(component);
		}
	}
	
	public static class CCAFluidComponent extends CCAComponent {
		public CCAFluidComponent(Component component) {
			super(component);
		}
	}
	
	public static class CCATransferComponent extends CCAComponent {
		public CCATransferComponent(Component component) {
			super(component);
		}
	}
	
	public static class CCARedstoneComponent extends CCAComponent {
		public CCARedstoneComponent(Component component) {
			super(component);
		}
	}
	
	public static class CCAOxygenComponent extends CCAComponent {
		public CCAOxygenComponent(Component component) {
			super(component);
		}
	}
	
	public static void postInit() {}
	
	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {}
	
	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(ATMOSPHERE, (c) -> new CCAAtmosphereComponent(AtmosphereComponent.of(c)));
	}
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, OXYGEN, (e) -> new CCAOxygenComponent(OxygenComponent.of(e)));
		
		registry.registerFor(ComponentFluidItemEntity.class, FLUID, (e) -> new CCAFluidComponent(e.createFluidComponent()));
		registry.registerFor(ComponentFluidItemEntity.class, ITEM, (e) -> new CCAItemComponent(e.createItemComponent()));
		
		registry.registerFor(ComponentEnergyItemEntity.class, ITEM, (e) -> new CCAItemComponent(e.createItemComponent()));
		registry.registerFor(ComponentEnergyItemEntity.class, ENERGY, (e) -> new CCAEnergyComponent(e.createEnergyComponent()));
		
		registry.registerFor(ComponentItemEntity.class, ITEM, (e) -> new CCAItemComponent(e.createItemComponent()));
		
		registry.registerFor(ComponentFluidEntity.class, FLUID, (e) -> new CCAFluidComponent(e.createFluidComponent()));
		
		registry.registerFor(ComponentEnergyEntity.class, ENERGY, (e) -> new CCAEnergyComponent(e.createEnergyComponent()));
	}
	
	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
		registry.registerFor(
				(i) -> i instanceof FluidVolumeItem,
				FLUID,
				(s) -> new CCAFluidComponent(FluidComponent.of(FluidVolume.of(((FluidVolumeItem) s.getItem()).getSize(), Fluids.EMPTY)))
		);
		
		registry.registerFor(
				(i) -> i instanceof EnergyVolumeItem,
				ENERGY,
				(s) -> new CCAEnergyComponent(EnergyComponent.of(((FluidVolumeItem) s.getItem()).getSize()))
		);
		
		registry.registerFor(
				(i) -> i == AMItems.SPACE_SUIT_CHESTPLATE.get(),
				FLUID,
				(s) -> new CCAFluidComponent(FluidComponent.of(FluidVolume.of(AMConfig.get().spaceSuitFluid, Fluids.EMPTY)))
		);
	}
	
	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(NETWORK, (w) -> new CCANetworkComponent(NetworkComponent.of(w)));
		registry.register(BRIDGE, (w) -> new CCABridgeComponent(BridgeComponent.of(w)));
	}
}
