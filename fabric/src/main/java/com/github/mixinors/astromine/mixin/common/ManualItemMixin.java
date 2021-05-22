package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.item.ManualItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.api.PatchouliAPI;

@Mixin(ManualItem.class)
public class ManualItemMixin {
	@Inject(at = @At("RETURN"), method = "use", cancellable = true)
	void astromine_use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		if (cir.getReturnValue() == null) {
			PatchouliAPI.get().openBookGUI((ServerPlayerEntity) user, AMCommon.id("manual"));
			
			cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
		}
	}
}
