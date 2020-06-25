package com.github.chainmailstudios.astromine.recipe;

import net.minecraft.fluid.Fluid;
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
import com.github.chainmailstudios.astromine.common.block.entity.LiquidGeneratorBlockEntity;
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

public class LiquidGeneratingRecipe implements Recipe<Inventory> {
	final Identifier identifier;
	final RegistryKey<Fluid> fluidKey;
	final Lazy<Fluid> fluid;
	final Fraction amount;
	final Fraction energyGenerated;
	
	public LiquidGeneratingRecipe(Identifier identifier, RegistryKey<Fluid> fluidKey, Fraction amount, Fraction energyGenerated) {
		this.identifier = identifier;
		this.fluidKey = fluidKey;
		this.fluid = new Lazy<>(() -> Registry.FLUID.get(this.fluidKey));
		this.amount = amount;
		this.energyGenerated = energyGenerated;
	}
	
	public boolean tryCrafting(LiquidGeneratorBlockEntity generator, boolean isActuallyDoing) {
		FluidInventoryComponent fluidComponent = generator.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);
		FluidVolume fluidVolume = fluidComponent.getVolume(0);
		if (!fluidVolume.getFluid().matchesType(fluid.get())) return false;
		if (!fluidVolume.getFraction().isBiggerOrEqualThan(amount)) return false;
		if (isActuallyDoing) {
			EnergyInventoryComponent energyComponent = generator.getComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);
			EnergyVolume energyVolume = energyComponent.getVolume(0);
			if (energyVolume.hasAvailable(energyGenerated)) {
				fluidVolume.setFraction(Fraction.simplify(Fraction.subtract(fluidVolume.getFraction(), amount)));
				energyVolume.setFraction(Fraction.simplify(Fraction.add(energyVolume.getFraction(), energyGenerated)));
			}
		}
		return true;
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		return false; // we are not dealing with items
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY; // we are not dealing with items
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY; // we are not dealing with items
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
	
	public Fluid getFluid() {
		return fluid.get();
	}
	
	public Fraction getAmount() {
		return amount;
	}
	
	public Fraction getEnergyGenerated() {
		return energyGenerated;
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
					FractionUtilities.fromJson(format.energyGenerated));
		}
		
		@Override
		public LiquidGeneratingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new LiquidGeneratingRecipe(identifier,
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					FractionUtilities.fromPacket(buffer));
		}
		
		@Override
		public void write(PacketByteBuf buffer, LiquidGeneratingRecipe recipe) {
			buffer.writeIdentifier(recipe.fluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.amount);
			FractionUtilities.toPacket(buffer, recipe.energyGenerated);
		}
	}
	
	public static final class Type implements RecipeType<LiquidGeneratingRecipe> {
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
		
		@Override
		public String toString() {
			return "Format{" +
			       "input=" + input +
			       ", amount=" + amount +
			       ", energyGenerated=" + energyGenerated +
			       '}';
		}
	}
}
