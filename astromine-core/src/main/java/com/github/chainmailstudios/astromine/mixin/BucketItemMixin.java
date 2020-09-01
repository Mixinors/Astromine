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

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin {
	@Shadow
	@Final
	private Fluid fluid;

	private static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
		float f = player.pitch;
		float g = player.yaw;
		Vec3d vec3d = player.getCameraPosVec(1.0F);
		float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
		float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
		float j = -MathHelper.cos(-f * 0.017453292F);
		float k = MathHelper.sin(-f * 0.017453292F);
		float l = i * j;
		float n = h * j;
		Vec3d vec3d2 = vec3d.add((double) l * 5.0D, (double) k * 5.0D, (double) n * 5.0D);
		return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
	}

	@Shadow
	public abstract TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand);

	@Inject(at = @At("HEAD"), method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", cancellable = true)
	void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> callbackInformationReturnable) {
		BlockHitResult result = raycast(world, user, fluid == Fluids.EMPTY ? RaycastContext.FluidHandling.SOURCE_ONLY : RaycastContext.FluidHandling.NONE);

		if (result.getType() == HitResult.Type.BLOCK) {
			BlockState state = world.getBlockState(result.getBlockPos());
			Block block = state.getBlock();

			if (block instanceof BlockEntityProvider) {
				BlockEntity attached = world.getBlockEntity(result.getBlockPos());

				if (attached instanceof BlockEntityClientSerializable && !world.isClient) {
					((BlockEntityClientSerializable) attached).sync();
				}

				if (attached != null) {
					if (attached instanceof ComponentProvider) {
						ComponentProvider provider = ComponentProvider.fromBlockEntity(attached);

						FluidInventoryComponent inventory = provider.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

						if (inventory != null && this.fluid != Fluids.EMPTY) {
							FluidVolume bucketVolume = new FluidVolume(this.fluid, Fraction.BUCKET);

							FluidVolume insertableVolume = user.isCreative() ? inventory.getFirstInsertableVolume(fluid, result.getSide()) : inventory.getFirstInsertableVolume(bucketVolume, result.getSide());

							if (insertableVolume != null) {
								insertableVolume.pullVolume(bucketVolume, bucketVolume.getFraction());

								callbackInformationReturnable.setReturnValue(TypedActionResult.success(user.isCreative() ? user.getStackInHand(hand) : new ItemStack(bucketVolume.getFluid().getBucketItem().getRecipeRemainder())));
								callbackInformationReturnable.cancel();
							}

							if (attached instanceof BlockEntityClientSerializable && !world.isClient) {
								((BlockEntityClientSerializable) attached).sync();
							}
						} else if (inventory != null) {
							FluidVolume extractableVolume = inventory.getFirstExtractableVolume(result.getSide());

							if (!extractableVolume.getFluid().matchesType(Fluids.EMPTY) && extractableVolume.hasStored(Fraction.bucket())) {
								FluidVolume bucketVolume = extractableVolume.extractVolume(Fraction.BUCKET);

								user.setStackInHand(hand, new ItemStack(bucketVolume.getFluid().getBucketItem()));

								callbackInformationReturnable.setReturnValue(TypedActionResult.success(user.isCreative() ? user.getStackInHand(hand) : new ItemStack(bucketVolume.getFluid().getBucketItem().getRecipeRemainder())));
								callbackInformationReturnable.cancel();
							}
						}
					}
				}
			}
		}
	}
}
