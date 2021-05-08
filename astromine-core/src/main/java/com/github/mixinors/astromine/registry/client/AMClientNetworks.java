package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.client.atmosphere.ClientAtmosphereManager;
import com.github.mixinors.astromine.common.entity.PrimitiveRocketEntity;
import com.github.mixinors.astromine.common.util.ClientUtils;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.UUID;

import static com.github.mixinors.astromine.registry.common.AMNetworks.*;

public class AMClientNetworks {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(PRIMITIVE_ROCKET_SPAWN, ((client, handler, buf, responseSender) -> {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			UUID uuid = buf.readUuid();
			int id = buf.readInt();
			
			client.execute(() -> {
				PrimitiveRocketEntity rocketEntity = AMEntityTypes.PRIMITIVE_ROCKET.create(ClientUtils.getWorld());
				
				rocketEntity.setUuid(uuid);
				rocketEntity.setEntityId(id);
				rocketEntity.updatePosition(x, y, z);
				rocketEntity.updateTrackedPosition(x, y, z);
				
				ClientUtils.getWorld().addEntity(id, rocketEntity);
			});
		}));
		
		ClientPlayNetworking.registerGlobalReceiver(GAS_ERASED, ((client, handler, buf, responseSender) -> {
			buf.retain();
			
			client.execute(() -> {
				ClientAtmosphereManager.onGasErased(buf);
			});
		}));
		
		ClientPlayNetworking.registerGlobalReceiver(GAS_ADDED, ((client, handler, buf, responseSender) -> {
			buf.retain();
			
			client.execute(() -> {
				ClientAtmosphereManager.onGasAdded(buf);
			});
		}));
		
		ClientPlayNetworking.registerGlobalReceiver(GAS_REMOVED, ((client, handler, buf, responseSender) -> {
			buf.retain();
			
			client.execute(() -> {
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
