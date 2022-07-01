/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.registry.SkyboxRegistry;
import com.github.mixinors.astromine.client.render.skybox.SpaceSkybox;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class AMSkyboxes {
	public static void init() {
		registerOrbitClouds(AMWorlds.EARTH_ORBIT_WORLD, "earth");
		registerOrbit(AMWorlds.MOON_ORBIT_WORLD, "moon");
		
		registerPlanet(AMWorlds.MOON_WORLD, "moon");
	}
	
	public static void registerOrbitClouds(RegistryKey<World> world, String planetName) {
		SkyboxRegistry.INSTANCE.register(
				world,
				new SpaceSkybox.Builder()
						.up(AMCommon.id("textures/skybox/" + planetName + "_orbit_up.png"))
						.down(AMCommon.id("textures/skybox/" + planetName + "_orbit_down.png"))
						.north(AMCommon.id("textures/skybox/" + planetName + "_orbit_north.png"))
						.south(AMCommon.id("textures/skybox/" + planetName + "_orbit_south.png"))
						.west(AMCommon.id("textures/skybox/" + planetName + "_orbit_west.png"))
						.east(AMCommon.id("textures/skybox/" + planetName + "_orbit_east.png"))
						.planet(AMCommon.id("textures/skybox/" + planetName + ".png"))
						.cloud(AMCommon.id("textures/skybox/" + planetName + "_cloud.png"))
						.build());
	}
	
	
	public static void registerOrbit(RegistryKey<World> world, String planetName) {
		SkyboxRegistry.INSTANCE.register(
				world,
				new SpaceSkybox.Builder()
						.up(AMCommon.id("textures/skybox/" + planetName + "_orbit_up.png"))
						.down(AMCommon.id("textures/skybox/" + planetName + "_orbit_down.png"))
						.north(AMCommon.id("textures/skybox/" + planetName + "_orbit_north.png"))
						.south(AMCommon.id("textures/skybox/" + planetName + "_orbit_south.png"))
						.west(AMCommon.id("textures/skybox/" + planetName + "_orbit_west.png"))
						.east(AMCommon.id("textures/skybox/" + planetName + "_orbit_east.png"))
						.planet(AMCommon.id("textures/skybox/" + planetName + ".png"))
						.build());
	}
	
	public static void registerPlanet(RegistryKey<World> world, String planetName) {
		SkyboxRegistry.INSTANCE.register(
				world,
				new SpaceSkybox.Builder()
						.up(AMCommon.id("textures/skybox/" + planetName + "_planet_up.png"))
						.down(AMCommon.id("textures/skybox/" + planetName + "_planet_down.png"))
						.north(AMCommon.id("textures/skybox/" + planetName + "_planet_north.png"))
						.south(AMCommon.id("textures/skybox/" + planetName + "_planet_south.png"))
						.west(AMCommon.id("textures/skybox/" + planetName + "_planet_west.png"))
						.east(AMCommon.id("textures/skybox/" + planetName + "_planet_east.png"))
						.build());
	}
}
