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

package com.github.chainmailstudios.astromine.common.screenhandler;

import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedItemScreenHandler;
import com.github.chainmailstudios.astromine.common.utilities.type.BufferType;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import com.github.vini2003.blade.common.data.Position;
import com.github.vini2003.blade.common.data.Size;
import com.github.vini2003.blade.common.widget.base.SlotListWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import java.util.Comparator;

public class BufferScreenHandler extends DefaultedItemScreenHandler {
	public final BufferType bufferType;

	public BufferScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerEntity player, BlockPos position, BufferType bufferType) {
		super(type, syncId, player, position);
		this.bufferType = bufferType;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		int slotWidth = 9 * 18 + (9 * 2) + 7;
		int slotHeight = 6 * 18;

		int leftPadding = 6;
		int topPadding = 6 + 12;

		SlotListWidget slotList = new SlotListWidget(ItemInventoryFromInventoryComponent.of(blockEntity.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)));
		slotList.setPosition(new Position(mainTab.getPosition().getX() + leftPadding, mainTab.getPosition().getY() + topPadding));
		slotList.setSize(new Size(slotWidth, slotHeight));

		addWidget(slotList);
	}
}
