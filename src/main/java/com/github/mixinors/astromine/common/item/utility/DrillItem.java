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
import com.github.mixinors.astromine.common.transfer.storage.EnergyStorageItem;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DrillItem extends DiggerItem implements EnergyStorageItem {
	private final long capacity;
	
	private final Tier material;

	private final int miningDiameter;
	
	public DrillItem(Tier material, float attackDamage, float attackSpeed, long capacity, int miningDiameter, Properties settings) {
		super(material, AMTagKeys.BlockTags.DRILL_MINEABLE, settings);
		
		this.material = material;
		this.capacity = capacity;
		this.miningDiameter = miningDiameter;
	}
	
	@Override
	public int getEnchantmentValue() {
		return material.getEnchantmentValue();
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (!target.level().isClientSide) {
			return tryUseEnergy(stack, getEnergyConsumedOnEntityHit());
		}
		
		return true;
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (!world.isClientSide && state.getDestroySpeed(world, pos) != 0.0F) {
			return tryUseEnergy(stack, getEnergyConsumedOnBlockBreak());
		}
		
		return true;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return getStoredEnergy(stack) <= getEnergyConsumedOnBlockBreak() ? 0F : super.getDestroySpeed(stack, state);
	}
	
	public long getEnergyConsumedOnBlockBreak() {
		return (long) (AMConfig.get().items.drillConsumedBlockBreak * material.getSpeed());
	}
	
	public long getEnergyConsumedOnEntityHit() {
		return (long) (AMConfig.get().items.drillConsumedEntityHit * material.getSpeed());
	}

	public int getMiningDiameter() {
		return miningDiameter;
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
	public int getBarWidth(ItemStack stack) {
		if (getEnergyCapacity() == 0) {
			return 0;
		}
		
		return (int) (13.0F * ((float) getStoredEnergy(stack) / (float) getEnergyCapacity()));
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getBarColor(ItemStack stack) {
		return 0xACE379;
	}
	
}
