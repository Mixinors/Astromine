package com.github.mixinors.astromine.common.block.rocket;

import com.github.mixinors.astromine.common.manager.RocketManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RocketDoorBlock extends HorizontalFacingBlock {
	private static final BooleanProperty TOP = BooleanProperty.of("top");
	
	public RocketDoorBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		var blockPos = ctx.getBlockPos();
		var world = ctx.getWorld();
		
		if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
			return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing()).with(TOP, false);
		} else {
			return null;
		}
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.setBlockState(pos.up(), state.with(TOP, true).with(FACING, state.get(FACING)), 3);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var x = pos.getX();
		x = x - (x % 32);
		var z = pos.getZ();
		z = z - (z % 32);
		
		var chunkPos = new ChunkPos(x,z);
		
		var rocket = RocketManager.get(chunkPos);
		
		if (rocket == null) {
			// TODO: Remove after debugging. Or leave, because it's useful if the Rocket is corrupted.
			rocket = RocketManager.create(player.getUuid(), UUID.randomUUID());
		}
		
		RocketManager.teleportToPlacer(player, rocket.getUuid());
		
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		var downPos = pos.down();
		var downState = world.getBlockState(downPos);
		
		return !state.get(TOP) ? downState.isSideSolidFullSquare(world, downPos, Direction.UP) : downState.isOf(this);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		
		builder.add(FACING).add(TOP);
	}
}
