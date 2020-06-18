package com.github.chainmailstudios.astromine;

import com.github.chainmailstudios.astromine.common.noise.OpenSimplexNoise;
import com.github.chainmailstudios.astromine.registry.*;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import blue.endless.jankson.Jankson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AstromineCommon implements ModInitializer {
	public static final String LOG_ID = "Astromine";
	public static final String MOD_ID = "astromine";

	public static final Jankson JANKSON = Jankson.builder().build();
	public static final Gson GSON = new Gson();

	public static final Logger LOGGER = LogManager.getLogger(LOG_ID);

	private static final int WIDTH = 4096;
	private static final int HEIGHT = 4096;
	private static final double FEATURE_SIZE = 16;

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
		AstromineBreathables.initialize();
		AstromineChunkGenerators.initialize();
		AstromineServerPackets.initialize();
		AstromineGravities.initialize();
		AstromineDimensionLayers.initialize();
		AstromineCommonCallbacks.initialize();

		Random random = new Random();

			OpenSimplexNoise noise = new OpenSimplexNoise();
			BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			for (int y = 0; y < HEIGHT; y++)
			{
				for (int x = 0; x < WIDTH; x++)
				{
					double value = noise.eval(x, y, 0);

					if (value > 0.8) {
						int rgb = 0x010101 * (int)((random.nextDouble() + 1) * 127.5);
						image.setRGB(x, y, rgb);
					}

				}
			}

		try {
			ImageIO.write(image, "png", new File("C:/Users/vini2003/Documents/noise.png"));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
