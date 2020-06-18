package com.github.chainmailstudios.astromine.common.volume.collection;

import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

public interface AgnosticSidedVolumeCollection {
	boolean contains(Direction direction, int volumeType);

	<T extends BaseVolume> T get(Direction direction, int volumeType);
}
