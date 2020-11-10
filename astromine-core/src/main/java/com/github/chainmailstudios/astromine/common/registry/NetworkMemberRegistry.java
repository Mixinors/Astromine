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

import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.base.BiRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.position.WorldPos;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * A class representing a specialized registry for
 * registration of {@link NetworkTypeProvider}s mapped to {@link NetworkType}s.
 */
public class NetworkMemberRegistry {
	public static final NetworkMemberRegistry INSTANCE = new NetworkMemberRegistry();

	private final Map<NetworkType, NetworkTypeProvider<?>> registries = new Reference2ObjectOpenHashMap<>();

	/** We only want one instance of this. */
	private NetworkMemberRegistry() {}

	/** Registers a given {@link NetworkType} to a {@link NetworkTypeProvider}. */
	public <T extends NetworkType, R extends NetworkTypeProvider<?>> void register(T t, R r) {
		registries.put(t, r);
	}

	/** Returns the {@link NetworkTypeProvider} associated with the given {@link NetworkType}. */
	public <T extends NetworkType> NetworkTypeProvider<T> get(T type) {
		return (NetworkTypeProvider<T>) registries.computeIfAbsent(type, NetworkTypeProviderImpl::new);
	}

	/** Returns the {@link NetworkMember} at the given position. */
	public static NetworkMember get(@Nullable WorldPos pos) {
		return INSTANCE.new NetworkMemberImpl(pos);
	}

	/** Returns the {@link NetworkMember} at the given {@link BlockEntity}'s position. */
	public static NetworkMember get(@Nullable BlockEntity blockEntity) {
		return blockEntity != null ? get(WorldPos.of(blockEntity.getWorld(), blockEntity.getPos())) : get((WorldPos) null);
	}

	/**
	 * An interface representing a provider of {@link NetworkType}s,
	 * used to gather the types present at a given position.
	 *
	 * {@link NetworkTypeProvider#register(Block, NetworkMemberType...)}
	 * registers the available types for the given block.
	 *
	 * {@link NetworkTypeProvider#get(WorldPos)} returns the types
	 * available at the given position.
	 */
	public interface NetworkTypeProvider<T extends NetworkType> {
		void register(Block block, NetworkMemberType... types);

		Collection<NetworkMemberType> get(WorldPos pos);
	}

	/**
	 * A class representing an implementation of {@link NetworkTypeProvider},
	 * defaulting to a {@link Map} for lookup of blocks.
	 */
	public static class NetworkTypeProviderImpl<T extends NetworkType> implements NetworkTypeProvider<T> {
		protected final Map<Block, Collection<NetworkMemberType>> types = new Reference2ObjectOpenHashMap<>();

		/** Instantiates a {@link NetworkTypeProviderImpl}. */
		public NetworkTypeProviderImpl() {}

		/** Instantiates a {@link NetworkTypeProviderImpl}.
		 * This solely exists to keep our code cleaner in {@link #get(NetworkType)}. */
		public NetworkTypeProviderImpl(T type) {}

		/** Override behavior to use our {@link Map}. */
		@Override
		public void register(Block block, NetworkMemberType... types) {
			this.types.computeIfAbsent(block, id -> Sets.newHashSet()).addAll(Arrays.asList(types));
		}

		/** Override behavior to use our {@link Map}. */
		@Override
		public Collection<NetworkMemberType> get(WorldPos pos) {
			return this.types.getOrDefault(pos.getBlock(), Collections.emptySet());
		}
	}

	/**
	 * A class representing an implementation of {@link NetworkMember}
	 * defaulting to a {@link WorldPos} for lookup of types.
	 */
	public class NetworkMemberImpl implements NetworkMember {
		@Nullable
		private final WorldPos pos;

		/** Instantiates a {@link NetworkMemberImpl} with the given value. */
		public NetworkMemberImpl(@Nullable WorldPos pos) {
			this.pos = pos;
		}

		/** Override behavior to use our {@link WorldPos}. */
		@Override
		public Collection<NetworkMemberType> getNetworkMemberTypes(NetworkType type) {
			return pos == null ? Collections.emptySet() : get(type).get(pos);
		}
	}
}
