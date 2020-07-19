package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.NameableComponent;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WTabbedPanel;
import com.github.chainmailstudios.astromine.common.widget.WTransferTypeSelectorPanel;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.WTabHolder;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Collection;

public class DefaultedBlockEntityHandledScreen<T extends DefaultedBlockEntityScreenHandler> extends DefaultedHandledScreen<T> {
	public WInterface mainInterface;
	public WTabbedPanel mainTabbedPanel;
	public WPanel mainPanel;
	public Collection<WSlot> playerSlots;

	public DefaultedBlockEntityHandledScreen(Text name, T linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		mainInterface = getInterface();

		mainTabbedPanel = mainInterface.createChild(WTabbedPanel::new, Position.ORIGIN, Size.of(176, 160 + 24));
		mainPanel = mainTabbedPanel.addTab(linkedScreenHandler.getWorld().getBlockState(linkedScreenHandler.syncBlockEntity.getPos()).getBlock().asItem()).getBody();

		MinecraftClient.getInstance().mouse.unlockCursor();
		addTitle(mainPanel);

		mainPanel.setInterface(getInterface());

		mainTabbedPanel.center();
		mainTabbedPanel.setOnAlign(widget -> {
			widget.center();
			widget.setY(widget.getY() - 12);
		});

		playerSlots = Sets.newHashSet(WSlot.addPlayerInventory(Position.of(mainPanel, 7, mainPanel.getHeight() - 18 - 11 - (18 * 3), 2), Size.of(18, 18), mainPanel));

		ComponentProvider componentProvider = ComponentProvider.fromBlockEntity(linkedScreenHandler.syncBlockEntity);

		BlockEntityTransferComponent transferComponent = componentProvider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

		transferComponent.get().forEach((type, entry) -> {
			ComponentType<?> componentType = ComponentRegistry.INSTANCE.get(type);
			if (componentType != null) {
				NameableComponent nameableComponent = (NameableComponent) componentProvider.getComponent(componentType);
				WTabHolder.WTab tab = mainTabbedPanel.addTab(nameableComponent.getSymbol());
				tab.setInterface(getInterface());
				WTransferTypeSelectorPanel.createTab(
						tab,
						Position.of(mainTabbedPanel, mainTabbedPanel.getWidth() / 2 - 38, 0, 0),
						transferComponent,
						linkedScreenHandler.syncBlockEntity.getPos(),
						type,
						getInterface()
				);
				tab.getBody().setLabel(nameableComponent.getName());
				playerSlots.addAll(WSlot.addPlayerInventory(Position.of(mainTabbedPanel, 7, mainTabbedPanel.getHeight() - 18 - 11 - (18 * 3), 2), Size.of(18, 18), tab));
			} else {
				BlockEntityTransferComponent.TransferComponentInfo info = BlockEntityTransferComponent.INFOS.get(type);
				if (info != null) {
					WTabHolder.WTab tab = mainTabbedPanel.addTab(info.getSymbol());
					tab.setInterface(getInterface());
					WTransferTypeSelectorPanel.createTab(
							tab,
							Position.of(mainTabbedPanel, mainTabbedPanel.getWidth() / 2 - 38, 0, 0),
							transferComponent,
							linkedScreenHandler.syncBlockEntity.getPos(),
							type,
							getInterface()
					);
					tab.getBody().setLabel(info.getName());
					playerSlots.addAll(WSlot.addPlayerInventory(Position.of(mainTabbedPanel, 7, mainTabbedPanel.getHeight() - 18 - 11 - (18 * 3), 2), Size.of(18, 18), tab));
				}
			}
		});
	}
}
