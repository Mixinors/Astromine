package com.github.mixinors.astromine.datagen;

import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;

public record HarvestData(
		TagKey<Block> mineableTag,
		int miningLevel
) {
	public static final HarvestData PICKAXE = new HarvestData(BlockTags.PICKAXE_MINEABLE);
	public static final HarvestData AXE = new HarvestData(BlockTags.AXE_MINEABLE);
	public static final HarvestData SHOVEL = new HarvestData(BlockTags.SHOVEL_MINEABLE);
	public static final HarvestData HOE = new HarvestData(BlockTags.HOE_MINEABLE);
	
	public static final HarvestData STONE_PICKAXE = new HarvestData(MiningLevels.STONE);
	public static final HarvestData IRON_PICKAXE = new HarvestData(MiningLevels.IRON);
	public static final HarvestData DIAMOND_PICKAXE = new HarvestData(MiningLevels.DIAMOND);
	public static final HarvestData NETHERITE_PICKAXE = new HarvestData(MiningLevels.NETHERITE);
	public static final HarvestData LEVEL_5_PICKAXE = new HarvestData(5);
	public static final HarvestData LEVEL_6_PICKAXE = new HarvestData(6);
	
	public static final HarvestData SPACE_STONE_HARVEST_DATA = DIAMOND_PICKAXE;
	
	public static final HarvestData PIPE_AND_CABLE_HARVEST_DATA = STONE_PICKAXE;
	
	public static final HarvestData PRIMITIVE_MACHINE_HARVEST_DATA = STONE_PICKAXE;
	public static final HarvestData BASIC_MACHINE_HARVEST_DATA = IRON_PICKAXE;
	public static final HarvestData ADVANCED_MACHINE_HARVEST_DATA = IRON_PICKAXE;
	public static final HarvestData ELITE_MACHINE_HARVEST_DATA = NETHERITE_PICKAXE;
	public static final HarvestData MISC_MACHINE_HARVEST_DATA = IRON_PICKAXE;
	
	public HarvestData(TagKey<Block> mineableTag) {
		this(mineableTag, MiningLevels.WOOD);
	}
	
	public HarvestData(int miningLevel) {
		this(BlockTags.PICKAXE_MINEABLE, miningLevel);
	}
	
	public TagKey<Block> miningLevelTag() {
		return MiningLevelManager.getBlockTag(miningLevel());
	}
}
