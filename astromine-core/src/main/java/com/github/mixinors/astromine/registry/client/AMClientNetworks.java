package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.client.atmosphere.ClientAtmosphereManager;
import com.github.mixinors.astromine.common.entity.PrimitiveRocketEntity;
import com.github.mixinors.astromine.common.util.ClientUtils;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import me.shedaniel.architectury.networking.NetworkManager;

import java.util.UUID;

import static com.github.mixinors.astromine.registry.common.AMNetworks.*;

public class AMClientNetworks {
	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.s2c(), PRIMITIVE_ROCKET_SPAWN, ((buf, context) -> {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			UUID uuid = buf.readUuid();
			int id = buf.readInt();

			context.queue(() -> {
				PrimitiveRocketEntity rocketEntity = AMEntityTypes.PRIMITIVE_ROCKET.create(ClientUtils.getWorld());
				
				rocketEntity.setUuid(uuid);
				rocketEntity.setEntityId(id);
				rocketEntity.updatePosition(x, y, z);
				rocketEntity.updateTrackedPosition(x, y, z);
				
				ClientUtils.getWorld().addEntity(id, rocketEntity);
			});
		}));

		NetworkManager.registerReceiver(NetworkManager.s2c(), GAS_ERASED, ((buf, context) -> {
			buf.retain();
			
			context.queue(() -> {
				ClientAtmosphereManager.onGasErased(buf);
			});
		}));

		NetworkManager.registerReceiver(NetworkManager.s2c(), GAS_ADDED, ((buf, context) -> {
			buf.retain();

			context.queue(() -> {
				ClientAtmosphereManager.onGasAdded(buf);
			});
		}));

		NetworkManager.registerReceiver(NetworkManager.s2c(), GAS_REMOVED, ((buf, context) -> {
			buf.retain();

			context.queue(() -> {
				ClientAtmosphereManager.onGasRemoved(buf);
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
