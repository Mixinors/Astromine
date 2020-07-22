package com.github.chainmailstudios.astromine.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;

import com.github.chainmailstudios.astromine.common.utilities.ToolUtilities;

public class MultitoolItem extends MiningToolItem {
    private final MiningToolItem first;
    private final MiningToolItem second;

    public MultitoolItem(MiningToolItem first, MiningToolItem second, ToolMaterial material, Settings settings) {
        super(ToolUtilities.getAttackDamage(first, second)-material.getAttackDamage(), ToolUtilities.getAttackSpeed(first, second), material, ToolUtilities.getEffectiveBlocks(first, second), settings);
        this.first = first;
        this.second = second;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return Math.max(first.getMiningSpeedMultiplier(stack, state), second.getMiningSpeedMultiplier(stack, state));
    }

    @Override
    public boolean isEffectiveOn(BlockState state) {
        return first.isEffectiveOn(state) || second.isEffectiveOn(state);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult result = first.useOnBlock(context);
        return result.isAccepted() ? result : second.useOnBlock(context);
    }
}
