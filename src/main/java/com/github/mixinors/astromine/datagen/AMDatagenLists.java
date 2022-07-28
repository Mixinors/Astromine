package com.github.mixinors.astromine.datagen;

import com.github.mixinors.astromine.common.fluid.base.ExtendedFluid;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.registry.common.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;

import java.util.List;
import java.util.Map;

public class AMDatagenLists {
	public static class BlockLists {
		public static final List<Block> PRIMITIVE_MACHINES = ImmutableList.of(
				AMBlocks.PRIMITIVE_TANK.get(),
				AMBlocks.PRIMITIVE_SOLID_GENERATOR.get(),
				AMBlocks.PRIMITIVE_FLUID_GENERATOR.get(),
				AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get(),
				AMBlocks.PRIMITIVE_ALLOY_SMELTER.get(),
				AMBlocks.PRIMITIVE_TRITURATOR.get(),
				AMBlocks.PRIMITIVE_PRESSER.get(),
				AMBlocks.PRIMITIVE_WIRE_MILL.get(),
				AMBlocks.PRIMITIVE_ELECTROLYZER.get(),
				AMBlocks.PRIMITIVE_REFINERY.get(),
				AMBlocks.PRIMITIVE_FLUID_MIXER.get(),
				AMBlocks.PRIMITIVE_SOLIDIFIER.get(),
				AMBlocks.PRIMITIVE_MELTER.get(),
				AMBlocks.PRIMITIVE_BUFFER.get(),
				AMBlocks.PRIMITIVE_CAPACITOR.get()
		);
		
		public static final List<Block> BASIC_MACHINES = ImmutableList.of(
				AMBlocks.BASIC_TANK.get(),
				AMBlocks.BASIC_SOLID_GENERATOR.get(),
				AMBlocks.BASIC_FLUID_GENERATOR.get(),
				AMBlocks.BASIC_ELECTRIC_FURNACE.get(),
				AMBlocks.BASIC_ALLOY_SMELTER.get(),
				AMBlocks.BASIC_TRITURATOR.get(),
				AMBlocks.BASIC_PRESSER.get(),
				AMBlocks.BASIC_WIRE_MILL.get(),
				AMBlocks.BASIC_ELECTROLYZER.get(),
				AMBlocks.BASIC_REFINERY.get(),
				AMBlocks.BASIC_FLUID_MIXER.get(),
				AMBlocks.BASIC_SOLIDIFIER.get(),
				AMBlocks.BASIC_MELTER.get(),
				AMBlocks.BASIC_BUFFER.get(),
				AMBlocks.BASIC_CAPACITOR.get()
		);
		
		public static final List<Block> ADVANCED_MACHINES = ImmutableList.of(
				AMBlocks.ADVANCED_TANK.get(),
				AMBlocks.ADVANCED_SOLID_GENERATOR.get(),
				AMBlocks.ADVANCED_FLUID_GENERATOR.get(),
				AMBlocks.ADVANCED_ELECTRIC_FURNACE.get(),
				AMBlocks.ADVANCED_ALLOY_SMELTER.get(),
				AMBlocks.ADVANCED_TRITURATOR.get(),
				AMBlocks.ADVANCED_PRESSER.get(),
				AMBlocks.ADVANCED_WIRE_MILL.get(),
				AMBlocks.ADVANCED_ELECTROLYZER.get(),
				AMBlocks.ADVANCED_REFINERY.get(),
				AMBlocks.ADVANCED_FLUID_MIXER.get(),
				AMBlocks.ADVANCED_SOLIDIFIER.get(),
				AMBlocks.ADVANCED_MELTER.get(),
				AMBlocks.ADVANCED_BUFFER.get(),
				AMBlocks.ADVANCED_CAPACITOR.get()
		);
		
