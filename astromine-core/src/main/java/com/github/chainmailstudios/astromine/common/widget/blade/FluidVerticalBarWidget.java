/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.client.render.sprite.SpriteRenderer;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.github.chainmailstudios.astromine.common.utilities.TextUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.vini2003.blade.client.utilities.Layers;
import com.github.vini2003.blade.common.utilities.Networks;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;

import com.google.common.collect.Lists;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;

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
public class FluidVerticalBarWidget extends AbstractWidget {
	private final ResourceLocation FLUID_BACKGROUND = AstromineCommon.identifier("textures/widget/fluid_volume_fractional_vertical_bar_background.png");

	private Supplier<FluidVolume> volumeSupplier;

	/** Instantiates a {@link FluidVerticalBarWidget}, adding the
	 * mouse click event to the synchronization list. */
	public FluidVerticalBarWidget() {
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
			volumeSupplier.get().setAmount(Fraction.EMPTY);
			volumeSupplier.get().setSize(Fraction.EMPTY);
			volumeSupplier.get().setFluid(Fluids.EMPTY);
		}
	}

	/** Returns this widget's tooltip. */
	@Environment(EnvType.CLIENT)
	@Override
	public List<Component> getTooltip() {
		ResourceLocation fluidId = volumeSupplier.get().getFluidId();

		return Lists.newArrayList(
				TextUtilities.getFluid(fluidId),
				TextUtilities.getIdentifier(fluidId),
				TextUtilities.getVolume(volumeSupplier.get()),
				TextUtilities.getMod(fluidId)
		);
	}

	/** Renders this widget. */
	@Environment(EnvType.CLIENT)
	@Override
	public void drawWidget(PoseStack matrices, MultiBufferSource provider) {
		if (getHidden()) {
			return;
		}

		float x = getPosition().getX();
		float y = getPosition().getY();

		float sX = getSize().getWidth();
		float sY = getSize().getHeight();

		float sBGY = (((sY / volumeSupplier.get().getSize().floatValue()) * volumeSupplier.get().getAmount().floatValue()));

		RenderType layer = Layers.get(FLUID_BACKGROUND);

		BaseRenderer.drawTexturedQuad(matrices, provider, layer, x, y, getSize().getWidth(), getSize().getHeight(), FLUID_BACKGROUND);

		if (volumeSupplier.get().getFluid() != Fluids.EMPTY) {
			SpriteRenderer
					.beginPass()
					.setup(provider, RenderType.SOLID)
					.sprite(FluidUtilities.getSprite(volumeSupplier.get().getFluid()))
					.color(FluidUtilities.getColor(Minecraft.getInstance().player, volumeSupplier.get().getFluid()))
					.light(0x00f000f0)
					.overlay(OverlayTexture.NO_OVERLAY)
					.alpha(0xff)
					.normal(matrices.last().normal(), 0, 0, 0)
					.position(matrices.last().pose(), x + 1, y + 1 + Math.max(0, sY - ((int) (sBGY) + 1)), x + sX - 1, y + sY - 1, 0F)
					.next(InventoryMenu.BLOCK_ATLAS);
		}
	}
}
