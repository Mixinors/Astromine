/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.datagen.registry;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.generator.tag.onetime.ItemInTagGenerator;
import com.github.mixinors.astromine.datagen.generator.tag.onetime.OneTimeTagGenerator;
import com.github.mixinors.astromine.datagen.generator.tag.onetime.TagInTagGenerator;
import com.github.mixinors.astromine.datagen.generator.tag.set.GenericSetTagGenerator;
import com.github.mixinors.astromine.datagen.generator.tag.set.SetTagGenerator;
import com.github.mixinors.astromine.datagen.generator.tag.*;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AMTagGenerators {
	private final List<SetTagGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeTagGenerator> ONE_TIME_GENERATORS = new ArrayList<>();
	
	public final SetTagGenerator ASTEROID_ORES = register(new OreTagGenerator(MaterialItemType.ASTEROID_ORE));
	// public final SetTagGenerator MOON_ORES = register(new OreTagGenerator(MaterialItemType.MOON_ORE));
	
	public final SetTagGenerator PICKAXES = register(new GenericSetTagGenerator("pickaxes", new Identifier("fabric", "pickaxes"), MaterialItemType.PICKAXE));
	public final SetTagGenerator AXES = register(new GenericSetTagGenerator("axes", new Identifier("fabric", "axes"), MaterialItemType.AXE));
	public final SetTagGenerator SWORDS = register(new GenericSetTagGenerator("swords", new Identifier("fabric", "swords"), MaterialItemType.SWORD));
	public final SetTagGenerator SHOVELS = register(new GenericSetTagGenerator("shovels", new Identifier("fabric", "shovels"), MaterialItemType.SHOVEL));
	public final SetTagGenerator HOES = register(new GenericSetTagGenerator("hoes", new Identifier("fabric", "hoes"), MaterialItemType.HOE));
	
	public final SetTagGenerator MINING_TOOLS = register(new GenericSetTagGenerator("mining_tools", AMCommon.id("mining_tools"), MaterialItemType.MINING_TOOL));
	public final OneTimeTagGenerator MINING_TOOLS_PICKAXES = register(new TagInTagGenerator(new Identifier("fabric", "pickaxes"), AMCommon.id("mining_tools")));
	public final OneTimeTagGenerator MINING_TOOLS_SHOVELS = register(new TagInTagGenerator(new Identifier("fabric", "shovels"), AMCommon.id("mining_tools")));
	
	public final SetTagGenerator MATTOCKS = register(new GenericSetTagGenerator("mattocks", AMCommon.id("mattocks"), MaterialItemType.MATTOCK));
	public final OneTimeTagGenerator MATTOCK_AXES = register(new TagInTagGenerator(new Identifier("fabric", "axes"), AMCommon.id("mattocks")));
	public final OneTimeTagGenerator MATTOCK_HOES = register(new TagInTagGenerator(new Identifier("fabric", "hoes"), AMCommon.id("mattocks")));
	
	public final SetTagGenerator HAMMERS = register(new GenericSetTagGenerator("hammers", AMCommon.id("hammers"), MaterialItemType.HAMMER));
	public final OneTimeTagGenerator HAMMERS_PICKAXES = register(new TagInTagGenerator(new Identifier("fabric", "pickaxes"), AMCommon.id("hammers")));
	
	public final SetTagGenerator EXCAVATORS = register(new GenericSetTagGenerator("excavators", AMCommon.id("excavators"), MaterialItemType.EXCAVATOR));
	public final OneTimeTagGenerator EXCAVATORS_SHOVELS = register(new TagInTagGenerator(new Identifier("fabric", "shovels"), AMCommon.id("excavators")));
	
	public final SetTagGenerator BEACON_BASE_BLOCKS = register(new BeaconBaseTagGenerator());
	
	public final SetTagGenerator BEACON_PAYMENT_INGOTS = register(new BeaconPaymentTagGenerator(MaterialItemType.INGOT));
	public final SetTagGenerator BEACON_PAYMENT_GEMS = register(new BeaconPaymentTagGenerator(MaterialItemType.GEM));
	
	public final SetTagGenerator PIGLIN_BARTERING_INGOTS = register(new PiglinBarterTagGenerator(MaterialItemType.INGOT));
	public final SetTagGenerator PIGLIN_BARTERING_GEMS = register(new PiglinBarterTagGenerator(MaterialItemType.GEM));
	
	public final SetTagGenerator PIGLIN_LOVED_INGOTS = register(new PiglinLovedTagGenerator(MaterialItemType.INGOT));
	public final SetTagGenerator PIGLIN_LOVED_BLOCKS = register(new PiglinLovedTagGenerator(MaterialItemType.BLOCK));
	public final SetTagGenerator PIGLIN_LOVED_ORES = register(new PiglinLovedTagGenerator(MaterialItemType.ORE));
	public final SetTagGenerator PIGLIN_LOVED_GEMS = register(new PiglinLovedTagGenerator(MaterialItemType.GEM));
	public final SetTagGenerator PIGLIN_LOVED_GEARS = register(new PiglinLovedTagGenerator(MaterialItemType.GEAR));
	public final SetTagGenerator PIGLIN_LOVED_PLATES = register(new PiglinLovedTagGenerator(MaterialItemType.PLATE));
	public final SetTagGenerator PIGLIN_LOVED_WIRES = register(new PiglinLovedTagGenerator(MaterialItemType.WIRE));
	public final SetTagGenerator PIGLIN_LOVED_DUSTS = register(new PiglinLovedTagGenerator(MaterialItemType.DUST));
	public final SetTagGenerator PIGLIN_LOVED_METEOR_CLUSTERS = register(new PiglinLovedTagGenerator(MaterialItemType.METEOR_CLUSTER));
	public final SetTagGenerator PIGLIN_LOVED_ASTEROID_CLUSTERS = register(new PiglinLovedTagGenerator(MaterialItemType.ASTEROID_CLUSTER));
	public final SetTagGenerator PIGLIN_LOVED_METEOR_ORES = register(new PiglinLovedTagGenerator(MaterialItemType.METEOR_ORE));
	public final SetTagGenerator PIGLIN_LOVED_ASTEROID_ORES = register(new PiglinLovedTagGenerator(MaterialItemType.ASTEROID_ORE));
	public final SetTagGenerator PIGLIN_LOVED_PICKAXES = register(new PiglinLovedTagGenerator(MaterialItemType.PICKAXE));
	public final SetTagGenerator PIGLIN_LOVED_AXES = register(new PiglinLovedTagGenerator(MaterialItemType.AXE));
	public final SetTagGenerator PIGLIN_LOVED_SHOVELS = register(new PiglinLovedTagGenerator(MaterialItemType.SHOVEL));
	public final SetTagGenerator PIGLIN_LOVED_SWORDS = register(new PiglinLovedTagGenerator(MaterialItemType.SWORD));
	public final SetTagGenerator PIGLIN_LOVED_HOES = register(new PiglinLovedTagGenerator(MaterialItemType.HOE));
	public final SetTagGenerator PIGLIN_LOVED_MATTOCKS = register(new PiglinLovedTagGenerator(MaterialItemType.MATTOCK));
	public final SetTagGenerator PIGLIN_LOVED_MINING_TOOLS = register(new PiglinLovedTagGenerator(MaterialItemType.MINING_TOOL));
	public final SetTagGenerator PIGLIN_LOVED_HAMMERS = register(new PiglinLovedTagGenerator(MaterialItemType.HAMMER));
	public final SetTagGenerator PIGLIN_LOVED_EXCAVATORS = register(new PiglinLovedTagGenerator(MaterialItemType.EXCAVATOR));
	public final SetTagGenerator PIGLIN_LOVED_HELMETS = register(new PiglinLovedTagGenerator(MaterialItemType.HELMET));
	public final SetTagGenerator PIGLIN_LOVED_CHESTPLATES = register(new PiglinLovedTagGenerator(MaterialItemType.CHESTPLATE));
	public final SetTagGenerator PIGLIN_LOVED_LEGGINGS = register(new PiglinLovedTagGenerator(MaterialItemType.LEGGINGS));
	public final SetTagGenerator PIGLIN_LOVED_BOOTS = register(new PiglinLovedTagGenerator(MaterialItemType.BOOTS));
	public final SetTagGenerator PIGLIN_LOVED_APPLES = register(new PiglinLovedTagGenerator(MaterialItemType.APPLE));
	
	public final SetTagGenerator PIGLIN_LOVED_NUGGETS = register(new PiglinNuggetTagGenerator(MaterialItemType.NUGGET));
	public final SetTagGenerator PIGLIN_LOVED_FRAGMENTS = register(new PiglinNuggetTagGenerator(MaterialItemType.FRAGMENT));
	public final SetTagGenerator PIGLIN_LOVED_TINY_DUSTS = register(new PiglinNuggetTagGenerator(MaterialItemType.TINY_DUST));
	
	public final SetTagGenerator PIGLIN_SAFE_HELMETS = register(new PiglinArmorTagGenerator(MaterialItemType.HELMET));
	public final SetTagGenerator PIGLIN_SAFE_CHESTPLATES = register(new PiglinArmorTagGenerator(MaterialItemType.CHESTPLATE));
	public final SetTagGenerator PIGLIN_SAFE_LEGGINGS = register(new PiglinArmorTagGenerator(MaterialItemType.LEGGINGS));
	public final SetTagGenerator PIGLIN_SAFE_BOOTS = register(new PiglinArmorTagGenerator(MaterialItemType.BOOTS));
	
	public final SetTagGenerator TRICKS_PIGLINS = register(new TricksPiglinsTagGenerator());
	
	public final SetTagGenerator METEOR_ORES = register(new OreTagGenerator(MaterialItemType.METEOR_ORE));
	
	public final SetTagGenerator PYRITE = register(new PyriteTagGenerator());
	public final SetTagGenerator GEM_NUGGETS = register(new GemNuggetTagGenerator());
	public final OneTimeTagGenerator NETHER_GOLD_ORE = register(new ItemInTagGenerator(new Identifier("c", "gold_ores"), new Identifier("nether_gold_ore")));
	public final SetTagGenerator CARBON_DUSTS = register(new CarbonDustTagGenerator());
	public final OneTimeTagGenerator STICK = register(new ItemInTagGenerator(new Identifier("c", "wood_sticks"), new Identifier("stick")));
	public final OneTimeTagGenerator ENCHANTED_GOLDEN_APPLE = register(new ItemInTagGenerator(new Identifier("c", "golden_apples"), new Identifier("enchanted_golden_apple")));
	
	public final SetTagGenerator GEARS = register(new GenericSetTagGenerator("gears", AMCommon.id("gears"), MaterialItemType.GEAR));
	public final SetTagGenerator METAL_PLATES = register(new GenericSetTagGenerator("metal_plates", AMCommon.id("metal_plates"), MaterialItemType.PLATE));

	public SetTagGenerator register(SetTagGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeTagGenerator register(OneTimeTagGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateTags(TagData tags) {
		AMMaterialSets.getMaterialSets().forEach((set) -> generateSetTags(tags, set));
		generateOneTimeTags(tags);
	}

	private void generateSetTags(TagData tags, MaterialSet set) {
		set.generateTags(tags);
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(tags, set);
					AMCommon.LOGGER.info("Tag generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AMCommon.LOGGER.error("Tag generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AMCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeTags(TagData tags) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(tags));
	}
}
