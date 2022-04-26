/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.registry;

import java.util.Optional;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.registry.base.BiRegistry;
import com.github.mixinors.astromine.mixin.common.EntityMixin;
import com.github.mixinors.astromine.mixin.common.LivingEntityMixin;
import com.github.mixinors.astromine.mixin.common.gravity.AbstractMinecartEntityMixin;
import com.github.mixinors.astromine.mixin.common.gravity.EggEntityMixin;
import com.github.mixinors.astromine.mixin.common.gravity.FishingBobberEntityMixin;
import com.github.mixinors.astromine.mixin.common.gravity.GravityEntityMixin;
import com.github.mixinors.astromine.mixin.common.gravity.HoneyBlockMixin;
import com.github.mixinors.astromine.mixin.common.gravity.ItemEntityMixin;
import com.github.mixinors.astromine.mixin.common.gravity.SquidEntityMixin;
import com.github.mixinors.astromine.mixin.common.gravity.StepAndDestroyBlockGoalMixin;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

/**
 * A {@link BiRegistry} for registration of
 * {@link RegistryKey<World>}s mapped to {@link Double}s.
 *
 * The registered gravity value will then be used for gravity
 * calculations, through:
 *
 * - {@link AbstractMinecartEntityMixin}
 * - {@link EggEntityMixin}
 * - {@link EntityMixin}
 * - {@link FishingBobberEntityMixin}
 * - {@link GravityEntityMixin}
 * - {@link HoneyBlockMixin}
 * - {@link ItemEntityMixin}
 * - {@link LivingEntityMixin}
 * - {@link SquidEntityMixin}
 * - {@link StepAndDestroyBlockGoalMixin}
 */
public class GravityRegistry extends BiRegistry<RegistryKey<World>, Double> {
	public static final GravityRegistry INSTANCE = new GravityRegistry();

	/** We only want one instance of this. */
	private GravityRegistry() {}

	/** Returns the gravity for the given registry key, or the default value. */
	@Override
	public Double get(RegistryKey<World> worldRegistryKey) {
		return Optional.ofNullable(super.get(worldRegistryKey)).orElse(AMConfig.get().world.gravity);
	}
}
