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

package com.github.mixinors.astromine.common.widget.blade;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.ClientUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import com.github.mixinors.astromine.client.BaseRenderer;
import com.github.mixinors.astromine.client.render.sprite.SpriteRenderer;
import com.github.mixinors.astromine.common.util.FluidUtils;
import com.github.mixinors.astromine.common.util.TextUtils;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.vini2003.blade.client.utilities.Layers;
import com.github.vini2003.blade.common.utilities.Networks;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Supplier;

/**
 * A vertical bar widget depicting
 * the energy level of the specified {@link #volumeSupplier}.
 *
 * The {@link #volumeSupplier} supplies the volume from which
 * {@link EnergyVolume#getAmount()} and {@link EnergyVolume#getSize()}
 * are queried from.
 */
public class VerticalFluidBarWidget extends AbstractWidget {
	private final Identifier FLUID_BACKGROUND = AMCommon.id("textures/widget/fluid_volume_fractional_vertical_bar_background.png");

	private Supplier<FluidVolume> volumeSupplier;

	/** Instantiates a {@link VerticalFluidBarWidget}, adding the
	 * mouse click event to the synchronization list. */
	public VerticalFluidBarWidget() {
		getSynchronize().add(Networks.getMOUSE_CLICK());
	}

	/** Returns this widget's {@link #volumeSupplier}. */
	public Supplier<FluidVolume> getVolumeSupplier() {
		return volumeSupplier;
	}

	/** Sets this widget's {@link #volumeSupplier} to the specified one. */
	public void setVolumeSupplier(Supplier<FluidVolume> volumeSupplier) {
		this.volumeSupplier = volumeSupplier;
	}

	/** Override mouse click behavior to clear tank on middle mouse click. */
	@Override
	public void onMouseClicked(float x, float y, int button) {
		super.onMouseClicked(x, y, button);

		if (isWithin(x, y) && !getHidden() && button == 2) {
			volumeSupplier.get().setAmount(0L);
			volumeSupplier.get().setSize(0L);
			volumeSupplier.get().setFluid(Fluids.EMPTY);
		}
	}

	/** Returns this widget's tooltip. */
	@Environment(EnvType.CLIENT)
	@Override
	public List<Text> getTooltip() {
		var volume = volumeSupplier.get();
		var fluidId = volume.getFluidId();

		return List.of(
				TextUtils.getFluid(fluidId),
				TextUtils.getIdentifier(fluidId),
				TextUtils.getFluidVolume(FluidVolume.of(volume.getAmount() / 81L, volume.getSize() / 81L, volume.getFluid())),
				TextUtils.getMod(fluidId)
		);
	}

	/** Renders this widget. */
	@Environment(EnvType.CLIENT)
	@Override
	public void drawWidget(MatrixStack matrices, VertexConsumerProvider provider) {
		if (getHidden()) {
			return;
		}

		var x = getPosition().getX();
		var y = getPosition().getY();

		var sX = getSize().getWidth();
		var sY = getSize().getHeight();

		var sBGY = (((sY / volumeSupplier.get().getSize().floatValue()) * volumeSupplier.get().getAmount().floatValue()));

		var layer = Layers.get(FLUID_BACKGROUND);

		BaseRenderer.drawTexturedQuad(matrices, provider, layer, x, y, getSize().getWidth(), getSize().getHeight(), FLUID_BACKGROUND);

		if (volumeSupplier.get().getFluid() != Fluids.EMPTY) {
			SpriteRenderer
					.beginPass()
					.setup(provider, RenderLayer.getSolid())
					.sprite(FluidUtils.getSprite(volumeSupplier.get().getFluid()))
					.color(FluidUtils.getColor(ClientUtils.getPlayer(), volumeSupplier.get().getFluid()))
					.light(0x00f000f0)
					.overlay(OverlayTexture.DEFAULT_UV)
					.alpha(0xff)
					.normal(matrices.peek().getNormal(), 0.0F, 0.0F, 0.0F)
					.position(matrices.peek().getModel(), x + 1.0F, y + 1.0F + Math.max(0.0F, sY - ((int) (sBGY) + 1.0F)), x + sX - 1.0F, y + sY - 1.0F, 0.0F)
					.next(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
		}
	}
}
