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

package com.github.mixinors.astromine.datagen.material;

public enum MaterialItemType {
	INGOT,
	GEM(""),
	MISC_RESOURCE(""),
	NUGGET,
	FRAGMENT,
	BLOCK(true),
	ORE(true),
	METEOR_ORE(true, "meteor", "ore"),
	ASTEROID_ORE(true, "asteroid", "ore", true),
	METEOR_CLUSTER("meteor", "cluster"),
	ASTEROID_CLUSTER("asteroid", "cluster", true),
	DUST,
	TINY_DUST,
	GEAR,
	PLATE,
	WIRE,
	PICKAXE,
	AXE,
	SHOVEL,
	SWORD,
	HOE,
	MATTOCK,
	MINING_TOOL,
	HAMMER,
	EXCAVATOR,
	HELMET,
	CHESTPLATE,
	LEGGINGS,
	BOOTS,
	MOON_ORE(true, "moon", "ore", true),
	BLOCK_2x2(true, "block"),
	APPLE;

	final boolean block;
	final String prefix;
	final String suffix;
	final boolean optionalInTag;

	MaterialItemType() {
		this(false, "", false);
	}

	MaterialItemType(boolean block) {
		this(block, "", false);
	}

	MaterialItemType(boolean block, String suffix) {
		this(block, "", suffix, false);
	}

	MaterialItemType(String suffix) {
		this(false, "", suffix, false);
	}

	MaterialItemType(boolean block, String prefix, boolean optionalInTag) {
		this.block = block;
		this.prefix = prefix;
		this.suffix = this.getName();
		this.optionalInTag = optionalInTag;
	}

	MaterialItemType(boolean block, String prefix, String suffix, boolean optionalInTag) {
		this.block = block;
		this.prefix = prefix;
		this.suffix = suffix;
		this.optionalInTag = optionalInTag;
	}

	MaterialItemType(String prefix, String suffix, boolean optionalInTag) {
		this(false, prefix, suffix, optionalInTag);
	}

	MaterialItemType(String prefix, String suffix) {
		this(false, prefix, suffix, false);
	}

	MaterialItemType(boolean block, String prefix, String suffix) {
		this(block, prefix, suffix, false);
	}

	public String getItemId(String materialName) {
		return prefix + (prefix.isEmpty() ? "" : "_") + materialName + (suffix.isEmpty() ? "" : "_") + suffix;
	}

	public String getName() {
		return this.name().toLowerCase();
	}

	public boolean isBlock() {
		return this.block;
	}

	public boolean isOptionalInTag() {
		return optionalInTag;
	}
}
