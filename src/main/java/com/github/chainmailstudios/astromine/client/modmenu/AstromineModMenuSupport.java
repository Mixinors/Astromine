package com.github.chainmailstudios.astromine.client.modmenu;

import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.gui.ConfigScreenProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AstromineModMenuSupport implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> {
			ConfigScreenProvider<AstromineConfig> configScreen = (ConfigScreenProvider<AstromineConfig>) AutoConfig.getConfigScreen(AstromineConfig.class, screen);
			configScreen.setOptionFunction((s, field) -> field.getName());
			return configScreen.get();
		};
	}
}
