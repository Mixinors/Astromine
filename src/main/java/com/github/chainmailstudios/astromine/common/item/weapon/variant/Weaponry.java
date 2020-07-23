/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.chainmailstudios.astromine.common.item.weapon.variant;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.weapon.BaseWeapon;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineSoundEvents;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class Weaponry {
	public static final class ScarH extends BaseWeapon {
		public static final Identifier TEXTURE = AstromineCommon.identifier("textures/entity/projectiles/bullet.png");

		public ScarH(Settings settings) {
			super(settings);
		}

		@Override
		public float getZoom() {
			return 45;
		}

		@Override
		public float getDamage() {
			return 0.05f;
		}

		@Override
		public float getDistance() {
			return 256;
		}

		@Override
		public int getPunch() {
			return 2;
		}

		@Override
		public float getRecoil() {
			return 50f;
		}

		@Override
		public long getShotInterval() {
			return 100;
		}

		@Override
		public long getReloadInterval() {
			return 3000;
		}

		@Override
		public SoundEvent getShotSound() {
			return AstromineSoundEvents.SCAR_H_SHOT;
		}

		@Override
		public Vector3f getTranslation() {
			return new Vector3f(-0.565f, 0, 0);
		}

		@Override
		public Item getAmmo() {
			return AstromineItems.NATO_7_62_X_51_MM;
		}

		@Override
		public Identifier getBulletTexture() {
			return TEXTURE;
		}

		@Override
		public TranslatableText getCategory() {
			return new TranslatableText("text.astromine.weapon.battle_rifle");
		}
	}

	public static final class BarretM98B extends BaseWeapon {
		public static final Identifier TEXTURE = AstromineCommon.identifier("textures/entity/projectiles/bullet.png");

		public BarretM98B(Item.Settings settings) {
			super(settings);
		}

		@Override
		public float getZoom() {
			return 20;
		}

		@Override
		public float getDamage() {
			return 0.25f;
		}

		@Override
		public float getDistance() {
			return 1024;
		}

		@Override
		public int getPunch() {
			return 8;
		}

		@Override
		public float getRecoil() {
			return 100f;
		}

		@Override
		public long getShotInterval() {
			return 2000;
		}

		@Override
		public long getReloadInterval() {
			return 6000;
		}

		@Override
		public SoundEvent getShotSound() {
			return AstromineSoundEvents.BARRET_M98B_SHOT;
		}

		@Override
		public Vector3f getTranslation() {
			return new Vector3f(-0.623f, 0.2075f, 0);
		}

		@Override
		public Item getAmmo() {
			return AstromineItems.LAPUA_8_6_X_70_MM;
		}

		@Override
		public Identifier getBulletTexture() {
			return TEXTURE;
		}

		@Override
		public TranslatableText getCategory() {
			return new TranslatableText("text.astromine.weapon.sniper_rifle");
		}
	}
}
