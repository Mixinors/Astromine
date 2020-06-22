package com.github.chainmailstudios.astromine.client.render.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.model.SuperSpaceSlimeEntityModel;
import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SuperSpaceSlimeEntityRenderer extends MobEntityRenderer<SuperSpaceSlimeEntity, SuperSpaceSlimeEntityModel> {

	private static final Identifier TEXTURE = AstromineCommon.identifier("textures/entity/space_slime/space_slime.png");
	private static final Identifier EXPLODING_TEXTURE = AstromineCommon.identifier("textures/entity/space_slime/space_slime_exploding.png");

	public SuperSpaceSlimeEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new SuperSpaceSlimeEntityModel(16), 0.25F);
		this.addFeature(new SlimeOverlayFeatureRenderer(this));
	}

	@Override
	public void render(SuperSpaceSlimeEntity slimeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		this.shadowRadius = 2.5f;
		super.render(slimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public void scale(SuperSpaceSlimeEntity slimeEntity, MatrixStack matrixStack, float f) {
		float scale = 0.999F;
		matrixStack.scale(scale, scale, scale);
		matrixStack.translate(0.0D, -0.525D, 0.0D);

		// calculate stretch slime size
		float slimeSize = 10f;
		float i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (slimeSize * 0.5F + 1.0F);
		float j = 1.0F / (i + 1.0F);

		// scale matrix based on slime size
		matrixStack.scale(j * slimeSize, 1.0F / j * slimeSize, j * slimeSize);

		// if the slime is exploding, make it quickly scale in and out
		if (slimeEntity.isExploding()) {
			float sin = 1 + (float) Math.sin(slimeEntity.getExplodingProgress() / 5f) / 10;
			matrixStack.scale(sin, sin, sin);
		}
	}

	/**
	 * Returns the current model texture for this {@link SuperSpaceSlimeEntityRenderer}.
	 *
	 * <p>The texture defaults to purple, but returns as red if {@link SuperSpaceSlimeEntity#isExploding}.
	 *
	 * @param slimeEntity Super Space Slime to return texture for
	 * @return texture of entity, accounting for current phase
	 */
	@Override
	public Identifier getTexture(SuperSpaceSlimeEntity slimeEntity) {
		return slimeEntity.isExploding() ? EXPLODING_TEXTURE : TEXTURE;
	}
}
