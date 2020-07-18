package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class AstromineWorlds {
	public static final RegistryKey<World> SPACE_WORLD = RegistryKey.of(Registry.DIMENSION, AstromineCommon.identifier("earth_space"));

	public static void initialize() {

	}
}
