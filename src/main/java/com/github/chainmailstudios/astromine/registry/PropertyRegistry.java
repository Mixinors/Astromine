package com.github.chainmailstudios.astromine.registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import com.mojang.serialization.Lifecycle;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidProperty;

public class PropertyRegistry extends SimpleRegistry<FluidProperty> {
	public static final Identifier IDENTIFIER = AstromineCommon.identifier("property");

	public static final PropertyRegistry INSTANCE = new PropertyRegistry();

	public PropertyRegistry() {
		super(RegistryKey.ofRegistry(IDENTIFIER), Lifecycle.stable());
	}
}
