package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.client.util.DrawingUtil;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.registry.client.AMRenderLayers;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class BodyWidget extends Widget {
	protected Supplier<Texture> foregroundTexture;
	
	private final Body body;
	
	private double angle = 0.0D;
	
	private double prevOrbitX = 0.0D;
	private double prevOrbitY = 0.0D;
	
	private double prevScale = 1.0D;
	
	private final List<Position> trailPositions = new ArrayList<>();
	
	public BodyWidget(Body body) {
		this.body = body;
		
		this.setPosition(body.getPosition());
		this.setSize(body.getSize());
		
		var bodyTexture = new ImageTexture(body.getTexture());
		
		this.foregroundTexture = () -> bodyTexture;
	}
	
	@Override
	public float getX() {
		if (body.getOrbitedBody() == null) {
			var client = InstanceUtil.getClient();
			var window = client.getWindow();
			
			var windowWidth = window.getScaledWidth();
			
			return (windowWidth / 2.0F) - (getWidth() / 2.0F);
		}
		
		return (float) prevOrbitX;
	}
	
	@Override
	public float getY() {
		if (body.getOrbitedBody() == null) {
			var client = InstanceUtil.getClient();
			var window = client.getWindow();
			
			var windowHeight = window.getScaledHeight();
			
			return  (windowHeight / 2.0F) - (getHeight() / 2.0F);
		}
		
		return (float) prevOrbitY;
	}
	
	public Body getBody() {
		return body;
	}
	
	@Override
	public Collection<Text> getTooltips() {
		return body.getTooltips();
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var orbitX = body.getOrbitWidth() * Math.cos(angle);
		var orbitY = body.getOrbitHeight() * Math.sin(angle);
		
		var orbitedBody = body.getOrbitedBody();
		
		if (orbitedBody != null) {
			var orbitedWidget = orbitedBody.getWidget();
			
			if (orbitedWidget != null) {
				orbitX += body.getOrbitedBody().getWidget().getX() + (getWidth() / 2.0F);
				orbitY += body.getOrbitedBody().getWidget().getY() + (getHeight() / 2.0F);
			}
		} else {
			var client = InstanceUtil.getClient();
			var window = client.getWindow();
			
			var windowWidth = window.getScaledWidth();
			var windowHeight = window.getScaledHeight();
			
			orbitX += (windowWidth / 2.0F) - (getWidth() / 2.0F);
			orbitY += (windowHeight / 2.0F) - (getHeight() / 2.0F);
		}
		
		prevOrbitX = orbitX;
		prevOrbitY = orbitY;
		
		var speed = 1.0D;
		
		for (var child : rootCollection.getScreenHandler().getAllChildren()) {
			if (child instanceof BodyWidget && child.isFocused()) {
				speed = 0.0D;
			}
		}
		
		angle += (body.getOrbitSpeed() / 360.0D * 16.0D) * tickDelta * 0.1D * speed;
		angle %= 360.0D;
		
		matrices.push();
		
		matrices.translate(orbitX, orbitY, 10.0F);
		
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0F));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F));

		var minPos = new Vector4f((float) orbitX - getWidth(), (float) orbitY - getWidth(), -getWidth(), 1.0F);
		var maxPos = new Vector4f((float) orbitX + getWidth(), (float) orbitY + getWidth(), +getWidth(), 1.0F);
		
		var mousePos = new Vec3f(PositionUtil.getMouseX(), PositionUtil.getMouseY(), 0.0F);
		
		var scale = 1.0F;
		
		if (mousePos.getX() > minPos.getX() && mousePos.getX() < maxPos.getX() && mousePos.getY() > minPos.getY() && mousePos.getY() < maxPos.getY()) {
			scale = (float) MathHelper.lerp(tickDelta / 2.0D, prevScale, 1.25D);
			
			setFocused(true);
		} else {
			scale = (float) MathHelper.lerp(tickDelta / 2.0D, prevScale, 1.0D);
		}
		
		prevScale = scale;

		if (body.getOrbitedBody() != null) {
			if (body.isOrbitTidalLocked()) {
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F - 360.0F + (float) Math.toDegrees(body.getWidget().angle)));
			} else {
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F - (float) Math.toDegrees(this.angle)));
			}
		}

		matrices.scale(scale, scale, scale);
		
		DrawingUtil.drawCube(
				matrices,
				provider,
				0.0F, 0.0F, 0.0F,
				getWidth(), getWidth(), getWidth(),
				new Color(0xEFEFEFFFL),
				AMRenderLayers.getBody(body.getTexture())
		);
		
		matrices.pop();
	}
}
