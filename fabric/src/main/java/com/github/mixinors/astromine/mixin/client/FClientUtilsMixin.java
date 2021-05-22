package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.fluid.render.AMFFluidRenderHandler;
import com.github.mixinors.astromine.common.resource.AMFSimpleSynchronousResourceReloadListener;
import com.github.mixinors.astromine.common.util.ClientUtils;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientUtils.class)
public class FClientUtilsMixin {
	@Overwrite
	@SuppressWarnings("all")
	public static void registerExtendedFluid(String name, int tint, Fluid still, Fluid flowing) {
		var stillSpriteId = new Identifier("block/water_still");
		var flowingSpriteId = new Identifier("block/water_flow");
		var id = AMCommon.id(name + "_reload_listener");
		
		var sprites = new Sprite[2];
		
		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(($, registry) -> {
			registry.register(stillSpriteId);
			registry.register(flowingSpriteId);
		});
		
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new AMFSimpleSynchronousResourceReloadListener(sprites, id, flowingSpriteId, stillSpriteId));
		
		var handler = new AMFFluidRenderHandler(sprites, tint);
		
		FluidRenderHandlerRegistry.INSTANCE.register(still, handler);
		FluidRenderHandlerRegistry.INSTANCE.register(flowing, handler);
	}
}
