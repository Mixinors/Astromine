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

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;

public class DimensionLayerRegistry {
	public static final DimensionLayerRegistry INSTANCE = new DimensionLayerRegistry();
	private final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> TOP_ENTRIES = new HashMap<>();
	private final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> BOTTOM_ENTRIES = new HashMap<>();
	private final Map<RegistryKey<World>, Pair<EntityPlacer, EntityPlacer>> PLACERS = new HashMap<>();

	private DimensionLayerRegistry() {
		// Unused.
	}

	public void register(Type type, RegistryKey<World> world, Integer levelY, RegistryKey<World> newWorld, EntityPlacer placer) {
		final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		ENTRIES.put(world, new Pair<>(levelY, newWorld));

		if (PLACERS.containsValue(world)) {
			PLACERS.put(world, new Pair<>(type == Type.TOP ? placer : PLACERS.get(world).getLeft(), type == Type.BOTTOM ? placer : PLACERS.get(world).getRight()));
		} else {
			PLACERS.put(world, new Pair<>(type == Type.TOP ? placer : null, type == Type.BOTTOM ? placer : null));
		}
	}

	public int getLevel(Type type, RegistryKey<World> world) {
		final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		final Pair<Integer, RegistryKey<World>> pair = ENTRIES.get(world);

		return pair == null ? Integer.MIN_VALUE : pair.getLeft();
	}

	public RegistryKey<World> getDimension(Type type, RegistryKey<World> world) {
		final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		final Pair<Integer, RegistryKey<World>> pair = ENTRIES.get(world);

		return pair == null ? null : pair.getRight();
	}

	public EntityPlacer getPlacer(Type type, RegistryKey<World> world) {
		return type == Type.TOP ? PLACERS.get(world).getLeft() : PLACERS.get(world).getRight();
	}

	public enum Type {
		TOP,
		BOTTOM
	}
}
