/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.block;

import com.github.mixinors.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.HoloBridgeProjectorBlockEntity;
import com.github.mixinors.astromine.common.util.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class HoloBridgeProjectorBlock extends HorizontalFacingBlockWithEntity {
	public HoloBridgeProjectorBlock(BlockBehaviour.Properties settings) {
		super(settings);
	}
	
	@Override
	public SavedData getSavedDataForDroppedItem() {
		return new SavedData(false, false, false, false);
	}
	
	@Override
	protected ItemInteractionResult useItemOn(net.minecraft.world.item.ItemStack stack, BlockState state, Level world, BlockPos position, Player player, InteractionHand hand, BlockHitResult hit) {
		var changeColor = false;
		Color color = null;
		
		if (stack.getItem() instanceof DyeItem dye) {
			changeColor = true;
			var dyeColor = dye.getDyeColor().getTextureDiffuseColor();
			color = new Color(((dyeColor >> 16) & 0xFF) / 255.0F, ((dyeColor >> 8) & 0xFF) / 255.0F, (dyeColor & 0xFF) / 255.0F, 0x7E);
		} else if (stack.getItem() == Items.WATER_BUCKET) {
			changeColor = true;
			color = HoloBridgeProjectorBlockEntity.DEFAULT_COLOR;
		}
		
		if (changeColor) {
			var originalEntity = (HoloBridgeProjectorBlockEntity) world.getBlockEntity(position);
			
			for (var entity : new HoloBridgeProjectorBlockEntity[] { originalEntity.getChild(), originalEntity, originalEntity.getParent() }) {
				if (entity != null) {
					entity.color = color;
					
					entity.setChanged();
					
					if (!world.isClientSide) {
						entity.syncData();
					}
					
					if (entity.hasChild()) {
						entity.getChild().color = color;
						
						entity.getChild().setChanged();
						
						if (!world.isClientSide) {
							entity.getChild().syncData();
						}
					}
					
					if (!player.isCreative()) {
						stack.shrink(1);
					}
				}
			}
			
			return ItemInteractionResult.sidedSuccess(world.isClientSide);
		}
		
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
	
	@Override
	public boolean hasScreenHandler() {
		return false;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HoloBridgeProjectorBlockEntity(pos, state);
	}
	
	@Override
	public AbstractContainerMenu createScreenHandler(BlockState state, Level world, BlockPos pos, int syncId, Inventory playerInventory, Player player) {
		return null;
	}
	
	@Override
	public void populateScreenHandlerBuffer(BlockState state, Level world, BlockPos pos, ServerPlayer player, FriendlyByteBuf buffer) {}
	
	@Override
	public boolean saveTagToDroppedItem() {
		return false;
	}
}
