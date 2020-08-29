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

package com.github.chainmailstudios.astromine.common.recipe;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingEnergyTieredBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.recipe.base.AdvancedRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.FractionUtilities;
import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import team.reborn.energy.Energy;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ElectrolyzingRecipe implements AdvancedRecipe<Inventory>, EnergyConsumingRecipe<Inventory> {
	private static final int INPUT_ENERGY_VOLUME = 0;
	private static final int INPUT_FLUID_VOLUME = 0;
	private static final int FIRST_OUTPUT_FLUID_VOLUME = 1;
	private static final int SECOND_OUTPUT_FLUID_VOLUME = 2;
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
	final double energyConsumed;
	final int time;

	public ElectrolyzingRecipe(Identifier identifier, RegistryKey<Fluid> inputFluidKey, Fraction inputAmount, RegistryKey<Fluid> firstOutputFluidKey, Fraction firstOutputAmount, RegistryKey<Fluid> secondOutputFluidKey, Fraction secondOutputAmount, double energyConsumed,
		int time) {
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
		this.energyConsumed = energyConsumed;
		this.time = time;
	}

	@Override
	public <T extends ComponentBlockEntity> boolean canCraft(T blockEntity) {
		Block block = blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock();
		if (!(block instanceof WrenchableHorizontalFacingEnergyTieredBlockWithEntity))
			return false;

		double machineSpeed = ((WrenchableHorizontalFacingEnergyTieredBlockWithEntity) block).getMachineSpeed();
		Fraction speed = FractionUtilities.fromFloating(machineSpeed);

		FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

		FluidVolume inputVolume = fluidComponent.getVolume(INPUT_FLUID_VOLUME);
		FluidVolume firstOutputVolume = fluidComponent.getVolume(FIRST_OUTPUT_FLUID_VOLUME);
		FluidVolume secondOutputVolume = fluidComponent.getVolume(SECOND_OUTPUT_FLUID_VOLUME);

		if (Energy.of(blockEntity).getEnergy() < energyConsumed * machineSpeed)
			return false;
		if (!inputVolume.getFluid().matchesType(inputFluid.get()))
			return false;
		if (!inputVolume.hasStored(Fraction.simplify(Fraction.multiply(inputAmount, speed))))
			return false;
		if (!firstOutputVolume.getFluid().matchesType(firstOutputFluid.get()) && !firstOutputVolume.isEmpty())
			return false;
		if (!firstOutputVolume.hasAvailable(Fraction.simplify(Fraction.multiply(firstOutputAmount, speed))))
			return false;
		if (!secondOutputVolume.getFluid().matchesType(secondOutputFluid.get()) && !secondOutputVolume.isEmpty())
			return false;
		return secondOutputVolume.hasAvailable(Fraction.simplify(Fraction.multiply(secondOutputAmount, speed)));
	}

	@Override
	public <T extends ComponentBlockEntity> void craft(T blockEntity) {
		if (canCraft(blockEntity)) {
			Block block = blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock();
			double machineSpeed = ((WrenchableHorizontalFacingEnergyTieredBlockWithEntity) block).getMachineSpeed();
			Fraction speed = FractionUtilities.fromFloating(machineSpeed);

			FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

			FluidVolume inputVolume = fluidComponent.getVolume(INPUT_FLUID_VOLUME);
			FluidVolume firstOutputVolume = fluidComponent.getVolume(FIRST_OUTPUT_FLUID_VOLUME);
			FluidVolume secondOutputVolume = fluidComponent.getVolume(SECOND_OUTPUT_FLUID_VOLUME);

			if (Energy.of(blockEntity).use(energyConsumed * machineSpeed)) {
				inputVolume.extractVolume(Fraction.simplify(Fraction.multiply(inputAmount, speed)));
				firstOutputVolume.insertVolume(new FluidVolume(firstOutputFluid.get(), Fraction.simplify(Fraction.multiply(firstOutputAmount, speed))));
				secondOutputVolume.insertVolume(new FluidVolume(secondOutputFluid.get(), Fraction.simplify(Fraction.multiply(secondOutputAmount, speed))));
			}
		}
	}

	@Override
	public <T extends RecipeConsumer> void tick(T t) {
		if (t.isFinished()) {
			t.reset();

			craft((ComponentBlockEntity) t);
		} else if (canCraft((ComponentBlockEntity) t)) {
			t.setLimit(getTime());
			t.increment();
			t.setActive(true);
		} else {
			t.reset();
		}
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

	public double getEnergyConsumed() {
		return energyConsumed;
	}

	public int getTime() {
		return time;
	}

	public static final class Serializer implements RecipeSerializer<ElectrolyzingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("electrolyzing");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public ElectrolyzingRecipe read(Identifier identifier, JsonObject object) {
			ElectrolyzingRecipe.Format format = new Gson().fromJson(object, ElectrolyzingRecipe.Format.class);

			return new ElectrolyzingRecipe(identifier, RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.input)), FractionUtilities.fromJson(format.inputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.firstOutput)), FractionUtilities.fromJson(
				format.firstOutputAmount), RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.secondOutput)), FractionUtilities.fromJson(format.secondOutputAmount), EnergyUtilities.fromJson(format.energyGenerated), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public ElectrolyzingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new ElectrolyzingRecipe(identifier, RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), RegistryKey.of(
				Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), EnergyUtilities.fromPacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, ElectrolyzingRecipe recipe) {
			buffer.writeIdentifier(recipe.inputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.inputAmount);
			buffer.writeIdentifier(recipe.firstOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.firstOutputAmount);
			buffer.writeIdentifier(recipe.secondOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.secondOutputAmount);
			EnergyUtilities.toPacket(buffer, recipe.energyConsumed);
			buffer.writeInt(recipe.getTime());
		}
	}

	public static final class Type implements AstromineRecipeType<ElectrolyzingRecipe> {
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

		@SerializedName("energy_consumed")
		JsonElement energyGenerated;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "input='" + input + '\'' + ", inputAmount=" + inputAmount + ", firstOutput='" + firstOutput + '\'' + ", firstOutputAmount=" + firstOutputAmount + ", secondOutput='" + secondOutput + '\'' + ", secondOutputAmount=" + secondOutputAmount +
				", energyGenerated=" + energyGenerated + ", time=" + time + '}';
		}
	}
}
