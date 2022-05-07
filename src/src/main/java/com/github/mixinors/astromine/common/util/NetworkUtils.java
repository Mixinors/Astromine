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

import com.github.mixinors.astromine.common.block.entity.cable.CableBlockEntity;
import com.github.mixinors.astromine.common.block.network.CableBlock;
import com.github.mixinors.astromine.common.component.world.WorldNetworkComponent;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.network.type.NetworkType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class NetworkUtils {
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
		
		// Store positions to trace, so we can go back to them.
		var posToTrace = new ArrayDeque<BlockPos>();
		posToTrace.add(startPos.getBlockPos());
		
		var instance = new Network(world, type);
		instance.addNode(startNode);
		
		while (!posToTrace.isEmpty()) {
			var pos = posToTrace.pop();
			
			var joined = false;
			
			var initialWorldPos = new WorldPos(world, pos);
			
			var cableBlockEntity = (CableBlockEntity) initialWorldPos.getBlockEntity();
			
			if (cableBlockEntity == null) continue;
			
			var connections = cableBlockEntity.getConnections();
			
			if (connections == null) continue;
			
			for (var dir : Direction.values()) {
				var offsetPos = pos.offset(dir);
				var offsetPosLong = offsetPos.asLong();
				
				if (tracedPos.contains(offsetPosLong)) {
					continue;
				}
				
				var offsetWorldPos = new WorldPos(world, offsetPos);
				
				var offsetStorage = type.find(offsetWorldPos, dir.getOpposite());
				
				var existingInstance = network.get(type, offsetPos);
				
				// Merge networks if necessary.
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
					var siding = StorageSiding.NONE;
					
					if (connections.isInsert(dir)) {
						siding = StorageSiding.INSERT;
					} else if (connections.isExtract(dir)) {
						siding = StorageSiding.EXTRACT;
					} else if (connections.isInsertExtract(dir)) {
						siding = StorageSiding.INSERT_EXTRACT;
					}
					
					instance.addMember(new Network.Member(offsetPos, dir.getOpposite(), siding));
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