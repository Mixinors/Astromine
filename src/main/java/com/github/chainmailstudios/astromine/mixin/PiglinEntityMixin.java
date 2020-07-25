package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;

@Mixin(PiglinEntity.class)
public class PiglinEntityMixin {
    @Inject(method = "equipToOffHand(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    public void equipToOffHandInject(ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() == PiglinBrain.BARTERING_ITEM) {
            ((MobEntity)(Object) this).equipStack(EquipmentSlot.OFFHAND, stack);
            ((MobEntity)(Object) this).updateDropChances(EquipmentSlot.OFFHAND);
            ci.cancel();
        }
    }
}
