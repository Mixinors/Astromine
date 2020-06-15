package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BossBarHud.class)
public abstract class BossBarHudMixin extends DrawableHelper {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    @Final
    private static Identifier BAR_TEX;
    private static final Identifier CUSTOM_BAR_TEX = AstromineCommon.identifier("textures/gui/bars.png");

    @Inject(
            method = "renderBossBar",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderCustomBossBar(MatrixStack matrixStack, int i, int j, BossBar bossBar, CallbackInfo ci) {
        if (bossBar instanceof ClientBossBar && ((TranslatableText) bossBar.getName()).getKey().contains("super_space_slim")) {
            this.client.getTextureManager().bindTexture(CUSTOM_BAR_TEX);

            // draw empty background bar
            this.drawTexture(matrixStack, i, j, 0, 0, 185, 12);

            // percentage -> texture width
            int overlayBarWidth = (int) (bossBar.getPercent() * 185.0F);

            // draw overlay
            this.drawTexture(matrixStack, i, j, 0, 12, overlayBarWidth, 12);

            ci.cancel();
        }

        this.client.getTextureManager().bindTexture(BAR_TEX);
    }
}
