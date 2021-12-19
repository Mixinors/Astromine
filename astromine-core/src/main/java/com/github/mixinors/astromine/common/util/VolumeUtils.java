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

package com.github.mixinors.astromine.common.util;

import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import net.minecraft.util.Pair;

public class VolumeUtils {
	/** Attempts to merge two {@link FluidVolume}s, returning a {@link Pair}
	 * with the results.
	 *
	 * The amount transferred is the {@link Math#min(long, long)}  between
	 * their available space, our amount, and the specified amount.
	 * */
	public static Pair<FluidVolume, FluidVolume> merge(FluidVolume source, FluidVolume target) {
		long targetMax = target.getSize();

		if (source.test(target)) {
			long sourceCount = source.getAmount();
			long targetCount = target.getAmount();

			long targetAvailable = Math.max(0, targetMax - targetCount);

			target.take(source, Math.min(sourceCount, targetAvailable));
		}

		return new Pair<>(source, target);
	}

	/** Inserts fluids from the first stack into the first fluid volume.
		* Inserts fluids from the first fluid volume into the first stack. */
	public static void transferBetween(ItemStore itemStorage, FluidStore fluidStorage, int firstStackSlot, int secondStackSlot, int volumeSlot) {
		if (fluidStorage != null) {
			if (itemStorage != null) {
				FluidStore firstStackFluidComponent = FluidStore.get(itemStorage.getStack(firstStackSlot));

				if (firstStackFluidComponent != null) {
					FluidVolume ourVolume = fluidStorage.getVolume(volumeSlot);

					firstStackFluidComponent.forEach(stackVolume -> {if (ourVolume.test(stackVolume.getFluid())) {
						if (itemStorage.getStack(firstStackSlot).getItem() instanceof BucketItem) {
							if (itemStorage.getStack(firstStackSlot).getItem() != Items.BUCKET && itemStorage.getStack(firstStackSlot).getCount() == 1) {
								if (ourVolume.hasAvailable(FluidVolume.BUCKET) || ourVolume.isEmpty()) {
									ourVolume.take(stackVolume, FluidVolume.BUCKET);

                                        itemStorage.setStack(firstStackSlot, new ItemStack(Items.BUCKET));
                                    }
                                }
                            } else {
                                ourVolume.take(stackVolume, FluidVolume.BUCKET);
                            }
                        }
                    });
                }

				FluidStore secondStackFluidComponent = FluidStore.get(itemStorage.getStack(secondStackSlot));

				if (secondStackFluidComponent != null) {
					FluidVolume ourVolume = fluidStorage.getVolume(volumeSlot);

					secondStackFluidComponent.forEach(stackVolume -> {if (stackVolume.test(ourVolume.getFluid())) {
						if (itemStorage.getStack(secondStackSlot).getItem() instanceof BucketItem) {
							if (itemStorage.getStack(secondStackSlot).getItem() == Items.BUCKET && itemStorage.getStack(secondStackSlot).getCount() == 1) {
								if (ourVolume.hasStored(FluidVolume.BUCKET)) {
									ourVolume.give(stackVolume, FluidVolume.BUCKET);

                                        itemStorage.setStack(secondStackSlot, new ItemStack(stackVolume.getFluid().getBucketItem()));
                                    }
                                }
                            } else {
                                ourVolume.give(stackVolume, FluidVolume.BUCKET);
                            }
                        }
                    });
                }
            }
        }
    }
}
