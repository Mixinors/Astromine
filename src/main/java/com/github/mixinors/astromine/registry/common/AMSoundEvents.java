/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class AMSoundEvents {
	public static final RegistrySupplier<SoundEvent> SPACE_SUIT_EQUIPPED = register("item.armor.equip_space_suit");
	
	public static final RegistrySupplier<SoundEvent> ALTAR_START = register("block.altar.start");
	public static final RegistrySupplier<SoundEvent> ALTAR_FINISH = register("block.altar.finish");
	
	public static final RegistrySupplier<SoundEvent> FIRE_EXTINGUISHER_OPEN = register("fire_extinguisher_open");
	
	public static final RegistrySupplier<SoundEvent> BRONZE_ARMOR_EQUIPPED = register("item.armor.equip_bronze");
	public static final RegistrySupplier<SoundEvent> STEEL_ARMOR_EQUIPPED = register("item.armor.equip_steel");
	public static final RegistrySupplier<SoundEvent> FOOLS_GOLD_ARMOR_EQUIPPED = register("item.armor.equip_fools_gold");
	
	public static final RegistrySupplier<SoundEvent> METITE_ARMOR_EQUIPPED = register("item.armor.equip_metite");
	public static final RegistrySupplier<SoundEvent> ASTERITE_ARMOR_EQUIPPED = register("item.armor.equip_asterite");
	public static final RegistrySupplier<SoundEvent> STELLUM_ARMOR_EQUIPPED = register("item.armor.equip_stellum");
	public static final RegistrySupplier<SoundEvent> GALAXIUM_ARMOR_EQUIPPED = register("item.armor.equip_galaxium");
	public static final RegistrySupplier<SoundEvent> UNIVITE_ARMOR_EQUIPPED = register("item.armor.equip_univite");
	
	
	public static final RegistrySupplier<SoundEvent> METEORIC_STEEL_ARMOR_EQUIPPED = register("item.armor.equip_meteoric_steel");
	
	public static final RegistrySupplier<SoundEvent> HOLOGRAPHIC_CONNECTOR_CLICK = register("holographic_connector_click");
	
	public static final RegistrySupplier<SoundEvent> MACHINE_CLICK = register("block.machine.click");
	public static final RegistrySupplier<SoundEvent> INCINERATE = register("block.shredder.shred");
	
	public static void init() {
	}
	
	public static RegistrySupplier<SoundEvent> register(String id) {
		return AMCommon.registry(Registry.SOUND_EVENT_KEY).register(AMCommon.id(id), () -> new SoundEvent(AMCommon.id(id)));
	}
}
