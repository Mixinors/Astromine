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
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.world.Heightmap;

import java.util.function.Supplier;

public class AMEntityTypes {
	public static final RegistrySupplier<EntityType<RocketEntity>> ROCKET = register("rocket", () -> FabricEntityTypeBuilder.create(SpawnGroup.MISC, RocketEntity::new).dimensions(EntityDimensions.changing(1.5F, 22.5F)).trackable(128, 4).build());
	public static final RegistrySupplier<EntityType<SpaceSlimeEntity>> SPACE_SLIME = register("space_slime", () -> FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SpaceSlimeEntity::new).dimensions(EntityDimensions.changing(2.04F, 2.04F)).trackable(128, 4).build());
	public static final RegistrySupplier<EntityType<SuperSpaceSlimeEntity>> SUPER_SPACE_SLIME = register("super_space_slime", () -> FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SuperSpaceSlimeEntity::new).dimensions(EntityDimensions.changing(6.125F, 6.125F)).trackable(128, 4).build());
	
	public static void init() {
		FabricDefaultAttributeRegistry.register(SPACE_SLIME.get(), HostileEntity.createHostileAttributes());
		FabricDefaultAttributeRegistry.register(SUPER_SPACE_SLIME.get(), SuperSpaceSlimeEntity.createAttributes());
		
		AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
			if (entity instanceof SuperSpaceSlimeEntity) {
				if (world.random.nextInt(10) == 0) {
					var spaceSlimeEntity = AMEntityTypes.SPACE_SLIME.get().create(world);
					spaceSlimeEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
					
					world.spawnEntity(spaceSlimeEntity);
				}
			}
			
			return ActionResult.PASS;
		});
		
		SpawnRestriction.register(AMEntityTypes.SPACE_SLIME.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpaceSlimeEntity::canSpawnInDark);
	}
	
	public static <T extends Entity> RegistrySupplier<EntityType<T>> registerBuilder(String id, Supplier<EntityType.Builder<T>> builder) {
		return registerBuilder(AMCommon.id(id), builder);
	}
	
	public static <T extends Entity> RegistrySupplier<EntityType<T>> registerBuilder(Identifier id, Supplier<EntityType.Builder<T>> builder) {
		return register(id, () -> builder.get().build(id.getPath()));
	}
	
	/**
	 * @param id   Name of EntityType instance to be registered
	 * @param type EntityType instance to register
	 *
	 * @return Registered EntityType
	 */
	public static <T extends Entity> RegistrySupplier<EntityType<T>> register(String id, Supplier<EntityType<T>> type) {
		return register(AMCommon.id(id), type);
	}
	
	public static <T extends Entity> RegistrySupplier<EntityType<T>> register(Identifier id, Supplier<EntityType<T>> type) {
		return AMCommon.registry(RegistryKeys.ENTITY_TYPE).register(id, type);
	}
}
