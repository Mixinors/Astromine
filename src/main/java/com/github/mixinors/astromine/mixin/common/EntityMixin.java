package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.access.EntityAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccessor {
	@Override
	public boolean astromine$isInIndustrialFluid() {
		return false;
	}
}
