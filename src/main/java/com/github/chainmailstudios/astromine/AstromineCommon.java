package com.github.chainmailstudios.astromine;

import blue.endless.jankson.Jankson;
import com.github.chainmailstudios.astromine.registry.*;
import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AstromineCommon implements ModInitializer {
    public static final String LOG_ID = "Astromine";
    public static final String MOD_ID = "astromine";

    public static final Jankson JANKSON = Jankson.builder().build();
    public static final Gson GSON = new Gson();

    public static final Logger LOGGER = LogManager.getLogger(LOG_ID);

    @Override
    public void onInitialize() {
        AstromineItems.initialize();
        AstromineBlocks.initialize();
        AstromineOres.initialize();
        AstromineEntities.initialize();
        AstrominePotions.initialize();
        AstromineFeatures.initialize();
        AstromineBiomes.initialize();
        AstromineChunkGenerators.initialize();
        AstromineServerPackets.initialize();
        AstromineGravities.initialize();
    }

    public static Identifier identifier(String name) {
        return new Identifier(MOD_ID, name);
    }
}
