package com.github.mixinors.astromine.common.screen.handler.body;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.github.mixinors.astromine.common.widget.BodyWidget;
import com.github.mixinors.astromine.registry.common.AMBodies;
import com.github.mixinors.astromine.registry.common.AMRegistries;
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
			
			for (var body : AMRegistries.BODY.getValues()) {
				var widget = new BodyWidget(body);
				
				if (body.getOrbit() != null) {
					var orbit = body.getOrbit();
					
					if (orbit.orbitedBodyOffset() != null) {
						var orbitedBodyOffset = orbit.orbitedBodyOffset();
						
						widget.setPosition(orbitedBodyOffset.getX(), orbitedBodyOffset.getY());
					}
				} else {
					widget.setPosition(width / 2.0F, height / 2.0F);
				}
			}
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return player.isAlive();
	}
}
