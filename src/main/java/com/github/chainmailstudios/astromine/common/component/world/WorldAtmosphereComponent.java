package com.github.chainmailstudios.astromine.common.component.world;

import net.minecraft.block.AirBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineDimensionTypes;
import nerdhub.cardinal.components.api.component.Component;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldAtmosphereComponent implements Component, Tickable {
	private static final Fraction TRESHHOLD = new Fraction(2, 1);

	private final Map<BlockPos, FluidVolume> volumes = new ConcurrentHashMap<>();

	private final World world;

	public WorldAtmosphereComponent(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public void add(BlockPos blockPos, FluidVolume volume) {
		volumes.put(blockPos, volume);
	}

	public void remove(BlockPos blockPos) {
		volumes.remove(blockPos);
	}

	public FluidVolume get(BlockPos position) {
		RegistryKey<DimensionType> key = world.getDimensionRegistryKey();

		boolean isSpace = (key == AstromineDimensionTypes.SPACE_REGISTRY_KEY);

		if (!isSpace && !volumes.containsKey(position)) {
			return FluidVolume.oxygen();
		} else {
			return volumes.getOrDefault(position, FluidVolume.empty());
		}
	}

	@Override
	public void tick() {
		List<Direction> directions = Lists.newArrayList(Direction.values());

		for (Map.Entry<BlockPos, FluidVolume> pair : volumes.entrySet()) {
			final FluidVolume fluidVolume = get(pair.getKey());

			Collections.shuffle(directions);

			for (Direction direction : directions) {
				final BlockPos offsetPosition = pair.getKey().offset(direction);

				if (world.getBlockState(offsetPosition).getBlock() instanceof AirBlock) {
					FluidVolume offsetFluidVolume = get(offsetPosition);

					if (!fluidVolume.isEmpty() && fluidVolume.getFraction().isBiggerThan(Fraction.max(TRESHHOLD, offsetFluidVolume.getFraction())) && offsetFluidVolume.canInsert(fluidVolume.getFluid(), Fraction.BUCKET)) {
						fluidVolume.pushVolume(offsetFluidVolume, Fraction.BUCKET);
						add(offsetPosition, offsetFluidVolume);
					} else if (!fluidVolume.isEmpty() && fluidVolume.getFraction().isBiggerThan(Fraction.max(TRESHHOLD, offsetFluidVolume.getFraction())) && offsetFluidVolume.equals(FluidVolume.oxygen())) {
						FluidVolume newVolume = new FluidVolume();
						fluidVolume.pushVolume(newVolume, Fraction.BUCKET);
						add(offsetPosition, newVolume);
					}
				} else {
					remove(offsetPosition);
				}
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		for (Map.Entry<BlockPos, FluidVolume> entry : volumes.entrySet()) {
			CompoundTag pointTag = new CompoundTag();
			pointTag.putLong("pos", entry.getKey().asLong());
			pointTag.put("volume", entry.getValue().toTag(new CompoundTag()));

			dataTag.put("0", pointTag);
		}

		tag.put("data", dataTag);

		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			CompoundTag pointTag = dataTag.getCompound(key);

			volumes.put(BlockPos.fromLong(pointTag.getLong("pos")), FluidVolume.fromTag(pointTag.getCompound("volume")));
		}
	}
}
