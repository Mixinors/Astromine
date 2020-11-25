/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.minecraft.util.Pair;

public class VolumeUtilities {
	/** Attempts to merge two {@link FluidVolume}s, returning a {@link Pair}
	 * with the results.
	 *
	 * The amount transferred is the {@link Fraction#minimum(Fraction, Fraction)} between
	 * their available space, our amount, and the specified amount.
	 * */
	public static Pair<FluidVolume, FluidVolume> merge(FluidVolume source, FluidVolume target) {
		Fraction targetMax = target.getSize();

		if (source.test(target)) {
			Fraction sourceCount = source.getAmount();
			Fraction targetCount = target.getAmount();

			Fraction targetAvailable = Fraction.maximum(Fraction.EMPTY, targetMax.subtract(targetCount));

			target.take(source, Fraction.minimum(sourceCount, targetAvailable));
		}

		return new Pair<>(source, target);
	}

	/** Inserts fluids from the first stack into the first fluid volume.
		* Inserts fluids from the first fluid volume into the first stack. */
	public static void transferBetween(ItemComponent itemComponent, FluidComponent fluidComponent, int firstStackSlot, int secondStackSlot, int volumeSlot) {
		if (fluidComponent != null) {
			if (itemComponent != null) {
				FluidComponent firstStackFluidComponent = FluidComponent.get(itemComponent.getStack(firstStackSlot));

				if (firstStackFluidComponent != null) {
					FluidVolume ourVolume = fluidComponent.getVolume(volumeSlot);

					firstStackFluidComponent.forEach(stackVolume -> {if (ourVolume.test(stackVolume.getFluid())) {
						if (itemComponent.getStack(firstStackSlot).getItem() instanceof BucketItem) {
							if (itemComponent.getStack(firstStackSlot).getItem() != Items.BUCKET && itemComponent.getStack(firstStackSlot).getCount() == 1) {
								if (ourVolume.hasAvailable(Fraction.BUCKET) || ourVolume.isEmpty()) {
									ourVolume.take(stackVolume, Fraction.BUCKET);

                                        itemComponent.setStack(firstStackSlot, new ItemStack(Items.BUCKET));
                                    }
                                }
                            } else {
                                ourVolume.take(stackVolume, Fraction.BUCKET);
                            }
                        }
                    });
                }

				FluidComponent secondStackFluidComponent = FluidComponent.get(itemComponent.getStack(secondStackSlot));

				if (secondStackFluidComponent != null) {
					FluidVolume ourVolume = fluidComponent.getVolume(volumeSlot);

					secondStackFluidComponent.forEach(stackVolume -> {if (stackVolume.test(ourVolume.getFluid())) {
						if (itemComponent.getStack(secondStackSlot).getItem() instanceof BucketItem) {
							if (itemComponent.getStack(secondStackSlot).getItem() == Items.BUCKET && itemComponent.getStack(secondStackSlot).getCount() == 1) {
								if (ourVolume.hasStored(Fraction.BUCKET)) {
									ourVolume.give(stackVolume, Fraction.BUCKET);

                                        itemComponent.setStack(secondStackSlot, new ItemStack(stackVolume.getFluid().getBucketItem()));
                                    }
                                }
                            } else {
                                ourVolume.give(stackVolume, Fraction.BUCKET);
                            }
                        }
                    });
                }
            }
        }
    }
}
