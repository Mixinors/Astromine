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

import net.minecraft.block.Block;

import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class AsteroidOreRegistry {
	public static final AsteroidOreRegistry INSTANCE = new AsteroidOreRegistry();

	private final Map<Block, @Nullable Range<Integer>> diameters = new HashMap<>();

	private AsteroidOreRegistry() {
		// Locked.
	}

	public void register(Range<Integer> range, Block block) {
		if (range.getMinimum() > range.getMaximum()) {
			range = Range.of(range.getMaximum(), range.getMinimum());
		} else if (range.getMinimum().equals(range.getMaximum())) {
			range = null;
		}

		if (range == null) {
			diameters.remove(block);
		} else {
			diameters.put(block, range);
		}
	}

	public Set<Block> keySet() {
		return diameters.keySet();
	}

	public int getDiameter(Random random, Block block) {
		Range<Integer> range = diameters.get(block);
		if (range == null)
			return 0;
		return (int) ((range.getMaximum() - range.getMinimum()) * Objects.requireNonNull(random, "random").nextFloat() + range.getMinimum());
	}
}
