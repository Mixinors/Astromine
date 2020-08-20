package com.github.chainmailstudios.astromine.foundations.datagen;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.datagen.generators.tag.*;
import me.shedaniel.cloth.api.datagen.v1.TagData;

import java.util.ArrayList;
import java.util.List;

public class TagGenerators {
	private static final List<SetTagGenerator> SET_GENERATORS = new ArrayList<>();
	private static final List<OneTimeTagGenerator> ONE_TIME_GENERATORS = new ArrayList<>();

	public static final SetTagGenerator BEACON_BASE_BLOCKS = register(new BeaconBaseTagGenerator());

	public static final SetTagGenerator BEACON_PAYMENT_INGOTS = register(new BeaconPaymentTagGenerator(MaterialItemType.INGOT));
	public static final SetTagGenerator BEACON_PAYMENT_GEMS = register(new BeaconPaymentTagGenerator(MaterialItemType.GEM));

	public static final SetTagGenerator PIGLIN_BARTERING_INGOTS = register(new PiglinBarterTagGenerator(MaterialItemType.INGOT));
	public static final SetTagGenerator PIGLIN_BARTERING_GEMS = register(new PiglinBarterTagGenerator(MaterialItemType.GEM));

	public static final SetTagGenerator PIGLIN_LOVED_INGOTS = register(new PiglinLovedTagGenerator(MaterialItemType.INGOT));
	public static final SetTagGenerator PIGLIN_LOVED_BLOCKS = register(new PiglinLovedTagGenerator(MaterialItemType.BLOCK));
	public static final SetTagGenerator PIGLIN_LOVED_ORES = register(new PiglinLovedTagGenerator(MaterialItemType.ORE));
	public static final SetTagGenerator PIGLIN_LOVED_GEMS = register(new PiglinLovedTagGenerator(MaterialItemType.GEM));
	public static final SetTagGenerator PIGLIN_LOVED_GEARS = register(new PiglinLovedTagGenerator(MaterialItemType.GEAR));
	public static final SetTagGenerator PIGLIN_LOVED_PLATES = register(new PiglinLovedTagGenerator(MaterialItemType.PLATES));
	public static final SetTagGenerator PIGLIN_LOVED_WIRES = register(new PiglinLovedTagGenerator(MaterialItemType.WIRE));
	public static final SetTagGenerator PIGLIN_LOVED_DUSTS = register(new PiglinLovedTagGenerator(MaterialItemType.DUST));
	public static final SetTagGenerator PIGLIN_LOVED_METEOR_CLUSTERS = register(new PiglinLovedTagGenerator(MaterialItemType.METEOR_CLUSTER));
	public static final SetTagGenerator PIGLIN_LOVED_ASTEROID_CLUSTERS = register(new PiglinLovedTagGenerator(MaterialItemType.ASTEROID_CLUSTER));
	public static final SetTagGenerator PIGLIN_LOVED_METEOR_ORES = register(new PiglinLovedTagGenerator(MaterialItemType.METEOR_ORE));
	public static final SetTagGenerator PIGLIN_LOVED_ASTEROID_ORES = register(new PiglinLovedTagGenerator(MaterialItemType.ASTEROID_ORE));
	public static final SetTagGenerator PIGLIN_LOVED_PICKAXES = register(new PiglinLovedTagGenerator(MaterialItemType.PICKAXE));
	public static final SetTagGenerator PIGLIN_LOVED_AXES = register(new PiglinLovedTagGenerator(MaterialItemType.AXE));
	public static final SetTagGenerator PIGLIN_LOVED_SHOVELS = register(new PiglinLovedTagGenerator(MaterialItemType.SHOVEL));
	public static final SetTagGenerator PIGLIN_LOVED_SWORDS = register(new PiglinLovedTagGenerator(MaterialItemType.SWORD));
	public static final SetTagGenerator PIGLIN_LOVED_HOES = register(new PiglinLovedTagGenerator(MaterialItemType.HOE));
	public static final SetTagGenerator PIGLIN_LOVED_MATTOCKS = register(new PiglinLovedTagGenerator(MaterialItemType.MATTOCK));
	public static final SetTagGenerator PIGLIN_LOVED_MINING_TOOLS = register(new PiglinLovedTagGenerator(MaterialItemType.MINING_TOOL));
	public static final SetTagGenerator PIGLIN_LOVED_HAMMERS = register(new PiglinLovedTagGenerator(MaterialItemType.HAMMER));
	public static final SetTagGenerator PIGLIN_LOVED_EXCAVATORS = register(new PiglinLovedTagGenerator(MaterialItemType.EXCAVATOR));
	public static final SetTagGenerator PIGLIN_LOVED_HELMETS = register(new PiglinLovedTagGenerator(MaterialItemType.HELMET));
	public static final SetTagGenerator PIGLIN_LOVED_CHESTPLATES = register(new PiglinLovedTagGenerator(MaterialItemType.CHESTPLATE));
	public static final SetTagGenerator PIGLIN_LOVED_LEGGINGS = register(new PiglinLovedTagGenerator(MaterialItemType.LEGGINGS));
	public static final SetTagGenerator PIGLIN_LOVED_BOOTS = register(new PiglinLovedTagGenerator(MaterialItemType.BOOTS));

