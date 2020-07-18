package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.common.world.feature.MeteorGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class MeteorSpawnerDevItem extends Item {

	public MeteorSpawnerDevItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient) {
			MeteorGenerator meteorGenerator = new MeteorGenerator(world.random, user.getBlockPos().getX(), user.getBlockPos().getZ());
			meteorGenerator.generate((ServerWorldAccess) world, new ChunkPos(user.getBlockPos()), world.random, user.getBlockPos());
		}

		return super.use(world, user, hand);
	}
}
