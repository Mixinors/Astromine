/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public final class GuiRenderers {
	private GuiRenderers() {
	}
	
	public static void panel(GuiGraphics graphics, int x, int y, int width, int height) {
		nineSlice(graphics, GuiSprites.PANEL, x, y, width, height, 18, 18, 4);
	}
	
	public static void button(GuiGraphics graphics, int x, int y, int width, int height, boolean hovered, boolean enabled) {
		nineSlice(graphics, enabled ? (hovered ? GuiSprites.BUTTON_FOCUSED : GuiSprites.BUTTON_ENABLED) : GuiSprites.BUTTON_DISABLED, x, y, width, height, 18, 18, 2);
	}
	
	public static void slot(GuiGraphics graphics, int x, int y) {
		graphics.blit(GuiSprites.SLOT, x, y, 0.0F, 0.0F, GuiSprites.SLOT_SIZE, GuiSprites.SLOT_SIZE, GuiSprites.SLOT_SIZE, GuiSprites.SLOT_SIZE);
	}
	
	public static void nineSlice(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int border) {
		var innerWidth = Math.max(0, width - border * 2);
		var innerHeight = Math.max(0, height - border * 2);
		var sourceInnerWidth = textureWidth - border * 2;
		var sourceInnerHeight = textureHeight - border * 2;
		
		graphics.blit(texture, x, y, 0.0F, 0.0F, border, border, textureWidth, textureHeight);
		graphics.blit(texture, x + width - border, y, textureWidth - border, 0.0F, border, border, textureWidth, textureHeight);
		graphics.blit(texture, x, y + height - border, 0.0F, textureHeight - border, border, border, textureWidth, textureHeight);
		graphics.blit(texture, x + width - border, y + height - border, textureWidth - border, textureHeight - border, border, border, textureWidth, textureHeight);
		
		if (innerWidth > 0) {
			graphics.blit(texture, x + border, y, innerWidth, border, border, 0.0F, sourceInnerWidth, border, textureWidth, textureHeight);
			graphics.blit(texture, x + border, y + height - border, innerWidth, border, border, textureHeight - border, sourceInnerWidth, border, textureWidth, textureHeight);
		}
		
		if (innerHeight > 0) {
			graphics.blit(texture, x, y + border, border, innerHeight, 0.0F, border, border, sourceInnerHeight, textureWidth, textureHeight);
			graphics.blit(texture, x + width - border, y + border, border, innerHeight, textureWidth - border, border, border, sourceInnerHeight, textureWidth, textureHeight);
		}
		
		if (innerWidth > 0 && innerHeight > 0) {
			graphics.blit(texture, x + border, y + border, innerWidth, innerHeight, border, border, sourceInnerWidth, sourceInnerHeight, textureWidth, textureHeight);
		}
	}
	
	public static void tiledFluid(GuiGraphics graphics, FluidStack stack, int x, int y, int width, int height) {
		if (stack.isEmpty() || width <= 0 || height <= 0) {
			return;
		}
		
		var extensions = IClientFluidTypeExtensions.of(stack.getFluid());
		var stillTexture = extensions.getStillTexture(stack);
		var sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(stillTexture);
		var tint = extensions.getTintColor(stack);
		var alpha = (tint >>> 24) & 0xFF;
		var red = ((tint >> 16) & 0xFF) / 255.0F;
		var green = ((tint >> 8) & 0xFF) / 255.0F;
		var blue = (tint & 0xFF) / 255.0F;
		var opacity = (alpha == 0 ? 0xFF : alpha) / 255.0F;
		
		for (var offsetX = 0; offsetX < width; offsetX += 16) {
			for (var offsetY = 0; offsetY < height; offsetY += 16) {
				var tileWidth = Math.min(16, width - offsetX);
				var tileHeight = Math.min(16, height - offsetY);
				var tileX = x + offsetX;
				var tileY = y + offsetY;
				
				graphics.enableScissor(tileX, tileY, tileX + tileWidth, tileY + tileHeight);
				graphics.blit(tileX, tileY, 0, 16, 16, sprite, red, green, blue, opacity);
				graphics.disableScissor();
			}
		}
	}
}
