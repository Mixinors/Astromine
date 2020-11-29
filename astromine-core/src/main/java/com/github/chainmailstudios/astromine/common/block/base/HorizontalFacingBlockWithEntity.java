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

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link BlockWithEntity} with a {@link DirectionProperty}
 * of {@link BlockStateProperties#HORIZONTAL_FACING}.
 */
public abstract class HorizontalFacingBlockWithEntity extends BlockWithEntity {
	/** Instantiates a {@link HorizontalFacingBlockWithEntity}. */
	public HorizontalFacingBlockWithEntity(Properties settings) {
		super(settings);
	}

	/** Override behavior to add the {@link DirectionProperty}. */
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		DirectionProperty directionProperty = getDirectionProperty();

		if (directionProperty != null) {
			builder.add(directionProperty);
		}

		super.createBlockStateDefinition(builder);
	}

	/** Override behavior to add the {@link DirectionProperty}. */
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		DirectionProperty directionProperty = getDirectionProperty();

		if (directionProperty != null) {
			return super.getStateForPlacement(context).setValue(getDirectionProperty(), context.getHorizontalDirection().getOpposite());
		}

		return super.getStateForPlacement(context);
	}

	/** Override behavior to add the {@link DirectionProperty}. */
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		DirectionProperty directionProperty = getDirectionProperty();

		if (directionProperty != null) {
			return state.setValue(getDirectionProperty(), rotation.rotate(state.getValue(getDirectionProperty())));
		}

		return super.rotate(state, rotation);
	}

	/** Override behavior to add the {@link DirectionProperty}. */
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		DirectionProperty directionProperty = getDirectionProperty();

		if (directionProperty != null) {
			return state.rotate(mirror.getRotation(state.getValue(getDirectionProperty())));
		}

		return super.mirror(state, mirror);
	}

	/** Returns the {@link DirectionProperty} of this block. */
	@Nullable
	public DirectionProperty getDirectionProperty() {
		return BlockStateProperties.HORIZONTAL_FACING;
	}
}
