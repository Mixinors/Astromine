package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.recipe.ElectrolyzingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

import java.util.Optional;

public class ElectrolyzerBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, Tickable {
	public int progress = 0;
	public int limit = 100;

	public boolean shouldTry = true;
	public boolean isActive = false;
	public boolean wasActive = false;

	Optional<ElectrolyzingRecipe> recipe = Optional.empty();

	public ElectrolyzerBlockEntity() {
		super(AstromineBlockEntityTypes.ELECTROLYZER);

		fluidComponent = new SimpleFluidInventoryComponent(2);

		energyComponent.getVolume(0).setSize(new Fraction(32, 1));
		fluidComponent.getVolume(0).setSize(new Fraction(4, 1));
		fluidComponent.getVolume(1).setSize(new Fraction(4, 1));

		fluidComponent.addListener(() -> {
			if (!this.world.isClient() && (!recipe.isPresent() || !recipe.get().tryCrafting(this, false)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(ElectrolyzingRecipe.Type.INSTANCE).values().stream()
						.filter(recipe -> recipe instanceof ElectrolyzingRecipe)
						.filter(recipe -> ((ElectrolyzingRecipe) recipe).tryCrafting(this, false))
						.findFirst();

			shouldTry = true;
		});

		addComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, fluidComponent);
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

		fluidComponent.dispatchConsumers();

		if (shouldTry) {
			if (recipe.isPresent() && recipe.get().tryCrafting(this, false)) {
				limit = recipe.get().getTime();

				boolean isEmpty = fluidComponent.getVolume(1).isEmpty();
				boolean isEqual = fluidComponent.getVolume(1).getFluid() == recipe.get().getOutputFluid();

				if ((isEmpty || isEqual) && energyComponent.getVolume(0).hasStored(recipe.get().getEnergyConsumed()) && fluidComponent.getVolume(1).hasAvailable(recipe.get().getOutputAmount())) {
					if (progress == limit) {
						recipe.get().tryCrafting(this, true);

						recipe = Optional.empty();

						progress = 0;
					} else {
						++progress;
					}

					isActive = true;
				}
			} else {
				shouldTry = false;
				isActive = false;

				recipe = Optional.empty();
			}
		} else {
			progress = 0;
			isActive = false;
		}

		if (isActive && !wasActive) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, true));
		} else if (!isActive && wasActive) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, false));
		}

		wasActive = isActive;
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID || type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public <T extends NetworkType> boolean isBuffer(T type) {
		return true;
	}
}
