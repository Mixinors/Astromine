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

package com.github.chainmailstudios.astromine.discoveries.client.render.sky.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import com.github.chainmailstudios.astromine.client.render.sky.skybox.Skybox;

import com.google.common.collect.ImmutableMap;

public class SpaceSkybox extends Skybox {
	public static final ResourceLocation UP = new ResourceLocation("skybox", "up");
	public static final ResourceLocation DOWN = new ResourceLocation("skybox", "down");
	public static final ResourceLocation WEST = new ResourceLocation("skybox", "west");
	public static final ResourceLocation EAST = new ResourceLocation("skybox", "east");
	public static final ResourceLocation NORTH = new ResourceLocation("skybox", "north");
	public static final ResourceLocation SOUTH = new ResourceLocation("skybox", "south");

	public static final ResourceLocation PLANET = new ResourceLocation("skybox", "planet");
	public static final ResourceLocation CLOUD = new ResourceLocation("skybox", "cloud");

	public static float u0C = 0.0f;
	public static float u1C = 1.0f;

	public static float u0P = 0.0f;
	public static float u1P = 1.0f;

	public ImmutableMap<ResourceLocation, ResourceLocation> textures;

	private SpaceSkybox(Builder builder) {
		this.textures = builder.textures.build();

		if (this.textures.size() != 8) {
			throw new UnsupportedOperationException("Skybox constructed without necessary information!");
		}
	}

	@Override
	public void render(PoseStack matrices, float tickDelta) {
		Minecraft client = Minecraft.getInstance();

		TextureManager textureManager = client.getTextureManager();

		Tesselator tessellator = Tesselator.getInstance();

		BufferBuilder buffer = tessellator.getBuilder();

		Level world = client.level;

		if (world == null) {
			return;
		}

		float rotation = (world.getDayTime() / 12000f) * 360;

		int rawLight = (int) ((world.getDayTime() / 12000) % 15);

		int vertexLight = 0x00f000f0 >> 2 | rawLight >> 3 | rawLight;

		for (int i = 0; i < 6; ++i) {
			matrices.pushPose();

			switch (i) {
				case 0: {
					textureManager.bind(this.textures.get(DOWN));

					matrices.mulPose(Vector3f.ZP.rotationDegrees(rotation));

					break;
				}
				case 1: {
					textureManager.bind(this.textures.get(WEST));

					matrices.mulPose(Vector3f.XP.rotationDegrees(90.0F));
					matrices.mulPose(Vector3f.YP.rotationDegrees(rotation));

					break;
				}
				case 2: {
					textureManager.bind(this.textures.get(EAST));

					matrices.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
					matrices.mulPose(Vector3f.YN.rotationDegrees(rotation));

					break;
				}
				case 3: {
					textureManager.bind(this.textures.get(UP));

					matrices.mulPose(Vector3f.XP.rotationDegrees(180.0F));
					matrices.mulPose(Vector3f.ZN.rotationDegrees(rotation));

					break;
				}
				case 4: {
					textureManager.bind(this.textures.get(NORTH));

					matrices.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
					matrices.mulPose(Vector3f.ZP.rotationDegrees(rotation));

					break;
				}
				case 5: {
					textureManager.bind(this.textures.get(SOUTH));

					matrices.mulPose(Vector3f.ZP.rotationDegrees(-90.0F));
					matrices.mulPose(Vector3f.ZP.rotationDegrees(rotation));

					break;
				}
			}

			buffer.begin(7, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

			Options options = Minecraft.getInstance().options;

			float distance = 16F * (float) Option.RENDER_DISTANCE.get(options) - 8F;

			buffer.vertex(matrices.last().pose(), -distance, -distance, -distance).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(vertexLight).endVertex();
			buffer.vertex(matrices.last().pose(), -distance, -distance, distance).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(vertexLight).endVertex();
			buffer.vertex(matrices.last().pose(), distance, -distance, distance).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(vertexLight).endVertex();
			buffer.vertex(matrices.last().pose(), distance, -distance, -distance).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(vertexLight).endVertex();

			tessellator.end();

			matrices.popPose();
		}

		textureManager.bind(this.textures.get(PLANET));

		matrices.pushPose();

		buffer.begin(7, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

		buffer.vertex(matrices.last().pose(), -100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), -100.0F).color(255, 255, 255, 255).uv(u0P, 0.0F).uv2(vertexLight).endVertex();
		buffer.vertex(matrices.last().pose(), -100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), 100.0F).color(255, 255, 255, 255).uv(u0P, 1.0F).uv2(vertexLight).endVertex();
		buffer.vertex(matrices.last().pose(), 100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), 100.0F).color(255, 255, 255, 255).uv(u1P, 1.0F).uv2(vertexLight).endVertex();
		buffer.vertex(matrices.last().pose(), 100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), -100.0F).color(255, 255, 255, 255).uv(u1P, 0.0F).uv2(vertexLight).endVertex();

		tessellator.end();

		matrices.popPose();

		textureManager.bind(this.textures.get(CLOUD));

		matrices.pushPose();

		buffer.begin(7, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();

		buffer.vertex(matrices.last().pose(), -100.0F, (float) (-60.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), -100.0F).color(255, 255, 255, 255).uv(u0C, 0).uv2(vertexLight).endVertex();
		buffer.vertex(matrices.last().pose(), -100.0F, (float) (-60.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), 100.0F).color(255, 255, 255, 255).uv(u0C, 1).uv2(vertexLight).endVertex();
		buffer.vertex(matrices.last().pose(), 100.0F, (float) (-60.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), 100.0F).color(255, 255, 255, 255).uv(u1C, 1).uv2(vertexLight).endVertex();
		buffer.vertex(matrices.last().pose(), 100.0F, (float) (-60.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), -100.0F).color(255, 255, 255, 255).uv(u1C, 0).uv2(vertexLight).endVertex();

		tessellator.end();

		RenderSystem.disableBlend();
		RenderSystem.disableDepthTest();

		matrices.popPose();

		u0C -= 0.00001f;
		u1C -= 0.00001f;

		u0P -= 0.0000066f;
		u1P -= 0.0000066f;
	}

	public static class Builder {
		ImmutableMap.Builder<ResourceLocation, ResourceLocation> textures = ImmutableMap.builder();

		public Builder() {}

		public Builder up(ResourceLocation up) {
			this.textures.put(UP, up);
			return this;
		}

		public Builder down(ResourceLocation down) {
			this.textures.put(DOWN, down);
			return this;
		}

		public Builder east(ResourceLocation east) {
			this.textures.put(EAST, east);
			return this;
		}

		public Builder west(ResourceLocation west) {
			this.textures.put(WEST, west);
			return this;
		}

		public Builder north(ResourceLocation north) {
			this.textures.put(NORTH, north);
			return this;
		}

		public Builder south(ResourceLocation south) {
			this.textures.put(SOUTH, south);
			return this;
		}

		public Builder planet(ResourceLocation planet) {
			this.textures.put(PLANET, planet);
			return this;
		}

		public Builder cloud(ResourceLocation cloud) {
			this.textures.put(CLOUD, cloud);
			return this;
		}

		public SpaceSkybox build() {
			return new SpaceSkybox(this);
		}
	}
}
