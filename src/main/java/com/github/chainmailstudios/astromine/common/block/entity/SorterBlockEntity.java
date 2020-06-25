package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.recipe.SortingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import spinnery.common.inventory.BaseInventory;

import java.util.Optional;

public class SorterBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	public int progress = 0;
	public int limit = 100;

	public boolean shouldTry = true;

	BaseInventory inputInventory = new BaseInventory(1);

	Optional<SortingRecipe> recipe = Optional.empty();

	public SorterBlockEntity() {
		super(AstromineBlockEntityTypes.SORTER);

		energyComponent.getVolume(0).setSize(new Fraction(32, 1));
		itemComponent = new SimpleItemInventoryComponent(2);

		itemComponent.addListener(() -> {
			inputInventory.setStack(0, itemComponent.getStack(1));
			recipe = (Optional<SortingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) SortingRecipe.Type.INSTANCE, inputInventory, world);
			shouldTry = true;
		});

		addComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, itemComponent);
	}

	@Override
	public <T extends NetworkType> boolean isRequester(T type) {
		return true;
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public void tick() {
		if (shouldTry) {
			if (recipe.isPresent() && recipe.get().matches(ItemInventoryFromInventoryComponent.of(itemComponent), world)) {
				limit = recipe.get().getTimeTotal();
				
				Fraction consumed = recipe.get().getEnergyTotal().copy();

				ItemStack output = recipe.get().getOutput();

				boolean isEmpty = itemComponent.getStack(0).isEmpty();
				boolean isEqual = ItemStack.areItemsEqual(itemComponent.getStack(0), output) && ItemStack.areTagsEqual(itemComponent.getStack(0), output);

				if (energyComponent.getVolume(0).hasStored(consumed) && (isEmpty || isEqual) && itemComponent.getStack(0).getCount() + output.getCount() <= itemComponent.getStack(0).getMaxCount()) {
					if (progress == recipe.get().getTimeTotal()) {
						energyComponent.getVolume(0).extractVolume(consumed);

						recipe.get().craft(ItemInventoryFromInventoryComponent.of(itemComponent));

						if (isEmpty) {
							itemComponent.setStack(0, output);
						} else {
							itemComponent.getStack(0).increment(output.getCount());
						}

						progress = 0;
					} else {
						++progress;
					}
				}
			} else {
				shouldTry = false;
			}
		} else {
			progress = 0;
		}
	}
}
