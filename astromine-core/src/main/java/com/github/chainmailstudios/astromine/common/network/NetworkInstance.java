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

package com.github.chainmailstudios.astromine.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.registry.NetworkTypeRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.position.WorldPos;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;

public class NetworkInstance implements Iterable<NetworkNode>, Tickable {
	public static final NetworkInstance EMPTY = new NetworkInstance();

	public final Set<NetworkMemberNode> members = Sets.newConcurrentHashSet();
	public final Set<NetworkNode> nodes = Sets.newConcurrentHashSet();

	private final World world;
	public NetworkType type;
	private CompoundTag additionalData = new CompoundTag();

	private NetworkInstance() {
		this.type = NetworkType.EMPTY;
		this.world = null;
	}

	public NetworkInstance(World world, NetworkType type) {
		this.type = type;
		this.world = world;
	}

	public CompoundTag getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(CompoundTag additionalData) {
		this.additionalData = additionalData;
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

	public void addMember(NetworkMemberNode member) {
		this.members.add(member);
	}

	public void removeNode(NetworkNode node) {
		this.nodes.remove(node);
	}

	public void removeMember(NetworkMemberNode node) {
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
		this.type.tick(this);
	}

	@Override
	public String toString() {
		return "NetworkInstance{" + "type=" + NetworkTypeRegistry.INSTANCE.getKey(type) + ", world=" + world.getRegistryKey().getValue() + ", members=" + members + ", nodes=" + nodes + ", additionalData=" + additionalData + '}';
	}

	public boolean isStupidlyEmpty() {
		this.nodes.removeIf(node -> !NetworkMemberRegistry.get(WorldPos.of(world, node.getBlockPos()), null).isNode(getType()));
		if (this.nodes.isEmpty()) {
			AstromineCommon.LOGGER.error("Network is empty! " + toString());
			return true;
		}
		return false;
	}
}
