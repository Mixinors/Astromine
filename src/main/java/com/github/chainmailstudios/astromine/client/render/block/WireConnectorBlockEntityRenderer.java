package com.github.chainmailstudios.astromine.client.render.block;

import com.github.chainmailstudios.astromine.common.block.entity.WireConnectorBlockEntity;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

public class WireConnectorBlockEntityRenderer<T extends WireConnectorBlockEntity> extends BlockEntityRenderer<T> {
	public WireConnectorBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(WireConnectorBlockEntity parent, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		for (NetworkNode node : parent.children) {
			if (parent.getWorld().getBlockEntity(node.getPosition()) == null) {
				continue;
			}

			WireConnectorBlockEntity child = (WireConnectorBlockEntity) parent.getWorld().getBlockEntity(node.getPosition());

			Collection<Vector3f> segments = WireConnectorBlockEntity.getSegments(parent, child, (float) parent.getPos().getSquaredDistance(node.getPosition()));

			if (segments.size() == 0) return;

			Vector3f origin = segments.iterator().next();
			Vector3f previous = origin;

			matrixStack.push();

			VertexConsumer consumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());

			int color = parent.getColor();

			int a = color >> 24 & 0xFF;
			int r = color >> 16 & 0xFF;
			int g = color >> 8 & 0xFF;
			int b = color & 0xFF;

			for (Vector3f vector : segments) {
				if (vector != origin) {
					float xA = vector.getX() - parent.getPos().getX();
					float xB = previous.getX() - parent.getPos().getX();

					float yA = vector.getY() - parent.getPos().getY();
					float yB = previous.getY() - parent.getPos().getY();

					float zA = vector.getZ() - parent.getPos().getZ();
					float zB = previous.getZ() - parent.getPos().getZ();

					consumer.vertex(matrixStack.peek().getModel(), xA, yA, zA).color(r, g, b, a).next();
					consumer.vertex(matrixStack.peek().getModel(), xB, yB, zB).color(r, g, b, a).next();
				}
				previous = vector;
			}

			matrixStack.pop();
		}
	}

	@Override
	public boolean rendersOutsideBoundingBox(WireConnectorBlockEntity blockEntity) {
		return true;
	}
}
