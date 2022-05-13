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
import com.github.mixinors.astromine.common.network.type.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkTypeRegistry;
import com.github.mixinors.astromine.registry.common.AMComponents;
import dev.architectury.utils.NbtType;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public final class WorldNetworkComponent implements Component {
	private static final String TYPE_KEY = "Type";
	private static final String NODES_KEY = "Nodes";
	private static final String MEMBERS_KEY = "Members";
	private static final String INSTANCES = "Instances";
	
	private final List<Network> instances = new ArrayList<>();
	
	private final World world;
	
	public WorldNetworkComponent(World world) {
		this.world = world;
	}
	
	@Nullable
	public static <V> WorldNetworkComponent get(V v) {
		try {
			return AMComponents.WORLD_NETWORK_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
	
	public World getWorld() {
		return world;
	}
	
	public void add(Network instance) {
		if (!instance.nodes.isEmpty()) {
			this.instances.add(instance);
		}
	}
	
	public void remove(Network instance) {
		this.instances.remove(instance);
	}
	
	public Network get(NetworkType type, BlockPos position) {
		return this.instances.stream().filter(instance -> instance.getType() == type && instance.nodes.stream().anyMatch(node -> ((Network.Node) node).blockPos().equals(position))).findFirst().orElse(null);
	}
	
	public boolean contains(NetworkType type, BlockPos position) {
		return get(type, position) != null;
	}
	
	public void tick() {
		this.instances.removeIf(Network::isEmpty);
		this.instances.forEach(Network::tick);
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		var instanceTags = new NbtList();
		
		for (var instance : instances) {
			var nodeList = new NbtList();
			
			for (var node : instance.nodes) {
				nodeList.add(NbtLong.of(((Network.Node) node).longPos()));
			}
			
			var memberList = new NbtList();
			
			for (var member : instance.members) {
				memberList.add(((Network.Member) member).toTag());
			}
			
			var type = instance.getType();
			
			if (type == null) {
				continue;
			}
			
			var data = new NbtCompound();
			
			data.putString(TYPE_KEY, NetworkTypeRegistry.INSTANCE.getKey(type).toString());
			data.put(NODES_KEY, nodeList);
			data.put(MEMBERS_KEY, memberList);
			
			instanceTags.add(data);
		}
		
		tag.put(INSTANCES, instanceTags);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		var instanceTags = tag.getList(INSTANCES, NbtType.COMPOUND);
		
		for (var instanceTag : instanceTags) {
			var dataTag = (NbtCompound) instanceTag;
			var nodeList = dataTag.getList(NODES_KEY, NbtType.LONG);
			var memberList = dataTag.getList(MEMBERS_KEY, NbtType.COMPOUND);
			
			var type = NetworkTypeRegistry.INSTANCE.get(new Identifier(dataTag.getString(TYPE_KEY)));
			
			if (type == null) {
				continue;
			}
			
			var instance = new Network(world, type);
			
			for (var nodeKey : nodeList) {
				instance.addNode(new Network.Node(((NbtLong) nodeKey).longValue()));
			}
			
			for (var memberTag : memberList) {
				instance.addMember(Network.Member.fromTag((NbtCompound) memberTag));
			}
			
			add(instance);
		}
	}
}