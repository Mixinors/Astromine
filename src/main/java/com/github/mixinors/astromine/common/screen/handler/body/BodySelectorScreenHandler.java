package com.github.mixinors.astromine.common.screen.handler.body;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.widget.BodySelectionWidget;
import com.github.mixinors.astromine.common.widget.BodyWidget;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Consumer;

public class BodySelectorScreenHandler extends BaseScreenHandler {
	public static final float SELECT_BUTTON_WIDTH = 18.0F;
	public static final float SELECT_BUTTON_HEIGHT = 18.0F;
	
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
			
			var bodySelectionWidget = new BodySelectionWidget();
			bodySelectionWidget.setSize(256.0F, 64.0F);
			
			bodySelectionWidget.setSelectListener((body) -> {
				// TODO: Launch the player's rocket to this body.
			});
			
			add(bodySelectionWidget);
			
			for (var body : AMRegistries.BODY.getValues()) {
				var widget = new BodyWidget(body);
				widget.onEvent(EventType.MOUSE_CLICKED, (MouseClickedEvent event) -> {
					if (!widget.isHovered()) return;
					
					if (event.button() == 0) {
						bodySelectionWidget.setBody(body);
					} else if (event.button() == 1) {
						bodySelectionWidget.setBody(null);
					}
				});
				
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
