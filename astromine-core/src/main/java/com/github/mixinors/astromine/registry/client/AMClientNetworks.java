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

package com.github.mixinors.astromine.registry.client;

import static com.github.mixinors.astromine.registry.common.AMNetworks.PRIMITIVE_ROCKET_SPAWN;

import com.github.mixinors.astromine.common.util.ClientUtils;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import dev.architectury.networking.NetworkManager;

public class AMClientNetworks {
	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.s2c(), PRIMITIVE_ROCKET_SPAWN, (( buf, context) -> {
			var x = buf.readDouble();
			var y = buf.readDouble();
			var z = buf.readDouble();
			var uuid = buf.readUuid();
			var id = buf.readInt();

			context.queue(() -> {
				var rocketEntity = AMEntityTypes.PRIMITIVE_ROCKET.get().create(ClientUtils.getWorld());
				
				rocketEntity.setUuid(uuid);
				rocketEntity.setId(id);
				rocketEntity.setPosition(x, y, z);
				rocketEntity.updateTrackedPosition(x, y, z);
				
				ClientUtils.getWorld().addEntity(id, rocketEntity);
			});
		}));
		
		// TODO: 08/08/2020 - 11:00:51
		// TODO: 27/08/2020 - 21:15:05
		// TODO: 08/05/2021 - 09:47:18
		// ClientSidePacketRegistry.INSTANCE.register(AstromineCommonPackets.PRESSURE_UPDATE, ((context, buffer) -> {
		// Identifier identifier = buffer.readIdentifier();
		//
		// AstromineScreens.GAS_IMAGE.setTexture(AstromineCommon.identifier("textures/symbol/" + identifier.getPath() + ".png"));
		// }));
	}
}
