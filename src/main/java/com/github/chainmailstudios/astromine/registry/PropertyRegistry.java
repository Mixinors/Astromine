package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fluid.logic.Property;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class PropertyRegistry extends SimpleRegistry<Property> {
    public static final Identifier IDENTIFIER = new Identifier(AstromineCommon.LOG_ID, "property");

    public static final PropertyRegistry INSTANCE = new PropertyRegistry();

    public PropertyRegistry() {
        super(RegistryKey.ofRegistry(IDENTIFIER), Lifecycle.stable());
    }
}
