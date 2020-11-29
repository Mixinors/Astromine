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

package com.github.chainmailstudios.astromine.common.item;

import com.zundrel.wrenchable.wrench.Wrench;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * A wrench {@link Item}.
 */
public class WrenchItem extends TieredItem implements Wrench {
	/** Instantiates a {@link WrenchItem}. */
	public WrenchItem(Tier material, Properties settings) {
		super(material, settings);
	}

	/* Override behavior to damage wrench when a block is wrenched. */
	@Override
	public void onBlockWrenched(Level world, ItemStack stack, Player player, InteractionHand hand, BlockHitResult result) {
		if (player instanceof ServerPlayer) {
			stack.hurt(1, world.getRandom(), (ServerPlayer) player);
		}
	}

	/* Override behavior to damage wrench when an entity is wrenched. */
	@Override
	public void onEntityWrenched(Level world, ItemStack stack, Player player, InteractionHand hand, EntityHitResult result) {
		if (player instanceof ServerPlayer) {
			stack.hurt(1, world.getRandom(), (ServerPlayer) player);
		}
	}
}
