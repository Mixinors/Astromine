package com.github.chainmailstudios.astromine.common.component.world;

import com.github.chainmailstudios.astromine.common.multiblock.MultiblockInstance;
import com.github.chainmailstudios.astromine.common.multiblock.MultiblockNode;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.nbt.CompoundTag;

import java.util.Map;
import java.util.Set;

public class WorldMultiblockComponent implements Component {
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
		CompoundTag dataTag = new CompoundTag();

		instances.forEach(instance -> {
			dataTag.put()
		})
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		return null;
	}
}
