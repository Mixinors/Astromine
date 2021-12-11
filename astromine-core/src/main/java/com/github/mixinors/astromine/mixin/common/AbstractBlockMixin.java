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

package com.github.mixinors.astromine.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.block.transfer.TransferType;
import com.github.mixinors.astromine.common.component.block.entity.TransferComponent;
import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.item.base.FluidVolumeItem;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
	@SuppressWarnings("all")
	@Inject(at = @At("HEAD"),
		method = "onUse(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", cancellable = true)
	void astromine_onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result, CallbackInfoReturnable<ActionResult> cir) {
		final ItemStack stack = player.getStackInHand(hand);

		final Item stackItem = stack.getItem();

		final boolean isBucket = stackItem instanceof BucketItem;

		final boolean isFluidVolumeItem = stackItem instanceof FluidVolumeItem;

		final FluidComponent stackFluidComponent = FluidComponent.get(stack);
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (blockEntity != null) {
			TransferComponent transferComponent = TransferComponent.get(blockEntity);

			if (transferComponent != null && transferComponent.hasFluid()) {
				TransferType type = transferComponent.getFluid(result.getSide());

				if (!type.canInsert() && !type.canExtract()) {
					return;
				}
			}
		}

		boolean shouldSkip = false;

		if (stackFluidComponent != null) {
			final Block block = state.getBlock();

			if (blockEntity != null) {
				FluidComponent blockEntityFluidComponent = FluidComponent.get(blockEntity);

				if (blockEntityFluidComponent != null) {
					FluidVolume stackVolume = stackFluidComponent.getFirst();

					if (stackVolume.isEmpty()) {
						FluidVolume extractable = blockEntityFluidComponent.getFirstExtractableVolume(result.getSide());

						if (isBucket && extractable != null) {
							if (extractable.hasStored(FluidVolume.BUCKET)) {
								if (stack.getCount() == 1 || (player.getInventory().getEmptySlot() == -1 && stack.getCount() == 1)) {
									stackVolume.take(extractable, FluidVolume.BUCKET);
									player.setStackInHand(hand, new ItemStack(stackVolume.getFluid().getBucketItem()));
								} else if (player.getInventory().getEmptySlot() != -1 && stack.getCount() > 1) {
									stackVolume.take(extractable, FluidVolume.BUCKET);
									stack.decrement(1);
									player.giveItemStack(new ItemStack(stackVolume.getFluid().getBucketItem()));
								}
							}
						} else if (extractable != null) {
							stackVolume.take(extractable, FluidVolume.BUCKET);
						}
					} else {
						FluidVolume insertable = blockEntityFluidComponent.getFirstInsertableVolume(result.getSide(), stackVolume);

						if (isBucket && insertable != null) {
							if (insertable.hasAvailable(FluidVolume.BUCKET)) {
								if (stack.getCount() == 1 || (player.getInventory().getEmptySlot() == -1 && stack.getCount() == 1)) {
									insertable.take(stackVolume, FluidVolume.BUCKET);
									if (!player.isCreative()) {
										player.setStackInHand(hand, new ItemStack(Items.BUCKET));
									}
								} else if (player.getInventory().getEmptySlot() != -1 && stack.getCount() > 1) {
									insertable.take(stackVolume, FluidVolume.BUCKET);
									if (!player.isCreative()) {
										stack.decrement(1);
										player.giveItemStack(new ItemStack(Items.BUCKET));
									}
								}
							} else if (insertable != null) {
								insertable.take(stackVolume, FluidVolume.BUCKET);
							}
						} else {}
					}

					shouldSkip = true;
				}
			}
		}

		if (shouldSkip) {
			cir.setReturnValue(ActionResult.SUCCESS);
		}
	}
}
