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

package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.widget.blade.TransferTypeSelectorButtonWidget;
import com.github.vini2003.blade.common.collection.TabWidgetCollection;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;

import com.google.common.collect.ImmutableMap;

public class WidgetUtilities {
	public static void createTransferTab(TabWidgetCollection tab, Position anchor, Direction rotation, BlockEntityTransferComponent component, BlockPos blockPos, ComponentKey<?> type) {
		final Position finalNorth = Position.of(anchor, 7 + 22, 31 + 22);
		final Position finalSouth = Position.of(anchor, 7 + 0, 31 + 44);
		final Position finalUp = Position.of(anchor, 7 + 22, 31 + 0);
		final Position finalDown = Position.of(anchor, 7 + 22, 31 + 44);
		final Position finalWest = Position.of(anchor, 7 + 44, 31 + 22);
		final Position finalEast = Position.of(anchor, 7 + 0, 31 + 22);

		final ImmutableMap<Direction, Position> positions = ImmutableMap.<Direction, Position> builder().put(Direction.NORTH, finalNorth).put(Direction.SOUTH, finalSouth).put(Direction.WEST, finalWest).put(Direction.EAST, finalEast).put(Direction.UP, finalUp).put(Direction.DOWN,
			finalDown).build();

		for (Direction direction : Direction.values()) {
			TransferTypeSelectorButtonWidget button = new TransferTypeSelectorButtonWidget();
			button.setPosition(positions.get(MirrorUtilities.rotate(direction, rotation)));
			button.setSize(Size.of(18, 18));
			button.setComponent(component);
			button.setType(type);
			button.setRotation(rotation);
			button.setDirection(direction);
			button.setBlockPos(blockPos);

			tab.addWidget(button);
		}
	}
}
