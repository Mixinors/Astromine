package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.MiningToolItem;

import com.github.chainmailstudios.astromine.access.MiningToolItemAccess;

import com.google.common.collect.Multimap;
import java.util.Set;
import java.util.UUID;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin implements MiningToolItemAccess {
    @Shadow @Final private Set<Block> effectiveBlocks;

    @Shadow public abstract Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot);

    @Override
    public double astromine_getAttackSpeed() {
        return getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_SPEED).stream().filter((EntityAttributeModifier modifier) -> modifier.getId().equals(UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3"))).findFirst().get().getValue();
    }

    @Override
    public Set<Block> astromine_getEffectiveBlocks() {
        return effectiveBlocks;
    }
}
