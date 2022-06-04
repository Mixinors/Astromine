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

import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtLong;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;


public final class Network<T> {
	private static final String POSITION_KEY = "Position";
	private static final String DIRECTION_KEY = "Direction";
	private static final String SIDING_KEY = "Siding";
	
	private final Set<Member> members = Sets.newConcurrentHashSet();
	private final Set<Node> nodes = Sets.newConcurrentHashSet();
	
	@Nullable
	private final World world;
	
	@Nullable
	private final NetworkType<T> type;
	
	public Network(@Nullable World world, @Nullable NetworkType<T> type) {
		this.type = type;
		this.world = world;
	}

	public void addNetwork(Network<T> network) {
		nodes.addAll(network.nodes);
		members.addAll(network.members);
	}
	
	@Nullable
	public NetworkType<T> getType() {
		return this.type;
	}
	
	@Nullable
	public World getWorld() {
		return world;
	}
	
	public Set<Network.Member> getMembers() {
		return members;
	}
	
	public Set<Network.Node> getNodes() {
		return nodes;
	}
	
	public int size() {
		return nodes.size();
	}
	
	public boolean isEmpty() {
		return nodes.isEmpty() && members.isEmpty();
	}
	
	public void tick() {
		if (type != null) {
			type.tick(this);
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		
		var that = (Network<?>) object;
		
		return Objects.equal(members, that.members) &&
				Objects.equal(nodes, that.nodes) &&
				Objects.equal(world, that.world) &&
				Objects.equal(type, that.type);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(members, nodes, world, type);
	}
	
	public record Member(
			BlockPos blockPos,
			Direction direction,
			StorageSiding siding
	) {
		public static Member fromNbt(NbtCompound nbt) {
			return new Member(
					NbtUtil.getBlockPos(nbt, POSITION_KEY),
					Direction.valueOf(nbt.getString(DIRECTION_KEY)),
					StorageSiding.valueOf(nbt.getString(SIDING_KEY))
			);
		}
		
		public NbtCompound toNbt() {
			var nbt = new NbtCompound();
			
			NbtUtil.putBlockPos(nbt, POSITION_KEY, blockPos);
			
			nbt.putString(DIRECTION_KEY, direction.name());
			nbt.putString(SIDING_KEY, siding.name());
			
			return nbt;
		}
	}
	
	public record Node(
			BlockPos blockPos
	) {
		public static Node fromNbt(NbtLong nbt) {
			return new Node(BlockPos.fromLong(nbt.longValue()));
		}
		
		public NbtLong toNbt() {
			return NbtLong.of(blockPos.asLong());
		}
	}
}
