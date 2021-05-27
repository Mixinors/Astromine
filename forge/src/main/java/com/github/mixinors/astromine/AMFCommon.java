package com.github.mixinors.astromine;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AMCommon.MOD_ID)
public class AMFCommon {
	public AMFCommon() {
		EventBuses.registerModEventBus(AMCommon.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	@SubscribeEvent
	private void init(FMLCommonSetupEvent event) {
		AMCommon.init();
	}
	
	@SubscribeEvent
	private void initClient(FMLClientSetupEvent event) {
		AMClient.init();
	}
	
	@SubscribeEvent
	private void initDedicated(FMLDedicatedServerSetupEvent event) {
		AMDedicated.init();
	}
}
