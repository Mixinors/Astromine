package com.github.chainmailstudios.astromine.common.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.client.render.SpriteRenderer;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;

import java.util.function.Supplier;

public class WFluidVolumeFractionalVerticalBar extends WFractionalVerticalBar {
	private Supplier<FluidVolume> volume;

	private final Identifier FLUID_BACKGROUND = AstromineCommon.identifier("textures/widget/fluid_volume_fractional_vertical_bar_background.png");

	public WFluidVolumeFractionalVerticalBar() {
		super();

		setUnit(new TranslatableText("text.astromine.fluid"));

		setBackgroundTexture(FLUID_BACKGROUND);
	}

	@Override
	public Identifier getBackgroundTexture() {
		return FLUID_BACKGROUND;
	}

	public FluidVolume getFluidVolume() {
		return volume.get();
	}

	public <W extends WFractionalVerticalBar> W setFluidVolume(Supplier<FluidVolume> volume) {
		setProgressFraction(volume.get()::getFraction);
		setLimitFraction(volume.get()::getSize);

		this.volume = volume;
		return (W) this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		float sBGY = (((sY / getLimitFraction().get().floatValue()) * getProgressFraction().get().floatValue()));

		BaseRenderer.drawTexturedQuad(matrices, provider, x, y, z, getWidth(), getHeight(), getBackgroundTexture());

		if (getFluidVolume().getFluid() != Fluids.EMPTY) {
			SpriteRenderer.beginPass()
					.setup(provider, RenderLayer.getSolid())
					.sprite(FluidUtilities.texture(getFluidVolume().getFluid())[0])
					.color(FluidUtilities.color(MinecraftClient.getInstance().player, getFluidVolume().getFluid()))
					.light(0x00f000f0)
					.overlay(OverlayTexture.DEFAULT_UV)
					.alpha(0xff)
					.normal(matrices.peek().getNormal(), 0, 0, 0)
					.position(matrices.peek().getModel(), x + 1, y + 1 + Math.max(0, sY - ((int) (sBGY) + 1)), x + sX - 1, y + sY - 1, z)
					.next(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
		}

		if (isFocused()) {
			getTooltip().clear();

			getTooltip().add(FluidUtilities.rawFraction(getProgressFraction().get(), getLimitFraction().get(), getUnit()));

			getTooltip().add(new TranslatableText("text.astromine.tooltip.fractional_value", getProgressFraction().get().toPrettyString(), getLimitFraction().get().toPrettyString()));

			drawTooltips(matrices, provider);
		}
	}
}
