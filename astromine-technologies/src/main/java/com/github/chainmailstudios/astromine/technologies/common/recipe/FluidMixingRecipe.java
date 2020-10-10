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

package com.github.chainmailstudios.astromine.technologies.common.recipe;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.FractionUtilities;
import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.handler.FluidHandler;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class FluidMixingRecipe implements Recipe<Inventory>, EnergyConsumingRecipe<Inventory> {
	final Identifier identifier;
	final RegistryKey<Fluid> firstInputFluidKey;
	final Lazy<Fluid> firstInputFluid;
	final Fraction firstInputAmount;
	final RegistryKey<Fluid> secondInputFluidKey;
	final Lazy<Fluid> secondInputFluid;
	final Fraction secondInputAmount;
	final RegistryKey<Fluid> outputFluidKey;
	final Lazy<Fluid> outputFluid;
	final Fraction outputAmount;
	final double energyConsumed;
	final int time;

	public FluidMixingRecipe(Identifier identifier, RegistryKey<Fluid> firstInputFluidKey, Fraction firstInputAmount, RegistryKey<Fluid> secondInputFluidKey, Fraction secondInputAmount, RegistryKey<Fluid> outputFluidKey, Fraction outputAmount, double energyConsumed, int time) {
		this.identifier = identifier;
		this.firstInputFluidKey = firstInputFluidKey;
		this.firstInputFluid = new Lazy<>(() -> Registry.FLUID.get(this.firstInputFluidKey));
		this.firstInputAmount = firstInputAmount;
		this.secondInputFluidKey = secondInputFluidKey;
		this.secondInputFluid = new Lazy<>(() -> Registry.FLUID.get(this.secondInputFluidKey));
		this.secondInputAmount = secondInputAmount;
		this.outputFluidKey = outputFluidKey;
		this.outputFluid = new Lazy<>(() -> Registry.FLUID.get(this.outputFluidKey));
		this.outputAmount = outputAmount;
		this.energyConsumed = energyConsumed;
		this.time = time;
	}

	public static boolean allows(World world, Fluid inserting, Fluid existing) {
		return world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().anyMatch(it -> {
			FluidMixingRecipe recipe = ((FluidMixingRecipe) it);

			return (existing == inserting || existing == Fluids.EMPTY) && (recipe.firstInputFluid.get() == inserting || recipe.secondInputFluid.get() == inserting);
		});
	}

	public boolean matches(FluidInventoryComponent fluidComponent) {
		FluidHandler fluidHandler = FluidHandler.of(fluidComponent);

		FluidVolume firstInputVolume = fluidHandler.getFirst();
		FluidVolume secondInputVolume = fluidHandler.getSecond();
		FluidVolume outputVolume = fluidHandler.getThird();

		if (!firstInputVolume.canAccept(firstInputFluid.get()) && !secondInputVolume.canAccept(firstInputFluid.get())) {
			return false;
		}

		if (!firstInputVolume.hasStored(firstInputAmount)) {
			return false;
		}

		if (!secondInputVolume.canAccept(secondInputFluid.get()) && !firstInputVolume.canAccept(secondInputFluid.get())) {
			return false;
		}

		if (!secondInputVolume.hasStored(secondInputAmount)) {
			return false;
		}

		if (!outputVolume.canAccept(outputFluid.get())) {
			return false;
		}

		return outputVolume.hasAvailable(outputAmount);
	}

	@Override
	public Identifier getId() {
		return identifier;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return false;
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.of(); // we are not dealing with items
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public Fluid getFirstInputFluid() {
		return firstInputFluid.get();
	}

	public Fraction getFirstInputAmount() {
		return firstInputAmount;
	}

	public Fluid getSecondInputFluid() {
		return secondInputFluid.get();
	}

	public Fraction getSecondInputAmount() {
		return secondInputAmount;
	}

	public Fluid getOutputFluid() {
		return outputFluid.get();
	}

	public Fraction getOutputAmount() {
		return outputAmount;
	}

	public double getEnergyConsumed() {
		return energyConsumed;
	}

	public int getTime() {
		return time;
	}

	public static final class Serializer implements RecipeSerializer<FluidMixingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("fluid_mixing");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public FluidMixingRecipe read(Identifier identifier, JsonObject object) {
			FluidMixingRecipe.Format format = new Gson().fromJson(object, FluidMixingRecipe.Format.class);

			return new FluidMixingRecipe(identifier, RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.firstInput)), FractionUtilities.fromJson(format.firstInputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.secondInput)), FractionUtilities.fromJson(
				format.secondInputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.output)), FractionUtilities.fromJson(format.outputAmount), EnergyUtilities.fromJson(format.energyGenerated), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public FluidMixingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new FluidMixingRecipe(identifier, RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(
				Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), EnergyUtilities.fromPacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, FluidMixingRecipe recipe) {
			buffer.writeIdentifier(recipe.firstInputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.firstInputAmount);
			buffer.writeIdentifier(recipe.secondInputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.secondInputAmount);
			buffer.writeIdentifier(recipe.outputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.outputAmount);
			EnergyUtilities.toPacket(buffer, recipe.energyConsumed);
			buffer.writeInt(recipe.getTime());
		}
	}

	public static final class Type implements AstromineRecipeType<FluidMixingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		@SerializedName("first_input")
		String firstInput;

		@SerializedName("first_input_amount")
		JsonElement firstInputAmount;

		@SerializedName("second_input")
		String secondInput;

		@SerializedName("second_input_amount")
		JsonElement secondInputAmount;

		String output;

		@SerializedName("output_amount")
		JsonElement outputAmount;

		@SerializedName("energy_consumed")
		JsonElement energyGenerated;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "firstInput='" + firstInput + '\'' + ", firstInputAmount=" + firstInputAmount + ", secondInput='" + secondInput + '\'' + ", secondInputAmount=" + secondInputAmount + ", output='" + output + '\'' + ", outputAmount=" + outputAmount +
				", energyGenerated=" + energyGenerated + '}';
		}
	}
}
