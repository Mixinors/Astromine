package com.github.chainmailstudios.astromine.mixin;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.github.chainmailstudios.astromine.common.world.generation.EarthSpaceBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.EarthSpaceChunkGenerator;
import com.github.chainmailstudios.astromine.common.world.generation.MoonBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.MoonChunkGenerator;
import com.mojang.datafixers.DataFixer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {

	@Mutable
	@Shadow @Final private ChunkGenerator chunkGenerator;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void handleConstructor(ServerWorld world, LevelStorage.Session session, DataFixer dataFixer, StructureManager structureManager, Executor workerExecutor, ChunkGenerator chunkGenerator, int viewDistance, boolean bl, WorldGenerationProgressListener worldGenerationProgressListener, Supplier<PersistentStateManager> supplier, CallbackInfo ci) {
		if (chunkGenerator instanceof EarthSpaceChunkGenerator) {
			this.chunkGenerator = new EarthSpaceChunkGenerator(new EarthSpaceBiomeSource(world.getSeed()), world.getSeed());
		}
		if (chunkGenerator instanceof MoonChunkGenerator) {
			this.chunkGenerator = new MoonChunkGenerator(new MoonBiomeSource(world.getSeed()), world.getSeed());
		}
	}
}
