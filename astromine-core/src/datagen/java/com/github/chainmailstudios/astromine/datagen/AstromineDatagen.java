package com.github.chainmailstudios.astromine.datagen;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.entrypoint.DatagenInitializer;

public class AstromineDatagen implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		try {
			FabricLoader.getInstance().getEntrypoints("main", ModInitializer.class).forEach(ModInitializer::onInitialize);
			FabricLoader.getInstance().getEntrypoints("astromine_datagen", DatagenInitializer.class).forEach(DatagenInitializer::registerData);
		} catch (Throwable throwable) {
			AstromineCommon.LOGGER.error("Data generation failed with stack trace:");
			throwable.printStackTrace();

			System.exit(0);
		}
		System.exit(0);
	}
}
