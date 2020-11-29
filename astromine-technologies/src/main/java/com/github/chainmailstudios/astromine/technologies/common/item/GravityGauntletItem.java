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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesItems;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class GravityGauntletItem extends EnergyVolumeItem implements DynamicAttributeTool {
	private static final Multimap<Attribute, AttributeModifier> EAMS = HashMultimap.create();

	static {
		EAMS.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "attack", 4f, AttributeModifier.Operation.ADDITION));
	}

	public GravityGauntletItem(Properties settings, double size) {
		super(settings, size);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);
		if (hand == InteractionHand.OFF_HAND)
			return InteractionResultHolder.pass(stack);
		ItemStack offStack = user.getItemInHand(InteractionHand.OFF_HAND);
		if (offStack.getItem() == AstromineTechnologiesItems.GRAVITY_GAUNTLET) {
			EnergyHandler selfHandler = Energy.of(stack);
			EnergyHandler otherHandler = Energy.of(offStack);
			if (selfHandler.getEnergy() > AstromineConfig.get().gravityGauntletConsumed && otherHandler.getEnergy() > AstromineConfig.get().gravityGauntletConsumed) {
				user.startUsingItem(hand);
				return InteractionResultHolder.success(stack);
			}
		}
		return super.use(world, user, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (world.isClientSide)
			return stack;
		ItemStack offStack = user.getItemInHand(InteractionHand.OFF_HAND);
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
		return super.finishUsingItem(stack, world, user);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 30;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (attacker.level.isClientSide)
			return super.hurtEnemy(stack, target, attacker);
		ItemStack offStack = attacker.getItemInHand(InteractionHand.OFF_HAND);
		if (offStack.getItem() == AstromineTechnologiesItems.GRAVITY_GAUNTLET) {
			if (stack.getOrCreateTag().getBoolean("Charged") && offStack.getOrCreateTag().getBoolean("Charged")) {
				target.knockback(1, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
				target.push(0f, 0.5f, 0f);
				stack.getOrCreateTag().putBoolean("Charged", false);
				offStack.getOrCreateTag().putBoolean("Charged", false);
				return true;
			}
		}
		return super.hurtEnemy(stack, target, attacker);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean("Charged");
	}

	// TODO: dynamic once not broken so only provide when charged
	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			return EAMS;
		}
		return super.getDefaultAttributeModifiers(slot);
	}
}
