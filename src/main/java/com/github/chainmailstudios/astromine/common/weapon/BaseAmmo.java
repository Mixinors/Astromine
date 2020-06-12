package com.github.chainmailstudios.astromine.common.weapon;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public abstract class BaseAmmo extends Item implements AmmoElement {
	public BaseAmmo(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("text.astromine.magazine.rounds").append(": " + (stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage()).formatted(Formatting.GRAY, Formatting.ITALIC));
	}
}
