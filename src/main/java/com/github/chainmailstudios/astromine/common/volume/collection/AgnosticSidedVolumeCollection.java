package com.github.chainmailstudios.astromine.common.volume.collection;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;

import net.minecraft.util.math.Direction;

public interface AgnosticSidedVolumeCollection {
	boolean contains(Direction direction, int volumeType);

	<U extends BaseVolume, T extends IndexedVolumeCollection<U>> T get(Direction direction, int volumeType);
}
