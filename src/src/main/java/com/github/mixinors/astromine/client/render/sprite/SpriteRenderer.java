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

package com.github.mixinors.astromine.client.render.sprite;

import dev.vini2003.hammer.core.api.client.util.InstanceUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

// TODO: Migrate to Hammer GUI!

/**
 * An advanced sprite renderer, which does processing in render passes.
 */
@Environment(EnvType.CLIENT)
public class SpriteRenderer {
	/** Instantiates a {@link RenderPass}. */
	public static RenderPass beginPass() {
		return new RenderPass();
	}
	
	/**
	 * A render pass, storing information used for rendering sprites.
	 */
	public static class RenderPass {
		private float x1 = 0.0F;
		private float x2 = 0.0F;
		
		private float y1 = 0.0F;
		private float y2 = 0.0F;
		
		private float z1 = 0.0F;
		
		private float uStart = 0.0F;
		private float uEnd = 1.0F;
		
		private float vStart = 0.0F;
		private float vEnd = 1.0F;
		
		private int u = 0;
		private int v = 1;
		
		private int r = 0xff;
		private int g = 0xff;
		private int b = 0xff;
		private int a = 0xff;
		
		private int l = 0;
		
		private float nX = 0.0F;
		private float nY = 0.0F;
		private float nZ = 0.0F;
		
		Sprite sprite;
		
		VertexConsumer consumer;
		
		VertexConsumerProvider consumers;
		
		MatrixStack matrices;
		
		Matrix4f model;
		
		Matrix3f normal;
		
		RenderLayer layer;
		
		/**
		 * We only want {@link #beginPass()} to be able to instantiate a {@link RenderPass}.
		 */
		private RenderPass() {}
		
		/**
		 * Sets the {@link VertexConsumer} of this pass, acquiring it from a {@link VertexConsumerProvider} for the given {@link RenderLayer}, and following up with {@link #setup(VertexConsumer, RenderLayer)}.
		 */
		public RenderPass setup(VertexConsumerProvider consumers, RenderLayer layer) {
			this.consumers = consumers;
			
			setup(consumers.getBuffer(layer), layer);
			
			return this;
		}
		
		/**
		 * Sets the {@link VertexConsumer} of this pass, alongside its {@link RenderLayer}, while also instantiating a new {@link MatrixStack}.
		 */
		public RenderPass setup(VertexConsumer consumer, RenderLayer layer) {
			this.consumer = consumer;
			this.matrices = new MatrixStack();
			this.layer = layer;
			
			return this;
		}
		
		/**
		 * Sets the {@link VertexConsumer} of this pass, acquiring it from a {@link VertexConsumerProvider} for the given {@link RenderLayer}, then storing it and the specified {@link MatrixStack}.
		 */
		public RenderPass setup(VertexConsumerProvider consumers, MatrixStack matrices, RenderLayer layer) {
			this.consumers = consumers;
			this.consumer = consumers.getBuffer(layer);
			this.matrices = matrices;
			this.layer = layer;
			
			return this;
		}
		
		/**
		 * Sets the position of this pass, storing the given model {@link Matrix4f}.
		 */
		public RenderPass position(Matrix4f model, float x1, float y1, float x2, float y2, float z1) {
			this.model = model;
			
			position(x1, y1, x2, y2, z1);
			
			return this;
		}
		
		/** Sets the position of this pass. */
		public RenderPass position(float x1, float y1, float x2, float y2, float z1) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.z1 = z1;
			
			return this;
		}
		
