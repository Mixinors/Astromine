package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Component.class)
public interface ComponentMixin extends dev.onyxstudios.cca.api.v3.component.Component {
	@Override
	default void writeToNbt(CompoundTag tag) {
		((Component) this).toTag(tag);
	}
	
	@Override
	default void readFromNbt(CompoundTag tag) {
		((Component) this).fromTag(tag);
	}
	
	@Mixin(Component.ServerTicking.class)
	interface ServerTickingMixin extends ServerTickingComponent {
		@Override
		default void serverTick() {
			((Component.ServerTicking) this).serverTick();
		}
	}
	
	@Mixin(Component.ClientTicking.class)
	interface ClientTickingMixin extends ClientTickingComponent {
		@Override
		default void clientTick() {
			((Component.ClientTicking) this).clientTick();
		}
	}
	
	@Mixin(Component.Synced.class)
	interface SyncedMixin extends AutoSyncedComponent {}
}
