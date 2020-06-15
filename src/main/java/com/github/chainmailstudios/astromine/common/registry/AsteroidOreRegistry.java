package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.google.common.collect.*;
import net.minecraft.block.Block;

import java.util.*;

public class AsteroidOreRegistry {
    private final HashMultimap<Integer, Block> ENTRIES = HashMultimap.create();

    public static final AsteroidOreRegistry INSTANCE = new AsteroidOreRegistry();

    private AsteroidOreRegistry() {
        // Unused.
    }

    public List<Block> get(Integer chance) {
        return Lists.newArrayList(ENTRIES.get(chance));
    }

    public void register(Range<Integer> range, Block block) {
        if (range.getMinimum() > range.getMaximum()) range = Range.of(range.getMaximum(), range.getMinimum());

        for (int chance = range.getMinimum(); chance < range.getMaximum(); ++chance) {
            register(chance, block);
        }
    }

    public void register(Integer chance, Block block) {
        ENTRIES.put(chance, block);
    }

    public void unregister(Integer chance, Block block) {
        ENTRIES.remove(chance, block);
    }
}
