package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FluidInserterBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, Tickable {
	private Fraction cooldown = Fraction.empty();

	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	public FluidInserterBlockEntity() {
		super(AstromineBlockEntityTypes.FLUID_INSERTER);

		fluidComponent.getVolume(0).setSize(Fraction.ofWhole(4));
		setMaxStoredPower(32000);
	}

	@Override
	public void tick() {
		start:
		if (this.world != null && !this.world.isClient()) {
			if (asEnergy().getEnergy() < 250) {
				cooldown.resetToEmpty();
				isActive = false;
				break start;
			}

			isActive = true;

			cooldown.add(Fraction.of(1, 40));
			cooldown.simplify();
			if (cooldown.isBiggerOrEqualThan(Fraction.ofWhole(1))) {
				cooldown.resetToEmpty();

				FluidVolume fluidVolume = fluidComponent.getVolume(0);

				Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
				BlockPos targetPos = pos.offset(direction);
				BlockState targetState = world.getBlockState(targetPos);

				if (targetState.isAir() && fluidVolume.hasStored(Fraction.bucket())) {
					FluidVolume toInsert = fluidVolume.extractVolume(Fraction.bucket());
					world.setBlockState(targetPos, toInsert.getFluid().getDefaultState().getBlockState());
					asEnergy().extract(250);
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
				}
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

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("cooldown", cooldown.toTag(new CompoundTag()));
		return super.toTag(tag);
	}

	@Override
	public <T extends NetworkType> boolean isProvider(T type) {
		return type == AstromineNetworkTypes.FLUID;
	}

	@Override
	public <T extends NetworkType> boolean isRequester(T type) {
		return type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID || type == AstromineNetworkTypes.ENERGY;
	}
}