		public static final List<Block> ELITE_MACHINES = ImmutableList.of(
				AMBlocks.ELITE_TANK.get(),
				AMBlocks.ELITE_SOLID_GENERATOR.get(),
				AMBlocks.ELITE_FLUID_GENERATOR.get(),
				AMBlocks.ELITE_ELECTRIC_FURNACE.get(),
				AMBlocks.ELITE_ALLOY_SMELTER.get(),
				AMBlocks.ELITE_TRITURATOR.get(),
				AMBlocks.ELITE_PRESSER.get(),
				AMBlocks.ELITE_WIRE_MILL.get(),
				AMBlocks.ELITE_ELECTROLYZER.get(),
				AMBlocks.ELITE_REFINERY.get(),
				AMBlocks.ELITE_FLUID_MIXER.get(),
				AMBlocks.ELITE_SOLIDIFIER.get(),
				AMBlocks.ELITE_MELTER.get(),
				AMBlocks.ELITE_BUFFER.get(),
				AMBlocks.ELITE_CAPACITOR.get()
		);
		
		public static final List<Block> CREATIVE_MACHINES = ImmutableList.of(
				AMBlocks.CREATIVE_TANK.get(),
				AMBlocks.CREATIVE_BUFFER.get(),
				AMBlocks.CREATIVE_CAPACITOR.get()
		);
		
		public static final List<Block> MISC_MACHINES = ImmutableList.of(
				AMBlocks.FLUID_EXTRACTOR.get(),
				AMBlocks.FLUID_INSERTER.get(),
				
				AMBlocks.BLOCK_BREAKER.get(),
				AMBlocks.BLOCK_PLACER.get(),
				
				AMBlocks.PUMP.get()
		);
		
		public static final List<Block> MACHINES = ImmutableList.<Block>builder()
																.addAll(PRIMITIVE_MACHINES)
																.addAll(BASIC_MACHINES)
																.addAll(ADVANCED_MACHINES)
																.addAll(ELITE_MACHINES)
																.addAll(CREATIVE_MACHINES)
																.addAll(MISC_MACHINES)
																.build();
		
		public static final List<Block> ENERGY_CABLES = ImmutableList.of(
				AMBlocks.PRIMITIVE_ENERGY_CABLE.get(),
				AMBlocks.BASIC_ENERGY_CABLE.get(),
				AMBlocks.ADVANCED_ENERGY_CABLE.get(),
				AMBlocks.ELITE_ENERGY_CABLE.get()
		);
		
		public static final List<Block> INFINIBURN_BLOCKS = ImmutableList.of(
				AMBlocks.BLAZING_ASTEROID_STONE.get()
		);
	}
	
	public static class ItemLists {
		public static final List<Item> DRILLS = ImmutableList.of(
				AMItems.PRIMITIVE_DRILL.get(),
				AMItems.BASIC_DRILL.get(),
				AMItems.ADVANCED_DRILL.get(),
				AMItems.ELITE_DRILL.get()
		);
		
		public static final List<Item> ONE_BIOFUEL_ITEMS = ImmutableList.of(
				Items.GRASS,
				Items.FERN,
				Items.DEAD_BUSH,
				Items.SEAGRASS,
				Items.SEA_PICKLE,
				Items.NETHER_SPROUTS,
				Items.LILY_PAD,
				Items.MELON_SLICE,
				Items.WHEAT,
				Items.APPLE,
				Items.COOKIE,
				Items.SUGAR_CANE,
				Items.KELP,
				Items.DRIED_KELP,
				Items.BAMBOO,
				Items.CARROT,
				Items.POTATO,
				Items.BAKED_POTATO,
				Items.POISONOUS_POTATO,
				Items.BEETROOT,
				Items.GLOW_LICHEN,
				Items.SUGAR,
				Items.HONEYCOMB
		);
		
		public static final List<Item> TWO_BIOFUEL_ITEMS = ImmutableList.of(
				Items.PORKCHOP,
				Items.COOKED_PORKCHOP,
				Items.BEEF,
				Items.COOKED_BEEF,
				Items.CHICKEN,
				Items.COOKED_CHICKEN,
				Items.ROTTEN_FLESH,
				Items.SPIDER_EYE,
				Items.FERMENTED_SPIDER_EYE,
				Items.RABBIT,
				Items.COOKED_RABBIT,
				Items.MUTTON,
				Items.COOKED_MUTTON,
				Items.CACTUS,
				Items.TALL_GRASS,
				Items.LARGE_FERN,
				Items.MOSS_CARPET,
				Items.LEATHER,
				Items.EGG,
				Items.CHORUS_FRUIT,
				Items.POPPED_CHORUS_FRUIT,
				Items.CHORUS_FLOWER,
				Items.CHORUS_PLANT
		);
		
