package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.bridge.HolographicBridgeManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class HolographicBridgeInvisibleBlock extends Block {
	public static final Block.Settings SETTINGS = FabricBlockSettings.of(Material.AIR).dropsNothing().strength(-1.0F, 3600000.8F).nonOpaque().allowsSpawning((a, b, c, d) -> false);

	public HolographicBridgeInvisibleBlock() {
		super(SETTINGS);
	}

	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Environment(EnvType.CLIENT)
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos position, ShapeContext context) {
		return HolographicBridgeManager.getShape(position);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos position, ShapeContext context) {
		return getOutlineShape(state, world, position, context);
	}
}
