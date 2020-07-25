package com.github.chainmailstudios.astromine.client.registry;

import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class NetworkMemberRegistry {
	public static final NetworkMemberRegistry INSTANCE = new NetworkMemberRegistry();

	private Map<NetworkType, NetworkTypeRegistry<?>> registries = new Reference2ObjectOpenHashMap<>();
	private Map<Block, NetworkMember> memberCache = new Reference2ObjectOpenHashMap<>();

	private NetworkMemberRegistry() {
		// Locked.
	}

	public static NetworkMember get(@Nullable Block block) {
		return INSTANCE.memberCache.computeIfAbsent(block, b -> INSTANCE.new NetworkMemberImpl(b));
	}

	public static NetworkMember get(@Nullable BlockEntity blockEntity) {
		return blockEntity != null && blockEntity.getCachedState() != null ? get(blockEntity.getCachedState().getBlock()) : get((Block) null);
	}

	public <T extends NetworkType> NetworkTypeRegistry<T> get(T type) {
		return (NetworkTypeRegistry<T>) registries.computeIfAbsent(type, NetworkTypeRegistryImpl::new);
	}

	public interface NetworkTypeRegistry<T extends NetworkType> {
		default void register(Block block, NetworkMemberType... types) {
			register(Registry.BLOCK.getId(block), types);
		}

		void register(Identifier id, NetworkMemberType... types);

		Collection<NetworkMemberType> get(Identifier id);
	}

	private static class NetworkTypeRegistryImpl<T extends NetworkType> implements NetworkTypeRegistry<T> {
		private final T type;
		private final Map<Identifier, Collection<NetworkMemberType>> types = Maps.newHashMap();

		public NetworkTypeRegistryImpl(T type) {
			this.type = type;
		}

		@Override
		public void register(Identifier identifier, NetworkMemberType... types) {
			this.types.computeIfAbsent(identifier, id -> Sets.newHashSet()).addAll(Arrays.asList(types));
		}

		@Override
		public Collection<NetworkMemberType> get(Identifier id) {
			return this.types.getOrDefault(id, Collections.emptySet());
		}
	}

	private class NetworkMemberImpl implements NetworkMember {
		@Nullable
		private Block block;

		public NetworkMemberImpl(@Nullable Block block) {
			this.block = block;
		}

		@Override
		public Collection<NetworkMemberType> getMemberNetworkTypeProperties(NetworkType type) {
			if (block == null) return Collections.emptySet();
			return get(type).get(Registry.BLOCK.getId(block));
		}
	}
}
