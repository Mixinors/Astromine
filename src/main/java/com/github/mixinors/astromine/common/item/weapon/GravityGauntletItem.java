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

package com.github.mixinors.astromine.common.item.weapon;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.item.storage.SimpleEnergyStorageItem;
import com.github.mixinors.astromine.common.util.ItemDataUtils;
import com.github.mixinors.astromine.registry.common.AMItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class GravityGauntletItem extends SimpleEnergyStorageItem {
	private static final String CHARGED_KEY = "Charged";
	
	public GravityGauntletItem(Properties settings, long capacity) {
		super(settings, capacity);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		var stack = user.getItemInHand(hand);
		
		if (hand == InteractionHand.OFF_HAND) {
			return InteractionResultHolder.pass(stack);
		}
		
		var offStack = user.getItemInHand(InteractionHand.OFF_HAND);
		
		if (offStack.is(AMItems.GRAVITY_GAUNTLET.get())) {
			if (getStoredEnergy(stack) >= AMConfig.get().items.gravityGauntletConsumed && getStoredEnergy(offStack) >= AMConfig.get().items.gravityGauntletConsumed) {
				user.startUsingItem(hand);
				
				return InteractionResultHolder.success(stack);
			}
		}
		
		return super.use(world, user, hand);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (world.isClientSide) {
			return stack;
		}
		
		var offStack = user.getItemInHand(InteractionHand.OFF_HAND);
		
		if (offStack.is(AMItems.GRAVITY_GAUNTLET.get())) {
			if (getStoredEnergy(stack) >= AMConfig.get().items.gravityGauntletConsumed && getStoredEnergy(offStack) >= AMConfig.get().items.gravityGauntletConsumed) {
				tryUseEnergy(stack, AMConfig.get().items.gravityGauntletConsumed);
				tryUseEnergy(offStack, AMConfig.get().items.gravityGauntletConsumed);
				
				setCharged(stack, true);
				setCharged(offStack, true);
				
				return stack;
			}
		}
		
		return super.finishUsingItem(stack, world, user);
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}
	
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 30;
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (attacker.level().isClientSide) {
			return super.hurtEnemy(stack, target, attacker);
		}
		
		var offStack = attacker.getItemInHand(InteractionHand.OFF_HAND);
		
		if (offStack.getItem() == AMItems.GRAVITY_GAUNTLET.get()) {
			if (isCharged(stack) && isCharged(offStack)) {
				target.knockback(1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
				
				target.push(0.0F, 0.5F, 0.0F);
				
				setCharged(stack, false);
				setCharged(offStack, false);
				
				return true;
			}
		}
		
		return super.hurtEnemy(stack, target, attacker);
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return isCharged(stack);
	}
	
	private static boolean isCharged(ItemStack stack) {
		return ItemDataUtils.get(stack).getBoolean(CHARGED_KEY);
	}
	
	private static void setCharged(ItemStack stack, boolean charged) {
		ItemDataUtils.update(stack, nbt -> nbt.putBoolean(CHARGED_KEY, charged));
	}
}
