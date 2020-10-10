/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.discoveries.registry;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.Heightmap;

import com.github.chainmailstudios.astromine.discoveries.common.entity.PrimitiveRocketEntity;
import com.github.chainmailstudios.astromine.discoveries.common.entity.SpaceSlimeEntity;
import com.github.chainmailstudios.astromine.discoveries.common.entity.SuperSpaceSlimeEntity;
import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;

public class AstromineDiscoveriesEntityTypes extends AstromineEntityTypes {
	public static final EntityType<PrimitiveRocketEntity> PRIMITIVE_ROCKET = register("primitive_rocket", FabricEntityTypeBuilder.create(SpawnGroup.MISC, PrimitiveRocketEntity::new).dimensions(EntityDimensions.changing(1.5f, 22.5f)).trackable(256, 4).build());

	public static final EntityType<SpaceSlimeEntity> SPACE_SLIME = register("space_slime", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SpaceSlimeEntity::new).dimensions(EntityDimensions.changing(2.04F, 2.04F)).trackable(128, 4).build());

	public static final EntityType<SuperSpaceSlimeEntity> SUPER_SPACE_SLIME = register("super_space_slime", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SuperSpaceSlimeEntity::new).dimensions(EntityDimensions.changing(6.125F, 6.125F)).trackable(128, 4).build());

	public static void initialize() {
		FabricDefaultAttributeRegistry.register(SPACE_SLIME, HostileEntity.createHostileAttributes());
		FabricDefaultAttributeRegistry.register(SUPER_SPACE_SLIME, SuperSpaceSlimeEntity.createAttributes());

		AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
			if (entity instanceof SuperSpaceSlimeEntity) {
				if (world.random.nextInt(10) == 0) {
					SpaceSlimeEntity spaceSlimeEntity = AstromineDiscoveriesEntityTypes.SPACE_SLIME.create(world);
					spaceSlimeEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
					world.spawnEntity(spaceSlimeEntity);
				}
			}

			return ActionResult.PASS;
		});

		SpawnRestriction.register(AstromineDiscoveriesEntityTypes.SPACE_SLIME, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpaceSlimeEntity::canSpawnInDark);
	}
}
