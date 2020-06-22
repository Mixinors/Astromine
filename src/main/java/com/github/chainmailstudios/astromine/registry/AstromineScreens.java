package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.client.screen.FluidTankContainerScreen;
import com.github.chainmailstudios.astromine.client.screen.FuelGeneratorContainerScreen;
import com.github.chainmailstudios.astromine.client.screen.NuclearWarheadContainerScreen;
import com.github.chainmailstudios.astromine.client.screen.VentContainerScreen;
import com.github.chainmailstudios.astromine.common.container.FluidTankContainer;
import com.github.chainmailstudios.astromine.common.container.FuelGeneratorContainer;
import com.github.chainmailstudios.astromine.common.container.NuclearWarheadContainer;
import com.github.chainmailstudios.astromine.common.container.VentContainer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
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

		ScreenRegistry.register(AstromineContainers.FUEL_GENERATOR, (ScreenRegistry.Factory<FuelGeneratorContainer, FuelGeneratorContainerScreen>) ((handler, inventory, title) -> {
			return new FuelGeneratorContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.VENT, (ScreenRegistry.Factory<VentContainer, VentContainerScreen>) ((handler, inventory, title) -> {
			return new VentContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.FLUID_TANK, (ScreenRegistry.Factory<FluidTankContainer, FluidTankContainerScreen>) ((handler, inventory, title) -> {
			return new FluidTankContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.NUCLEAR_WARHEAD, (ScreenRegistry.Factory<NuclearWarheadContainer, NuclearWarheadContainerScreen>) ((handler, inventory, title) -> {
			return new NuclearWarheadContainerScreen(title, handler, inventory.player);
		}));
	}
}
