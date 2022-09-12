/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.screen.handler.storage;

import com.github.mixinors.astromine.common.block.entity.storage.BufferBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.gui.api.common.widget.list.slot.SlotListWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class BufferScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final BufferBlockEntity buffer;
	
	public BufferScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.BUFFER, syncId, player, position);
		
		buffer = (BufferBlockEntity) blockEntity;
	}
	
	@Override
	public void init(int width, int height) {
		super.init(width, height);
		
		if (!(buffer instanceof BufferBlockEntity.Creative)) {
			var slotList = new SlotListWidget(buffer.getItemStorage(), 9 * 18, 6 * 18, 0);
			slotList.setPosition(new Position(tab, PAD_7, PAD_10));
			slotList.setSize(new Size(SLOT_WIDTH * 9.0F, SLOT_HEIGHT * 6.0F));
			
			tab.add(slotList);
		} else {
			var slot = new SlotWidget(0, buffer.getItemStorage(), Slot::new);
			slot.setPosition(new Position(tab, TABS_WIDTH / 2.0F - SLOT_WIDTH / 2.0F, SLOT_WIDTH));
			slot.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			var launchButton = new ButtonWidget();
			launchButton.onEvent(EventType.MOUSE_CLICKED, (MouseClickedEvent event) -> {
				buffer.getItemStorage().setStack(0, ItemStack.EMPTY);
			});
			
			launchButton.setPosition(new Position(slot, -(32.0F - SLOT_WIDTH) / 2.0F, SLOT_HEIGHT + PAD_3));
			launchButton.setSize(new Size(CLEAR_BUTTON_WIDTH, CLEAR_BUTTON_HEIGHT));
			launchButton.setLabel(Text.translatable("text.astromine.clear"));
			
			tab.add(launchButton);
			tab.add(slot);
		}
	}
	
	@Override
	public Size getTabsSizeExtension() {
		if (!(buffer instanceof BufferBlockEntity.Creative)) {
			return new Size(0.0F, 58.0F);
		} else {
			return super.getTabsSizeExtension();
		}
	}
}
