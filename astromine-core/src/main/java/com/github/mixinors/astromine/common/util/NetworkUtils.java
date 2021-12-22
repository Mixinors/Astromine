/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.util;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.github.mixinors.astromine.common.block.base.CableBlock;
import com.github.mixinors.astromine.common.component.world.WorldNetworkComponent;
import com.github.mixinors.astromine.common.network.NetworkInstance;
import com.github.mixinors.astromine.common.network.NetworkMember;
import com.github.mixinors.astromine.common.network.NetworkMemberNode;
import com.github.mixinors.astromine.common.network.NetworkNode;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkMemberRegistry;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class NetworkUtils {
	public static class Tracer {
		/**
		 * Traces a network of the given {@link NetworkType}
		 * from the specified initial {@link WorldPos}.
		 * <p>
		 * Interconnected networks will be merged if necessary.
		 */
		public static void trace(NetworkType type, WorldPos initialPosition) {
			World world = initialPosition.getWorld();

			WorldNetworkComponent networkComponent = WorldNetworkComponent.get(world);

			NetworkMember initialMember = NetworkMemberRegistry.get(initialPosition, null);

			if (!initialMember.acceptsType(type) || !initialMember.isNode(type) || networkComponent.contains(type, initialPosition.getBlockPos())) {
				return;
			}

			LongSet tracedPositions = new LongOpenHashSet();

			tracedPositions.add(initialPosition.getBlockPos().asLong());

			ArrayDeque<BlockPos> positionsToTrace = new ArrayDeque<>(Collections.singleton(initialPosition.getBlockPos()));

			NetworkInstance instance = new NetworkInstance(world, type);

			instance.addNode(NetworkNode.of(initialPosition.getBlockPos()));

			while (!positionsToTrace.isEmpty()) {
				BlockPos position = positionsToTrace.pop();

				boolean joined = false;

				WorldPos initialObject = WorldPos.of(world, position);

				for (Direction direction : Direction.values()) {
					BlockPos offsetPosition = position.offset(direction);
					long offsetPositionLong = offsetPosition.asLong();

					if (tracedPositions.contains(offsetPositionLong)) {
						continue;
					}

					WorldPos offsetObject = WorldPos.of(world, offsetPosition);

					NetworkMember offsetMember = NetworkMemberRegistry.get(offsetObject, direction.getOpposite());

					NetworkInstance existingInstance = networkComponent.get(type, offsetPosition);

					if (existingInstance != NetworkInstance.EMPTY) {
						existingInstance.join(instance);
						networkComponent.remove(instance);
						networkComponent.add(existingInstance);
						instance = existingInstance;
						joined = true;
					}

					if (offsetMember.acceptsType(type)) {
						if (offsetMember.isRequester(type) || offsetMember.isProvider(type) || offsetMember.isBuffer(type)) {
							instance.addMember(NetworkMemberNode.of(offsetPosition, direction.getOpposite()));
						}

						if (offsetMember.isNode(type) && offsetObject.getBlock() == initialObject.getBlock()) {
							positionsToTrace.addLast(offsetPosition);
							instance.addNode(NetworkNode.of(offsetPosition));
						}
					}

					tracedPositions.add(offsetPositionLong);
				}

				if (joined) {
					return;
				}
			}

			networkComponent.add(instance);
		}
	}

	/**
	 * A cable modeller,
	 * which scans neighboring blocks and produces
	 * a connected {@link BlockState} or {@link VoxelShape}.
	 */
	public static class Modeller {
		protected static final VoxelShape[] SHAPE_CACHE = new VoxelShape[64];

		public static int of(BlockState blockState) {
			int i = 0;

			for (Map.Entry<Direction, BooleanProperty> property : CableBlock.PROPERTIES.entrySet()) {
				if (blockState.get(property.getValue())) {
					i |= 1 << property.getKey().getId();
				}
			}

			return i;
		}

		public static Set<Direction> of(NetworkType type, BlockPos initialPosition, World world) {
			Set<Direction> directions = EnumSet.noneOf(Direction.class);

			WorldPos initialObject = WorldPos.of(world, initialPosition);

			for (Direction direction : Direction.values()) {
				WorldPos pos = WorldPos.of(world, initialPosition.offset(direction));

				NetworkMember offsetMember = NetworkMemberRegistry.get(pos, direction.getOpposite());

				if (offsetMember.acceptsType(type) && (!offsetMember.isNode(type) || pos.getBlock() == initialObject.getBlock())) {
					directions.add(direction);
				}
			}

			return directions;
		}

		/**
		 * Returns a {@link BlockState} with {@code directions}
		 * as {@link CableBlock} properties.
		 */
		public static BlockState toBlockState(Set<Direction> directions, BlockState state) {
			if (!(state.getBlock() instanceof CableBlock))
				return state;
			for (Direction direction : Direction.values()) {
				state = state.with(CableBlock.PROPERTIES.get(direction), directions.contains(direction));
			}
			return state;
		}

		/**
		 * Returns a {@link VoxelShape} with {@code directions}
		 * as {@link CableBlock} shapes.
		 */
		private static VoxelShape toVoxelShape(int directions, VoxelShape shape) {
			for (Direction direction : Direction.values()) {
				if ((directions & (0x1 << direction.getId())) != 0) {
					shape = VoxelShapes.union(shape, CableBlock.SHAPE_MAP.get(CableBlock.PROPERTIES.get(direction)));
				}
			}
			return shape;
		}

		/**
		 * Returns a {@link VoxelShape} with {@code directions}
		 * as {@link CableBlock} shapes, also caches the shapes.
		 */
		public static VoxelShape getVoxelShape(Set<Direction> directions) {
			int i = 0;

			for (Direction direction : directions) {
				i |= 1 << direction.getId();
			}

			return getVoxelShape(i);
		}

		/**
		 * Returns a {@link VoxelShape} with {@code directions}
		 * as {@link CableBlock} shapes, also caches the shapes.
		 */
		public static VoxelShape getVoxelShape(int directions) {
			VoxelShape shape = SHAPE_CACHE[directions];
			if (shape != null) {
				return shape;
			}
			return SHAPE_CACHE[directions] = toVoxelShape(directions, CableBlock.CENTER_SHAPE);
		}
	}
}
