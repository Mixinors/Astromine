package com.github.chainmailstudios.astromine.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class SpriteRenderer {
    public static RenderPass beginPass() {
        return new RenderPass();
    }

    public static class RenderPass {
        int x1 = 0;
        int x2 = 1;
        int y1 = 0;
        int y2 = 0;
        int z1 = 0;
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
        int nX = 0;
        int nY = 0;
        int nZ = 0;
        Sprite sprite;
        VertexConsumer consumer;
        VertexConsumerProvider consumers;
        MatrixStack matrices;
        Matrix4f model;
        Matrix3f normal;
        RenderLayer layer;

        private RenderPass() {
        }

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

        public RenderPass position(Matrix4f model, int x1, int y1, int x2, int y2, int z1) {
            this.position(x1, y1, x2, y2, z1);
            this.model = model;

            return this;
        }

        public RenderPass position(int x1, int y1, int x2, int y2, int z1) {
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

        public RenderPass sprite(int uStart, int uEnd, int vStart, int vEnd) {
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

        public RenderPass normal(Matrix3f normal, int nX, int nY, int nZ) {
            this.normal(nX, nY, nZ);
            this.normal = normal;

            return this;
        }

        public RenderPass normal(int nX, int nY, int nZ) {
            this.nX = nX;
            this.nY = nY;
            this.nZ = nZ;

            return this;
        }

        public void next() {
            if (this.consumer == null) throw new RuntimeException("Invalid VertexConsumer!");
            if (this.matrices == null) throw new RuntimeException("Invalid MatrixStack!");
            if (this.sprite == null) throw new RuntimeException("Invalid Sprite!");

            if (this.model == null) this.model = this.matrices.peek().getModel();
            if (this.normal == null) this.normal = this.matrices.peek().getNormal();

            MinecraftClient.getInstance().getTextureManager().bindTexture(this.sprite.getId());

            this.consumer.vertex(this.model, this.x1, this.y2, this.z1).color(this.r, this.g, this.b, this.a).texture(this.uStart, this.vEnd).overlay(this.u, this.v).light(this.l).normal(this.normal, this.nX, this.nY, this.nZ).next();
            this.consumer.vertex(this.model, this.x2, this.y2, this.z1).color(this.r, this.g, this.b, this.a).texture(this.uEnd, this.vEnd).overlay(this.u, this.v).light(this.l).normal(this.normal, this.nX, this.nY, this.nZ).next();
            this.consumer.vertex(this.model, this.x2, this.y1, this.z1).color(this.r, this.g, this.b, this.a).texture(this.uEnd, this.vStart).overlay(this.u, this.v).light(this.l).normal(this.normal, this.nX, this.nY, this.nZ).next();
            this.consumer.vertex(this.model, this.x1, this.y1, this.z1).color(this.r, this.g, this.b, this.a).texture(this.uStart, this.vStart).overlay(this.u, this.v).light(this.l).normal(this.normal, this.nX, this.nY, this.nZ).next();

            if (this.consumers instanceof VertexConsumerProvider.Immediate) {
                ((VertexConsumerProvider.Immediate) this.consumers).draw(this.layer);
            }
        }
    }
}