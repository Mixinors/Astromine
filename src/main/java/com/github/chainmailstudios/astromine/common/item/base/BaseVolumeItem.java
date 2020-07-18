package com.github.chainmailstudios.astromine.common.item.base;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import net.minecraft.item.Item;

public class BaseVolumeItem extends Item {
	protected Fraction size;

	public BaseVolumeItem(Settings settings) {
		super(settings);
	}

	public Fraction getSize() {
		return size;
	}
}
