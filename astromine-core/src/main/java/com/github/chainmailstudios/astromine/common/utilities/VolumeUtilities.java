package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class VolumeUtilities {
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
                                if (ourVolume.hasAvailable(Fraction.bucket()) || ourVolume.isEmpty()) {
                                    ourVolume.moveFrom(stackVolume, Fraction.bucket());

                                    itemComponent.setFirst(new ItemStack(Items.BUCKET));
                                }
                            }
                        } else {
                            ourVolume.moveFrom(stackVolume, Fraction.bucket());
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
                                if (ourVolume.hasStored(Fraction.bucket())) {
                                    ourVolume.add(stackVolume, Fraction.bucket());

                                    itemComponent.setSecond(new ItemStack(stackVolume.getFluid().getBucketItem()));
                                }
                            }
                        } else {
                            ourVolume.add(stackVolume, Fraction.bucket());
                        }
                    }
                }
            }
        }
    }
}
