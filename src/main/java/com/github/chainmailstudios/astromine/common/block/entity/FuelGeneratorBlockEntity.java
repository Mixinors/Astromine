package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.inventory.InventoryComponent;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuelGeneratorBlockEntity extends AlphaBlockEntity {
	private final Map<Integer, ItemStack> contents = new HashMap<>();

	public FuelGeneratorBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
}