		public static final List<Item> FOUR_BIOFUEL_ITEMS = ImmutableList.of(
				Items.CAKE,
				Items.SPORE_BLOSSOM
		);
		
		public static final List<Item> NINE_BIOFUEL_ITEMS = ImmutableList.of(
				Items.HONEY_BLOCK,
				Items.HONEYCOMB_BLOCK,
				Items.PUMPKIN_PIE,
				Items.MOSS_BLOCK
		);
	}
	
	public static class FluidLists {
		public static final List<ExtendedFluid> INDUSTRIAL_FLUIDS = ImmutableList.of(
				AMFluids.OIL,
				AMFluids.FUEL,
				AMFluids.BIOMASS,
				AMFluids.OXYGEN,
				AMFluids.HYDROGEN
		);
		
		public static final List<ExtendedFluid> MOLTEN_FLUIDS = ImmutableList.of(
				AMFluids.MOLTEN_AMETHYST,
				AMFluids.MOLTEN_ASTERITE,
				AMFluids.MOLTEN_BRONZE,
				AMFluids.MOLTEN_COPPER,
				AMFluids.MOLTEN_DIAMOND,
				AMFluids.MOLTEN_ELECTRUM,
				AMFluids.MOLTEN_EMERALD,
				AMFluids.MOLTEN_FOOLS_GOLD,
				AMFluids.MOLTEN_GALAXIUM,
				AMFluids.MOLTEN_GOLD,
				AMFluids.MOLTEN_IRON,
				AMFluids.MOLTEN_LAPIS,
				AMFluids.MOLTEN_LEAD,
				AMFluids.MOLTEN_METEORIC_STEEL,
				AMFluids.MOLTEN_METITE,
				AMFluids.MOLTEN_NETHERITE,
				AMFluids.MOLTEN_QUARTZ,
				AMFluids.MOLTEN_REDSTONE,
				AMFluids.MOLTEN_SILVER,
				AMFluids.MOLTEN_STEEL,
				AMFluids.MOLTEN_STELLUM,
				AMFluids.MOLTEN_TIN,
				AMFluids.MOLTEN_UNIVITE,
				AMFluids.MOLTEN_LUNUM
		);
		
		public static final List<ExtendedFluid> FLUIDS = ImmutableList.<ExtendedFluid>builder()
																			   .addAll(INDUSTRIAL_FLUIDS)
																			   .addAll(MOLTEN_FLUIDS)
																			   .build();
	}
	
	public static class EntityTypeLists {
		public static final List<EntityType<?>> FISH = ImmutableList.of(
				EntityType.COD,
				EntityType.PUFFERFISH,
				EntityType.SALMON,
				EntityType.TROPICAL_FISH
		);
		
		public static final List<EntityType<?>> SQUIDS = ImmutableList.of(
				EntityType.SQUID,
				EntityType.GLOW_SQUID
		);
		
		public static final List<EntityType<?>> GUARDIANS = ImmutableList.of(
				EntityType.GUARDIAN,
				EntityType.ELDER_GUARDIAN
		);
		
		public static final List<EntityType<?>> SKELETONS = ImmutableList.of(
				EntityType.SKELETON,
				EntityType.WITHER_SKELETON,
				EntityType.STRAY,
				EntityType.SKELETON_HORSE
		);
		
		public static final List<EntityType<?>> ZOMBIES = ImmutableList.of(
				EntityType.ZOMBIE,
				EntityType.ZOMBIE_VILLAGER,
				EntityType.ZOMBIFIED_PIGLIN,
				EntityType.ZOGLIN,
				EntityType.GIANT,
				EntityType.ZOMBIE_HORSE,
				EntityType.DROWNED
		);
		
		public static final List<EntityType<?>> SPACE_SLIMES = ImmutableList.of(
				AMEntityTypes.SPACE_SLIME.get(),
				AMEntityTypes.SUPER_SPACE_SLIME.get()
		);
		
