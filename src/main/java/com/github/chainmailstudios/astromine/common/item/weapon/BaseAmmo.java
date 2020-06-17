package com.github.chainmailstudios.astromine.common.item.weapon;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public abstract class BaseAmmo extends Item implements Ammo {
	public BaseAmmo(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("text.astromine.magazine.rounds").append(": " + (stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage()).formatted(Formatting.GRAY, Formatting.ITALIC));
	}
}
