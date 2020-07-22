package com.github.chainmailstudios.astromine.client.registry;

import com.github.chainmailstudios.astromine.common.registry.base.MultiRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AsteroidOreRegistry extends MultiRegistry<Integer, Block> {
	public static final AsteroidOreRegistry INSTANCE = new AsteroidOreRegistry();

	public final Map<Block, Integer> minimumDiameters = new HashMap<>();
	public final Map<Block, Integer> maximumDiameters = new HashMap();

	private AsteroidOreRegistry() {
		// Locked.
	}

	public void register(Range<Integer> range, int minimumDiameter, int maximumDiameter, Block block) {
		if (range.getMinimum() > range.getMaximum()) {
			range = Range.of(range.getMaximum(), range.getMinimum());
		}

		for (int chance = range.getMinimum(); chance < range.getMaximum(); ++chance) {
			this.register(chance, block);
		}
	}

	public int getDiameter(Random random, Block block) {
		return (int) MathHelper.clamp(maximumDiameters.get(block) * random.nextFloat(), minimumDiameters.get(block), maximumDiameters.get(block));
	}
}
