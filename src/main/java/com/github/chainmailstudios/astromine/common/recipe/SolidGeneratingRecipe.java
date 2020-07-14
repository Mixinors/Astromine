package com.github.chainmailstudios.astromine.common.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.recipe.base.AdvancedRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.common.utilities.FractionUtilities;
import com.github.chainmailstudios.astromine.common.utilities.IngredientUtilities;
import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class SolidGeneratingRecipe implements AdvancedRecipe<Inventory> {
	final Identifier identifier;
	final Ingredient input;
	final int amount;
	final Fraction energyGenerated;
	final int time;

	private static final int OUTPUT_ENERGY_VOLUME = 0;
	private static final int INPUT_ITEM_SLOT = 0;

	public SolidGeneratingRecipe(Identifier identifier, Ingredient input, int amount, Fraction energyGenerated, int time) {
		this.identifier = identifier;
		this.input = input;
		this.amount = amount;
		this.energyGenerated = energyGenerated;
		this.time = time;
	}

	@Override
	public <T extends DefaultedBlockEntity> boolean canCraft(T blockEntity) {
		ItemInventoryComponent itemComponent = blockEntity.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT);

		ItemStack itemStack = itemComponent.getStack(0);

		return input.test(itemStack);
	}

	@Override
	public <T extends DefaultedBlockEntity> void craft(T blockEntity) {
		if (canCraft(blockEntity)) {
			EnergyInventoryComponent energyComponent = blockEntity.getComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);
			ItemInventoryComponent itemComponent = blockEntity.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT);

			EnergyVolume energyVolume = energyComponent.getVolume(OUTPUT_ENERGY_VOLUME);
			ItemStack itemStack = itemComponent.getStack(INPUT_ITEM_SLOT);

			if (energyVolume.hasAvailable(energyGenerated)) {
				itemStack.decrement(amount);
				energyVolume.pullVolume(EnergyVolume.of(energyGenerated), energyGenerated);
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

	public Ingredient getInput() {
		return input;
	}

	public int getAmount() {
		return amount;
	}

	public Fraction getEnergyGenerated() {
		return energyGenerated;
	}

	public int getTime() {
		return time;
	}

	public static final class Serializer implements RecipeSerializer<SolidGeneratingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("solid_generating");
		
		public static final Serializer INSTANCE = new Serializer();
		
		private Serializer() {
			// Locked.
		}
		
		@Override
		public SolidGeneratingRecipe read(Identifier identifier, JsonObject object) {
			SolidGeneratingRecipe.Format format = new Gson().fromJson(object, SolidGeneratingRecipe.Format.class);
			
			return new SolidGeneratingRecipe(identifier,
					IngredientUtilities.fromJson(format.input),
					ParsingUtilities.fromJson(format.amount, Integer.class),
					FractionUtilities.fromJson(format.energyGenerated),
					ParsingUtilities.fromJson(format.time, Integer.class));
		}
		
		@Override
		public SolidGeneratingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new SolidGeneratingRecipe(identifier,
					IngredientUtilities.fromPacket(buffer),
					PacketUtilities.fromPacket(buffer, Integer.class),
					FractionUtilities.fromPacket(buffer),
					PacketUtilities.fromPacket(buffer, Integer.class));
		}
		
		@Override
		public void write(PacketByteBuf buffer, SolidGeneratingRecipe recipe) {
			IngredientUtilities.toPacket(buffer, recipe.input);
			PacketUtilities.toPacket(buffer, recipe.amount);
			FractionUtilities.toPacket(buffer, recipe.energyGenerated);
			PacketUtilities.toPacket(buffer, recipe.time);
		}
	}
	
	public static final class Type implements RecipeType<SolidGeneratingRecipe> {
		public static final Type INSTANCE = new Type();
		
		private Type() {
			// Locked.
		}
	}
	
	public static final class Format {
		JsonObject input;

		@SerializedName("amount")
		JsonElement amount;

		@SerializedName("energy_generated")
		JsonElement energyGenerated;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" +
					"input=" + input +
					", amount=" + amount +
					", energyGenerated=" + energyGenerated +
					", time=" + time +
					'}';
		}
	}
}
