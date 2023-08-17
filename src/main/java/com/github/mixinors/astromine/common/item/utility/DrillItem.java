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

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.item.AppendingGroupItem;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.base.SimpleBatteryItem;

import java.util.function.Consumer;

public class DrillItem extends MiningToolItem implements SimpleBatteryItem, AppendingGroupItem {
	private final long capacity;
	
	private final ToolMaterial material;
	
	public DrillItem(ToolMaterial material, float attackDamage, float attackSpeed, long capacity, Settings settings) {
		super(attackDamage, attackSpeed, material, AMTagKeys.BlockTags.DRILL_MINEABLE, settings);
		
		this.material = material;
		this.capacity = capacity;
	}
	
	@Override
	public int getEnchantability() {
		return material.getEnchantability();
	}
	
	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (!target.getWorld().isClient) {
			return tryUseEnergy(stack, getEnergyConsumedOnEntityHit());
		}
		
		return true;
	}
	
	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
			return tryUseEnergy(stack, getEnergyConsumedOnBlockBreak());
		}
		
		return true;
	}
	
	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return getStoredEnergy(stack) <= getEnergyConsumedOnBlockBreak() ? 0F : super.getMiningSpeedMultiplier(stack, state);
	}
	
	public long getEnergyConsumedOnBlockBreak() {
		return (long) (AMConfig.get().items.drillConsumedBlockBreak * material.getMiningSpeedMultiplier());
	}
	
	public long getEnergyConsumedOnEntityHit() {
		return (long) (AMConfig.get().items.drillConsumedEntityHit * material.getMiningSpeedMultiplier());
	}
	
	@Override
	public long getEnergyCapacity() {
		return capacity;
	}
	
	@Override
	public long getEnergyMaxInput() {
		return capacity;
	}
	
	@Override
	public long getEnergyMaxOutput() {
		return capacity;
	}
	
	/** Override behavior to return our progress. */
	@Override
	public int getItemBarStep(ItemStack stack) {
		if (getEnergyCapacity() == 0) {
			return 0;
		}
		
		return (int) (13.0F * ((float) SimpleBatteryItem.getStoredEnergyUnchecked(stack) / (float) getEnergyCapacity()));
	}
	
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getItemBarColor(ItemStack stack) {
		return 0xACE379;
	}
	
	@Override
	public void appendStacks(Consumer<ItemStack> stacks) {
		var stack = new ItemStack(this);
		
		setStoredEnergy(stack, getEnergyCapacity());
		
		stacks.accept(stack);
	}
}
