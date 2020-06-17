package com.github.chainmailstudios.astromine.misc;

import net.minecraft.block.BlockState;
import net.minecraft.world.ModifiableWorld;

import com.terraformersmc.shapes.api.Filler;
import com.terraformersmc.shapes.api.Position;

public class SimpleFiller implements Filler {
	private final ModifiableWorld world;
	private final BlockState state;

	public SimpleFiller(ModifiableWorld world, BlockState state) {
		this.world = world;
		this.state = state;
	}

	public static SimpleFiller of(ModifiableWorld world, BlockState state) {
		return new SimpleFiller(world, state);
	}

	@Override
	public void accept(Position position) {
		this.world.setBlockState(position.toBlockPos(), this.state, 3);
	}
}
