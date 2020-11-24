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

import com.github.chainmailstudios.astromine.registry.AstromineIdentifierFixes;
import org.jetbrains.annotations.ApiStatus;

public class AstromineFoundationsIdentifierFixes extends AstromineIdentifierFixes {
	public static void initialize() {
		initializePlateFix();
	}

	@ApiStatus.AvailableSince("1.11.4")
	public static void initializePlateFix() {
		register("metite_plates", "metite_plate");
		register("stellum_plates", "stellum_plate");
		register("univite_plates", "univite_plate");
		register("lunum_plates", "lunum_plate");

		register("copper_plates", "copper_plate");
		register("tin_plates", "tin_plate");
		register("silver_plates", "silver_plate");
		register("lead_plates", "lead_plate");

		register("steel_plates", "steel_plate");
		register("bronze_plates", "bronze_plate");
		register("electrum_plates", "electrum_plate");
		register("rose_gold_plates", "rose_gold_plate");
		register("sterling_silver_plates", "sterling_silver_plate");
		register("fools_gold_plates", "fools_gold_plate");
		register("meteoric_steel_plates", "meteoric_steel_plate");

		register("iron_plates", "iron_plate");
		register("gold_plates", "gold_plate");
		register("netherite_plates", "netherite_plate");
	}
}
