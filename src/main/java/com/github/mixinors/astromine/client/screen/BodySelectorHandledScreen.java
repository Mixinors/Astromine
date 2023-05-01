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

package com.github.mixinors.astromine.client.screen;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.screen.handler.body.BodySelectorScreenHandler;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.gui.api.client.screen.base.BaseHandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BodySelectorHandledScreen extends BaseHandledScreen<BodySelectorScreenHandler> {
	private static float OFFSET_X = 0.0F;
	private static float PREV_OFFSET_X = 0.0F;
	
	private static float OFFSET_Y = 0.0F;
	private static float PREV_OFFSET_Y = 0.0F;
	
	private static float ZOOM = 0.0F;
	private static float PREV_ZOOM = 0.0F;
	
	public static final Texture STANDARD_BACKGROUND_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/space.png"));
	
	protected Supplier<Texture> backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
	
	public BodySelectorHandledScreen(@NotNull BodySelectorScreenHandler handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
		super(handler, inventory, title);
		
		playerInventoryTitleX = -Integer.MAX_VALUE;
		playerInventoryTitleY = -Integer.MAX_VALUE;
		
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
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		
		var client = InstanceUtil.getClient();
		
		var provider = client.getBufferBuilders().getEffectVertexConsumers();
		
		backgroundTexture.get().draw(matrices, provider, 0, 0, width, height);
		
		provider.draw();
		
		PREV_ZOOM = MathHelper.lerp(delta, PREV_ZOOM, ZOOM);
		
		PREV_OFFSET_X = MathHelper.lerp(delta, PREV_OFFSET_X, OFFSET_X);
		PREV_OFFSET_Y = MathHelper.lerp(delta, PREV_OFFSET_Y, OFFSET_Y);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		OFFSET_X += deltaX;
		OFFSET_Y += deltaY;
		
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		var scale = 1.0F + (float) (amount / 4.0F);
		
		var targetZoom = ZOOM * scale;
		
		if (targetZoom > 5.0F) {
			targetZoom = 5.0F;
		} else if (targetZoom < 1.0F) {
			targetZoom = 1.0F;
		}
		
		var zoomFactor = targetZoom / ZOOM;
		
		OFFSET_X = (float) (OFFSET_X * zoomFactor + mouseX * (1.0F - zoomFactor));
		OFFSET_Y = (float) (OFFSET_Y * zoomFactor + mouseY * (1.0F - zoomFactor));
		
		ZOOM = targetZoom;
		
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	
	@Override
	public void renderBackground(MatrixStack matrices) {
	
	}
}
