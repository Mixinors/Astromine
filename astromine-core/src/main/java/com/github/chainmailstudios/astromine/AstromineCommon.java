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

package com.github.chainmailstudios.astromine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.util.Identifier;

import blue.endless.jankson.Jankson;
import com.github.chainmailstudios.astromine.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class AstromineCommon implements ModInitializer {
	public static final String LOG_ID = "Astromine";
	public static final String MOD_ID = "astromine";

	public static final Jankson JANKSON = Jankson.builder().build();
	public static final Gson GSON = new Gson();

	public static final Logger LOGGER = LogManager.getLogger(LOG_ID);

	public static Identifier identifier(String name) {
		if (name.indexOf(':') >= 0)
			return new Identifier(name);
		return new Identifier(MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		AstromineIdentifierFixes.initialize();
		AstromineDimensions.initialize();
		AstromineFeatures.initialize();
		AstromineItems.initialize();
		AstromineBlocks.initialize();
		AstromineScreenHandlers.initialize();
		AstromineEntityTypes.initialize();
		AstromineNetworkTypes.initialize();
		AstrominePotions.initialize();
		AstromineBiomeSources.initialize();
		AstromineBiomes.initialize();
		AstromineFluids.initialize();
		AstromineBreathables.initialize();
		AstromineChunkGenerators.initialize();
		AstromineCommonPackets.initialize();
		AstromineGravities.initialize();
		AstromineDimensionLayers.initialize();
		AstromineCommonCallbacks.initialize();
		AstromineRecipeSerializers.initialize();
		AstromineCommands.initialize();
		AstromineAtmospheres.initialize();
		AstromineBlockEntityTypes.initialize();
		AstromineSoundEvents.initialize();
		AstromineNetworkMembers.initialize();
		AstromineCriteria.initialize();
		AstromineFluidEffects.initialize();
		AstromineAttributes.initialize();

		if (FabricLoader.getInstance().isModLoaded("libblockattributes_fluids")) {
			try {
				Class.forName("com.github.chainmailstudios.astromine.common.lba.LibBlockAttributesCompatibility").getDeclaredMethod("initialize").invoke(null);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
