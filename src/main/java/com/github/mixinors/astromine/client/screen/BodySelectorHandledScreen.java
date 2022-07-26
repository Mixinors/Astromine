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
import com.github.mixinors.astromine.common.screen.handler.RecipeCreatorScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.body.BodySelectorScreenHandler;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.TiledFluidTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.gui.api.client.screen.BaseHandledScreen;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BodySelectorHandledScreen extends BaseHandledScreen<BodySelectorScreenHandler> {
	public static final Texture STANDARD_BACKGROUND_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/space.png"));
	
	protected Supplier<Texture> backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
	
	public BodySelectorHandledScreen(@NotNull BodySelectorScreenHandler handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
		super(handler, inventory, title);
		
		playerInventoryTitleX = -Integer.MAX_VALUE;
		playerInventoryTitleY = -Integer.MAX_VALUE;
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
	public void renderBackground(MatrixStack matrices) {
	
	}
}
