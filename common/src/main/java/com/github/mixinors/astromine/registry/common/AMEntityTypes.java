/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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
import com.github.mixinors.astromine.common.entity.PrimitiveRocketEntity;
import com.github.mixinors.astromine.common.entity.SpaceSlimeEntity;
import com.github.mixinors.astromine.common.entity.SuperSpaceSlimeEntity;
import com.github.mixinors.astromine.mixin.common.SpawnRestrictionAccessor;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.entity.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.minecraft.world.Heightmap;

import java.util.function.Supplier;

public class AMEntityTypes {
	// TODO: Reimplement this on Forge module!
	public static RegistrySupplier<EntityType<PrimitiveRocketEntity>> PRIMITIVE_ROCKET = null;
	
	// TODO: Reimplement this on Forge module!
	public static RegistrySupplier<EntityType<SpaceSlimeEntity>> SPACE_SLIME = null;
	
	// TODO: Reimplement this on Forge module!
	public static RegistrySupplier<EntityType<SuperSpaceSlimeEntity>> SUPER_SPACE_SLIME = null;
	
	public static void init() {
		SpawnRestrictionAccessor.callRegister(AMEntityTypes.SPACE_SLIME.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpaceSlimeEntity::canSpawnInDark);
	}

	public static <T extends Entity> RegistrySupplier<EntityType<T>> registerBuilder(String id, Supplier<EntityType.Builder<T>> builder) {
		return registerBuilder(AMCommon.id(id), builder);
	}

	public static <T extends Entity> RegistrySupplier<EntityType<T>> registerBuilder(Identifier id, Supplier<EntityType.Builder<T>> builder) {
		return register(id, () -> builder.get().build(id.getPath()));
	}

	/**
	 * @param id
	 *        Name of EntityType instance to be registered
	 * @param type
	 *        EntityType instance to register
	 *
	 * @return Registered EntityType
	 */
	public static <T extends Entity> RegistrySupplier<EntityType<T>> register(String id, Supplier<EntityType<T>> type) {
		return register(AMCommon.id(id), type);
	}

	public static <T extends Entity> RegistrySupplier<EntityType<T>> register(Identifier id, Supplier<EntityType<T>> type) {
		return AMCommon.registry(Registry.ENTITY_TYPE_KEY).registerSupplied( id, type);
	}
}
