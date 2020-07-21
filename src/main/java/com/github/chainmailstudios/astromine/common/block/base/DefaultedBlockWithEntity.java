package com.github.chainmailstudios.astromine.common.block.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public abstract class DefaultedBlockWithEntity extends Block implements BlockEntityProvider {
	protected DefaultedBlockWithEntity(AbstractBlock.Settings settings) {
		super(settings);
	}

	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		super.onSyncedBlockEvent(state, world, pos, type, data);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
		super.appendProperties(builder);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(ACTIVE, false);
	}

	public static void markActive(World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE, true));
	}

	public static void markInactive(World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE, false));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);

		CompoundTag tag = itemStack.getOrCreateTag();

		if (tag.contains("tracker")) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			BlockPos oldPos = blockEntity.getPos();
			blockEntity.fromTag(state, itemStack.getOrCreateTag());
			blockEntity.setPos(oldPos);
		}
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		player.incrementStat(Stats.MINED.getOrCreateStat(this));
		player.addExhaustion(0.005F);

		boolean isTagged = false;

		if (world instanceof ServerWorld) {
			for (ItemStack drop : getDroppedStacks(state, (ServerWorld) world, pos, blockEntity, player, stack)) {
				if (!isTagged && drop.getItem() == asItem()) {
					drop.setTag(blockEntity.toTag(drop.getOrCreateTag()));
					drop.getTag().putByte("tracker", (byte) 0);
					isTagged = true;
				}

				dropStack(world, pos, drop);
			}
		}

		state.onStacksDropped(world, pos, stack);
	}
}
