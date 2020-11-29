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

package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ToolUtilities {
	/** Returns half of the sum of the attack damage of two {@link DiggerItem}s. */
	public static float getAttackDamage(DiggerItem first, DiggerItem second) {
		return (first.getAttackDamage() + second.getAttackDamage()) / 2F;
	}

	/** Returns a third of the sum of the attack speed of two {@link DiggerItem}s. */
	public static float getAttackSpeed(DiggerItem first, DiggerItem second) {
		return (getAttackSpeed(first) + getAttackSpeed(second)) / 3F;
	}

	/** Returns the attack speed of a {@link DiggerItem}. */
	private static float getAttackSpeed(DiggerItem item) {
		return item.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_SPEED).stream().filter((AttributeModifier modifier) -> modifier.getId().equals(UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3"))).map(AttributeModifier::getAmount).findFirst().orElse(0d).floatValue();
	}

	/** Returns an {@link ItemStack} of our manual. */
	public static ItemStack getAstromineBook() {
		return new ItemStack(AstromineItems.MANUAL);
	}
}
