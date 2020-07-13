package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.block.entity.EnergyEmitter;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.recipe.LiquidGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.SolidGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import java.util.Optional;

public class SolidGeneratorBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, RecipeConsumer, Tickable {
	public int current = 0;
	public int limit = 100;

	public boolean isActive = false;

	public boolean[] activity = { false, false, false, false, false };

	private Optional<SolidGeneratingRecipe> recipe = Optional.empty();

	private static final int INPUT_ENERGY_VOLUME = 0;

	public SolidGeneratorBlockEntity() {
		super(AstromineBlockEntityTypes.SOLID_GENERATOR);
		
		energyComponent.getVolume(INPUT_ENERGY_VOLUME).setSize(new Fraction(32, 1));

		itemComponent.addListener(() -> {
			if (this.world != null && !this.world.isClient() && (!recipe.isPresent() || !recipe.get().canCraft(this)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(SolidGeneratingRecipe.Type.INSTANCE).values().stream()
						.filter(recipe -> recipe instanceof SolidGeneratingRecipe)
						.filter(recipe -> ((SolidGeneratingRecipe) recipe).canCraft(this))
						.findFirst();
		});
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

		if (recipe.isPresent()) {
			recipe.get().tick(this);

			if (recipe.isPresent() && !recipe.get().canCraft(this)) {
				recipe = Optional.empty();
			}

			isActive = true;
		} else {
			ItemStack burnStack = itemComponent.getStack(0);

			Integer value = FuelRegistry.INSTANCE.get(burnStack.getItem());

			boolean isFuel = value != null && value > 0;

			if (isFuel) {
				if (current == 0) {
					limit = FuelRegistry.INSTANCE.get(burnStack.getItem());
					++current;
					burnStack.decrement(1);
				}
			}

			if (current > 0 && current <= limit) {
				Fraction produced = new Fraction(1, 128);
				if (energyComponent.getVolume(0).hasAvailable(produced)) {
					++current;
					energyComponent.getVolume(0).pullVolume(EnergyVolume.of(produced), produced);
				}
			} else {
				current = 0;
				limit = 100;
			}

			if (!isFuel && current == 0) {
				isActive = false;
			} else {
				isActive = true;
			}
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

		EnergyEmitter.emit(this, INPUT_ENERGY_VOLUME);
	}
	
	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.ENERGY;
	}
	
	@Override
	public <T extends NetworkType> boolean isBuffer(T type) {
		return true;
	}
}
