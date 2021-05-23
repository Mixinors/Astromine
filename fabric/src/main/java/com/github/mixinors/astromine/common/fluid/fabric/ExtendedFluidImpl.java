package com.github.mixinors.astromine.common.fluid.fabric;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

public class ExtendedFluidImpl {
	public static final Material MATERIAL = new FabricMaterialBuilder(MaterialColor.WATER)
			.allowsMovement()
			.lightPassesThrough()
			.destroyedByPiston()
			.replaceable()
			.liquid()
			.notSolid().build();
	
	public static Material getMaterial() {
		return MATERIAL;
	}
}
