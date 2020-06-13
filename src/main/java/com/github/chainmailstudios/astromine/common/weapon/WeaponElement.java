package com.github.chainmailstudios.astromine.common.weapon;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;

public interface WeaponElement {
	float getZoom();

	float getDamage();

	float getDistance();

	int getPunch();

	float getRecoil();

	long getInterval();

	SoundEvent getShotSound();

	Vector3f getTranslation();

	Item getAmmo();
}
