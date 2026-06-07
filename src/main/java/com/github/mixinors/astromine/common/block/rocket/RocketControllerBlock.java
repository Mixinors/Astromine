package com.github.mixinors.astromine.common.block.rocket;

import com.github.mixinors.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.rocket.RocketControllerBlockEntity;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.screen.handler.rocket.RocketControllerScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RocketControllerBlock extends HorizontalFacingBlockWithEntity {
	public RocketControllerBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public SavedData getSavedDataForDroppedItem() {
		return MACHINE;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RocketControllerBlockEntity(pos, state);
	}
	
	@Override
	public boolean hasScreenHandler() {
		return true;
	}
	
	@Override
	public AbstractContainerMenu createScreenHandler(BlockState state, Level world, BlockPos pos, int syncId, Inventory playerInventory, Player player) {
		return new RocketControllerScreenHandler(syncId, playerInventory.player, pos);
	}
	
	@Override
	public void populateScreenHandlerBuffer(BlockState state, Level world, BlockPos pos, ServerPlayer player, FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(world, pos, state, placer, stack);
		
		var blockEntity = world.getBlockEntity(pos);
		
		if (blockEntity instanceof RocketControllerBlockEntity && world instanceof ServerLevel serverLevel) {
			RocketManager.create(serverLevel.getServer(), placer.getUUID(), UUID.randomUUID());
		}
	}
}
