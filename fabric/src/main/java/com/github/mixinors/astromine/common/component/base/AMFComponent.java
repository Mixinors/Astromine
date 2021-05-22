package com.github.mixinors.astromine.common.component.base;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;

public class AMFComponent implements Component {
	protected final com.github.mixinors.astromine.common.component.Component component;
	
	public AMFComponent(com.github.mixinors.astromine.common.component.Component component) {
		this.component = component;
	}
	
	@Override
	public void readFromNbt(CompoundTag tag) {
		component.fromTag(tag);
	}
	
	@Override
	public void writeToNbt(CompoundTag tag) {
		component.toTag(tag);
	}
	
	public static class ServerTicking extends AMFComponent implements ServerTickingComponent {
		public ServerTicking(com.github.mixinors.astromine.common.component.Component.ServerTicking component) {
			super((com.github.mixinors.astromine.common.component.Component) component);
		}
		
		@Override
		public void serverTick() {
			((com.github.mixinors.astromine.common.component.Component.ServerTicking) component).serverTick();
		}
	}
	
	public static class ClientTicking extends AMFComponent implements ClientTickingComponent {
		public ClientTicking(com.github.mixinors.astromine.common.component.Component.ClientTicking component) {
			super((com.github.mixinors.astromine.common.component.Component) component);
		}
		
		@Override
		public void clientTick() {
			((com.github.mixinors.astromine.common.component.Component.ClientTicking) component).clientTick();
		}
	}
	
	public static class Synced extends AMFComponent implements AutoSyncedComponent {
		public Synced(com.github.mixinors.astromine.common.component.Component.Synced component) {
			super((com.github.mixinors.astromine.common.component.Component) component);
		}
	}
}
