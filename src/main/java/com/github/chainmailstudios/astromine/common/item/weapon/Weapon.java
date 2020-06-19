package com.github.chainmailstudios.astromine.common.item.weapon;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public interface Weapon {
	float getZoom();

	float getDamage();

	float getDistance();

	int getPunch();

	float getRecoil();

	default boolean isReloading(long currentTime) {
		return currentTime - this.getLastReload() < this.getReloadInterval();
	}

	long getLastReload();

	long getReloadInterval();

	void setLastReload(long lastReload);

	default boolean isWaiting(long currentTime) {
		return currentTime - this.getLastShot() < this.getShotInterval();
	}

	long getLastShot();

	long getShotInterval();

	void setLastShot(long lastShot);

	SoundEvent getShotSound();

	Vector3f getTranslation();

	Item getAmmo();

	Identifier getBulletTexture();
}
