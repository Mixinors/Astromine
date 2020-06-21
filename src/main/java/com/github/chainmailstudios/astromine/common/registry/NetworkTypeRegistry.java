package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class NetworkTypeRegistry {
    public static final NetworkTypeRegistry INSTANCE = new NetworkTypeRegistry();

    private static final BiMap<Identifier, NetworkType> entries = HashBiMap.create();

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

    public NetworkType getById(Identifier identifier) {
        return entries.get(identifier);
    }

    public Identifier getId(NetworkType type) {
        return entries.inverse().get(type);
    }
}
