package com.github.mixinors.astromine.registry.common;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.targets.ArchitecturyTarget;

public class AMContainerInfoHandlers {
	public static void init() {
		postInit();
	}
	
	@ExpectPlatform
	public static void postInit() {
		throw new AssertionError();
	}
}
