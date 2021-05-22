package com.github.mixinors.astromine.common.resource;

import com.github.mixinors.astromine.techreborn.common.util.ClientUtils;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.texture.Sprite;
import net.minecraft.resource.ResourceManager;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public record ExtendedFluidResourceListener(Sprite[] sprites,
                                            Identifier id,
                                            Identifier flowingSpriteId,
                                            Identifier stillSpriteId) implements SimpleSynchronousResourceReloadListener {
	@Override
	public Identifier getFabricId() {
		return id;
	}
	
	@Override
	public void apply(ResourceManager resourceManager) {
		var atlas = ClientUtils.getInstance().getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
		
		sprites[0] = atlas.apply(stillSpriteId);
		sprites[1] = atlas.apply(flowingSpriteId);
	}
}
