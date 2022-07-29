package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.client.screen.BodySelectorHandledScreen;
import com.github.mixinors.astromine.client.util.DrawingUtil;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.screen.handler.body.BodySelectorScreenHandler;
import com.github.mixinors.astromine.registry.client.AMRenderLayers;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

import java.util.Collection;

public class BodyWidget extends Widget {
	private final Body body;
	
	private boolean focused = false;
	
	public BodyWidget(Body body) {
		this.body = body;
	}
	
	public Body getBody() {
		return body;
	}
	
	@Override
	public float getX() {
		if (body.getOrbit() == null) {
			var client = InstanceUtil.getClient();
			var window = client.getWindow();
			
			var windowWidth = window.getScaledWidth();
			
			return ((windowWidth / 2.0F) - (getWidth() / 2.0F));
		}
		
		return (float) body.getOrbitX();
	}
	
	@Override
	public float getY() {
		if (body.getOrbit() == null) {
			var client = InstanceUtil.getClient();
			var window = client.getWindow();
			
			var windowHeight = window.getScaledHeight();
			
			return ((windowHeight / 2.0F) - (getWidth() / 2.0F));
		}
		
		return (float) body.getOrbitY();
	}
	
	@Override
	public boolean isFocused() {
		return focused;
	}
	
	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	@Override
	public Collection<Text> getTooltips() {
		return ImmutableList.of(body.getName(), body.getDescription());
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var orbitX = 0.0D;
		var orbitY = 0.0D;
		
		if (body.getOrbit() != null) {
			var orbit = body.getOrbit();
			
			orbitX += orbit.width() * Math.cos(body.getAngle());
			orbitY += orbit.height() * Math.sin(body.getAngle());
			
			if (orbit.orbitedBodyId() != null) {
				var orbitedBody = AMRegistries.BODY.get(orbit.orbitedBodyId());
				
				if (orbitedBody != null) {
					orbitX += orbitedBody.getOrbitX() + getWidth() / 2.0F;
					orbitY += orbitedBody.getOrbitY() + getHeight() / 2.0F;
				}
			}
		} else {
			orbitX += getX();
			orbitY += getY();
		}
		
		body.setOrbitX(orbitX);
		body.setOrbitY(orbitY);
		
		var offsetX = BodySelectorHandledScreen.getOffsetX();
		var offsetY = BodySelectorHandledScreen.getOffsetY();
		
		var zoom = BodySelectorHandledScreen.getZoom();
		
		orbitX *= zoom;
		orbitY *= zoom;
		
		orbitX += offsetX;
		orbitY += offsetY;
		
		var speed = 1.0D;
		
		if (rootCollection != null && rootCollection.getScreenHandler() instanceof BodySelectorScreenHandler screenHandler) {
			for (var child : screenHandler.getAllChildren()) {
				if (child instanceof BodyWidget && child.isFocused()) {
					speed = 0.0D;
				}
			}
		}
		
		if (matrices != null) {
			matrices.push();
			
			matrices.translate(orbitX, orbitY, 10.0F);
			
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0F));
			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F));
			
			var minPos = new Vector4f((float) orbitX - getWidth() * zoom, (float) orbitY - getWidth() * zoom, -getWidth() * zoom, 1.0F);
			var maxPos = new Vector4f((float) orbitX + getWidth() * zoom, (float) orbitY + getWidth() * zoom, +getWidth() * zoom, 1.0F);
			
			var mousePos = new Vec3f(PositionUtil.getMouseX(), PositionUtil.getMouseY(), 0.0F);
			
			body.setPrevScale(body.getScale());
			
			var scale = 1.0F;
			
			if (mousePos.getX() > minPos.getX() && mousePos.getX() < maxPos.getX() && mousePos.getY() > minPos.getY() && mousePos.getY() < maxPos.getY()) {
				scale = (float) MathHelper.lerp(tickDelta / 2.0D, body.getPrevScale(), 1.25D);
				
				setFocused(true);
			} else {
				scale = (float) MathHelper.lerp(tickDelta / 2.0D, body.getPrevScale(), 1.0D);
			}
			
			body.setScale(scale);
			
			if (body.getOrbit() != null) {
				var orbit = body.getOrbit();
				
				var angle = body.getAngle();
				
				angle += (orbit.speed() / 360.0D * 16.0D) * tickDelta * 0.1D * speed;
				angle %= 360.0D;
				
				body.setAngle(angle);
				
				if (orbit.tidalLocked()) {
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F - 360.0F + (float) Math.toDegrees(body.getAngle())));
				} else {
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F - (float) Math.toDegrees(body.getAngle())));
				}
			}
			
			var texture = body.getTexture();
			
			DrawingUtil.drawCube(
					matrices,
					provider,
					0.0F, 0.0F, 0.0F,
					getWidth() * zoom * scale, getHeight() * zoom * scale, getLength() * zoom * scale,
					new Color(0xEFEFEFFFL),
					AMRenderLayers.getBody(texture.up()),
					AMRenderLayers.getBody(texture.down()),
					AMRenderLayers.getBody(texture.north()),
					AMRenderLayers.getBody(texture.south()),
					AMRenderLayers.getBody(texture.west()),
					AMRenderLayers.getBody(texture.east())
			);
			
			matrices.pop();
		}
	}
}
