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

	@Comment("Whether generation of Copper Ore in the Overworld is enabled.")
	public boolean overworldCopperOre = true;

	@Comment("Bottom offset of Overworld Copper ore.")
	public int overworldCopperOreBottomOffset = 0;

	@Comment("Top offset of Overworld Copper ore.")
	public int overworldCopperOreTopOffset = 0;

	@Comment("Maximum layer of Overworld Copper ore.")
	public int overworldCopperOreMaximumLayer = 64;

	@Comment("Maximum count of Overworld Copper ore veins per chunk.")
	public int overworldCopperOreMaximumVeins = 20;

	@Comment("Maximum count of Overworld Copper ore blocks per vein.")
	public int overworldCopperOreMaximumBlocks = 14;

	@Comment("Whether generation of Tin Ore in the Overworld is enabled.")
	public boolean overworldTinOre = true;

	@Comment("Bottom offset of Overworld Tin Ore.")
	public int overworldTinOreBottomOffset = 0;

	@Comment("Top offset of Overworld Tin Ore.")
	public int overworldTinOreTopOffset = 0;

	@Comment("Maximum layer of Overworld Tin Ore.")
	public int overworldTinOreMaximumLayer = 64;

	@Comment("Maximum count of Overworld Tin Ore veins per chunk.")
	public int overworldTinOreMaximumVeins = 20;

	@Comment("Maximum count of Overworld Tin Ore blocks per vein.")
	public int overworldTinOreMaximumBlocks = 11;
	
	@Comment("Minimum range of Asteroid Coal Ore weight.")
	public int asteroidCoalOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Coal Ore weight.")
	public int asteroidCoalOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Coal Ore veins.")
	public int asteroidCoalOreMinimumSize = 8;
	
	@Comment("Maximum Size of Asteroid Coal Ore veins.")
	public int asteroidCoalOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Iron Ore weight.")
	public int asteroidIronOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Iron Ore weight.")
	public int asteroidIronOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Iron Ore veins.")
	public int asteroidIronOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Iron Ore veins.")
	public int asteroidIronOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Gold Ore weight.")
	public int asteroidGoldOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Gold Ore weight.")
	public int asteroidGoldOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Gold Ore veins.")
	public int asteroidGoldOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Gold Ore veins.")
	public int asteroidGoldOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Copper Ore weight.")
	public int asteroidCopperOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Copper Ore weight.")
	public int asteroidCopperOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Copper Ore veins.")
	public int asteroidCopperOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Copper Ore veins.")
	public int asteroidCopperOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Tin Ore weight.")
	public int asteroidTinOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Tin Ore weight.")
	public int asteroidTinOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Tin Ore veins.")
	public int asteroidTinOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Tin Ore veins.")
	public int asteroidTinOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Redstone Ore weight.")
	public int asteroidRedstoneOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Redstone Ore weight.")
	public int asteroidRedstoneOreMaximumRange = 32;

	@Comment("Minimum size of Asteroid Redstone Ore veins.")
	public int asteroidRedstoneOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Redstone Ore veins.")
	public int asteroidRedstoneOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Lapis Ore weight.")
	public int asteroidLapisOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Lapis Ore weight.")
	public int asteroidLapisOreMaximumRange = 32;

	@Comment("Minimum size of Asteroid Lapis Ore veins.")
	public int asteroidLapisOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Lapis Ore veins.")
	public int asteroidLapisOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Diamond Ore weight.")
	public int asteroidDiamondOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Diamond Ore weight.")
	public int asteroidDiamondOreMaximumRange = 16;

	@Comment("Minimum size of Asteroid Diamond Ore veins.")
	public int asteroidDiamondOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Diamond Ore veins.")
	public int asteroidDiamondOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Emerald Ore weight.")
	public int asteroidEmeraldOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Emerald Ore weight.")
	public int asteroidEmeraldOreMaximumRange = 16;

	@Comment("Minimum size of Asteroid Emerald Ore veins.")
	public int asteroidEmeraldOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Emerald Ore veins.")
	public int asteroidEmeraldOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Metite Ore weight.")
	public int asteroidMetiteOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Metite Ore weight.")
	public int asteroidMetiteOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Metite Ore veins.")
	public int asteroidMetiteOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Metite Ore veins.")
	public int asteroidMetiteOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Asterite Ore weight.")
	public int asteroidAsteriteOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Asterite Ore weight.")
	public int asteroidAsteriteOreMaximumRange = 32;

	@Comment("Minimum size of Asteroid Asterite Ore veins.")
	public int asteroidAsteriteOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Asterite Ore veins.")
	public int asteroidAsteriteOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Stellum Ore weight.")
	public int asteroidStellumOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Stellum Ore weight.")
	public int asteroidStellumOreMaximumRange = 8;

	@Comment("Minimum size of Asteroid Stellum Ore veins.")
	public int asteroidStellumOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Stellum Ore veins.")
	public int asteroidStellumOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Galaxium Ore weight.")
	public int asteroidGalaxiumOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Galaxium Ore weight.")
	public int asteroidGalaxiumOreMaximumRange = 4;

	@Comment("Minimum size of Asteroid Galaxium Ore veins.")
	public int asteroidGalaxiumOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Galaxium Ore veins.")
	public int asteroidGalaxiumOreMaximumSize = 48;

	@Comment("Whether the mod will attempt to update old item and block names to new ones. Usage recommended when first loading old chunks after an update.")
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
