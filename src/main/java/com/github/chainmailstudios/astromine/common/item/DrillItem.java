package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.utilities.ToolUtilities;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import draylar.magna.api.MagnaTool;
import draylar.magna.item.ExcavatorItem;
import draylar.magna.item.HammerItem;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

public class DrillItem extends EnergyVolumeItem implements DynamicAttributeTool, Vanishable, MagnaTool {
	private final int radius;
	private final HammerItem hammer;
	private final ExcavatorItem excavator;
	private final ToolMaterial material;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public DrillItem(ToolMaterial material, float attackDamage, float attackSpeed, int radius, double energy, Settings settings) {
		super(settings, energy);
		this.radius = radius;
		this.hammer = new HammerItem(material, (int) attackDamage, attackSpeed, settings, radius);
		this.excavator = new ExcavatorItem(material, (int) attackDamage, attackSpeed, settings, radius);
		this.material = material;
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", ToolUtilities.getAttackDamage(hammer, excavator), EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", ToolUtilities.getAttackSpeed(hammer, excavator), EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public int getEnchantability() {
		return material.getEnchantability();
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		hammer.attemptBreak(world, pos, (PlayerEntity) miner, getRadius(stack), getProcessor(world, (PlayerEntity) miner, pos, stack));
		excavator.attemptBreak(world, pos, (PlayerEntity) miner, getRadius(stack), getProcessor(world, (PlayerEntity) miner, pos, stack));
		if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
			EnergyHandler energy = Energy.of(stack);
			energy.use(getEnergy());
		}
		return true;
	}

	@Override
	public boolean isEffectiveOn(BlockState state) {
		return hammer.isEffectiveOn(state) || excavator.isEffectiveOn(state);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		Energy.of(context.getStack()).set(3200000);
		ActionResult result = hammer.useOnBlock(context);
		return result.isAccepted() ? result : excavator.useOnBlock(context);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}

	@Override
	public float getMiningSpeedMultiplier(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (hammer.isIn(tag) || excavator.isIn(tag))
			return Energy.of(this).getEnergy() <= getEnergy() ? 0F : material.getMiningSpeedMultiplier();
		return 1;
	}

	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (hammer.isIn(tag) || excavator.isIn(tag))
			return material.getMiningLevel();
		return 0;
	}

	public double getEnergy() {
		return 128D * material.getMiningSpeedMultiplier();
	}

	@Override
	public int getRadius(ItemStack itemStack) {
		return radius;
	}

	@Override
	public boolean playBreakEffects() {
		return true;
	}
}
