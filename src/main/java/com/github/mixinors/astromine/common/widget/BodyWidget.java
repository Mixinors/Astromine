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
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class BodyWidget extends Widget {
	private final Body body;
	
	// Required because isFocused is updated by Hammer,
	// and that causes issues on mouse movement.
	private boolean hovered = false;
	
	public BodyWidget(Body body) {
		this.body = body;
	}
	
	public Body getBody() {
		return body;
	}
	
	@Override
	public float getX() {
		// Return center X of screen if no orbit is present.
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
		// Return center Y of screen if no orbit is present.
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
		return ImmutableList.of(body.name().asOrderedText());
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		// Get the offset caused by mouse dragging.
		var offsetX = BodySelectorHandledScreen.getOffsetX();
		var offsetY = BodySelectorHandledScreen.getOffsetY();
		
		// Get the zoom caused by mouse zooming.
		var zoom = BodySelectorHandledScreen.getZoom();
		
		if (body.orbit() != null) {
			var orbit = body.orbit();
			
			Vec2f prevPos = null;
			
			var focusedColor = new Color(1.0F, 1.0F, 1.0F, 0.1F);
			var unfocusedColor = new Color(0.4F, 0.4F, 0.4F, 0.1F);
			
			var orbitOffset = orbit.orbitedBodyOffset();
		
			// Draw elliptical orbit around the body.
			for (var angle = 0.0F; angle <= 360.0F; angle += 1.0F) {
				var trailX = orbit.width() * Math.cos(Math.toRadians(angle));
				var trailY = orbit.height() * Math.sin(Math.toRadians(angle));
				
				// Apply offset, if present.
				if (orbitOffset != null) {
					trailX += orbitOffset.getX();
					trailY += orbitOffset.getY();
				}
				
				// Apply orbited body's position as an offset, if present.
				// Required since, by default, this renders the orbit at the origin.
				if (orbit.orbitedBodyId() != null) {
					var orbitedBody = AMRegistries.BODY.get(orbit.orbitedBodyId());
					
					if (orbitedBody != null) {
						trailX += orbitedBody.getOrbitX() + orbitedBody.getScale();
						trailY += orbitedBody.getOrbitY() + orbitedBody.getScale();
					}
				}
				
				// Scale trail positions by the zoom.
				trailX *= zoom;
				trailY *= zoom;
				
				// Offset trail positions by the offset.
				trailX += offsetX;
				trailY += offsetY;
				
				var pos = new Vec2f((float) trailX, (float) trailY);
				
				if (prevPos == null) {
					prevPos = pos;
				} else {
					// Draw segment or orbit line.
					if (isHovered()) {
						DrawingUtil.drawLine(matrices, provider, pos.x, pos.y, 0.0F, prevPos.x, prevPos.y, 0.0F, focusedColor, RenderLayer.getLines());
					} else {
						DrawingUtil.drawLine(matrices, provider, pos.x, pos.y, 0.0F, prevPos.x, prevPos.y, 0.0F, unfocusedColor, RenderLayer.getLines());
					}
					
					prevPos = pos;
				}
			}
		}
		
		// Calculate the body's position.
		var orbitX = 0.0D;
		var orbitY = 0.0D;
		
		// If the body is orbiting another body, calculate the position.
		if (body.orbit() != null) {
			var orbit = body.orbit();
			
			// Add the orbit-relative position, based on the body's angle.
			orbitX += orbit.width() * Math.cos(body.getAngle());
			orbitY += orbit.height() * Math.sin(body.getAngle());
			
			var orbitOffset = orbit.orbitedBodyOffset();
			
			// Add the orbit's offset, if present.
			if (orbitOffset != null) {
				orbitX += orbitOffset.getX();
				orbitY += orbitOffset.getY();
			}
			
			// Add the orbited body's position as an offset, if present.
			if (orbit.orbitedBodyId() != null) {
				var orbitedBody = AMRegistries.BODY.get(orbit.orbitedBodyId());
				
				if (orbitedBody != null) {
					orbitX += orbitedBody.getOrbitX();
					orbitY += orbitedBody.getOrbitY();
				}
			}
		} else {
			// Otherwise, the body is orbiting the origin.
			orbitX += getX();
			orbitY += getY();
		}
		
		// Update the body's position, before rendering.
		body.setOrbitX(orbitX);
		body.setOrbitY(orbitY);
		
		// Scale the body's position by the zoom.
		orbitX *= zoom;
		orbitY *= zoom;
		
		// Offset the body's position by the offset.
		orbitX += offsetX;
		orbitY += offsetY;
		
		var speed = 1.0D;
		
		// Zero the speed if the any body is hovered.
		if (rootCollection != null && rootCollection.getScreenHandler() instanceof BodySelectorScreenHandler screenHandler) {
			for (var child : screenHandler.getAllChildren()) {
				if (child instanceof BodyWidget bodyChild && bodyChild.isHovered()) {
					speed = 0.0D;
				}
			}
		}
		
		if (matrices != null) {
			// Calculate the top left screen position of the body.
			var minPos = new Vector4f((float) orbitX - getWidth() * zoom, (float) orbitY - getWidth() * zoom, -getWidth() * zoom, 1.0F);
			
			// Calculate the bottom right screen position of the body.
			var maxPos = new Vector4f((float) orbitX + getWidth() * zoom, (float) orbitY + getWidth() * zoom, +getWidth() * zoom, 1.0F);
			
			// Get the mouse's position.
			var mousePos = new Vector3f(PositionUtil.getMouseX(), PositionUtil.getMouseY(), 0.0F);
			
			body.setPrevScale(body.getScale());
			
			var scale = 1.0F;
			
			// Update hovered (focused) status based on top left/bottom right and mouse positions.
			if (mousePos.x() > minPos.x() && mousePos.x() < maxPos.x() && mousePos.y() > minPos.y() && mousePos.y() < maxPos.y()) {
				scale = (float) MathHelper.lerp(tickDelta / 2.0D, body.getPrevScale(), 1.25D);
				
				setHovered(true);
			} else {
				scale = (float) MathHelper.lerp(tickDelta / 2.0D, body.getPrevScale(), 1.0D);
				
				setHovered(false);
			}
			
			body.setScale(scale);
			
			// Update the body's angle based on its orbiting speed, if any.
			// TODO: Update; a body's orbit speed may be unrelated to its spin speed.
			if (body.orbit() != null) {
				var orbit = body.orbit();
				
				var angle = body.getAngle();
				
				angle += (orbit.speed() / 360.0D * 16.0D) * tickDelta * 0.1D * speed;
				angle %= 360.0D;
				
				body.setAngle(angle);
			}
			
			var texture = body.texture();
			
			// Draw the body.
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
		}
	}
}
