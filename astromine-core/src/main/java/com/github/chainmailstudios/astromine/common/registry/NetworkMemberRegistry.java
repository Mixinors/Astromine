/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.registry;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.utilities.data.position.WorldPos;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class NetworkMemberRegistry {
	public static final NetworkMemberRegistry INSTANCE = new NetworkMemberRegistry();

	private final Map<NetworkType, NetworkTypeRegistry<?>> registries = new Reference2ObjectOpenHashMap<>();

	private NetworkMemberRegistry() {}

	public static NetworkMember get(@Nullable WorldPos pos, @Nullable Direction direction) {
		return INSTANCE.new NetworkMemberImpl(pos, direction);
	}

	public static NetworkMember get(@Nullable BlockEntity blockEntity, @Nullable Direction direction) {
		return blockEntity != null ? get(WorldPos.of(blockEntity.getWorld(), blockEntity.getPos()), null) : get((WorldPos) null, null);
	}

	public <T extends NetworkType, R extends NetworkTypeRegistry<?>> void register(T t, R r) {
		registries.put(t, r);
	}

	public <T extends NetworkType> NetworkTypeRegistry<T> get(T type) {
		return (NetworkTypeRegistry<T>) registries.computeIfAbsent(type, NetworkTypeRegistryImpl::new);
	}

	public interface NetworkTypeRegistry<T extends NetworkType> {
		void register(Block block, NetworkMemberType... types);

		Collection<NetworkMemberType> get(WorldPos pos, @Nullable Direction direction);
	}

	public static class NetworkTypeRegistryImpl<T extends NetworkType> implements NetworkTypeRegistry<T> {
		protected final Map<Block, Collection<NetworkMemberType>> types = new Reference2ObjectOpenHashMap<>();

		public NetworkTypeRegistryImpl() {}

		public NetworkTypeRegistryImpl(T type) {}

		@Override
		public void register(Block block, NetworkMemberType... types) {
			this.types.computeIfAbsent(block, id -> Sets.newHashSet()).addAll(Arrays.asList(types));
		}

		@Override
		public Collection<NetworkMemberType> get(WorldPos pos, @Nullable Direction direction) {
			return this.types.getOrDefault(pos.getBlock(), Collections.emptySet());
		}
	}

	public class NetworkMemberImpl implements NetworkMember {
		@Nullable
		private final WorldPos pos;
		@Nullable
		private final Direction direction;

		public NetworkMemberImpl(@Nullable WorldPos pos, @Nullable Direction direction) {
			this.pos = pos;
			this.direction = direction;
		}

		@Override
		public Collection<NetworkMemberType> getMemberNetworkTypeProperties(NetworkType type) {
			if (pos == null)
				return Collections.emptySet();
			return get(type).get(pos, direction);
		}
	}
}
