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

import com.github.mixinors.astromine.AMCommon;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.targets.ArchitecturyTarget;
import net.minecraft.util.Identifier;

public class AMComponents {
	public static final Identifier NETWORK = AMCommon.id("network");
	public static final Identifier ATMOSPHERE = AMCommon.id("atmosphere");
	public static final Identifier BRIDGE = AMCommon.id("bridge");

	public static final Identifier ITEM = AMCommon.id("item");
	public static final Identifier FLUID = AMCommon.id("fluid");
	public static final Identifier ENERGY = AMCommon.id("energy");

	public static final Identifier TRANSFER = AMCommon.id("transfer");
	public static final Identifier REDSTONE = AMCommon.id("redstone");

	public static final Identifier OXYGEN = AMCommon.id("oxygen");

	public static void init() {
		postInit();
	}
	
	@ExpectPlatform
	public static void postInit() {
		throw new AssertionError();
	}
}
