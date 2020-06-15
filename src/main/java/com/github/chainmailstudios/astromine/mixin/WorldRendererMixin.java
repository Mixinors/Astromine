package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.world.AstromineDimensionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	@Final
	private TextureManager textureManager;

	@Unique
	private final Identifier SPACE_SKY_ONE = AstromineCommon.identifier("textures/environment/space_0.png");
	@Unique
	private final Identifier SPACE_SKY_TWO = AstromineCommon.identifier("textures/environment/space_1.png");
	@Unique
	private final Identifier SPACE_SKY_THREE = AstromineCommon.identifier("textures/environment/space_2.png");
	@Unique
	private final Identifier SPACE_SKY_FOUR = AstromineCommon.identifier("textures/environment/space_3.png");
	@Unique
	private final Identifier SPACE_SKY_FIVE = AstromineCommon.identifier("textures/environment/space_4.png");
	@Unique
	private final Identifier SPACE_SKY_SIX = AstromineCommon.identifier("textures/environment/space_5.png");

	private void renderSpaceSky(MatrixStack matrices) {
		RenderSystem.disableAlphaTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(false);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		for (int i = 0; i < 6; ++i) {

			matrices.push();
			if (i == 0) {
				this.textureManager.bindTexture(SPACE_SKY_ONE);
			}

			if (i == 1) {
				this.textureManager.bindTexture(SPACE_SKY_TWO);
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
			}

			if (i == 2) {
				this.textureManager.bindTexture(SPACE_SKY_THREE);
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
			}

			if (i == 3) {
				this.textureManager.bindTexture(SPACE_SKY_FOUR);
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
			}

			if (i == 4) {
				this.textureManager.bindTexture(SPACE_SKY_FIVE);
				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
			}

			if (i == 5) {
				this.textureManager.bindTexture(SPACE_SKY_SIX);
				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
			}

			Matrix4f matrix4f = matrices.peek().getModel();
			bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(255, 255, 255, 255).next();
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 1.0F).color(255, 255, 255, 255).next();
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(1.0F, 1.0F).color(255, 255, 255, 255).next();
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(1.0F, 0.0F).color(255, 255, 255, 255).next();
			tessellator.draw();
			matrices.pop();
		}

		RenderSystem.depthMask(true);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
	}

	@Inject(at = @At("HEAD"), method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V", cancellable = true)
	void onRenderSky(MatrixStack matrices, float tickDelta, CallbackInfo callbackInformation) {
		if (client.world.getDimensionRegistryKey().getValue().equals(AstromineDimensionType.KEY_ID)) {
			renderSpaceSky(matrices);
			callbackInformation.cancel();
		}
	}
}
