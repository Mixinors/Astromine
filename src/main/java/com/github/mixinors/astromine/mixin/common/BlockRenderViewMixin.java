package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.registry.common.AMBiomes;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderView.class)
public interface BlockRenderViewMixin {
	@Inject(at = @At("HEAD"), method = "getLightLevel", cancellable = true)
	private void astromine$getLightLevel(LightType type, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		var client = InstanceUtil.getClient();
		
		if (client.player != null && client.world != null) {
			if (client.world.getBiome(client.player.getBlockPos()).getKey().orElseThrow().equals(AMBiomes.MOON_DARK_SIDE_KEY)) {
				cir.setReturnValue(0);
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "getBaseLightLevel", cancellable = true)
	private void astromine$getBaseLightLevel(BlockPos pos, int ambientDarkness, CallbackInfoReturnable<Integer> cir) {
		var client = InstanceUtil.getClient();
		
		if (client.player != null && client.world != null) {
			if (client.world.getBiome(client.player.getBlockPos()).getKey().orElseThrow().equals(AMBiomes.MOON_DARK_SIDE_KEY)) {
				cir.setReturnValue(0);
			}
		}
	}
}
