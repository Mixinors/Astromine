package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AMBodies {
	public Identifier SUN = AMCommon.id("sun");
	
	public Identifier EARTH = AMCommon.id("earth");
	public Identifier MOON = AMCommon.id("moon");
	
	public static void init() {

	}
}
