package com.github.mixinors.astromine.mixin.common;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TallPlantBlock.class)
public interface TallPlantBlockAccessor {
	@Invoker
	static void onBreakInCreative(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
