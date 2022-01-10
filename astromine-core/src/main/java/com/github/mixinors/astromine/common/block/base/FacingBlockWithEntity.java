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

package com.github.mixinors.astromine.common.block.base;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;

/**
 * A {@link BlockWithEntity} with a {@link DirectionProperty}.
 */
public abstract class FacingBlockWithEntity extends BlockWithEntity {
	/**
	 * Instantiates a {@link FacingBlockWithEntity}.
	 */
	protected FacingBlockWithEntity(Settings settings) {
		super(settings);
	}

	/** Override behavior to add the {@link DirectionProperty}. */
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		var directionProperty = getDirectionProperty();

		if (directionProperty != null) {
			builder.add(directionProperty);
		}

		super.appendProperties(builder);
	}

	/** Override behavior to add the {@link DirectionProperty}. */
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		var directionProperty = getDirectionProperty();

		if (directionProperty != null) {
			return super.getPlacementState(context).with(getDirectionProperty(), context.getPlayerFacing().getOpposite());
		}

		return super.getPlacementState(context);
	}

	/** Override behavior to add the {@link DirectionProperty}. */
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		var directionProperty = getDirectionProperty();

		if (directionProperty != null) {
			return state.with(getDirectionProperty(), rotation.rotate(state.get(getDirectionProperty())));
		}

		return super.rotate(state, rotation);
	}

	/** Override behavior to add the {@link DirectionProperty}. */
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		var directionProperty = getDirectionProperty();

		if (directionProperty != null) {
			return state.rotate(mirror.getRotation(state.get(getDirectionProperty())));
		}

		return super.mirror(state, mirror);
	}

	/** Returns the {@link DirectionProperty} of this block. */
	@Nullable
	public abstract DirectionProperty getDirectionProperty();
}
