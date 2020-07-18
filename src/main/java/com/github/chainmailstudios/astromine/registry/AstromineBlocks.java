package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

import net.minecraft.block.Block;
import net.minecraft.block.MagmaBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.AstromineOreBlock;
import com.github.chainmailstudios.astromine.common.block.BlockBreakerBlock;
import com.github.chainmailstudios.astromine.common.block.BlockPlacerBlock;
import com.github.chainmailstudios.astromine.common.block.CreativeBufferBlock;
import com.github.chainmailstudios.astromine.common.block.CreativeCapacitorBlock;
import com.github.chainmailstudios.astromine.common.block.CreativeTankBlock;
import com.github.chainmailstudios.astromine.common.block.ElectricSmelterBlock;
import com.github.chainmailstudios.astromine.common.block.ElectrolyzerBlock;
import com.github.chainmailstudios.astromine.common.block.EnergyCableBlock;
import com.github.chainmailstudios.astromine.common.block.FluidCableBlock;
import com.github.chainmailstudios.astromine.common.block.FluidExtractorBlock;
import com.github.chainmailstudios.astromine.common.block.FluidInserterBlock;
import com.github.chainmailstudios.astromine.common.block.FluidMixerBlock;
import com.github.chainmailstudios.astromine.common.block.HolographicBridgeInvisibleBlock;
import com.github.chainmailstudios.astromine.common.block.HolographicBridgeProjectorBlock;
import com.github.chainmailstudios.astromine.common.block.LiquidGeneratorBlock;
import com.github.chainmailstudios.astromine.common.block.NuclearWarheadBlock;
import com.github.chainmailstudios.astromine.common.block.PresserBlock;
import com.github.chainmailstudios.astromine.common.block.SolidGeneratorBlock;
import com.github.chainmailstudios.astromine.common.block.TankBlock;
import com.github.chainmailstudios.astromine.common.block.TrituratorBlock;
import com.github.chainmailstudios.astromine.common.block.VentBlock;

