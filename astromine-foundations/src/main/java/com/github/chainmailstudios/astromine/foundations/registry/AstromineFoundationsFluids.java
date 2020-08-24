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

package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.common.fluid.ExtendedFluid;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import net.minecraft.fluid.Fluid;

public class AstromineFoundationsFluids extends AstromineFluids {
	public static final Fluid OXYGEN = ExtendedFluid.builder().fog(0x7e159ef9).tint(0xff159ef9).damage(0).toxic(false).infinite(false).name("oxygen").group(AstromineFoundationsItemGroups.FOUNDATIONS).build();

	public static final Fluid HYDROGEN = ExtendedFluid.builder().fog(0x7eff0019).tint(0xffff0019).damage(0).toxic(false).infinite(false).name("hydrogen").group(AstromineFoundationsItemGroups.FOUNDATIONS).build();

	public static final Fluid ROCKET_FUEL = ExtendedFluid.builder().fog(0x7e9ed5f7).tint(0xff9ed5f7).damage(4).toxic(true).infinite(false).name("rocket_fuel").group(AstromineFoundationsItemGroups.FOUNDATIONS).build();

	public static void initialize() {

	}
}
