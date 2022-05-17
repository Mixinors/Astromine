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

package com.github.mixinors.astromine.datagen.family.material.variant;

import com.github.mixinors.astromine.common.util.WordUtils;
import com.github.mixinors.astromine.datagen.family.material.family.MaterialFamily;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public interface Variant<T extends ItemConvertible> {
	/**
	 * The name of this variant
	 */
	String getName();
	
	/**
	 * The consumer that registers this variant's model
	 */
	BiConsumer<?, T> getModelRegistrar();
	
	/**
	 * The path of this variant's family-independent tag, e.g. {@code raw_ore_blocks}, {@code gems}
	 */
	default String getTagPath() {
		return WordUtils.pluralize(getName());
	}
	
	/**
	 * The prefix of a family-dependent tag, e.g. {@code raw}
	 */
	default String getTagPrefix() {
		return "";
	}
	
	/**
	 * The centre of a family-dependent tag, e.g. {@code iron}, {@code diamonds}
	 *
	 * @param material the family's name, e.g. {@code iron}, {@code diamond}
	 */
	default String getTagCentre(String material) {
		return shouldPluralizeTagCentre() ? WordUtils.pluralize(material) : material;
	}
	
	/**
	 * The suffix of a family-dependent tag, e.g. {@code blocks}
	 */
	default String getTagSuffix() {
		return getTagPath();
	}
	
	/**
	 * The path of a family-dependent tag, e.g. {@code raw_iron_blocks}, {@code diamonds}
	 *
	 * @param material the family's name
	 */
	default String getTagPath(String material) {
		var prefix = hasTagPrefix() ? getTagPrefix() + "_" : "";
		var suffix = hasTagSuffix() ? "_" + getTagSuffix() : "";
		return prefix + getTagCentre(material) + suffix;
	}
	
	/**
	 * The {@link Identifier} of this variant's family-independent tag, e.g. {@code c:raw_ore_blocks}, {@code c:gems}
	 */
	default Identifier getTagId() {
		return new Identifier("c", getTagPath());
	}
	
	/**
	 * The {@link Identifier} of a family-dependent tag for the given family, e.g. {@code c:raw_iron_blocks}, {@code c:diamonds}
	 */
	default Identifier getTagId(MaterialFamily family) {
		return getTagId(family.getName());
	}
	
	/**
	 * The {@link Identifier} of a family-dependent tag for the given family name, e.g. {@code c:raw_iron_blocks}, {@code c:diamonds}
	 *
	 * @param family the family's name
	 */
	default Identifier getTagId(String family) {
		return new Identifier("c", getTagPath(family));
	}
	
	/**
	 * This variant's family-independent tag, as an {@link TagKey identified tag}
	 */
	default TagKey<T> getTag() {
		return createTag(getTagId());
	}
	
	/**
	 * This variant's family-dependent tag, as an {@link TagKey identified tag}, for the given family
	 */
	default TagKey<T> getTag(MaterialFamily family) {
		return getTag(family.getName());
	}
	
	/**
	 * This variant's family-dependent tag, as an {@link TagKey identified tag}, for the given family name
	 */
	default TagKey<T> getTag(String family) {
		return createTag(getTagId(family));
	}
	
	/**
	 * Whether the variant has a family-independent tag, e.g. {@code c:raw_ore_blocks}, {@code c:gems}
	 */
	default boolean hasTag() {
		return true;
	}
	
	/**
	 * Whether this variant's family-dependent tags have a prefix
	 *
	 * @see #getTagPrefix()
	 */
	default boolean hasTagPrefix() {
		return !getTagPrefix().isEmpty();
	}
	
	/**
	 * Whether this variant's family-dependent tags have a suffix
	 *
	 * @see #getTagSuffix()
	 */
	default boolean hasTagSuffix() {
		return !getTagSuffix().isEmpty();
	}
	
	/**
	 * Whether this variant's family-dependent tags should have their centre pluralized
	 *
	 * @see #getTagCentre(String)
	 */
	default boolean shouldPluralizeTagCentre() {
		return !hasTagSuffix();
	}
	
	/**
	 * The tag factory for this variant, should be effectively static
	 */
	TagKey<T> createTag(Identifier id);
	
	/**
	 * The amount of molten fluid produced when melted, in droplets.
	 */
	default long getMeltedFluidAmount(MaterialFamily family) {
		return getMeltedFluidAmount(family.isBlock2x2());
	}
	
	/**
	 * The amount of molten fluid produced when melted, in droplets.
	 */
	long getMeltedFluidAmount(boolean block2x2);
	
	default int getMeltingTime(MaterialFamily family) {
		return (int) (getMeltingTimeMultiplier() * family.getBaseMeltingTime());
	}
	
	float getMeltingTimeMultiplier();
	
	default int getMeltingEnergy(MaterialFamily family) {
		return (int) (getMeltingEnergyMultiplier() * family.getBaseMeltingEnergy());
	}
	
	default float getMeltingEnergyMultiplier() {
		return getMeltingTimeMultiplier();
	}
}
