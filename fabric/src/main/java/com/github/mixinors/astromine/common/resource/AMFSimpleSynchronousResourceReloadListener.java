package com.github.mixinors.astromine.common.resource;

import com.github.mixinors.astromine.common.util.ClientUtils;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.texture.Sprite;
import net.minecraft.resource.ResourceManager;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class AMFSimpleSynchronousResourceReloadListener implements SimpleSynchronousResourceReloadListener {
	private final Sprite[] sprites;
	
	private final Identifier id;
	
	private final Identifier flowingSpriteId;
	private final Identifier stillSpriteId;
	
	public AMFSimpleSynchronousResourceReloadListener(Sprite[] sprites, Identifier id, Identifier flowingSpriteId, Identifier stillSpriteId) {
		this.sprites = sprites;
		this.id = id;
		this.flowingSpriteId = flowingSpriteId;
		this.stillSpriteId = stillSpriteId;
	}
	
	@Override
	public Identifier getFabricId() {
		return id;
	}
	
	@Override
	public void apply(ResourceManager resourceManager) {
		Function<Identifier, Sprite> atlas = ClientUtils.getInstance().getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
		sprites[0] = atlas.apply(stillSpriteId);
		sprites[1] = atlas.apply(flowingSpriteId);
	}
}
