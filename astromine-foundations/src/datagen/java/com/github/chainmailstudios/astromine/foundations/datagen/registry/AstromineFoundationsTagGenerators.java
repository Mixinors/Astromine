package com.github.chainmailstudios.astromine.foundations.datagen.registry;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.tag.onetime.ItemInTagGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.tag.onetime.OneTimeTagGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.tag.onetime.TagInTagGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.tag.set.GenericSetTagGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.tag.set.SetTagGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineTagGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.generators.tag.*;

public class AstromineFoundationsTagGenerators extends AstromineTagGenerators {
	public final SetTagGenerator PICKAXES = register(new GenericSetTagGenerator("pickaxes", new Identifier("fabric", "pickaxes"), MaterialItemType.PICKAXE));
	public final SetTagGenerator AXES = register(new GenericSetTagGenerator("axes", new Identifier("fabric", "axes"), MaterialItemType.AXE));
	public final SetTagGenerator SWORDS = register(new GenericSetTagGenerator("swords", new Identifier("fabric", "swords"), MaterialItemType.SWORD));
	public final SetTagGenerator SHOVELS = register(new GenericSetTagGenerator("shovels", new Identifier("fabric", "shovels"), MaterialItemType.SHOVEL));
	public final SetTagGenerator HOES = register(new GenericSetTagGenerator("hoes", new Identifier("fabric", "hoes"), MaterialItemType.HOE));

	public final SetTagGenerator MINING_TOOLS = register(new GenericSetTagGenerator("mining_tools", AstromineCommon.identifier("mining_tools"), MaterialItemType.MINING_TOOL));
	public final OneTimeTagGenerator MINING_TOOLS_PICKAXES = register(new TagInTagGenerator(new Identifier("fabric", "pickaxes"), AstromineCommon.identifier("mining_tools")));
	public final OneTimeTagGenerator MINING_TOOLS_SHOVELS = register(new TagInTagGenerator(new Identifier("fabric", "shovels"), AstromineCommon.identifier("mining_tools")));

	public final SetTagGenerator MATTOCKS = register(new GenericSetTagGenerator("mattocks", AstromineCommon.identifier("mattocks"), MaterialItemType.MATTOCK));
	public final OneTimeTagGenerator MATTOCK_AXES = register(new TagInTagGenerator(new Identifier("fabric", "axes"), AstromineCommon.identifier("mattocks")));
	public final OneTimeTagGenerator MATTOCK_HOES = register(new TagInTagGenerator(new Identifier("fabric", "hoes"), AstromineCommon.identifier("mattocks")));

	public final SetTagGenerator HAMMERS = register(new GenericSetTagGenerator("hammers", AstromineCommon.identifier("hammers"), MaterialItemType.HAMMER));
	public final OneTimeTagGenerator HAMMERS_PICKAXES = register(new TagInTagGenerator(new Identifier("fabric", "pickaxes"), AstromineCommon.identifier("hammers")));

	public final SetTagGenerator EXCAVATORS = register(new GenericSetTagGenerator("excavators", AstromineCommon.identifier("excavators"), MaterialItemType.EXCAVATOR));
	public final OneTimeTagGenerator EXCAVATORS_SHOVELS = register(new TagInTagGenerator(new Identifier("fabric", "shovels"), AstromineCommon.identifier("excavators")));

	public final SetTagGenerator WRENCHES = register(new GenericSetTagGenerator("wrenches", new Identifier("c", "wrenches"), MaterialItemType.WRENCH));

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

	public final SetTagGenerator GEARS = register(new GenericSetTagGenerator("gears", AstromineCommon.identifier("gears"), MaterialItemType.GEAR));
	public final SetTagGenerator PLATES = register(new GenericSetTagGenerator("plates", AstromineCommon.identifier("plates"), MaterialItemType.PLATE));
}
