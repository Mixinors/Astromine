package com.github.chainmailstudios.astromine.registry;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "astromine/config")
public class AstromineConfig implements ConfigData {
	@Comment("Whether nuclear warheads are enabled.")
	public boolean nuclearWarheadEnabled = true;

	public static AstromineConfig get() {
		return AutoConfig.getConfigHolder(AstromineConfig.class).getConfig();
	}

	public static void initialize() {
		AutoConfig.register(AstromineConfig.class, JanksonConfigSerializer::new);
	}
}
