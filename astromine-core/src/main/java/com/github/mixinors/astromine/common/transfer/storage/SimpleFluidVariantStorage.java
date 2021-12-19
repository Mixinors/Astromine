package com.github.mixinors.astromine.common.transfer.storage;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class SimpleFluidVariantStorage extends SingleVariantStorage<FluidVariant> {
	private long capacity = 81000L;
	
	public SimpleFluidVariantStorage() {
	}
	
	@Override
	public long getCapacity() {
		return capacity;
	}
	
	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}
	
	@Override
	protected FluidVariant getBlankVariant() {
		return FluidVariant.blank();
	}
	
	@Override
	protected long getCapacity(FluidVariant variant) {
		return capacity;
	}
}
