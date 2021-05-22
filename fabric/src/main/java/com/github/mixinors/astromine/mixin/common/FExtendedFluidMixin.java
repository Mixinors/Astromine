package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.fluid.ExtendedFluid;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ExtendedFluid.class)
public class FExtendedFluidMixin {
	private static final Material astromine_MATERIAL = new FabricMaterialBuilder(MaterialColor.WATER)
			.allowsMovement()
			.lightPassesThrough()
			.destroyedByPiston()
			.replaceable()
			.liquid()
			.notSolid().build();
	
	@Overwrite
	@SuppressWarnings("all")
	private static Material proxy_getMaterial() {
		return astromine_MATERIAL;
	}
}