		public static final List<EntityType<?>> DOES_NOT_BREATHE_ENTITY_TYPES = ImmutableList.of(
				EntityType.ARMOR_STAND,
				EntityType.IRON_GOLEM
		);
		
		public static final List<EntityType<?>> CAN_BREATHE_WATER_ENTITY_TYPES = ImmutableList.of(
				EntityType.AXOLOTL,
				EntityType.DOLPHIN, // not scientifically accurate, but shut up
				EntityType.TURTLE
		);
		
		public static final List<EntityType<?>> CAN_BREATHE_LAVA_ENTITY_TYPES = ImmutableList.of(
				EntityType.VEX,
				EntityType.MAGMA_CUBE,
				EntityType.BLAZE,
				EntityType.GHAST,
				EntityType.ZOGLIN,
				EntityType.ZOMBIFIED_PIGLIN,
				EntityType.WITHER,
				EntityType.STRIDER
		);
		
		public static final List<EntityType<?>> IGNORES_DIMENSIONAL_LAYERS = ImmutableList.of(
				AMEntityTypes.ROCKET.get()
		);
	}
	
	public static class ItemTagLists {
		public static final List<TagKey<Item>> ONE_BIOFUEL_TAGS = ImmutableList.of(
				AMTagKeys.createCommonItemTag("mushrooms"),
				AMTagKeys.createCommonItemTag("nether_fungi"),
				AMTagKeys.createCommonItemTag("nether_roots"),
				AMTagKeys.createCommonItemTag("vines"),
				AMTagKeys.createCommonItemTag("berries"),
				AMTagKeys.createCommonItemTag("seeds")
		);
		
		public static final List<TagKey<Item>> FOUR_BIOFUEL_TAGS = ImmutableList.of(
				AMTagKeys.createCommonItemTag("metal_apples")
		);
		
		public static final List<TagKey<Item>> NINE_BIOFUEL_TAGS = ImmutableList.of(
				AMTagKeys.createCommonItemTag("gourds")
		);
		
		/**
		 * To add vanilla tags to an Astromine tag they need to be 'forced'
		 */
		public static final List<TagKey<Item>> ONE_BIOFUEL_TAGS_FORCED = ImmutableList.of(
				ItemTags.SMALL_FLOWERS,
				ItemTags.LEAVES
		);
		
		public static final List<TagKey<Item>> TWO_BIOFUEL_TAGS_FORCED = ImmutableList.of(
				ItemTags.TALL_FLOWERS,
				ItemTags.SAPLINGS
		);
		
		public static final List<TagKey<Item>> FOUR_BIOFUEL_TAGS_FORCED = ImmutableList.of(
				ItemTags.FISHES
		);
		
		public static final Map<List<ItemVariant>, TagKey<Item>> GENERIC_TAGS = ImmutableMap.of(
				ItemVariantLists.CLUSTER_VARIANTS, AMTagKeys.createCommonItemTag("clusters"),
				ItemVariantLists.ARMOR_VARIANTS, AMTagKeys.createCommonItemTag("armor"),
				ItemVariantLists.TOOL_VARIANTS, AMTagKeys.createCommonItemTag("tools")
		);
		
