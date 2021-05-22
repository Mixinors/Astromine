package com.github.mixinors.astromine.common.registry;

import com.github.mixinors.astromine.common.entity.PrimitiveRocketEntity;
import com.github.mixinors.astromine.common.entity.SpaceSlimeEntity;
import com.github.mixinors.astromine.common.entity.SuperSpaceSlimeEntity;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.ActionResult;

public class AMFEntityTypes extends AMEntityTypes {
	public static void init() {
		PRIMITIVE_ROCKET = register("primitive_rocket", () -> FabricEntityTypeBuilder.create(SpawnGroup.MISC, PrimitiveRocketEntity::new).dimensions(EntityDimensions.changing(1.5f, 22.5f)).trackable(256, 4).build());
		
		SPACE_SLIME = register("space_slime", () -> FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SpaceSlimeEntity::new).dimensions(EntityDimensions.changing(2.04F, 2.04F)).trackable(128, 4).build());
		
		SUPER_SPACE_SLIME = register("super_space_slime", () -> FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SuperSpaceSlimeEntity::new).dimensions(EntityDimensions.changing(6.125F, 6.125F)).trackable(128, 4).build());
		
		FabricDefaultAttributeRegistry.register(SPACE_SLIME.get(), HostileEntity.createHostileAttributes());
		FabricDefaultAttributeRegistry.register(SUPER_SPACE_SLIME.get(), SuperSpaceSlimeEntity.createAttributes());
	}
}
