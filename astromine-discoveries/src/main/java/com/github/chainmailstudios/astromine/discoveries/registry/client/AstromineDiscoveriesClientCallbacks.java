package com.github.chainmailstudios.astromine.discoveries.registry.client;

import com.github.chainmailstudios.astromine.common.callback.SkyPropertiesCallback;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.MarsSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.MoonSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.SpaceSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.VulcanSkyProperties;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesDimensions;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientCallbacks;

public class AstromineDiscoveriesClientCallbacks extends AstromineClientCallbacks {
	public static void initialize() {
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.EARTH_SPACE_ID, new SpaceSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.MOON_ID, new MoonSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.MARS_ID, new MarsSkyProperties()));
		SkyPropertiesCallback.EVENT.register((properties) -> properties.put(AstromineDiscoveriesDimensions.VULCAN_ID, new VulcanSkyProperties()));
	}
}
