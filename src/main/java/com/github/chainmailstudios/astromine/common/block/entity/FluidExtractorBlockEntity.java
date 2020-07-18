package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

public class FluidExtractorBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, Tickable {
	private Fraction cooldown = Fraction.empty();

	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	public FluidExtractorBlockEntity() {
		super(AstromineBlockEntityTypes.FLUID_EXTRACTOR);

		setMaxStoredPower(32000);
		fluidComponent.getVolume(0).setSize(Fraction.ofWhole(4));
	}

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(1);
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
				FluidState targetFluidState = world.getFluidState(targetPos);

				if (targetFluidState.isStill()) {
					FluidVolume toInsert = new FluidVolume(targetFluidState.getFluid(), Fraction.bucket());
					if (fluidVolume.hasAvailable(Fraction.bucket())) {
						fluidVolume.pullVolume(toInsert, toInsert.getFraction());
						asEnergy().extract(250);

						world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
						world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1, 1);
					}
				}
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
