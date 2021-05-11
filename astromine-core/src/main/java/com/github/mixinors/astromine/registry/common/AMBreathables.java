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

import net.minecraft.entity.*;

import com.github.mixinors.astromine.common.registry.BreathableRegistry;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.entity.vehicle.*;

public class AMBreathables {
	public static void init() {
		BreathableRegistry.INSTANCE.register(EntityType.BAT, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.BEE, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.BLAZE, AMTags.NORMAL_BREATHABLE, AMTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.CAT, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.CAVE_SPIDER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.CHICKEN, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.COD, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.COW, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.CREEPER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.DOLPHIN, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.DONKEY, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.DROWNED, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ELDER_GUARDIAN, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ENDERMAN, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ENDERMITE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.EVOKER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.FOX, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.GHAST, AMTags.NORMAL_BREATHABLE, AMTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.GIANT, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.GUARDIAN, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.HOGLIN, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.HORSE, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.HUSK, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ILLUSIONER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.IRON_GOLEM, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.LLAMA, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.MAGMA_CUBE, AMTags.NORMAL_BREATHABLE, AMTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.MULE, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.OCELOT, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PANDA, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PARROT, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PHANTOM, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PIG, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PIGLIN, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PIGLIN_BRUTE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PILLAGER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.POLAR_BEAR, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PUFFERFISH, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.RABBIT, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.RAVAGER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SALMON, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SHEEP, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SHULKER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SILVERFISH, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SKELETON, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SKELETON_HORSE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SLIME, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SNOW_GOLEM, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SPIDER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.SQUID, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.STRAY, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.STRIDER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE, AMTags.LAVA_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.TRADER_LLAMA, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.TROPICAL_FISH, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.TURTLE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.VEX, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.VILLAGER, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.VINDICATOR, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WANDERING_TRADER, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WITCH, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WITHER_SKELETON, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.WOLF, AMTags.NORMAL_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOGLIN, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE_HORSE, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIE_VILLAGER, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.ZOMBIFIED_PIGLIN, AMTags.NORMAL_BREATHABLE, AMTags.WATER_BREATHABLE);
		BreathableRegistry.INSTANCE.register(EntityType.PLAYER, AMTags.NORMAL_BREATHABLE);
	}
}