public class AstromineBlocks {
	public static final Block HOLOGRAPHIC_BRIDGE_PROJECTOR = register("holographic_bridge_projector", new HolographicBridgeProjectorBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(4, 16).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK = register("holographic_bridge_invisible", new HolographicBridgeInvisibleBlock(FabricBlockSettings.of(HolographicBridgeInvisibleBlock.MATERIAL).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().lightLevel(15).allowsSpawning((a, b, c, d) -> false)));

	public static final Block VENT = register("vent", new VentBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(4, 16).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block FLUID_TANK = register("tank", new TankBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(4, 16).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block LIQUID_GENERATOR = register("liquid_generator", new LiquidGeneratorBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(4, 16).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block SOLID_GENERATOR = register("solid_generator", new SolidGeneratorBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(4, 16).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block FLUID_CABLE = register("fluid_cable", new FluidCableBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block ENERGY_CABLE = register("energy_cable", new EnergyCableBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(1F, 1.5F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block NUCLEAR_WARHEAD = register("nuclear_warhead", new NuclearWarheadBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(1, 4F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block CREATIVE_TANK = register("creative_tank", new CreativeTankBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1, 3600000.8F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block CREATIVE_CAPACITOR = register("creative_capacitor", new CreativeCapacitorBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(-1.0F, 3600000.8F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block CREATIVE_BUFFER = register("creative_buffer", new CreativeBufferBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(-1.0F, 3600000.8F).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());

	public static final Block PRESSER = register("presser", new PresserBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1F, 4F).breakByTool(FabricToolTags.PICKAXES, 4).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block TRITURATOR = register("triturator", new TrituratorBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1F, 4F).breakByTool(FabricToolTags.PICKAXES, 4).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block ELECTRIC_SMELTER = register("electric_smelter", new ElectricSmelterBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1, 4F).breakByTool(FabricToolTags.PICKAXES, 4).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block FLUID_EXTRACTOR = register("fluid_extractor", new FluidExtractorBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1F, 4F).breakByTool(FabricToolTags.PICKAXES, 4).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block FLUID_INSERTER = register("fluid_inserter", new FluidInserterBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1F, 4F).breakByTool(FabricToolTags.PICKAXES, 4).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block BLOCK_BREAKER = register("block_breaker", new BlockBreakerBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1F, 4F).breakByTool(FabricToolTags.PICKAXES, 4).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block BLOCK_PLACER = register("block_placer", new BlockPlacerBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1F, 4F).breakByTool(FabricToolTags.PICKAXES, 4).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block ELECTROLYZER = register("electrolyzer", new ElectrolyzerBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1F, 4F).breakByTool(FabricToolTags.PICKAXES, 4).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block FLUID_MIXER = register("fluid_mixer", new FluidMixerBlock(FabricBlockSettings.of(Material.METAL).dropsNothing().strength(1F, 4F).breakByTool(FabricToolTags.PICKAXES, 4).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	
	public static final Block COPPER_ORE = register("copper_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 3).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block TIN_ORE = register("tin_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(3, 3).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	
	public static final Block METEOR_METITE_ORE = register("meteor_metite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_METITE_ORE = register("asteroid_metite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_ASTERITE_ORE = register("asteroid_asterite_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(40, 1000).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STELLUM_ORE = register("asteroid_stellum_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 80).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_GALAXIUM_ORE = register("asteroid_galaxium_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(80, 1300).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_COPPER_ORE = register("asteroid_copper_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_TIN_ORE = register("asteroid_tin_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	
	public static final Block ASTEROID_COAL_ORE = register("asteroid_coal_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_IRON_ORE = register("asteroid_iron_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_GOLD_ORE = register("asteroid_gold_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_REDSTONE_ORE = register("asteroid_redstone_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_LAPIS_ORE = register("asteroid_lapis_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_DIAMOND_ORE = register("asteroid_diamond_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_EMERALD_ORE = register("asteroid_emerald_ore", new AstromineOreBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(15, 100).sounds(BlockSoundGroup.STONE)), AstromineItems.getBasicSettings().fireproof());
	
	public static final Block METITE_BLOCK = register("metite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BLUE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(8, 100).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block ASTERITE_BLOCK = register("asterite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.RED).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(25, 1000).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block STELLUM_BLOCK = register("stellum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.YELLOW).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(10, 80).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings().fireproof());
	public static final Block GALAXIUM_BLOCK = register("galaxium_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.LIME).requiresTool().breakByTool(FabricToolTags.PICKAXES, 5).strength(50, 1300).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block UNIVITE_BLOCK = register("univite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.PURPLE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 6).strength(80, 2000).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings().fireproof());
	
	public static final Block COPPER_BLOCK = register("copper_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(4, 6).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block TIN_BLOCK = register("tin_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.LIGHT_GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1).strength(4, 6).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block STEEL_BLOCK = register("steel_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 6).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	public static final Block BRONZE_BLOCK = register("bronze_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(6, 6).sounds(BlockSoundGroup.METAL)), AstromineItems.getBasicSettings());
	
	public static final Block METEOR_STONE = register("meteor_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.BLACK).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(30, 1500)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE = register("asteroid_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50, 1500)), AstromineItems.getBasicSettings().fireproof());
	public static final Block BLAZING_ASTEROID_STONE = register("blazing_asteroid_stone", new MagmaBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50, 1500).lightLevel((state) -> 3).ticksRandomly().allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune()).postProcess((state, world, pos) -> true).emissiveLighting((state, world, pos) -> true)), AstromineItems.getBasicSettings().fireproof());

	public static final Block METEOR_STONE_SLAB = register("meteor_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE_SLAB = register("asteroid_stone_slab", new SlabBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineItems.getBasicSettings().fireproof());

	public static final Block METEOR_STONE_STAIRS = register("meteor_stone_stairs", new StairsBlock(METEOR_STONE.getDefaultState(), FabricBlockSettings.copyOf(METEOR_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE_STAIRS = register("asteroid_stone_stairs", new StairsBlock(ASTEROID_STONE.getDefaultState(), FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineItems.getBasicSettings().fireproof());

	public static final Block METEOR_STONE_WALL = register("meteor_stone_wall", new WallBlock(FabricBlockSettings.copyOf(METEOR_STONE)), AstromineItems.getBasicSettings().fireproof());
	public static final Block ASTEROID_STONE_WALL = register("asteroid_stone_wall", new WallBlock(FabricBlockSettings.copyOf(ASTEROID_STONE)), AstromineItems.getBasicSettings().fireproof());

	public static void initialize() {
		// Unused.
	}

	/**
	 * @param name     Name of block instance to be registered
	 * @param block    Block instance to be registered
	 * @param settings Item.Settings of BlockItem of Block instance to be registered
	 * @return Block instance registered
	 */
	public static <T extends Block> T register(String name, T block, Item.Settings settings) {
		return register(name, block, new BlockItem(block, settings));
	}

	/**
	 * @param name  Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @param item  BlockItem instance of Block to be registered
	 * @return Block instance registered
	 */
	public static <T extends Block> T register(String name, T block, BlockItem item) {
		T b = register(AstromineCommon.identifier(name), block);
		if (item != null) {
			AstromineItems.register(name, item);
		}
		return b;
	}

	/**
	 * @param name  Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @return Block instance registered
	 */
	public static <T extends Block> T register(String name, T block) {
		return register(AstromineCommon.identifier(name), block);
	}

	/**
	 * @param name  Identifier of block instance to be registered
	 * @param block Block instance to be registered
	 * @return Block instance registered
	 */
	public static <T extends Block> T register(Identifier name, T block) {
		return Registry.register(Registry.BLOCK, name, block);
	}
}
