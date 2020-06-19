package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;

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
	}
}
