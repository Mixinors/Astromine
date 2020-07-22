package com.github.chainmailstudios.astromine.common.recipe.base;

import net.minecraft.nbt.CompoundTag;

public interface RecipeConsumer {
	double getCurrent();

	int getLimit();

	boolean isActive();

	void setCurrent(double current);

	void setLimit(int limit);

	void setActive(boolean isActive);

	void increment();

	default void reset() {
		setCurrent(0);
		setLimit(100);
		setActive(false);
	}

	default boolean isFinished() {
		return getCurrent() >= getLimit();
	}

	default CompoundTag writeRecipeProgress(CompoundTag tag) {
		tag.putDouble("current", getCurrent());
		tag.putInt("limit", getLimit());
		return tag;
	}

	default void readRecipeProgress(CompoundTag tag) {
		setCurrent(tag.getDouble("current"));
		setLimit(tag.getInt("limit"));
	}
}
