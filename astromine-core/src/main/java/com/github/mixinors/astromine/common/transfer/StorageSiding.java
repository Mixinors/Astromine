package com.github.mixinors.astromine.common.transfer;

public enum StorageSiding {
	INSERT,
	EXTRACT,
	INSERT_EXTRACT,
	NONE;
	
	public StorageSiding next() {
		return switch (this) {
			case INSERT -> EXTRACT;
			case EXTRACT -> INSERT_EXTRACT;
			case INSERT_EXTRACT -> NONE;
			case NONE -> INSERT;
		};
	}
	
	public StorageSiding previous() {
		return switch (this) {
			case INSERT -> NONE;
			case EXTRACT -> INSERT;
			case INSERT_EXTRACT -> EXTRACT;
			case NONE -> INSERT_EXTRACT;
		};
	}
}
