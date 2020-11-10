package com.github.chainmailstudios.astromine.common.utilities;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.google.gson.JsonElement;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;

public class VolumeUtilities {
    /** Returns the amount of fluid transferred during transport as per {@link AstromineConfig}. */
    public static FluidAmount getTransferFluidAmount() {
        return FluidAmount.of(AstromineConfig.get().fluidTransferNumerator, AstromineConfig.get().fluidTransferDenominator);
    }

    /** Inserts fluids from the first stack into the first fluid volume.
     * Inserts fluids from the first fluid volume into the first stack. */
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
                                    ourVolume.take(stackVolume, Fraction.BUCKET);

                                    itemComponent.setFirst(new ItemStack(Items.BUCKET));
                                }
                            }
                        } else {
                            ourVolume.take(stackVolume, Fraction.BUCKET);
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
                                    ourVolume.give(stackVolume, Fraction.BUCKET);

                                    itemComponent.setSecond(new ItemStack(stackVolume.getFluid().getBucketItem()));
                                }
                            }
                        } else {
                            ourVolume.give(stackVolume, Fraction.BUCKET);
                        }
                    }
                }
            }
        }
    }
}
