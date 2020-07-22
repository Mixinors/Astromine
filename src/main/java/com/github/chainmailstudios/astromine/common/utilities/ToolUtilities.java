package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.block.Block;
import net.minecraft.item.MiningToolItem;

import com.github.chainmailstudios.astromine.access.MiningToolItemAccess;

import com.google.common.collect.Sets;
import java.util.Set;

public class ToolUtilities {
    public static float getAttackDamage(MiningToolItem first, MiningToolItem second) {
        return Math.max(first.getAttackDamage(), second.getAttackDamage());
    }

    public static float getAttackSpeed(MiningToolItem first, MiningToolItem second) {
        return (float)(((MiningToolItemAccess) first).astromine_getAttackSpeed()+((MiningToolItemAccess) second).astromine_getAttackSpeed())/2;
    }

    public static Set<Block> getEffectiveBlocks(MiningToolItem first, MiningToolItem second) {
        return Sets.union(((MiningToolItemAccess) first).astromine_getEffectiveBlocks(), ((MiningToolItemAccess) second).astromine_getEffectiveBlocks());
    }
}
