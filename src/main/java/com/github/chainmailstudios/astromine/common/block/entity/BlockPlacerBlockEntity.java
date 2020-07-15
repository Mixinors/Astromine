package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Optional;

public class BlockPlacerBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	private Fraction cooldown = Fraction.empty();

	public boolean isActive = false;

	public boolean[] activity = { false, false, false, false, false };

	public BlockPlacerBlockEntity() {
		super(AstromineBlockEntityTypes.BLOCK_PLACER);

		energyComponent.getVolume(0).setSize(Fraction.ofWhole(32));
	}

	@Override
	public void tick() {
		start: if (this.world != null && !this.world.isClient()) {
			EnergyVolume energyVolume = energyComponent.getVolume(0);

			if (!energyVolume.hasStored(Fraction.of(1, 8))) {
				cooldown.resetToEmpty();
				isActive = false;
				break start;
			}

			isActive = true;

			cooldown.add(Fraction.of(1, 40));
			cooldown.simplify();
			if (cooldown.isBiggerOrEqualThan(Fraction.ofWhole(1))) {
				cooldown.resetToEmpty();

				ItemStack stored = itemComponent.getStack(0);

				Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
				BlockPos targetPos = pos.offset(direction);
				BlockState targetState = world.getBlockState(targetPos);

				if (stored.getItem() instanceof BlockItem && targetState.isAir()) {
					BlockState newState = ((BlockItem) stored.getItem()).getBlock().getDefaultState();
					world.setBlockState(targetPos, newState);
					stored.decrement(1);

					energyVolume.extractVolume(Fraction.of(1 ,8));
				}
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
		return type == AstromineNetworkTypes.ENERGY;
	}
}
