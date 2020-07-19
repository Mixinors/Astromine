package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedItemScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public abstract class DefaultedItemHandledScreen<T extends DefaultedBlockEntityScreenHandler> extends DefaultedBlockEntityHandledScreen<T> {
	public DefaultedItemHandledScreen(Text name, DefaultedItemScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, (T) linkedScreenHandler, player);
	}
}
