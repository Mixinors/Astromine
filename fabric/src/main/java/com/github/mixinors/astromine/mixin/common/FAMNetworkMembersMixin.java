package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.registry.common.AMNetworkMembers;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AMNetworkMembers.class)
public abstract class FAMNetworkMembersMixin {
	@Shadow
	public static void acceptBlock(RegistryKey<Block> id, Block block) {}
	
	@Inject(at = @At("TAIL"), method = "init")
	private static void astromine_init(CallbackInfo ci) {
		RegistryEntryAddedCallback.event(Registry.BLOCK).register((index, identifier, block) -> acceptBlock(RegistryKey.of(Registry.BLOCK_KEY, identifier), block));
	}
}
