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

package com.github.mixinors.astromine.common.block.base;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public abstract class FacingBlockWithEntity extends BlockWithEntity {
	protected FacingBlockWithEntity(Properties settings) {
		super(settings);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		var directionProperty = getDirectionProperty();
		
		if (directionProperty != null) {
			builder.add(directionProperty);
		}
		
		super.createBlockStateDefinition(builder);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		var directionProperty = getDirectionProperty();
		
		if (directionProperty != null) {
			return super.getStateForPlacement(context).setValue(getDirectionProperty(), context.getHorizontalDirection().getOpposite());
		}
		
		return super.getStateForPlacement(context);
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		var directionProperty = getDirectionProperty();
		
		if (directionProperty != null) {
			return state.setValue(getDirectionProperty(), rotation.rotate(state.getValue(getDirectionProperty())));
		}
		
		return super.rotate(state, rotation);
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		var directionProperty = getDirectionProperty();
		
		if (directionProperty != null) {
			return state.rotate(mirror.getRotation(state.getValue(getDirectionProperty())));
		}
		
		return super.mirror(state, mirror);
	}
	
	@Nullable
	public abstract DirectionProperty getDirectionProperty();
}
