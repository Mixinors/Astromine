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

package com.github.chainmailstudios.astromine.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;

import org.jetbrains.annotations.Nullable;

public abstract class HorizontalFacingBlockWithEntity extends BlockWithEntity {
	public HorizontalFacingBlockWithEntity(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		DirectionProperty directionProperty = getDirectionProperty();
		if (directionProperty != null) {
			builder.add(directionProperty);
		}
		super.appendProperties(builder);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		DirectionProperty directionProperty = getDirectionProperty();
		if (directionProperty != null) {
			return super.getPlacementState(context).with(getDirectionProperty(), context.getPlayerFacing().getOpposite());
		}
		return super.getPlacementState(context);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		DirectionProperty directionProperty = getDirectionProperty();
		if (directionProperty != null) {
			return state.with(getDirectionProperty(), rotation.rotate(state.get(getDirectionProperty())));
		}
		return super.rotate(state, rotation);
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		DirectionProperty directionProperty = getDirectionProperty();
		if (directionProperty != null) {
			return state.rotate(mirror.getRotation(state.get(getDirectionProperty())));
		}
		return super.mirror(state, mirror);
	}

	@Nullable
	public DirectionProperty getDirectionProperty() {
		return Properties.HORIZONTAL_FACING;
	}
}
