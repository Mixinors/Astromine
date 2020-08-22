/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.registry.client;

import com.github.chainmailstudios.astromine.client.cca.ClientAtmosphereManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import net.minecraft.client.MinecraftClient;

import com.github.chainmailstudios.astromine.common.entity.RocketEntity;
import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class AstromineClientPackets {
	@Environment(EnvType.CLIENT)
	public static void initialize() {
		// TODO: 08/08/2020 - 11:00:51
		// ClientSidePacketRegistry.INSTANCE.register(AstromineCommonPackets.PRESSURE_UPDATE, ((context, buffer) -> {
		// Identifier identifier = buffer.readIdentifier();
		//
		// AstromineScreens.GAS_IMAGE.setTexture(AstromineCommon.identifier("textures/symbol/" + identifier.getPath() + ".png"));
		// }));

		ClientSidePacketRegistry.INSTANCE.register(RocketEntity.ROCKET_SPAWN, (context, buffer) -> {
			double x = buffer.readDouble();
			double y = buffer.readDouble();
			double z = buffer.readDouble();
			UUID uuid = buffer.readUuid();
			int id = buffer.readInt();

			context.getTaskQueue().execute(() -> {
				RocketEntity rocketEntity = AstromineEntityTypes.ROCKET.create(MinecraftClient.getInstance().world);

				rocketEntity.setUuid(uuid);
				rocketEntity.setEntityId(id);
				rocketEntity.updatePosition(x, y, z);
				rocketEntity.updateTrackedPosition(x, y, z);

				MinecraftClient.getInstance().world.addEntity(id, rocketEntity);
			});
		});

		ClientSidePacketRegistry.INSTANCE.register(ClientAtmosphereManager.GAS_ERASED, (context, buffer) -> {
			buffer.retain();

			context.getTaskQueue().execute(() -> {
				ClientAtmosphereManager.onGasErased(buffer);
			});
		});

		ClientSidePacketRegistry.INSTANCE.register(ClientAtmosphereManager.GAS_ADDED, (context, buffer) -> {
			buffer.retain();

			context.getTaskQueue().execute(() -> {
				ClientAtmosphereManager.onGasAdded(buffer);
			});
		});

		ClientSidePacketRegistry.INSTANCE.register(ClientAtmosphereManager.GAS_REMOVED, (context, buffer) -> {
			buffer.retain();

			context.getTaskQueue().execute(() -> {
				ClientAtmosphereManager.onGasRemoved(buffer);
			});
		});
	}
}
