/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.config;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.config.section.*;
import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AMConfig {
	private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("astromine.json");
	private static final com.google.gson.Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static AMConfig INSTANCE;
	public boolean nuclearWarheadEnabled = true;
	public boolean compatibilityMode = true;
	public BlocksConfigSection blocks = new BlocksConfigSection();
	public ItemsConfigSection items = new ItemsConfigSection();
	public WorldConfigSection world = new WorldConfigSection();
	public NetworksConfigSection networks = new NetworksConfigSection();
	public EntitiesConfigSection entities = new EntitiesConfigSection();
	public SecretConfigSection secret = new SecretConfigSection();
	
	public static void init() {
		get();
	}
	
	public static AMConfig get() {
		if (INSTANCE == null) {
			try {
				if (Files.exists(CONFIG_PATH)) {
					try (var reader = Files.newBufferedReader(CONFIG_PATH)) {
						INSTANCE = GSON.fromJson(reader, AMConfig.class);
					}
				}
				
				if (INSTANCE == null) {
					INSTANCE = new AMConfig();
				}
				
				save();
			} catch (Exception exception) {
				AMCommon.LOGGER.warn("Failed to load Astromine config from {}; using defaults", CONFIG_PATH, exception);
				
				INSTANCE = new AMConfig();
			}
		}
		
		return INSTANCE;
	}
	
	public static void save() throws IOException {
		Files.createDirectories(CONFIG_PATH.getParent());
		
		try (var writer = Files.newBufferedWriter(CONFIG_PATH)) {
			GSON.toJson(INSTANCE, writer);
		}
	}
}
