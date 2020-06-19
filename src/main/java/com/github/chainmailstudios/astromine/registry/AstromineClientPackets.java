package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.entity.RocketEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;


import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;

import net.minecraft.client.MinecraftClient;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class AstromineClientPackets {
	public static final Identifier PRESSURE_UPDATE = AstromineCommon.identifier("pressure_update");

	public static void initialize() {
		ClientSidePacketRegistry.INSTANCE.register(PRESSURE_UPDATE, ((context, buffer) -> {
			Identifier identifier = new Identifier(buffer.readString());
			String fraction = buffer.readString();

			AstromineScreens.PRESSURE_TEXT.setText(I18n.translate("gas." + identifier.getNamespace() + "." + identifier.getPath()));
			AstromineScreens.FRACTION_TEXT.setText(fraction);
			AstromineScreens.GAS_IMAGE.setTexture(AstromineCommon.identifier("textures/symbol/" + identifier.getPath() + ".png"));
		}));

		ClientSidePacketRegistry.INSTANCE.register(RocketEntity.ROCKET_SPAWN, (context, buffer) -> {
			double x = buffer.readDouble();
			double y = buffer.readDouble();
			double z = buffer.readDouble();
			UUID uuid = buffer.readUuid();
			int id = buffer.readInt();

			context.getTaskQueue().execute(() -> {
				RocketEntity rocketEntity = AstromineEntities.ROCKET.create(MinecraftClient.getInstance().world);

				rocketEntity.setUuid(uuid);
				rocketEntity.setEntityId(id);
				rocketEntity.updatePosition(x, y, z);
				rocketEntity.updateTrackedPosition(x, y, z);

				MinecraftClient.getInstance().world.addEntity(id, rocketEntity);
			});
		});
	}
}
