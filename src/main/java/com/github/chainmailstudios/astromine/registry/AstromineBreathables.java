/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.BreathableRegistry;
import net.minecraft.entity.EntityType;

public class AstromineBreathables {
	public static void initialize() {
		BreathableRegistry.INSTANCE.register(EntityType.COD, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.DOLPHIN, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.DROWNED, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ELDER_GUARDIAN, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.GUARDIAN, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.HUSK, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.MAGMA_CUBE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PHANTOM, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PUFFERFISH, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SALMON, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SKELETON, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SKELETON_HORSE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SQUID, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.STRAY, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.STRIDER, AstromineTags.NORMAL_BREATHABLE, AstromineTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.TROPICAL_FISH, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.TURTLE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WITHER, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WITHER_SKELETON, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOGLIN, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE_HORSE, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE_VILLAGER, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIFIED_PIGLIN, AstromineTags.NORMAL_BREATHABLE, AstromineTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PLAYER, AstromineTags.NORMAL_BREATHABLE);
	}
}
