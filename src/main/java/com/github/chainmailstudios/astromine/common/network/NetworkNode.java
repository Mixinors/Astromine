package com.github.chainmailstudios.astromine.common.network;

import net.minecraft.util.math.BlockPos;

public class NetworkNode {
    public BlockPos position;

    public static NetworkNode of(BlockPos position) {
        return new NetworkNode().setPosition(position);
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public NetworkNode setPosition(BlockPos position) {
        this.position = position;
        return this;
    }

    @Override
    public int hashCode() {
        return this.position.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NetworkNode)) return false;
        return ((NetworkNode) object).position.equals(this.position);
    }
}
