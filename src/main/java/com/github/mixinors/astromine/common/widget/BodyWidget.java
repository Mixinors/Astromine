package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.registry.common.AMBodies;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class BodyWidget extends Widget {
	protected Supplier<Texture> foregroundTexture;
	
	protected Texture selectedTexture = new PartitionedTexture(AMCommon.id("textures/widget/body_selected.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	protected Texture unselectedTexture = new PartitionedTexture(AMCommon.id("textures/widget/body_unselected.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	
	private final Body body;
	
	private double angle = 0.0D;
	
	private double prevOrbitX = 0.0D;
	private double prevOrbitY = 0.0D;
	
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
		
		angle += (180.0D / 360.0D) * tickDelta * 0.1D * speed;
		angle %= 360.0D;
		
		if (trailPositions.size() > 0) {
			var prevTrailPosition = trailPositions.get(trailPositions.size() - 1);
			var trailPosition = new Position((float) orbitX + getWidth() / 2.0F, (float) orbitY + getHeight() / 2.0F);
			
			if (trailPosition.distanceTo(prevTrailPosition) > 1.0F) {
				trailPositions.add(trailPosition);
			}
		} else {
			trailPositions.add(new Position((float) orbitX + getWidth() / 2.0F, (float) orbitY + getHeight() / 2.0F));
		}
		if (trailPositions.size() >= 250) {
			trailPositions.remove(0);
		}
		
		if (trailPositions.size() >= 2) {
			RenderSystem.lineWidth(10.0F);
			
			var tesselator = Tessellator.getInstance();
			var builder = tesselator.getBuffer();
			
			builder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
			
			var prevTrailPosition = trailPositions.get(0);
			
			var focused = isFocused();
			
			for (var i = 1; i < trailPositions.size(); ++i) {
				var trailPosition = trailPositions.get(i);
			
				if (!isPointWithin(prevTrailPosition.getX(), prevTrailPosition.getY())) {
					if (focused) {
						builder.vertex(prevTrailPosition.getX(), prevTrailPosition.getY(), 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).next();
						builder.vertex(trailPosition.getX(), trailPosition.getY(), 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).next();
					} else {
						builder.vertex(prevTrailPosition.getX(), prevTrailPosition.getY(), 0.0F).color(0.0F, 0.58F, 1.0F, 1.0F).next();
						builder.vertex(trailPosition.getX(), trailPosition.getY(), 0.0F).color(0.0F, 0.58F, 1.0F, 1.0F).next();
					}
				}
				
				prevTrailPosition = trailPosition;
			}
			
			tesselator.draw();
		}
		
		foregroundTexture.get().draw(matrices, provider, (float) orbitX, (float) orbitY, getWidth(), getHeight());
		
		if (isFocused()) {
			selectedTexture.draw(matrices, provider, (float) orbitX - 2.0F, (float) orbitY - 2.0F, getWidth() + 4.0F, getHeight() + 4.0F);
		} else {
			unselectedTexture.draw(matrices, provider, (float) orbitX - 2.0F, (float) orbitY - 2.0F, getWidth() + 4.0F, getHeight() + 4.0F);
		}
	}
}
