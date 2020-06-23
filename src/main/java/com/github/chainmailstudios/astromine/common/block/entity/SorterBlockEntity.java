package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyBlockEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.recipe.SortingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;

import java.util.Optional;

public class SorterBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	public SorterBlockEntity() {
		super(AstromineBlockEntityTypes.SORTER);

		energyComponent.getVolume(0).setSize(new Fraction(16, 1));
		inventoryComponent = new SimpleItemInventoryComponent(2);
	}

	@Override
	public boolean isBuffer() {
		return true;
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public void tick() {
		BaseInventory inputInventory = new BaseInventory(1);
		inputInventory.setStack(0, inventoryComponent.getStack(0));

		Optional<SortingRecipe> recipe = (Optional<SortingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) SortingRecipe.Type.INSTANCE, inputInventory, world);

		if (recipe.isPresent() && recipe.get().matches(ItemInventoryFromInventoryComponent.of(inventoryComponent), world)) {
			Fraction consumed = new Fraction(recipe.get().getEnergyTotal(), 1);

			ItemStack output = recipe.get().getOutput();

			boolean isEmpty = inventoryComponent.getStack(0).isEmpty();
			boolean isEqual = ItemStack.areEqual(inventoryComponent.getStack(0), output);

			if (energyComponent.getVolume(0).hasStored(consumed) && (isEmpty || isEqual) && inventoryComponent.getStack(0).getCount() + output.getCount() <= inventoryComponent.getStack(0).getMaxCount()) {
				energyComponent.getVolume(0).extractVolume(consumed);

				recipe.get().craft(ItemInventoryFromInventoryComponent.of(inventoryComponent));

				if (isEmpty) {
					inventoryComponent.setStack(0, output);
				} else {
					inventoryComponent.getStack(0).increment(output.getCount());
				}
			}
		}
	}
}
