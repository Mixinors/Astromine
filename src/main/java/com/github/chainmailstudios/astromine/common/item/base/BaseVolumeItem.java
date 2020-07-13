package com.github.chainmailstudios.astromine.common.item.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class BaseVolumeItem extends Item {
	protected Fraction size;

	public BaseVolumeItem(Settings settings) {
		super(settings);
	}

	public Fraction getSize() {
		return size;
	}
}
