package com.github.chainmailstudios.astromine.client.render.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.model.RocketEntityModel;
import com.github.chainmailstudios.astromine.common.entity.RocketEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;

public class RocketEntityRenderer extends EntityRenderer<RocketEntity> {
	public static final Identifier identifier = AstromineCommon.identifier("textures/entity/rocket/rocket.png");

	private final RocketEntityModel model = new RocketEntityModel();

	public RocketEntityRenderer(EntityRenderDispatcher dispatcher, final EntityRendererRegistry.Context context) {
		super(dispatcher);
	}

	@Override
	public void render(RocketEntity rocket, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		matrices.push();

		matrices.translate(0.0D, 0.375D, 0.0D);

		matrices.scale(-1.0F, -1.0F, 1.0F);

		matrices.scale(2, 2, 2);

		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

		this.model.setAngles(rocket, 0, 0.0F, -0.1F, rocket.getYaw(tickDelta), rocket.getPitch(tickDelta));

		VertexConsumer vertexConsumer = provider.getBuffer(this.model.getLayer(this.getTexture(rocket)));

		this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

		matrices.pop();

		super.render(rocket, yaw, tickDelta, matrices, provider, light);
	}

	@Override
	public Identifier getTexture(RocketEntity entity) {
		return identifier;
	}
}
