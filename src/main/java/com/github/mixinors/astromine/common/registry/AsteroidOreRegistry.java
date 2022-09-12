/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.registry;

import com.github.mixinors.astromine.common.util.data.range.Range;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public class AsteroidOreRegistry {
	public static final AsteroidOreRegistry INSTANCE = new AsteroidOreRegistry();
	
	public final Reference2ReferenceMap<Block, @Nullable Pair<Range<Integer>, Range<Integer>>> diameters = new Reference2ReferenceOpenHashMap<>();
	
	private AsteroidOreRegistry() {
		// Locked.
	}
	
	public void register(Range<Integer> weightRange, Range<Integer> sizeRange, Block block) {
		if (weightRange.minimum() > weightRange.maximum()) {
			weightRange = new Range(weightRange.maximum(), weightRange.minimum());
		} else if (weightRange.minimum().equals(weightRange.maximum())) {
			weightRange = null;
		}
		if (sizeRange.minimum() > sizeRange.maximum()) {
			sizeRange = new Range(sizeRange.maximum(), sizeRange.minimum());
		} else if (sizeRange.minimum().equals(sizeRange.maximum())) {
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
		var pair = diameters.get(block);
		
		if (pair == null) {
			return 0;
		}
		
		return (int) (((pair.getRight().maximum() - pair.getRight().minimum()) * Objects.requireNonNull(random, "random").nextFloat() + pair.getRight().minimum()) * 0.9);
	}
}
