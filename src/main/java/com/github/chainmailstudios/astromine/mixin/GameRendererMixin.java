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

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	private static double lastFov = 0;
	private static boolean isTransitioning = false;
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(at = @At("RETURN"), method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", cancellable = true)
	void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> callbackInformationReturnable) {
		final double gameFov = callbackInformationReturnable.getReturnValueD();
		double newFov = gameFov;

		if (MinecraftClient.getInstance().player == null)
			return;

		if (MinecraftClient.getInstance().options.keyUse.isPressed()) {
			Item heldItem = MinecraftClient.getInstance().player.getMainHandStack().getItem();

			if (heldItem instanceof Weapon) {
				double weaponFov = ((Weapon) heldItem).getZoom();

				if (newFov > weaponFov) {
					newFov = MathHelper.lerp(tickDelta / 10, lastFov, weaponFov);
				} else {
					newFov = weaponFov;
				}

				isTransitioning = true;
			}
		} else {
			Item heldItem = MinecraftClient.getInstance().player.getMainHandStack().getItem();

			if (heldItem instanceof Weapon) {
				if (isTransitioning && lastFov < newFov) {
					newFov = MathHelper.lerp(tickDelta / 10, lastFov, gameFov);
				} else {
					isTransitioning = false;
				}
			}
		}

		if (newFov != gameFov) {
			callbackInformationReturnable.setReturnValue(newFov);
			lastFov = newFov;
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"), method = "renderHand(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/Camera;F)V")
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
