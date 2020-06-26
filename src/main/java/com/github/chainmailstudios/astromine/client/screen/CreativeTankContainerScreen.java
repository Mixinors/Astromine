package com.github.chainmailstudios.astromine.client.screen;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedFluidContainerScreen;
import com.github.chainmailstudios.astromine.common.block.entity.CreativeTankBlockEntity;
import com.github.chainmailstudios.astromine.common.container.CreativeTankContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedFluidContainer;
import com.github.chainmailstudios.astromine.registry.AstromineCommonPackets;
import io.netty.buffer.Unpooled;
import spinnery.client.configuration.widget.WOptionField;
import spinnery.widget.WTextField;
import spinnery.widget.api.Filter;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class CreativeTankContainerScreen extends DefaultedFluidContainerScreen<CreativeTankContainer> {
	public CreativeTankContainerScreen(Text name, DefaultedFluidContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		mainPanel.setSize(Size.of(mainPanel.getSize().getWidth(), mainPanel.getSize().getHeight()));
		mainPanel.center();
		fluidBar.centerX();

		WTextField field = mainPanel.createChild(WOptionField::new, Position.of(mainPanel, 0, 80, 0), Size.of(64, 18))
				.setFilter(Filter.get(String.class))
				.setOnKeyPressed((widget, keyPressed, character, keyModifier) -> {
					if (Identifier.isValid((widget).getText())) {
						PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
						buffer.writeBlockPos(linkedContainer.blockEntity.getPos());
						buffer.writeIdentifier(CreativeTankBlockEntity.FLUID_CHANGE_PACKET);
						buffer.writeIdentifier(new Identifier(widget.getText()));
						ClientSidePacketRegistry.INSTANCE.sendToServer(AstromineCommonPackets.BLOCK_ENTITY_UPDATE_PACKET, buffer);
					}
				});

		field.centerX();
	}
}
