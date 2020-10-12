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

package com.github.chainmailstudios.astromine.technologies.common.item;

import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;

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

import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesItems;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class GravityGauntletItem extends EnergyVolumeItem implements DynamicAttributeTool {
	private static final Multimap<EntityAttribute, EntityAttributeModifier> EAMS = HashMultimap.create();

	static {
		EAMS.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "attack", 4f, EntityAttributeModifier.Operation.ADDITION));
	}

	public GravityGauntletItem(Settings settings, double size) {
		super(settings, size);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (hand == Hand.OFF_HAND)
			return TypedActionResult.pass(stack);
		ItemStack offStack = user.getStackInHand(Hand.OFF_HAND);
		if (offStack.getItem() == AstromineTechnologiesItems.GRAVITY_GAUNTLET) {
			EnergyHandler selfHandler = Energy.of(stack);
			EnergyHandler otherHandler = Energy.of(offStack);
			if (selfHandler.getEnergy() > AstromineConfig.get().gravityGauntletConsumed && otherHandler.getEnergy() > AstromineConfig.get().gravityGauntletConsumed) {
				user.setCurrentHand(hand);
				return TypedActionResult.success(stack);
			}
		}
		return super.use(world, user, hand);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (world.isClient)
			return stack;
		ItemStack offStack = user.getStackInHand(Hand.OFF_HAND);
		if (offStack.getItem() == AstromineTechnologiesItems.GRAVITY_GAUNTLET) {
			EnergyHandler selfHandler = Energy.of(stack);
			EnergyHandler otherHandler = Energy.of(offStack);
			if (selfHandler.getEnergy() > AstromineConfig.get().gravityGauntletConsumed && otherHandler.getEnergy() > AstromineConfig.get().gravityGauntletConsumed) {
				selfHandler.extract(AstromineConfig.get().gravityGauntletConsumed);
				otherHandler.extract(AstromineConfig.get().gravityGauntletConsumed);
				stack.getOrCreateTag().putBoolean("Charged", true);
				offStack.getOrCreateTag().putBoolean("Charged", true);
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
		if (attacker.world.isClient)
			return super.postHit(stack, target, attacker);
		ItemStack offStack = attacker.getStackInHand(Hand.OFF_HAND);
		if (offStack.getItem() == AstromineTechnologiesItems.GRAVITY_GAUNTLET) {
			if (stack.getOrCreateTag().getBoolean("Charged") && offStack.getOrCreateTag().getBoolean("Charged")) {
				target.takeKnockback(1, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
				target.addVelocity(0f, 0.5f, 0f);
				stack.getOrCreateTag().putBoolean("Charged", false);
				offStack.getOrCreateTag().putBoolean("Charged", false);
				return true;
			}
		}
		return super.postHit(stack, target, attacker);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean("Charged");
	}

	// TODO: dynamic once not broken so only provide when charged
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			return EAMS;
		}
		return super.getAttributeModifiers(slot);
	}
}
