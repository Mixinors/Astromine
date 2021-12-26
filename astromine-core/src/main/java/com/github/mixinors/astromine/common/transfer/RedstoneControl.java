package com.github.mixinors.astromine.common.transfer;

public enum RedstoneControl {
	WORK_WHEN_ON,
	WORK_WHEN_OFF,
	WORK_ALWAYS;
	
	public RedstoneControl next() {
		return switch (this) {
			case WORK_WHEN_ON -> WORK_WHEN_OFF;
			case WORK_WHEN_OFF -> WORK_ALWAYS;
			case WORK_ALWAYS -> WORK_WHEN_ON;
		};
	}
	
	public RedstoneControl previous() {
		return switch (this) {
			case WORK_WHEN_ON -> WORK_ALWAYS;
			case WORK_ALWAYS -> WORK_WHEN_OFF;
			case WORK_WHEN_OFF -> WORK_WHEN_ON;
		};
	}
}
