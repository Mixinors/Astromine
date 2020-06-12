package com.github.chainmailstudios.astromine;

import blue.endless.jankson.Jankson;
import com.github.chainmailstudios.astromine.world.gen.AstromineBiomeSource;
import com.github.chainmailstudios.astromine.world.gen.AstromineChunkGenerator;
import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineCommon implements ModInitializer {
	public static final String LOG_ID = "Astromine";
	public static final String MOD_ID = "astromine";

	public static final Jankson JANKSON = Jankson.builder().build();
	public static final Gson GSON = new Gson();

	public static final Logger LOGGER = LogManager.getLogger(LOG_ID);

	@Override
	public void onInitialize() {
		AstromineBlocks.initialize();
		AstromineItems.initialize();
    
    		Registry.register(Registry.BIOME_SOURCE, id(MOD_ID), AstromineBiomeSource.CODEC);
		Registry.register(Registry.CHUNK_GENERATOR, id(MOD_ID), AstromineChunkGenerator.CODEC);
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}
