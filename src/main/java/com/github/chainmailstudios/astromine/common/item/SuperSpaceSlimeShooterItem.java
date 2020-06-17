package com.github.chainmailstudios.astromine.common.item;

import java.util.Optional;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.entity.projectile.BulletEntity;
import com.github.chainmailstudios.astromine.common.inventory.InventoryComponentFromInventory;
import com.github.chainmailstudios.astromine.common.item.weapon.BaseWeapon;
import com.github.chainmailstudios.astromine.common.utilities.ClientUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineEntities;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import com.github.chainmailstudios.astromine.registry.AstromineSounds;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SuperSpaceSlimeShooterItem extends BaseWeapon {
	public static final Identifier TEXTURE = AstromineCommon.identifier("textures/item/empty.png");

	public SuperSpaceSlimeShooterItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public TranslatableText getCategory() {
		return new TranslatableText("text.astromine.weapon.fantasy");
	}

	@Override
	public void tryShoot(World world, PlayerEntity user) {
		Optional<ItemStack> optionalMagazine = InventoryComponentFromInventory.of(user.inventory).getContentsMatching(stack -> stack.getItem() == this.getAmmo()).stream().findFirst();

		if (optionalMagazine.isPresent() || user.isCreative()) {
			long currentAttempt = System.currentTimeMillis();

			if (this.isReloading(currentAttempt)) {
				return;
			}

			if (optionalMagazine.isPresent() && !user.isCreative()) {
				ItemStack magazine = optionalMagazine.get();

				if (!world.isClient && world.random.nextInt(8) == 0) {
					magazine.decrement(1);

					if (magazine.isEmpty()) {
						this.setLastReload(currentAttempt);
					}
				}
			}

			if (optionalMagazine.isPresent() || user.isCreative()) {
				BulletEntity bulletEntity = new BulletEntity(AstromineEntities.BULLET_ENTITY_TYPE, user, world);

				bulletEntity.setProperties(user, user.pitch, user.yaw, 0.0F, this.getDistance(), 0);

				bulletEntity.setCritical(false);

				bulletEntity.setDamage(this.getDamage());

				bulletEntity.setPunch(this.getPunch());

				bulletEntity.setTexture(this.getBulletTexture());

				bulletEntity.setSound(AstromineSounds.EMPTY);

				if (world.isClient) {
					ClientUtilities.addEntity(bulletEntity);

					world.playSound(user, user.getBlockPos(), this.getShotSound(), SoundCategory.PLAYERS, 1f, 1f);
				} else {
					user.world.spawnEntity(bulletEntity);
				}

				if (world.isClient) {
					user.pitch -= this.getRecoil() / 16f / (ClientUtilities.Weapon.isAiming() ? 2 : 1);
					user.yaw += (world.random.nextBoolean() ? world.random.nextInt(16) / 16f : -world.random.nextInt(16) / 16f) / (ClientUtilities.Weapon.isAiming() ? 2 : 1);
				} else {
					// Ray-trace out to find end location of particle beam.
					HitResult result = user.rayTrace(10, 0, false);
					Vec3d hitPos = result.getPos();
					Vec3d originPos = user.getPos().add(0, 1, 0);

					// Calculate change in slope for particles.
					Vec3d change = originPos.subtract(hitPos);
					double distance = Math.sqrt(Math.pow(change.x, 2) + Math.pow(change.y, 2) + Math.pow(change.z, 2));
					change = new Vec3d(change.x / distance, change.y / distance, change.z / distance);

					ServerWorld serverWorld = (ServerWorld) world;
					Vec3d increase = originPos;

					// Spawn particles.
					for (int i = 0; i < 5; i++) {
						serverWorld.spawnParticles(AstromineParticles.SPACE_SLIME, increase.getX(), increase.getY(), increase.getZ(), 1, 0, 0, 0, 0);
						increase = increase.subtract(change);
					}
				}
			} else {
				world.playSound(user, user.getBlockPos(), AstromineSounds.EMPTY_MAGAZINE, SoundCategory.PLAYERS, 1f, 1f);
			}
		}
	}

	@Override
	public float getZoom() {
		return 50;
	}

	@Override
	public float getDamage() {
		return 1;
	}

	@Override
	public float getDistance() {
		return 10;
	}

	@Override
	public int getPunch() {
		return 1;
	}

	@Override
	public float getRecoil() {
		return 1;
	}

	@Override
	public long getReloadInterval() {
		return 0;
	}

	@Override
	public long getShotInterval() {
		return 0;
	}

	@Override
	public SoundEvent getShotSound() {
		return SoundEvents.BLOCK_SLIME_BLOCK_HIT;
	}

	@Override
	public Vector3f getTranslation() {
		return new Vector3f(0, 0, 0);
	}

	@Override
	public Item getAmmo() {
		return AstromineItems.SPACE_SLIME_BALL;
	}

	@Override
	public Identifier getBulletTexture() {
		return TEXTURE;
	}
}
