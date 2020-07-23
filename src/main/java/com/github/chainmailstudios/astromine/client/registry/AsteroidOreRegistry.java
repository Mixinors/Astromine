/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
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
package com.github.chainmailstudios.astromine.client.registry;

import com.github.chainmailstudios.astromine.common.registry.base.MultiRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;
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
