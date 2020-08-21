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
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.base.TieredHorizontalFacingEnergyMachineBlock;
import com.github.chainmailstudios.astromine.common.block.entity.base.AbstractBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.recipe.base.AdvancedRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.FractionUtilities;
import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class LiquidGeneratingRecipe implements AdvancedRecipe<Inventory>, EnergyGeneratingRecipe<Inventory> {
	final Identifier identifier;
	final RegistryKey<Fluid> fluidKey;
	final Lazy<Fluid> fluid;
	final Fraction amount;
	final double energyGenerated;
	final int time;

	private static final int INPUT_ENERGY_VOLUME = 0;
	private static final int INPUT_FLUID_VOLUME = 0;

	public LiquidGeneratingRecipe(Identifier identifier, RegistryKey<Fluid> fluidKey, Fraction amount, double energyGenerated, int time) {
		this.identifier = identifier;
		this.fluidKey = fluidKey;
		this.fluid = new Lazy<>(() -> Registry.FLUID.get(this.fluidKey));
		this.amount = amount;
		this.energyGenerated = energyGenerated;
		this.time = time;
	}

	@Override
	public <T extends AbstractBlockEntity> boolean canCraft(T blockEntity) {
		Block block = blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock();
		if (!(block instanceof TieredHorizontalFacingEnergyMachineBlock))
			return false;
		Fraction speed = FractionUtilities.fromFloating(((TieredHorizontalFacingEnergyMachineBlock) block).getMachineSpeed() / 2);

		FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

		FluidVolume fluidVolume = fluidComponent.getVolume(0);

		if (!fluidVolume.getFluid().matchesType(fluid.get()))
			return false;
		return fluidVolume.hasStored(Fraction.simplify(Fraction.multiply(amount, speed)));
	}

	@Override
	public <T extends AbstractBlockEntity> void craft(T blockEntity) {
		if (canCraft(blockEntity)) {
			Block block = blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock();
			double machineSpeed = ((TieredHorizontalFacingEnergyMachineBlock) block).getMachineSpeed() / 2;
			Fraction speed = FractionUtilities.fromFloating(machineSpeed);

			EnergyHandler energyHandler = Energy.of(blockEntity);
			FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

			FluidVolume fluidVolume = fluidComponent.getVolume(INPUT_FLUID_VOLUME);

			if (EnergyUtilities.hasAvailable(energyHandler, energyGenerated * Math.max(1, machineSpeed))) {
				fluidVolume.extractVolume(Fraction.simplify(Fraction.multiply(amount, speed)));
				energyHandler.insert(energyGenerated * machineSpeed);
			}
		}
	}

	@Override
	public <T extends RecipeConsumer> void tick(T t) {
		if (t.isFinished()) {
			t.reset();

			craft((AbstractBlockEntity) t);
		} else if (canCraft((AbstractBlockEntity) t)) {
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
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.of(); // we are not dealing with items
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineBlocks.ADVANCED_LIQUID_GENERATOR);
	}

	public Fluid getFluid() {
		return fluid.get();
	}

	public Fraction getAmount() {
		return amount;
	}

	public double getEnergyGenerated() {
		return energyGenerated;
	}

	public int getTime() {
		return time;
	}

	public static final class Serializer implements RecipeSerializer<LiquidGeneratingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("liquid_generating");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public LiquidGeneratingRecipe read(Identifier identifier, JsonObject object) {
			LiquidGeneratingRecipe.Format format = new Gson().fromJson(object, LiquidGeneratingRecipe.Format.class);

			return new LiquidGeneratingRecipe(identifier, RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.input)), FractionUtilities.fromJson(format.amount), EnergyUtilities.fromJson(format.energyGenerated), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public LiquidGeneratingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new LiquidGeneratingRecipe(identifier, RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()), FractionUtilities.fromPacket(buffer), EnergyUtilities.fromPacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, LiquidGeneratingRecipe recipe) {
			buffer.writeIdentifier(recipe.fluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.amount);
			EnergyUtilities.toPacket(buffer, recipe.energyGenerated);
			buffer.writeInt(recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<LiquidGeneratingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		String input;

		@SerializedName("amount")
		JsonElement amount;

		@SerializedName("energy_generated")
		JsonElement energyGenerated;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "input='" + input + '\'' + ", amount=" + amount + ", energyGenerated=" + energyGenerated + ", time=" + time + '}';
		}
	}
}
