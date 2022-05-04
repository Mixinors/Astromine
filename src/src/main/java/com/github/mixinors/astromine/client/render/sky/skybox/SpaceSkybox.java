/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.client.render.sky.skybox;

import com.google.common.collect.ImmutableMap;

import com.github.mixinors.astromine.common.util.ClientUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.option.Option;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

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
		textures = builder.textures.build();

        if (textures.size() != 8) {
            throw new UnsupportedOperationException("Skybox constructed without necessary information!");
        }
    }

    @Override
    public void render(MatrixStack matrices, float tickDelta) {
		var client = ClientUtils.getInstance();
	
		var tessellator = Tessellator.getInstance();
	
		var buffer = tessellator.getBuffer();

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();

        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);

        RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapShader);

        World world = client.world;

        if (world == null) {
            return;
        }
	
		var rotation = (world.getTimeOfDay() / 12000f) * 360;
	
		var rawLight = (int) ((world.getTimeOfDay() / 12000) % 15);
	
		var vertexLight = 0x00f000f0 >> 2 | rawLight >> 3 | rawLight;

        for (var i = 0; i < 6; ++i) {
            matrices.push();
			switch (i) {
				case 0 -> {
					RenderSystem.setShaderTexture(0, textures.get(DOWN));
					
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotation));
				}
				case 1 -> {
					RenderSystem.setShaderTexture(0, textures.get(WEST));
					
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
				}
				case 2 -> {
					RenderSystem.setShaderTexture(0, textures.get(EAST));
					
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
					matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(rotation));
				}
				case 3 -> {
					RenderSystem.setShaderTexture(0, textures.get(UP));
					
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F));
					matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(rotation));
				}
				case 4 -> {
					RenderSystem.setShaderTexture(0, textures.get(NORTH));
					
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotation));
				}
				case 5 -> {
					RenderSystem.setShaderTexture(0, textures.get(SOUTH));
					
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotation));
				}
			}

            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
	
			var options = ClientUtils.getInstance().options;
	
			var distance = 16F * (float) Option.RENDER_DISTANCE.get(options) - 8F;

            buffer.vertex(matrices.peek().getPositionMatrix(), -distance, -distance, -distance).color(1F, 1F, 1F, 1F).texture(0.0F, 0.0F).light(vertexLight).next();
            buffer.vertex(matrices.peek().getPositionMatrix(), -distance, -distance, distance).color(1F, 1F, 1F, 1F).texture(0.0F, 1.0F).light(vertexLight).next();
            buffer.vertex(matrices.peek().getPositionMatrix(), distance, -distance, distance).color(1F, 1F, 1F, 1F).texture(1.0F, 1.0F).light(vertexLight).next();
            buffer.vertex(matrices.peek().getPositionMatrix(), distance, -distance, -distance).color(1F, 1F, 1F, 1F).texture(1.0F, 0.0F).light(vertexLight).next();

            tessellator.draw();

            matrices.pop();
        }
		
		RenderSystem.setShaderTexture(0, textures.get(PLANET));

        matrices.push();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

		var lerpPlayerY = MathHelper.lerp(tickDelta, client.player.prevY, client.player.getY());
		
        buffer.vertex(matrices.peek().getPositionMatrix(), -100.0F, (float) (-64.0F - (lerpPlayerY)), -100.0F).color(255, 255, 255, 255).texture(u0P, 0.0F).light(vertexLight).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), -100.0F, (float) (-64.0F - (lerpPlayerY)), 100.0F).color(255, 255, 255, 255).texture(u0P, 1.0F).light(vertexLight).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 100.0F, (float) (-64.0F - (lerpPlayerY)), 100.0F).color(255, 255, 255, 255).texture(u1P, 1.0F).light(vertexLight).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 100.0F, (float) (-64.0F - (lerpPlayerY)), -100.0F).color(255, 255, 255, 255).texture(u1P, 0.0F).light(vertexLight).next();

        tessellator.draw();

        matrices.pop();

        RenderSystem.setShaderTexture(0, textures.get(CLOUD));

        matrices.push();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        buffer.vertex(matrices.peek().getPositionMatrix(), -100.0F, (float) (-60.0F - (lerpPlayerY)), -100.0F).color(255, 255, 255, 255).texture(u0C, 0).light(vertexLight).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), -100.0F, (float) (-60.0F - (lerpPlayerY)), 100.0F).color(255, 255, 255, 255).texture(u0C, 1).light(vertexLight).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 100.0F, (float) (-60.0F - (lerpPlayerY)), 100.0F).color(255, 255, 255, 255).texture(u1C, 1).light(vertexLight).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 100.0F, (float) (-60.0F - (lerpPlayerY)), -100.0F).color(255, 255, 255, 255).texture(u1C, 0).light(vertexLight).next();

        tessellator.draw();

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();

        matrices.pop();

        u0C -= 0.00001f;
        u1C -= 0.00001f;

        u0P -= 0.0000066f;
        u1P -= 0.0000066f;

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static class Builder {
        ImmutableMap.Builder<Identifier, Identifier> textures = ImmutableMap.builder();

        public Builder() {
        }

        public Builder up(Identifier up) {
			textures.put(UP, up);
            return this;
        }

        public Builder down(Identifier down) {
			textures.put(DOWN, down);
            return this;
        }

        public Builder east(Identifier east) {
			textures.put(EAST, east);
            return this;
        }

        public Builder west(Identifier west) {
			textures.put(WEST, west);
            return this;
        }

        public Builder north(Identifier north) {
			textures.put(NORTH, north);
            return this;
        }

        public Builder south(Identifier south) {
			textures.put(SOUTH, south);
            return this;
        }

        public Builder planet(Identifier planet) {
			textures.put(PLANET, planet);
            return this;
        }

        public Builder cloud(Identifier cloud) {
			textures.put(CLOUD, cloud);
            return this;
        }

        public SpaceSkybox build() {
            return new SpaceSkybox(this);
        }
    }
}