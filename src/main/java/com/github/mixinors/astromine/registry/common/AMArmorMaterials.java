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
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AMArmorMaterials {
	private static final DeferredRegister<ArmorMaterial> REGISTRY = DeferredRegister.create(Registries.ARMOR_MATERIAL, AMCommon.MOD_ID);
	
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SPACE_SUIT = register("space_suit", new int[] { 1, 2, 3, 1 }, 2, AMSoundEvents.SPACE_SUIT_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("metite_ingots")));
	
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BRONZE = register("bronze", new int[] { 2, 5, 6, 2 }, 16, AMSoundEvents.BRONZE_ARMOR_EQUIPPED, 0.7F, 0.0F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("bronze_ingots")));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> STEEL = register("steel", new int[] { 3, 5, 7, 2 }, 12, AMSoundEvents.STEEL_ARMOR_EQUIPPED, 0.6F, 0.0F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("steel_ingots")));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FOOLS_GOLD = register("fools_gold", new int[] { 2, 5, 6, 2 }, 10, AMSoundEvents.FOOLS_GOLD_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("fools_gold_ingots")));
	
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> METITE = register("metite", new int[] { 2, 4, 6, 2 }, 7, AMSoundEvents.METITE_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("metite_ingots")));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ASTERITE = register("asterite", new int[] { 4, 7, 8, 4 }, 20, AMSoundEvents.ASTERITE_ARMOR_EQUIPPED, 4.0F, 0.1F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("asterites")));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> STELLUM = register("stellum", new int[] { 3, 5, 6, 2 }, 15, AMSoundEvents.STELLUM_ARMOR_EQUIPPED, 6.0F, 0.2F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("stellum_ingots")));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GALAXIUM = register("galaxium", new int[] { 4, 8, 9, 4 }, 18, AMSoundEvents.GALAXIUM_ARMOR_EQUIPPED, 4.5F, 0.1F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("galaxiums")));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> UNIVITE = register("univite", new int[] { 5, 8, 9, 5 }, 22, AMSoundEvents.UNIVITE_ARMOR_EQUIPPED, 5.0F, 0.1F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("univite_ingots")));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LUNUM = register("lunum", new int[] { 3, 4, 6, 3 }, 10, AMSoundEvents.LUNUM_ARMOR_EQUIPPED, 0.0F, 0.0F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("lunum_ingots")));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> METEORIC_STEEL = register("meteoric_steel", new int[] { 3, 5, 7, 2 }, 10, AMSoundEvents.METEORIC_STEEL_ARMOR_EQUIPPED, 0.4F, 0.0F, () -> Ingredient.of(AMTagKeys.createCommonItemTag("meteoric_steel_ingots")));
	
	public static void init() {
		REGISTRY.register(AMCommon.modEventBus());
	}
	
	public static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String name, int[] protectionAmounts, int enchantability, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> supplier) {
		return REGISTRY.register(name, () -> new ArmorMaterial(defense(protectionAmounts), enchantability, equipSound, supplier, List.of(new ArmorMaterial.Layer(AMCommon.id(name))), toughness, knockbackResistance));
	}
	
	private static Map<ArmorItem.Type, Integer> defense(int[] protectionAmounts) {
		return Map.of(
				ArmorItem.Type.BOOTS, protectionAmounts[0],
				ArmorItem.Type.LEGGINGS, protectionAmounts[1],
				ArmorItem.Type.CHESTPLATE, protectionAmounts[2],
				ArmorItem.Type.HELMET, protectionAmounts[3]
		);
	}
}
