package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedFluidScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public abstract class DefaultedFluidHandledScreen<T extends DefaultedBlockEntityScreenHandler> extends DefaultedBlockEntityHandledScreen<T> {
	public WFluidVolumeFractionalVerticalBar fluidBar;

	public DefaultedFluidHandledScreen(Text name, DefaultedFluidScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, (T) linkedScreenHandler, player);

		fluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 20, 0), Size.of(24, 48));

		ComponentProvider componentProvider = linkedScreenHandler.blockEntity;

		fluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(0));
	}
}
