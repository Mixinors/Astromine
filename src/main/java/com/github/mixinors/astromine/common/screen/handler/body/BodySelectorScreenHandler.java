package com.github.mixinors.astromine.common.screen.handler.body;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.github.mixinors.astromine.common.widget.BodyWidget;
import com.github.mixinors.astromine.registry.common.AMBodies;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.MouseReleasedEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Random;

public class BodySelectorScreenHandler extends BaseScreenHandler {
	public BodySelectorScreenHandler(int syncId, PlayerEntity player) {
		super(AMScreenHandlers.BODY_SELECTOR.get(), syncId, player);
	}
	
	@Override
	public void init(int width, int height) {
		if (isClient()) {
			var panel = new PanelWidget();
			panel.setPosition(width / 2.0F - 768.0F / 2.0F, height / 2.0F - 768.0F / 2.0F);
			panel.setSize(1, 1);
			
			add(panel);
			
			// Add Sun.
			var sun = new BodyWidget(AMBodies.SUN);
			sun.setPosition(width / 2.0F - sun.getWidth() / 2.0F, height / 2.0F - sun.getHeight() / 2.0F);
			
			sun.getBody().setWidget(sun);
			
			panel.add(sun);
			
			// Add Earth.
			var earth = new BodyWidget(AMBodies.EARTH);
			earth.setPosition(sun.getPosition().getX() + (width * 0.15F), sun.getPosition().getY() + (height * 0.15F));
			
			earth.getBody().setWidget(earth);
			
			panel.add(earth);
			
			// Add Moon.
			var moon = new BodyWidget(AMBodies.MOON);
			moon.setPosition(earth.getPosition().getX() + (width * 0.02F), earth.getPosition().getY() - (height * 0.02F));
			
			moon.getBody().setWidget(moon);
			
			panel.add(moon);
			
			// Add Mars.
			var mars = new BodyWidget(AMBodies.MARS);
			mars.setPosition(earth.getPosition().getX() + (width * 0.15F + width * 0.02F), earth.getPosition().getY() + (height * 0.15F + height * 0.02F));
			
			mars.getBody().setWidget(mars);
			
			panel.add(mars);
		}
	}
	
	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		
		if (isClient()) {
			for (var child : getChildren()) {
				if (child instanceof BodyWidget bodyWidget) {
					bodyWidget.getBody().setWidget(null);
				}
			}
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return player.isAlive();
	}
}
