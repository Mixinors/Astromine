package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.network.NetworkType;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class NetworkTypeRegistry {
    public static final NetworkTypeRegistry INSTANCE = new NetworkTypeRegistry();

    private static final Map<Identifier, NetworkType> entries = new HashMap<>();

    private NetworkTypeRegistry() {
        // Unused.
    }

    public NetworkType register(Identifier identifier, NetworkType type) {
        entries.put(identifier, type);
        return type;
    }

    public void unregister(Identifier identifier, NetworkType type) {
        entries.remove(identifier, type);
    }

    public NetworkType get(Identifier identifier) {
        return entries.get(identifier);
    }
}
