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

import com.github.mixinors.astromine.common.block.entity.cable.CableBlockEntity;
import com.github.mixinors.astromine.common.component.world.NetworksComponent;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayDeque;

public class NetworkUtils {
	public static void trace(NetworkType<?> type, World world, BlockPos startPos) {
		var network = NetworksComponent.get(world);
		
		// Starting position already exists.
		if (network.contains(type, startPos)) {
			return;
		}
		
		var startNode = new Network.Node(startPos);
		
		// Store traced positions so we don't repeat them.
		var tracedPos = new LongOpenHashSet();
		tracedPos.add(startPos.asLong());
		
		// Store positions to trace, so we can go back to them.
		var posToTrace = new ArrayDeque<BlockPos>();
		posToTrace.add(startPos);
		
		var instance = new Network(world, type);
		instance.getNodes().add(startNode);
		
		while (!posToTrace.isEmpty()) {
			var pos = posToTrace.pop();
			
			var joined = false;
			
			var initialBlockState = world.getBlockState(pos);
			
			var cableBlockEntity = (CableBlockEntity) world.getBlockEntity(pos);
			
			if (cableBlockEntity == null) {
				continue;
			}
			
			var connections = cableBlockEntity.getConnections();
			
			if (connections == null) {
				continue;
			}
			
			for (var directions : DirectionUtils.VALUES) {
				var offsetPos = pos.offset(directions);
				var offsetPosLong = offsetPos.asLong();
				
				if (tracedPos.contains(offsetPosLong)) {
					continue;
				}
				
				var offsetStorage = type.find(world, offsetPos, directions.getOpposite());
				
				var existingInstance = network.get(type, offsetPos);
				
				// Merge networks if necessary.
				if (existingInstance != null) {
					existingInstance.addNetwork(instance);
					
					network.remove(instance);
					network.add(existingInstance);
					
					instance = existingInstance;
					
					joined = true;
				}
				
				var offsetBlockState = world.getBlockState(offsetPos);
				
				// Add a member if a storage is present.
				// Otherwise, it must be a cable block.
				if (offsetStorage != null) {
					var siding = StorageSiding.NONE;
					
					if (connections.isInsert(directions)) {
						siding = StorageSiding.INSERT;
					} else if (connections.isExtract(directions)) {
						siding = StorageSiding.EXTRACT;
					} else if (connections.isInsertExtract(directions)) {
						siding = StorageSiding.INSERT_EXTRACT;
					}
					
					instance.getMembers().add(new Network.Member(offsetPos, directions.getOpposite(), siding));
				} else if (offsetBlockState.getBlock() == initialBlockState.getBlock()) {
					posToTrace.addLast(offsetPos);
					
					instance.getNodes().add(new Network.Node(offsetPos));
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