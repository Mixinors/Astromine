package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import spinnery.common.utility.MutablePair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BlockBreakerBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	private Fraction cooldown = Fraction.empty();

	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	public BlockBreakerBlockEntity() {
		super(AstromineBlockEntityTypes.BLOCK_BREAKER);
	}

	@Override
	protected double getEnergySize() {
		return AstromineConfig.get().blockBreakerEnergy;
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(1);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient()) return;
		start:
		if (this.world != null && !this.world.isClient()) {
			if (asEnergy().getEnergy() < AstromineConfig.get().blockBreakerEnergyConsumed) {
				cooldown.resetToEmpty();
				isActive = false;
				break start;
			}

			isActive = true;

			cooldown.add(Fraction.of(1, AstromineConfig.get().blockBreakerTimeConsumed));
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

				asEnergy().extract(AstromineConfig.get().blockBreakerEnergyConsumed);
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
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		cooldown = Fraction.fromTag(tag.getCompound("cooldown"));
		super.fromTag(state, tag);
	}

	@Override
	protected @NotNull Map<NetworkType, Collection<NetworkMemberType>> createMemberProperties() {
		return ofTypes(AstromineNetworkTypes.ENERGY, REQUESTER);
	}
}
