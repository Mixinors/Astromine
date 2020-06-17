package com.github.chainmailstudios.astromine.common.volume.collection;

import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

public interface AgnosticSidedVolumeCollection {
	boolean contains(Direction direction, int volumeType);

	<U extends BaseVolume, T extends IndexedVolumeCollection<U>> T get(Direction direction, int volumeType);
}
