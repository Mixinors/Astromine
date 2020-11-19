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

package com.github.chainmailstudios.astromine.common.component.world;

import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkTypeRegistry;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A {@link Component} which stores information about
 * a {@link World}'s networks.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #writeToNbt(CompoundTag)} and {@link #readFromNbt(CompoundTag)}.
 */
public class WorldNetworkComponent implements Component, Tickable {
	private final Set<NetworkInstance> instances = Sets.newConcurrentHashSet();

	private final World world;

	/** Instantiates a {@link WorldNetworkComponent} with the given value. */
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
	@Override
	public void tick() {
		this.instances.removeIf(NetworkInstance::isEmpty);
		this.instances.forEach(NetworkInstance::tick);
	}

	/** Serializes this {@link WorldNetworkComponent} to a {@link CompoundTag}. */
	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag instanceTags = new ListTag();

		for (NetworkInstance instance : instances) {
			ListTag nodeList = new ListTag();
			for (NetworkNode node : instance.nodes) {
				nodeList.add(LongTag.of(node.getLongPosition()));
			}

			ListTag memberList = new ListTag();
			for (NetworkMemberNode member : instance.members) {
				memberList.add(member.toTag());
			}

			CompoundTag data = new CompoundTag();

			data.putString("type", NetworkTypeRegistry.INSTANCE.getKey(instance.getType()).toString());
			data.put("nodes", nodeList);
			data.put("members", memberList);

			instanceTags.add(data);
		}

		tag.put("instanceTags", instanceTags);
	}

	/** Deserializes this {@link WorldNetworkComponent} from a {@link CompoundTag}. */
	@Override
	public void readFromNbt(CompoundTag tag) {
		ListTag instanceTags = tag.getList("instanceTags", NbtType.COMPOUND);
		for (Tag instanceTag : instanceTags) {
			CompoundTag dataTag = (CompoundTag) instanceTag;
			ListTag nodeList = dataTag.getList("nodes", NbtType.LONG);
			ListTag memberList = dataTag.getList("members", NbtType.COMPOUND);

			NetworkType type = NetworkTypeRegistry.INSTANCE.get(new Identifier(dataTag.getString("type")));
			NetworkInstance instance = new NetworkInstance(world, type);

			for (Tag nodeKey : nodeList) {
				instance.addNode(NetworkNode.of(((LongTag) nodeKey).getLong()));
			}

			for (Tag memberTag : memberList) {
				instance.addMember(NetworkMemberNode.fromTag((CompoundTag) memberTag));
			}

			add(instance);
		}
	}

	@Nullable
	public static <V> WorldNetworkComponent get(V v) {
		try {
			return AstromineComponents.WORLD_NETWORK_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
}
