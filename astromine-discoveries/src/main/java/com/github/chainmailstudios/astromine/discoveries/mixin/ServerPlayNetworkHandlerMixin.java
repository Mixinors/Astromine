package com.github.chainmailstudios.astromine.discoveries.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import com.github.chainmailstudios.astromine.discoveries.common.entity.base.RocketEntity;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayPacketListener {
    @Shadow public ServerPlayerEntity player;

    @Inject(method= "onClientCommand(Lnet/minecraft/network/packet/c2s/play/ClientCommandC2SPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;updateLastActionTime()V"))
    public void onClientCommandInject(ClientCommandC2SPacket packet, CallbackInfo ci) {
        if(packet.getMode().equals(ClientCommandC2SPacket.Mode.OPEN_INVENTORY) && this.player.getVehicle() instanceof RocketEntity) { ;
            ((RocketEntity)this.player.getVehicle()).openInventory(this.player);
        }
    }
}
