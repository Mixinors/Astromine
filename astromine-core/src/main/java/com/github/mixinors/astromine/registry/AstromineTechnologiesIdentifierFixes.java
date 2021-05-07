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

package com.github.mixinors.astromine.registry;

import org.jetbrains.annotations.ApiStatus;

public class AstromineTechnologiesIdentifierFixes extends AstromineIdentifierFixes {
    public static void initialize() {
        initializeFixesOnePointTwelve();
    }

    @ApiStatus.AvailableSince("1.12.0")
    public static void initializeFixesOnePointTwelve() {
        register("fluid_cable", "fluid_pipe");

        register("gas_canister", "large_portable_tank");
        register("pressurized_gas_canister", "portable_tank");

        register("fluid_inserter", "fluid_placer");
        register("fluid_extractor", "fluid_collector");

        register("primitive_presser", "primitive_press");
        register("basic_presser", "basic_press");
        register("advanced_presser", "advanced_press");
        register("elite_presser", "elite_press");

        register("primitive_liquid_generator", "primitive_fluid_generator");
        register("basic_liquid_generator", "basic_fluid_generator");
        register("advanced_liquid_generator", "advanced_fluid_generator");
        register("elite_liquid_generator", "elite_fluid_generator");

        register("primitive_electric_smelter", "primitive_electric_furnace");
        register("basic_electric_smelter", "basic_electric_furnace");
        register("advanced_electric_smelter", "advanced_electric_furnace");
        register("elite_electric_smelter", "elite_electric_furnace");
    }
}