		public static final Map<TagKey<Block>, TagKey<Item>> COPY = ImmutableMap.<TagKey<Block>, TagKey<Item>>builder()
				.put(AMTagKeys.BlockTags.YELLOW_SANDSTONES, AMTagKeys.ItemTags.YELLOW_SANDSTONES)
				.put(AMTagKeys.BlockTags.RED_SANDSTONES, AMTagKeys.ItemTags.RED_SANDSTONES)
				.put(AMTagKeys.BlockTags.SANDSTONES, AMTagKeys.ItemTags.SANDSTONES)
				.put(AMTagKeys.BlockTags.QUARTZ_BLOCKS, AMTagKeys.ItemTags.QUARTZ_BLOCKS)
				.put(AMTagKeys.BlockTags.UNWAXED_COPPER_BLOCKS, AMTagKeys.ItemTags.UNWAXED_COPPER_BLOCKS)
				.put(AMTagKeys.BlockTags.WAXED_COPPER_BLOCKS, AMTagKeys.ItemTags.WAXED_COPPER_BLOCKS)
				.put(AMTagKeys.BlockTags.COPPER_BLOCKS, AMTagKeys.ItemTags.COPPER_BLOCKS)
				.put(AMTagKeys.BlockTags.UNWAXED_CUT_COPPER, AMTagKeys.ItemTags.UNWAXED_CUT_COPPER)
				.put(AMTagKeys.BlockTags.WAXED_CUT_COPPER, AMTagKeys.ItemTags.WAXED_CUT_COPPER)
				.put(AMTagKeys.BlockTags.CUT_COPPER, AMTagKeys.ItemTags.CUT_COPPER)
				.put(AMTagKeys.BlockTags.PURPUR_BLOCKS, AMTagKeys.ItemTags.PURPUR_BLOCKS)
				.put(AMTagKeys.BlockTags.MUSHROOMS, AMTagKeys.ItemTags.MUSHROOMS)
				.put(AMTagKeys.BlockTags.MUSHROOM_BLOCKS, AMTagKeys.ItemTags.MUSHROOM_BLOCKS)
				.put(AMTagKeys.BlockTags.NETHER_FUNGI, AMTagKeys.ItemTags.NETHER_FUNGI)
				.put(AMTagKeys.BlockTags.NETHER_ROOTS, AMTagKeys.ItemTags.NETHER_ROOTS)
				.put(AMTagKeys.BlockTags.NETHER_VINES, AMTagKeys.ItemTags.NETHER_VINES)
				.put(AMTagKeys.BlockTags.PUMPKINS, AMTagKeys.ItemTags.PUMPKINS)
				.put(AMTagKeys.BlockTags.GOURDS, AMTagKeys.ItemTags.GOURDS)
				.put(AMTagKeys.BlockTags.WEEPING_VINES, AMTagKeys.ItemTags.WEEPING_VINES)
				.put(AMTagKeys.BlockTags.TWISTING_VINES, AMTagKeys.ItemTags.TWISTING_VINES)
				.put(ConventionalBlockTags.ORES, ConventionalItemTags.ORES)
				.build();
		
		public static final Map<BlockFamily.Variant, TagKey<Item>> BLOCK_FAMILY_VARIANTS = ImmutableMap.of(
				BlockFamily.Variant.SLAB, ItemTags.SLABS,
				BlockFamily.Variant.STAIRS, ItemTags.STAIRS,
				BlockFamily.Variant.WALL, ItemTags.WALLS,
				BlockFamily.Variant.BUTTON, ItemTags.BUTTONS,
				BlockFamily.Variant.DOOR, ItemTags.DOORS,
				BlockFamily.Variant.FENCE, ItemTags.FENCES,
				BlockFamily.Variant.SIGN, ItemTags.SIGNS,
				BlockFamily.Variant.TRAPDOOR, ItemTags.TRAPDOORS
		);
	}
	
	public static class BlockTagLists {
		public static final List<TagKey<Block>> INFINIBURN_TAGS = ImmutableList.of(
				AMTagKeys.createCommonBlockTag("meteor_ores"),
				AMTagKeys.createCommonBlockTag("asteroid_ores")
		);
		
		public static final Map<BlockFamily.Variant, TagKey<Block>> BLOCK_FAMILY_VARIANTS = ImmutableMap.<BlockFamily.Variant, TagKey<Block>>builder()
				.put(BlockFamily.Variant.SLAB, BlockTags.SLABS)
				.put(BlockFamily.Variant.STAIRS, BlockTags.STAIRS)
				.put(BlockFamily.Variant.WALL, BlockTags.WALLS)
				.put(BlockFamily.Variant.BUTTON, BlockTags.BUTTONS)
				.put(BlockFamily.Variant.DOOR, BlockTags.DOORS)
				.put(BlockFamily.Variant.FENCE, BlockTags.FENCES)
				.put(BlockFamily.Variant.SIGN, BlockTags.SIGNS)
				.put(BlockFamily.Variant.TRAPDOOR, BlockTags.TRAPDOORS)
				.put(BlockFamily.Variant.PRESSURE_PLATE, BlockTags.PRESSURE_PLATES)
				.put(BlockFamily.Variant.FENCE_GATE, BlockTags.FENCE_GATES)
				.put(BlockFamily.Variant.WALL_SIGN, BlockTags.WALL_SIGNS)
				.build();
	}
	
