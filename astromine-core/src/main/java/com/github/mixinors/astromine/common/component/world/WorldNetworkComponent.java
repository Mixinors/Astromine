/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.component.world;

import com.github.mixinors.astromine.common.network.NetworkInstance;
import com.github.mixinors.astromine.common.network.NetworkMemberNode;
import com.github.mixinors.astromine.common.network.NetworkNode;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkTypeRegistry;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.google.common.collect.Sets;
import dev.architectury.utils.NbtType;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A {@link Component} which stores information about
 * a {@link World}'s networks.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link NbtCompound} - through {@link #writeToNbt(NbtCompound)} and {@link #readFromNbt(NbtCompound)}.
 */
public final class WorldNetworkComponent implements Component {
	private final Set<NetworkInstance> instances = Sets.newConcurrentHashSet();

	private final World world;

	/** Instantiates a {@link WorldNetworkComponent}. */
	public WorldNetworkComponent(World world) {
		this.world = world;
	}

	/** Returns this component's world. */
	public World getWorld() {
		return world;
	}

	/** Adds the given {@link NetworkInstance} to this component. */
	public void add(NetworkInstance instance) {
		if (!instance.nodes.isEmpty())
			this.instances.add(instance);
	}

	/** Removes the given {@link NetworkInstance} from this component. */
	public void remove(NetworkInstance instance) {
		this.instances.remove(instance);
	}

	/** Returns the {@link NetworkInstance} of the given {@link NetworkType}
	 * at the specified {@link BlockPos}. */
	public NetworkInstance get(NetworkType type, BlockPos position) {
		return this.instances.stream().filter(instance -> instance.getType() == type && instance.nodes.stream().anyMatch(node -> node.getBlockPosition().equals(position))).findFirst().orElse(NetworkInstance.EMPTY);
	}

	/** Asserts whether any {@link NetworkInstance} exists for the given {@link NetworkType}
	 * at the specified {@link BlockPos}. */
	public boolean contains(NetworkType type, BlockPos position) {
		return get(type, position) != NetworkInstance.EMPTY;
	}

	/** Override behavior to implement network ticking logic. */
	public void tick() {
		this.instances.removeIf(NetworkInstance::isEmpty);
		this.instances.forEach(NetworkInstance::tick);
	}

	/** Serializes this {@link WorldNetworkComponent} to a {@link NbtCompound}. */
	@Override
	public void writeToNbt(NbtCompound tag) {
		var instanceTags = new NbtList();

		for (NetworkInstance instance : instances) {
			var nodeList = new NbtList();
			for (NetworkNode node : instance.nodes) {
				nodeList.add(NbtLong.of(node.getLongPosition()));
			}

			var memberList = new NbtList();
			for (NetworkMemberNode member : instance.members) {
				memberList.add(member.toTag());
			}

			var data = new NbtCompound();

			data.putString("type", NetworkTypeRegistry.INSTANCE.getKey(instance.getType()).toString());
			data.put("nodes", nodeList);
			data.put("members", memberList);

			instanceTags.add(data);
		}

		tag.put("instanceTags", instanceTags);
	}

	/** Deserializes this {@link WorldNetworkComponent} from a {@link NbtCompound}. */
	@Override
	public void readFromNbt(NbtCompound tag) {
		var instanceTags = tag.getList("instanceTags", NbtType.COMPOUND);
		for (NbtElement instanceTag : instanceTags) {
			var dataTag = (NbtCompound) instanceTag;
			var nodeList = dataTag.getList("nodes", NbtType.LONG);
			var memberList = dataTag.getList("members", NbtType.COMPOUND);

			var type = NetworkTypeRegistry.INSTANCE.get(new Identifier(dataTag.getString("type")));
			var instance = new NetworkInstance(world, type);

			for (NbtElement nodeKey : nodeList) {
				instance.addNode(NetworkNode.of(((NbtLong) nodeKey).longValue()));
			}

			for (NbtElement memberTag : memberList) {
				instance.addMember(NetworkMemberNode.fromTag((NbtCompound) memberTag));
			}

			add(instance);
		}
	}

	/** Returns the {@link WorldNetworkComponent} of the given {@link V}. */
	@Nullable
	public static <V> WorldNetworkComponent get(V v) {
		try {
			return AMComponents.WORLD_NETWORK_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
}
