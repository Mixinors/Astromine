package com.github.chainmailstudios.astromine.common.item.weapon;

import java.util.List;
import java.util.Optional;

import com.github.chainmailstudios.astromine.common.entity.projectile.BulletEntity;
import com.github.chainmailstudios.astromine.common.inventory.InventoryComponentFromInventory;
import com.github.chainmailstudios.astromine.common.utilities.ClientUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineEntities;
import com.github.chainmailstudios.astromine.registry.AstromineSounds;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public abstract class BaseWeapon extends Item implements Weapon {
	private long lastShot = 0;
	private long lastReload = 0;

	public BaseWeapon(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return TypedActionResult.pass(user.getStackInHand(hand));
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(this.getCategory().formatted(Formatting.GRAY, Formatting.ITALIC));
	}

	public abstract TranslatableText getCategory();

	@Override
	public long getLastReload() {
		return this.lastReload;
	}

	@Override
	public void setLastReload(long lastReload) {
		this.lastReload = lastReload;
	}

	@Override
	public long getLastShot() {
		return this.lastShot;
	}

	@Override
	public void setLastShot(long lastShot) {
		this.lastShot = lastShot;
	}

	public void tryShoot(World world, PlayerEntity user) {
		Optional<ItemStack> optionalMagazine = InventoryComponentFromInventory.of(user.inventory).getContentsMatching(stack -> stack.getItem() == this.getAmmo()).stream().filter(stack -> stack.getDamage() < stack.getMaxDamage()).findFirst();

		if (optionalMagazine.isPresent() || user.isCreative()) {
			long currentAttempt = System.currentTimeMillis();

			if (this.isReloading(currentAttempt)) {
				return;
			}

			if (optionalMagazine.isPresent() && !user.isCreative()) {
				ItemStack magazine = optionalMagazine.get();

				if (magazine.getDamage() >= magazine.getMaxDamage()) {
					if (world.isClient) {
						world.playSound(user, user.getBlockPos(), AstromineSounds.EMPTY_MAGAZINE, SoundCategory.PLAYERS, 1f, 1f);
					}

					return;
				}

				if (!world.isClient) {
					magazine.damage(1, world.random, (ServerPlayerEntity) user);

					if (magazine.getDamage() >= magazine.getMaxDamage()) {
						this.setLastReload(currentAttempt);
					}
				}
			}

			if (optionalMagazine.isPresent() || user.isCreative()) {
				PersistentProjectileEntity persistentProjectileEntity = new BulletEntity(AstromineEntities.BULLET_ENTITY_TYPE, user, world);

				persistentProjectileEntity.setProperties(user, user.pitch, user.yaw, 0.0F, this.getDistance(), 0);

				persistentProjectileEntity.setCritical(true);

				persistentProjectileEntity.setDamage(this.getDamage());

				persistentProjectileEntity.setPunch(this.getPunch());

				persistentProjectileEntity.setSound(AstromineSounds.EMPTY);

				if (world.isClient) {
					ClientUtilities.addEntity(persistentProjectileEntity);

					world.playSound(user, user.getBlockPos(), this.getShotSound(), SoundCategory.PLAYERS, 1f, 1f);
				} else {
					user.world.spawnEntity(persistentProjectileEntity);
				}

				if (world.isClient) {
					user.pitch -= this.getRecoil() / 16f / (ClientUtilities.Weapon.isAiming() ? 2 : 1);
					user.yaw += (world.random.nextBoolean() ? world.random.nextInt(16) / 16f : -world.random.nextInt(16) / 16f) / (ClientUtilities.Weapon.isAiming() ? 2 : 1);
				}
			}
		}
	}
}
