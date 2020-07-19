package com.github.chainmailstudios.astromine.common.component.world;

import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkTypeRegistry;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class WorldNetworkComponent implements Component, Tickable {
	private final Set<NetworkInstance> instances = Sets.newConcurrentHashSet();

	private final World world;

	public WorldNetworkComponent(World world) {
		this.world = world;
	}

	public void addInstance(NetworkInstance instance) {
		if (!instance.nodes.isEmpty())
			this.instances.add(instance);
	}

	public void removeInstance(NetworkInstance instance) {
		this.instances.remove(instance);
	}

	public NetworkInstance getInstance(NetworkType type, BlockPos position) {
		return this.instances.stream()
				.filter(instance -> instance.getType() == type && instance.nodes.stream().anyMatch(node -> node.getBlockPos().equals(position)))
				.findFirst()
				.orElse(NetworkInstance.EMPTY);
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
				nodeList.put(String.valueOf(++i), node.toTag(new CompoundTag()));
			}

			CompoundTag memberList = new CompoundTag();

			int k = 0;

			for (NetworkMemberNode member : instance.members) {
				memberList.put(String.valueOf(++k), member.toTag(new CompoundTag()));
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
				CompoundTag nodeTag = nodeList.getCompound(nodeKey);
				instance.addNode(NetworkNode.fromTag(nodeTag));
			}

			for (String memberKey : memberList.getKeys()) {
				CompoundTag memberTag = nodeList.getCompound(memberKey);
				instance.addMember(NetworkMemberNode.fromTag(memberTag));
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
