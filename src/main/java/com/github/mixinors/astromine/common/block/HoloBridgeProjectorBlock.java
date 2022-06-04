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
import dev.architectury.hooks.block.BlockEntityHooks;
import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HoloBridgeProjectorBlock extends HorizontalFacingBlockWithEntity {
	public HoloBridgeProjectorBlock(AbstractBlock.Settings settings) {
		super(settings);
	}
	
	@Override
	public SavedData getSavedDataForDroppedItem() {
		return new SavedData(false, false, false, false);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var stack = player.getStackInHand(hand);
		
		var changeColor = false;
		Color color = null;
		
		if (stack.getItem() instanceof DyeItem dye) {
			changeColor = true;
			var dyeColor = dye.getColor().getColorComponents();
			color = new Color(dyeColor[0], dyeColor[1], dyeColor[2], 0x7E);
		} else if (stack.getItem() == Items.WATER_BUCKET) {
			changeColor = true;
			color = HoloBridgeProjectorBlockEntity.DEFAULT_COLOR;
		}
		
		if (changeColor) {
			var originalEntity = (HoloBridgeProjectorBlockEntity) world.getBlockEntity(position);
			
			for (var entity : new HoloBridgeProjectorBlockEntity[] { originalEntity.getChild(), originalEntity, originalEntity.getParent() }) {
				if (entity != null) {
					entity.color = color;
					
					entity.markDirty();
					
					if (!world.isClient) {
						BlockEntityHooks.syncData(entity);
					}
					
					if (entity.hasChild()) {
						entity.getChild().color = color;
						
						entity.getChild().markDirty();
						
						if (!world.isClient) {
							BlockEntityHooks.syncData(entity.getChild());
						}
					}
					
					if (!player.isCreative()) {
						stack.decrement(1);
					}
				}
			}
			
			return ActionResult.PASS;
		}
		
		return ActionResult.PASS;
	}
	
	@Override
	public boolean hasScreenHandler() {
		return false;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new HoloBridgeProjectorBlockEntity(pos, state);
	}
	
	@Override
	public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return null;
	}
	
	@Override
	public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {}
	
	@Override
	public boolean saveTagToDroppedItem() {
		return false;
	}
}
