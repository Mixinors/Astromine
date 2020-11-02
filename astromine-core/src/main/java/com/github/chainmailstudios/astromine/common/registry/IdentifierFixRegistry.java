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

package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.registry.base.UniRegistry;

import java.util.Optional;

public class IdentifierFixRegistry extends UniRegistry<String, String> {
	public static final IdentifierFixRegistry INSTANCE = new IdentifierFixRegistry();

	@Override
	public String get(String oldPath) {
		String newPath = Optional.ofNullable(super.get(oldPath)).orElse(oldPath);
		if (this.containsKey(newPath))
			return this.get(newPath);
		return newPath;
	}

	@Override
	public String register(String oldPath, String newPath) {
		if (oldPath.equals(newPath))
			throw new IllegalArgumentException("Invalid Identifier fix attempted with paths " + oldPath + " and " + newPath + ", paths identical");
		else if (this.containsKey(newPath)) {
			if (this.get(newPath).equals(oldPath))
				throw new IllegalArgumentException("Invalid Identifier fix attempted with paths " + oldPath + " and " + newPath + ", would cause recursion");
			else return this.register(oldPath, this.get(newPath));
		} else return super.register(oldPath, newPath);
	}
}
