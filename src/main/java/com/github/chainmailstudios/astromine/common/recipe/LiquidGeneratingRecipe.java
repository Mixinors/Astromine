package com.github.chainmailstudios.astromine.common.recipe;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
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
	public <T extends DefaultedBlockEntity> boolean canCraft(T blockEntity) {
		FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

		FluidVolume fluidVolume = fluidComponent.getVolume(0);

		if (!fluidVolume.getFluid().matchesType(fluid.get())) return false;
		return fluidVolume.hasStored(amount);
	}

	@Override
	public <T extends DefaultedBlockEntity> void craft(T blockEntity) {
		if (canCraft(blockEntity)) {
			EnergyHandler energyHandler = Energy.of(blockEntity);
			FluidInventoryComponent fluidComponent = blockEntity.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

			FluidVolume fluidVolume = fluidComponent.getVolume(INPUT_FLUID_VOLUME);

			if (EnergyUtilities.hasAvailable(energyHandler, energyGenerated)) {
				fluidVolume.extractVolume(amount);
				energyHandler.insert(energyGenerated);
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
		return new ItemStack(AstromineBlocks.ELITE_LIQUID_GENERATOR);
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

			return new LiquidGeneratingRecipe(identifier,
					RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.input)),
					FractionUtilities.fromJson(format.amount),
					EnergyUtilities.fromJson(format.energyGenerated),
					ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public LiquidGeneratingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new LiquidGeneratingRecipe(identifier,
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					EnergyUtilities.fromPacket(buffer),
					PacketUtilities.fromPacket(buffer, Integer.class));
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
			return "Format{" +
					"input='" + input + '\'' +
					", amount=" + amount +
					", energyGenerated=" + energyGenerated +
					", time=" + time +
					'}';
		}
	}
}
