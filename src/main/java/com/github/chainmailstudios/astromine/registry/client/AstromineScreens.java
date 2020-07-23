/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.chainmailstudios.astromine.registry.client;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.screen.*;
import com.github.chainmailstudios.astromine.common.screenhandler.*;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.MinecraftClient;
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

		ScreenRegistry.register(AstromineScreenHandlers.FLUID_EXTRACTOR, (ScreenRegistry.Factory<FluidExtractorScreenHandler, FluidExtractorHandledScreen>) ((handler, inventory, title) ->
				new FluidExtractorHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.FLUID_INSERTER, (ScreenRegistry.Factory<FluidInserterScreenHandler, FluidInserterHandledScreen>) ((handler, inventory, title) ->
				new FluidInserterHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.BLOCK_BREAKER, (ScreenRegistry.Factory<BlockBreakerScreenHandler, BlockBreakerHandledScreen>) ((handler, inventory, title) ->
				new BlockBreakerHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.BLOCK_PLACER, (ScreenRegistry.Factory<BlockPlacerScreenHandler, BlockPlacerHandledScreen>) ((handler, inventory, title) ->
				new BlockPlacerHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.LIQUID_GENERATOR, (ScreenRegistry.Factory<LiquidGeneratorScreenHandler, LiquidGeneratorHandledScreen>) ((handler, inventory, title) ->
				new LiquidGeneratorHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.SOLID_GENERATOR, (ScreenRegistry.Factory<SolidGeneratorScreenHandler, SolidGeneratorHandledScreen>) ((handler, inventory, title) -> {
			return new SolidGeneratorHandledScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineScreenHandlers.VENT, (ScreenRegistry.Factory<VentScreenHandler, VentHandledScreen>) ((handler, inventory, title) -> {
			return new VentHandledScreen(title, handler, inventory.player);
		}));
		ScreenRegistry.register(AstromineScreenHandlers.TANK, (ScreenRegistry.Factory<FluidTankScreenHandler, TankHandledScreen>) ((handler, inventory, title) ->
				new TankHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.NUCLEAR_WARHEAD, (ScreenRegistry.Factory<NuclearWarheadScreenHandler, NuclearWarheadHandledScreen>) ((handler, inventory, title) ->
				new NuclearWarheadHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.CREATIVE_CAPACITOR, (ScreenRegistry.Factory<CreativeCapacitorScreenHandler, CreativeCapacitorHandledScreen>) ((handler, inventory, title) ->
				new CreativeCapacitorHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.CREATIVE_TANK, (ScreenRegistry.Factory<CreativeTankScreenHandler, CreativeTankHandledScreen>) ((handler, inventory, title) ->
				new CreativeTankHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.CREATIVE_BUFFER, (ScreenRegistry.Factory<CreativeBufferScreenHandler, CreativeBufferHandledScreen>) ((handler, inventory, title) ->
				new CreativeBufferHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.BUFFER, (ScreenRegistry.Factory<BufferScreenHandler, BufferHandledScreen>) ((handler, inventory, title) ->
				new BufferHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.TRITURATOR, (ScreenRegistry.Factory<TrituratorScreenHandler, TrituratorHandledScreen>) ((handler, inventory, title) ->
				new TrituratorHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.PRESSER, (ScreenRegistry.Factory<PresserScreenHandler, PresserHandledScreen>) ((handler, inventory, title) ->
				new PresserHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.ELECTRIC_SMELTER, (ScreenRegistry.Factory<ElectricSmelterScreenHandler, ElectricSmelterHandledScreen>) ((handler, inventory, title) ->
				new ElectricSmelterHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.ELECTROLYZER, (ScreenRegistry.Factory<ElectrolyzerScreenHandler, ElectrolyzerHandledScreen>) ((handler, inventory, title) ->
				new ElectrolyzerHandledScreen(title, handler, inventory.player)
		));

		ScreenRegistry.register(AstromineScreenHandlers.FLUID_MIXER, (ScreenRegistry.Factory<FluidMixerScreenHandler, FluidMixerHandledScreen>) ((handler, inventory, title) -> {
			return new FluidMixerHandledScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineScreenHandlers.CRAFTING_RECIPE_CREATOR, (ScreenRegistry.Factory<CraftingRecipeCreatorScreenHandler, CraftingRecipeCreatorHandledScreen>) ((handler, inventory, title) -> {
			return new CraftingRecipeCreatorHandledScreen(title, handler, inventory.player);
		}));

		ScreenRegistry.register(AstromineScreenHandlers.ALLOY_SMELTER, (ScreenRegistry.Factory<AlloySmelterScreenHandler, AlloySmelterHandledScreen>) ((handler, inventory, title) -> {
			return new AlloySmelterHandledScreen(title, handler, inventory.player);
		}));
	}
}
