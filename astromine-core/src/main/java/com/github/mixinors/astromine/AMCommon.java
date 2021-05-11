/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import com.github.mixinors.astromine.registry.common.*;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.Registry;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import net.minecraft.util.Lazy;
import net.minecraft.util.registry.RegistryKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class AMCommon implements ModInitializer {
	public static final String LOG_ID = "Astromine";
	public static final String MOD_ID = "astromine";
	
	public static final Gson GSON = new Gson();

	public static final Logger LOGGER = LogManager.getLogger(LOG_ID);
	
	public static final Lazy<Registries> REGISTRIES = new Lazy<>(() -> Registries.get(MOD_ID));

	public static Identifier id(String name) {
		if (name.indexOf(':') >= 0)
			return new Identifier(name);
		return new Identifier(MOD_ID, name);
	}
	
	public static <T> Registry<T> registry(RegistryKey<net.minecraft.util.registry.Registry<T>> key) {
		return REGISTRIES.get().get(key);
	}

	@Override
	public void onInitialize() {
		AMIdentifierFixes.init();
		AMDimensions.init();
		AMFeatures.init();
		AMItems.init();
		AMBlocks.init();
		AMScreenHandlers.init();
		AMEntityTypes.init();
		AMNetworkTypes.init();
		AMPotions.init();
		AMBiomeSources.init();
		AMBiomes.init();
		AMFluids.init();
		AMBreathables.init();
		AMChunkGenerators.init();
		AMGravities.init();
		AMDimensionLayers.init();
		AMCallbacks.init();
		AMRecipeSerializers.init();
		AMCommands.init();
		AMAtmospheres.init();
		AMBlockEntityTypes.init();
		AMSoundEvents.init();
		AMNetworkMembers.init();
		AMCriteria.init();
		AMAttributes.init();
		AMDecorators.init();
		AMToolMaterials.init();
		AMOres.init();
		AMItemGroups.init();
		AMNetworks.init();
	}
}