package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.access.WorldChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin {
	// change Integer to the object of your choice
	@Mixin(WorldChunk.class)
	public static class WorldChunkMixin implements WorldChunkAccess {
		@Shadow @Final private World world;
		@Shadow @Final private ChunkPos pos;
		private WorldChunk east,west,north,south;
		private Runnable unload;

		@Override
		public void astromine_addUnloadListener(Runnable runnable) {
			if(this.unload == null) {
				this.unload = runnable;
			} else {
				Runnable run = this.unload;
				this.unload = () -> {run.run(); runnable.run();};
			}
		}

		@Override
		public void astromine_runUnloadListeners() {
			if(this.unload != null) this.unload.run();
		}

		@Override
		public WorldChunk astromine_east() {
			WorldChunk chunk = this.east;
			if(chunk == null) {
				ChunkPos pos = this.pos;
				chunk = this.east = this.world.getChunk(pos.x + 1, pos.z);
				((WorldChunkAccess)chunk).astromine_addUnloadListener(() -> this.east = null);
			}

			return chunk;
		}

		@Override
		public WorldChunk astromine_west() {
			WorldChunk chunk = this.west;
			if(chunk == null) {
				ChunkPos pos = this.pos;
				chunk = this.west = this.world.getChunk(pos.x - 1, pos.z);
				((WorldChunkAccess)chunk).astromine_addUnloadListener(() -> this.west = null);
			}

			return chunk;
		}

		@Override
		public WorldChunk astromine_north() {
			WorldChunk chunk = this.north;
			if(chunk == null) {
				ChunkPos pos = this.pos;
				chunk = this.north = this.world.getChunk(pos.x, pos.z-1);
				((WorldChunkAccess)chunk).astromine_addUnloadListener(() -> this.north = null);
			}

			return chunk;
		}

		@Override
		public WorldChunk astromine_south() {
			WorldChunk chunk = this.south;
			if(chunk == null) {
				ChunkPos pos = this.pos;
				chunk = this.south = this.world.getChunk(pos.x, pos.z+1);
				((WorldChunkAccess)chunk).astromine_addUnloadListener(() -> this.south = null);
			}

			return chunk;
		}
	}


	@Inject(method = "serialize", at = @At("RETURN"))
	private static void serialize(ServerWorld serverWorld, Chunk chunk, CallbackInfoReturnable<CompoundTag> cir) {
		if(chunk instanceof WorldChunkAccess) {
			((WorldChunkAccess)chunk).astromine_runUnloadListeners();
		}
	}
}
