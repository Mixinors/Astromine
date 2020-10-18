package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
}
