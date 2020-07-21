package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.recipe.AlloySmelterRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import org.jetbrains.annotations.NotNull;
import spinnery.common.inventory.BaseInventory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class AlloySmelterBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	public static final int SPEED = 1;
	public int progress = 0;
	public int limit = 100;

	public boolean shouldTry = true;
	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	Optional<AlloySmelterRecipe> recipe = Optional.empty();

	public AlloySmelterBlockEntity() {
		super(AstromineBlockEntityTypes.ALLOY_SMELTER);

		setMaxStoredPower(32000);
		addEnergyListener(() -> shouldTry = true);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(3).withInsertPredicate((direction, itemStack, slot) -> {
			return slot == 0 || slot == 1;
		}).withExtractPredicate(((direction, stack, slot) -> {
			return slot == 2;
		})).withListener((inv) -> {
			shouldTry = true;
		});
	}

	@Override
	protected @NotNull Map<NetworkType, Collection<NetworkMemberType>> createMemberProperties() {
		return ofTypes(AstromineNetworkTypes.ENERGY, REQUESTER);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		progress = tag.getInt("progress");
		limit = tag.getInt("limit");
		shouldTry = true;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("progress", progress);
		tag.putInt("limit", limit);
		return super.toTag(tag);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient()) return;
		if (shouldTry) {
			BaseInventory inputInventory = new BaseInventory(2);
			inputInventory.setStack(0, itemComponent.getStack(0));
			inputInventory.setStack(1, itemComponent.getStack(1));
			if (!recipe.isPresent()) {
				if (hasWorld() && !world.isClient) {
					recipe = world.getRecipeManager().getFirstMatch(AlloySmelterRecipe.Type.INSTANCE, inputInventory, world);
				}
			}
			if (recipe.isPresent() && recipe.get().matches(inputInventory, world)) {
				limit = recipe.get().getTime();

				ItemStack output = recipe.get().getOutput().copy();

				for (int i = 0; i < SPEED; i++) {
					boolean isEmpty = itemComponent.getStack(2).isEmpty();
					boolean isEqual = ItemStack.areItemsEqual(itemComponent.getStack(2), output) && ItemStack.areTagsEqual(itemComponent.getStack(2), output);

					if (asEnergy().use(6) && (isEmpty || isEqual) && itemComponent.getStack(2).getCount() + output.getCount() <= itemComponent.getStack(2).getMaxCount()) {
						if (progress == limit) {
							itemComponent.getStack(0).decrement(1);
							itemComponent.getStack(1).decrement(1);

							if (isEmpty) {
								itemComponent.setStack(2, output);
							} else {
								itemComponent.getStack(2).increment(output.getCount());
							}

							progress = 0;
						} else {
							++progress;
						}

						isActive = true;
					}
				}
			} else {
				shouldTry = false;
				isActive = false;
				progress = 0;
			}
		} else {
			progress = 0;
			isActive = false;
		}

		if (activity.length - 1 >= 0) System.arraycopy(activity, 1, activity, 0, activity.length - 1);

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, false));
		}
	}
}
