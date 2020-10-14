package com.github.chainmailstudios.astromine.datagen.material;

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
	WRENCH,
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
