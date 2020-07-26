/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedFluidHandledScreen;
import com.github.chainmailstudios.astromine.common.block.entity.CreativeTankBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.CreativeTankScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedFluidScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineCommonPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import spinnery.client.configuration.widget.WOptionField;
import spinnery.widget.WTextField;
import spinnery.widget.api.Filter;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class CreativeTankHandledScreen extends DefaultedFluidHandledScreen<CreativeTankScreenHandler> {
	public CreativeTankHandledScreen(Text name, DefaultedFluidScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		mainPanel.setSize(Size.of(mainPanel.getSize().getWidth(), mainPanel.getSize().getHeight()));
		mainPanel.center();
		fluidBar.centerX();

		WTextField field = mainPanel.createChild(WOptionField::new, Position.of(mainPanel, 0, 80, 0), Size.of(64, 18)).setFilter(Filter.get(String.class)).setOnKeyPressed((widget, keyPressed, character, keyModifier) -> {
			if (Identifier.isValid((widget).getText())) {
				PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
				buffer.writeBlockPos(linkedScreenHandler.blockEntity.getPos());
				buffer.writeIdentifier(CreativeTankBlockEntity.FLUID_CHANGE_PACKET);
				buffer.writeIdentifier(new Identifier(widget.getText()));
				ClientSidePacketRegistry.INSTANCE.sendToServer(AstromineCommonPackets.BLOCK_ENTITY_UPDATE_PACKET, buffer);
			}
		});

		field.centerX();
	}
}
