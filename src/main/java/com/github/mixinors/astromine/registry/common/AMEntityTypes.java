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
import com.github.mixinors.astromine.common.entity.rocket.RocketEntity;
import com.github.mixinors.astromine.common.entity.slime.SpaceSlimeEntity;
import com.github.mixinors.astromine.common.entity.slime.SuperSpaceSlimeEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class AMEntityTypes {
	private static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, AMCommon.MOD_ID);
	
	public static final DeferredHolder<EntityType<?>, EntityType<RocketEntity>> ROCKET = registerBuilder("rocket", () -> EntityType.Builder.of(RocketEntity::new, MobCategory.MISC).sized(1.5F, 22.5F).clientTrackingRange(128).updateInterval(4));
	public static final DeferredHolder<EntityType<?>, EntityType<SpaceSlimeEntity>> SPACE_SLIME = registerBuilder("space_slime", () -> EntityType.Builder.of(SpaceSlimeEntity::new, MobCategory.MONSTER).sized(0.52F, 0.52F).eyeHeight(0.325F).spawnDimensionsScale(4.0F).clientTrackingRange(128).updateInterval(2));
	public static final DeferredHolder<EntityType<?>, EntityType<SuperSpaceSlimeEntity>> SUPER_SPACE_SLIME = registerBuilder("super_space_slime", () -> EntityType.Builder.of(SuperSpaceSlimeEntity::new, MobCategory.MONSTER).sized(6.125F, 6.125F).clientTrackingRange(128).updateInterval(4));
	
	public static void init() {
		REGISTRY.register(AMCommon.modEventBus());
		AMCommon.modEventBus().addListener(AMEntityTypes::registerAttributes);
		AMCommon.modEventBus().addListener(AMEntityTypes::registerSpawnPlacements);
		NeoForge.EVENT_BUS.addListener(AMEntityTypes::onAttackEntity);
	}
	
	public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerBuilder(String id, Supplier<EntityType.Builder<T>> builder) {
		return registerBuilder(AMCommon.id(id), builder);
	}
	
	public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerBuilder(ResourceLocation id, Supplier<EntityType.Builder<T>> builder) {
		return register(id, () -> builder.get().build(id.getPath()));
	}
	
	/**
	 * @param id   Name of EntityType instance to be registered
	 * @param type EntityType instance to register
	 *
	 * @return Registered EntityType
	 */
	public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String id, Supplier<EntityType<T>> type) {
		return register(AMCommon.id(id), type);
	}
	
	public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(ResourceLocation id, Supplier<EntityType<T>> type) {
		return REGISTRY.register(id.getPath(), type);
	}
	
	private static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SPACE_SLIME.get(), Monster.createMonsterAttributes().build());
		event.put(SUPER_SPACE_SLIME.get(), SuperSpaceSlimeEntity.createAttributes().build());
	}
	
	private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		event.register(SPACE_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpaceSlimeEntity::canSpawnInDark, RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}
	
	private static void onAttackEntity(AttackEntityEvent event) {
		var entity = event.getTarget();
		if (!(entity instanceof SuperSpaceSlimeEntity) || entity.level().random.nextInt(10) != 0) {
			return;
		}
		
		var spaceSlimeEntity = SPACE_SLIME.get().create(entity.level());
		if (spaceSlimeEntity != null) {
			spaceSlimeEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
			entity.level().addFreshEntity(spaceSlimeEntity);
		}
	}
}
