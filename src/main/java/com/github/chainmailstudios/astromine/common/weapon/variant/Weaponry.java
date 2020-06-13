package com.github.chainmailstudios.astromine.common.weapon.variant;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.weapon.BaseWeapon;
import com.github.chainmailstudios.astromine.common.weapon.Weapon;
import com.github.chainmailstudios.astromine.registry.AstromineItemGroups;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineSounds;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class Weaponry {
	public static final class ScarH extends BaseWeapon implements Weapon {
		public static final Item.Settings SETTINGS = new Item.Settings().fireproof().maxCount(1).group(AstromineItemGroups.ASTROMINE);

		public static final Identifier TEXTURE = new Identifier(AstromineCommon.MOD_ID, "textures/entity/projectiles/bullet.png");

		public ScarH() {
			super(SETTINGS);
		}

		@Override
		public float getZoom() {
			return 45;
		}

		@Override
		public float getDamage() {
			return 0.5f;
		}

		@Override
		public long getShotInterval() {
			return 6;
		}

		@Override
		public float getRecoil() {
			return 50f;
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
		public long getReloadInterval() {
			return 3000;
		}

		@Override
		public SoundEvent getShotSound() {
			return AstromineSounds.SCAR_H_SHOT;
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

	public static final class BarretM98B extends BaseWeapon implements Weapon {
		public static final Item.Settings SETTINGS = new Item.Settings().fireproof().maxCount(1).group(AstromineItemGroups.ASTROMINE);

		public static final Identifier TEXTURE = new Identifier(AstromineCommon.MOD_ID, "textures/entity/projectiles/bullet.png");

		public BarretM98B() {
			super(SETTINGS);
		}

		@Override
		public float getZoom() {
			return 20;
		}

		@Override
		public float getDamage() {
			return 25f;
		}

		@Override
		public long getShotInterval() {
			return 2000;
		}

		@Override
		public float getRecoil() {
			return 100f;
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
		public long getReloadInterval() {
			return 6000;
		}

		@Override
		public SoundEvent getShotSound() {
			return AstromineSounds.BARRET_M98B_SHOT;
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
