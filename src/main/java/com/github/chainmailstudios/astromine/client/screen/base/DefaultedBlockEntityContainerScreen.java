package com.github.chainmailstudios.astromine.client.screen.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedBlockEntityContainer;
import com.github.chainmailstudios.astromine.common.widget.WTransferTypeSelectorPanel;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Collection;

public class DefaultedBlockEntityContainerScreen<T extends DefaultedBlockEntityContainer> extends DefaultedContainerScreen<T> {
	public WInterface mainInterface;
	public WPanel mainPanel;
	public Collection<WSlot> playerSlots;

	public DefaultedBlockEntityContainerScreen(Text name, T linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		mainInterface = getInterface();

		mainPanel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(176, 160));

		addTitle(mainPanel);
		mainPanel.center();
		mainPanel.setOnAlign(WAbstractWidget::center);

		playerSlots = WSlot.addPlayerInventory(Position.of(mainPanel, 7, 77, 0), Size.of(18, 18), mainPanel);

		ComponentProvider componentProvider = ComponentProvider.fromBlockEntity(linkedContainer.syncBlockEntity);

		BlockEntityTransferComponent transferComponent = componentProvider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

		mainPanel.createChild(WTransferTypeSelectorPanel::new, Position.of(mainPanel, mainPanel.getWidth(), 0, 0), Size.of(76, 100))
				.setBlockPos(linkedContainer.syncBlockEntity.getPos())
				.setProvider(componentProvider)
				.setComponent(transferComponent);
	}
}
