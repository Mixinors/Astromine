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

package com.github.mixinors.astromine.common.widget.blade;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.mixinors.astromine.AMCommon;
import dev.vini2003.hammer.core.api.client.texture.BaseTexture;
import dev.vini2003.hammer.core.api.client.texture.FluidTexture;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.TiledFluidTexture;
import dev.vini2003.hammer.core.api.common.util.FluidTextUtils;
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;

public class FluidFilterWidget extends ButtonWidget {
	private static final BaseTexture STANDARD_BACKGROUND_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/fluid_filter_background.png"));
	private static final BaseTexture STANDARD_FOREGROUND_TEXTURE = new FluidTexture(FluidVariant.blank());
	
	private BaseTexture backgroundTexture = STANDARD_BACKGROUND_TEXTURE;
	private BaseTexture foregroundtexture = STANDARD_FOREGROUND_TEXTURE;

	private Supplier<FluidVariant> fluidSupplier = FluidVariant::blank;
	private Consumer<FluidVariant> fluidConsumer = fluid -> {};
	
	@Override
	public void onMouseClicked(float x, float y, int button) {
		super.onMouseClicked(x, y, button);
		
		var handled = getHandled();
		
		var handler = handled.getHandler();
		
		var stack = getHandled().getHandler().getCursorStack();

		if (isPointWithin(x, y)) {
			if (!stack.isEmpty()) {
				var fluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.ofPlayerCursor(handler.getPlayer(), handler));
				
				if (fluidStorage != null) {
					var fluidVariant = StorageUtil.findStoredResource(fluidStorage, null);
					
					fluidSupplier = () -> fluidVariant;
					fluidConsumer.accept(fluidSupplier.get());
				}
			} else if (button == 2) {
				fluidSupplier = FluidVariant::blank;
				fluidConsumer.accept(fluidSupplier.get());
			}
		}
	}
	
	@NotNull
	@Override
	public List<Text> getTooltip() {
		var fluidTooltip = FluidTextUtils.getVariantTooltips(fluidSupplier.get()).get(0);

		return List.of(new TranslatableText("text.astromine.filter", fluidTooltip));
	}
	
	@Override
	public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider, float delta) {
		var x = getX();
		var y=  getY();

		var width = getWidth();
		var height = getHeight();
		
		backgroundTexture.draw(matrices, provider, x, y, width, height);
		
		if (fluidSupplier.get().isBlank()) return;
		
		foregroundtexture = new TiledFluidTexture(fluidSupplier.get());
		
		foregroundtexture.draw(matrices, provider, x + 1, y + 1, width - 2, height - 2);
	}
	
	public Supplier<FluidVariant> getFluidSupplier() {
		return fluidSupplier;
	}
	
	public void setFluidSupplier(Supplier<FluidVariant> fluidSupplier) {
		this.fluidSupplier = fluidSupplier;
	}
	
	public Consumer<FluidVariant> getFluidConsumer() {
		return fluidConsumer;
	}
	
	public void setFluidConsumer(Consumer<FluidVariant> fluidConsumer) {
		this.fluidConsumer = fluidConsumer;
	}
}
