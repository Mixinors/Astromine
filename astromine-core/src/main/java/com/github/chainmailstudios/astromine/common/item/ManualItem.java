package com.github.chainmailstudios.astromine.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;

public class ManualItem extends Item {
	public ManualItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient() && user instanceof ServerPlayerEntity) {
			PatchouliAPI.instance.openBookGUI((ServerPlayerEntity) user, new Identifier("astromine-core:manual"));
			return TypedActionResult.success(user.getStackInHand(hand));
		}
		return TypedActionResult.consume(user.getStackInHand(hand));
	}
}
