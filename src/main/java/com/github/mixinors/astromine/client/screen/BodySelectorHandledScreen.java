/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.screen;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.screen.base.CustomForegroundBaseHandledScreen;
import com.github.mixinors.astromine.client.util.DrawingUtil;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.screen.handler.body.BodySelectorScreenHandler;
import com.github.mixinors.astromine.common.util.Color;
import com.github.mixinors.astromine.registry.client.AMRenderLayers;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BodySelectorHandledScreen extends CustomForegroundBaseHandledScreen<BodySelectorScreenHandler> {
	private static final ResourceLocation SPACE = AMCommon.id("textures/widget/space.png");
	private static final ResourceLocation GO_BUTTON_UP = AMCommon.id("textures/widget/small_square_button_go_up_clear.png");
	private static final ResourceLocation GO_BUTTON_DOWN = AMCommon.id("textures/widget/small_square_button_go_down_clear.png");
	private static final int SELECTION_WIDTH = 256;
	private static final int SELECTION_HEIGHT = 64;
	private static float OFFSET_X = 0.0F;
	private static float PREV_OFFSET_X = 0.0F;
	private static float OFFSET_Y = 0.0F;
	private static float PREV_OFFSET_Y = 0.0F;
	private static float ZOOM = 1.0F;
	private static float PREV_ZOOM = 1.0F;
	
	private Body selected;
	private Body hovered;
	private int closeDelay = -1;
	
	public BodySelectorHandledScreen(@NotNull BodySelectorScreenHandler handler, @NotNull Inventory inventory, @NotNull Component title) {
		super(handler, inventory, title);
		
		OFFSET_X = 0.0F;
		OFFSET_Y = 0.0F;
		ZOOM = 1.0F;
	}
	
	public static float getOffsetX() {
		return PREV_OFFSET_X;
	}
	
	public static float getOffsetY() {
		return PREV_OFFSET_Y;
	}
	
	public static float getZoom() {
		return PREV_ZOOM;
	}
	
	@Override
	protected void init() {
		super.init();
		
		imageWidth = width;
		imageHeight = height;
		leftPos = 0;
		topPos = 0;
	}
	
	@Override
	public void containerTick() {
		PREV_ZOOM = Mth.lerp(0.5F, PREV_ZOOM, ZOOM);
		PREV_OFFSET_X = Mth.lerp(0.5F, PREV_OFFSET_X, OFFSET_X);
		PREV_OFFSET_Y = Mth.lerp(0.5F, PREV_OFFSET_Y, OFFSET_Y);
		
		if (closeDelay > 0) {
			closeDelay -= 1;
		} else if (closeDelay == 0) {
			onClose();
			closeDelay = -1;
		}
	}
	
	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		graphics.blit(SPACE, 0, 0, 0.0F, 0.0F, width, height, 256, 256);
		
		hovered = null;
		updateBodyPositions();
		
		for (var body : AMRegistries.BODY.getValues()) {
			if (isBodyHovered(body, mouseX, mouseY)) {
				hovered = body;
				break;
			}
		}
		
		var anyHovered = hovered != null;
		
		for (var body : AMRegistries.BODY.getValues()) {
			updateBodyScale(body, body == hovered, partialTick);
			updateBodyAngle(body, anyHovered, partialTick);
		}
		
		updateBodyPositions();
		
		var provider = Minecraft.getInstance().renderBuffers().bufferSource();
		var matrices = graphics.pose();
		
		for (var body : AMRegistries.BODY.getValues()) {
			var orbit = body.orbit();
			
			if (orbit != null) {
				drawOrbit(matrices, provider, body, body == hovered);
			}
		}
		
		for (var body : AMRegistries.BODY.getValues()) {
			drawBody(matrices, provider, body, 100.0F);
		}
		
		provider.endBatch();
		
		if (selected != null) {
			var bounds = bodyBounds(selected);
			graphics.renderOutline((int) Math.round(bounds.x() - 2.0D), (int) Math.round(bounds.y() - 2.0D), (int) Math.round(bounds.width() + 4.0D), (int) Math.round(bounds.height() + 4.0D), 0xFF66D9EF);
		}
		
		drawSelectedPanel(graphics);
	}
	
	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
	}
	
	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		
		if (hovered != null) {
			graphics.renderTooltip(font, hovered.name(), mouseX, mouseY);
		}
	}
	
	@Override
	public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0 && selected != null && isSelectionButtonHovered((int) mouseX, (int) mouseY)) {
			closeDelay = 5;
			return true;
		}
		
		if (button == 0 && hovered != null) {
			selected = hovered;
			return true;
		}
		
		if (button == 1 && hovered != null) {
			selected = null;
			return true;
		}
		
		if (button == 0 && !isSelectionPanelHovered((int) mouseX, (int) mouseY)) {
			selected = null;
		}
		
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		OFFSET_X += (float) deltaX;
		OFFSET_Y += (float) deltaY;
		
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		var scale = 1.0F + (float) (scrollY / 4.0F);
		var targetZoom = Mth.clamp(ZOOM * scale, 1.0F, 5.0F);
		var zoomFactor = targetZoom / ZOOM;
		
		OFFSET_X = (float) (OFFSET_X * zoomFactor + mouseX * (1.0F - zoomFactor));
		OFFSET_Y = (float) (OFFSET_Y * zoomFactor + mouseY * (1.0F - zoomFactor));
		ZOOM = targetZoom;
		
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}
	
	private void updateBodyPositions() {
		var positions = new HashMap<Body, ScreenPos>();
		
		for (var body : AMRegistries.BODY.getValues()) {
			updateBodyPosition(body, positions);
		}
	}
	
	private ScreenPos updateBodyPosition(Body body, Map<Body, ScreenPos> positions) {
		var cached = positions.get(body);
		
		if (cached != null) {
			return cached;
		}
		
		var orbit = body.orbit();
		var x = width / 2.0D - body.size().width() / 2.0D;
		var y = height / 2.0D - body.size().height() / 2.0D;
		
		if (orbit != null) {
			x = orbit.width() * Math.cos(body.getAngle());
			y = orbit.height() * Math.sin(body.getAngle());
			
			var orbitOffset = orbit.orbitedBodyOffset();
			
			if (orbitOffset != null) {
				x += orbitOffset.x();
				y += orbitOffset.y();
			}
			
			if (orbit.orbitedBodyId() != null) {
				var parent = AMRegistries.BODY.get(orbit.orbitedBodyId());
				
				if (parent != null) {
					var parentPos = updateBodyPosition(parent, positions);
					x += parentPos.x();
					y += parentPos.y();
				}
			}
		}
		
		var pos = new ScreenPos(x, y);
		body.setOrbitX(x);
		body.setOrbitY(y);
		positions.put(body, pos);
		return pos;
	}
	
	private void updateBodyScale(Body body, boolean isHovered, float partialTick) {
		var currentScale = body.getScale() <= 0.0D ? 1.0D : body.getScale();
		body.setPrevScale(currentScale);
		body.setScale(Mth.lerp(partialTick / 2.0D, body.getPrevScale(), isHovered ? 1.25D : 1.0D));
	}
	
	private void updateBodyAngle(Body body, boolean anyHovered, float partialTick) {
		var orbit = body.orbit();
		
		if (orbit == null) {
			return;
		}
		
		var speed = anyHovered ? 0.0D : 1.0D;
		var angle = body.getAngle();
		angle += (orbit.speed() / 360.0D * 16.0D) * partialTick * 0.1D * speed;
		angle %= 360.0D;
		body.setAngle(angle);
	}
	
	private boolean isBodyHovered(Body body, int mouseX, int mouseY) {
		var bounds = bodyBounds(body);
		return mouseX > bounds.x() && mouseX < bounds.x() + bounds.width() && mouseY > bounds.y() && mouseY < bounds.y() + bounds.height();
	}
	
	private BodyBounds bodyBounds(Body body) {
		var zoom = Math.max(1.0F, PREV_ZOOM);
		var scale = Math.max(1.0D, body.getScale());
		var bodyWidth = body.size().width() * zoom * scale;
		var bodyHeight = body.size().height() * zoom * scale;
		var screenX = body.getOrbitX() * zoom + PREV_OFFSET_X;
		var screenY = body.getOrbitY() * zoom + PREV_OFFSET_Y;
		
		return new BodyBounds(screenX - bodyWidth, screenY - bodyHeight, bodyWidth * 2.0D, bodyHeight * 2.0D);
	}
	
	private void drawOrbit(com.mojang.blaze3d.vertex.PoseStack matrices, net.minecraft.client.renderer.MultiBufferSource provider, Body body, boolean focused) {
		var orbit = body.orbit();
		
		if (orbit == null) {
			return;
		}
		
		var previous = (ScreenPos) null;
		var color = focused ? new Color(1.0F, 1.0F, 1.0F, 0.1F) : new Color(0.4F, 0.4F, 0.4F, 0.1F);
		
		for (var angle = 0.0F; angle <= 360.0F; angle += 1.0F) {
			var trailX = orbit.width() * Math.cos(Math.toRadians(angle));
			var trailY = orbit.height() * Math.sin(Math.toRadians(angle));
			
			var orbitOffset = orbit.orbitedBodyOffset();
			
			if (orbitOffset != null) {
				trailX += orbitOffset.x();
				trailY += orbitOffset.y();
			}
			
			if (orbit.orbitedBodyId() != null) {
				var parent = AMRegistries.BODY.get(orbit.orbitedBodyId());
				
				if (parent != null) {
					trailX += parent.getOrbitX() + parent.getScale();
					trailY += parent.getOrbitY() + parent.getScale();
				}
			}
			
			trailX *= PREV_ZOOM;
			trailY *= PREV_ZOOM;
			trailX += PREV_OFFSET_X;
			trailY += PREV_OFFSET_Y;
			
			var pos = new ScreenPos(trailX, trailY);
			
			if (previous != null) {
				DrawingUtil.drawLine(matrices, provider, (float) pos.x(), (float) pos.y(), 0.0F, (float) previous.x(), (float) previous.y(), 0.0F, color, RenderType.lines());
			}
			
			previous = pos;
		}
	}
	
	private void drawBody(com.mojang.blaze3d.vertex.PoseStack matrices, net.minecraft.client.renderer.MultiBufferSource provider, Body body, float z) {
		var texture = body.texture();
		var zoom = Math.max(1.0F, PREV_ZOOM);
		var scale = Math.max(1.0D, body.getScale());
		var depth = body.size().length() <= 0.0F ? (body.size().width() + body.size().height()) / 2.0F : body.size().length();
		
		DrawingUtil.drawBody(
				matrices,
				provider,
				(float) (body.getOrbitX() * zoom + PREV_OFFSET_X), (float) (body.getOrbitY() * zoom + PREV_OFFSET_Y), z,
				(float) (body.size().width() * zoom * scale), (float) (body.size().height() * zoom * scale), (float) (depth * zoom * scale),
				(float) body.getAngle(),
				body.orbit() != null && body.orbit().tidalLocked(),
				new Color(0xEFEFEFFFL),
				AMRenderLayers.getBody(texture.up()),
				AMRenderLayers.getBody(texture.down()),
				AMRenderLayers.getBody(texture.north()),
				AMRenderLayers.getBody(texture.south()),
				AMRenderLayers.getBody(texture.west()),
				AMRenderLayers.getBody(texture.east())
		);
	}
	
	private void drawSelectedPanel(GuiGraphics graphics) {
		if (selected == null) {
			return;
		}
		
		var panelX = width / 2 - SELECTION_WIDTH / 2;
		var panelY = height - SELECTION_HEIGHT + 10;
		var bodyWidth = (int) (SELECTION_WIDTH * 0.15F);
		var informationWidth = (int) (SELECTION_WIDTH * 0.7F);
		var informationX = panelX + bodyWidth;
		var buttonX = panelX + Math.round(SELECTION_WIDTH * 0.85F);
		var buttonY = panelY + (SELECTION_HEIGHT - (int) BodySelectorScreenHandler.SELECT_BUTTON_HEIGHT) / 2;
		var lines = font.split(selected.description(), informationWidth);
		var textHeight = 9 + 4 + lines.size() * 9;
		var textY = panelY + (SELECTION_HEIGHT - textHeight) / 2 - 6;
		var iconSize = Math.max(16, bodyWidth / 2);
		var iconX = panelX + bodyWidth / 2 - iconSize / 2;
		var iconY = panelY + (SELECTION_HEIGHT - iconSize) / 2;
		
		graphics.drawString(font, selected.name(), informationX + (informationWidth - font.width(selected.name())) / 2, textY, 0xFFFFFFFF, false);
		
		var lineY = textY + 13;
		
		for (var line : lines) {
			graphics.drawString(font, line, informationX, lineY, 0xFF808080, false);
			lineY += 9;
		}
		
		var provider = Minecraft.getInstance().renderBuffers().bufferSource();
		var texture = selected.texture();
		var previewDepth = selected.size().length() <= 0.0F ? iconSize : selected.size().length() * (iconSize / Math.max(1.0F, selected.size().width())) ;
		
		DrawingUtil.drawBody(
				graphics.pose(),
				provider,
				iconX + iconSize / 2.0F, iconY + iconSize / 2.0F, 600.0F,
				iconSize * 0.5F, iconSize * 0.5F, previewDepth * 0.5F,
				(float) selected.getAngle(),
				false,
				new Color(0xEFEFEFFFL),
				AMRenderLayers.getBody(texture.up()),
				AMRenderLayers.getBody(texture.down()),
				AMRenderLayers.getBody(texture.north()),
				AMRenderLayers.getBody(texture.south()),
				AMRenderLayers.getBody(texture.west()),
				AMRenderLayers.getBody(texture.east())
		);
		provider.endBatch();
		
		graphics.blit(closeDelay >= 0 ? GO_BUTTON_DOWN : GO_BUTTON_UP, buttonX, buttonY, 0.0F, 0.0F, (int) BodySelectorScreenHandler.SELECT_BUTTON_WIDTH, (int) BodySelectorScreenHandler.SELECT_BUTTON_HEIGHT, (int) BodySelectorScreenHandler.SELECT_BUTTON_WIDTH, (int) BodySelectorScreenHandler.SELECT_BUTTON_HEIGHT);
	}
	
	private boolean isSelectionPanelHovered(int mouseX, int mouseY) {
		if (selected == null) {
			return false;
		}
		
		var panelX = width / 2 - SELECTION_WIDTH / 2;
		var panelY = height - SELECTION_HEIGHT + 10;
		
		return mouseX >= panelX && mouseX < panelX + SELECTION_WIDTH && mouseY >= panelY && mouseY < panelY + SELECTION_HEIGHT;
	}
	
	private boolean isSelectionButtonHovered(int mouseX, int mouseY) {
		var panelX = width / 2 - SELECTION_WIDTH / 2;
		var panelY = height - SELECTION_HEIGHT + 10;
		var buttonX = panelX + Math.round(SELECTION_WIDTH * 0.85F);
		var buttonY = panelY + (SELECTION_HEIGHT - (int) BodySelectorScreenHandler.SELECT_BUTTON_HEIGHT) / 2;
		
		return mouseX >= buttonX && mouseX < buttonX + BodySelectorScreenHandler.SELECT_BUTTON_WIDTH && mouseY >= buttonY && mouseY < buttonY + BodySelectorScreenHandler.SELECT_BUTTON_HEIGHT;
	}
	
	private record ScreenPos(double x, double y) {
	}
	
	private record BodyBounds(double x, double y, double width, double height) {
	}
}
