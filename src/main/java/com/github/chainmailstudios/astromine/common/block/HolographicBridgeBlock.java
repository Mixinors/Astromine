package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.HolographicBridgeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.world.BlockView;

public class HolographicBridgeBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public static final AbstractBlock.Settings SETTINGS = FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 4).strength(4, 16).sounds(BlockSoundGroup.METAL);

	public HolographicBridgeBlock() {
		super(SETTINGS);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.appendProperties(builder);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new HolographicBridgeBlockEntity();
	}
}
