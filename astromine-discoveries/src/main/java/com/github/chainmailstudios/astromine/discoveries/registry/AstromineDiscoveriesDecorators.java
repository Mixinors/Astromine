package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.discoveries.common.world.decorator.MoonOreDecorator;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;

public class AstromineDiscoveriesDecorators {
	public static Decorator<CountConfig> MOON_ORE = register("moon_ore_decorator", new MoonOreDecorator(CountConfig.CODEC));


	public static void initialize() {

	}

	private static <T extends DecoratorConfig, G extends Decorator<T>> G register(String name, G decorator) {
		return Registry.register(Registry.DECORATOR, AstromineCommon.identifier(name), decorator);
	}
}
