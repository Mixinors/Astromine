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

package com.github.mixinors.astromine.datagen;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import com.github.mixinors.astromine.AstromineCommon;
import com.github.mixinors.astromine.datagen.entrypoint.DatagenInitializer;

public class AstromineDatagenInitializer implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		try {
			FabricLoader.getInstance().getEntrypoints("main", ModInitializer.class).forEach(ModInitializer::onInitialize); // Ensures blocks and items are registered
			FabricLoader.getInstance().getEntrypoints("astromine_datagen", DatagenInitializer.class).forEach(DatagenInitializer::getMaterialSets); // Ensures Material Sets are registered
			FabricLoader.getInstance().getEntrypoints("astromine_datagen", DatagenInitializer.class).forEach(DatagenInitializer::registerData);
		} catch (Exception e) {
			AstromineCommon.LOGGER.error("Data generation failed.");
			AstromineCommon.LOGGER.error(e.getMessage(), e);
			System.exit(1);
		}
		System.exit(0);
	}
}
