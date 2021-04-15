/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.chainmailstudios.astromine.common.widget.blade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.client.render.sprite.SpriteRenderer;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.github.chainmailstudios.astromine.common.utilities.TextUtilities;
import com.github.vini2003.blade.client.utilities.Layers;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A filter widget depicting
 * the filter {@link Fluid} for a related object.
 *
 * The {@link #fluidSupplier} supplies the {@link Fluid}
 * displayed by this widget.
 *
 * When clicked, the fluid is changed, triggering the
 * {@link #fluidConsumer} to update the related object.
 */
public class FluidFilterWidget extends ButtonWidget {
	private static final Identifier FLUID_BACKGROUND = AstromineCommon.identifier("textures/widget/fluid_filter_background.png");

	private Supplier<Fluid> fluidSupplier = () -> Fluids.EMPTY;

	private Consumer<Fluid> fluidConsumer = fluid -> {};

	/** Returns this widget's {@link #fluidSupplier}. */
	public Supplier<Fluid> getFluidSupplier() {
		return fluidSupplier;
	}

	/** Sets this widget's {@link #fluidSupplier} to the specified one. */
	public void setFluidSupplier(Supplier<Fluid> fluidSupplier) {
		this.fluidSupplier = fluidSupplier;
	}

	/** Returns this widget's {@link #fluidConsumer}. */
	public Consumer<Fluid> getFluidConsumer() {
		return fluidConsumer;
	}

	/** Sets this widget's {@link #fluidConsumer} to the specified one. */
	public void setFluidConsumer(Consumer<Fluid> fluidConsumer) {
		this.fluidConsumer = fluidConsumer;
	}

	/** Override mouse click behavior to set contents on left mouse click. */
	@Environment(EnvType.CLIENT)
	@Override
	public void onMouseClicked(float x, float y, int button) {
		super.onMouseClicked(x, y, button);

		ItemStack stack = getHandler().getPlayer().inventory.getCursorStack();

		if (isWithin(x, y)) {
			if (!stack.isEmpty()) {
				FluidComponent fluidComponent = FluidComponent.get(stack);

				fluidSupplier = () -> fluidComponent.getFirst().getFluid();
				fluidConsumer.accept(fluidSupplier.get());
			} else if (button == 2) {
				fluidSupplier = () -> Fluids.EMPTY;
				fluidConsumer.accept(fluidSupplier.get());
			}
		}
	}

	/** Returns this widget's tooltip. */
	@NotNull
	@Override
	public List<Text> getTooltip() {
		Identifier fluidId = Registry.FLUID.getId(fluidSupplier.get());

		return Collections.singletonList(new TranslatableText("text.astromine.filter", TextUtilities.getFluid(fluidId)));
	}

	/** Renders this widget. */
	@Environment(EnvType.CLIENT)
	@Override
	public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider) {
		if (getHidden()) {
			return;
		}

		float x = getPosition().getX();
		float y = getPosition().getY();

		float sX = getSize().getWidth();
		float sY = getSize().getHeight();

		RenderLayer layer = Layers.get(FLUID_BACKGROUND);

		BaseRenderer.drawTexturedQuad(matrices, provider, layer, x, y, getSize().getWidth(), getSize().getHeight(), FLUID_BACKGROUND);

		if (fluidSupplier.get() != Fluids.EMPTY) {
			SpriteRenderer
					.beginPass()
					.setup(provider, RenderLayer.getSolid())
					.sprite(FluidUtilities.getSprite(fluidSupplier.get()))
					.color(FluidUtilities.getColor(MinecraftClient.getInstance().player, fluidSupplier.get())).light(0x00f000f0).overlay(OverlayTexture.DEFAULT_UV).alpha(
				0xff).normal(matrices.peek().getNormal(), 0, 0, 0).position(matrices.peek().getModel(), x + 1, y + 1, x + sX - 1, y + sY - 1, 0F).next(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
		}
	}
}