	public static final SetTagGenerator PIGLIN_LOVED_NUGGETS = register(new PiglinNuggetTagGenerator(MaterialItemType.NUGGET));
	public static final SetTagGenerator PIGLIN_LOVED_FRAGMENTS = register(new PiglinNuggetTagGenerator(MaterialItemType.FRAGMENT));
	public static final SetTagGenerator PIGLIN_LOVED_TINY_DUSTS = register(new PiglinNuggetTagGenerator(MaterialItemType.TINY_DUST));

	public static final SetTagGenerator PIGLIN_SAFE_HELMETS = register(new PiglinArmorTagGenerator(MaterialItemType.HELMET));
	public static final SetTagGenerator PIGLIN_SAFE_CHESTPLATES = register(new PiglinArmorTagGenerator(MaterialItemType.CHESTPLATE));
	public static final SetTagGenerator PIGLIN_SAFE_LEGGINGS = register(new PiglinArmorTagGenerator(MaterialItemType.LEGGINGS));
	public static final SetTagGenerator PIGLIN_SAFE_BOOTS = register(new PiglinArmorTagGenerator(MaterialItemType.BOOTS));

	public static final SetTagGenerator METEOR_ORES = register(new OreTagGenerator(MaterialItemType.METEOR_ORE));
	public static final SetTagGenerator ASTEROID_ORES = register(new OreTagGenerator(MaterialItemType.ASTEROID_ORE));

	public static final SetTagGenerator PYRITE = register(new PyriteTagGenerator());
	public static final SetTagGenerator GEM_NUGGETS = register(new GemNuggetTagGenerator());
	public static final OneTimeTagGenerator NETHER_GOLD_ORE = register(new ItemInTagGenerator(new Identifier("c", "gold_ores"), new Identifier("nether_gold_ore")));
	public static final SetTagGenerator CARBON_DUSTS = register(new CarbonDustTagGenerator());
	public static final OneTimeTagGenerator STICK = register(new ItemInTagGenerator(new Identifier("c", "wood_sticks"), new Identifier("stick")));

	public static final OneTimeTagGenerator MATTOCK_AXES = register(new TagInTagGenerator(new Identifier("fabric", "axes"), AstromineCommon.identifier("mattocks")));
	public static final OneTimeTagGenerator MATTOCK_HOES = register(new TagInTagGenerator(new Identifier("fabric", "hoes"), AstromineCommon.identifier("mattocks")));
	public static final OneTimeTagGenerator MINING_TOOLS_PICKAXES = register(new TagInTagGenerator(new Identifier("fabric", "pickaxes"), AstromineCommon.identifier("mining_tools")));
	public static final OneTimeTagGenerator MINING_TOOLS_SHOVELS = register(new TagInTagGenerator(new Identifier("fabric", "shovels"), AstromineCommon.identifier("mining_tools")));
	public static final OneTimeTagGenerator HAMMERS_PICKAXES = register(new TagInTagGenerator(new Identifier("fabric", "pickaxes"), AstromineCommon.identifier("hammers")));
	public static final OneTimeTagGenerator EXCAVATORS_SHOVELS = register(new TagInTagGenerator(new Identifier("fabric", "shovels"), AstromineCommon.identifier("excavators")));

	public static SetTagGenerator register(SetTagGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public static OneTimeTagGenerator register(OneTimeTagGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public static void generateSetTags(TagData tags, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(tags, set);
					System.out.println("generated tag " + generator.getGeneratorName());
				}
			} catch (Exception e) {
				System.out.println("oh fuck tag bronked for " + generator.getGeneratorName());
				System.out.println(e.getMessage());
			}
		});
	}

	public static void generateOneTimeTags(TagData tags) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(tags));
	}
}
