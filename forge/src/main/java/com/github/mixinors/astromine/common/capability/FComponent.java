package com.github.mixinors.astromine.common.capability;

import com.github.mixinors.astromine.common.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class FComponent implements INBTSerializable<CompoundTag> {
	protected final Component component;
	
	public FComponent(Component component) {
		this.component = component;
	}
	
	@Override
	public CompoundTag serializeNBT() {
		var tag = new CompoundTag();
		component.toTag(tag);
		return tag;
	}
	
	@Override
	public void deserializeNBT(CompoundTag tag) {
		component.fromTag(tag);
	}
	
	public Component peek() {
		return component;
	}
	
	public static class ServerTicking extends FComponent {
		public ServerTicking(com.github.mixinors.astromine.common.component.Component.ServerTicking component) {
			super((com.github.mixinors.astromine.common.component.Component) component);
		}
		
		public void serverTick() {
			((com.github.mixinors.astromine.common.component.Component.ServerTicking) component).serverTick();
		}
	}
	
	public static class ClientTicking extends FComponent {
		public ClientTicking(com.github.mixinors.astromine.common.component.Component.ClientTicking component) {
			super((com.github.mixinors.astromine.common.component.Component) component);
		}
		
		public void clientTick() {
			((com.github.mixinors.astromine.common.component.Component.ClientTicking) component).clientTick();
		}
	}
	
	public static class Synced extends FComponent {
		public Synced(com.github.mixinors.astromine.common.component.Component.Synced component) {
			super((com.github.mixinors.astromine.common.component.Component) component);
		}
	}
}
