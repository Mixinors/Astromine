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
package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.IdentifierFixRegistry;

public class AstromineIdentifierFixes {
	public static void initialize() {
		IdentifierFixRegistry.INSTANCE.register("space", "earth_space");
		//TODO: update this at some point
//		IdentifierFixRegistry.INSTANCE.register("asteroid_belt", "earth_space");
		IdentifierFixRegistry.INSTANCE.register("fuel_mixer", "advanced_fluid_mixer");
		IdentifierFixRegistry.INSTANCE.register("fuel_mixing", "fluid_mixing");
		IdentifierFixRegistry.INSTANCE.register("sorter", "advanced_triturator");
		IdentifierFixRegistry.INSTANCE.register("sorting", "triturating");
		IdentifierFixRegistry.INSTANCE.register("machine_chassis", "advanced_machine_chassis");
		IdentifierFixRegistry.INSTANCE.register("solid_generator", "advanced_solid_generator");
		IdentifierFixRegistry.INSTANCE.register("liquid_generator", "advanced_liquid_generator");
		IdentifierFixRegistry.INSTANCE.register("electric_smelter", "advanced_electric_smelter");
		IdentifierFixRegistry.INSTANCE.register("alloy_smelter", "advanced_alloy_smelter");
		IdentifierFixRegistry.INSTANCE.register("triturator", "advanced_triturator");
		IdentifierFixRegistry.INSTANCE.register("presser", "advanced_presser");
		IdentifierFixRegistry.INSTANCE.register("electrolyzer", "advanced_electrolyzer");
		IdentifierFixRegistry.INSTANCE.register("fluid_mixer", "advanced_fluid_mixer");
	}
}
