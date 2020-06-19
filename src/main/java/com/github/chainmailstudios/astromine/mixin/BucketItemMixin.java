package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.component.FluidInventoryComponent;
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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
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
		BlockHitResult result = rayTrace(world, user, fluid == Fluids.EMPTY ? RayTraceContext.FluidHandling.SOURCE_ONLY : RayTraceContext.FluidHandling.NONE);

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
						ComponentProvider provider = (ComponentProvider) attached;

						FluidInventoryComponent inventory = provider.getComponent(result.getSide(), FluidInventoryComponent.class);

						if (inventory != null) {
							FluidVolume bucketVolume = new FluidVolume(this.fluid, Fraction.BUCKET);

							if (inventory.canInsert(bucketVolume)) {
								inventory.insert(bucketVolume);
								callbackInformationReturnable.setReturnValue(TypedActionResult.success(new ItemStack(bucketVolume.getFluid().getBucketItem().getRecipeRemainder())));
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
