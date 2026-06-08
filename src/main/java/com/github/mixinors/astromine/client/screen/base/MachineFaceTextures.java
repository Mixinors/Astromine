/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.client.screen.base;

import com.github.mixinors.astromine.common.block.base.BlockWithEntity;
import com.github.mixinors.astromine.common.util.MirrorUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

final class MachineFaceTextures {
	private MachineFaceTextures() {
	}

	static ResourceLocation texture(BlockState state, Direction physicalSide, Direction facing) {
		var blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
		var localSide = MirrorUtils.rotate(physicalSide, facing);
		var activeSuffix = state.hasProperty(BlockWithEntity.ACTIVE) && state.getValue(BlockWithEntity.ACTIVE) && !blockId.getPath().equals("rocket_controller") ? "_active" : "";

		return ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), "textures/block/" + blockId.getPath() + "_" + suffix(localSide) + activeSuffix + ".png");
	}

	private static String suffix(Direction localSide) {
		return switch (localSide) {
			case UP -> "top";
			case DOWN -> "bottom";
			case EAST -> "left";
			case WEST -> "right";
			case SOUTH -> "back";
			case NORTH -> "front";
		};
	}
}
