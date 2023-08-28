package com.github.mixinors.astromine.common.block.rocket;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.rocket.RocketControllerBlockEntity;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.screen.handler.rocket.RocketControllerScreenHandler;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RocketControllerBlock extends HorizontalFacingBlockWithEntity {
	public RocketControllerBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public SavedData getSavedDataForDroppedItem() {
		return MACHINE;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RocketControllerBlockEntity(pos, state);
	}
	
	@Override
	public boolean hasScreenHandler() {
		return true;
	}
	
	@Override
	public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		if (!world.isClient()) {
			RocketManager.sync(world.getServer());
		}
		
		var rocketController = (RocketControllerBlockEntity) world.getBlockEntity(pos);
		if (rocketController != null) {
			if (((RocketControllerBlockEntity) world.getBlockEntity(pos)).getRocket() != null) {
				return new RocketControllerScreenHandler(syncId, playerInventory.player, pos);
			}
		}
		return null;
	}
	
	@Override
	public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);
		
		var blockEntity = world.getBlockEntity(pos);
		
		if (!placer.getWorld().isClient())
			RocketManager.sync(placer.getServer());
		
		if (blockEntity instanceof RocketControllerBlockEntity && world.getDimensionKey().equals(AMWorlds.ROCKET_INTERIORS_DIMENSION_TYPE_KEY)) {
			var controllerBlockEntity = ((RocketControllerBlockEntity) blockEntity);
			if (controllerBlockEntity.getRocket() == null) {
				//RocketManager.create(placer.getUuid(), UUID.randomUUID());
			}
		}
	}
}
