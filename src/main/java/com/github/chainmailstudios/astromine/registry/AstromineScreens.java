package com.github.chainmailstudios.astromine.registry;

import spinnery.client.screen.InGameHudScreen;
import spinnery.widget.WInterface;
import spinnery.widget.WStaticText;
import spinnery.widget.api.Position;

public class AstromineScreens {
	public static WStaticText PRESSURE_TEXT;

	public static void initialize() {
		InGameHudScreen.addOnInitialize(() -> {
			WInterface mainInterface = InGameHudScreen.getInterface();

			PRESSURE_TEXT = mainInterface.createChild(WStaticText::new, Position.of(4, 4, 0)).setScale(0.75F);
		});
	}
}
