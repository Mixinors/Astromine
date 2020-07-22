package com.github.chainmailstudios.astromine.common.world.feature;

import com.github.chainmailstudios.astromine.common.miscellaneous.OpenSimplexNoise;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineFeatures;
import com.terraformersmc.shapes.api.Position;
import com.terraformersmc.shapes.api.Quaternion;
import com.terraformersmc.shapes.api.Shape;
import com.terraformersmc.shapes.impl.Shapes;
import com.terraformersmc.shapes.impl.layer.transform.RotateLayer;
import com.terraformersmc.shapes.impl.layer.transform.TranslateLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceWithDimensions;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MeteorGenerator extends StructurePieceWithDimensions {

	private static final OpenSimplexNoise noise = new OpenSimplexNoise();

	public MeteorGenerator(Random random, int x, int z) {
		super(AstromineFeatures.METEOR, random, x, 64, z, 16, 16, 16);
	}

	public MeteorGenerator(StructureManager manager, CompoundTag tag) {
		super(AstromineFeatures.METEOR, tag);
	}

	@Override
	public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
		return generate(world, chunkPos, random, blockPos);
	}

	public boolean generate(ServerWorldAccess world, ChunkPos chunkPos, Random random, BlockPos blockPos) {
		BlockPos originPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(chunkPos.getStartX() + 8, 0, chunkPos.getStartZ() + 8));
		emptySphere(
				world,
				originPos,
				30,
				state -> {
					if (world.getRandom().nextInt(10) == 0) {
						return Blocks.FIRE.getDefaultState();
					} else {
						return Blocks.AIR.getDefaultState();
					}
				},
				state -> Blocks.COBBLESTONE.getDefaultState()
		);

		originPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(chunkPos.getStartX() + 8, 0, chunkPos.getStartZ() + 8));
		buildSphere(world, originPos, 15, AstromineBlocks.METEOR_STONE.getDefaultState());

		Shape vein = Shapes.ellipsoid((float) 4, (float) 4, (float) 4)
				.applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 360, random.nextDouble() * 360, random.nextDouble() * 360, true)))
				.applyLayer(TranslateLayer.of(Position.of(originPos)));

		for (Position streamPosition : vein.stream().collect(Collectors.toSet())) {
			BlockPos orePosition = streamPosition.toBlockPos();

			if (world.getBlockState(orePosition).getBlock() == AstromineBlocks.METEOR_STONE) {
				world.setBlockState(orePosition, AstromineBlocks.METEOR_METITE_ORE.getDefaultState(), 0b0110100);
			}
		}

		return true;
	}

	private void emptySphere(ServerWorldAccess world, BlockPos originPos, int radius, GroundManipulator bottom, GroundManipulator underneath) {
		List<BlockPos> placedPositions = new ArrayList<>();

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -radius; y <= radius; y++) {
					double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y, 2));

					// place blocks within spherical radius
					if (distance <= radius + (5 * noise.eval((originPos.getX() + x) / 10f, (originPos.getZ() + z) / 10f))) {
						BlockPos offsetPos = originPos.add(x, y, z);

						world.setBlockState(
								offsetPos,
								Blocks.AIR.getDefaultState(),
								3
						);

						placedPositions.add(offsetPos);
					}
				}
			}
		}

		List<BlockPos> bottomPositions = new ArrayList<>();
		List<BlockPos> underneathPositions = new ArrayList<>();

		for (BlockPos pos : placedPositions) {
			// store bottom block
			if (world.getBlockState(pos).isAir() && !world.getBlockState(pos.down()).isAir()) {
				bottomPositions.add(pos);
				underneathPositions.add(pos.down());
			}
		}

		for (BlockPos pos : bottomPositions) {
			world.setBlockState(pos, bottom.manipulate(world.getBlockState(pos)), 3);
		}

		for (BlockPos pos : underneathPositions) {
			world.setBlockState(pos, underneath.manipulate(world.getBlockState(pos)), 3);
		}
	}

	public static void buildSphere(ServerWorldAccess world, BlockPos originPos, int radius, BlockState state) {
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -radius; y <= radius; y++) {
					double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y, 2));

					// place blocks within spherical radius
					if (distance <= radius - ((radius * 1f / 3f) * noise.eval((originPos.getX() + x) / 10f, (originPos.getY() + y) / 10f, (originPos.getZ() + z) / 10f))) {
						world.setBlockState(
								originPos.add(x, y, z),
								state,
								3
						);
					}
				}
			}
		}
	}

	@FunctionalInterface
	public interface GroundManipulator {
		BlockState manipulate(BlockState state);
	}
}
