package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.recipe.PressingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Tickable;
import spinnery.common.inventory.BaseInventory;

import java.util.Optional;

public class PresserBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	public int progress = 0;
	public int limit = 100;

	public boolean shouldTry = true;

	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	BaseInventory inputInventory = new BaseInventory(1);

	Optional<PressingRecipe> recipe = Optional.empty();

	public PresserBlockEntity() {
		super(AstromineBlockEntityTypes.PRESSER);

		setMaxStoredPower(32000);
		itemComponent = new SimpleItemInventoryComponent(2);

		itemComponent.addListener(() -> {
			inputInventory.setStack(0, itemComponent.getStack(1));
			recipe = (Optional<PressingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) PressingRecipe.Type.INSTANCE, inputInventory, world);
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
				limit = recipe.get().getTime();

				double consumed = recipe.get().getEnergyConsumed();

				ItemStack output = recipe.get().getOutput();

				boolean isEmpty = itemComponent.getStack(0).isEmpty();
				boolean isEqual = ItemStack.areItemsEqual(itemComponent.getStack(0), output) && ItemStack.areTagsEqual(itemComponent.getStack(0), output);

				if (asEnergy().use(consumed) && (isEmpty || isEqual) && itemComponent.getStack(0).getCount() + output.getCount() <= itemComponent.getStack(0).getMaxCount()) {
					if (progress == recipe.get().getTime()) {
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

		for (int i = 1; i < activity.length; ++i) {
			activity[i - 1] = activity[i];
		}

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, false));
		}
	}
}
