package com.github.mixinors.astromine.registry.common;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.PacketByteBuf;

public class AMTrackedDataHandlers {
	public static final TrackedDataHandler<Long> LONG = new TrackedDataHandler<>() {
		public void write(PacketByteBuf packetByteBuf, Long long_) {
			packetByteBuf.writeLong(long_);
		}
		
		public Long read(PacketByteBuf packetByteBuf) {
			return packetByteBuf.readLong();
		}
		
		public Long copy(Long long_) {
			return long_;
		}
	};
}
