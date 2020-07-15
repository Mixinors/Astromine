package com.github.chainmailstudios.astromine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.util.Identifier;

import blue.endless.jankson.Jankson;
import com.github.chainmailstudios.astromine.registry.AstromineAtmospheres;
import com.github.chainmailstudios.astromine.registry.AstromineBiomes;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineBreathables;
import com.github.chainmailstudios.astromine.registry.AstromineChunkGenerators;
import com.github.chainmailstudios.astromine.registry.AstromineCommands;
import com.github.chainmailstudios.astromine.registry.AstromineCommonCallbacks;
import com.github.chainmailstudios.astromine.registry.AstromineCommonPackets;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;
import com.github.chainmailstudios.astromine.registry.AstromineDimensionLayers;
import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineFeatures;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.github.chainmailstudios.astromine.registry.AstromineGravities;
import com.github.chainmailstudios.astromine.registry.AstromineIdentifierFixes;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import com.github.chainmailstudios.astromine.registry.AstromineOres;
import com.github.chainmailstudios.astromine.registry.AstrominePotions;
import com.github.chainmailstudios.astromine.registry.AstromineRecipeSerializers;
import com.github.chainmailstudios.astromine.registry.AstromineWorlds;
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
		return new Identifier(MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		AstromineIdentifierFixes.initialize();
		AstromineConfig.initialize();
		AstromineItems.initialize();
		AstromineBlocks.initialize();
		AstromineOres.initialize();
		AstromineContainers.initialize();
		AstromineEntityTypes.initialize();
		AstromineComponentTypes.initialize();
		AstromineNetworkTypes.initialize();
		AstrominePotions.initialize();
		AstromineFeatures.initialize();
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
		AstromineWorlds.initialize();
		AstromineAtmospheres.initialize();
		AstromineBlockEntityTypes.initialize();

		if (FabricLoader.getInstance().isModLoaded("libblockattributes_fluids")) {
			try {
				Class.forName("com.github.chainmailstudios.astromine.common.lba.LibBlockAttributesCompatibility").getDeclaredMethod("initialize").invoke(null);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
