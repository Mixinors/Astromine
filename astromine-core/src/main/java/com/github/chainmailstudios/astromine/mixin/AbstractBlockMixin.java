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

package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.TransferComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class AbstractBlockMixin {
	@SuppressWarnings("all")
	@Inject(at = @At("HEAD"),
	        method = "use",
	        cancellable = true)
	void astromine_onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir) {
		final ItemStack stack = player.getItemInHand(hand);

		final Item stackItem = stack.getItem();

		final boolean isBucket = stackItem instanceof BucketItem;

		final boolean isFluidVolumeItem = stackItem instanceof FluidVolumeItem;

		final FluidComponent stackFluidComponent = FluidComponent.get(stack);

		if (state.getBlock().isEntityBlock()) {
			TransferComponent transferComponent = TransferComponent.get(world.getBlockEntity(pos));

			if (transferComponent != null && transferComponent.hasFluid()) {
				TransferType type = transferComponent.getFluid(result.getDirection());

				if (!type.canInsert() && !type.canExtract()) {
					return;
				}
			}
		}

		boolean shouldSkip = false;

		if (stackFluidComponent != null) {
			final Block block = state.getBlock();

			if (block.isEntityBlock()) {
				final BlockEntity blockEntity = world.getBlockEntity(pos);

				FluidComponent blockEntityFluidComponent = FluidComponent.get(blockEntity);

				if (blockEntityFluidComponent != null) {
					FluidVolume stackVolume = stackFluidComponent.getFirst();

					if (stackVolume.isEmpty()) {
						FluidVolume extractable = blockEntityFluidComponent.getFirstExtractableVolume(result.getDirection());

						if (isBucket && extractable != null) {
							if (extractable.hasStored(Fraction.BUCKET)) {
								if (stack.getCount() == 1 || (player.inventory.getFreeSlot() == -1 && stack.getCount() == 1)) {
									stackVolume.take(extractable, Fraction.BUCKET);
									player.setItemInHand(hand, new ItemStack(stackVolume.getFluid().getBucket()));
								} else if (player.inventory.getFreeSlot() != -1 && stack.getCount() > 1) {
									stackVolume.take(extractable, Fraction.BUCKET);
									stack.shrink(1);
									player.addItem(new ItemStack(stackVolume.getFluid().getBucket()));
								}
							}
						} else if (extractable != null) {
							stackVolume.take(extractable, Fraction.BUCKET);
						}
					} else {
						FluidVolume insertable = blockEntityFluidComponent.getFirstInsertableVolume(result.getDirection(), stackVolume);

						if (isBucket && insertable != null) {
							if (insertable.hasAvailable(Fraction.BUCKET)) {
								if (stack.getCount() == 1 || (player.inventory.getFreeSlot() == -1 && stack.getCount() == 1)) {
									insertable.take(stackVolume, Fraction.BUCKET);
									if (!player.isCreative()) {
										player.setItemInHand(hand, new ItemStack(Items.BUCKET));
									}
								} else if (player.inventory.getFreeSlot() != -1 && stack.getCount() > 1) {
									insertable.take(stackVolume, Fraction.BUCKET);
									if (!player.isCreative()) {
										stack.shrink(1);
										player.addItem(new ItemStack(Items.BUCKET));
									}
								}
							} else if (insertable != null) {
								insertable.take(stackVolume, Fraction.BUCKET);
							}
						} else {
						}
					}

					shouldSkip = true;
				}
			}
		}

		if (shouldSkip) {
			cir.setReturnValue(InteractionResult.SUCCESS);
		}
	}
}
