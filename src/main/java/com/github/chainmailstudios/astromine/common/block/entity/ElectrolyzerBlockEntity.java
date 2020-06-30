package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import net.minecraft.block.BlockState;
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

public class ElectrolyzerBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, RecipeConsumer, Tickable {
	public int current = 0;
	public int limit = 100;

	public boolean isActive = false;

	private Optional<ElectrolyzingRecipe> recipe = Optional.empty();

	private static final int INPUT_ENERGY_VOLUME = 0;
	private static final int INPUT_FLUID_VOLUME = 0;
	private static final int FIRST_OUTPUT_FLUID_VOLUME = 1;
	private static final int SECOND_OUTPUT_FLUID_VOLUME = 2;

	public ElectrolyzerBlockEntity() {
		super(AstromineBlockEntityTypes.ELECTROLYZER);

		fluidComponent = new SimpleFluidInventoryComponent(3);

		energyComponent.getVolume(INPUT_ENERGY_VOLUME).setSize(new Fraction(32, 1));
		fluidComponent.getVolume(INPUT_FLUID_VOLUME).setSize(new Fraction(4, 1));
		fluidComponent.getVolume(FIRST_OUTPUT_FLUID_VOLUME).setSize(new Fraction(4, 1));
		fluidComponent.getVolume(SECOND_OUTPUT_FLUID_VOLUME).setSize(new Fraction(4, 1));

		fluidComponent.addListener(() -> {
			if (!this.world.isClient() && (!recipe.isPresent() || !recipe.get().canCraft(this)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(ElectrolyzingRecipe.Type.INSTANCE).values().stream()
						.filter(recipe -> recipe instanceof ElectrolyzingRecipe)
						.filter(recipe -> ((ElectrolyzingRecipe) recipe).canCraft(this))
						.findFirst();
		});

		addComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, fluidComponent);
	}

	@Override
	public int getCurrent() {
		return current;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setCurrent(int current) {
		this.current = current;
	}

	@Override
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		readRecipeProgress(tag);
		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		writeRecipeProgress(tag);
		return super.toTag(tag);
	}

	@Override
	public void tick() {
		if (world.isClient()) return;

		// TODO: Fix this; currently no caching happens!
		fluidComponent.dispatchConsumers();

		boolean wasActive = isActive;

		if (recipe.isPresent()) {
			recipe.get().tick(this);

			if (!recipe.get().canCraft(this)) {
				recipe = Optional.empty();
			}
		}

		if (isActive && !wasActive) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, true));
		} else if (!isActive && wasActive) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, false));
		}
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
