package com.github.mixinors.astromine.common.registry;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;

public class AMFComponents implements WorldComponentInitializer, ChunkComponentInitializer, ItemComponentInitializer, EntityComponentInitializer, BlockComponentInitializer {
	
	
	public static void initialize() {
	
	}
	
	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
	
	}
	
	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
	
	}
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
	
	}
	
	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
	
	}
	
	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
	
	}
}
