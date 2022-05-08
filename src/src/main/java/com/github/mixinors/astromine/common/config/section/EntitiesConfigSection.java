package com.github.mixinors.astromine.common.config.section;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public class EntitiesConfigSection {
	@Comment("Chance for Piglins to realize if you try to trick them (1 in x)")
	public int piglinAngerChance = 5;
	
	@Comment("The maximum fuel a Primitive Rocket can hold")
	public long primitiveRocketFluid = FluidConstants.BUCKET * 16L;
}