	public static class EntityTypeTagLists {
		public static final List<TagKey<EntityType<?>>> DOES_NOT_BREATHE_TAGS = ImmutableList.of(
				AMTagKeys.EntityTypeTags.SKELETONS,
				AMTagKeys.EntityTypeTags.SPACE_SLIMES
		);
		
		public static final List<TagKey<EntityType<?>>> CAN_BREATHE_WATER_TAGS = ImmutableList.of(
				AMTagKeys.EntityTypeTags.FISH,
				AMTagKeys.EntityTypeTags.SQUIDS,
				AMTagKeys.EntityTypeTags.GUARDIANS,
				AMTagKeys.EntityTypeTags.ZOMBIES // note: need to do special handling for husk -> zombie -> drowned
		);
		
		public static final List<TagKey<EntityType<?>>> CANNOT_BREATHE_OXYGEN_TAGS = ImmutableList.of(
				AMTagKeys.EntityTypeTags.FISH,
				AMTagKeys.EntityTypeTags.SQUIDS
		);
	}
	
	public static class BlockVariantLists {
		public static final List<BlockVariant> ORE_VARIANTS = ImmutableList.of(
				BlockVariant.STONE_ORE,
				BlockVariant.DEEPSLATE_ORE,
				BlockVariant.NETHER_ORE,
				BlockVariant.METEOR_ORE,
				BlockVariant.ASTEROID_ORE,
				BlockVariant.MOON_ORE,
				BlockVariant.DARK_MOON_ORE
		);
	}
	
	public static class ItemVariantLists {
		public static final List<ItemVariant> ARMOR_VARIANTS = ImmutableList.of(
				ItemVariant.HELMET,
				ItemVariant.CHESTPLATE,
				ItemVariant.LEGGINGS,
				ItemVariant.BOOTS
		);
		
		public static final List<ItemVariant> TOOL_VARIANTS = ImmutableList.of(
				ItemVariant.PICKAXE,
				ItemVariant.AXE,
				ItemVariant.SHOVEL,
				ItemVariant.SWORD,
				ItemVariant.HOE
		);
		
		public static final List<ItemVariant> EQUIPMENT_VARIANTS = ImmutableList.<ItemVariant>builder()
																						 .addAll(ARMOR_VARIANTS)
																						 .addAll(TOOL_VARIANTS)
																						 .add(ItemVariant.HORSE_ARMOR)
																						 .build();

		public static final List<ItemVariant> CLUSTER_VARIANTS = ImmutableList.of(
				ItemVariant.METEOR_ORE_CLUSTER,
				ItemVariant.ASTEROID_ORE_CLUSTER
		);
	}
	
	public static class BlockFamilyLists {
		public static final List<BlockFamily> SPACE_STONE_FAMILIES = ImmutableList.of(
				AMBlockFamilies.METEOR_STONE,
				AMBlockFamilies.SMOOTH_METEOR_STONE,
				AMBlockFamilies.POLISHED_METEOR_STONE,
				AMBlockFamilies.METEOR_STONE_BRICK,
				AMBlockFamilies.ASTEROID_STONE,
				AMBlockFamilies.SMOOTH_ASTEROID_STONE,
				AMBlockFamilies.POLISHED_ASTEROID_STONE,
				AMBlockFamilies.ASTEROID_STONE_BRICK
		);

		public static final List<BlockFamily> MOON_STONE_FAMILIES = ImmutableList.of(
				AMBlockFamilies.MOON_STONE,
				AMBlockFamilies.SMOOTH_MOON_STONE,
				AMBlockFamilies.POLISHED_MOON_STONE,
				AMBlockFamilies.MOON_STONE_BRICK,
				AMBlockFamilies.DARK_MOON_STONE,
				AMBlockFamilies.SMOOTH_DARK_MOON_STONE,
				AMBlockFamilies.POLISHED_DARK_MOON_STONE,
				AMBlockFamilies.DARK_MOON_STONE_BRICK
		);
	}
}
