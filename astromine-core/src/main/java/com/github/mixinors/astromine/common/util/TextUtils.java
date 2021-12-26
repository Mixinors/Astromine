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

import com.github.mixinors.astromine.common.volume.base.Volume;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import dev.architectury.platform.Platform;
import team.reborn.energy.api.EnergyStorage;

import net.minecraft.fluid.Fluid;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;

@SuppressWarnings("unstable")
public class TextUtils {
	public static final char ENERGY_UNIT = 'E';
	public static final char FLUID_UNIT = 'D';

	/**
	 * Returns the "Astromine" text, formatted with {@link Formatting#BLUE}
	 * and {@link Formatting#ITALIC}.
	 */
	public static Text getAstromine() {
		return new LiteralText("Astromine").formatted(Formatting.BLUE, Formatting.ITALIC);
	}

	/**
	 * Returns the "Energy" text.
	 */
	public static Text getEnergy() {
		return new TranslatableText("text.astromine.energy");
	}

	/**
	 * Returns the "16kE / 32kE" text.
	 */
	public static Text getEnergyStorage(EnergyStorage storage) {
		return getEnergyAmount(storage.getAmount()).append(new LiteralText(" / ").formatted(Formatting.GRAY)).append(getEnergyAmount(storage.getCapacity()));
	}

	/**
	 * Returns the "16kD / 32kD" text.
	 */
	public static Text getFluidStorage(StorageView<FluidVariant> storage) {
		Fluid fluid = storage.getResource().getFluid();
		return getFluidAmount(fluid, storage.getAmount()).append(new LiteralText(" / ").formatted(Formatting.GRAY)).append(getFluidAmount(fluid, storage.getCapacity()));
	}

	/**
	 * Returns the "Water" / "Lava" / "Hydrogen" / ... text.
	 */
	public static Text getFluid(Identifier fluidId) {
		return new TranslatableText(String.format("block.%s.%s", fluidId.getNamespace(), fluidId.getPath()));
	}

	/**
	 * Returns the "Water" / "Lava" / "Hydrogen" / ... text.
	 */
	public static Text getFluid(Fluid fluid) {
		return getFluid(Registry.FLUID.getId(fluid));
	}

	/**
	 * Returns the "Water" / "Lava" / "Hydrogen" / ... text.
	 */
	public static Text getFluidVariant(FluidVariant variant) {
		return getFluid(variant.getFluid());
	}

	/**
	 * Returns the "astromine:oxygen" / "minecraft:stone" ... text, formatted with {@link Formatting#DARK_GRAY}.
	 */
	public static Text getIdentifier(Identifier identifier) {
		return new LiteralText(identifier.toString()).formatted(Formatting.DARK_GRAY);
	}

	/**
	 * Returns the "Astromine" / "TechReborn" / "Minecraft" ... text, formatted with {@link Formatting#BLUE}
	 * and {@link Formatting#ITALIC}.
	 */
	public static Text getMod(Identifier identifier) {
		return new LiteralText(Platform.getMod(identifier.getNamespace()).getName()).formatted(Formatting.BLUE, Formatting.ITALIC);
	}

	/**
	 * Returns the "75%" text.
	 */
	public static Text getRatio(int progress, int limit) {
		return new LiteralText((int) ((float) progress / (float) limit * 100) + "%");
	}

	/**
	 * Returns the 16kD text.
	 */
	public static LiteralText getAmount(long amount, char unit) {
		return new LiteralText(NumberUtils.shorten(amount, unit));
	}

	/**
	 * Returns the 16kD text, formatted with the color of the fluid.
	 */
	public static MutableText getFluidAmount(Fluid fluid, long amount) {
		Style style = Style.EMPTY.withColor(TextColor.fromRgb(FluidUtils.getColor(ClientUtils.getPlayer(), fluid)));
		return getFluidAmount(amount).fillStyle(style);
	}

	/**
	 * Returns the 16kD text.
	 */
	public static LiteralText getFluidAmount(long amount) {
		return getAmount(amount, FLUID_UNIT);
	}

	/**
	 * Returns the 16kE text, formatted with {@link Formatting#GREEN}.
	 */
	public static MutableText getEnergyAmount(long amount) {
		return getAmount(amount, ENERGY_UNIT).formatted(Formatting.GREEN);
	}
}
