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

import com.github.mixinors.astromine.common.registry.base.MultiRegistry;
import com.github.mixinors.astromine.mixin.common.LivingEntityMixin;

import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

/**
 * A {@link MultiRegistry} for registration of
 * {@link EntityType}s to {@link Tag<Fluid>}.
 *
 * The specified {@link Tag<Fluid>}(s) registered for
 * the {@link EntityType} are used to verify if the entity
 * can breathe, in {@link LivingEntityMixin}.
 */
public class BreathableRegistry extends MultiRegistry<EntityType<?>, Tag<Fluid>> {
	public static final BreathableRegistry INSTANCE = new BreathableRegistry();

	/** We only want one instance of this. */
	private BreathableRegistry() {}

	/** Registers a variable amount of {@link Tag<Fluid>}. */
	@SafeVarargs
	public final void register(EntityType<?> type, Tag<Fluid>... tags) {
		for (var tag : tags) {
			register(type, tag);
		}
	}

	/** Asserts whether the given {@link EntityType} can breathe in the specified {@link Fluid}, or not. */
	public boolean canBreathe(EntityType<?> type, Fluid fluid) {
		return get(type).stream().anyMatch(tag -> tag.contains(fluid)) || get(type).isEmpty();
	}
}
