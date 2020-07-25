package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.Item;

import com.github.chainmailstudios.astromine.registry.AstromineTags;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @Inject(method = "acceptsForBarter(Lnet/minecraft/item/Item;)Z", at = @At("RETURN"), cancellable = true)
    private static void acceptsForBarterInject(Item item, CallbackInfoReturnable<Boolean> cir) {
        if(item.isIn(AstromineTags.PIGLIN_BARTERING_ITEMS)) cir.setReturnValue(true);
    }
}
