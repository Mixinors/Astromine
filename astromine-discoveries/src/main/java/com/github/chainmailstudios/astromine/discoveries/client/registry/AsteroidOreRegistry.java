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

package com.github.chainmailstudios.astromine.discoveries.client.registry;

import net.minecraft.block.Block;
import net.minecraft.util.Pair;

import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class AsteroidOreRegistry {
	public static final AsteroidOreRegistry INSTANCE = new AsteroidOreRegistry();

	public final Reference2ReferenceMap<Block, @Nullable Pair<Range<Integer>, Range<Integer>>> diameters = new Reference2ReferenceOpenHashMap<>();

	private AsteroidOreRegistry() {
		// Locked.
	}

	public void register(Range<Integer> weightRange, Range<Integer> sizeRange, Block block) {
		if (weightRange.getMinimum() > weightRange.getMaximum()) {
			weightRange = Range.of(weightRange.getMaximum(), weightRange.getMinimum());
		} else if (weightRange.getMinimum().equals(weightRange.getMaximum())) {
			weightRange = null;
		}
		if (sizeRange.getMinimum() > sizeRange.getMaximum()) {
			sizeRange = Range.of(sizeRange.getMaximum(), sizeRange.getMinimum());
		} else if (sizeRange.getMinimum().equals(sizeRange.getMaximum())) {
			sizeRange = null;
		}

		if (weightRange == null || sizeRange == null) {
			diameters.remove(block);
		} else {
			diameters.put(block, new Pair<>(weightRange, sizeRange));
		}
	}

	public Set<Block> keySet() {
		return diameters.keySet();
	}

	public int getDiameter(Random random, Block block) {
		@Nullable
		Pair<Range<Integer>, Range<Integer>> pair = diameters.get(block);
		if (pair == null)
			return 0;
		return (int) (((pair.getRight().getMaximum() - pair.getRight().getMinimum()) * Objects.requireNonNull(random, "random").nextFloat() + pair.getRight().getMinimum()) * 0.9);
	}
}
