package com.github.chainmailstudios.astromine.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;

public class ShaxeItem extends MiningToolItem implements DynamicAttributeTool {
	private final PickaxeItem pickaxe;
	private final ShovelItem shovel;

	public ShaxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
		super(attackDamage, attackSpeed, material, Sets.newHashSet(), settings);
		this.pickaxe = new PickaxeItem(material, (int) attackDamage, attackSpeed, settings);
		this.shovel = new ShovelItem(material, attackDamage, attackSpeed, settings);
	}

	@Override
	public float getAttackDamage() {
		return Math.max(pickaxe.getAttackDamage(), shovel.getAttackDamage());
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return Math.max(pickaxe.getMiningSpeedMultiplier(stack, state), shovel.getMiningSpeedMultiplier(stack, state));
	}

	@Override
	public boolean isEffectiveOn(BlockState state) {
		return pickaxe.isEffectiveOn(state) || shovel.isEffectiveOn(state);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return pickaxe.useOnBlock(context).isAccepted() ? ActionResult.SUCCESS : shovel.useOnBlock(context);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getDynamicModifiers(EquipmentSlot slot, ItemStack stack, LivingEntity user) {
		return ImmutableMultimap.of(
				EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", getAttackDamage(), EntityAttributeModifier.Operation.ADDITION)
		);
	}
}
