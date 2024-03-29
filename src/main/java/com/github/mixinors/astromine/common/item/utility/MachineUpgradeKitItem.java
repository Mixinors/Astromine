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

package com.github.mixinors.astromine.common.item.utility;

import com.github.mixinors.astromine.common.block.base.TieredBlock;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public class MachineUpgradeKitItem extends Item {
	private final Tier from;
	private final Tier to;
	
	public MachineUpgradeKitItem(Tier from, Tier to, Settings settings) {
		super(settings);
		
		this.from = from;
		this.to = to;
	}
	
	public MachineUpgradeKitItem(Tier to, Settings settings) {
		this(Tier.values()[to.ordinal() - 1], to, settings);
	}
	
	public Tier from() {
		return from;
	}
	
	public Tier to() {
		return to;
	}
	
	public boolean isValidFor(Tier tier) {
		return tier == from;
	}
	
	public boolean isValidFor(TieredBlock block) {
		return isValidFor(block.getTier()) && block.hasTier(to());
	}
	
	public boolean isValidFor(Block block) {
		return block instanceof TieredBlock tieredBlock && isValidFor(tieredBlock);
	}
	
	@Nullable
	public Block getUpgrade(TieredBlock block) {
		if (isValidFor(block)) {
			return block.getForTier(to());
		} else {
			return null;
		}
	}
	
	@Nullable
	public Block getUpgrade(Block block) {
		if (block instanceof TieredBlock tieredBlock) {
			return getUpgrade(tieredBlock);
		} else {
			return null;
		}
	}
}
