package com.github.chainmailstudios.astromine.registry;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "astromine/config")
public class AstromineConfig implements ConfigData {
	public static final AstromineConfig DEFAULT = new AstromineConfig();

	@Comment("Whether nuclear warheads are enabled.")
	public boolean nuclearWarheadEnabled = true;

	@Comment("Y level in the Overworld to get to Space.")
	public int spaceTravelYLevel = 1024;

	@Comment("Y level to spawn at when travelling to Space.")
	public int spaceSpawnYLevel = 32;

	@Comment("Y level in Space to get back to the Overworld.")
	public int overworldTravelYLevel = -58;

	@Comment("Y level to spawn at when returning to the Overworld.")
	public int overworldSpawnYLevel = 992;

	@Comment("Gravity level in Space")
	public double spaceGravity = 0.01d;

	@Comment("Gravity level in Moon")
	public double moonGravity = 0.03d;

	@Comment("Gravity level in Mars")
	public double marsGravity = 0.045d;

	@Comment("Whether generation of Copper Ore in the Overworld is enabled.")
	public boolean overworldCopperOre = true;

	@Comment("Whether generation of Tin Ore in the Overworld is enabled.")
	public boolean overworldTinOre = true;

	@Comment("Whether the mod will attempt to update old item and block names to new ones. Probably faster when false, but old worlds may break.")
	public boolean compatibilityMode = true;

	public static AstromineConfig get() {
		try {
			return AutoConfig.getConfigHolder(AstromineConfig.class).getConfig();
		} catch(RuntimeException e) {
			return DEFAULT;
		}
	}

	public static void initialize() {
		AutoConfig.register(AstromineConfig.class, JanksonConfigSerializer::new);
	}
}
