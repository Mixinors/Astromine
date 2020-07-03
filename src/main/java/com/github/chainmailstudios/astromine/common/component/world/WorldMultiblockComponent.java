package com.github.chainmailstudios.astromine.common.component.world;

import com.github.chainmailstudios.astromine.common.multiblock.MultiblockInstance;
import com.github.chainmailstudios.astromine.common.multiblock.MultiblockNode;
import com.github.chainmailstudios.astromine.common.multiblock.MultiblockTypeRegistry;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Set;

public class WorldMultiblockComponent implements Component {
	private final World world;

	public WorldMultiblockComponent(World world) {
		this.world = world;
	}

	private final Set<MultiblockInstance<?>> instances = Sets.newConcurrentHashSet();

	public boolean contains(MultiblockNode node) {
		return instances.stream().anyMatch(instance -> instance.contains(node));
	}

	public MultiblockInstance<?> get(MultiblockNode node) {
		return instances.stream().filter(instance -> instance.contains(node)).findFirst().orElse(null);
	}

	public void add(MultiblockInstance<?> instance) {
		instances.add(instance);
	}

	public void remove(MultiblockInstance<?> instance) {
		instances.remove(instance);
	}

	@Override
	public void fromTag(CompoundTag compoundTag) {
		CompoundTag dataTag = compoundTag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			CompoundTag instanceTag = dataTag.getCompound(key);

			MultiblockInstance<?> instance = new MultiblockInstance<>(MultiblockTypeRegistry.INSTANCE.get(new Identifier(instanceTag.getString("type"))));
			instance.fromTag(instanceTag);
			instances.add(instance);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		CompoundTag dataTag = new CompoundTag();

		int i = 0;

		for (MultiblockInstance<?> instance : instances) {
			dataTag.put(String.valueOf(i), instance.toTag(new CompoundTag()));
		}

		compoundTag.put("data", dataTag);

		return compoundTag;
	}
}
