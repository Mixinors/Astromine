/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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
import com.github.mixinors.astromine.mixin.common.DyeColorAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.block.entity.HoloBridgeProjectorBlockEntity;
import com.github.vini2003.blade.common.miscellaneous.Color;

public class HoloBridgeProjectorBlock extends HorizontalFacingBlockWithEntity {
	public HoloBridgeProjectorBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var stack = player.getStackInHand(hand);

		if (stack.getItem() instanceof DyeItem dyeItem) {
			
			var originalEntity = (HoloBridgeProjectorBlockEntity) world.getBlockEntity(pos);

			for (var entity : new HoloBridgeProjectorBlockEntity[] { originalEntity.getChild(), originalEntity, originalEntity.getParent() }) {
				if (entity != null) {
					var color = ((DyeColorAccessor) (Object) dyeItem.getColor()).getColor();

					var colorColor = new Color((color >> 16 & 0xFF) / 255F, (color >> 8 & 0xFF) / 255F, (color & 0xFF) / 255F, 0x7E);

					entity.color = colorColor;

					entity.markDirty();

					if (!world.isClient)
						entity.syncData();

					if (entity.hasChild()) {
						entity.getChild().color = colorColor;

						entity.getChild().markDirty();

						if (!world.isClient) {
							entity.getChild().syncData();
						}
					}

					if (!player.isCreative()) {
						stack.decrement(1);
					}
				}
			}
		}

		return ActionResult.PASS;
	}

	@Override
	public BlockEntity createBlockEntity() {
		return new HoloBridgeProjectorBlockEntity();
	}

	@Override
	public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buf) {}

	@Override
	protected boolean saveTagToDroppedItem() {
		return false;
	}
}
