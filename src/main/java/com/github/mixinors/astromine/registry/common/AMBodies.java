package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class AMBodies {
	public static final Body SUN = new Body.Builder()
			.setPosition(new Position(0.0F, 0.0F))
			.setSize(new Size(24.0F, 24.0F))
			.setMass(1.989D * Math.pow(10.0D, 30.0D))
			.setTemperature(5778.0D)
			.setTexture(AMCommon.id("textures/widget/sun.png"))
			.setTooltip(() -> ImmutableList.of(new TranslatableText("text.astromine.sun")))
			.createBody();
	
	public static final Body EARTH = new Body.Builder()
			.setPosition(new Position(149_600_000.0F, 0.0F))
			.setSize(new Size(16.0F, 16.0F))
			.setOrbitTime(2.0D)
			.setOrbitWidth(64.0F)
			.setOrbitHeight(64.0F)
			.setOrbitedBody(SUN)
			.setOrbitTidalLocked(false)
			.setMass(5.972 * Math.pow(10.0D, 24.0D))
			.setTemperature(5_778.0D)
			.setWorldKey(World.OVERWORLD)
			.setOrbitWorldKey(AMWorlds.EARTH_ORBIT_WORLD)
			.setTexture(AMCommon.id("textures/widget/earth.png"))
			.setTooltip(() -> ImmutableList.of(new TranslatableText("text.astromine.earth")))
			.createBody();
	
	public static final Body MOON = new Body.Builder()
			.setPosition(new Position(384_000.0F, 0.0F))
			.setSize(new Size(4.5F, 4.5F))
			.setOrbitTime(40.0D)
			.setOrbitWidth(24.0F)
			.setOrbitHeight(24.0F)
			.setOrbitedBody(EARTH)
			.setOrbitTidalLocked(true)
			.setMass(7.34767309 * Math.pow(10.0D, 22.0D))
			.setTemperature(26.0D)
			.setWorldKey(AMWorlds.MOON_WORLD)
			.setTexture(AMCommon.id("textures/widget/moon.png"))
			.setTooltip(() -> ImmutableList.of(new TranslatableText("text.astromine.moon")))
			.createBody();
	
	public static final Body MARS = new Body.Builder()
			.setPosition(new Position(173_030_000.0F, 0.0F))
			.setSize(new Size(9.0F, 9.0F))
			.setOrbitTime(1.8D)
			.setOrbitWidth(128.0F)
			.setOrbitHeight(128.0F)
			.setOrbitedBody(SUN)
			.setOrbitTidalLocked(false)
			.setMass(6.39 * Math.pow(10.0D, 23.0D))
			.setTemperature(210.0D)
			.setWorldKey(AMWorlds.MOON_WORLD)
			.setTexture(AMCommon.id("textures/widget/mars.png"))
			.setTooltip(() -> ImmutableList.of(new TranslatableText("text.astromine.mars")))
			.createBody();
	
	public static void init() {
		BodyManager.add(SUN);
		
		BodyManager.add(EARTH);
		BodyManager.add(MOON);
		BodyManager.add(MARS);
	}
}
