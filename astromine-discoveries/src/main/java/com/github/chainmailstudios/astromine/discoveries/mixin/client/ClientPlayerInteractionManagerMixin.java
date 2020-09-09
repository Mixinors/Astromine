package com.github.chainmailstudios.astromine.discoveries.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;

import com.github.chainmailstudios.astromine.discoveries.common.entity.base.RocketEntity;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "hasRidingInventory()Z", at = @At("HEAD"), cancellable = true)
    public void hasRidingInventoryInject(CallbackInfoReturnable<Boolean> cir) {
        if (this.client.player.hasVehicle() && this.client.player.getVehicle() instanceof RocketEntity) cir.setReturnValue(true);
    }
}
