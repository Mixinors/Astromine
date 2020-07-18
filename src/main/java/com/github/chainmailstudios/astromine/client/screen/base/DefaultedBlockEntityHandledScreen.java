package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WTransferTypeSelectorPanel;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Collection;

public class DefaultedBlockEntityHandledScreen<T extends DefaultedBlockEntityScreenHandler> extends DefaultedHandledScreen<T> {
	public WInterface mainInterface;
	public WPanel mainPanel;
	public WTransferTypeSelectorPanel transferPanel;
	public Collection<WSlot> playerSlots;

	public DefaultedBlockEntityHandledScreen(Text name, T linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		mainInterface = getInterface();

		mainPanel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(176, 160));

		addTitle(mainPanel);

		mainPanel.center();
		mainPanel.setOnAlign(WAbstractWidget::center);

		playerSlots = WSlot.addPlayerInventory(Position.of(mainPanel, 7, mainPanel.getHeight() - 18 - 7 - (18 * 3), 2), Size.of(18, 18), mainPanel);

		ComponentProvider componentProvider = ComponentProvider.fromBlockEntity(linkedScreenHandler.syncBlockEntity);

		BlockEntityTransferComponent transferComponent = componentProvider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

		transferPanel = mainPanel.createChild(WTransferTypeSelectorPanel::new, Position.of(mainPanel, mainPanel.getWidth(), 0, 0), Size.of(76, 100))
				.setBlockPos(linkedScreenHandler.syncBlockEntity.getPos())
				.setProvider(componentProvider)
				.setComponent(transferComponent);
	}
}
