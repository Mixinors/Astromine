package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;

public class EntityUtilities {
	/** Gets the amount of air the given {@link LivingEntity} should have
	 * next tick if they are currently unable to breathe.
	 * @param air The amount of air the entity currently has
	 */
	public static int getNextAirUnderwater(LivingEntity entity, int air) {
		int i = EnchantmentHelper.getRespiration(entity);
		return i > 0 && entity.getRandom().nextInt(i + 1) > 0 ? air : air - 1;
	}
}
