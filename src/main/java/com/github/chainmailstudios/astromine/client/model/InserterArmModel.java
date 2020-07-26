package com.github.chainmailstudios.astromine.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class InserterArmModel extends Model {
	private final ModelPart lowerArm;
	private final ModelPart middleArm;
	private final ModelPart topArm;

	public InserterArmModel() {
		super(RenderLayer::getEntitySolid);
		textureWidth = 16;
		textureHeight = 16;

		lowerArm = new ModelPart(this);
		lowerArm.setPivot(0.0F, 23.0F, 0.0F);
		lowerArm.setTextureOffset(0, 0).addCuboid(0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		lowerArm.setTextureOffset(4, 0).addCuboid(-1.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

		middleArm = new ModelPart(this);
		middleArm.setPivot(0.0F, -7.0F, 0.0F);
		lowerArm.addChild(middleArm);
		middleArm.setTextureOffset(8, 0).addCuboid(-0.5F, -6.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);

		topArm = new ModelPart(this);
		topArm.setPivot(0.0F, -5.0F, 0.0F);
		middleArm.addChild(topArm);
		topArm.setTextureOffset(0, 13).addCuboid(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
		topArm.setTextureOffset(8, 13).addCuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		topArm.setTextureOffset(0, 11).addCuboid(-1.5F, -3.0F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		topArm.setTextureOffset(8, 10).addCuboid(1.5F, -5.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		topArm.setTextureOffset(12, 13).addCuboid(-2.5F, -5.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
	}

	public ModelPart getLowerArm() {
		return lowerArm;
	}

	public ModelPart getMiddleArm() {
		return middleArm;
	}

	public ModelPart getTopArm() {
		return topArm;
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		lowerArm.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart ModelPart, float x, float y, float z) {
		ModelPart.pitch = x;
		ModelPart.yaw = y;
		ModelPart.roll = z;
	}
}