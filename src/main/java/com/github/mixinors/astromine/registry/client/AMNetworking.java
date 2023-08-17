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

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.common.entity.base.ExtendedEntity;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.manager.StationManager;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.world.ClientWorld;

import static com.github.mixinors.astromine.registry.common.AMNetworking.*;

public class AMNetworking {
	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.s2c(), ROCKET_SPAWN, (buf, context) -> {
			var x = buf.readDouble();
			var y = buf.readDouble();
			var z = buf.readDouble();
			
			var uuid = buf.readUuid();
			
			var id = buf.readInt();
			
			context.queue(() -> {
				var player = context.getPlayer();
				var world = (ClientWorld) player.getWorld();
				
				var rocketEntity = AMEntityTypes.ROCKET.get().create(world);
				
				rocketEntity.setUuid(uuid);
				rocketEntity.setId(id);
				rocketEntity.setPosition(x, y, z);
				rocketEntity.updateTrackedPosition(x, y, z);
				
				world.addEntity(id, rocketEntity);
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.s2c(), SYNC_ENTITY, (buf, context) -> {
			var id = buf.readInt();
			
			var nbt = buf.readNbt();
			
			context.queue(() -> {
				var player = context.getPlayer();
				
				if (player != null) {
					var world = (ClientWorld) player.getWorld();
					
					if (world.getEntityById(id) instanceof ExtendedEntity entity) {
						entity.readFromNbt(nbt);
					}
				}
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.s2c(), SYNC_BODIES, BodyManager::onSync);
		NetworkManager.registerReceiver(NetworkManager.s2c(), SYNC_ROCKETS, RocketManager::onSync);
		NetworkManager.registerReceiver(NetworkManager.s2c(), SYNC_STATIONS, StationManager::onSync);
		
		// TODO: 08/08/2020 - 11:00:51
		// TODO: 27/08/2020 - 21:15:05
		// TODO: 08/05/2021 - 09:47:18
		// TODO: 06/05/2022 - 20:06:53
		// TODO: 02/07/2022 - 01:23:00
		// TODO: 18/09/2022 - 15:03:00
		// TODO: 15/06/2023 - 21:12:00
		// ClientSidePacketRegistry.INSTANCE.register(AstromineCommonPackets.PRESSURE_UPDATE, ((context, buffer) -> {
		// Identifier identifier = buffer.readIdentifier();
		//
		// AstromineScreens.GAS_IMAGE.setTexture(AstromineCommon.identifier("textures/symbol/" + identifier.getPath() + ".png"));
		// }));
	}
}
