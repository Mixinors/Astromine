package com.github.mixinors.astromine.common.block.rocket;

import com.github.mixinors.astromine.common.manager.RocketManager;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class RocketDoorBlock extends HorizontalDirectionalBlock {
	private static final BooleanProperty TOP = BooleanProperty.create("top");
	
	public RocketDoorBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return MapCodec.unit(this);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		var blockPos = ctx.getClickedPos();
		var world = ctx.getLevel();
		
		if (blockPos.getY() < world.getMaxBuildHeight() - 1 && world.getBlockState(blockPos.above()).canBeReplaced(ctx)) {
			return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(TOP, false);
		} else {
			return null;
		}
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.setBlock(pos.above(), state.setValue(TOP, true).setValue(FACING, state.getValue(FACING)), 3);
	}
	
	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!(world instanceof ServerLevel serverLevel)) {
			return ItemInteractionResult.sidedSuccess(world.isClientSide);
		}
		
		var chunkPos = RocketManager.getInteriorBaseChunk(pos);
		var rocket = RocketManager.get(serverLevel.getServer(), chunkPos);
		
		if (rocket == null) {
			return ItemInteractionResult.FAIL;
		}
		
		if (!RocketManager.teleportToPlacer(player, rocket.getUuid())) {
			return ItemInteractionResult.FAIL;
		}
		
		return ItemInteractionResult.sidedSuccess(world.isClientSide);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		var downPos = pos.below();
		var downState = world.getBlockState(downPos);
		
		return !state.getValue(TOP) ? downState.isFaceSturdy(world, downPos, Direction.UP) : downState.is(this);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		
		builder.add(FACING).add(TOP);
	}
}
