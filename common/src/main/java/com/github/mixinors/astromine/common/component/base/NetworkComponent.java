package com.github.mixinors.astromine.common.component.base;

import com.github.mixinors.astromine.common.component.Component;
import com.github.mixinors.astromine.common.network.NetworkInstance;
import com.github.mixinors.astromine.common.network.NetworkMemberNode;
import com.github.mixinors.astromine.common.network.NetworkNode;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkTypeRegistry;
import com.github.mixinors.astromine.registry.common.AMComponents;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.utils.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface NetworkComponent extends Component, Component.ServerTicking {
	/** Returns the {@link NetworkComponent} of the given {@link V}. */
	@Nullable
	static <V> NetworkComponent from(V v) {
		return fromPost(v);
	}
	
	@ExpectPlatform
	static <V> NetworkComponent fromPost(V v) {
		throw new AssertionError();
	}
	
	/** Instantiates a {@link NetworkComponent}. */
	static NetworkComponent of(World world) {
		return new NetworkComponentImpl(world);
	}
	
	/** Returns this component's world. */
	public World getWorld();
	
	/** Returns this component's instances. */
	Set<NetworkInstance> getInstances();
	
	/** Adds the given {@link NetworkInstance} to this component. */
	default void add(NetworkInstance instance) {
		if (!instance.nodes.isEmpty())
			getInstances().add(instance);
	}
	
	/** Removes the given {@link NetworkInstance} from this component. */
	default void remove(NetworkInstance instance) {
		getInstances().remove(instance);
	}
	
	/** Returns the {@link NetworkInstance} of the given {@link NetworkType}
	 * at the specified {@link BlockPos}. */
	default NetworkInstance get(NetworkType type, BlockPos pos) {
		return getInstances().stream().filter(instance -> instance.getType() == type && instance.nodes.stream().anyMatch(node -> node.getBlockPosition().equals(pos))).findFirst().orElse(NetworkInstance.EMPTY);
	}
	
	/** Asserts whether any {@link NetworkInstance} exists for the given {@link NetworkType}
	 * at the specified {@link BlockPos}. */
	default boolean contains(NetworkType type, BlockPos pos) {
		return get(type, pos) != NetworkInstance.EMPTY;
	}
	
	/** Override behavior to implement network ticking logic. */
	@Override
	default void serverTick() {
		getInstances().removeIf(NetworkInstance::isEmpty);
		getInstances().forEach(NetworkInstance::tick);
	}
	
	/** Serializes this {@link NetworkComponentImpl} to a {@link CompoundTag}. */
	@Override
	default void toTag(CompoundTag tag) {
		var tags = new ListTag();
		
		for (var instance : getInstances()) {
			var nodes = new ListTag();
			
			for (var node : instance.nodes) {
				nodes.add(LongTag.of(node.getLongPosition()));
			}
			
			var members = new ListTag();
			
			for (var member : instance.members) {
				members.add(member.toTag());
			}
			
			var data = new CompoundTag();
			
			data.putString("Type", NetworkTypeRegistry.INSTANCE.getKey(instance.getType()).toString());
			data.put("Nodes", nodes);
			data.put("Members", members);
			
			tags.add(data);
		}
		
		tag.put("Tags", tags);
	}
	
	/** Deserializes this {@link NetworkComponentImpl} from a {@link CompoundTag}. */
	@Override
	default void fromTag(CompoundTag tag) {
		var tags = tag.getList("Tags", NbtType.COMPOUND);
		
		for (var instanceTag : tags) {
			var data = (CompoundTag) instanceTag;
			var nodes = data.getList("Nodes", NbtType.LONG);
			var members = data.getList("Members", NbtType.COMPOUND);
			
			var type = NetworkTypeRegistry.INSTANCE.get(new Identifier(data.getString("Type")));
			var instance = new NetworkInstance(getWorld(), type);
			
			for (var nodeKey : nodes) {
				instance.addNode(NetworkNode.of(((LongTag) nodeKey).getLong()));
			}
			
			for (var memberTag : members) {
				instance.addMember(NetworkMemberNode.fromTag((CompoundTag) memberTag));
			}
			
			add(instance);
		}
	}
	
	/** Returns this component's {@link Identifier}. */
	@Override
	default Identifier getId() {
		return AMComponents.NETWORK;
	}
}
