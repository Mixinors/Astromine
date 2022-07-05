package com.github.mixinors.astromine.common.screen.handler.body;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.github.mixinors.astromine.common.widget.BodyWidget;
import com.github.mixinors.astromine.registry.common.AMBodies;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.MouseReleasedEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class BodySelectorScreenHandler extends BaseScreenHandler {
	public BodySelectorScreenHandler(int syncId, PlayerEntity player) {
		super(AMScreenHandlers.BODY_SELECTOR.get(), syncId, player);
	}
	
	@Override
	public void init(int width, int height) {
		var panel = new PanelWidget();
		panel.setPosition(width / 2 - 768 / 2, height / 2 - 768 / 2);
		panel.setSize(1, 1);
		
		add(panel);
		
		for (var b : BodyManager.getBodies()) {
			var body = new BodyWidget(b);
			body.setPosition(0, 0);
			body.setSize(32, 32);
			
			panel.add(body);
		}
		
		var r = new Random();
		Vec3d[] sP = { Vec3d.ZERO };
		
		panel.onEvent(EventType.MOUSE_CLICKED, (MouseClickedEvent event) -> {
			var x = event.x() / InstanceUtil.getClient().getWindow().getScaledWidth() * 384_000;
			var z = event.y() / InstanceUtil.getClient().getWindow().getScaledHeight() * 384_000;
			
			sP[0] = new Vec3d(x, 0, z);
		});
		
		panel.onEvent(EventType.MOUSE_RELEASED, (MouseReleasedEvent event) -> {
			var x = event.x() / InstanceUtil.getClient().getWindow().getScaledWidth() * 384_000;
			var z = event.y() / InstanceUtil.getClient().getWindow().getScaledHeight() * 384_000;
			
			var v = new Vec3d(x, 0, z).subtract(sP[0]);
			
			var b = new Body(sP[0], v, 1, 0, null, null);
			
			BodyManager.add(b);
			
			panel.add(new BodyWidget(b));
		});
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return player.isAlive();
	}
}
