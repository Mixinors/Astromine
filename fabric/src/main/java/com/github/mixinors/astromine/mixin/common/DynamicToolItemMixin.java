package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.item.DynamicToolItem;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DynamicToolItem.class)
public class DynamicToolItemMixin implements DynamicAttributeTool {
	@Shadow @Final public MiningToolItem first;
	
	@Shadow @Final private ToolMaterial material;
	
	@Shadow @Final public MiningToolItem second;
	
	@Override
	public float getMiningSpeedMultiplier(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (first.isIn(tag) || second.isIn(tag))
			return material.getMiningSpeedMultiplier();
		return 1;
	}

	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (first.isIn(tag) || second.isIn(tag))
			return material.getMiningLevel();
		return 0;
	}
}
