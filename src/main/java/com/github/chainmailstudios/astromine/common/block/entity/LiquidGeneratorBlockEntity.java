package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.LiquidGeneratorBlock;
import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.recipe.LiquidGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class LiquidGeneratorBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, RecipeConsumer, Tickable {
	public double current = 0;
	public int limit = 100;

	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	private Optional<LiquidGeneratingRecipe> recipe = Optional.empty();

	private static final int INPUT_ENERGY_VOLUME = 0;
	private static final int INPUT_FLUID_VOLUME = 0;

	public LiquidGeneratorBlockEntity(BlockEntityType<?> type) {
		super(type);

		fluidComponent.getVolume(INPUT_FLUID_VOLUME).setSize(getTankSize());
	}

	abstract Fraction getTankSize();

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(1).withListener((inv) -> {
			if (this.world != null && !this.world.isClient() && (!recipe.isPresent() || !recipe.get().canCraft(this)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(LiquidGeneratingRecipe.Type.INSTANCE).values().stream()
						.filter(recipe -> recipe instanceof LiquidGeneratingRecipe)
						.filter(recipe -> ((LiquidGeneratingRecipe) recipe).canCraft(this))
						.findFirst();
		});
	}

	@Override
	public double getCurrent() {
		return current;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setCurrent(double current) {
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
	public void increment() {
		this.current += 1 * ((LiquidGeneratorBlock) this.getCachedState().getBlock()).getMachineSpeed();
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
		super.tick();

		if (world.isClient()) return;

		if (recipe.isPresent()) {
			recipe.get().tick(this);

			if (recipe.isPresent() && !recipe.get().canCraft(this)) {
				recipe = Optional.empty();
			}

			isActive = true;
		} else {
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

	@Override
	protected @NotNull Map<NetworkType, Collection<NetworkMemberType>> createMemberProperties() {
		return ofTypes(AstromineNetworkTypes.FLUID, REQUESTER, AstromineNetworkTypes.ENERGY, PROVIDER);
	}

	public static class Primitive extends LiquidGeneratorBlockEntity {
		public Primitive() {
			super(AstromineBlockEntityTypes.PRIMITIVE_LIQUID_GENERATOR);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().primitiveLiquidGeneratorEnergy;
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().primitiveLiquidGeneratorFluid, 1);
		}
	}

	public static class Basic extends LiquidGeneratorBlockEntity {
		public Basic() {
			super(AstromineBlockEntityTypes.BASIC_LIQUID_GENERATOR);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().basicLiquidGeneratorEnergy;
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().basicLiquidGeneratorFluid, 1);
		}
	}

	public static class Advanced extends LiquidGeneratorBlockEntity {
		public Advanced() {
			super(AstromineBlockEntityTypes.ADVANCED_LIQUID_GENERATOR);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().advancedLiquidGeneratorEnergy;
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().advancedLiquidGeneratorFluid, 1);
		}
	}

	public static class Elite extends LiquidGeneratorBlockEntity {
		public Elite() {
			super(AstromineBlockEntityTypes.ELITE_LIQUID_GENERATOR);
		}

		@Override
		protected double getEnergySize() {
			return AstromineConfig.get().eliteLiquidGeneratorEnergy;
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().eliteLiquidGeneratorFluid, 1);
		}
	}
}
