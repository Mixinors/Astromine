package com.github.chainmailstudios.astromine;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import blue.endless.jankson.Jankson;
import com.github.chainmailstudios.astromine.registry.AstromineBiomes;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineChunkGenerators;
import com.github.chainmailstudios.astromine.registry.AstromineCommonCallbacks;
import com.github.chainmailstudios.astromine.registry.AstromineDimensionLayers;
import com.github.chainmailstudios.astromine.registry.AstromineEntities;
import com.github.chainmailstudios.astromine.registry.AstromineFeatures;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.github.chainmailstudios.astromine.registry.AstromineGravities;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineOres;
import com.github.chainmailstudios.astromine.registry.AstrominePotions;
import com.github.chainmailstudios.astromine.registry.AstromineServerPackets;
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
		AstromineItems.initialize();
		AstromineBlocks.initialize();
		AstromineOres.initialize();
		AstromineEntities.initialize();
		AstrominePotions.initialize();
		AstromineFeatures.initialize();
		AstromineBiomes.initialize();
		AstromineFluids.initialize();
		AstromineChunkGenerators.initialize();
		AstromineServerPackets.initialize();
		AstromineGravities.initialize();
		AstromineDimensionLayers.initialize();
		AstromineCommonCallbacks.initialize();
	}
}
