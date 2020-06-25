package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedFluidContainer;
import com.github.chainmailstudios.astromine.common.widget.WTransferTypeSelectorPanel;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.common.container.BaseContainer;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Collection;

public abstract class DefaultedFluidContainerScreen<T extends BaseContainer> extends BaseContainerScreen<T> {
	public WInterface mainInterface;
	public WPanel mainPanel;
	public Collection<WSlot> playerSlots;
	public WFluidVolumeFractionalVerticalBar fluidBar;

	public DefaultedFluidContainerScreen(Text name, DefaultedFluidContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);

		mainInterface = getInterface();

		mainPanel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(176, 160));

		mainPanel.center();
		mainPanel.setOnAlign(WAbstractWidget::center);

		playerSlots = WSlot.addPlayerInventory(Position.of(mainPanel, 7, 77, 0), Size.of(18, 18), mainPanel);

		fluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 7, 0), Size.of(24, 48));

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		fluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(0));

		BlockEntityTransferComponent transferComponent = componentProvider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

		mainPanel.createChild(WTransferTypeSelectorPanel::new, Position.of(mainPanel, mainPanel.getWidth(), 0, 0), Size.of(96, 100))
				.setProvider(componentProvider)
				.setComponent(transferComponent);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
		super.render(matrices, mouseX, mouseY, tickDelta);
	}
}
