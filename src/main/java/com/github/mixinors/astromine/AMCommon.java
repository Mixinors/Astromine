/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.registry.common.*;
import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AMCommon.MOD_ID)
public class AMCommon {
	public static final String LOG_ID = "Astromine";
	public static final String MOD_ID = "astromine";
	
	public static final Gson GSON = new Gson();
	
	public static final Logger LOGGER = LogManager.getLogger(LOG_ID);
	
	private static IEventBus modEventBus;
	
	public static ResourceLocation id(String name) {
		if (name.indexOf(':') >= 0) {
			return ResourceLocation.parse(name);
		}
		
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
	
	public static IEventBus modEventBus() {
		return modEventBus;
	}
	
	public AMCommon(IEventBus modBus, ModContainer container) {
		modEventBus = modBus;
		init();
	}
	
	private static void init() {
		AMConfig.init();
		AMComponents.init();
		AMAttributes.init();
		AMParticles.init();
		AMWorlds.init();
		AMFeatures.init();
		AMStructures.init();
		AMArmorMaterials.init();
		AMItems.init();
		AMBlocks.init();
		AMScreenHandlers.init();
		AMEntityTypes.init();
		AMNetworkTypes.init();
		AMBiomeSources.init();
		AMBiomes.init();
		AMFluids.init();
		AMChunkGenerators.init();
		AMEvents.init();
		AMRecipeSerializers.init();
		AMCommands.init();
		AMBlockEntityTypes.init();
		AMSoundEvents.init();
		AMCriteria.init();
		AMToolMaterials.init();
		AMOres.init();
		AMItemGroups.init();
		AMNetworking.init();
		AMLookups.init();
		AMBodies.init();
		AMDatagen.init();
	}
}
