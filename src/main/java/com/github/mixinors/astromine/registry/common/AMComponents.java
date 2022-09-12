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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.component.entity.OxygenComponent;
import com.github.mixinors.astromine.common.component.level.RocketsComponent;
import com.github.mixinors.astromine.common.component.level.StationsComponent;
import com.github.mixinors.astromine.common.component.world.HoloBridgesComponent;
import com.github.mixinors.astromine.common.component.world.NetworksComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.entity.LivingEntity;

public class AMComponents implements WorldComponentInitializer, EntityComponentInitializer, LevelComponentInitializer {
	public static final ComponentKey<NetworksComponent> NETWORKS = ComponentRegistry.getOrCreate(AMCommon.id("networks"), NetworksComponent.class);
	public static final ComponentKey<HoloBridgesComponent> HOLO_BRIDGES = ComponentRegistry.getOrCreate(AMCommon.id("holo_bridges"), HoloBridgesComponent.class);
	
	public static final ComponentKey<OxygenComponent> OXYGEN_COMPONENT = ComponentRegistry.getOrCreate(AMCommon.id("oxygen"), OxygenComponent.class);
	public static final ComponentKey<StationsComponent> STATIONS = ComponentRegistry.getOrCreate(AMCommon.id("stations"), StationsComponent.class);
	public static final ComponentKey<RocketsComponent> ROCKETS = ComponentRegistry.getOrCreate(AMCommon.id("rockets"), RocketsComponent.class);
	
	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(NETWORKS, NetworksComponent::new);
		registry.register(HOLO_BRIDGES, HoloBridgesComponent::new);
		registry.register(ROCKETS, RocketsComponent::new);
		registry.register(STATIONS, StationsComponent::new);
	}
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, OXYGEN_COMPONENT, OxygenComponent::new);
	}
	
	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
	}
}
