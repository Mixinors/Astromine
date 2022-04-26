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

import com.github.mixinors.astromine.common.config.entry.tiered.CapacitorConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.TankConfig;
import com.github.mixinors.astromine.common.config.section.*;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "astromine")
public class AMConfig implements ConfigData {
	@ConfigEntry.Gui.Excluded
	public static AMConfig INSTANCE;

	@Comment("Whether Nuclear Warheads are enabled")
	public boolean nuclearWarheadEnabled = true;

	@Comment("Whether to attempt to migrate old, broken data to new data. Usage recommended when first loading old chunks after an update")
	public boolean compatibilityMode = true;
	
	@Comment("Block Settings")
	@ConfigEntry.Gui.CollapsibleObject
	public BlocksConfigSection blocks = new BlocksConfigSection();
	
	@Comment("Item Settings")
	@ConfigEntry.Gui.CollapsibleObject
	public ItemsConfigSection items = new ItemsConfigSection();
	
	@Comment("World Settings")
	@ConfigEntry.Gui.CollapsibleObject
	public WorldConfigSection world = new WorldConfigSection();
	
	@Comment("Network Settings")
	@ConfigEntry.Gui.CollapsibleObject
	public NetworksConfigSection networks = new NetworksConfigSection();
	
	@Comment("Entity Settings")
	@ConfigEntry.Gui.CollapsibleObject
	public EntitiesConfigSection entities = new EntitiesConfigSection();
	
	@Comment("Secret Settings")
	@ConfigEntry.Gui.CollapsibleObject
	public SecretConfigSection secret = new SecretConfigSection();
	
	public static AMConfig get() {
		if (INSTANCE == null) {
			try {
				AutoConfig.register(AMConfig.class, JanksonConfigSerializer::new);
				
				try {
					AutoConfig.getConfigHolder(AMConfig.class).save();
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
				
				INSTANCE = AutoConfig.getConfigHolder(AMConfig.class).getConfig();
			} catch (Throwable throwable) {
				throwable.printStackTrace();
				
				INSTANCE = new AMConfig();
			}
		}

		return INSTANCE;
	}
}
