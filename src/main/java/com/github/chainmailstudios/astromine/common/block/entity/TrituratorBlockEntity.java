package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.recipe.TrituratingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Tickable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class TrituratorBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	public int progress = 0;
	public int limit = 100;

	public boolean shouldTry = true;

	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	Optional<TrituratingRecipe> recipe = Optional.empty();

	public TrituratorBlockEntity() {
		super(AstromineBlockEntityTypes.TRITURATOR);

		setMaxStoredPower(32000);
		addEnergyListener(() -> shouldTry = true);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2).withInsertPredicate((direction, itemStack, slot) -> {
			return slot == 1;
		}).withListener((inv) -> {
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
			if (!recipe.isPresent()) {
				if (hasWorld() && !world.isClient) {
					recipe = (Optional<TrituratingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) TrituratingRecipe.Type.INSTANCE, ItemInventoryFromInventoryComponent.of(itemComponent), world);
				}
			}
			if (recipe.isPresent() && recipe.get().matches(ItemInventoryFromInventoryComponent.of(itemComponent), world)) {
				limit = recipe.get().getTime();

				double consumed = recipe.get().getEnergyConsumed() / (double) limit;

				ItemStack output = recipe.get().getOutput();

				boolean isEmpty = itemComponent.getStack(0).isEmpty();
				boolean isEqual = ItemStack.areItemsEqual(itemComponent.getStack(0), output) && ItemStack.areTagsEqual(itemComponent.getStack(0), output);

				if (asEnergy().use(consumed) && (isEmpty || isEqual) && itemComponent.getStack(0).getCount() + output.getCount() <= itemComponent.getStack(0).getMaxCount()) {
					if (progress >= limit) {
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
				} else {
					isActive = false;
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
