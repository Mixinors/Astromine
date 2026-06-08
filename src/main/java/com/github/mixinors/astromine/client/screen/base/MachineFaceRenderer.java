/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.screen.base;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

final class MachineFaceRenderer {
	private MachineFaceRenderer() {
	}

	static void render(GuiGraphics graphics, ExtendedBlockEntity blockEntity, Direction side, int x, int y, int width, int height) {
		var state = blockEntity.getBlockState();
		var face = faceQuad(blockEntity, state, side);

		if (face == null) {
			drawSprite(graphics, particleSprite(state), -1, state, blockEntity, x, y, width, height);
			return;
		}

		drawSprite(graphics, face.getSprite(), face.getTintIndex(), state, blockEntity, x, y, width, height);
	}

	private static BakedQuad faceQuad(ExtendedBlockEntity blockEntity, BlockState state, Direction side) {
		var model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state);
		var random = random(blockEntity, state);
		var faceQuad = firstQuad(model.getQuads(state, side, random));

		if (faceQuad != null) {
			return faceQuad;
		}

		for (var quad : model.getQuads(state, null, random(blockEntity, state))) {
			if (quad.getDirection() == side) {
				return quad;
			}
		}

		return null;
	}

	private static TextureAtlasSprite particleSprite(BlockState state) {
		return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state);
	}

	private static BakedQuad firstQuad(List<BakedQuad> quads) {
		return quads.isEmpty() ? null : quads.getFirst();
	}

	private static RandomSource random(ExtendedBlockEntity blockEntity, BlockState state) {
		return RandomSource.create(state.getSeed(blockEntity.getBlockPos()));
	}

	private static void drawSprite(GuiGraphics graphics, TextureAtlasSprite sprite, int tintIndex, BlockState state, ExtendedBlockEntity blockEntity, int x, int y, int width, int height) {
		var color = tintIndex < 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(state, blockEntity.getLevel(), blockEntity.getBlockPos(), tintIndex);

		if (color == -1) {
			graphics.blit(x, y, 0, width, height, sprite);
			return;
		}

		var red = ((color >> 16) & 0xFF) / 255.0F;
		var green = ((color >> 8) & 0xFF) / 255.0F;
		var blue = (color & 0xFF) / 255.0F;
		graphics.blit(x, y, 0, width, height, sprite, red, green, blue, 1.0F);
	}
}
