/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.entity.rocket;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RocketEntity extends com.github.mixinors.astromine.common.entity.rocket.base.RocketEntity {
	public RocketEntity(EntityType<?> type, World world) {
		super(type, world);
		
		fluidStorage.getStorage(FLUID_INPUT_SLOT_1).setCapacity(FluidConstants.BUCKET * 16);
		fluidStorage.getStorage(FLUID_INPUT_SLOT_2).setCapacity(FluidConstants.BUCKET * 16);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (!world.isClient) {
			//if (world.getRegistryKey().equals(AMWorlds.EARTH_ORBIT_WORLD)) {
			//	setVelocity(0.0F, 0.0F, 0.0F);
			//
			//	dataTracker.set(RUNNING, false);
			//}
			
			try (var transaction = Transaction.openOuter()) {
				var wildItemStorage = itemStorage.getWildProxy();
				var wildFluidStorage = fluidStorage.getWildProxy();
				
				var itemInputStorage1 = wildItemStorage.getStorage(ITEM_INPUT_SLOT_1);
				var itemInputStorage2 = wildItemStorage.getStorage(ITEM_INPUT_SLOT_2);
				
				var itemBufferStorage = wildItemStorage.getStorage(ITEM_BUFFER_SLOT_1);
				
				var itemOutputStorage1 = wildItemStorage.getStorage(ITEM_OUTPUT_SLOT_1);
				var itemOutputStorage2 = wildItemStorage.getStorage(ITEM_OUTPUT_SLOT_2);
				
				var fluidInputStorage1 = wildFluidStorage.getStorage(FLUID_INPUT_SLOT_1);
				var fluidInputStorage2 = wildFluidStorage.getStorage(FLUID_INPUT_SLOT_2);
				
				var itemInputFluidStorage1 = FluidStorage.ITEM.find(itemInputStorage1.getStack(), ContainerItemContext.ofSingleSlot(itemInputStorage1));
				var itemInputFluidStorage2 = FluidStorage.ITEM.find(itemInputStorage2.getStack(), ContainerItemContext.ofSingleSlot(itemInputStorage2));
				
				var itemOutputFluidStorage1 = FluidStorage.ITEM.find(itemOutputStorage1.getStack(), ContainerItemContext.ofSingleSlot(itemOutputStorage1));
				var itemOutputFluidStorage2 = FluidStorage.ITEM.find(itemOutputStorage2.getStack(), ContainerItemContext.ofSingleSlot(itemOutputStorage2));
				
				//..StorageUtil.move(itemInputFluidStorage1, fluidInputStorage1, fluidVariant -> {
				//	..	return getFirstFuel().testVariant(fluidVariant);
				//	..}, FluidConstants.BUCKET, transaction);
				//..StorageUtil.move(itemInputFluidStorage2, fluidInputStorage2, fluidVariant -> {
				//	..	return getSecondFuel().testVariant(fluidVariant);
				//	..}, FluidConstants.BUCKET, transaction);
				
				StorageUtil.move(fluidInputStorage1, itemOutputFluidStorage1, fluidVariant -> true, FluidConstants.BUCKET, transaction);
				StorageUtil.move(fluidInputStorage2, itemOutputFluidStorage2, fluidVariant -> true, FluidConstants.BUCKET, transaction);
				
				StorageUtil.move(itemInputStorage1, itemBufferStorage, (variant) -> {
					var stored = StorageUtil.findStoredResource(itemInputFluidStorage1, transaction);
					return stored == null || stored.isBlank();
				}, 1, transaction);
				
				StorageUtil.move(itemInputStorage2, itemBufferStorage, (variant) -> {
					var stored = StorageUtil.findStoredResource(itemInputFluidStorage2, transaction);
					return stored == null || stored.isBlank();
				}, 1, transaction);
				
				transaction.commit();
			}
		}
	}
}
