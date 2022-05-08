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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public final class WorldNetworkComponent implements Component {
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
	public void writeToNbt(NbtCompound tag) {
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
			
			data.putString("Type", NetworkTypeRegistry.INSTANCE.getKey(type).toString());
			data.put("Nodes", nodeList);
			data.put("Members", memberList);
			
			instanceTags.add(data);
		}
		
		tag.put("Instances", instanceTags);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		var instanceTags = tag.getList("Instances", NbtType.COMPOUND);
		
		for (var instanceTag : instanceTags) {
			var dataTag = (NbtCompound) instanceTag;
			var nodeList = dataTag.getList("Nodes", NbtType.LONG);
			var memberList = dataTag.getList("Members", NbtType.COMPOUND);
			
			var type = NetworkTypeRegistry.INSTANCE.get(new Identifier(dataTag.getString("Type")));
			
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