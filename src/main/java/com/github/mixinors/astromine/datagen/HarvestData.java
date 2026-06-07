package com.github.mixinors.astromine.datagen;

import com.github.mixinors.astromine.registry.common.AMTagKeys;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

public record HarvestData(
		TagKey<Block> mineableTag,
		int miningLevel
) {
	public static final HarvestData PICKAXE = new HarvestData(BlockTags.MINEABLE_WITH_PICKAXE);
	public static final HarvestData AXE = new HarvestData(BlockTags.MINEABLE_WITH_AXE);
	public static final HarvestData SHOVEL = new HarvestData(BlockTags.MINEABLE_WITH_SHOVEL);
	public static final HarvestData HOE = new HarvestData(BlockTags.MINEABLE_WITH_HOE);
	
	public static final int WOOD = 0;
	public static final int STONE = 1;
	public static final int IRON = 2;
	public static final int DIAMOND = 3;
	public static final int NETHERITE = 4;
	
	public static final HarvestData STONE_PICKAXE = new HarvestData(STONE);
	public static final HarvestData IRON_PICKAXE = new HarvestData(IRON);
	public static final HarvestData DIAMOND_PICKAXE = new HarvestData(DIAMOND);
	public static final HarvestData NETHERITE_PICKAXE = new HarvestData(NETHERITE);
	public static final HarvestData LEVEL_5_PICKAXE = new HarvestData(5);
	public static final HarvestData LEVEL_6_PICKAXE = new HarvestData(6);
	
	public static final HarvestData SPACE_STONE_HARVEST_DATA = DIAMOND_PICKAXE;
	public static final HarvestData MOON_STONE_HARVEST_DATA = PICKAXE;
	
	public static final HarvestData PIPE_AND_CABLE_HARVEST_DATA = STONE_PICKAXE;
	
	public static final HarvestData PRIMITIVE_MACHINE_HARVEST_DATA = STONE_PICKAXE;
	public static final HarvestData BASIC_MACHINE_HARVEST_DATA = IRON_PICKAXE;
	public static final HarvestData ADVANCED_MACHINE_HARVEST_DATA = IRON_PICKAXE;
	public static final HarvestData ELITE_MACHINE_HARVEST_DATA = NETHERITE_PICKAXE;
	public static final HarvestData MISC_MACHINE_HARVEST_DATA = IRON_PICKAXE;
	
	public HarvestData(TagKey<Block> mineableTag) {
		this(mineableTag, WOOD);
	}
	
	public HarvestData(int miningLevel) {
		this(BlockTags.MINEABLE_WITH_PICKAXE, miningLevel);
	}
	
	public TagKey<Block> miningLevelTag() {
		return switch (miningLevel()) {
			case STONE -> BlockTags.NEEDS_STONE_TOOL;
			case IRON -> BlockTags.NEEDS_IRON_TOOL;
			case DIAMOND -> BlockTags.NEEDS_DIAMOND_TOOL;
			case NETHERITE -> Tags.Blocks.NEEDS_NETHERITE_TOOL;
			default -> AMTagKeys.createCommonBlockTag("needs_tool_level_" + miningLevel());
		};
	}
}
