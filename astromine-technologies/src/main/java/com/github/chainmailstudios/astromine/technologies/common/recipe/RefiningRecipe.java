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
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class RefiningRecipe implements Recipe<Inventory>, EnergyConsumingRecipe<Inventory> {
	final Identifier identifier;
	final RegistryKey<Fluid> inputFluidKey;
	final Lazy<Fluid> inputFluid;
	final Fraction inputAmount;
	final RegistryKey<Fluid> firstOutputFluidKey;
	final Lazy<Fluid> firstOutputFluid;
	final Fraction firstOutputAmount;
	final RegistryKey<Fluid> secondOutputFluidKey;
	final Lazy<Fluid> secondOutputFluid;
	final Fraction secondOutputAmount;
	final RegistryKey<Fluid> thirdOutputFluidKey;
	final Lazy<Fluid> thirdOutputFluid;
	final Fraction thirdOutputAmount;
	final RegistryKey<Fluid> fourthOutputFluidKey;
	final Lazy<Fluid> fourthOutputFluid;
	final Fraction fourthOutputAmount;
	final RegistryKey<Fluid> fifthOutputFluidKey;
	final Lazy<Fluid> fifthOutputFluid;
	final Fraction fifthOutputAmount;
	final RegistryKey<Fluid> sixthOutputFluidKey;
	final Lazy<Fluid> sixthOutputFluid;
	final Fraction sixthOutputAmount;
	final RegistryKey<Fluid> seventhOutputFluidKey;
	final Lazy<Fluid> seventhOutputFluid;
	final Fraction seventhOutputAmount;
	final double energyConsumed;
	final int time;

	public RefiningRecipe(Identifier identifier, RegistryKey<Fluid> inputFluidKey, Fraction inputAmount, RegistryKey<Fluid> firstOutputFluidKey, Fraction firstOutputAmount, RegistryKey<Fluid> secondOutputFluidKey, Fraction secondOutputAmount,
		RegistryKey<Fluid> thirdOutputFluidKey, Fraction thirdOutputAmount, RegistryKey<Fluid> fourthOutputFluidKey, Fraction fourthOutputAmount, RegistryKey<Fluid> fifthOutputFluidKey, Fraction fifthOutputAmount, RegistryKey<Fluid> sixthOutputFluidKey,
		Fraction sixthOutputAmount, RegistryKey<Fluid> seventhOutputFluidKey, Fraction seventhOutputAmount, double energyConsumed, int time) {
		this.identifier = identifier;
		this.inputFluidKey = inputFluidKey;
		this.inputFluid = new Lazy<>(() -> Registry.FLUID.get(this.inputFluidKey));
		this.inputAmount = inputAmount;
		this.firstOutputFluidKey = firstOutputFluidKey;
		this.firstOutputFluid = new Lazy<>(() -> Registry.FLUID.get(this.firstOutputFluidKey));
		this.firstOutputAmount = firstOutputAmount;
		this.secondOutputFluidKey = secondOutputFluidKey;
		this.secondOutputFluid = new Lazy<>(() -> Registry.FLUID.get(this.secondOutputFluidKey));
		this.secondOutputAmount = secondOutputAmount;
		this.thirdOutputFluidKey = thirdOutputFluidKey;
		this.thirdOutputFluid = new Lazy<>(() -> Registry.FLUID.get(this.thirdOutputFluidKey));
		this.thirdOutputAmount = thirdOutputAmount;
		this.fourthOutputFluidKey = fourthOutputFluidKey;
		this.fourthOutputFluid = new Lazy<>(() -> Registry.FLUID.get(this.fourthOutputFluidKey));
		this.fourthOutputAmount = fourthOutputAmount;
		this.fifthOutputFluidKey = fifthOutputFluidKey;
		this.fifthOutputFluid = new Lazy<>(() -> Registry.FLUID.get(this.fifthOutputFluidKey));
		this.fifthOutputAmount = fifthOutputAmount;
		this.sixthOutputFluidKey = sixthOutputFluidKey;
		this.sixthOutputFluid = new Lazy<>(() -> Registry.FLUID.get(this.sixthOutputFluidKey));
		this.sixthOutputAmount = sixthOutputAmount;
		this.seventhOutputFluidKey = seventhOutputFluidKey;
		this.seventhOutputFluid = new Lazy<>(() -> Registry.FLUID.get(this.seventhOutputFluidKey));
		this.seventhOutputAmount = seventhOutputAmount;
		this.energyConsumed = energyConsumed;
		this.time = time;
	}

	public boolean matches(FluidInventoryComponent fluidComponent) {
		FluidHandler fluidHandler = FluidHandler.of(fluidComponent);

		FluidVolume inputVolume = fluidHandler.getFirst();
		FluidVolume firstOutputVolume = fluidHandler.getSecond();
		FluidVolume secondOutputVolume = fluidHandler.getThird();
		FluidVolume thirdOutputVolume = fluidHandler.getFourth();
		FluidVolume fourthOutputVolume = fluidHandler.getFifth();
		FluidVolume fifthOutputVolume = fluidHandler.getSixth();
		FluidVolume sixthOutputVolume = fluidHandler.getSeventh();
		FluidVolume seventhOutputVolume = fluidHandler.getEighth();

		if (!inputVolume.canAccept(inputFluid.get())) {
			return false;
		}
		if (!inputVolume.hasStored(inputAmount)) {
			return false;
		}

		if (!firstOutputVolume.canAccept(firstOutputFluid.get())) {
			return false;
		}
		if (!firstOutputVolume.hasAvailable(firstOutputAmount)) {
			return false;
		}

		if (!secondOutputVolume.canAccept(secondOutputFluid.get())) {
			return false;
		}

		if (secondOutputVolume.hasAvailable(secondOutputAmount)) {
			return false;
		}

		if (!thirdOutputVolume.canAccept(thirdOutputFluid.get())) {
			return false;
		}

		if (thirdOutputVolume.hasAvailable(thirdOutputAmount)) {
			return false;
		}

		if (!fourthOutputVolume.canAccept(fourthOutputFluid.get())) {
			return false;
		}

		if (fourthOutputVolume.hasAvailable(fourthOutputAmount)) {
			return false;
		}

		if (!fifthOutputVolume.canAccept(fifthOutputFluid.get())) {
			return false;
		}

		if (fifthOutputVolume.hasAvailable(fifthOutputAmount)) {
			return false;
		}

		if (!sixthOutputVolume.canAccept(sixthOutputFluid.get())) {
			return false;
		}

		if (sixthOutputVolume.hasAvailable(sixthOutputAmount)) {
			return false;
		}

		if (!seventhOutputVolume.canAccept(seventhOutputFluid.get())) {
			return false;
		}

		return !seventhOutputVolume.hasAvailable(seventhOutputAmount);
	}

	public static boolean allows(World world, Fluid inserting, Fluid existing) {
		return world.getRecipeManager().getAllOfType(RefiningRecipe.Type.INSTANCE).values().stream().anyMatch(it -> {
			RefiningRecipe recipe = ((RefiningRecipe) it);

			return (existing == inserting || existing == Fluids.EMPTY) && (recipe.inputFluid.get() == inserting);
		});
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
		return null;
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
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public Fluid getInputFluid() {
		return inputFluid.get();
	}

	public Fraction getInputAmount() {
		return inputAmount.copy();
	}

	public Fluid getFirstOutputFluid() {
		return firstOutputFluid.get();
	}

	public Fraction getFirstOutputAmount() {
		return firstOutputAmount.copy();
	}

	public Fluid getSecondOutputFluid() {
		return secondOutputFluid.get();
	}

	public Fraction getSecondOutputAmount() {
		return secondOutputAmount.copy();
	}

	public Fluid getThirdOutputFluid() {
		return thirdOutputFluid.get();
	}

	public Fraction getThirdOutputAmount() {
		return thirdOutputAmount.copy();
	}

	public Fluid getFourthOutputFluid() {
		return fourthOutputFluid.get();
	}

	public Fraction getFourthOutputAmount() {
		return fourthOutputAmount.copy();
	}

	public Fluid getFifthOutputFluid() {
		return fifthOutputFluid.get();
	}

	public Fraction getFifthOutputAmount() {
		return fifthOutputAmount.copy();
	}

	public Fluid getSixthOutputFluid() {
		return sixthOutputFluid.get();
	}

	public Fraction getSixthOutputAmount() {
		return sixthOutputAmount.copy();
	}

	public Fluid getSeventhOutputFluid() {
		return seventhOutputFluid.get();
	}

	public Fraction getSeventhOutputAmount() {
		return seventhOutputAmount.copy();
	}

	public double getEnergyConsumed() {
		return energyConsumed;
	}

	public int getTime() {
		return time;
	}

	public static final class Serializer implements RecipeSerializer<RefiningRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("refining");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public RefiningRecipe read(Identifier identifier, JsonObject object) {
			RefiningRecipe.Format format = new Gson().fromJson(object, RefiningRecipe.Format.class);

			return new RefiningRecipe(identifier, RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.input)), FractionUtilities.fromJson(format.inputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.firstOutput)), FractionUtilities.fromJson(
				format.firstOutputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.secondOutput)), FractionUtilities.fromJson(format.secondOutputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.thirdOutput)), FractionUtilities.fromJson(
					format.thirdOutputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.fourthOutput)), FractionUtilities.fromJson(format.fourthOutputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.fifthOutput)), FractionUtilities.fromJson(
						format.fifthOutputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.sixthOutput)), FractionUtilities.fromJson(format.sixthOutputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.seventhOutput)), FractionUtilities.fromJson(
							format.seventhOutputAmount), EnergyUtilities.fromJson(format.energyGenerated), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public RefiningRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new RefiningRecipe(identifier, RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(
				Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities
					.fromPacket(buffer), RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(Registry.FLUID_KEY, buffer
						.readIdentifier()), FractionUtilities.fromPacket(buffer), EnergyUtilities.fromPacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, RefiningRecipe recipe) {
			buffer.writeIdentifier(recipe.inputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.inputAmount);
			buffer.writeIdentifier(recipe.firstOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.firstOutputAmount);
			buffer.writeIdentifier(recipe.secondOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.secondOutputAmount);
			buffer.writeIdentifier(recipe.thirdOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.thirdOutputAmount);
			buffer.writeIdentifier(recipe.fourthOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.fourthOutputAmount);
			buffer.writeIdentifier(recipe.fifthOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.fifthOutputAmount);
			buffer.writeIdentifier(recipe.sixthOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.sixthOutputAmount);
			buffer.writeIdentifier(recipe.seventhOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.seventhOutputAmount);
			EnergyUtilities.toPacket(buffer, recipe.energyConsumed);
			buffer.writeInt(recipe.getTime());
		}
	}

	public static final class Type implements AstromineRecipeType<RefiningRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		String input;
		@SerializedName("input_amount")
		JsonElement inputAmount;

		@SerializedName("first_output")
		String firstOutput;

		@SerializedName("first_output_amount")
		JsonElement firstOutputAmount;

		@SerializedName("second_output")
		String secondOutput;

		@SerializedName("second_output_amount")
		JsonElement secondOutputAmount;

		@SerializedName("third_output")
		String thirdOutput;

		@SerializedName("third_output_amount")
		JsonElement thirdOutputAmount;

		@SerializedName("fourth_output")
		String fourthOutput;

		@SerializedName("fourth_output_amount")
		JsonElement fourthOutputAmount;

		@SerializedName("fifth_output")
		String fifthOutput;

		@SerializedName("fifth_output_amount")
		JsonElement fifthOutputAmount;

		@SerializedName("sixth_output")
		String sixthOutput;

		@SerializedName("sixth_output_amount")
		JsonElement sixthOutputAmount;

		@SerializedName("seventh_output")
		String seventhOutput;

		@SerializedName("seventh_output_amount")
		JsonElement seventhOutputAmount;

		@SerializedName("energy_consumed")
		JsonElement energyGenerated;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "input='" + input + '\'' + ", inputAmount=" + inputAmount + ", firstOutput='" + firstOutput + '\'' + ", firstOutputAmount=" + firstOutputAmount + ", secondOutput='" + secondOutput + '\'' + ", secondOutputAmount=" + secondOutputAmount +
				", thirdOutput='" + thirdOutput + '\'' + ", thirdOutputAmount=" + thirdOutputAmount + ", fourthOutput='" + fourthOutput + '\'' + ", fourthOutputAmount=" + fourthOutputAmount + ", fifthOutput='" + fifthOutput + '\'' + ", fifthOutputAmount=" + fifthOutputAmount +
				", sixthOutput='" + sixthOutput + '\'' + ", sixthOutputAmount=" + sixthOutputAmount + ", seventhOutput='" + seventhOutput + '\'' + ", seventhOutputAmount=" + seventhOutputAmount + ", energyGenerated=" + energyGenerated + ", time=" + time + '}';
		}
	}
}
