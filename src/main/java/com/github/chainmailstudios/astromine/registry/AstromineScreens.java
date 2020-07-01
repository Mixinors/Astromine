package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.client.screen.*;
import com.github.chainmailstudios.astromine.common.container.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import spinnery.client.screen.InGameHudScreen;
import spinnery.widget.WInterface;
import spinnery.widget.WStaticImage;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

@Environment(EnvType.CLIENT)
public class AstromineScreens {
	public static WStaticImage GAS_IMAGE;

	public static void initialize() {
		InGameHudScreen.addOnInitialize(() -> {
			WInterface mainInterface = InGameHudScreen.getInterface();

			GAS_IMAGE = mainInterface.createChild(WStaticImage::new, Position.of(0, 4, 0), Size.of(16, 16)).setTexture(AstromineCommon.identifier("textures/symbol/oxygen.png"));
		});

		ScreenRegistry.register(AstromineContainers.FLUID_EXTRACTOR, (ScreenRegistry.Factory<FluidExtractorContainer, FluidExtractorContainerScreen>) ((handler, inventory, title) -> {
			return new FluidExtractorContainerScreen(title, handler, inventory.player);
		}));

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

		ScreenRegistry.register(AstromineContainers.ELECTRIC_SMELTER, (ScreenRegistry.Factory<ElectricSmelterContainer, SmelterContainerScreen>) ((handler, inventory, title) -> {
			return new SmelterContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.ELECTROLYZER, (ScreenRegistry.Factory<ElectrolyzerContainer, ElectrolyzerContainerScreen>) ((handler, inventory, title) -> {
			return new ElectrolyzerContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.fluid_mixer, (ScreenRegistry.Factory<FuelMixerContainer, FuelMixerContainerScreen>) ((handler, inventory, title) -> {
			return new FuelMixerContainerScreen(title, handler, inventory.player);
		}));
		ScreenRegistry.register(AstromineContainers.CRAFTING_RECIPE_CREATOR, (ScreenRegistry.Factory<CraftingRecipeCreatorContainer, CraftingRecipeCreatorContainerScreen>) ((handler, inventory, title) -> {
			return new CraftingRecipeCreatorContainerScreen(title, handler, inventory.player);
		}));

	}
}
