/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.foundations.registry;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.ConfigManager;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "astromine/foundations")
public class AstromineFoundationsConfig implements ConfigData {
	@ConfigEntry.Gui.Excluded
	public static AstromineFoundationsConfig instance;

	@Comment("Whether generation of Copper Ore in the Overworld is enabled.")
	public boolean overworldCopperOre = true;

	@Comment("Whether generation of Tin Ore in the Overworld is enabled.")
	public boolean overworldTinOre = true;

	@Comment("Whether generation of Silver Ore in the Overworld is enabled.")
	public boolean overworldSilverOre = true;

	@Comment("Whether generation of Lead Ore in the Overworld is enabled.")
	public boolean overworldLeadOre = true;

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

	@Comment("Minimum range of Asteroid Silver Ore weight.")
	public int asteroidSilverOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Silver Ore weight.")
	public int asteroidSilverOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Silver Ore veins.")
	public int asteroidSilverOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Silver Ore veins.")
	public int asteroidSilverOreMaximumSize = 48;

	@Comment("Minimum range of Asteroid Lead Ore weight.")
	public int asteroidLeadOreMinimumRange = 0;

	@Comment("Maximum range of Asteroid Lead Ore weight.")
	public int asteroidLeadOreMaximumRange = 48;

	@Comment("Minimum size of Asteroid Lead Ore veins.")
	public int asteroidLeadOreMinimumSize = 8;

	@Comment("Maximum Size of Asteroid Lead Ore veins.")
	public int asteroidLeadOreMaximumSize = 48;

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

	public static AstromineFoundationsConfig get() {
		if (instance == null) {
			try {
				AutoConfig.register(AstromineFoundationsConfig.class, JanksonConfigSerializer::new);
				try {
					((ConfigManager<AstromineFoundationsConfig>) AutoConfig.getConfigHolder(AstromineFoundationsConfig.class)).save();
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
				instance = AutoConfig.getConfigHolder(AstromineFoundationsConfig.class).getConfig();
			} catch (Throwable throwable) {
				throwable.printStackTrace();
				instance = new AstromineFoundationsConfig();
			}
		}

		return instance;
	}
}
