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
import java.util.Collection;
import java.util.Random;

public class BodySelectorScreenHandler extends BaseScreenHandler {
	private static final ThreadLocal<BodyWidget> SUN = ThreadLocal.withInitial(() -> new BodyWidget(AMBodies.SUN));
	private static final ThreadLocal<BodyWidget> EARTH = ThreadLocal.withInitial(() -> new BodyWidget(AMBodies.EARTH));
	private static final ThreadLocal<BodyWidget> MOON = ThreadLocal.withInitial(() -> new BodyWidget(AMBodies.MOON));
	private static final ThreadLocal<BodyWidget> MARS = ThreadLocal.withInitial(() -> new BodyWidget(AMBodies.MARS));
	
	private static final ThreadLocal<Collection<ThreadLocal<BodyWidget>>> BODIES = ThreadLocal.withInitial(ArrayList::new);
	
	private static int PREV_WIDTH = -1;
	private static int PREV_HEIGHT = -1;
	
	static {
		BODIES.get().add(SUN);
		BODIES.get().add(EARTH);
		BODIES.get().add(MOON);
		BODIES.get().add(MARS);
	}
	
	public BodySelectorScreenHandler(int syncId, PlayerEntity player) {
		super(AMScreenHandlers.BODY_SELECTOR.get(), syncId, player);
	}
	
	public static void update(float tickDelta) {
		for (var body : BODIES.get()) {
			body.get().draw(null, null, tickDelta);
		}
	}
	
	@Override
	public void init(int width, int height) {
		if (isClient()) {
			var panel = new PanelWidget();
			panel.setPosition(width / 2.0F - 768.0F / 2.0F, height / 2.0F - 768.0F / 2.0F);
			panel.setSize(1, 1);
			
			add(panel);
			
			// Add Sun.
			var sun = SUN.get();
			
			if (width != PREV_WIDTH || height != PREV_HEIGHT) {
				sun.setPosition(width / 2.0F - sun.getWidth() / 2.0F, height / 2.0F - sun.getHeight() / 2.0F);
			}
			
			panel.add(sun);
			
			// Add Earth.
			var earth = EARTH.get();
			
			if (width != PREV_WIDTH || height != PREV_HEIGHT) {
				earth.setPosition(sun.getPosition().getX() + (width * 0.15F), sun.getPosition().getY() + (height * 0.15F));
			}
			
			panel.add(earth);
			
			// Add Moon.
			var moon = MOON.get();
			
			if (width != PREV_WIDTH || height != PREV_HEIGHT) {
				moon.setPosition(earth.getPosition().getX() + (width * 0.02F), earth.getPosition().getY() - (height * 0.02F));
			}

			panel.add(moon);
			
			// Add Mars.
			var mars = MARS.get();
			
			if (width != PREV_WIDTH || height != PREV_HEIGHT) {
				mars.setPosition(earth.getPosition().getX() + (width * 0.15F + width * 0.02F), earth.getPosition().getY() + (height * 0.15F + height * 0.02F));
			}
			
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
