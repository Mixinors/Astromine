package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.item.weapon.BaseWeapon;
import com.github.chainmailstudios.astromine.registry.AstromineCommonPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	public Screen currentScreen;

	@Shadow
	@Final
	public GameOptions options;

	@Shadow
	public ClientPlayerEntity player;

	@Shadow
	public ClientWorld world;

	@Shadow
	protected int attackCooldown;

	@Inject(at = @At("HEAD"), method = "handleInputEvents()V", cancellable = true)
	void onHandleInputEvents(CallbackInfo callbackInformation) {
		if (this.currentScreen == null && this.options.keyAttack.isPressed()) {
			if (this.player.getMainHandStack().getItem() instanceof BaseWeapon) {
				BaseWeapon weapon = (BaseWeapon) this.player.getMainHandStack().getItem();

				long currentShot = System.currentTimeMillis();

				if (!weapon.isWaiting(currentShot)) {
					weapon.tryShoot(this.world, this.player);

					ClientSidePacketRegistry.INSTANCE.sendToServer(AstromineCommonPackets.SHOT_PACKET, new PacketByteBuf(Unpooled.buffer()));

					weapon.setLastShot(currentShot);
				}

				this.attackCooldown = 64;

				callbackInformation.cancel();
			}
		}
	}
}
