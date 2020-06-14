package com.github.chainmailstudios.astromine.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;

import static com.github.chainmailstudios.astromine.AstromineCommon.identifier;

public class AstrominePotions {
	public static final Potion ROCKET_FUEL = Registry.register(Registry.POTION, identifier("rocket_fuel"), new Potion(
			new StatusEffectInstance(StatusEffects.POISON, 20, 0),
			new StatusEffectInstance(StatusEffects.WITHER, 20, 0),
			new StatusEffectInstance(StatusEffects.NAUSEA, 72000, 10)
	));

	public static final Potion SUGAR_WATER = Registry.register(Registry.POTION, identifier("sugar_water"), new Potion(
			new StatusEffectInstance(StatusEffects.SPEED, 20, 2),
			new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 1)
	));

	public static void initialize() {
	}
}
