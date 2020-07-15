package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import spinnery.common.utility.MutablePair;

import java.util.List;
import java.util.Optional;

public class BlockBreakerBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	private Fraction cooldown = Fraction.empty();

	public boolean isActive = false;

	public boolean[] activity = { false, false, false, false, false };

	public BlockBreakerBlockEntity() {
		super(AstromineBlockEntityTypes.BLOCK_BREAKER);

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

				if (targetState.isAir()) {
					isActive = false;
					break start;
				}

				BlockEntity targetEntity = world.getBlockEntity(targetPos);

				List<ItemStack> drops = Block.getDroppedStacks(targetState, (ServerWorld) world, targetPos, targetEntity);

				final ItemStack hack = stored.copy();

				Optional<ItemStack> matching = drops.stream().filter(stack -> hack.isEmpty() || StackUtilities.equalItemAndTag(stack, hack)).findFirst();

				if (matching.isPresent()) {
					ItemStack match = matching.get();
					MutablePair<ItemStack, ItemStack> pair = StackUtilities.merge(match, stored, match.getMaxCount(), stored.getMaxCount());
					itemComponent.setStack(0, pair.getSecond());
					drops.remove(match);
					drops.add(pair.getFirst());
				}

				for (ItemStack stack : drops) {
					if (stack.isEmpty()) continue;
					ItemScatterer.spawn(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), stack);
				}

				world.breakBlock(targetPos, false);

				energyVolume.extractVolume(Fraction.of(1 ,8));
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
