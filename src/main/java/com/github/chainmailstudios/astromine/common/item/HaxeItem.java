package com.github.chainmailstudios.astromine.common.item;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;

public class HaxeItem extends MiningToolItem {
	private final AxeItem axe;
	private final HoeItem hoe;

	public HaxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
		super(attackDamage, attackSpeed, material, Sets.newHashSet(), settings);
		this.axe = new AxeItem(material, attackDamage, attackSpeed, settings);
		this.hoe = new HoeItem(material, (int) attackDamage, attackSpeed, settings);
	}

	@Override
	public float getAttackDamage() {
		return Math.max(axe.getAttackDamage(), hoe.getAttackDamage());
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return Math.max(axe.getMiningSpeedMultiplier(stack, state), hoe.getMiningSpeedMultiplier(stack, state));
	}

	@Override
	public boolean isEffectiveOn(BlockState state) {
		return axe.isEffectiveOn(state) || hoe.isEffectiveOn(state);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return axe.useOnBlock(context).isAccepted() ? ActionResult.SUCCESS : hoe.useOnBlock(context);
	}
}
