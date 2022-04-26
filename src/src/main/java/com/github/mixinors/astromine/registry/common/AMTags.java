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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AMTags {
	public static final TagKey<Item> TRICKS_PIGLINS = ofItem(AMCommon.id("tricks_piglins"));
	public static final TagKey<Block> DRILL_MINEABLES = ofBlock(AMCommon.id("drill_mineables"));
	
	public static final TagKey<Fluid> INDUSTRIAL_FLUID = ofFluid(AMCommon.id("industrial_fluid"));
	
	public static final TagKey<Fluid> NORMAL_BREATHABLE = ofFluid(AMCommon.id("normal_breathable"));
	public static final TagKey<Fluid> WATER_BREATHABLE = ofFluid(AMCommon.id("water_breathable"));
	public static final TagKey<Fluid> LAVA_BREATHABLE = ofFluid(AMCommon.id("lava_breathable"));
	
	public static final TagKey<Fluid> ROCKET_FUELS = ofFluid(AMCommon.id("rocket_fuels"));
	
	public static final TagKey<EntityType<?>> DOES_NOT_BREATHE = ofEntityType(AMCommon.id("does_not_breathe"));
	
	public static TagKey<Item> ofItem(Identifier id) {
		return TagKey.of(Registry.ITEM_KEY, id);
	}
	
	public static TagKey<Block> ofBlock(Identifier id) {
		return TagKey.of(Registry.BLOCK_KEY, id);
	}
	
	public static TagKey<Fluid> ofFluid(Identifier id) {
		return TagKey.of(Registry.FLUID_KEY, id);
	}
	
	public static TagKey<EntityType<?>> ofEntityType(Identifier id) {
		return TagKey.of(Registry.ENTITY_TYPE_KEY, id);
	}
}
