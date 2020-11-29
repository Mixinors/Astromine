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

package com.github.chainmailstudios.astromine.common.screenhandler.base.block;

import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a {@link AbstractContainerMenu} with an associated
 * {@link BlockPos}, {@link Block} and {@link BlockState}
 */
public abstract class BlockStateScreenHandler extends BaseScreenHandler {
	protected BlockPos position;
	protected Block originalBlock;
	protected BlockState state;

	/** Instantiates a {@link BlockStateScreenHandler}. */
	public BlockStateScreenHandler(MenuType<?> type, int syncId, Player player, BlockPos position) {
		super(type, syncId, player);

		this.state = player.level.getBlockState(position);
		this.originalBlock = state.getBlock();
		this.position = position;
	}

	/** Override behavior to only allow the {@link AbstractContainerMenu} to be open
	 * when possible, and while the associated {@link BlockState} has not
	 * changed. */
	@Override
	public boolean stillValid(@Nullable Player player) {
		return stillValid(ContainerLevelAccess.create(player.level, position), player, originalBlock);
	}
}
