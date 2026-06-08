package com.github.mixinors.astromine.common.gravity;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GravityManager {
	public static final double DEFAULT_GRAVITY = LivingEntity.DEFAULT_BASE_GRAVITY;
	public static final ResourceLocation DIMENSION_GRAVITY_MODIFIER = AMCommon.id("dimension_gravity");

	private static final Map<ResourceKey<Level>, Double> GRAVITY_BY_LEVEL = new ConcurrentHashMap<>();

	private GravityManager() {
	}

	public static double get(ResourceKey<Level> level) {
		return GRAVITY_BY_LEVEL.getOrDefault(level, DEFAULT_GRAVITY);
	}

	public static void set(ResourceKey<Level> level, double gravity) {
		GRAVITY_BY_LEVEL.put(level, gravity);
	}

	public static void reset(ResourceKey<Level> level) {
		GRAVITY_BY_LEVEL.remove(level);
	}

	public static void clear() {
		GRAVITY_BY_LEVEL.clear();
	}

	public static void apply(LivingEntity entity) {
		var gravity = get(entity.level().dimension());
		var instance = entity.getAttribute(Attributes.GRAVITY);

		if (instance == null) {
			return;
		}

		if (Double.compare(gravity, DEFAULT_GRAVITY) == 0) {
			instance.removeModifier(DIMENSION_GRAVITY_MODIFIER);
			return;
		}

		applyModifier(instance, gravity - DEFAULT_GRAVITY);
	}

	private static void applyModifier(AttributeInstance instance, double amount) {
		var current = instance.getModifier(DIMENSION_GRAVITY_MODIFIER);

		if (current != null && Double.compare(current.amount(), amount) == 0 && current.operation() == AttributeModifier.Operation.ADD_VALUE) {
			return;
		}

		instance.addOrUpdateTransientModifier(new AttributeModifier(DIMENSION_GRAVITY_MODIFIER, amount, AttributeModifier.Operation.ADD_VALUE));
	}
}
