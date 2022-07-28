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

package com.github.mixinors.astromine.client.render.skybox;

import com.github.mixinors.astromine.client.render.skybox.base.AbstractSkybox;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record Skybox(
		Identifier id,
		
		Texture up,
		Texture down,
		Texture north,
		Texture south,
		Texture east,
		Texture west
) {
	public static final Codec<Skybox> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Identifier.CODEC.fieldOf("id").forGetter(Skybox::id),
					
					Texture.CODEC.fieldOf("up").forGetter(Skybox::up),
					Texture.CODEC.fieldOf("down").forGetter(Skybox::down),
					Texture.CODEC.fieldOf("west").forGetter(Skybox::west),
					Texture.CODEC.fieldOf("east").forGetter(Skybox::east),
					Texture.CODEC.fieldOf("north").forGetter(Skybox::north),
					Texture.CODEC.fieldOf("south").forGetter(Skybox::south)
			).apply(instance, Skybox::new)
	);
	
	public record Texture(
			Identifier id,
			@Nullable Identifier topOverlayId,
			@Nullable Identifier bottomOverlayId
	) {
		public static final Codec<Texture> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Identifier.CODEC.fieldOf("id").forGetter(Texture::id),
						
						Identifier.CODEC.optionalFieldOf("topOverlayId").forGetter(texture -> Optional.ofNullable(texture.topOverlayId)),
						Identifier.CODEC.optionalFieldOf("bottomOverlayId").forGetter(texture -> Optional.ofNullable(texture.bottomOverlayId))
				).apply(instance, (id, topOverlayId, bottomOverlayId) -> new Texture(id, topOverlayId.orElse(null), bottomOverlayId.orElse(null)))
		);
	}
	
	private static float u0C = 0.0F;
	private static float u1C = 1.0F;
	
	private static float u0P = 0.0F;
	private static float u1P = 1.0F;
	
	public void render(MatrixStack matrices, float tickDelta) {
		var client = InstanceUtil.getClient();
		
		var tessellator = Tessellator.getInstance();
		
		var builder = tessellator.getBuffer();
		
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
		
		RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapShader);
		
		var world = client.world;
		
		if (world == null) {
			return;
		}
		
		var rotation = (world.getTimeOfDay() / 12000.0F) * 360.0F;
		
		var rawLight = (int) ((world.getTimeOfDay() / 12000.0F) % 15);
		
		var vertexLight = 0x00f000f0 >> 2 | rawLight >> 3 | rawLight;
		
		for (var i = 0; i < 6; ++i) {
			matrices.push();
			
			switch (i) {
				case 0 -> {
					RenderSystem.setShaderTexture(0, down.id);
					
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotation));
				}
				case 1 -> {
					RenderSystem.setShaderTexture(0, west.id);
					
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
				}
				case 2 -> {
					RenderSystem.setShaderTexture(0, east.id);
					
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
					matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(rotation));
				}
				case 3 -> {
					RenderSystem.setShaderTexture(0, up.id);
					
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F));
					matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(rotation));
				}
				case 4 -> {
					RenderSystem.setShaderTexture(0, north.id);
					
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotation));
				}
				case 5 -> {
					RenderSystem.setShaderTexture(0, south.id);
					
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotation));
				}
			}
			
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
			
			var options = client.options;
			
			var distance = 16.0F * (float) Option.RENDER_DISTANCE.get(options) - 8F;
			
			builder.vertex(matrices.peek().getPositionMatrix(), -distance, -distance, -distance).color(1.0F, 1.0F, 1.0F, 1.0F).texture(0.0F, 0.0F).light(vertexLight).next();
			builder.vertex(matrices.peek().getPositionMatrix(), -distance, -distance, distance).color(1.0F, 1.0F, 1.0F, 1.0F).texture(0.0F, 1.0F).light(vertexLight).next();
			builder.vertex(matrices.peek().getPositionMatrix(), distance, -distance, distance).color(1.0F, 1.0F, 1.0F, 1.0F).texture(1.0F, 1.0F).light(vertexLight).next();
			builder.vertex(matrices.peek().getPositionMatrix(), distance, -distance, -distance).color(1.0F, 1.0F, 1.0F, 1.0F).texture(1.0F, 0.0F).light(vertexLight).next();
			
			tessellator.draw();
			
			matrices.pop();
		}
		
		var lerpPlayerY = MathHelper.lerp(tickDelta, client.player.prevY, client.player.getY());
		
		if (up.topOverlayId != null ||
			up.bottomOverlayId != null ||
			east.topOverlayId != null ||
			east.bottomOverlayId != null ||
			west.topOverlayId != null ||
			west.bottomOverlayId != null ||
			north.topOverlayId != null ||
			north.bottomOverlayId != null ||
			south.topOverlayId != null ||
			south.bottomOverlayId != null) {
			throw new RuntimeException("Up, East, West, North and South overlay textures are not supported yet");
		}
		
		if (down.bottomOverlayId != null) {
			RenderSystem.setShaderTexture(0, down.bottomOverlayId);
			
			matrices.push();
			
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
			
			builder.vertex(matrices.peek().getPositionMatrix(), -100.0F, (float) (-64.0F - (lerpPlayerY)), -100.0F).color(255, 255, 255, 255).texture(u0P, 0.0F).light(vertexLight).next();
			builder.vertex(matrices.peek().getPositionMatrix(), -100.0F, (float) (-64.0F - (lerpPlayerY)), 100.0F).color(255, 255, 255, 255).texture(u0P, 1.0F).light(vertexLight).next();
			builder.vertex(matrices.peek().getPositionMatrix(), 100.0F, (float) (-64.0F - (lerpPlayerY)), 100.0F).color(255, 255, 255, 255).texture(u1P, 1.0F).light(vertexLight).next();
			builder.vertex(matrices.peek().getPositionMatrix(), 100.0F, (float) (-64.0F - (lerpPlayerY)), -100.0F).color(255, 255, 255, 255).texture(u1P, 0.0F).light(vertexLight).next();
			
			tessellator.draw();
			
			matrices.pop();
		}
		
		if (down.topOverlayId != null) {
			RenderSystem.setShaderTexture(0, down.topOverlayId);
			
			matrices.push();
			
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
			
			RenderSystem.enableBlend();
			RenderSystem.enableDepthTest();
			
			builder.vertex(matrices.peek().getPositionMatrix(), -100.0F, (float) (-60.0F - (lerpPlayerY)), -100.0F).color(255, 255, 255, 255).texture(u0C, 0).light(vertexLight).next();
			builder.vertex(matrices.peek().getPositionMatrix(), -100.0F, (float) (-60.0F - (lerpPlayerY)), 100.0F).color(255, 255, 255, 255).texture(u0C, 1).light(vertexLight).next();
			builder.vertex(matrices.peek().getPositionMatrix(), 100.0F, (float) (-60.0F - (lerpPlayerY)), 100.0F).color(255, 255, 255, 255).texture(u1C, 1).light(vertexLight).next();
			builder.vertex(matrices.peek().getPositionMatrix(), 100.0F, (float) (-60.0F - (lerpPlayerY)), -100.0F).color(255, 255, 255, 255).texture(u1C, 0).light(vertexLight).next();
			
			tessellator.draw();
			
			RenderSystem.disableBlend();
			RenderSystem.disableDepthTest();
			
			matrices.pop();
		}
		
		u0C -= 0.00001F;
		u1C -= 0.00001F;
		
		u0P -= 0.0000066F;
		u1P -= 0.0000066F;
		
		RenderSystem.depthMask(true);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}
}
