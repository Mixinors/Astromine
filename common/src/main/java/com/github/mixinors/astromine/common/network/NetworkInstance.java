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

package com.github.mixinors.astromine.common.network;

import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkMemberRegistry;
import com.github.mixinors.astromine.common.registry.NetworkTypeRegistry;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A collection of {@link NetworkNode}s
 * and {@link NetworkMemberNode}s, representing a simple
 * network structure.
 */
public final class NetworkInstance implements Tickable {
	public static final NetworkInstance EMPTY = new NetworkInstance();

	public final Set<NetworkMemberNode> members = Sets.newConcurrentHashSet();

	public final Set<NetworkNode> nodes = Sets.newConcurrentHashSet();

	private final World world;

	private final NetworkType type;

	/** Instantiates a {@link NetworkInstance} with {@link NetworkType#EMPTY}. */
	private NetworkInstance() {
		this.type = NetworkType.EMPTY;
		this.world = null;
	}

	/** Instantiates a {@link NetworkInstance}. */
	public NetworkInstance(World world, NetworkType type) {
		this.type = type;
		this.world = world;
	}

	/** Adds the given node to this instance. */
	public void addNode(NetworkNode node) {
		this.nodes.add(node);
	}

	/** Adds the given position as a node to this instance. */
	public void addBlockPos(BlockPos position) {
		this.nodes.add(NetworkNode.of(position));
	}

	/** Adds the given node as a member node to this instance. */
	public void addMember(NetworkMemberNode member) {
		this.members.add(member);
	}

	/** Removes the given node from this instance. */
	public void removeNode(NetworkNode node) {
		this.nodes.remove(node);
	}

	/** Removes the given member node from this instance. */
	public void removeMember(NetworkMemberNode node) {
		this.members.remove(node);
	}

	/** Joins this instance with another, taking
	 * their nodes and member nodes. */
	public NetworkInstance join(NetworkInstance controller) {
		this.nodes.addAll(controller.nodes);
		this.members.addAll(controller.members);

		return this;
	}

	/** Returns this instance's type. */
	public NetworkType getType() {
		return this.type;
	}

	/** Returns this instance's world. */
	public World getWorld() {
		return world;
	}

	/** Returns this instance's size. */
	public int size() {
		return this.nodes.size();
	}

	/** Asserts whether this instance is empty, or not. */
	public boolean isEmpty() {
		this.nodes.removeIf(node -> !NetworkMemberRegistry.get(WorldPos.of(world, node.getBlockPosition()), null).isNode(getType()));
		if (this.nodes.isEmpty()) {
			AMCommon.LOGGER.error("Network is empty! " + toString());
			return true;
		}
		return false;
	}

	/** Override behavior to tick network via our {@link NetworkType}. */
	@Override
	public void tick() {
		this.type.tick(this);
	}

	/** Asserts the equality of the objects. */
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;

		if (object == null || getClass() != object.getClass()) return false;

		NetworkInstance that = (NetworkInstance) object;

		return Objects.equal(members, that.members) &&
				Objects.equal(nodes, that.nodes) &&
				Objects.equal(world, that.world) &&
				Objects.equal(type, that.type);
	}

	/** Returns the hash for this instance. */
	@Override
	public int hashCode() {
		return Objects.hashCode(members, nodes, world, type);
	}

	/** Returns this node's string representation.
	 * For example, it may be
	 * "astromine:fluid_network, minecraft:overworld
	 *  [323, 54, 73], NORTH
	 *  [595, 58, 27], SOUTH
	 *  [4933, 93, 4]
	 *  [593, 58, 95]"
	 * */
	@Override
	public String toString() {
		return String.format(
				"%s, %s,\n%s,\n%s",
				NetworkTypeRegistry.INSTANCE.getKey(type),
				world.getRegistryKey().getValue(),
				members.stream().map(NetworkMemberNode::toString).collect(Collectors.joining("\n")),
				nodes.stream().map(NetworkNode::toString).collect(Collectors.joining("\n"))
		);
	}
}
