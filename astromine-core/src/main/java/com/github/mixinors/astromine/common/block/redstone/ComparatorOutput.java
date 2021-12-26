/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.block.redstone;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ComparatorBlockEntity;

import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;

/**
 * A handler of {@link ComparatorBlockEntity}
 * output levels.
 */
public class ComparatorOutput {
	public static int forItems(BlockEntity blockEntity) {
		if(blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
			return StorageUtil.calculateComparatorOutput(extendedBlockEntity.getItemStorage(), null);
		}
		return 0;
	}

	public static int forFluids(BlockEntity blockEntity) {
		if(blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
			return StorageUtil.calculateComparatorOutput(extendedBlockEntity.getFluidStorage(), null);
		}
		return 0;
	}

	public static int forEnergy(BlockEntity blockEntity) {
		if(blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
			return forEnergy(extendedBlockEntity.getEnergyStorage());
		}
		return 0;
	}

	/**
	 * Returns the output level for an {@link EnergyStorage}.
	 */
	public static int forEnergy(@Nullable EnergyStorage storage) {
		if (storage == null) {
			return 0;
		}

		if (storage.getAmount() <= 0.0001) {
			return 0;
		}

		return 1 + (int) (storage.getAmount() / storage.getCapacity() * 14.0);
	}
}