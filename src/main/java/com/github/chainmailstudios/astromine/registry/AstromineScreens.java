package com.github.chainmailstudios.astromine.registry;

import spinnery.client.screen.InGameHudScreen;
import spinnery.widget.WInterface;
import spinnery.widget.WStaticImage;
import spinnery.widget.WStaticText;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class AstromineScreens {
	public static WStaticImage GAS_IMAGE;
	public static WStaticText PRESSURE_TEXT;
	public static WStaticText FRACTION_TEXT;

	public static void initialize() {
		InGameHudScreen.addOnInitialize(() -> {
			WInterface mainInterface = InGameHudScreen.getInterface();

			GAS_IMAGE = mainInterface.createChild(WStaticImage::new, Position.of(4, 4, 0), Size.of(32, 32));
			PRESSURE_TEXT = mainInterface.createChild(WStaticText::new, Position.of(4, 40, 0)).setScale(0.75F);
			FRACTION_TEXT = mainInterface.createChild(WStaticText::new, Position.of(4, 50, 0)).setScale(0.5F);
		});
	}
}
