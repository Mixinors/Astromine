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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.fluid.base.ExtendedFluid;
import com.github.mixinors.astromine.datagen.AMDatagenLists;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AMFluidTagProvider extends FluidTagsProvider {
	
	public AMFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, AMCommon.MOD_ID, existingFileHelper);
	}
	
	public static final Map<ExtendedFluid.Entry, TagKey<Fluid>> FLUID_TAGS = new HashMap<>();
	
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		AMDatagenLists.FluidLists.FLUIDS.forEach((fluid) -> {
			var tag = AMTagKeys.createCommonFluidTag(BuiltInRegistries.FLUID.getKey(fluid.getSource()).getPath());
			
			FLUID_TAGS.put(fluid, tag);
			
			var tagBuilder = tag(tag);
			tagBuilder.add(fluid.getSource(), fluid.getFlowing());
		});
		
		var industrialFluidsTagBuilder = tag(AMTagKeys.FluidTags.INDUSTRIAL_FLUIDS);
		
		AMDatagenLists.FluidLists.INDUSTRIAL_FLUIDS.forEach((fluid) -> industrialFluidsTagBuilder.addTag(FLUID_TAGS.get(fluid)));
		
		var moltenFluidsTagBuilder = tag(AMTagKeys.FluidTags.MOLTEN_FLUIDS);
		
		AMDatagenLists.FluidLists.MOLTEN_FLUIDS.forEach((fluid) -> moltenFluidsTagBuilder.addTag(FLUID_TAGS.get(fluid)));
	}
}
