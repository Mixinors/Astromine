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
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import dev.onyxstudios.cca.api.v3.component.Component;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;
import java.util.Set;

public class WorldNetworkComponent implements Component, Tickable {
	private final Set<NetworkInstance> instances = Sets.newConcurrentHashSet();

	private final World world;

	public WorldNetworkComponent(World world) {
		this.world = world;
	}

	@Nullable
	public static <V> WorldNetworkComponent get(V v) {
		try {
			return AstromineComponents.WORLD_NETWORK_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	public void addInstance(NetworkInstance instance) {
		if (!instance.nodes.isEmpty())
			this.instances.add(instance);
	}

	public void removeInstance(NetworkInstance instance) {
		this.instances.remove(instance);
	}

	public NetworkInstance getInstance(NetworkType type, BlockPos position) {
		return this.instances.stream().filter(instance -> instance.getType() == type && instance.nodes.stream().anyMatch(node -> node.getBlockPos().equals(position))).findFirst().orElse(NetworkInstance.EMPTY);
	}

	public boolean containsInstance(NetworkType type, BlockPos position) {
		return getInstance(type, position) != NetworkInstance.EMPTY;
	}

	public World getWorld() {
		return world;
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag instanceTags = new ListTag();

		for (NetworkInstance instance : instances) {
			ListTag nodeList = new ListTag();
			for (NetworkNode node : instance.nodes) {
				nodeList.add(LongTag.of(node.getPos()));
			}

			ListTag memberList = new ListTag();
			for (NetworkMemberNode member : instance.members) {
				memberList.add(member.toTag(new CompoundTag()));
			}

			CompoundTag data = new CompoundTag();

			data.putString("type", NetworkTypeRegistry.INSTANCE.getKey(instance.getType()).toString());
			data.put("nodes", nodeList);
			data.put("members", memberList);
			data.put("additionalData", instance.getAdditionalData());

			instanceTags.add(data);
		}

		tag.put("instanceTags", instanceTags);
	}

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

			if (dataTag.contains("additionalData")) {
				instance.setAdditionalData(dataTag.getCompound("additionalData"));
			}

			addInstance(instance);
		}
	}

	@Override
	public void tick() {
		this.instances.removeIf(NetworkInstance::isStupidlyEmpty);
		this.instances.forEach(NetworkInstance::tick);
	}
}
