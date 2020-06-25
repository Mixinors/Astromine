package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.SmelterBlock;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.recipe.SortingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;

import java.util.Optional;

public class SmelterBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	public SmelterBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	public int progress = 0;
	public int limit = 100;

	public boolean shouldTry = true;
	public boolean isActive = false;
	public boolean wasActive = false;

	BaseInventory inputInventory = new BaseInventory(1);

	Optional<SmeltingRecipe> recipe = Optional.empty();

	public SmelterBlockEntity() {
		super(AstromineBlockEntityTypes.SMELTER);

		energyComponent.getVolume(0).setSize(new Fraction(16, 1));
		inventoryComponent = new SimpleItemInventoryComponent(2);

		inventoryComponent.addListener(() -> {
			inputInventory.setStack(0, inventoryComponent.getStack(1));
			recipe = (Optional<SmeltingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) RecipeType.SMELTING, inputInventory, world);
			shouldTry = true;
		});
	}

	@Override
	public boolean isRequester() {
		return true;
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		progress = tag.getInt("progress");
		limit = tag.getInt("limit");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("progress", progress);
		tag.putInt("limit", limit);
		return super.toTag(tag);
	}

	@Override
	public void tick() {
		if (world.isClient()) return;
		if (shouldTry) {
			if (recipe.isPresent() && recipe.get().matches(inputInventory, world)) {
				limit = recipe.get().getCookTime() / 10;

				Fraction consumed = new Fraction(recipe.get().getCookTime(), 10 * 20 * 2);

				ItemStack output = recipe.get().getOutput().copy();

				boolean isEmpty = inventoryComponent.getStack(0).isEmpty();
				boolean isEqual = ItemStack.areItemsEqual(inventoryComponent.getStack(0), output) && ItemStack.areTagsEqual(inventoryComponent.getStack(0), output);

				if (energyComponent.getVolume(0).hasStored(consumed) && (isEmpty || isEqual) && inventoryComponent.getStack(0).getCount() + output.getCount() <= inventoryComponent.getStack(0).getMaxCount()) {
					if (progress == limit) {
						energyComponent.getVolume(0).extractVolume(consumed);

						inventoryComponent.getStack(1).decrement(1);

						if (isEmpty) {
							inventoryComponent.setStack(0, output);
						} else {
							inventoryComponent.getStack(0).increment(output.getCount());
						}

						progress = 0;
					} else {
						++progress;
					}

					isActive = true;
				}
			} else {
				shouldTry = false;
				isActive = false;
			}
		} else {
			progress = 0;
			isActive = false;
		}

		if (isActive && !wasActive) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(SmelterBlock.ACTIVE, true));
		} else if (!isActive && wasActive) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(SmelterBlock.ACTIVE, false));
		}

		wasActive = isActive;
	}
}
