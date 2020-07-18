package com.github.chainmailstudios.astromine.common.recipe.base;

import net.minecraft.nbt.CompoundTag;

public interface RecipeConsumer {
	int getCurrent();

	int getLimit();

	boolean isActive();

	void setCurrent(int current);

	void setLimit(int limit);

	void setActive(boolean isActive);

	default void increment() {
		setCurrent(getCurrent() + 1);
	}

	default void decrement() {
		setCurrent(getCurrent() - 1);
	}

	default void reset() {
		setCurrent(0);
		setLimit(100);
		setActive(false);
	}

	default boolean isFinished() {
		return getCurrent() >= getLimit();
	}

	default CompoundTag writeRecipeProgress(CompoundTag tag) {
		tag.putInt("current", getCurrent());
		tag.putInt("limit", getLimit());
		return tag;
	}

	default void readRecipeProgress(CompoundTag tag) {
		setCurrent(tag.getInt("current"));
		setLimit(tag.getInt("limit"));
	}
}
