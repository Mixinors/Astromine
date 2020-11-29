/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.technologies.common.block;

import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.HolographicBridgeProjectorBlockEntity;
import com.github.vini2003.blade.common.miscellaneous.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class HolographicBridgeProjectorBlock extends WrenchableHorizontalFacingBlockWithEntity {
	public HolographicBridgeProjectorBlock(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos position, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.getItem() instanceof DyeItem) {
			DyeItem dye = (DyeItem) stack.getItem();

			HolographicBridgeProjectorBlockEntity originalEntity = (HolographicBridgeProjectorBlockEntity) world.getBlockEntity(position);

			for (HolographicBridgeProjectorBlockEntity entity : new HolographicBridgeProjectorBlockEntity[] {originalEntity.getChild(), originalEntity, originalEntity.getParent()}) {
				if (entity != null) {
					int color = dye.getDyeColor().textureDiffuseColor;

					Color colorColor = new Color((color >> 16 & 0xFF) / 255F, (color >> 8 & 0xFF) / 255F, (color & 0xFF) / 255F, 0x7E);

					entity.color = colorColor;

					entity.setChanged();

					if (!world.isClientSide)
						entity.sync();

					if (entity.hasChild()) {
						entity.getChild().color = colorColor;

						entity.getChild().setChanged();

						if (!world.isClientSide) {
							entity.getChild().sync();
						}
					}

					if (!player.isCreative()) {
						stack.shrink(1);
					}
				}
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean hasScreenHandler() {
		return false;
	}

	@Override
	public BlockEntity createBlockEntity() {
		return new HolographicBridgeProjectorBlockEntity();
	}

	@Override
	public AbstractContainerMenu createScreenHandler(BlockState state, Level world, BlockPos pos, int syncId, Inventory playerInventory, Player player) {
		return null;
	}

	@Override
	public void populateScreenHandlerBuffer(BlockState state, Level world, BlockPos pos, ServerPlayer player, FriendlyByteBuf buffer) {}

	@Override
	protected boolean saveTagToDroppedItem() {
		return false;
	}
}
