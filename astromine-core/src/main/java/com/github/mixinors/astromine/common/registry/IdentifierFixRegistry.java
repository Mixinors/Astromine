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

package com.github.mixinors.astromine.common.registry;

import net.minecraft.util.Identifier;

import com.github.mixinors.astromine.common.registry.base.UniRegistry;
import com.github.mixinors.astromine.mixin.common.IdentifierMixin;

import java.util.Optional;

/**
 * An {@link UniRegistry} for registration of
 * {@link String}s mapped to {@link String}s.
 *
 * The registered path will then be replaced by the new one,
 * through {@link IdentifierMixin}.
 */
public class IdentifierFixRegistry extends UniRegistry<String, String> {
	public static final IdentifierFixRegistry INSTANCE = new IdentifierFixRegistry();

	/** We only want one instance of this. */
	private IdentifierFixRegistry() {}

	/** Returns the fixed path for an {@link Identifier#getPath()},
	 * or the value passed if no fixes are registered. */
	@Override
	public String get(String oldPath) {
		var newPath = Optional.ofNullable(super.get(oldPath)).orElse(oldPath);

		return containsKey(newPath) ? get(newPath) : newPath;
	}

	/** Register a fix for an {@link Identifier#getPath()}. */
	@Override
	public String register(String oldPath, String newPath) {
		if (oldPath.equals(newPath))
			{
			throw new IllegalArgumentException(String.format("Invalid Identifier path fix attempted: '%s' and '%s' are the same!", newPath , oldPath));
		}  else if (containsKey(newPath)) {
			if (get(newPath).equals(oldPath)) {
				throw new IllegalArgumentException(String.format("Invalid Identifier path fix attempted: '%s' and '%s' would cause recursion!", oldPath, newPath));
			} else {
				return register(oldPath, get(newPath));
			}
		} else {
			return super.register(oldPath, newPath);
		}
	}
}
