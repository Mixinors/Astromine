package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.callback.ServerChunkTickCallback;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(method = "tickChunk", at = @At("HEAD"))
	private void tickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		ServerChunkTickCallback.EVENT.invoker().tickChunk((ServerWorld) (Object) this, chunk);
	}
}
