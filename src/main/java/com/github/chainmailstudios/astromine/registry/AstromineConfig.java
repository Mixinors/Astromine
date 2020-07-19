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

	@Comment("Y level in the Overworld to get to Space.")
	public int spaceTravelYLevel = 1024;

	@Comment("Y level to spawn at when travelling to Space.")
	public int spaceSpawnYLevel = 32;

	@Comment("Y level in Space to get back to the Overworld.")
	public int overworldTravelYLevel = -58;

	@Comment("Y level to spawn at when returning to the Overworld")
	public int overworldSpawnYLevel = 992;

	@Comment("Gravity level in Space")
	public double spaceGravity = 0.01d;

	@Comment("Whether generation of Copper Ore in the Overworld is enabled")
	public boolean overworldCopperOre = true;

	@Comment("Whether generation of Tin Ore in the Overworld is enabled")
	public boolean overworldTinOre = true;

	public static AstromineConfig get() {
		return AutoConfig.getConfigHolder(AstromineConfig.class).getConfig();
	}

	public static void initialize() {
		AutoConfig.register(AstromineConfig.class, JanksonConfigSerializer::new);
	}
}
