package com.github.mixinors.astromine.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {
	@Accessor(value = "submergedFluidTag")
	Tag<Fluid> getField_25599();
}
