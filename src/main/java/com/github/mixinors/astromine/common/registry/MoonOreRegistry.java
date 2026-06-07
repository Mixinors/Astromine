package com.github.mixinors.astromine.common.registry;

import com.github.mixinors.astromine.common.util.WeightedList;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;

public class MoonOreRegistry {
	public static final MoonOreRegistry INSTANCE = new MoonOreRegistry();
	
	private final WeightedList<Block> entries = new WeightedList<>();
	
	private MoonOreRegistry() {
		// Locked.
	}
	
	public void register(Block block, int weight) {
		entries.add(block, weight);
	}
	
	public Block getRandom(RandomSource random) {
		return entries.shuffle(random).stream().findFirst().orElseGet(AMBlocks.MOON_STONE);
	}
}
