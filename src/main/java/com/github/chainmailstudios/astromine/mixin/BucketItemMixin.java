package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.collection.AgnosticIndexedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.collection.AgnosticSidedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin {
	@Shadow @Final private Fluid fluid;

	@Inject(at = @At("HEAD"), method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", cancellable = true)
	void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> callbackInformationReturnable) {
		ItemStack stack = user.getStackInHand(hand);

		BlockHitResult result = rayTrace(world, user, fluid == Fluids.EMPTY ? RayTraceContext.FluidHandling.SOURCE_ONLY : RayTraceContext.FluidHandling.NONE);

		if (result.getType() == HitResult.Type.BLOCK) {
			BlockState state = world.getBlockState(result.getBlockPos());
			Block block = state.getBlock();
			Direction direction = result.getSide();

			if (block instanceof BlockEntityProvider) {
				BlockEntity attached = world.getBlockEntity(result.getBlockPos());

				if (attached instanceof BlockEntityClientSerializable && !world.isClient) {
					((BlockEntityClientSerializable) attached).sync();
				}

				if (attached != null) {
					if (attached instanceof AgnosticSidedVolumeCollection) {
						AgnosticSidedVolumeCollection collection = (AgnosticSidedVolumeCollection) attached;

						if (collection.contains(direction, FluidVolume.TYPE)) {
							FluidVolume volume = collection.get(direction, FluidVolume.TYPE);

							if (fluid != Fluids.EMPTY && volume.getFluid() == fluid && volume.fits(Fraction.BUCKET)) {
								FluidVolume newVolume = volume.give(Fraction.BUCKET);
								volume.setFluid(newVolume.getFluid());
								volume.setFraction(newVolume.getFraction());
								callbackInformationReturnable.setReturnValue(TypedActionResult.success(user.isCreative() ? stack : new ItemStack(Items.BUCKET)));
								callbackInformationReturnable.cancel();
							} else if (fluid != Fluids.EMPTY && volume.getFluid() == Fluids.EMPTY && volume.fits(Fraction.BUCKET)) {
								volume.setFluid(fluid);
								FluidVolume newVolume = volume.give(Fraction.BUCKET);
								volume.setFluid(newVolume.getFluid());
								volume.setFraction(newVolume.getFraction());
								callbackInformationReturnable.setReturnValue(TypedActionResult.success(user.isCreative() ? stack : new ItemStack(Items.BUCKET)));
								callbackInformationReturnable.cancel();
							} else if (fluid == Fluids.EMPTY && !volume.isEmpty() && volume.getFraction().equals(Fraction.BUCKET) || volume.getFraction().isBiggerThan(Fraction.BUCKET)) {
								FluidVolume newVolume = volume.take(Fraction.BUCKET);
								volume.setFluid(newVolume.getFluid());
								volume.setFraction(newVolume.getFraction());
								callbackInformationReturnable.setReturnValue(TypedActionResult.success(new ItemStack(volume.getFluid().getBucketItem())));
								callbackInformationReturnable.cancel();
							}

							if (attached instanceof BlockEntityClientSerializable && !world.isClient) {
								((BlockEntityClientSerializable) attached).sync();
							}
						}
					} else if (attached instanceof AgnosticIndexedVolumeCollection) {
						AgnosticIndexedVolumeCollection collection = (AgnosticIndexedVolumeCollection) attached;

						if (collection.contains(FluidVolume.TYPE)) {
							FluidVolume volume = collection.get(FluidVolume.TYPE);

							if (fluid != Fluids.EMPTY && volume.getFluid() == fluid && volume.fits(Fraction.BUCKET)) {
								FluidVolume newVolume = volume.give(Fraction.BUCKET);
								volume.setFluid(newVolume.getFluid());
								volume.setFraction(newVolume.getFraction());
								callbackInformationReturnable.setReturnValue(TypedActionResult.success(user.isCreative() ? stack : new ItemStack(Items.BUCKET)));
								callbackInformationReturnable.cancel();
							} else if (fluid != Fluids.EMPTY && volume.getFluid() == Fluids.EMPTY && volume.fits(Fraction.BUCKET)) {
								volume.setFluid(fluid);
								FluidVolume newVolume = volume.give(Fraction.BUCKET);
								volume.setFluid(newVolume.getFluid());
								volume.setFraction(newVolume.getFraction());
								callbackInformationReturnable.setReturnValue(TypedActionResult.success(user.isCreative() ? stack : new ItemStack(Items.BUCKET)));
								callbackInformationReturnable.cancel();
							} else if (fluid == Fluids.EMPTY && !volume.isEmpty() && volume.getFraction().equals(Fraction.BUCKET) || volume.getFraction().isBiggerThan(Fraction.BUCKET)) {
								FluidVolume newVolume = volume.take(Fraction.BUCKET);
								volume.setFluid(newVolume.getFluid());
								volume.setFraction(newVolume.getFraction());
								callbackInformationReturnable.setReturnValue(TypedActionResult.success(new ItemStack(volume.getFluid().getBucketItem())));
								callbackInformationReturnable.cancel();
							}

							if (attached instanceof BlockEntityClientSerializable && !world.isClient) {
								((BlockEntityClientSerializable) attached).sync();
							}
						}
					}
				}
			}
		}
	}

	private static BlockHitResult rayTrace(World world, PlayerEntity player, RayTraceContext.FluidHandling fluidHandling) {
		float f = player.pitch;
		float g = player.yaw;
		Vec3d vec3d = player.getCameraPosVec(1.0F);
		float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
		float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
		float j = -MathHelper.cos(-f * 0.017453292F);
		float k = MathHelper.sin(-f * 0.017453292F);
		float l = i * j;
		float n = h * j;
		Vec3d vec3d2 = vec3d.add((double)l * 5.0D, (double)k * 5.0D, (double)n * 5.0D);
		return world.rayTrace(new RayTraceContext(vec3d, vec3d2, RayTraceContext.ShapeType.OUTLINE, fluidHandling, player));
	}

}
