package com.github.chainmailstudios.astromine.common.weapon;

import java.util.List;
import java.util.stream.Collectors;

import com.github.chainmailstudios.astromine.common.entity.projectile.BulletEntity;
import com.github.chainmailstudios.astromine.common.inventory.InventoryComponent;
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

public abstract class BaseWeapon extends Item implements WeaponElement {
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

	public void tryShot(World world, PlayerEntity user) {
		InventoryComponent component = InventoryComponentFromInventory.of(user.inventory);

		List<ItemStack> ammoStacks = (List<ItemStack>) component.getContentsMatching(stack -> stack.getItem() == this.getAmmo());

		ammoStacks = ammoStacks.stream().filter(stack -> stack.getDamage() < stack.getMaxDamage()).collect(Collectors.toList());

		if (!ammoStacks.isEmpty()) {
			ItemStack ammoStack = ammoStacks.get(0);

			if (ammoStack.getDamage() >= ammoStack.getMaxDamage()) {
				if (world.isClient) {
					ClientUtilities.playSound(user.getBlockPos(), AstromineSounds.EMPTY_MAGAZINE, SoundCategory.PLAYERS, 1, 1, true);
				}

				return;
			}

			if (!world.isClient) {
				ammoStack.damage(1, world.random, (ServerPlayerEntity) user);
			}

			PersistentProjectileEntity persistentProjectileEntity = new BulletEntity(AstromineEntities.BULLET_ENTITY_TYPE, user, world);

			persistentProjectileEntity.setProperties(user, user.pitch, user.yaw, 0.0F, this.getDistance(), 0);

			persistentProjectileEntity.setCritical(true);

			persistentProjectileEntity.setDamage(this.getDamage());

			persistentProjectileEntity.setPunch(this.getPunch());

			persistentProjectileEntity.setSound(AstromineSounds.EMPTY);

			if (world.isClient) {
				ClientUtilities.addEntity(persistentProjectileEntity);

				ClientUtilities.playSound(user.getBlockPos(), this.getShotSound(), SoundCategory.PLAYERS, 1, 1, true);
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
