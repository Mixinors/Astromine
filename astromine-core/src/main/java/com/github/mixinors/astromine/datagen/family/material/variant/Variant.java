package com.github.mixinors.astromine.datagen.family.material.variant;

import java.util.function.BiConsumer;

import com.github.mixinors.astromine.common.util.WordUtils;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;

import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.tag.TagFactory;

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
		String prefix = hasTagPrefix() ? getTagPrefix() + "_" : "";
		String suffix = hasTagSuffix() ? "_" + getTagSuffix() : "";
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
	 * This variant's family-independent tag, as an {@link Tag.Identified identified tag}
	 */
	default Tag.Identified<T> getTag() {
		return getTagFactory().create(getTagId());
	}

	/**
	 * This variant's family-dependent tag, as an {@link Tag.Identified identified tag}, for the given family
	 */
	default Tag.Identified<T> getTag(MaterialFamily family) {
		return getTag(family.getName());
	}

	/**
	 * This variant's family-dependent tag, as an {@link Tag.Identified identified tag}, for the given family name
	 */
	default Tag.Identified<T> getTag(String family) {
		return getTagFactory().create(getTagId(family));
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
	TagFactory<T> getTagFactory();
}
