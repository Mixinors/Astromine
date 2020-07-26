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
package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.client.render.sky.MarsSkyProperties;
import com.github.chainmailstudios.astromine.client.render.sky.MoonSkyProperties;
import com.github.chainmailstudios.astromine.client.render.sky.SpaceSkyProperties;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(SkyProperties.class)
public class SkyPropertiesMixin {

	@Shadow
	@Final
	private static Object2ObjectMap<RegistryKey<DimensionType>, SkyProperties> BY_DIMENSION_TYPE;

	static {
		BY_DIMENSION_TYPE.put(AstromineDimensions.EARTH_SPACE_REGISTRY_KEY, new SpaceSkyProperties());
		BY_DIMENSION_TYPE.put(AstromineDimensions.MOON_REGISTRY_KEY, new MoonSkyProperties());
		BY_DIMENSION_TYPE.put(AstromineDimensions.MARS_REGISTRY_KEY, new MarsSkyProperties());
	}
}
