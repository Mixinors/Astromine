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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;

@Environment(EnvType.CLIENT)
@Mixin(BossBarHud.class)
public abstract class BossBarHudMixin extends DrawableHelper {

	private static final Identifier CUSTOM_BAR_TEX = AstromineCommon.identifier("textures/gui/bars.png");
	@Shadow
	@Final
	private static Identifier BARS_TEXTURE;
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "renderBossBar", at = @At("HEAD"), cancellable = true)
	private void astromine_renderCustomBossBar(MatrixStack matrices, int i, int j, BossBar bossBar, CallbackInfo ci) {
		if (bossBar instanceof ClientBossBar && bossBar.getName() instanceof TranslatableText && ((TranslatableText) bossBar.getName()).getKey().contains("super_space_slim")) {
			this.client.getTextureManager().bindTexture(CUSTOM_BAR_TEX);

			// draw empty background bar
			this.drawTexture(matrices, i, j, 0, 0, 185, 12);

			// percentage -> texture width
			int overlayBarWidth = (int) (bossBar.getPercent() * 185.0F);

			// draw overlay
			this.drawTexture(matrices, i, j, 0, 12, overlayBarWidth, 12);

			ci.cancel();

			this.client.getTextureManager().bindTexture(BARS_TEXTURE);
		}
	}
}
