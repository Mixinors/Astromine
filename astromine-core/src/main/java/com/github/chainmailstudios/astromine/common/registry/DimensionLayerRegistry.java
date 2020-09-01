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

package com.github.chainmailstudios.astromine.common.registry;

import net.minecraft.util.Pair;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.entity.placer.EntityPlacer;

import java.util.HashMap;
import java.util.Map;

public class DimensionLayerRegistry {
	public static final DimensionLayerRegistry INSTANCE = new DimensionLayerRegistry();

	private final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> TOP_ENTRIES = new HashMap<>();
	private final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> BOTTOM_ENTRIES = new HashMap<>();
	private final Map<RegistryKey<World>, Pair<EntityPlacer, EntityPlacer>> PLACERS = new HashMap<>();

	private DimensionLayerRegistry() {

	}

	public void register(Type type, RegistryKey<World> dimension, Integer levelY, RegistryKey<World> newDimension, EntityPlacer placer) {
		final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		ENTRIES.put(dimension, new Pair<>(levelY, newDimension));

		if (PLACERS.containsKey(dimension)) {
			PLACERS.put(dimension, new Pair<>(type == Type.TOP ? placer : PLACERS.get(dimension).getLeft(), type == Type.BOTTOM ? placer : PLACERS.get(dimension).getRight()));
		} else {
			PLACERS.put(dimension, new Pair<>(type == Type.TOP ? placer : null, type == Type.BOTTOM ? placer : null));
		}
	}

	public int getLevel(Type type, RegistryKey<World> dimension) {
		final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		final Pair<Integer, RegistryKey<World>> pair = ENTRIES.get(dimension);

		return pair == null ? Integer.MIN_VALUE : pair.getLeft();
	}

	public RegistryKey<World> getDimension(Type type, RegistryKey<World> dimension) {
		final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		final Pair<Integer, RegistryKey<World>> pair = ENTRIES.get(dimension);

		return pair == null ? null : pair.getRight();
	}

	public EntityPlacer getPlacer(Type type, RegistryKey<World> dimension) {
		return type == Type.TOP ? PLACERS.get(dimension).getLeft() : PLACERS.get(dimension).getRight();
	}

	public enum Type {
		TOP,
		BOTTOM
	}
}
