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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.world.structure.CraterGenerator;
import com.github.mixinors.astromine.common.world.structure.CraterStructure;
import com.github.mixinors.astromine.common.world.structure.MeteorGenerator;
import com.github.mixinors.astromine.common.world.structure.MeteorStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class AMStructures {
	public static final ResourceLocation METEOR_ID = AMCommon.id("meteor");
	public static final ResourceLocation CRATER_ID = AMCommon.id("crater");
	
	private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, AMCommon.MOD_ID);
	private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, AMCommon.MOD_ID);
	
	public static final DeferredHolder<StructureType<?>, StructureType<MeteorStructure>> METEOR_STRUCTURE_TYPE = STRUCTURE_TYPES.register(METEOR_ID.getPath(), () -> MeteorStructure.TYPE);
	public static final DeferredHolder<StructurePieceType, StructurePieceType> METEOR_STRUCTURE_PIECE = registerStructurePiece(METEOR_ID, () -> (StructurePieceType.ContextlessType) MeteorGenerator::new);
	
	public static final DeferredHolder<StructureType<?>, StructureType<CraterStructure>> CRATER_STRUCTURE_TYPE = STRUCTURE_TYPES.register(CRATER_ID.getPath(), () -> CraterStructure.TYPE);
	public static final DeferredHolder<StructurePieceType, StructurePieceType> CRATER_STRUCTURE_PIECE = registerStructurePiece(CRATER_ID, () -> (StructurePieceType.ContextlessType) CraterGenerator::new);
	
	public static void init() {
		STRUCTURE_TYPES.register(AMCommon.modEventBus());
		STRUCTURE_PIECES.register(AMCommon.modEventBus());
	}
	
	public static <T extends StructurePieceType> DeferredHolder<StructurePieceType, T> registerStructurePiece(ResourceLocation id, Supplier<T> pieceType) {
		return STRUCTURE_PIECES.register(id.getPath(), pieceType);
	}
}
