package com.github.mixinors.astromine.client.render.block.entity;

import com.github.mixinors.astromine.common.block.entity.utility.PumpBlockEntity;
import com.github.mixinors.astromine.registry.client.AMRenderLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;

public class PumpBlockEntityRenderer implements BlockEntityRenderer<PumpBlockEntity> {
	public PumpBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}
	
	@Override
	public void render(PumpBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light, int overlay) {
		var lerpDepth = Mth.lerp(tickDelta / 100.0F, entity.prevDepth, entity.depth);
		entity.prevDepth = lerpDepth;
		
		var maxY = (float) (lerpDepth / 20.0D + 1.0D);
		var consumer = provider.getBuffer(AMRenderLayers.getPumpTube());
		var pose = matrices.last();
		var prevY = 0.0F;
		var prevV = 1.0F - (maxY - (int) maxY);
		
		for (var y = 1.0F; y < maxY; y += 1.0F) {
			var v = prevV + Math.min(maxY - y, 1.0F);
			var dY = Math.min(y, maxY - 1.0F);
			
			addPumpVertex(pose, consumer, 5.0F / 16.0F, -prevY, 5.0F / 16.0F, 0.0F, prevV, overlay, light, 0.0F, 0.0F, -1.0F);
			addPumpVertex(pose, consumer, 10.0F / 16.0F, -prevY, 5.0F / 16.0F, 1.0F, prevV, overlay, light, 0.0F, 0.0F, -1.0F);
			addPumpVertex(pose, consumer, 10.0F / 16.0F, -dY, 5.0F / 16.0F, 1.0F, v, overlay, light, 0.0F, 0.0F, -1.0F);
			addPumpVertex(pose, consumer, 5.0F / 16.0F, -dY, 5.0F / 16.0F, 0.0F, v, overlay, light, 0.0F, 0.0F, -1.0F);
			
			addPumpVertex(pose, consumer, 5.0F / 16.0F, -prevY, 10.0F / 16.0F, 0.0F, prevV, overlay, light, 0.0F, 0.0F, 1.0F);
			addPumpVertex(pose, consumer, 5.0F / 16.0F, -dY, 10.0F / 16.0F, 0.0F, v, overlay, light, 0.0F, 0.0F, 1.0F);
			addPumpVertex(pose, consumer, 10.0F / 16.0F, -dY, 10.0F / 16.0F, 1.0F, v, overlay, light, 0.0F, 0.0F, 1.0F);
			addPumpVertex(pose, consumer, 10.0F / 16.0F, -prevY, 10.0F / 16.0F, 1.0F, prevV, overlay, light, 0.0F, 0.0F, 1.0F);
			
			addPumpVertex(pose, consumer, 5.0F / 16.0F, -prevY, 5.0F / 16.0F, 0.0F, prevV, overlay, light, -1.0F, 0.0F, 0.0F);
			addPumpVertex(pose, consumer, 5.0F / 16.0F, -dY, 5.0F / 16.0F, 0.0F, v, overlay, light, -1.0F, 0.0F, 0.0F);
			addPumpVertex(pose, consumer, 5.0F / 16.0F, -dY, 10.0F / 16.0F, 1.0F, v, overlay, light, -1.0F, 0.0F, 0.0F);
			addPumpVertex(pose, consumer, 5.0F / 16.0F, -prevY, 10.0F / 16.0F, 1.0F, prevV, overlay, light, -1.0F, 0.0F, 0.0F);
			
			addPumpVertex(pose, consumer, 10.0F / 16.0F, -dY, 5.0F / 16.0F, 0.0F, v, overlay, light, 1.0F, 0.0F, 0.0F);
			addPumpVertex(pose, consumer, 10.0F / 16.0F, -prevY, 5.0F / 16.0F, 0.0F, prevV, overlay, light, 1.0F, 0.0F, 0.0F);
			addPumpVertex(pose, consumer, 10.0F / 16.0F, -prevY, 10.0F / 16.0F, 1.0F, prevV, overlay, light, 1.0F, 0.0F, 0.0F);
			addPumpVertex(pose, consumer, 10.0F / 16.0F, -dY, 10.0F / 16.0F, 1.0F, v, overlay, light, 1.0F, 0.0F, 0.0F);
			
			prevY = y;
			prevV = v;
		}
	}
	
	private static void addPumpVertex(PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float z, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
		consumer.addVertex(pose, x, y, z)
				.setColor(0xFFFFFFFF)
				.setUv(u, v)
				.setOverlay(overlay)
				.setLight(light)
				.setNormal(pose, normalX, normalY, normalZ);
	}
	
	@Override
	public boolean shouldRenderOffScreen(PumpBlockEntity blockEntity) {
		return true;
	}

	@Override
	public AABB getRenderBoundingBox(PumpBlockEntity blockEntity) {
		var tubeLength = Math.max(1.0D, blockEntity.depth / 20.0D + 1.0D);

		return new AABB(blockEntity.getBlockPos()).expandTowards(0.0D, -tubeLength, 0.0D).inflate(1.0D);
	}
}
