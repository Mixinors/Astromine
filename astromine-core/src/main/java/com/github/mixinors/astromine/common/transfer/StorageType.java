package com.github.mixinors.astromine.common.transfer;

import com.github.mixinors.astromine.registry.common.AMItems;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public enum StorageType {
	ITEM,
	FLUID,
	ENERGY;
	
	public Item getItem() {
		return switch (this) {
			case ITEM -> AMItems.ITEM.get();
			case FLUID -> AMItems.FLUID.get();
			case ENERGY -> AMItems.ENERGY.get();
		};
	}
	
	public Text getName() {
		return switch (this) {
			case ITEM -> new TranslatableText("text.astromine.item");
			case FLUID -> new TranslatableText("text.astromine.fluid");
			case ENERGY -> new TranslatableText("text.astromine.energy");
		};
	}
}
