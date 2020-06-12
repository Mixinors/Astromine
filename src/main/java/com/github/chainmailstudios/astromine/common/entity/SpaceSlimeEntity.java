package com.github.chainmailstudios.astromine.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;

public class SpaceSlimeEntity extends SlimeEntity {

    public SpaceSlimeEntity(EntityType<? extends SlimeEntity> entityType, World world) {
        super(entityType, world);
    }
}
