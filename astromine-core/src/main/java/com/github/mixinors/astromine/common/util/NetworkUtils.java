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

package com.github.mixinors.astromine.common.util;

import java.util.ArrayDeque;
import java.util.EnumSet;
import java.util.Set;

import com.github.mixinors.astromine.common.block.network.CableBlock;
import com.github.mixinors.astromine.common.component.world.WorldNetworkComponent;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.network.type.NetworkType;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import net.minecraft.block.BlockState;
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
		public static void trace(NetworkType type, WorldPos startPos) {
			var world = startPos.getWorld();
			
			var network = WorldNetworkComponent.get(world);
			
			if (network.contains(type, startPos.getBlockPos())) {
				// Starting position already exists.
				return;
			}
			
			var startNode = new Network.Node(startPos.getBlockPos());
			
			// Store traced positions so we don't repeat them.
			var tracedPos = new LongOpenHashSet();
			tracedPos.add(startPos.getBlockPos().asLong());
			
			// Store positions to trace so we can go back to them.
			var posToTrace = new ArrayDeque<BlockPos>();
			posToTrace.add(startPos.getBlockPos());
			
			var instance = new Network(world, type);
			instance.addNode(startNode);
			
			while (!posToTrace.isEmpty()) {
				var pos = posToTrace.pop();
				
				var joined = false;
				
				var initialWorldPos = new WorldPos(world, pos);
				
				for (var dir : Direction.values()) {
					var offsetPos = pos.offset(dir);
					var offsetPosLong = offsetPos.asLong();
					
					if (tracedPos.contains(offsetPosLong)) {
						continue;
					}
					
					var offsetWorldPos = new WorldPos(world, offsetPos);
					
					var offsetStorage = type.find(offsetWorldPos, dir.getOpposite());
					
					var existingInstance = network.get(type, offsetPos);
					
					if (existingInstance != null) {
						existingInstance.join(instance);
						network.remove(instance);
						network.add(existingInstance);
						instance = existingInstance;
						joined = true;
					}
					
					// Add a member if a storage is present.
					// Otherwise, it must be a cable block.
					if (offsetStorage != null) {
						instance.addMember(new Network.Member(offsetPos, dir.getOpposite()));
					} else if (offsetWorldPos.getBlock() == initialWorldPos.getBlock()) {
						posToTrace.addLast(offsetPos);
						
						instance.addNode(new Network.Node(offsetPos));
					}
					
					tracedPos.add(offsetPosLong);
				}
				
				if (joined) {
					return;
				}
			}
			
			network.add(instance);
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
			var i = 0;
			
			for (var property : CableBlock.PROPERTIES.entrySet()) {
				if (blockState.get(property.getValue())) {
					i |= 1 << property.getKey().getId();
				}
			}
			
			return i;
		}
		
		public static Set<Direction> of(NetworkType type, BlockPos initialPosition, World world) {
			var directions = EnumSet.noneOf(Direction.class);
			
			var initialWorldPos = new WorldPos(world, initialPosition);
			
			for (var dir : Direction.values()) {
				var offsetWorldPos = new WorldPos(world, initialPosition.offset(dir));
				
				if (type.find(offsetWorldPos, dir.getOpposite()) != null || (offsetWorldPos.getBlock() == initialWorldPos.getBlock())) {
					directions.add(dir);
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
			for (var direction : Direction.values()) {
				state = state.with(CableBlock.PROPERTIES.get(direction), directions.contains(direction));
			}
			return state;
		}
		
		/**
		 * Returns a {@link VoxelShape} with {@code directions}
		 * as {@link CableBlock} shapes.
		 */
		private static VoxelShape toVoxelShape(int directions, VoxelShape shape) {
			for (var direction : Direction.values()) {
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
			var i = 0;
			
			for (var direction : directions) {
				i |= 1 << direction.getId();
			}
			
			return getVoxelShape(i);
		}
		
		/**
		 * Returns a {@link VoxelShape} with {@code directions}
		 * as {@link CableBlock} shapes, also caches the shapes.
		 */
		public static VoxelShape getVoxelShape(int directions) {
			var shape = SHAPE_CACHE[directions];
			if (shape != null) {
				return shape;
			}
			return SHAPE_CACHE[directions] = toVoxelShape(directions, CableBlock.CENTER_SHAPE);
		}
	}
}