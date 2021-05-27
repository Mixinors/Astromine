package com.github.mixinors.astromine.registry.common.forge;

import com.github.mixinors.astromine.common.capability.FComponent;
import com.github.mixinors.astromine.common.component.Component;
import com.github.mixinors.astromine.common.component.base.*;
import com.github.mixinors.astromine.common.entity.base.*;
import com.github.mixinors.astromine.common.item.base.EnergyItem;
import com.github.mixinors.astromine.common.item.base.FluidItem;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.registry.common.AMItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AMComponentsImpl {
	public static abstract class FNetworkComponent extends FComponent.ServerTicking {
		public FNetworkComponent(Component.ServerTicking component) {
			super(component);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FComponent fComp) {
				return component.equals(fComp.peek());
			}
			
			return false;
		}
	}
	
	private static class FNetworkComponentImpl extends FNetworkComponent {
		public FNetworkComponentImpl(Component.ServerTicking component) {
			super(component);
		}
	}
	
	public static abstract class FAtmosphereComponent extends FComponent.ServerTicking {
		public FAtmosphereComponent(Component.ServerTicking component) {
			super(component);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FComponent ccaComp) {
				return component.equals(ccaComp.peek());
			}
			
			return false;
		}
	}
	
	private static class FAtmosphereComponentImpl extends FAtmosphereComponent {
		public FAtmosphereComponentImpl(Component.ServerTicking component) {
			super(component);
		}
	}
	
	public static abstract class FBridgeComponent extends FComponent {
		public FBridgeComponent(Component component) {
			super(component);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FComponent ccaComp) {
				return component.equals(ccaComp.peek());
			}
			
			return false;
		}
	}
	
	private static class FBridgeComponentImpl extends FBridgeComponent {
		public FBridgeComponentImpl(Component component) {
			super(component);
		}
	}
	
	public static abstract class FItemComponent extends FComponent {
		public FItemComponent(Component component) {
			super(component);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FComponent ccaComp) {
				return component.equals(ccaComp.peek());
			}
			
			return false;
		}
	}
	
	private static class FItemComponentImpl extends FItemComponent {
		public FItemComponentImpl(Component component) {
			super(component);
		}
	}
	
	public static abstract class FEnergyComponent extends FComponent {
		public FEnergyComponent(Component component) {
			super(component);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FComponent ccaComp) {
				return component.equals(ccaComp.peek());
			}
			
			return false;
		}
	}
	
	private static class FEnergyComponentImpl extends FEnergyComponent {
		public FEnergyComponentImpl(Component component) {
			super(component);
		}
	}
	
	public static abstract class FFluidComponent extends FComponent {
		public FFluidComponent(Component component) {
			super(component);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FComponent ccaComp) {
				return component.equals(ccaComp.peek());
			}
			
			return false;
		}
	}
	
	private static class FFluidComponentImpl extends FFluidComponent {
		public FFluidComponentImpl(Component component) {
			super(component);
		}
	}
	
	public static abstract class FTransferComponent extends FComponent {
		public FTransferComponent(Component component) {
			super(component);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FComponent ccaComp) {
				return component.equals(ccaComp.peek());
			}
			
			return false;
		}
	}
	
	private static class FTransferComponentImpl extends FTransferComponent {
		public FTransferComponentImpl(Component component) {
			super(component);
		}
	}
	
	public static abstract class FRedstoneComponent extends FComponent {
		public FRedstoneComponent(Component component) {
			super(component);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FComponent ccaComp) {
				return component.equals(ccaComp.peek());
			}
			
			return false;
		}
	}
	
	private static class FRedstoneComponentImpl extends FRedstoneComponent {
		public FRedstoneComponentImpl(Component component) {
			super(component);
		}
	}
	
	public static abstract class FOxygenComponent extends FComponent {
		public FOxygenComponent(Component component) {
			super(component);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FComponent ccaComp) {
				return component.equals(ccaComp.peek());
			}
			
			return false;
		}
	}
	
	private static class FOxygenComponentImpl extends FOxygenComponent {
		public FOxygenComponentImpl(Component component) {
			super(component);
		}
	}
	
	private static final Capability.IStorage<FComponent> DEFAULT_STORAGE = new Capability.IStorage<>() {
		@Nullable
		@Override
		public Tag writeNBT(Capability<FComponent> capability, FComponent instance, Direction dir) {
			return instance.serializeNBT();
		}
		
		@Override
		public void readNBT(Capability<FComponent> capability, FComponent instance, Direction dir, Tag tag) {
			instance.deserializeNBT((CompoundTag) tag);
		}
	};
	
	private record FCapabilityProvider(Object instance) implements ICapabilityProvider {
		@NotNull
		@Override
		public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
			return LazyOptional.of(() -> (T) instance);
		}
	}
	
	;
	
	public static void postInit() {
		CapabilityManager.INSTANCE.register(FComponent.class, DEFAULT_STORAGE, () -> new FNetworkComponentImpl(null));
		CapabilityManager.INSTANCE.register(FComponent.class, DEFAULT_STORAGE, () -> new FAtmosphereComponentImpl(null));
		CapabilityManager.INSTANCE.register(FComponent.class, DEFAULT_STORAGE, () -> new FBridgeComponentImpl(null));
		CapabilityManager.INSTANCE.register(FComponent.class, DEFAULT_STORAGE, () -> new FItemComponentImpl(null));
		CapabilityManager.INSTANCE.register(FComponent.class, DEFAULT_STORAGE, () -> new FEnergyComponentImpl(null));
		CapabilityManager.INSTANCE.register(FComponent.class, DEFAULT_STORAGE, () -> new FFluidComponentImpl(null));
		CapabilityManager.INSTANCE.register(FComponent.class, DEFAULT_STORAGE, () -> new FTransferComponentImpl(null));
		CapabilityManager.INSTANCE.register(FComponent.class, DEFAULT_STORAGE, () -> new FRedstoneComponentImpl(null));
		CapabilityManager.INSTANCE.register(FComponent.class, DEFAULT_STORAGE, () -> new FOxygenComponentImpl(null));
		
	}
	
	@SubscribeEvent
	public static void attachWorldCapabilities(AttachCapabilitiesEvent<World> event) {
		event.addCapability(AMComponents.NETWORK, new FCapabilityProvider(new FComponent(NetworkComponent.of(event.getObject()))));
		event.addCapability(AMComponents.BRIDGE, new FCapabilityProvider(new FComponent(BridgeComponent.of(event.getObject()))));
	}
	
	@SubscribeEvent
	public static void attachChunkCapabilities(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(AMComponents.ATMOSPHERE, new FCapabilityProvider(new FComponent(AtmosphereComponent.of(event.getObject()))));
	}
	
	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity e) {
			event.addCapability(AMComponents.OXYGEN, new FCapabilityProvider(new FComponent(OxygenComponent.of(e))));
		}
		
		if (event.getObject() instanceof ComponentFluidItemEntity e) {
			event.addCapability(AMComponents.FLUID, new FCapabilityProvider(new FComponent(e.createFluidComponent())));
		}
		
		if (event.getObject() instanceof ComponentFluidItemEntity e) {
			event.addCapability(AMComponents.ITEM, new FCapabilityProvider(new FComponent(e.createItemComponent())));
		}
		
		if (event.getObject() instanceof ComponentEnergyItemEntity e) {
			event.addCapability(AMComponents.ITEM, new FCapabilityProvider(new FComponent(e.createItemComponent())));
		}
		
		if (event.getObject() instanceof ComponentEnergyItemEntity e) {
			event.addCapability(AMComponents.ENERGY, new FCapabilityProvider(new FComponent(e.createEnergyComponent())));
		}
		
		if (event.getObject() instanceof ComponentItemEntity e) {
			event.addCapability(AMComponents.ITEM, new FCapabilityProvider(new FComponent(e.createItemComponent())));
		}
		
		if (event.getObject() instanceof ComponentFluidEntity e) {
			event.addCapability(AMComponents.FLUID, new FCapabilityProvider(new FComponent(e.createFluidComponent())));
		}
		
		if (event.getObject() instanceof ComponentEnergyEntity e) {
			event.addCapability(AMComponents.ENERGY, new FCapabilityProvider(new FComponent(e.createEnergyComponent())));
		}
	}
	
	@SubscribeEvent
	public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
		var stack = event.getObject();
		var item = stack.getItem();
		
		if (item instanceof FluidItem fluidItem) {
			event.addCapability(AMComponents.FLUID, new FCapabilityProvider(new FComponent(FluidComponent.of(fluidItem.getSize()))));
		}
		
		if (item instanceof EnergyItem energyItem) {
			event.addCapability(AMComponents.ENERGY, new FCapabilityProvider(new FComponent(EnergyComponent.of(energyItem.getSize()))));
		}
		
		if (item == AMItems.SPACE_SUIT_CHESTPLATE.get()) {
			event.addCapability(AMComponents.ITEM, new FCapabilityProvider(new FComponent(FluidComponent.of(AMConfig.get().spaceSuitFluid))));
		}
	}
}
