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
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureType;

import java.util.function.Supplier;

public class AMStructureTypes {
	public static final Identifier METEOR_ID = AMCommon.id("meteor");
	public static final RegistrySupplier<StructureType<?>> METEOR_STRUCTURE_TYPE = registerStructureType(METEOR_ID, () -> (StructureType<MeteorStructure>) () -> MeteorStructure.CODEC);
	public static final RegistrySupplier<StructurePieceType> METEOR_STRUCTURE_PIECE = registerStructurePiece(METEOR_ID, () -> (StructurePieceType.Simple) MeteorGenerator::new);
	
	public static final Identifier CRATER_ID = AMCommon.id("crater");
	public static final RegistrySupplier<StructureType<?>> CRATER_STRUCTURE_TYPE = registerStructureType(CRATER_ID, () -> (StructureType<CraterStructure>) () -> CraterStructure.CODEC);
	public static final RegistrySupplier<StructurePieceType> CRATER_STRUCTURE_PIECE = registerStructurePiece(CRATER_ID, () -> (StructurePieceType.Simple) CraterGenerator::new);
	
	public static void init() {
	}
	
	public static <T extends StructureType<?>> RegistrySupplier<T> registerStructureType(Identifier id, Supplier<T> type) {
		return AMCommon.registry(RegistryKeys.STRUCTURE_TYPE).register(id, type);
	}
	
	public static <T extends StructurePieceType> RegistrySupplier<T> registerStructurePiece(Identifier id, Supplier<T> pieceType) {
		return AMCommon.registry(RegistryKeys.STRUCTURE_PIECE).register(id, pieceType);
	}
}
