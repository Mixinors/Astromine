package com.github.mixinors.astromine.common.component.base;

import com.github.mixinors.astromine.common.block.redstone.RedstoneType;

import java.util.Objects;

class RedstoneComponentImpl implements RedstoneComponent{
	private RedstoneType type = RedstoneType.WORK_WHEN_OFF;
	
	public RedstoneType getType() {
		return type;
	}
	
	@Override
	public void setType(RedstoneType type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RedstoneComponentImpl that = (RedstoneComponentImpl) o;
		return type == that.type;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(type);
	}
}
