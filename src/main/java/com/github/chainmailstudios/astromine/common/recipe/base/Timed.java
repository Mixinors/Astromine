package com.github.chainmailstudios.astromine.common.recipe.base;

import net.minecraft.nbt.CompoundTag;

public interface Timed {
	int getCurrent();
	int getLimit();

	void setCurrent(int current);
	void setLimit(int limit);

	void start();
	void end();

	default CompoundTag toTag(CompoundTag tag) {
		tag.putInt("current", getCurrent());
		tag.putInt("limit", getLimit());
		return tag;
	}

	default void fromTag(CompoundTag tag) {
		setCurrent(tag.getInt("current"));
		setLimit(tag.getInt("limit"));
	}
}
