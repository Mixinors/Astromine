package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.common.item.DrillItem;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DrillItem.class)
public abstract class FDrillItemMixin implements DynamicAttributeTool {
	@Shadow protected abstract double getEnergyConsumed();
	
	@Shadow @Final private ToolMaterial material;
	
	@Override
	public float postProcessMiningSpeed(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user, float currentSpeed, boolean isEffective) {
		return EnergyComponent.from(stack).getAmount() <= getEnergyConsumed() ? 0F : currentSpeed;
	}

	@Override
	public float getMiningSpeedMultiplier(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		return material.getMiningSpeedMultiplier();
	}

	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		return material.getMiningLevel();
	}
}
