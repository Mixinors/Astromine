package com.github.chainmailstudios.astromine.common.recipe;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
import com.github.chainmailstudios.astromine.common.recipe.base.AdvancedRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.utilities.FractionUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ElectrolyzingRecipe implements AdvancedRecipe<Inventory> {
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
	final Fraction energyConsumed;
	final int time;

	private static final int INPUT_ENERGY_VOLUME = 0;
	private static final int INPUT_FLUID_VOLUME = 0;
	private static final int FIRST_OUTPUT_FLUID_VOLUME = 1;
	private static final int SECOND_OUTPUT_FLUID_VOLUME = 2;

	public ElectrolyzingRecipe(Identifier identifier, RegistryKey<Fluid> inputFluidKey, Fraction inputAmount, RegistryKey<Fluid> firstOutputFluidKey, Fraction firstOutputAmount, RegistryKey<Fluid> secondOutputFluidKey, Fraction secondOutputAmount, Fraction energyConsumed, int time) {
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
	public <T extends DefaultedBlockEntity> boolean canCraft(T blockEntity) {
		FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

		FluidVolume inputVolume = fluidComponent.getVolume(INPUT_FLUID_VOLUME);
		FluidVolume firstOutputVolume = fluidComponent.getVolume(FIRST_OUTPUT_FLUID_VOLUME);
		FluidVolume secondOutputVolume = fluidComponent.getVolume(SECOND_OUTPUT_FLUID_VOLUME);

		if (!inputVolume.getFluid().matchesType(inputFluid.get())) return false;
		if (!inputVolume.hasStored(inputAmount)) return false;
		if (!firstOutputVolume.getFluid().matchesType(firstOutputFluid.get()) && !firstOutputVolume.isEmpty()) return false;
		if (!firstOutputVolume.hasAvailable(firstOutputAmount)) return false;
		if (!secondOutputVolume.getFluid().matchesType(secondOutputFluid.get()) && !secondOutputVolume.isEmpty()) return false;
		if (!secondOutputVolume.hasAvailable(secondOutputAmount)) return false;

		return true;
	}

	@Override
	public <T extends DefaultedBlockEntity> void craft(T blockEntity) {
		if (canCraft(blockEntity)) {
			FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

			FluidVolume inputVolume = fluidComponent.getVolume(INPUT_FLUID_VOLUME);
			FluidVolume firstOutputVolume = fluidComponent.getVolume(FIRST_OUTPUT_FLUID_VOLUME);
			FluidVolume secondOutputVolume = fluidComponent.getVolume(SECOND_OUTPUT_FLUID_VOLUME);

			EnergyInventoryComponent energyComponent = blockEntity.getComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);

			EnergyVolume energyVolume = energyComponent.getVolume(INPUT_ENERGY_VOLUME);

			if (energyVolume.hasStored(energyConsumed)) {
				inputVolume.extractVolume(inputAmount);
				energyVolume.extractVolume(energyConsumed);
				firstOutputVolume.insertVolume(new FluidVolume(firstOutputFluid.get(), firstOutputAmount));
				secondOutputVolume.insertVolume(new FluidVolume(secondOutputFluid.get(), secondOutputAmount));
			}
		}
	}

	@Override
	public <T extends RecipeConsumer> void tick(T t) {
		if (t.isFinished()) {
			t.reset();

			craft((DefaultedBlockEntity) t);
		} else if (canCraft((DefaultedBlockEntity) t)) {
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

	public Fraction getEnergyConsumed() {
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
			
			return new ElectrolyzingRecipe(identifier,
					RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.input)),
					FractionUtilities.fromJson(format.inputAmount),
					RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.firstOutput)),
					FractionUtilities.fromJson(format.firstOutputAmount),
					RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.secondOutput)),
					FractionUtilities.fromJson(format.secondOutputAmount),
					FractionUtilities.fromJson(format.energyGenerated),
					ParsingUtilities.fromJson(format.time, Integer.class));
		}
		
		@Override
		public ElectrolyzingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new ElectrolyzingRecipe(identifier,
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					FractionUtilities.fromPacket(buffer),
					PacketUtilities.fromPacket(buffer, Integer.class));
		}
		
		@Override
		public void write(PacketByteBuf buffer, ElectrolyzingRecipe recipe) {
			buffer.writeIdentifier(recipe.inputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.inputAmount);
			buffer.writeIdentifier(recipe.firstOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.firstOutputAmount);
			buffer.writeIdentifier(recipe.secondOutputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.secondOutputAmount);
			FractionUtilities.toPacket(buffer, recipe.energyConsumed);
			buffer.writeInt(recipe.getTime());
		}
	}
	
	public static final class Type implements RecipeType<ElectrolyzingRecipe> {
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
			return "Format{" +
					"input='" + input + '\'' +
					", inputAmount=" + inputAmount +
					", firstOutput='" + firstOutput + '\'' +
					", firstOutputAmount=" + firstOutputAmount +
					", secondOutput='" + secondOutput + '\'' +
					", secondOutputAmount=" + secondOutputAmount +
					", energyGenerated=" + energyGenerated +
					", time=" + time +
					'}';
		}
	}
}
