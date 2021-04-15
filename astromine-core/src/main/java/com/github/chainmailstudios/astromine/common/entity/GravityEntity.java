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

package com.github.chainmailstudios.astromine.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;

/**
 * An interface meant to be implemented by mixins
 * to {@link Entity}-ies, which provides a method
 * to query the gravity of its {@link World}.
 */
public interface GravityEntity {
	/** Returns this entity's gravity multiplier,
	 * used when an entity requires lower or higher
	 * gravity than others. */
	default double astromine_getGravityMultiplier() {
		return 1.0d;
	}

	/** Returns this entity's world's gravity. */
	double astromine_getGravity();

	/** Returns the given world's gravity. */
	default double astromine_getGravity(World world) {
		return GravityRegistry.INSTANCE.get(world.getRegistryKey()) * astromine_getGravityMultiplier();
	}
}
