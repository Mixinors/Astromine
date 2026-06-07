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

package com.github.mixinors.astromine.common.screen.handler.base.block;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockStateScreenHandler extends AbstractContainerMenu {
	protected final Level world;
	protected final Player player;
	
	protected final BlockPos blockPos;
	protected final BlockState blockState;
	protected final Block block;
	
	public BlockStateScreenHandler(Supplier<? extends MenuType<?>> type, int syncId, Player player, BlockPos blockPos) {
		super(type.get(), syncId);
		
		this.player = player;
		this.world = player.level();
		
		this.blockPos = blockPos;
		
		this.blockState = world.getBlockState(blockPos);
		
		this.block = blockState.getBlock();
		
	}
	
	@Override
	public boolean stillValid(@Nullable Player player) {
		if (player == null) {
			return false;
		}
		
		return stillValid(ContainerLevelAccess.create(player.level(), blockPos), player, block);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public BlockPos getBlockPos() {
		return blockPos;
	}
	
	public void init(int width, int height) {
	}
	
	protected void addPlayerInventory(Inventory inventory, int x, int y) {
		for (var row = 0; row < 3; ++row) {
			for (var column = 0; column < 9; ++column) {
				addSlot(new net.minecraft.world.inventory.Slot(inventory, column + row * 9 + 9, x + 1 + column * 18, y + 1 + row * 18));
			}
		}
		
		for (var column = 0; column < 9; ++column) {
			addSlot(new net.minecraft.world.inventory.Slot(inventory, column, x + 1 + column * 18, y + 1 + 58));
		}
	}
}
