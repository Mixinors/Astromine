package com.github.mixinors.astromine.common.screen.handler.body;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.widget.BodySelectionWidget;
import com.github.mixinors.astromine.common.widget.BodyWidget;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Consumer;

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
			
			var bodySelectionWidget = new BodySelectionWidget(AMRegistries.BODY.get(AMCommon.id("earth")));
			bodySelectionWidget.setPosition(0.0F, 0.0F);
			bodySelectionWidget.setSize(128, 32);
			
			add(bodySelectionWidget);
			
			for (var body : AMRegistries.BODY.getValues()) {
				var widget = new BodyWidget(body);
				
				if (body.orbit() != null) {
					var orbit = body.orbit();
					
					if (orbit.orbitedBodyId() != null) {
						if (orbit.orbitedBodyOffset() != null) {
							var orbitedBodyOffset = orbit.orbitedBodyOffset();
							
							widget.setPosition(orbitedBodyOffset.getX(), orbitedBodyOffset.getY());
						}
					}
				} else {
					widget.setPosition(width / 2.0F, height / 2.0F);
				}
				
				widget.setSize(body.size());
				
				panel.add(widget);
			}
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return player.isAlive();
	}
}
