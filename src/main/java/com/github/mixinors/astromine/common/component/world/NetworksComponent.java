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

package com.github.mixinors.astromine.common.component.world;

import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class NetworksComponent implements Component {
	private static final String TYPE_KEY = "Type";
	private static final String NODES_KEY = "Nodes";
	private static final String MEMBERS_KEY = "Members";
	private static final String INSTANCES = "Instances";
	
	private final List<Network<?>> instances = new ArrayList<>();
	
	private final World world;
	
	@Nullable
	public static <V> NetworksComponent get(V v) {
		try {
			return AMComponents.NETWORKS.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
	
	public NetworksComponent(World world) {
		this.world = world;
	}

	public static void onServerLevelPre(ServerWorld world) {
		var component = get(world);

		if (component != null) {
			component.tick();
		}
	}
	
	public World getWorld() {
		return world;
	}
	
	public void add(Network<?> instance) {
		if (!instance.getNodes().isEmpty()) {
			instances.add(instance);
		}
	}
	
	public void remove(Network<?> instance) {
		instances.remove(instance);
	}
	
	public <T> Network<T> get(NetworkType<T> type, BlockPos position) {
		return (Network<T>) instances.stream().filter(instance -> instance.getType() == type && instance.getNodes().stream().anyMatch(node -> node.blockPos().equals(position))).findFirst().orElse(null);
	}
	
	public <T> boolean contains(NetworkType<T> type, BlockPos position) {
		return get(type, position) != null;
	}
	
	public void tick() {
		instances.removeIf(Network::isEmpty);
		instances.forEach(Network::tick);
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound nbt) {
		var instanceTags = new NbtList();
		
		for (var instance : instances) {
			var nodeList = new NbtList();
			
			for (var node : instance.getNodes()) {
				nodeList.add(node.toNbt());
			}
			
			var memberList = new NbtList();
			
			for (var member : instance.getMembers()) {
				memberList.add(member.toNbt());
			}
			
			var type = instance.getType();
			
			if (type == null) {
				continue;
			}
			
			var data = new NbtCompound();
			
			NbtUtil.putIdentifier(data, TYPE_KEY, AMRegistries.NETWORK_TYPE.getKey(type));
			
			data.put(NODES_KEY, nodeList);
			data.put(MEMBERS_KEY, memberList);
			
			instanceTags.add(data);
		}
		
		nbt.put(INSTANCES, instanceTags);
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		var instanceTags = nbt.getList(INSTANCES, NbtElement.COMPOUND_TYPE);
		
		for (var instanceTag : instanceTags) {
			var dataTag = (NbtCompound) instanceTag;
			
			var nodeList = dataTag.getList(NODES_KEY, NbtElement.LONG_TYPE);
			var memberList = dataTag.getList(MEMBERS_KEY, NbtElement.COMPOUND_TYPE);
			
			var type = AMRegistries.NETWORK_TYPE.getEntry(NbtUtil.getIdentifier(dataTag, TYPE_KEY)).get();
			
			if (type == null) {
				continue;
			}
			
			var instance = new Network<>(world, type);
			
			for (var nodeKey : nodeList) {
				instance.getNodes().add(Network.Node.fromNbt((NbtLong) nodeKey));
			}
			
			for (var memberTag : memberList) {
				instance.getMembers().add(Network.Member.fromNbt((NbtCompound) memberTag));
			}
			
			add(instance);
		}
	}
}