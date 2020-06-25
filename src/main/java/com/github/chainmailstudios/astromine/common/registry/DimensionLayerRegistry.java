package com.github.chainmailstudios.astromine.common.registry;

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;

import net.minecraft.util.Pair;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;

public class DimensionLayerRegistry {
	public static final DimensionLayerRegistry INSTANCE = new DimensionLayerRegistry();
	private final Map<RegistryKey<DimensionType>, Pair<Integer, RegistryKey<DimensionType>>> TOP_ENTRIES = new HashMap<>();
	private final Map<RegistryKey<DimensionType>, Pair<Integer, RegistryKey<DimensionType>>> BOTTOM_ENTRIES = new HashMap<>();
	private final Map<RegistryKey<DimensionType>, Pair<EntityPlacer, EntityPlacer>> PLACERS = new HashMap<>();

	private DimensionLayerRegistry() {
		// Unused.
	}

	public void register(Type type, RegistryKey<DimensionType> dimension, Integer levelY, RegistryKey<DimensionType> newDimension, EntityPlacer placer) {
		final Map<RegistryKey<DimensionType>, Pair<Integer, RegistryKey<DimensionType>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		ENTRIES.put(dimension, new Pair<>(levelY, newDimension));

		if (PLACERS.containsValue(dimension)) {
			PLACERS.put(dimension, new Pair<>(type == Type.TOP ? placer : PLACERS.get(dimension).getLeft(), type == Type.BOTTOM ? placer : PLACERS.get(dimension).getRight()));
		} else {
			PLACERS.put(dimension, new Pair<>(type == Type.TOP ? placer : null, type == Type.BOTTOM ? placer : null));
		}
	}

	public int getLevel(Type type, RegistryKey<DimensionType> dimension) {
		final Map<RegistryKey<DimensionType>, Pair<Integer, RegistryKey<DimensionType>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		final Pair<Integer, RegistryKey<DimensionType>> pair = ENTRIES.get(dimension);

		return pair == null ? Integer.MIN_VALUE : pair.getLeft();
	}

	public RegistryKey<DimensionType> getDimension(Type type, RegistryKey<DimensionType> dimension) {
		final Map<RegistryKey<DimensionType>, Pair<Integer, RegistryKey<DimensionType>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		final Pair<Integer, RegistryKey<DimensionType>> pair = ENTRIES.get(dimension);

		return pair == null ? null : pair.getRight();
	}

	public EntityPlacer getPlacer(Type type, RegistryKey<DimensionType> dimensin) {
		return type == Type.TOP ? PLACERS.get(dimensin).getLeft() : PLACERS.get(dimensin).getRight();
	}

	public enum Type {
		TOP, BOTTOM
	}
}
