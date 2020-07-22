package com.github.chainmailstudios.astromine.common.item;

import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;

import java.util.Set;

public class ShaxeItem extends MiningToolItem {
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
}
