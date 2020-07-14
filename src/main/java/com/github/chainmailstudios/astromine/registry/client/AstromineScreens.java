package com.github.chainmailstudios.astromine.registry.client;

import com.github.chainmailstudios.astromine.client.screen.*;
import com.github.chainmailstudios.astromine.common.container.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

import net.minecraft.client.MinecraftClient;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;
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

			GAS_IMAGE = mainInterface.createChild(WStaticImage::new, Position.of(0, MinecraftClient.getInstance().getWindow().getScaledHeight() - 18, 0), Size.of(16, 16)).setTexture(AstromineCommon.identifier("textures/symbol/oxygen.png"));
			GAS_IMAGE.setHidden(true);
		});

		ScreenRegistry.register(AstromineContainers.FLUID_EXTRACTOR, (ScreenRegistry.Factory<FluidExtractorContainer, FluidExtractorContainerScreen>) ((handler, inventory, title) ->
			 new FluidExtractorContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.LIQUID_GENERATOR, (ScreenRegistry.Factory<LiquidGeneratorContainer, LiquidGeneratorContainerScreen>) ((handler, inventory, title) ->
			new LiquidGeneratorContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.SOLID_GENERATOR, (ScreenRegistry.Factory<SolidGeneratorContainer, SolidGeneratorContainerScreen>) ((handler, inventory, title) -> {
			return new SolidGeneratorContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.VENT, (ScreenRegistry.Factory<VentContainer, VentContainerScreen>) ((handler, inventory, title) -> {
			return new VentContainerScreen(title, handler, inventory.player);
		}));
		ScreenRegistry.register(AstromineContainers.TANK, (ScreenRegistry.Factory<FluidTankContainer, TankContainerScreen>) ((handler, inventory, title) ->
			new TankContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.NUCLEAR_WARHEAD, (ScreenRegistry.Factory<NuclearWarheadContainer, NuclearWarheadContainerScreen>) ((handler, inventory, title) ->
			new NuclearWarheadContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.CREATIVE_CAPACITOR, (ScreenRegistry.Factory<CreativeCapacitorContainer, CreativeCapacitorContainerScreen>) ((handler, inventory, title) ->
			new CreativeCapacitorContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.CREATIVE_TANK, (ScreenRegistry.Factory<CreativeTankContainer, CreativeTankContainerScreen>) ((handler, inventory, title) ->
			new CreativeTankContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.CREATIVE_BUFFER, (ScreenRegistry.Factory<CreativeBufferContainer, CreativeBufferContainerScreen>) ((handler, inventory, title) ->
			new CreativeBufferContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.SORTER, (ScreenRegistry.Factory<SorterContainer, SorterContainerScreen>) ((handler, inventory, title) ->
			new SorterContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.PRESSER, (ScreenRegistry.Factory<PresserContainer, PresserContainerScreen>) ((handler, inventory, title) ->
				new PresserContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.ELECTRIC_SMELTER, (ScreenRegistry.Factory<ElectricSmelterContainer, ElectricSmelterContainerScreen>) ((handler, inventory, title) ->
			new ElectricSmelterContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.ELECTROLYZER, (ScreenRegistry.Factory<ElectrolyzerContainer, ElectrolyzerContainerScreen>) ((handler, inventory, title) ->
			new ElectrolyzerContainerScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineContainers.FLUID_MIXER, (ScreenRegistry.Factory<FluidMixerContainer, FluidMixerContainerScreen>) ((handler, inventory, title) -> {
			return new FluidMixerContainerScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineContainers.CRAFTING_RECIPE_CREATOR, (ScreenRegistry.Factory<CraftingRecipeCreatorContainer, CraftingRecipeCreatorContainerScreen>) ((handler, inventory, title) -> {
			return new CraftingRecipeCreatorContainerScreen(title, handler, inventory.player);
		}));

	}
}