		/**
		 * Sets the sprite of this pass, and following up with {@link #sprite(float, float, float, float)}.
		 */
		public RenderPass sprite(Sprite sprite) {
			this.sprite = sprite;
			
			sprite(sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
			
			return this;
		}
		
		/** Sets the texture mapping coordinates of this pass. */
		public RenderPass sprite(float uStart, float uEnd, float vStart, float vEnd) {
			this.uStart = uStart;
			this.uEnd = uEnd;
			this.vStart = vStart;
			this.vEnd = vEnd;
			
			return this;
		}
		
		/**
		 * Sets the overlay of this pass with {@link #overlay(int, int)}.
		 */
		public RenderPass overlay(int uv) {
			overlay(uv & '\uffff', uv >> 16 & '\uffff');
			
			return this;
		}
		
		/** Sets the overlay of this pass. */
		public RenderPass overlay(int u, int v) {
			this.u = u;
			this.v = v;
			
			return this;
		}
		
		/** Sets the color of this pass. */
		public RenderPass color(int color) {
			this.r = ((color >> 16) & 0xFF);
			this.g = ((color >> 8) & 0xFF);
			this.b = (color & 0xFF);
			
			return this;
		}
		
		/** Sets the color of this pass. */
		public RenderPass color(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
			
			return this;
		}
		
		/** Sets the alpha of this pass. */
		public RenderPass alpha(int a) {
			this.a = a;
			
			return this;
		}
		
		/** Sets the alpha of this pass. */
		public RenderPass light(int l) {
			this.l = l;
			
			return this;
		}
		
		/**
		 * Sets the normal of this pass, storing the given {@link Matrix3f}, and following up with {@link #normal(float, float, float)}.
		 */
		public RenderPass normal(Matrix3f normal, float nX, float nY, float nZ) {
			this.normal = normal;
			
			normal(nX, nY, nZ);
			
			return this;
		}
		
		/** Sets the normal of this pass. */
		public RenderPass normal(float nX, float nY, float nZ) {
			this.nX = nX;
			this.nY = nY;
			this.nZ = nZ;
			
			return this;
		}
		
		/** Renders this pass, with {@link #next(Identifier)}. */
		public void next() {
			if (this.sprite == null) {
				throw new RuntimeException("Invalid Sprite!");
			}
			
			next(sprite.getId());
		}
		
		/** Renders this pass. */
		public void next(Identifier texture) {
			if (this.consumer == null) {
				throw new RuntimeException("Invalid VertexConsumer!");
			}
			if (this.matrices == null) {
				throw new RuntimeException("Invalid MatrixStack!");
			}
			if (this.sprite == null) {
				throw new RuntimeException("Invalid Sprite!");
			}
			
			if (this.model == null) {
				this.model = this.matrices.peek().getPositionMatrix();
			}
			if (this.normal == null) {
				this.normal = this.matrices.peek().getNormalMatrix();
			}
			
			var sX = sprite.getWidth();
			var sY = sprite.getHeight();
			
			InstanceUtils.getClient().getTextureManager().bindTexture(texture);
			
			for (var y = y1; y < y2; y += Math.min(y2 - y, sY)) {
				for (var x = x1; x < x2; x += Math.min(x2 - x, sX)) {
					var nSX = Math.min(x2 - x, sX);
					var nSY = Math.min(y2 - y, sY);
					
					var isOverX = nSX < sX;
					var isOverY = nSY < sY;
					
					var dX = 0.0F;
					var dY = 0.0F;
					
					if (isOverX) {
						dX = (uEnd - uStart) * (1 - (nSX / sX));
					}
					
					if (isOverY) {
						dY = (vEnd - vStart) * (1 - (nSY / sY));
					}
					
					this.consumer = consumers.getBuffer(RenderLayer.getSolid());
					
					this.consumer.vertex(this.model, x, y + nSY, z1).color(this.r, this.g, this.b, this.a).texture(this.uStart, this.vEnd - dY).overlay(this.u, this.v).light(this.l).normal(this.normal, this.nX, this.nY, this.nZ).next();
					this.consumer.vertex(this.model, x + nSX, y + nSY, z1).color(this.r, this.g, this.b, this.a).texture(this.uEnd - dX, this.vEnd - dY).overlay(this.u, this.v).light(this.l).normal(this.normal, this.nX, this.nY, this.nZ).next();
					this.consumer.vertex(this.model, x + nSX, y, z1).color(this.r, this.g, this.b, this.a).texture(this.uEnd - dX, this.vStart).overlay(this.u, this.v).light(this.l).normal(this.normal, this.nX, this.nY, this.nZ).next();
					this.consumer.vertex(this.model, x, y, z1).color(this.r, this.g, this.b, this.a).texture(this.uStart, this.vStart).overlay(this.u, this.v).light(this.l).normal(this.normal, this.nX, this.nY, this.nZ).next();
				}
			}
			
		}
	}
}
