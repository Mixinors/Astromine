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

package com.github.mixinors.astromine.datagen.provider.tag;

import java.util.HashMap;
import java.util.Map;

import com.github.mixinors.astromine.common.fluid.SimpleFluid;
import com.github.mixinors.astromine.datagen.DatagenLists;
import com.github.mixinors.astromine.registry.common.AMTagKeys;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class AMFluidTagProvider extends FabricTagProvider.FluidTagProvider {
	
	public AMFluidTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	public static final Map<SimpleFluid, TagKey<Fluid>> FLUID_TAGS = new HashMap<>();

	@Override
	protected void generateTags() {
		DatagenLists.FluidLists.FLUIDS.forEach((fluid) -> {
			var tag = AMTagKeys.createCommonFluidTag(Registry.FLUID.getId(fluid.getStill()).getPath());
			FLUID_TAGS.put(fluid, tag);
			var tagBuilder = getOrCreateTagBuilder(tag);
			tagBuilder.add(fluid.getStill(), fluid.getFlowing());
		});

		var industrialFluidsTagBuilder = getOrCreateTagBuilder(AMTagKeys.FluidTags.INDUSTRIAL_FLUIDS);
		DatagenLists.FluidLists.INDUSTRIAL_FLUIDS.forEach((fluid) -> industrialFluidsTagBuilder.addTag(FLUID_TAGS.get(fluid)));

		var moltenFluidsTagBuilder = getOrCreateTagBuilder(AMTagKeys.FluidTags.MOLTEN_FLUIDS);
		DatagenLists.FluidLists.MOLTEN_FLUIDS.forEach((fluid) -> moltenFluidsTagBuilder.addTag(FLUID_TAGS.get(fluid)));
	}
}
