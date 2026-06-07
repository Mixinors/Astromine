package com.github.mixinors.astromine.client.render.block.entity;

import com.github.mixinors.astromine.common.block.HoloBridgeProjectorBlock;
import com.github.mixinors.astromine.common.block.entity.HoloBridgeProjectorBlockEntity;
import com.github.mixinors.astromine.registry.client.AMRenderLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.core.Direction;

public class HoloBridgeBlockEntityRenderer implements BlockEntityRenderer<HoloBridgeProjectorBlockEntity> {
	public HoloBridgeBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}
	
	@Override
	public void render(HoloBridgeProjectorBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light, int overlay) {
		var level = entity.getLevel();
		
		if (level == null) {
			return;
		}
		
		var state = level.getBlockState(entity.getBlockPos());
		
		if (!(state.getBlock() instanceof HoloBridgeProjectorBlock) || !entity.hasChild()) {
			return;
		}
		
		if (entity.segments == null || entity.segments.isEmpty()) {
			return;
		}
		
		var pos = entity.getBlockPos();
		var direction = state.getValue(HorizontalDirectionalBlock.FACING);
		var offsetX = direction == Direction.NORTH ? 1.0F : 0.0F;
		var offsetZ = direction == Direction.WEST ? 1.0F : 0.0F;
		var start = entity.segments.get(0);
		var end = entity.segments.get(entity.segments.size() - 1);
		
		matrices.pushPose();
		
		var pose = matrices.last();
		var consumer = provider.getBuffer(AMRenderLayers.getHolographicBridge());
		var xA = end.x() - pos.getX();
		var xB = start.x() - pos.getX();
		var yA = end.y() - pos.getY();
		var yB = start.y() - pos.getY();
		var zA = end.z() - pos.getZ();
		var zB = start.z() - pos.getZ();
		
		addBridgeVertex(pose, consumer, xA, yA, zA, 0.0F, 0.0F, entity);
		addBridgeVertex(pose, consumer, xB, yB, zB, 0.0F, 1.0F, entity);
		addBridgeVertex(pose, consumer, xB + offsetX, yB, zB + offsetZ, 1.0F, 1.0F, entity);
		addBridgeVertex(pose, consumer, xA + offsetX, yA, zA + offsetZ, 1.0F, 0.0F, entity);
		
		matrices.popPose();
	}
	
	private static void addBridgeVertex(PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float z, float u, float v, HoloBridgeProjectorBlockEntity entity) {
		consumer.addVertex(pose, x, y, z)
				.setColor(entity.color.getR(), entity.color.getG(), entity.color.getB(), entity.color.getA())
				.setUv(u, v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(LightTexture.FULL_BRIGHT)
				.setNormal(pose, 0.0F, 1.0F, 0.0F);
	}
	
	@Override
	public boolean shouldRenderOffScreen(HoloBridgeProjectorBlockEntity blockEntity) {
		return true;
	}
}
