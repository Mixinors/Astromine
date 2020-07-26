package com.github.chainmailstudios.astromine.common.utilities;

import com.zundrel.wrenchable.WrenchableUtilities;
import com.zundrel.wrenchable.block.BlockWrenchable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public interface MachineBlockWrenchable extends BlockWrenchable {
    @Override
    default void onWrenched(World world, PlayerEntity playerEntity, BlockHitResult blockHitResult) {
        if (playerEntity.isSneaking()) {
            world.breakBlock(blockHitResult.getBlockPos(), true, playerEntity);
        }

        WrenchableUtilities.doHorizontalFacingBehavior(world, playerEntity, blockHitResult);
    }
}
