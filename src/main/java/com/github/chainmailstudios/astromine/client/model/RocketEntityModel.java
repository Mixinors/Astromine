package com.github.chainmailstudios.astromine.client.model;

import com.github.chainmailstudios.astromine.common.entity.RocketEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class RocketEntityModel extends EntityModel<RocketEntity> {
	private final ModelPart model;

	public RocketEntityModel() {
		textureWidth = 128;
		textureHeight = 128;

		model = new ModelPart(this);

		model.setPivot(0.0F, 24.0F, 0.0F);

		model.addCuboid("rocket", -6.0F, -11.0F, -6.0F, 12, 11, 12, 0.0F, 48, 0);
		model.addCuboid("rocket", -5.0F, -18.0F, -5.0F, 10, 7, 10, 0.0F, 78, 84);
		model.addCuboid("rocket", -4.0F, -21.0F, -4.0F, 8, 3, 8, 0.0F, 84, 0);
		model.addCuboid("rocket", -6.0F, -106.0F, -6.0F, 12, 86, 12, 0.0F, 0, 0);
		model.addCuboid("rocket", -5.0F, -142.0F, -5.0F, 10, 36, 10, 0.0F, 48, 48);
		model.addCuboid("rocket", -4.0F, -161.0F, -4.0F, 8, 19, 8, 0.0F, 78, 23);
		model.addCuboid("rocket", -2.0F, -180.0F, -2.0F, 4, 19, 4, 0.0F, 0, 98);
		model.addCuboid("rocket", -1.0F, -33.0F, 6.0F, 2, 15, 9, 0.0F, 88, 50);
		model.addCuboid("rocket", -1.0F, -33.0F, -15.0F, 2, 15, 9, 0.0F, 48, 23);
		model.addCuboid("rocket", -1.0F, -48.0F, 6.0F, 2, 15, 5, 0.0F, 70, 101);
		model.addCuboid("rocket", -1.0F, -48.0F, -11.0F, 2, 15, 5, 0.0F, 84, 101);
		model.addCuboid("rocket", -15.0F, -33.0F, -1.0F, 9, 15, 2, 0.0F, 16, 98);
		model.addCuboid("rocket", 6.0F, -33.0F, -1.0F, 9, 15, 2, 0.0F, 48, 94);
		model.addCuboid("rocket", -11.0F, -48.0F, -1.0F, 5, 15, 2, 0.0F, 98, 101);
		model.addCuboid("rocket", 6.0F, -48.0F, -1.0F, 5, 15, 2, 0.0F, 102, 11);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}

	@Override
	public void setAngles(RocketEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		model.render(matrices, vertices, light, overlay);
	}
}