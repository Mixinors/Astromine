package com.github.chainmailstudios.astromine.common.network;

import com.google.common.collect.Sets;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Set;

public class NetworkInstance implements Iterable<NetworkNode>, Tickable {
	public static final NetworkInstance EMPTY = new NetworkInstance();

	public final Set<NetworkNode> members = Sets.newConcurrentHashSet();
	public final Set<NetworkNode> nodes = Sets.newConcurrentHashSet();

	private final World world;

	public NetworkType type;

	private NetworkInstance() {
		this.type = NetworkType.EMPTY;
		this.world = null;
	}

	public NetworkInstance(World world, NetworkType type) {
		this.type = type;
		this.world = world;
	}

	@Override
	public Iterator<NetworkNode> iterator() {
		return this.nodes.iterator();
	}

	public void addNode(NetworkNode node) {
		this.nodes.add(node);
	}

	public void addBlockPos(BlockPos position) {
		this.nodes.add(NetworkNode.of(position));
	}

	public void addMember(NetworkNode member) {
		this.members.add(member);
	}

	public void removeNode(NetworkNode node) {
		this.nodes.remove(node);
	}

	public void removeMember(NetworkNode node) {
		this.members.remove(node);
	}

	public NetworkInstance join(NetworkInstance controller) {
		this.nodes.addAll(controller.nodes);
		this.members.addAll(controller.members);

		return this;
	}

	public NetworkType getType() {
		return this.type;
	}

	public NetworkInstance setType(NetworkType type) {
		this.type = type;
		return this;
	}

	public World getWorld() {
		return world;
	}

	public int size() {
		return this.nodes.size();
	}

	@Override
	public void tick() {
		this.type.simulate(this);
	}
}
