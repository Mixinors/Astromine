package com.github.mixinors.astromine.mixin.client;

import com.mojang.datafixers.util.Either;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

@Mixin(BlockModel.class)
public interface JsonUnbakedModelAccessor {
	@Accessor(value = "parent", remap = false)
	BlockModel getParent();
	
	@Accessor(value = "parentLocation", remap = false)
	ResourceLocation getParentLocation();
	
	@Accessor(value = "textureMap", remap = false)
	Map<String, Either<Material, String>> getTextureMap();
	
}
