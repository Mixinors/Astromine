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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import com.mojang.blaze3d.systems.RenderSystem;

import com.github.chainmailstudios.astromine.client.render.sky.skybox.Skybox;

import com.google.common.collect.ImmutableMap;

public class SpaceSkybox extends Skybox {
	public static final Identifier UP = new Identifier("skybox", "up");
	public static final Identifier DOWN = new Identifier("skybox", "down");
	public static final Identifier WEST = new Identifier("skybox", "west");
	public static final Identifier EAST = new Identifier("skybox", "east");
	public static final Identifier NORTH = new Identifier("skybox", "north");
	public static final Identifier SOUTH = new Identifier("skybox", "south");

	public static final Identifier PLANET = new Identifier("skybox", "planet");
	public static final Identifier CLOUD = new Identifier("skybox", "cloud");

	public static float u0C = 0.0f;
	public static float u1C = 1.0f;

	public static float u0P = 0.0f;
	public static float u1P = 1.0f;

	public ImmutableMap<Identifier, Identifier> textures;

	private SpaceSkybox(Builder builder) {
		this.textures = builder.textures.build();

		if (this.textures.size() != 8) {
			throw new UnsupportedOperationException("Skybox constructed without necessary information!");
		}
	}

	@Override
	public void render(MatrixStack matrices, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();

		TextureManager textureManager = client.getTextureManager();

		Tessellator tessellator = Tessellator.getInstance();

		BufferBuilder buffer = tessellator.getBuffer();

		World world = client.world;

		if (world == null) {
			return;
		}

		float rotation = (world.getTimeOfDay() / 12000f) * 360;

		int rawLight = (int) ((world.getTimeOfDay() / 12000) % 15);

		int vertexLight = 0x00f000f0 >> 2 | rawLight >> 3 | rawLight;

		for (int i = 0; i < 6; ++i) {
			matrices.push();

			switch (i) {
				case 0: {
					textureManager.bindTexture(this.textures.get(DOWN));

					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));

					break;
				}
				case 1: {
					textureManager.bindTexture(this.textures.get(WEST));

					matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
					matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));

					break;
				}
				case 2: {
					textureManager.bindTexture(this.textures.get(EAST));

					matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
					matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(rotation));

					break;
				}
				case 3: {
					textureManager.bindTexture(this.textures.get(UP));

					matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
					matrices.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(rotation));

					break;
				}
				case 4: {
					textureManager.bindTexture(this.textures.get(NORTH));

					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));

					break;
				}
				case 5: {
					textureManager.bindTexture(this.textures.get(SOUTH));

					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
					matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));

					break;
				}
			}

			buffer.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

			GameOptions options = MinecraftClient.getInstance().options;

			float distance = 16F * (float) Option.RENDER_DISTANCE.get(options) - 8F;

			buffer.vertex(matrices.peek().getModel(), -distance, -distance, -distance).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(vertexLight).next();
			buffer.vertex(matrices.peek().getModel(), -distance, -distance, distance).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(vertexLight).next();
			buffer.vertex(matrices.peek().getModel(), distance, -distance, distance).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(vertexLight).next();
			buffer.vertex(matrices.peek().getModel(), distance, -distance, -distance).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(vertexLight).next();

			tessellator.draw();

			matrices.pop();
		}

		textureManager.bindTexture(this.textures.get(PLANET));

		matrices.push();

		buffer.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

		buffer.vertex(matrices.peek().getModel(), -100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), -100.0F).color(255, 255, 255, 255).texture(u0P, 0.0F).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), -100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), 100.0F).color(255, 255, 255, 255).texture(u0P, 1.0F).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), 100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), 100.0F).color(255, 255, 255, 255).texture(u1P, 1.0F).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), 100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), -100.0F).color(255, 255, 255, 255).texture(u1P, 0.0F).light(vertexLight).next();

		tessellator.draw();

		matrices.pop();

		textureManager.bindTexture(this.textures.get(CLOUD));

		matrices.push();

		buffer.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();

		buffer.vertex(matrices.peek().getModel(), -100.0F, (float) (-60.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), -100.0F).color(255, 255, 255, 255).texture(u0C, 0).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), -100.0F, (float) (-60.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), 100.0F).color(255, 255, 255, 255).texture(u0C, 1).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), 100.0F, (float) (-60.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), 100.0F).color(255, 255, 255, 255).texture(u1C, 1).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), 100.0F, (float) (-60.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY())), -100.0F).color(255, 255, 255, 255).texture(u1C, 0).light(vertexLight).next();

		tessellator.draw();

		RenderSystem.disableBlend();
		RenderSystem.disableDepthTest();

		matrices.pop();

		u0C -= 0.00001f;
		u1C -= 0.00001f;

		u0P -= 0.0000066f;
		u1P -= 0.0000066f;
	}

	public static class Builder {
		ImmutableMap.Builder<Identifier, Identifier> textures = ImmutableMap.builder();

		public Builder() {

		}

		public Builder up(Identifier up) {
			this.textures.put(UP, up);
			return this;
		}

		public Builder down(Identifier down) {
			this.textures.put(DOWN, down);
			return this;
		}

		public Builder east(Identifier east) {
			this.textures.put(EAST, east);
			return this;
		}

		public Builder west(Identifier west) {
			this.textures.put(WEST, west);
			return this;
		}

		public Builder north(Identifier north) {
			this.textures.put(NORTH, north);
			return this;
		}

		public Builder south(Identifier south) {
			this.textures.put(SOUTH, south);
			return this;
		}

		public Builder planet(Identifier planet) {
			this.textures.put(PLANET, planet);
			return this;
		}

		public Builder cloud(Identifier cloud) {
			this.textures.put(CLOUD, cloud);
			return this;
		}

		public SpaceSkybox build() {
			return new SpaceSkybox(this);
		}
	}
}
