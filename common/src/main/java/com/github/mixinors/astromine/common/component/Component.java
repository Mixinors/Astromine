package com.github.mixinors.astromine.common.component;

import com.github.mixinors.astromine.common.component.base.RedstoneComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

/**
 * A class used to attach data to an object.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public interface Component {
	/** Serializes this {@link Component} to a {@link CompoundTag}. */
	void toTag(CompoundTag tag);
	
	/** Deserializes this {@link Component} from a {@link CompoundTag}. */
	void fromTag(CompoundTag tag);
	
	/** Returns this component's {@link Identifier}. */
	Identifier getId();
	
	/** An interface used by components which tick on the server. */
	interface ServerTicking {
		void serverTick();
	}
	
	/** An interface used by components which tick on the client. */
	interface ClientTicking {
		void clientTick();
	}
	
	/** An interface used by components which sync to the client. */
	interface Synced {}
}
