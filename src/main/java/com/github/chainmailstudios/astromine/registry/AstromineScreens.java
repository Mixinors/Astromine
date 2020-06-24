package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.client.screen.*;
import com.github.chainmailstudios.astromine.common.container.*;
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

		ScreenRegistry.register(AstromineContainers.LIQUID_GENERATOR, (ScreenRegistry.Factory<LiquidGeneratorContainer, LiquidGeneratorContainerScreen>) ((handler, inventory, title) -> {
			return new LiquidGeneratorContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.VENT, (ScreenRegistry.Factory<VentContainer, VentContainerScreen>) ((handler, inventory, title) -> {
			return new VentContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.TANK, (ScreenRegistry.Factory<FluidTankContainer, TankContainerScreen>) ((handler, inventory, title) -> {
			return new TankContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.NUCLEAR_WARHEAD, (ScreenRegistry.Factory<NuclearWarheadContainer, NuclearWarheadContainerScreen>) ((handler, inventory, title) -> {
			return new NuclearWarheadContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.CREATIVE_CAPACITOR, (ScreenRegistry.Factory<CreativeCapacitorContainer, CreativeCapacitorContainerScreen>) ((handler, inventory, title) -> {
			return new CreativeCapacitorContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.CREATIVE_TANK, (ScreenRegistry.Factory<CreativeTankContainer, CreativeTankContainerScreen>) ((handler, inventory, title) -> {
			return new CreativeTankContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.CREATIVE_BUFFER, (ScreenRegistry.Factory<CreativeBufferContainer, CreativeBufferContainerScreen>) ((handler, inventory, title) -> {
			return new CreativeBufferContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.SORTER, (ScreenRegistry.Factory<SorterContainer, SorterContainerScreen>) ((handler, inventory, title) -> {
			return new SorterContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.SMELTER, (ScreenRegistry.Factory<SmelterContainer, SmelterContainerScreen>) ((handler, inventory, title) -> {
			return new SmelterContainerScreen(title, handler, inventory.player);
		}));
	}
}
