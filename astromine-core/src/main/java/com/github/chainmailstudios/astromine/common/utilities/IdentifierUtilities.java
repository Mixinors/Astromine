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

package com.github.chainmailstudios.astromine.common.utilities;

/**
 * Utilities for dealing with Identifiers.
 */
public class IdentifierUtilities {
	/**
	 * Assets whether an identifier is valid or not.
	 *
	 * @param identifier
	 *        the specified identifier.
	 *
	 * @return true if yes; false if no.
	 */
	public static boolean isValid(String identifier) {
		try {
			return isNamespaceValid(identifier.substring(0, identifier.indexOf(":"))) && isPathValid(identifier.substring(identifier.indexOf(":") + 1));
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * Assets whether an identifier's namespace is valid or not.
	 *
	 * @param namespace
	 *        the specified namespace.
	 *
	 * @return true if yes; false if no.
	 */
	private static boolean isNamespaceValid(String namespace) {
		return namespace.chars().allMatch((c) -> {
			return c == 95 || c == 45 || c >= 97 && c <= 122 || c >= 48 && c <= 57 || c == 46;
		});
	}

	/**
	 * Assets whether an identifier's path is valid or not.
	 *
	 * @param path
	 *        the specified path.
	 *
	 * @return true if yes; false if no.
	 */
	private static boolean isPathValid(String path) {
		return path.chars().allMatch((c) -> {
			return c == 95 || c == 45 || c >= 97 && c <= 122 || c >= 48 && c <= 57 || c == 47 || c == 46;
		});
	}
}
