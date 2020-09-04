package com.github.chainmailstudios.astromine.datagen.material;

public enum MaterialItemType {
	INGOT,
	GEM(""),
	MISC_RESOURCE(""),
	NUGGET,
	FRAGMENT,
	BLOCK,
	ORE,
	METEOR_ORE("meteor", "ore"),
	ASTEROID_ORE("asteroid", "ore", true),
	METEOR_CLUSTER("meteor", "cluster"),
	ASTEROID_CLUSTER("asteroid", "cluster", true),
	DUST,
	TINY_DUST,
	GEAR,
	PLATES,
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
	MOON_ORE("moon", "ore", true);

	final String prefix;
	final String suffix;
	final boolean optionalInTag;

	MaterialItemType() {
		this.prefix = "";
		this.suffix = this.getName();
		this.optionalInTag = false;
	}

	MaterialItemType(String suffix) {
		this("", suffix, false);
	}

	MaterialItemType(String prefix, String suffix, boolean optionalInTag) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.optionalInTag = optionalInTag;
	}

	MaterialItemType(String prefix, String suffix) {
		this(prefix, suffix, false);
	}

	public String getItemId(String materialName) {
		return prefix + (prefix.isEmpty() ? "" : "_") + materialName + (suffix.isEmpty() ? "" : "_") + suffix;
	}

	public String getName() {
		return this.name().toLowerCase();
	}

	public boolean isBlock() {
		return this == BLOCK || this == ORE || this == METEOR_ORE || this == ASTEROID_ORE || this == MOON_ORE;
	}

	public boolean isOptionalInTag() {
		return optionalInTag;
	}
}
