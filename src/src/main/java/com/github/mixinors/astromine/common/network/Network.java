/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.common.network.type.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkTypeRegistry;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A collection of {@link Node}s and {@link Member}s, representing a simple network structure.
 */
public final class Network<T extends NetworkType> {
	private static final String POSITION_KEY = "Position";
	private static final String DIRECTION_KEY = "Direction";
	private static final String SIDING_KEY = "Siding";
	
	public final Set<Member> members = Sets.newConcurrentHashSet();
	
	public final Set<Node> nodes = Sets.newConcurrentHashSet();
	
	@Nullable
	private final World world;
	
	@Nullable
	private final T type;
	
	/** Instantiates a {@link Network}. */
	public Network(@Nullable World world, @Nullable T type) {
		this.type = type;
		this.world = world;
	}
	
	/** Adds the given node to this instance. */
	public void addNode(Node node) {
		this.nodes.add(node);
	}
	
	/** Adds the given position as a node to this instance. */
	public void addBlockPos(BlockPos position) {
		this.nodes.add(new Node(position));
	}
	
	/** Adds the given node as a member node to this instance. */
	public void addMember(Member member) {
		this.members.add(member);
	}
	
	/** Removes the given node from this instance. */
	public void removeNode(Node node) {
		this.nodes.remove(node);
	}
	
	/** Removes the given member node from this instance. */
	public void removeMember(Member node) {
		this.members.remove(node);
	}
	
	/**
	 * Joins this instance with another, taking their nodes and member nodes.
	 */
	public Network join(Network controller) {
		this.nodes.addAll(controller.nodes);
		this.members.addAll(controller.members);
		
		return this;
	}
	
	/** Returns this instance's type. */
	public @Nullable
	NetworkType getType() {
		return this.type;
	}
	
	/** Returns this instance's world. */
	public @Nullable
	World getWorld() {
		return world;
	}
	
	/** Returns this instance's size. */
	public int size() {
		return this.nodes.size();
	}
	
	/** Asserts whether this instance is empty, or not. */
	public boolean isEmpty() {
		return this.type == null || (this.nodes.isEmpty() && this.members.isEmpty());
	}
	
	/** Override behavior to tick network via our {@link NetworkType}. */
	public void tick() {
		if (type != null) {
			this.type.tick(this);
		}
	}
	
	/** Asserts the equality of the objects. */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		
		var that = (Network) object;
		
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
	
	/**
	 * Returns this node's string representation. For example, it may be "astromine:fluid_network, minecraft:overworld [323, 54, 73], NORTH [595, 58, 27], SOUTH [4933, 93, 4] [593, 58, 95]"
	 */
	@Override
	public String toString() {
		if (type == null) {
			return "Empty";
		} else {
			return String.format(
					"%s, %s,\n%s,\n%s",
					NetworkTypeRegistry.INSTANCE.getKey(type),
					world.getRegistryKey().getValue(),
					members.stream().map(Member::toString).collect(Collectors.joining("\n")),
					nodes.stream().map(Node::toString).collect(Collectors.joining("\n"))
			);
		}
	}
	
	/**
	 * A node for a {@link Member} of a network.
	 * <br>
	 * Serialization and deserialization methods are provided for: - {@link NbtCompound} - through {@link #toTag()} and {@link #fromTag(NbtCompound)}.
	 */
	public static class Member {
		private long longPos;
		
		private Direction direction;
		
		private StorageSiding siding;
		
		public Member(long longPos, Direction direction, StorageSiding siding) {
			this.longPos = longPos;
			this.direction = direction;
			this.siding = siding;
		}
		
		/** Instantiates a {@link Member}. */
		public Member(BlockPos blockPos, Direction direction, StorageSiding siding) {
			this(blockPos.asLong(), direction, siding);
		}
		
		/** Returns this node's {@link BlockPos} from its long representation. */
		public BlockPos getBlockPos() {
			return BlockPos.fromLong(this.longPos);
		}
		
		/** Returns this node's {@link Direction} from its id representation. **/
		public Direction getDirection() {
			return direction;
		}
		
		public StorageSiding getSiding() {
			return siding;
		}
		
		public void setSiding(StorageSiding siding) {
			this.siding = siding;
		}
		
		/** Returns this node's {@link BlockEntity}. **/
		public BlockEntity blockEntity(World world) {
			return world.getBlockEntity(getBlockPos());
		}
		
		/**
		 * Returns this node's string representation. For example, it may be "[273, 98, -479], NORTH"
		 */
		@Override
		public String toString() {
			return String.format("[%s, %s, %s], %s", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getY(), direction.asString().toUpperCase());
		}
		
		/** Deserializes a {@link Member} from a {@link NbtCompound}. */
		public static Member fromTag(NbtCompound tag) {
			return new Member(
					tag.getLong(POSITION_KEY),
					Direction.valueOf(tag.getString(DIRECTION_KEY)),
					StorageSiding.valueOf(tag.getString(SIDING_KEY))
			);
		}
		
		/** Serializes a {@link Member} to a {@link NbtCompound}. */
		public NbtCompound toTag() {
			var tag = new NbtCompound();
			
			tag.putLong(POSITION_KEY, longPos);
			
			tag.putString(DIRECTION_KEY, direction.name());
			tag.putString(SIDING_KEY, siding.name());
			
			return tag;
		}
	}
	
	/**
	 * A node for a network, with no attached {@link Node}.
	 * <br>
	 * Serialization and deserialization methods are provided for: - {@link NbtCompound} - through {@link #toTag()} and {@link #fromTag(NbtCompound)}.
	 */
	public record Node(long longPos) {
		/** Instantiates a {@link Member}. */
		public Node(BlockPos blockPos) {
			this(blockPos.asLong());
		}
		
		/** Returns this node's {@link BlockPos} from its long representation. */
		public BlockPos blockPos() {
			return BlockPos.fromLong(this.longPos);
		}
		
		/**
		 * Returns this node's string representation. For example, it may be "[184, 47, -759]"
		 */
		@Override
		public String toString() {
			return String.format("[%s, %s, %s]", blockPos().getX(), blockPos().getY(), blockPos().getY());
		}
		
		/** Deserializes a {@link Node} from a {@link NbtCompound}. */
		public static Node fromTag(NbtCompound tag) {
			return new Node(tag.getLong(POSITION_KEY));
		}
		
		/** Serializes a {@link Node} to a {@link NbtCompound}. */
		public NbtCompound toTag() {
			var tag = new NbtCompound();
			
			tag.putLong(POSITION_KEY, longPos);
			
			return tag;
		}
	}
}
