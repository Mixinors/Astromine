package com.github.chainmailstudios.astromine.common.recipe;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.TieredHorizontalFacingMachineBlock;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
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
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
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
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

public class FluidMixingRecipe implements AdvancedRecipe<Inventory>, EnergyConsumingRecipe<Inventory> {
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

	private static final int INPUT_ENERGY_VOLUME = 0;
	private static final int FIRST_INPUT_FLUID_VOLUME = 0;
	private static final int SECOND_INPUT_FLUID_VOLUME = 1;
	private static final int OUTPUT_FLUID_VOLUME = 2;

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

	@Override
	public <T extends DefaultedBlockEntity> boolean canCraft(T blockEntity) {
		Block block = blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock();
		if (!(block instanceof TieredHorizontalFacingMachineBlock)) return false;

		FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

		FluidVolume firstInputVolume = fluidComponent.getVolume(FIRST_INPUT_FLUID_VOLUME);
		FluidVolume secondInputVolume = fluidComponent.getVolume(SECOND_INPUT_FLUID_VOLUME);
		FluidVolume outputVolume = fluidComponent.getVolume(OUTPUT_FLUID_VOLUME);

		if (!firstInputVolume.getFluid().matchesType(firstInputFluid.get())) return false;
		if (!firstInputVolume.hasStored(firstInputAmount)) return false;
		if (!secondInputVolume.getFluid().matchesType(secondInputFluid.get())) return false;
		if (!secondInputVolume.hasStored(secondInputAmount)) return false;
		if (!outputVolume.getFluid().matchesType(outputFluid.get()) && !outputVolume.isEmpty()) return false;
		return outputVolume.hasAvailable(outputAmount);
	}

	@Override
	public <T extends DefaultedBlockEntity> void craft(T blockEntity) {
		if (canCraft(blockEntity)) {
			Block block = blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock();
			double machineSpeed = ((TieredHorizontalFacingMachineBlock) block).getMachineSpeed() / 2;
			Fraction speed = FractionUtilities.fromFloating(machineSpeed);

			FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

			FluidVolume firstInputFluidVolume = fluidComponent.getVolume(FIRST_INPUT_FLUID_VOLUME);
			FluidVolume secondInputFluidVolume = fluidComponent.getVolume(SECOND_INPUT_FLUID_VOLUME);
			FluidVolume outputVolume = fluidComponent.getVolume(OUTPUT_FLUID_VOLUME);

			EnergyHandler energyHandler = Energy.of(blockEntity);

			if (energyHandler.getEnergy() >= energyConsumed * Math.max(1, machineSpeed)) {
				firstInputFluidVolume.extractVolume(Fraction.simplify(Fraction.multiply(firstInputAmount, speed)));
				secondInputFluidVolume.extractVolume(Fraction.simplify(Fraction.multiply(secondInputAmount, speed)));
				outputVolume.insertVolume(new FluidVolume(outputFluid.get(), Fraction.simplify(Fraction.multiply(outputAmount, speed)).copy()));
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

	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.of(); // we are not dealing with items
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineBlocks.ADVANCED_FLUID_MIXER);
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

			return new FluidMixingRecipe(identifier,
					RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.firstInput)),
					FractionUtilities.fromJson(format.firstInputAmount),
					RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.secondInput)),
					FractionUtilities.fromJson(format.secondInputAmount),
					RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.output)),
					FractionUtilities.fromJson(format.outputAmount),
					EnergyUtilities.fromJson(format.energyGenerated),
					ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public FluidMixingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new FluidMixingRecipe(identifier,
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					EnergyUtilities.fromPacket(buffer),
					PacketUtilities.fromPacket(buffer, Integer.class));
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
			return "Format{" +
			       "firstInput='" + firstInput + '\'' +
			       ", firstInputAmount=" + firstInputAmount +
			       ", secondInput='" + secondInput + '\'' +
			       ", secondInputAmount=" + secondInputAmount +
			       ", output='" + output + '\'' +
			       ", outputAmount=" + outputAmount +
			       ", energyGenerated=" + energyGenerated +
			       '}';
		}
	}
}
