package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

import com.github.chainmailstudios.astromine.common.item.weapon.Weapon;

@Mixin (GameRenderer.class)
public class GameRendererMixin {
	private static double lastFov = 0;
	private static boolean isTransitioning = false;
	@Shadow @Final private MinecraftClient client;

	@Inject (at = @At ("RETURN"), method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", cancellable = true)
	void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> callbackInformationReturnable) {
		double gameFov = callbackInformationReturnable.getReturnValueD();

		if (MinecraftClient.getInstance().options.keyUse.isPressed()) {
			Item heldItem = MinecraftClient.getInstance().player.getMainHandStack().getItem();


			if (heldItem instanceof Weapon) {
				double weaponFov = ((Weapon) heldItem).getZoom();

				if (gameFov > weaponFov) {
					gameFov = MathHelper.lerp(tickDelta / 10, lastFov, weaponFov);
				} else {
					gameFov = weaponFov;
				}

				isTransitioning = true;
			}
		} else {
			Item heldItem = MinecraftClient.getInstance().player.getMainHandStack().getItem();

			if (heldItem instanceof Weapon) {
				if (isTransitioning && lastFov < gameFov) {
					gameFov = MathHelper.lerp(tickDelta / 10, lastFov, gameFov);
				} else {
					isTransitioning = false;
				}
			}
		}

		callbackInformationReturnable.setReturnValue(gameFov);
		callbackInformationReturnable.cancel();

		lastFov = gameFov;
	}

	@Inject (at = @At (value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"),
	         method = "renderHand(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/Camera;F)V")
	void onRenderWorld(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo callbackInformation) {
		if (MinecraftClient.getInstance().options.keyUse.isPressed()) {
			Item heldItem = MinecraftClient.getInstance().player.getMainHandStack().getItem();

			if (heldItem instanceof Weapon) {
				Weapon weapon = (Weapon) heldItem;

				Vector3f translation = weapon.getTranslation();

				matrices.translate(translation.getX(), translation.getY(), translation.getZ());
			}
		}
	}
}
