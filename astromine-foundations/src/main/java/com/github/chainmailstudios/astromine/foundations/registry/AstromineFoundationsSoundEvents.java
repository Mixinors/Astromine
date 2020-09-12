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

package com.github.chainmailstudios.astromine.foundations.registry;

import net.minecraft.sound.SoundEvent;

import com.github.chainmailstudios.astromine.registry.AstromineSoundEvents;

public class AstromineFoundationsSoundEvents extends AstromineSoundEvents {
	public static final SoundEvent FIRE_EXTINGUISHER_OPEN = register("fire_extinguisher_open");

	// Armory
	public static final SoundEvent COPPER_ARMOR_EQUIPPED = register("item.armor.equip_copper");
	public static final SoundEvent TIN_ARMOR_EQUIPPED = register("item.armor.equip_tin");
	public static final SoundEvent SILVER_ARMOR_EQUIPPED = register("item.armor.equip_silver");
	public static final SoundEvent LEAD_ARMOR_EQUIPPED = register("item.armor.equip_lead");

	public static final SoundEvent BRONZE_ARMOR_EQUIPPED = register("item.armor.equip_bronze");
	public static final SoundEvent STEEL_ARMOR_EQUIPPED = register("item.armor.equip_steel");
	public static final SoundEvent ELECTRUM_ARMOR_EQUIPPED = register("item.armor.equip_electrum");
	public static final SoundEvent ROSE_GOLD_ARMOR_EQUIPPED = register("item.armor.equip_rose_gold");
	public static final SoundEvent STERLING_SILVER_ARMOR_EQUIPPED = register("item.armor.equip_sterling_silver");
	public static final SoundEvent FOOLS_GOLD_ARMOR_EQUIPPED = register("item.armor.equip_fools_gold");

	public static final SoundEvent METITE_ARMOR_EQUIPPED = register("item.armor.equip_metite");
	public static final SoundEvent ASTERITE_ARMOR_EQUIPPED = register("item.armor.equip_asterite");
	public static final SoundEvent STELLUM_ARMOR_EQUIPPED = register("item.armor.equip_stellum");
	public static final SoundEvent GALAXIUM_ARMOR_EQUIPPED = register("item.armor.equip_galaxium");
	public static final SoundEvent UNIVITE_ARMOR_EQUIPPED = register("item.armor.equip_univite");
	public static final SoundEvent LUNUM_ARMOR_EQUIPPED = register("item.armor.equip_lunum");

	public static final SoundEvent METEORIC_STEEL_ARMOR_EQUIPPED = register("item.armor.equip_meteoric_steel");
}
