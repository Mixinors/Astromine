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

package com.github.chainmailstudios.astromine.client.render.sprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class SpriteRenderer {
	public static RenderPass beginPass() {
		return new RenderPass();
	}

	public static class RenderPass {
		float x1 = 0;
		float x2 = 1;
		float y1 = 0;
		float y2 = 0;
		float z1 = 0;
		float uStart = 0F;
		float uEnd = 1F;
		float vStart = 0F;
		float vEnd = 1F;
		int u = 0;
		int v = 1;
		int r = 0xff;
		int g = 0xff;
		int b = 0xff;
		int a = 0xff;
		int l = 0;
		float nX = 0;
		float nY = 0;
		float nZ = 0;
		Sprite sprite;
		VertexConsumer consumer;
		VertexConsumerProvider consumers;
		MatrixStack matrices;
		Matrix4f model;
		Matrix3f normal;
		RenderLayer layer;

		private RenderPass() {}

		public RenderPass setup(VertexConsumerProvider consumers, RenderLayer layer) {
			this.consumers = consumers;
			this.setup(consumers.getBuffer(layer), layer);

			return this;
		}

		public RenderPass setup(VertexConsumer consumer, RenderLayer layer) {
			this.consumer = consumer;
			this.matrices = new MatrixStack();
			this.layer = layer;

			return this;
		}

		public RenderPass setup(VertexConsumerProvider consumers, MatrixStack matrices, RenderLayer layer) {
			this.consumers = consumers;
			this.consumer = consumers.getBuffer(layer);
			this.matrices = matrices;
			this.layer = layer;

			return this;
		}

		public RenderPass position(Matrix4f model, float x1, float y1, float x2, float y2, float z1) {
			this.position(x1, y1, x2, y2, z1);
			this.model = model;

			return this;
		}

		public RenderPass position(float x1, float y1, float x2, float y2, float z1) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.z1 = z1;

			return this;
		}

		public RenderPass sprite(Sprite sprite) {
			this.uStart = sprite.getMinU();
			this.uEnd = sprite.getMaxU();
			this.vStart = sprite.getMinV();
			this.vEnd = sprite.getMaxV();

			this.sprite = sprite;

			return this;
		}

		public RenderPass sprite(float uStart, float uEnd, float vStart, float vEnd) {
			this.uStart = uStart;
			this.uEnd = uEnd;
			this.vStart = vStart;
			this.vEnd = vEnd;

			return this;
		}

		public RenderPass overlay(int uv) {
			return this.overlay(uv & '\uffff', uv >> 16 & '\uffff');
		}

		public RenderPass overlay(int u, int v) {
			this.u = u;
			this.v = v;

			return this;
		}

		public RenderPass color(int color) {
			this.r = ((color >> 16) & 0xFF);
			this.g = ((color >> 8) & 0xFF);
			this.b = (color & 0xFF);

			return this;
		}

		public RenderPass color(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;

			return this;
		}

		public RenderPass alpha(int a) {
			this.a = a;

			return this;
		}

		public RenderPass light(int l) {
			this.l = l;

			return this;
		}

		public RenderPass normal(Matrix3f normal, float nX, float nY, float nZ) {
			this.normal(nX, nY, nZ);
			this.normal = normal;

			return this;
		}

		public RenderPass normal(float nX, float nY, float nZ) {
			this.nX = nX;
			this.nY = nY;
			this.nZ = nZ;

			return this;
		}

		public void next() {
			if (this.sprite == null) {
				throw new RuntimeException("Invalid Sprite!");
			}

			next(sprite.getId());
		}

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
				this.model = this.matrices.peek().getModel();
			}
			if (this.normal == null) {
				this.normal = this.matrices.peek().getNormal();
			}

			float sX = sprite.getWidth();
			float sY = sprite.getHeight();

			MinecraftClient.getInstance().getTextureManager().bindTexture(texture);

			for (float y = y1; y < y2; y += Math.min(y2 - y, sY)) {
				for (float x = x1; x < x2; x += Math.min(x2 - x, sX)) {
					float nSX = Math.min(x2 - x, sX);
					float nSY = Math.min(y2 - y, sY);

					boolean isOverX = nSX < sX;
					boolean isOverY = nSY < sY;

					float dX = 0;
					float dY = 0;

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
