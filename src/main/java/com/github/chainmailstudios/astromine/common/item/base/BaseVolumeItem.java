package com.github.chainmailstudios.astromine.common.item.base;

import net.minecraft.item.Item;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;

public class BaseVolumeItem extends Item {
	protected Fraction size;

	public BaseVolumeItem(Settings settings) {
		super(settings);
	}

	public Fraction getSize() {
		return size;
	}
}
