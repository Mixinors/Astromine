package com.github.chainmailstudios.astromine.common.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import com.mojang.blaze3d.systems.RenderSystem;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import org.lwjgl.opengl.GL11;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;
import spinnery.widget.WAbstractBar;
import spinnery.widget.WTooltip;
import spinnery.widget.api.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class WFractionalVerticalBar extends WAbstractBar {
	private Supplier<Fraction> progressFraction = Fraction::empty;
	private Supplier<Fraction> limitFraction = Fraction::empty;
	
	private final WTooltip tooltip = new WTooltip();
	private final List<Text> tooltipText = new ArrayList<>();
	
	private Text unit;
	
	public WFractionalVerticalBar() {
		tooltip.setParent(this);
		tooltip.setInterface(getInterface());
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
	
	public List<Text> getTooltip() {
		return tooltipText;
	}
	
	@Override
	public void onMouseMoved(float mouseX, float mouseY) {
		tooltip.setHidden(!isFocused());
		
		if (!tooltip.isHidden()) {
			tooltip.setWidth(tooltipText.stream().map(TextRenderer::width).max(Integer::compareTo).orElse(0));
			tooltip.setHeight(TextRenderer.height() * tooltipText.size());
			
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
			tooltipText.clear();
			tooltipText.add(FluidUtilities.rawFraction(progressFraction.get(), limitFraction.get(), unit));
			tooltipText.add(new TranslatableText("text.astromine.tooltip.fractional_value", progressFraction.get().toPrettyString(), limitFraction.get().toPrettyString()));
			
			drawTooltips(matrices, provider);
		}
	}

	protected void drawTooltips(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		tooltip.setWidth(tooltipText.stream().map(TextRenderer::width).max(Integer::compareTo).orElse(0));
		tooltip.setHeight(TextRenderer.height() * tooltipText.size());

		tooltip.draw(matrices, provider);

		RenderSystem.translatef(0, 0, 250);

		Position position = Position.of(tooltip, 1, 1, 0);
		float lineX = position.getX();
		float lineY = position.getY();
		float lineZ = position.getZ();
		for (Text text : tooltipText) {
			TextRenderer.pass().text(text).at(lineX, lineY, lineZ).scale(1.0f).render(matrices, provider);
			lineY += TextRenderer.height();
		}

		RenderSystem.translatef(0, 0, -250);
	}
}
