package com.github.chainmailstudios.astromine.client.render;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.render.layer.Layers;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

public class SpaceRenderer {
	private static final Identifier SPACE_SKY_ONE = AstromineCommon.identifier("textures/skybox/space_0.png");
	private static final Identifier SPACE_SKY_TWO = AstromineCommon.identifier("textures/skybox/space_1.png");
	private static final Identifier SPACE_SKY_THREE = AstromineCommon.identifier("textures/skybox/space_2.png");
	private static final Identifier SPACE_SKY_FOUR = AstromineCommon.identifier("textures/skybox/space_3.png");
	private static final Identifier SPACE_SKY_FIVE = AstromineCommon.identifier("textures/skybox/space_4.png");
	private static final Identifier SPACE_SKY_SIX = AstromineCommon.identifier("textures/skybox/space_5.png");

	private static final Identifier EARTH = AstromineCommon.identifier("textures/skybox/earth.png");

	public static void render(MatrixStack matrices, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();

		TextureManager textureManager = client.getTextureManager();

		Tessellator tessellator = Tessellator.getInstance();

		BufferBuilder buffer = tessellator.getBuffer();

		World world = client.world;

		if (world == null) return;

		float rotation = (world.getTimeOfDay() / 12000f) * 360;

		int rawLight = (int) ((world.getTimeOfDay() / 12000) % 15);

		int vertexLight = 0x00f000f0 >> 2 | rawLight >> 3 | rawLight;

		for (int i = 0; i < 6; ++i) {
			matrices.push();

			switch (i) {
				case 0: {
					textureManager.bindTexture(SPACE_SKY_ONE);

					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));

					break;
				}
				case 1: {
					textureManager.bindTexture(SPACE_SKY_TWO);

					matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
					matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));

					break;
				}
				case 2: {
					textureManager.bindTexture(SPACE_SKY_THREE);

					matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
					matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(rotation));

					break;
				}
				case 3: {
					textureManager.bindTexture(SPACE_SKY_FOUR);

					matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
					matrices.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(rotation));

					break;
				}
				case 4: {
					textureManager.bindTexture(SPACE_SKY_FIVE);

					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));

					break;
				}
				case 5: {
					textureManager.bindTexture(SPACE_SKY_SIX);

					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));

					break;
				}
			}


			buffer.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

			buffer.vertex(matrices.peek().getModel(), -100.0F, -100.0F, -100.0F).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(vertexLight).next();
			buffer.vertex(matrices.peek().getModel(), -100.0F, -100.0F, 100.0F).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(vertexLight).next();
			buffer.vertex(matrices.peek().getModel(), 100.0F, -100.0F, 100.0F).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(vertexLight).next();
			buffer.vertex(matrices.peek().getModel(), 100.0F, -100.0F, -100.0F).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(vertexLight).next();

			tessellator.draw();

			matrices.pop();
		}

		textureManager.bindTexture(EARTH);

		matrices.push();

		buffer.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

		buffer.vertex(matrices.peek().getModel(), -100.0F, -100.0F, -100.0F).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), -100.0F, -100.0F, 100.0F).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), 100.0F, -100.0F, 100.0F).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), 100.0F, -100.0F, -100.0F).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(vertexLight).next();

		tessellator.draw();

		matrices.pop();
	}
}
