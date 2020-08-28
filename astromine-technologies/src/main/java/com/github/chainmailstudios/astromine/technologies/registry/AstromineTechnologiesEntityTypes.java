package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;
import com.github.chainmailstudios.astromine.technologies.common.entity.RocketEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class AstromineTechnologiesEntityTypes extends AstromineEntityTypes {
	public static final EntityType<RocketEntity> ROCKET = register("rocket", FabricEntityTypeBuilder.create(SpawnGroup.MISC, RocketEntity::new).dimensions(EntityDimensions.changing(1.5f, 20f)).trackable(256, 4).build());

	public static void initialize() {

	}
}
