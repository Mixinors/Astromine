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
import com.github.mixinors.astromine.common.item.storage.SimpleEnergyStorageItem;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class GravityGauntletItem extends SimpleEnergyStorageItem {
	private static final String CHARGED_KEY = "Charged";
	
	private static final Multimap<EntityAttribute, EntityAttributeModifier> ATTRIBUTE_MODIFIERS = HashMultimap.create();
	
	static {
		ATTRIBUTE_MODIFIERS.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "attack", 4f, EntityAttributeModifier.Operation.ADDITION));
	}
	
	public GravityGauntletItem(Settings settings, long capacity) {
		super(settings, capacity);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var stack = user.getStackInHand(hand);
		
		if (hand == Hand.OFF_HAND) {
			return TypedActionResult.pass(stack);
		}
		
		var offStack = user.getStackInHand(Hand.OFF_HAND);
		
		if (offStack.isOf(AMItems.GRAVITY_GAUNTLET.get())) {
			if (getStoredEnergy(stack) >= AMConfig.get().items.gravityGauntletConsumed && getStoredEnergy(offStack) >= AMConfig.get().items.gravityGauntletConsumed) {
				user.setCurrentHand(hand);
				
				return TypedActionResult.success(stack);
			}
		}
		
		return super.use(world, user, hand);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (world.isClient) {
			return stack;
		}
		var offStack = user.getStackInHand(Hand.OFF_HAND);
		
		if (offStack.isOf(AMItems.GRAVITY_GAUNTLET.get())) {
			if (getStoredEnergy(stack) >= AMConfig.get().items.gravityGauntletConsumed && getStoredEnergy(offStack) >= AMConfig.get().items.gravityGauntletConsumed) {
				tryUseEnergy(stack, AMConfig.get().items.gravityGauntletConsumed);
				tryUseEnergy(offStack, AMConfig.get().items.gravityGauntletConsumed);
				
				stack.getOrCreateNbt().putBoolean(CHARGED_KEY, true);
				
				offStack.getOrCreateNbt().putBoolean(CHARGED_KEY, true);
				
				return stack;
			}
		}
		
		return super.finishUsing(stack, world, user);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 30;
	}
	
	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (attacker.world.isClient) {
			return super.postHit(stack, target, attacker);
		}
		
		var offStack = attacker.getStackInHand(Hand.OFF_HAND);
		
		if (offStack.getItem() == AMItems.GRAVITY_GAUNTLET.get()) {
			if (stack.getOrCreateNbt().getBoolean(CHARGED_KEY) && offStack.getOrCreateNbt().getBoolean(CHARGED_KEY)) {
				target.takeKnockback(1, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
				
				target.addVelocity(0f, 0.5f, 0f);
				
				stack.getOrCreateNbt().putBoolean(CHARGED_KEY, false);
				
				offStack.getOrCreateNbt().putBoolean(CHARGED_KEY, false);
				
				return true;
			}
		}
		return super.postHit(stack, target, attacker);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return stack.getOrCreateNbt().getBoolean(CHARGED_KEY);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			return ATTRIBUTE_MODIFIERS;
		}
		
		return super.getAttributeModifiers(slot);
	}
}
