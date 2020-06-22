package com.github.chainmailstudios.astromine.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class MeteorFeature extends StructureFeature<DefaultFeatureConfig> {

	public MeteorFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	public StructureFeature.StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
		return MeteorFeature.Start::new;
	}

	public static class Start extends StructureStart<DefaultFeatureConfig> {

		public Start(StructureFeature<DefaultFeatureConfig> structureFeature, int i, int j, BlockBox blockBox, int k, long l) {
			super(structureFeature, i, j, blockBox, k, l);
		}

		public void init(ChunkGenerator chunkGenerator, StructureManager structureManager, int i, int j, Biome biome, DefaultFeatureConfig defaultFeatureConfig) {
			MeteorGenerator meteorGenerator = new MeteorGenerator(this.random, i * 16, j * 16);
			this.children.add(meteorGenerator);
			this.setBoundingBoxFromChildren();
		}
	}
}
