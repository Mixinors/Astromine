package com.github.chainmailstudios.astromine.common.network;

import com.google.common.collect.Sets;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Set;

public class NetworkController implements Iterable<NetworkNode>, Tickable {
	public static final NetworkController EMPTY = new NetworkController();
	public final Set<NetworkNode> memberNodes = Sets.newConcurrentHashSet();
	public final Set<NetworkNode> nodes = Sets.newConcurrentHashSet();
	public NetworkTicker type = NetworkTicker.EMPTY;
	public World world;

	private NetworkController() {
	}

	public NetworkController(World world, NetworkTicker type) {
		this.type = type;
		this.world = world;
	}

	@Override
	public Iterator<NetworkNode> iterator() {
		return nodes.iterator();
	}

	public void addNode(NetworkNode node) {
		nodes.add(node);
	}

	public void addPosition(BlockPos position) {
		nodes.add(NetworkNode.of(position));
	}

	public void addMember(NetworkNode member) {
		memberNodes.add(member);
	}

	public void removeNode(NetworkNode node) {
		nodes.remove(node);
	}

	public void removeMember(NetworkNode node) {
		memberNodes.remove(node);
	}

	public NetworkController join(NetworkController controller) {
		nodes.addAll(controller.nodes);
		memberNodes.addAll(controller.memberNodes);
		NetworkManager.INSTANCE.remove(controller);
		return this;
	}

	public Boolean isNullOrEmpty() {
		return type == NetworkTicker.EMPTY;
	}

	public NetworkTicker getType() {
		return type;
	}

	public NetworkController setType(NetworkTicker type) {
		this.type = type;
		return this;
	}

	public int size() {
		return nodes.size();
	}

	@Override
	public void tick() {
		type.tick(this);
	}
}
