/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.registry.common;

import net.minecraft.entity.EntityType;

import com.github.mixinors.astromine.common.registry.BreathableRegistry;

public class AMBreathables {
	public static void init() {
		BreathableRegistry.INSTANCE.register(EntityType.COD, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.DOLPHIN, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.DROWNED, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ELDER_GUARDIAN, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.GUARDIAN, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.HUSK, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.MAGMA_CUBE, AMTags.NORMAL_BREATHABLE, AMTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PHANTOM, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PUFFERFISH, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SALMON, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SKELETON, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SKELETON_HORSE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SQUID, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.STRAY, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.STRIDER, AMTags.NORMAL_BREATHABLE, AMTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.TROPICAL_FISH, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.TURTLE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WITHER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WITHER_SKELETON, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOGLIN, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE_HORSE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE_VILLAGER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIFIED_PIGLIN, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PLAYER, AMTags.NORMAL_BREATHABLE);
	}
}
