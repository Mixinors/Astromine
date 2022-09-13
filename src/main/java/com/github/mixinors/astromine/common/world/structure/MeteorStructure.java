/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class MeteorStructure extends Structure {
	public static final Codec<MeteorStructure> CODEC = createCodec(MeteorStructure::new);
	
	public static final StructureType<MeteorStructure> TYPE = () -> CODEC;
	
	public MeteorStructure(Config config) {
		super(config);
	}
	
	private static void addPieces(StructurePiecesCollector collector, Context context) {
		collector.addPiece(new MeteorGenerator(context.random(), context.chunkPos().getStartX(), context.chunkPos().getStartZ()));
	}
	
	@Override
	public GenerationStep.Feature getFeatureGenerationStep() {
		return GenerationStep.Feature.SURFACE_STRUCTURES;
	}
	
	@Override
	public Optional<StructurePosition> getStructurePosition(Context context) {
		return getStructurePosition(context, Heightmap.Type.WORLD_SURFACE_WG, collector -> addPieces(collector, context));
	}
	
	@Override
	public StructureType<?> getType() {
		return TYPE;
	}
}
