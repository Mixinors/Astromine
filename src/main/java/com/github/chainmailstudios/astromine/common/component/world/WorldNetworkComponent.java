package com.github.chainmailstudios.astromine.common.component.world;

import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkTypeRegistry;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.component.Component;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
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

			instanceTags.add(data);
		}

		tag.put("instanceTags", instanceTags);

		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
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

			addInstance(instance);
		}
	}

	@Override
	public void tick() {
		this.instances.removeIf(NetworkInstance::isStupidlyEmpty);
		this.instances.forEach(NetworkInstance::tick);
	}
}
