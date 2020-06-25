package com.github.chainmailstudios.astromine.common.component.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkTypeRegistry;
import nerdhub.cardinal.components.api.component.Component;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Sets;
import java.util.Set;

public class WorldNetworkComponent implements Component, Tickable {
	private final Set<NetworkInstance> instances = Sets.newConcurrentHashSet();

	private final World world;

	public WorldNetworkComponent(World world) {
		this.world = world;
	}

	public void addInstance(NetworkInstance controller) {
		this.instances.add(controller);
	}

	public void removeInstance(NetworkInstance controller) {
		this.instances.remove(controller);
	}

	public NetworkInstance getInstance(NetworkType type, BlockPos position) {
		for (NetworkInstance controller : this.instances) {
			if (controller.getType() == type && controller.nodes.contains(NetworkNode.of(position))) {
				return controller;
			}
		}

		return NetworkInstance.EMPTY;
	}

	public boolean containsInstance(NetworkType type, BlockPos position) {
		return getInstance(type, position) != NetworkInstance.EMPTY;
	}

	public World getWorld() {
		return world;
	}

	@Override
	@NotNull
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag instanceTag = new CompoundTag();

		for (NetworkInstance instance : instances) {
			int hash = instance.hashCode();

			CompoundTag nodeList = new CompoundTag();

			int i = 0;

			for (NetworkNode node : instance.nodes) {
				nodeList.putLong(String.valueOf(++i), node.getPos());
			}

			CompoundTag memberList = new CompoundTag();

			int k = 0;

			for (NetworkNode member : instance.members) {
				nodeList.putLong(String.valueOf(++k), member.getPos());
			}

			CompoundTag data = new CompoundTag();

			data.putString("type", NetworkTypeRegistry.INSTANCE.getKey(instance.getType()).toString());

			data.put("nodes", nodeList);
			data.put("members", memberList);

			instanceTag.put(String.valueOf(hash), data);
		}

		tag.put("instances", instanceTag);

		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag instanceTag = tag.getCompound("instances");
		for (String controllerKey : instanceTag.getKeys()) {
			CompoundTag dataTag = instanceTag.getCompound(controllerKey);
			CompoundTag nodeList = dataTag.getCompound("nodes");
			CompoundTag memberList = dataTag.getCompound("members");

			NetworkType type = NetworkTypeRegistry.INSTANCE.get(new Identifier(dataTag.getString("type")));

			NetworkInstance instance = new NetworkInstance(world, type);

			for (String nodeKey : nodeList.getKeys()) {
				instance.addNode(NetworkNode.of(nodeList.getLong(nodeKey)));
			}

			for (String memberKey : memberList.getKeys()) {
				instance.addMember(NetworkNode.of(memberList.getLong(memberKey)));
			}

			addInstance(instance);
		}
	}

	@Override
	public void tick() {
		this.instances.forEach(NetworkInstance::tick);
	}
}
