package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.recipe.FluidMixingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class FluidMixerBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, RecipeConsumer, Tickable {
	public int current = 0;
	public int limit = 100;

	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	private Optional<FluidMixingRecipe> recipe = Optional.empty();

	private static final int FIRST_INPUT_FLUID_VOLUME = 0;
	private static final int SECOND_INPUT_FLUID_VOLUME = 1;
	private static final int OUTPUT_FLUID_VOLUME = 2;

	public FluidMixerBlockEntity(BlockEntityType<?> type) {
		super(type);

		fluidComponent.getVolume(FIRST_INPUT_FLUID_VOLUME).setSize(new Fraction(4, 1));
		fluidComponent.getVolume(SECOND_INPUT_FLUID_VOLUME).setSize(new Fraction(4, 1));
		fluidComponent.getVolume(OUTPUT_FLUID_VOLUME).setSize(new Fraction(4, 1));
	}

	abstract int getMachineSpeed();

	abstract Fraction getTankSize();

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(3).withListener((inv) -> {
			if (this.world != null && !this.world.isClient() && (!recipe.isPresent() || !recipe.get().canCraft(this)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(FluidMixingRecipe.Type.INSTANCE).values().stream()
						.filter(recipe -> recipe instanceof FluidMixingRecipe)
						.filter(recipe -> ((FluidMixingRecipe) recipe).canCraft(this))
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
		return ofTypes(AstromineNetworkTypes.FLUID, REQUESTER_PROVIDER, AstromineNetworkTypes.ENERGY, REQUESTER);
	}

	public static class Primitive extends FluidMixerBlockEntity {
		public Primitive() {
			super(AstromineBlockEntityTypes.PRIMITIVE_FLUID_MIXER);
		}

		@Override
		int getMachineSpeed() {
			return 1;
		}

		@Override
		protected int getEnergySize() {
			return 2048;
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(4, 1);
		}
	}

	public static class Basic extends FluidMixerBlockEntity {
		public Basic() {
			super(AstromineBlockEntityTypes.BASIC_FLUID_MIXER);
		}

		@Override
		public int getMachineSpeed() {
			return 2;
		}

		@Override
		protected int getEnergySize() {
			return 8192;
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(8, 1);
		}
	}

	public static class Advanced extends FluidMixerBlockEntity {
		public Advanced() {
			super(AstromineBlockEntityTypes.ADVANCED_FLUID_MIXER);
		}

		@Override
		public int getMachineSpeed() {
			return 4;
		}

		@Override
		protected int getEnergySize() {
			return 32767;
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(16, 1);
		}
	}

	public static class Elite extends FluidMixerBlockEntity {
		public Elite() {
			super(AstromineBlockEntityTypes.ELITE_FLUID_MIXER);
		}

		@Override
		int getMachineSpeed() {
			return 8;
		}

		@Override
		protected int getEnergySize() {
			return 131068;
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(64, 1);
		}
	}
}
