package com.github.chainmailstudios.astromine.common.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public interface TooltipCallback {
	Event<TooltipCallback> EVENT = EventFactory.createArrayBacked(TooltipCallback.class, (listeners) -> ((stack, world, tooltip, context) -> {
		for (TooltipCallback listener : listeners) {
			listener.handle(stack, world, tooltip, context);
		}
	}));

	void handle(ItemStack stack, World world, List<Text> tooltip, TooltipContext context);
}
