package com.github.chainmailstudios.astromine.client.render.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.entity.projectile.BulletEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BulletEntityRenderer extends ProjectileEntityRenderer<BulletEntity> {
	public static final Identifier TEXTURE = new Identifier(AstromineCommon.MOD_ID, "textures/entity/projectiles/bullet.png");

	public BulletEntityRenderer(final EntityRenderDispatcher dispatcher, final EntityRendererRegistry.Context context) {
		super(dispatcher);
	}

	@Override
	public Identifier getTexture(BulletEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(BulletEntity persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}
}
