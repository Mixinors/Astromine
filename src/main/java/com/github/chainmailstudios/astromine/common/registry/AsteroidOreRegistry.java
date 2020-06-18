package com.github.chainmailstudios.astromine.common.registry;

import net.minecraft.block.Block;

import com.github.chainmailstudios.astromine.common.utilities.data.Range;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import java.util.List;

public class AsteroidOreRegistry {
	public static final AsteroidOreRegistry INSTANCE = new AsteroidOreRegistry();
	private final HashMultimap<Integer, Block> ENTRIES = HashMultimap.create();

	private AsteroidOreRegistry() {
		// Unused.
	}

	public List<Block> get(Integer chance) {
		return Lists.newArrayList(this.ENTRIES.get(chance));
	}

	public void register(Range<Integer> range, Block block) {
		if (range.getMinimum() > range.getMaximum()) {
			range = Range.of(range.getMaximum(), range.getMinimum());
		}

		for (int chance = range.getMinimum(); chance < range.getMaximum(); ++chance) {
			this.register(chance, block);
		}
	}

	public void register(Integer chance, Block block) {
		this.ENTRIES.put(chance, block);
	}

	public void unregister(Integer chance, Block block) {
		this.ENTRIES.remove(chance, block);
	}
}
