package com.github.chainmailstudios.astromine.common.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.joml.Vector3i;

public class MultiblockAnchor<T extends MultiblockType> {
	private final T type;

	public MultiblockAnchor(T type) {
		this.type = type;
	}

	public boolean canBuild(World world, Vector3i position) {
		return type.getStructure().entrySet().stream().allMatch(entry -> {
			Vector3i vector = position.add(entry.getKey());
			return world.getBlockState(new BlockPos(vector.x, vector.y, vector.z)).getBlock() == entry.getValue();
		});
	}
}
