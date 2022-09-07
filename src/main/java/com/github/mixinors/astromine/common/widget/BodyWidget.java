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
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

import java.util.List;

public class BodyWidget extends Widget {
	private final Body body;
	
	private boolean drawing = false;
	private boolean hovered = false;
	
	public BodyWidget(Body body) {
		this.body = body;
	}
	
	public Body getBody() {
		return body;
	}
	
	@Override
	public float getX() {
		if (body.orbit() == null) {
			var client = InstanceUtil.getClient();
			var window = client.getWindow();
			
			var windowWidth = window.getScaledWidth();
			
			return ((windowWidth / 2.0F) - (getWidth() / 2.0F));
		}
		
		return (float) body.getOrbitX();
	}
	
	@Override
	public float getY() {
		if (body.orbit() == null) {
			var client = InstanceUtil.getClient();
			var window = client.getWindow();
			
			var windowHeight = window.getScaledHeight();
			
			return ((windowHeight / 2.0F) - (getWidth() / 2.0F));
		}
		
		return (float) body.getOrbitY();
	}
	
	public boolean isHovered() {
		return hovered;
	}
	
	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}
	
	@Override
	public boolean isFocused() {
		return hovered;
	}
	
	@Override
	public List<OrderedText> getTooltip() {
		var client = InstanceUtil.getClient();;
		
		var name = body.name();
		var description = body.description().copy().formatted(Formatting.GRAY);
		
		return ImmutableList.<OrderedText>builder().add(name.asOrderedText()).addAll(client.textRenderer.wrapLines(description, 128)).build();
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var offsetX = BodySelectorHandledScreen.getOffsetX();
		var offsetY = BodySelectorHandledScreen.getOffsetY();
		
		var zoom = BodySelectorHandledScreen.getZoom();
		
		if (body.orbit() != null) {
			var orbit = body.orbit();
			
			Vec2f prevPos = null;
			
			var focusedColor = new Color(1.0F, 1.0F, 1.0F, 0.1F);
			var unfocusedColor = new Color(0.4F, 0.4F, 0.4F, 0.1F);
			
			var orbitOffset = orbit.orbitedBodyOffset();
			
			for (var angle = 0.0F; angle <= 360.0F; angle += 1.0F) {
				var trailX = orbit.width() * Math.cos(Math.toRadians(angle));
				var trailY = orbit.height() * Math.sin(Math.toRadians(angle));
				
				if (orbitOffset != null) {
					trailX += orbitOffset.getX();
					trailY += orbitOffset.getY();
				}
				
				if (orbit.orbitedBodyId() != null) {
					var orbitedBody = AMRegistries.BODY.get(orbit.orbitedBodyId());
					
					if (orbitedBody != null) {
						trailX += orbitedBody.getOrbitX() + orbitedBody.getScale();
						trailY += orbitedBody.getOrbitY() + orbitedBody.getScale();
					}
				}
				
				trailX *= zoom;
				trailY *= zoom;
				
				trailX += offsetX;
				trailY += offsetY;
				
				var pos = new Vec2f((float) trailX, (float) trailY);
				
				if (prevPos == null) {
					prevPos = pos;
				} else {
					if (isHovered()) {
						DrawingUtil.drawLine(matrices, provider, pos.x, pos.y, 0.0F, prevPos.x, prevPos.y, 0.0F, focusedColor, RenderLayer.getLines());
					} else {
						DrawingUtil.drawLine(matrices, provider, pos.x, pos.y, 0.0F, prevPos.x, prevPos.y, 0.0F, unfocusedColor, RenderLayer.getLines());
					}
					
					prevPos = pos;
				}
			}
		}
		
		var orbitX = 0.0D;
		var orbitY = 0.0D;
		
		if (body.orbit() != null) {
			var orbit = body.orbit();
			
			orbitX += orbit.width() * Math.cos(body.getAngle());
			orbitY += orbit.height() * Math.sin(body.getAngle());
			
			var orbitOffset = orbit.orbitedBodyOffset();
			
			if (orbitOffset != null) {
				orbitX += orbitOffset.getX();
				orbitY += orbitOffset.getY();
			}
			
			if (orbit.orbitedBodyId() != null) {
				var orbitedBody = AMRegistries.BODY.get(orbit.orbitedBodyId());
				
				if (orbitedBody != null) {
					orbitX += orbitedBody.getOrbitX();
					orbitY += orbitedBody.getOrbitY();
				}
			}
		} else {
			orbitX += getX();
			orbitY += getY();
		}
		
		body.setOrbitX(orbitX);
		body.setOrbitY(orbitY);
		
		orbitX *= zoom;
		orbitY *= zoom;
		
		orbitX += offsetX;
		orbitY += offsetY;
		
		var speed = 1.0D;
		
		if (rootCollection != null && rootCollection.getScreenHandler() instanceof BodySelectorScreenHandler screenHandler) {
			for (var child : screenHandler.getAllChildren()) {
				if (isHovered() || (child instanceof BodyWidget bodyChild && bodyChild.isHovered())) {
					speed = 0.0D;
				}
			}
		}
		
		if (matrices != null) {
			matrices.push();
			
			matrices.translate(orbitX, orbitY, 100.0F);
			
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0F));
			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F));
			
			var minPos = new Vector4f((float) orbitX - getWidth() * zoom, (float) orbitY - getWidth() * zoom, -getWidth() * zoom, 1.0F);
			var maxPos = new Vector4f((float) orbitX + getWidth() * zoom, (float) orbitY + getWidth() * zoom, +getWidth() * zoom, 1.0F);
			
			var mousePos = new Vec3f(PositionUtil.getMouseX(), PositionUtil.getMouseY(), 0.0F);
			
			body.setPrevScale(body.getScale());
			
			var scale = 1.0F;
			
			if (mousePos.getX() > minPos.getX() && mousePos.getX() < maxPos.getX() && mousePos.getY() > minPos.getY() && mousePos.getY() < maxPos.getY()) {
				scale = (float) MathHelper.lerp(tickDelta / 2.0D, body.getPrevScale(), 1.25D);
				
				setHovered(true);
			} else {
				scale = (float) MathHelper.lerp(tickDelta / 2.0D, body.getPrevScale(), 1.0D);
				
				setHovered(false);
			}
			
			body.setScale(scale);
			
			if (body.orbit() != null) {
				var orbit = body.orbit();
				
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
			
			var texture = body.texture();
			
			DrawingUtil.drawBody(
					provider,
					(float) orbitX, (float) orbitY, 100.0F,
					getWidth() * zoom * scale, getHeight() * zoom * scale, getLength() * zoom * scale,
					(float) body.getAngle(),
					body.orbit() != null && body.orbit().tidalLocked(),
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
