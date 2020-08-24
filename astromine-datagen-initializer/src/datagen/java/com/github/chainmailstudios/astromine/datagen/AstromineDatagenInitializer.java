package com.github.chainmailstudios.astromine.datagen;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.entrypoint.DatagenInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class AstromineDatagenInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		try {
			FabricLoader.getInstance().getEntrypoints("main", ModInitializer.class).forEach(ModInitializer::onInitialize);
			FabricLoader.getInstance().getEntrypoints("astromine_datagen", DatagenInitializer.class).forEach(DatagenInitializer::registerData);
		} catch (Throwable throwable) {
			AstromineCommon.LOGGER.error("Data generation failed.", throwable);
			System.exit(1);
		}
		System.exit(0);
	}
}
