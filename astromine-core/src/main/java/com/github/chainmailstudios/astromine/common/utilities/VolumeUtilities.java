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
import net.minecraft.network.PacketByteBuf;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;

import com.google.gson.JsonElement;

public class VolumeUtilities {
	public static FluidAmount transferFluidAmount() {
		return FluidAmount.of(AstromineConfig.get().fluidTransferNumerator, AstromineConfig.get().fluidTransferDenominator);
	}

	public static FluidVolume fromFluidVolumeJson(JsonElement jsonElement) {
		return FluidVolume.fromJson(jsonElement);
	}

	public static FluidVolume fromFluidVolumePacket(PacketByteBuf buffer) {
		return FluidVolume.fromPacket(buffer);
	}

	public static void toFluidVolumePacket(PacketByteBuf buffer, FluidVolume volume) {
		volume.toPacket(buffer);
	}

	public static EnergyVolume fromEnergyVolumeJson(JsonElement jsonElement) {
		return EnergyVolume.fromJson(jsonElement);
	}

	public static EnergyVolume fromEnergyVolumePacket(PacketByteBuf buffer) {
		return EnergyVolume.fromPacket(buffer);
	}

	public static void toEnergyVolumePacket(PacketByteBuf buffer, EnergyVolume volume) {
		volume.toPacket(buffer);
	}

	public static void transferBetweenFirstAndSecond(FluidComponent fluidComponent, ItemComponent itemComponent) {
		if (fluidComponent != null) {
			if (itemComponent != null) {
				FluidComponent firstStackFluidComponent = FluidComponent.get(itemComponent.getFirst());

				if (firstStackFluidComponent != null) {
					FluidVolume ourVolume = fluidComponent.getFirst();
					FluidVolume stackVolume = firstStackFluidComponent.getFirst();

					if (ourVolume.test(stackVolume.getFluid())) {
						if (itemComponent.getFirst().getItem() instanceof BucketItem) {
							if (itemComponent.getFirst().getItem() != Items.BUCKET && itemComponent.getFirst().getCount() == 1) {
								if (ourVolume.hasAvailable(Fraction.BUCKET) || ourVolume.isEmpty()) {
									ourVolume.moveFrom(stackVolume, Fraction.BUCKET);

									itemComponent.setFirst(new ItemStack(Items.BUCKET));
								}
							}
						} else {
							ourVolume.moveFrom(stackVolume, Fraction.BUCKET);
						}
					}
				}

				FluidComponent secondStackFluidComponent = FluidComponent.get(itemComponent.getSecond());

				if (secondStackFluidComponent != null) {
					FluidVolume ourVolume = fluidComponent.getFirst();
					FluidVolume stackVolume = secondStackFluidComponent.getFirst();

					if (stackVolume.test(ourVolume.getFluid())) {
						if (itemComponent.getSecond().getItem() instanceof BucketItem) {
							if (itemComponent.getSecond().getItem() == Items.BUCKET && itemComponent.getSecond().getCount() == 1) {
								if (ourVolume.hasStored(Fraction.BUCKET)) {
									ourVolume.add(stackVolume, Fraction.BUCKET);

									itemComponent.setSecond(new ItemStack(stackVolume.getFluid().getBucketItem()));
								}
							}
						} else {
							ourVolume.add(stackVolume, Fraction.BUCKET);
						}
					}
				}
			}
		}
	}
}
