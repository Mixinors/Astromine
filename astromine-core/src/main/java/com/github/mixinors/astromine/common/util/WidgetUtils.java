/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.google.common.collect.ImmutableMap;

import com.github.mixinors.astromine.common.widget.blade.StorageSidingWidget;
import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;

import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class WidgetUtils {
	public static List<StorageSidingWidget> createStorageSiding(Position anchor, ExtendedBlockEntity blockEntity, StorageSiding[] sidings, StorageType type, Direction rotation) {
		Position north = Position.of(anchor, 7.0F + 22.0F, 31.0F + 22.0F, 0.0F);
		Position south = Position.of(anchor, 7.0F, 31.0F + 44, 0.0F);
		Position up = Position.of(anchor, 7.0F + 22.0F, 31.0F, 0.0F);
		Position down = Position.of(anchor, 7.0F + 22.0F, 31.0F + 44.0F, 0.0F);
		Position west = Position.of(anchor, 7.0F + 44.0F, 31.0F + 22.0F, 0.0F);
		Position east = Position.of(anchor, 7.0F, 31.0F + 22.0F, 0.0F);

		ImmutableMap<Direction, Position> positions = ImmutableMap.<Direction, Position>builder()
				.put(Direction.NORTH, north)
				.put(Direction.SOUTH, south)
				.put(Direction.WEST, west)
				.put(Direction.EAST, east)
				.put(Direction.UP, up)
				.put(Direction.DOWN, down)
				.build();

		ArrayList<StorageSidingWidget> list = new ArrayList<StorageSidingWidget>();
		
		for (Direction direction : Direction.values()) {
			StorageSidingWidget button = new StorageSidingWidget();
			button.setPosition(positions.get(MirrorUtils.rotate(direction, rotation)));
			button.setSize(Size.of(18.0F, 18.0F, 0.0F));
			button.setBlockEntity(blockEntity);
			button.setSiding(sidings[direction.ordinal()]);
			button.setType(type);
			button.setRotation(rotation);
			button.setDirection(direction);

			list.add(button);
		}
		
		return list;
	}
}
