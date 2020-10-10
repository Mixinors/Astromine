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

import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import draylar.magna.api.MagnaTool;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class DrillItem extends EnergyVolumeItem implements DynamicAttributeTool, Vanishable, MagnaTool, DiggerTool {
	private final int radius;
	private final ToolMaterial material;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public DrillItem(ToolMaterial material, float attackDamage, float attackSpeed, int radius, double size, Settings settings) {
		super(settings, size);

		this.radius = radius;
		this.material = material;

		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();

		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", attackDamage, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));

		this.attributeModifiers = builder.build();
	}

	@Override
	public int getEnchantability() {
		return material.getEnchantability();
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (!target.world.isClient) {
			EnergyHandler energy = Energy.of(stack);
			energy.use(getEnergy() * AstromineConfig.get().drillEntityHitMultiplier);
		}

		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
			EnergyHandler energy = Energy.of(stack);
			energy.use(getEnergy());
		}

		return true;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}

	@Override
	public float getMiningSpeedMultiplier(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		return material.getMiningSpeedMultiplier();
	}

	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		return material.getMiningLevel();
	}

	@Override
	public float postProcessMiningSpeed(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user, float currentSpeed, boolean isEffective) {
		return Energy.of(stack).getEnergy() <= getEnergy() ? 0F : currentSpeed;
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return 0F; // Disallow vanilla from overriding our #postProcessMiningSpeed
	}

	public double getEnergy() {
		return AstromineConfig.get().drillConsumed * material.getMiningSpeedMultiplier();
	}

	@Override
	public int getRadius(ItemStack stack) {
		return radius;
	}

	@Override
	public boolean playBreakEffects() {
		return true;
	}
}
