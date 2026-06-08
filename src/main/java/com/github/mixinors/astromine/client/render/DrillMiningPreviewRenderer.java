/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.client.render;

import java.util.LinkedHashSet;
import java.util.Set;
import com.github.mixinors.astromine.common.item.utility.DrillItem;
import com.github.mixinors.astromine.common.item.utility.DrillMiningArea;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

public final class DrillMiningPreviewRenderer {
	private DrillMiningPreviewRenderer() {
	}

	public static void render(RenderHighlightEvent.Block event) {
		var minecraft = Minecraft.getInstance();
		var player = minecraft.player;
		var level = minecraft.level;

		if (player == null || level == null || !(player.getMainHandItem().getItem() instanceof DrillItem drill) || drill.getMiningDiameter() <= 1) {
			return;
		}

		var target = event.getTarget();
		var origin = target.getBlockPos();
		var cameraPosition = event.getCamera().getPosition();
		var consumer = event.getMultiBufferSource().getBuffer(RenderType.lines());
		var context = CollisionContext.of(player);
		var segments = new LinkedHashSet<LineSegment>();

		for (var pos : DrillMiningArea.positions(origin, target.getDirection(), drill.getMiningDiameter())) {
			if (pos.equals(origin)) {
				continue;
			}

			var state = level.getBlockState(pos);

			if (state.isAir() || !level.getWorldBorder().isWithinBounds(pos)) {
				continue;
			}

			collectSegments(segments, state.getShape(level, pos, context), pos.getX(), pos.getY(), pos.getZ());
		}

		renderSegments(event.getPoseStack(), consumer, segments, cameraPosition.x(), cameraPosition.y(), cameraPosition.z());
	}

	private static void collectSegments(Set<LineSegment> segments, VoxelShape shape, int blockX, int blockY, int blockZ) {
		shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> segments.add(LineSegment.create(
				x1 + blockX,
				y1 + blockY,
				z1 + blockZ,
				x2 + blockX,
				y2 + blockY,
				z2 + blockZ
		)));
	}

	private static void renderSegments(PoseStack poseStack, VertexConsumer consumer, Set<LineSegment> segments, double cameraX, double cameraY, double cameraZ) {
		var pose = poseStack.last();

		for (var segment : segments) {
			var x1 = segment.x1() / (double) LineSegment.SCALE - cameraX;
			var y1 = segment.y1() / (double) LineSegment.SCALE - cameraY;
			var z1 = segment.z1() / (double) LineSegment.SCALE - cameraZ;
			var x2 = segment.x2() / (double) LineSegment.SCALE - cameraX;
			var y2 = segment.y2() / (double) LineSegment.SCALE - cameraY;
			var z2 = segment.z2() / (double) LineSegment.SCALE - cameraZ;
			var normalX = (float) (x2 - x1);
			var normalY = (float) (y2 - y1);
			var normalZ = (float) (z2 - z1);
			var length = Mth.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);

			normalX /= length;
			normalY /= length;
			normalZ /= length;

			consumer.addVertex(pose, (float) x1, (float) y1, (float) z1)
					.setColor(0.0F, 0.0F, 0.0F, 0.4F)
					.setNormal(pose, normalX, normalY, normalZ);
			consumer.addVertex(pose, (float) x2, (float) y2, (float) z2)
					.setColor(0.0F, 0.0F, 0.0F, 0.4F)
					.setNormal(pose, normalX, normalY, normalZ);
		}
	}

	private record LineSegment(long x1, long y1, long z1, long x2, long y2, long z2) {
		private static final long SCALE = 1_000_000L;

		private static LineSegment create(double x1, double y1, double z1, double x2, double y2, double z2) {
			var sx1 = quantize(x1);
			var sy1 = quantize(y1);
			var sz1 = quantize(z1);
			var sx2 = quantize(x2);
			var sy2 = quantize(y2);
			var sz2 = quantize(z2);

			if (isAfter(sx1, sy1, sz1, sx2, sy2, sz2)) {
				return new LineSegment(sx2, sy2, sz2, sx1, sy1, sz1);
			}

			return new LineSegment(sx1, sy1, sz1, sx2, sy2, sz2);
		}

		private static long quantize(double value) {
			return Math.round(value * SCALE);
		}

		private static boolean isAfter(long x1, long y1, long z1, long x2, long y2, long z2) {
			if (x1 != x2) {
				return x1 > x2;
			}

			if (y1 != y2) {
				return y1 > y2;
			}

			return z1 > z2;
		}
	}
}
