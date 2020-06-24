package com.github.chainmailstudios.astromine.common.widget;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.lwjgl.opengl.GL11;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;
import spinnery.widget.WAbstractBar;
import spinnery.widget.WStaticText;
import spinnery.widget.WTooltip;
import spinnery.widget.api.Position;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class WFractionalVerticalBar extends WAbstractBar {
	private Supplier<Fraction> progressFraction = Fraction::empty;
	private Supplier<Fraction> limitFraction = Fraction::empty;

	private final WTooltip tooltip = new WTooltip();
	private final WStaticText tooltipText = new WStaticText().setPosition(Position.of(tooltip, 1, 1, 0));

	private Text unit;

	public WFractionalVerticalBar() {
		tooltip.setParent(this);
		tooltip.setInterface(getInterface());

		tooltipText.setParent(this);
		tooltipText.setInterface(getInterface());
	}

	public Supplier<Fraction> getProgressFraction() {
		return progressFraction;
	}

	public <W extends WFractionalVerticalBar> W setProgressFraction(Supplier<Fraction> progress) {
		this.progressFraction = progress;
		return (W) this;
	}

	public Supplier<Fraction> getLimitFraction() {
		return limitFraction;
	}

	public <W extends WFractionalVerticalBar> W setLimitFraction(Supplier<Fraction> limitFraction) {
		this.limitFraction = limitFraction;
		return (W) this;
	}

	public Text getUnit() {
		return unit;
	}

	public <W extends WFractionalVerticalBar> W setUnit(Text unit) {
		this.unit = unit;
		return (W) this;
	}

	public WTooltip getTooltip() {
		return tooltip;
	}

	public WStaticText getTooltipText() {
		return tooltipText;
	}

	@Override
	public void onMouseMoved(float mouseX, float mouseY) {
		tooltip.setHidden(!isFocused());
		tooltipText.setHidden(!isFocused());

		if (!tooltip.isHidden()) {
			tooltip.setWidth(TextRenderer.width(tooltipText.getText()));
			tooltip.setHeight(TextRenderer.height());

			tooltip.setX((int) (mouseX + 8));
			tooltip.setY((int) (mouseY - (tooltip.getHeight() / 2f)));
		}

		super.onMouseMoved(mouseX, mouseY);
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

		float rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
		float scale = (float) MinecraftClient.getInstance().getWindow().getScaleFactor();

		float sBGY = (((sY / limitFraction.get().intValue()) * progressFraction.get().intValue()));

		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY - sBGY) * scale)), (int) (sX * scale), (int) ((sY - sBGY) * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), z, getWidth(), getHeight(), getBackgroundTexture());

		GL11.glScissor((int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sBGY * scale));

		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), z, getWidth(), getHeight(), getForegroundTexture());

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		if (isFocused()) {
			tooltipText.setText(FluidUtilities.fraction(progressFraction.get(), limitFraction.get(), unit));
			tooltipText.setWidth(TextRenderer.width(tooltipText.getText())); // update width dumb

			tooltip.draw(matrices, provider);

			RenderSystem.translatef(0, 0, 250);

			tooltipText.draw(matrices, provider);

			RenderSystem.translatef(0, 0, -250);
		}
	}
}
