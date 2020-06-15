package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.registry.SkyboxRegistry;
import com.github.chainmailstudios.astromine.client.render.skybox.SpaceSkybox;
import com.github.chainmailstudios.astromine.world.AstromineDimensionTypes;

public class AstromineSkyboxes {
	public static void initialize() {
		SkyboxRegistry.INSTANCE.register(AstromineDimensionTypes.KEY_ID, new SpaceSkybox.Builder()
			.up(AstromineCommon.identifier("textures/skybox/earth_space_up.png"))
			.down(AstromineCommon.identifier("textures/skybox/earth_space_down.png"))
			.north(AstromineCommon.identifier("textures/skybox/earth_space_north.png"))
			.south(AstromineCommon.identifier("textures/skybox/earth_space_south.png"))
			.west(AstromineCommon.identifier("textures/skybox/earth_space_west.png"))
			.east(AstromineCommon.identifier("textures/skybox/earth_space_east.png"))
			.planet(AstromineCommon.identifier("textures/skybox/earth.png"))
			.build());
	}
}
