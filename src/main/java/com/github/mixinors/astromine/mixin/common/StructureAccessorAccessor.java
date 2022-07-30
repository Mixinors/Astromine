package com.github.mixinors.astromine.mixin.common;

import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureAccessor.class)
public interface StructureAccessorAccessor {
	@Accessor
	WorldAccess getWorld();
}
