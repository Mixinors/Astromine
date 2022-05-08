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

package com.github.mixinors.astromine.common.block.entity.utility;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.provider.FluidStorageSizeProvider;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public class DrainBlockEntity extends ExtendedBlockEntity implements FluidStorageSizeProvider {
	private static final int INPUT_SLOT = 0;
	
	private static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	private static final int[] EXTRACT_SLOTS = new int[] { };
	
	public DrainBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.DRAIN, blockPos, blockState);
		
		fluidStorage = new SimpleFluidStorage(1, getFluidStorageSize()).insertPredicate((variant, slot) ->
				shouldRun()
		).extractPredicate((variant, slot) ->
				false
		).listener(() -> {
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
		
		fluidStorage.getStorage(INPUT_SLOT).setCapacity(getFluidStorageSize());
		
		Arrays.fill(fluidStorage.getSidings(), StorageSiding.INSERT);
	}
	
	@Override
	public void tick() {
		if (world == null) {
			return;
		}
		
		var inputStorage = fluidStorage.getStorage(INPUT_SLOT);
		
		if (!inputStorage.isResourceBlank()) {
			try (var transaction = Transaction.openOuter()) {
				inputStorage.extract(inputStorage.getResource(), Long.MAX_VALUE, transaction);
				
				transaction.commit();
			}
		}
	}
	
	@Override
	public long getFluidStorageSize() {
		return Long.MAX_VALUE;
	}
}
