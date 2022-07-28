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
import com.github.mixinors.astromine.common.widget.BodyWidget;
import com.github.mixinors.astromine.registry.common.AMBodies;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.gui.api.client.screen.BaseHandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BodySelectorHandledScreen extends BaseHandledScreen<BodySelectorScreenHandler> {
	public static float DRAG_DELTA_X = 0.0F;
	public static float DRAG_DELTA_Y = 0.0F;
	
	public static float SCROLL_DELTA = 0.0F;
	
	public static final Texture STANDARD_BACKGROUND_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/space.png"));
	
	protected Supplier<Texture> backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
	
	public BodySelectorHandledScreen(@NotNull BodySelectorScreenHandler handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
		super(handler, inventory, title);
		
		playerInventoryTitleX = -Integer.MAX_VALUE;
		playerInventoryTitleY = -Integer.MAX_VALUE;
		
		DRAG_DELTA_X = 0.0F;
		DRAG_DELTA_Y = 0.0F;
		
		SCROLL_DELTA = 1.0F;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		
		var client = InstanceUtil.getClient();
		
		var provider = client.getBufferBuilders().getEffectVertexConsumers();
		
		backgroundTexture.get().draw(matrices, provider, 0, 0, width, height);
		
		provider.draw();
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		BodySelectorHandledScreen.DRAG_DELTA_X += deltaX;
		BodySelectorHandledScreen.DRAG_DELTA_Y += deltaY;
		
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		var client = InstanceUtil.getClient();
		
		var window = client.getWindow();
		
		var scale = 1.0F + (amount / 12.0F);
		
		// Negative Delta X goes left.
		// Positive Delta X goes right.
		
		// Negative Delta Y goes up.
		// Positive Delta Y goes down.
		
		// X = 0 is screen top left.
		// Y = 0 is screen top left.

		DRAG_DELTA_X -= (mouseX * scale) - mouseX;
		DRAG_DELTA_Y -= (mouseY * scale) - mouseY;
		
		SCROLL_DELTA += (scale - 1.0F);
		
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	
	@Override
	public void renderBackground(MatrixStack matrices) {
	
	}
}
