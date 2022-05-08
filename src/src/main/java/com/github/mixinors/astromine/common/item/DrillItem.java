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

package com.github.mixinors.astromine.common.item;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.item.base.EnergyStorageItem;
import com.github.mixinors.astromine.registry.common.AMTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.base.SimpleBatteryItem;

public class DrillItem extends MiningToolItem implements SimpleBatteryItem {
	private final int radius;
	private final ToolMaterial material;
	private final long capacity;
	
	public DrillItem(ToolMaterial material, float attackDamage, float attackSpeed, int radius, long capacity, Settings settings) {
		super(attackDamage, attackSpeed, material, AMTags.DRILL_MINEABLES, settings);
		
		this.radius = radius;
		this.material = material;
		this.capacity = capacity;
	}
	
	@Override
	public int getEnchantability() {
		return material.getEnchantability();
	}
	
	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (!target.world.isClient) {
			return tryUseEnergy(stack, (long) (getEnergyConsumed() * AMConfig.get().items.drillEntityHitMultiplier));
		}
		
		return true;
	}
	
	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
			return tryUseEnergy(stack, getEnergyConsumed());
		}
		
		return true;
	}
	
	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return getStoredEnergy(stack) <= getEnergyConsumed() ? 0F : super.getMiningSpeedMultiplier(stack, state);
	}
	
	public long getEnergyConsumed() {
		return (long) (AMConfig.get().items.drillConsumed * material.getMiningSpeedMultiplier());
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
		
		return (int) (13 * ((float) SimpleBatteryItem.getStoredEnergyUnchecked(stack) / (float) getEnergyCapacity()));
	}
	
	/** Override behavior to return true. */
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}
	
	/** Override behavior to return a median red. */
	@Override
	public int getItemBarColor(ItemStack stack) {
		return 0x91261f;
	}
	
	/**
	 * Override behavior to add instances of {@link EnergyStorageItem} as {@link ItemStack}s to {@link ItemGroup}s with full energy.
	 */
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		
		if (this.isIn(group)) {
			var stack = new ItemStack(this);
			
			setStoredEnergy(stack, getEnergyCapacity());
			
			stacks.add(stack);
		}
	}
}
