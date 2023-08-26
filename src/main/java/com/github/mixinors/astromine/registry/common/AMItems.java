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
import com.github.mixinors.astromine.common.body.BodyTemperature;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.item.ManualItem;
import com.github.mixinors.astromine.common.item.armor.AnimatedArmorItem;
import com.github.mixinors.astromine.common.item.armor.SpaceSuitArmorItem;
import com.github.mixinors.astromine.common.item.entity.RocketItem;
import com.github.mixinors.astromine.common.item.rocket.*;
import com.github.mixinors.astromine.common.item.storage.SimpleEnergyStorageItem;
import com.github.mixinors.astromine.common.item.storage.SimpleFluidStorageItem;
import com.github.mixinors.astromine.common.item.utility.DrillItem;
import com.github.mixinors.astromine.common.item.utility.FireExtinguisherItem;
import com.github.mixinors.astromine.common.item.utility.HolographicConnectorItem;
import com.github.mixinors.astromine.common.item.utility.MachineUpgradeKitItem;
import com.github.mixinors.astromine.common.item.weapon.GravityGauntletItem;
import com.github.mixinors.astromine.common.rocket.RocketFuelTankPart;
import com.github.mixinors.astromine.common.rocket.RocketHullPart;
import com.github.mixinors.astromine.common.rocket.RocketLandingMechanismPart;
import com.github.mixinors.astromine.common.rocket.RocketThrusterPart;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Supplier;

public class AMItems {
	public static final RegistrySupplier<Item> ENERGY = registerNoGroup("energy", () -> new Item(new Item.Settings()));
	public static final RegistrySupplier<Item> FLUID = registerNoGroup("fluid", () -> new Item(new Item.Settings()));
	public static final RegistrySupplier<Item> ITEM = registerNoGroup("item", () -> new Item(new Item.Settings()));
	
	public static final RegistrySupplier<Item> MANUAL = register("manual", () -> new ManualItem(getSettings().maxCount(1)));
	
	public static final RegistrySupplier<Item> SPACE_SLIME_SPAWN_EGG = register("space_slime_spawn_egg", () -> new SpawnEggItem(AMEntityTypes.SPACE_SLIME.get(), 0xFFFFFFFF, 0xFFFFFFFF, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> SPACE_SLIME_BALL = register("space_slime_ball", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> ASTEROID_METITE_ORE_CLUSTER = register("asteroid_metite_ore_cluster", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTEROID_ASTERITE_ORE_CLUSTER = register("asteroid_asterite_ore_cluster", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTEROID_STELLUM_ORE_CLUSTER = register("asteroid_stellum_ore_cluster", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> ASTEROID_GALAXIUM_ORE_CLUSTER = register("asteroid_galaxium_ore_cluster", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> ASTEROID_TIN_ORE_CLUSTER = register("asteroid_tin_ore_cluster", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTEROID_SILVER_ORE_CLUSTER = register("asteroid_silver_ore_cluster", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTEROID_LEAD_ORE_CLUSTER = register("asteroid_lead_ore_cluster", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> ASTEROID_IRON_ORE_CLUSTER = register("asteroid_iron_ore_cluster", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTEROID_GOLD_ORE_CLUSTER = register("asteroid_gold_ore_cluster", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTEROID_COPPER_ORE_CLUSTER = register("asteroid_copper_ore_cluster", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> ASTEROID_DIAMOND_ORE_CLUSTER = register("asteroid_diamond_ore_cluster", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTEROID_EMERALD_ORE_CLUSTER = register("asteroid_emerald_ore_cluster", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> ASTEROID_COAL_ORE_CLUSTER = register("asteroid_coal_ore_cluster", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTEROID_LAPIS_ORE_CLUSTER = register("asteroid_lapis_ore_cluster", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTEROID_REDSTONE_ORE_CLUSTER = register("asteroid_redstone_ore_cluster", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> LOW_CAPACITY_ROCKET_FUEL_TANK = register("low_capacity_rocket_fuel_tank", () -> new RocketFuelTankItem(AMItems.getSettings().maxCount(1), RocketFuelTankPart.Capacity.LOW));
	public static final RegistrySupplier<Item> MEDIUM_CAPACITY_ROCKET_FUEL_TANK = register("medium_capacity_rocket_fuel_tank", () -> new RocketFuelTankItem(AMItems.getSettings().maxCount(1), RocketFuelTankPart.Capacity.MEDIUM));
	public static final RegistrySupplier<Item> HIGH_CAPACITY_ROCKET_FUEL_TANK = register("high_capacity_rocket_fuel_tank", () -> new RocketFuelTankItem(AMItems.getSettings().maxCount(1), RocketFuelTankPart.Capacity.HIGH));
	
	public static final RegistrySupplier<Item> LOW_DURABILITY_ROCKET_HULL = register("low_durability_rocket_hull", () -> new RocketHullItem(AMItems.getSettings().maxCount(1), RocketHullPart.Durability.LOW));
	public static final RegistrySupplier<Item> MEDIUM_DURABILITY_ROCKET_HULL = register("medium_durability_rocket_hull", () -> new RocketHullItem(AMItems.getSettings().maxCount(1), RocketHullPart.Durability.MEDIUM));
	public static final RegistrySupplier<Item> HIGH_DURABILITY_ROCKET_HULL = register("high_durability_rocket_hull", () -> new RocketHullItem(AMItems.getSettings().maxCount(1), RocketHullPart.Durability.HIGH));
	
	public static final RegistrySupplier<Item> STANDING_ROCKET_LANDING_MECHANISM = register("standing_rocket_landing_mechanism", () -> new RocketLandingMechanismItem(AMItems.getSettings().maxCount(1), RocketLandingMechanismPart.Type.STANDING));
	public static final RegistrySupplier<Item> PERCHING_ROCKET_LANDING_MECHANISM = register("perching_rocket_landing_mechanism", () -> new RocketLandingMechanismItem(AMItems.getSettings().maxCount(1), RocketLandingMechanismPart.Type.PERCHING));
	public static final RegistrySupplier<Item> HOVERING_ROCKET_LANDING_MECHANISM = register("hovering_rocket_landing_mechanism", () -> new RocketLandingMechanismItem(AMItems.getSettings().maxCount(1), RocketLandingMechanismPart.Type.HOVERING));
	
	public static final RegistrySupplier<Item> ROCKET_LIFE_SUPPORT = register("rocket_life_support", () -> new RocketLifeSupportItem(AMItems.getSettings().maxCount(1)));
	
	public static final RegistrySupplier<Item> LOW_TEMPERATURE_ROCKET_SHIELDING = register("low_temperature_rocket_shielding", () -> new RocketShieldingItem(AMItems.getSettings().maxCount(1), BodyTemperature.EXTREMELY_COLD, BodyTemperature.COLD, BodyTemperature.AVERAGE));
	public static final RegistrySupplier<Item> HIGH_TEMPERATURE_ROCKET_SHIELDING = register("high_temperature_rocket_shielding", () -> new RocketShieldingItem(AMItems.getSettings().maxCount(1), BodyTemperature.EXTREMELY_HOT, BodyTemperature.HOT, BodyTemperature.AVERAGE));
	
	public static final RegistrySupplier<Item> LOW_EFFICIENCY_ROCKET_THRUSTER = register("low_efficiency_rocket_thruster", () -> new RocketThrusterItem(AMItems.getSettings().maxCount(1), RocketThrusterPart.Efficiency.LOW));
	public static final RegistrySupplier<Item> MEDIUM_EFFICIENCY_ROCKET_THRUSTER = register("medium_efficiency_rocket_thruster", () -> new RocketThrusterItem(AMItems.getSettings().maxCount(1), RocketThrusterPart.Efficiency.MEDIUM));
	public static final RegistrySupplier<Item> HIGH_EFFICIENCY_ROCKET_THRUSTER = register("high_efficiency_rocket_thruster", () -> new RocketThrusterItem(AMItems.getSettings().maxCount(1), RocketThrusterPart.Efficiency.HIGH));
	
	public static final RegistrySupplier<Item> SPACE_SUIT_HELMET = register("space_suit_helmet", () -> new SpaceSuitArmorItem(AMArmorMaterials.SPACE_SUIT, ArmorItem.Type.HELMET, AMItems.getSettings()));
	public static final RegistrySupplier<Item> SPACE_SUIT_CHESTPLATE = register("space_suit_chestplate", () -> new SpaceSuitArmorItem.Chestplate(AMArmorMaterials.SPACE_SUIT, ArmorItem.Type.CHESTPLATE, AMItems.getSettings(), AMConfig.get().items.spaceSuitChestplateFluid, AMConfig.get().items.spaceSuitChestplateEnergy));
	public static final RegistrySupplier<Item> SPACE_SUIT_LEGGINGS = register("space_suit_leggings", () -> new SpaceSuitArmorItem(AMArmorMaterials.SPACE_SUIT, ArmorItem.Type.LEGGINGS, AMItems.getSettings()));
	public static final RegistrySupplier<Item> SPACE_SUIT_BOOTS = register("space_suit_boots", () -> new SpaceSuitArmorItem(AMArmorMaterials.SPACE_SUIT, ArmorItem.Type.BOOTS, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> ROCKET = register("rocket", () -> new RocketItem(AMEntityTypes.ROCKET.get(), AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> METITE_NUGGET = register("metite_nugget", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> STELLUM_NUGGET = register("stellum_nugget", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> UNIVITE_NUGGET = register("univite_nugget", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> LUNUM_NUGGET = register("lunum_nugget", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> TIN_NUGGET = register("tin_nugget", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> SILVER_NUGGET = register("silver_nugget", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> LEAD_NUGGET = register("lead_nugget", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> STEEL_NUGGET = register("steel_nugget", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> BRONZE_NUGGET = register("bronze_nugget", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ELECTRUM_NUGGET = register("electrum_nugget", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> FOOLS_GOLD_NUGGET = register("fools_gold_nugget", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_NUGGET = register("meteoric_steel_nugget", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> COPPER_NUGGET = register("copper_nugget", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> NETHERITE_NUGGET = register("netherite_nugget", () -> new Item(AMItems.getSettings().fireproof()));
	
	public static final RegistrySupplier<Item> ASTERITE_FRAGMENT = register("asterite_fragment", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> GALAXIUM_FRAGMENT = register("galaxium_fragment", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> DIAMOND_FRAGMENT = register("diamond_fragment", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> EMERALD_FRAGMENT = register("emerald_fragment", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> QUARTZ_FRAGMENT = register("quartz_fragment", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> AMETHYST_FRAGMENT = register("amethyst_fragment", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> COPPER_WIRE = register("copper_wire", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> TIN_WIRE = register("tin_wire", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> SILVER_WIRE = register("silver_wire", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ELECTRUM_WIRE = register("electrum_wire", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> GOLD_WIRE = register("gold_wire", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> ASTERITE = register("asterite", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> GALAXIUM = register("galaxium", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> METITE_INGOT = register("metite_ingot", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> STELLUM_INGOT = register("stellum_ingot", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> UNIVITE_INGOT = register("univite_ingot", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> LUNUM_INGOT = register("lunum_ingot", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> TIN_INGOT = register("tin_ingot", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> SILVER_INGOT = register("silver_ingot", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> LEAD_INGOT = register("lead_ingot", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> BRONZE_INGOT = register("bronze_ingot", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> STEEL_INGOT = register("steel_ingot", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ELECTRUM_INGOT = register("electrum_ingot", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> FOOLS_GOLD_INGOT = register("fools_gold_ingot", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_INGOT = register("meteoric_steel_ingot", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> RAW_TIN = register("raw_tin", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> RAW_SILVER = register("raw_silver", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> RAW_LEAD = register("raw_lead", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> RAW_LUNUM = register("raw_lunum", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> METITE_DUST = register("metite_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTERITE_DUST = register("asterite_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> STELLUM_DUST = register("stellum_dust", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> GALAXIUM_DUST = register("galaxium_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> UNIVITE_DUST = register("univite_dust", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> LUNUM_DUST = register("lunum_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> TIN_DUST = register("tin_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> SILVER_DUST = register("silver_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> LEAD_DUST = register("lead_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> STEEL_DUST = register("steel_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> BRONZE_DUST = register("bronze_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ELECTRUM_DUST = register("electrum_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> FOOLS_GOLD_DUST = register("fools_gold_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_DUST = register("meteoric_steel_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> IRON_DUST = register("iron_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> GOLD_DUST = register("gold_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> COPPER_DUST = register("copper_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> NETHERITE_DUST = register("netherite_dust", () -> new Item(AMItems.getSettings().fireproof()));
	
	public static final RegistrySupplier<Item> DIAMOND_DUST = register("diamond_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> EMERALD_DUST = register("emerald_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> QUARTZ_DUST = register("quartz_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> AMETHYST_DUST = register("amethyst_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> COAL_DUST = register("coal_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> LAPIS_DUST = register("lapis_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> CHARCOAL_DUST = register("charcoal_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> RAW_NETHERITE_DUST = register("raw_netherite_dust", () -> new Item(AMItems.getSettings().fireproof()));
	
	public static final RegistrySupplier<Item> METITE_TINY_DUST = register("metite_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTERITE_TINY_DUST = register("asterite_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> STELLUM_TINY_DUST = register("stellum_tiny_dust", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> GALAXIUM_TINY_DUST = register("galaxium_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> UNIVITE_TINY_DUST = register("univite_tiny_dust", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> LUNUM_TINY_DUST = register("lunum_tiny_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> TIN_TINY_DUST = register("tin_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> SILVER_TINY_DUST = register("silver_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> LEAD_TINY_DUST = register("lead_tiny_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> STEEL_TINY_DUST = register("steel_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> BRONZE_TINY_DUST = register("bronze_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ELECTRUM_TINY_DUST = register("electrum_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> FOOLS_GOLD_TINY_DUST = register("fools_gold_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_TINY_DUST = register("meteoric_steel_tiny_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> IRON_TINY_DUST = register("iron_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> GOLD_TINY_DUST = register("gold_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> COPPER_TINY_DUST = register("copper_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> NETHERITE_TINY_DUST = register("netherite_tiny_dust", () -> new Item(AMItems.getSettings().fireproof()));
	
	public static final RegistrySupplier<Item> DIAMOND_TINY_DUST = register("diamond_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> EMERALD_TINY_DUST = register("emerald_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> QUARTZ_TINY_DUST = register("quartz_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> AMETHYST_TINY_DUST = register("amethyst_tiny_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> COAL_TINY_DUST = register("coal_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> LAPIS_TINY_DUST = register("lapis_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> REDSTONE_TINY_DUST = register("redstone_tiny_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> CHARCOAL_TINY_DUST = register("charcoal_tiny_dust", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> RAW_NETHERITE_TINY_DUST = register("raw_netherite_tiny_dust", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> GLOWSTONE_TINY_DUST = register("glowstone_tiny_dust", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> METITE_PLATE = register("metite_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> STELLUM_PLATE = register("stellum_plate", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> UNIVITE_PLATE = register("univite_plate", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> LUNUM_PLATE = register("lunum_plate", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> TIN_PLATE = register("tin_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> SILVER_PLATE = register("silver_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> LEAD_PLATE = register("lead_plate", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> STEEL_PLATE = register("steel_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> BRONZE_PLATE = register("bronze_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ELECTRUM_PLATE = register("electrum_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> FOOLS_GOLD_PLATE = register("fools_gold_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_PLATE = register("meteoric_steel_plate", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> IRON_PLATE = register("iron_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> GOLD_PLATE = register("gold_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> COPPER_PLATE = register("copper_plate", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> NETHERITE_PLATE = register("netherite_plate", () -> new Item(AMItems.getSettings().fireproof()));
	
	public static final RegistrySupplier<Item> METITE_GEAR = register("metite_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> STELLUM_GEAR = register("stellum_gear", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> UNIVITE_GEAR = register("univite_gear", () -> new Item(AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> LUNUM_GEAR = register("lunum_gear", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> TIN_GEAR = register("tin_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> SILVER_GEAR = register("silver_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> LEAD_GEAR = register("lead_gear", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> STEEL_GEAR = register("steel_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> BRONZE_GEAR = register("bronze_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> ELECTRUM_GEAR = register("electrum_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> FOOLS_GOLD_GEAR = register("fools_gold_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_GEAR = register("meteoric_steel_gear", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> IRON_GEAR = register("iron_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> GOLD_GEAR = register("gold_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> COPPER_GEAR = register("copper_gear", () -> new Item(AMItems.getSettings()));
	public static final RegistrySupplier<Item> NETHERITE_GEAR = register("netherite_gear", () -> new Item(AMItems.getSettings().fireproof()));
	
	public static final RegistrySupplier<PickaxeItem> BRONZE_PICKAXE = register("bronze_pickaxe", () -> new PickaxeItem(AMToolMaterials.BRONZE, 1, -2.8f, AMItems.getSettings()));
	public static final RegistrySupplier<AxeItem> BRONZE_AXE = register("bronze_axe", () -> new AxeItem(AMToolMaterials.BRONZE, 5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<ShovelItem> BRONZE_SHOVEL = register("bronze_shovel", () -> new ShovelItem(AMToolMaterials.BRONZE, 1.5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<HoeItem> BRONZE_HOE = register("bronze_hoe", () -> new HoeItem(AMToolMaterials.BRONZE, -2, 0f, AMItems.getSettings()));
	public static final RegistrySupplier<Item> BRONZE_SWORD = register("bronze_sword", () -> new SwordItem(AMToolMaterials.BRONZE, 3, -2.4f, AMItems.getSettings()));
	
	public static final RegistrySupplier<PickaxeItem> STEEL_PICKAXE = register("steel_pickaxe", () -> new PickaxeItem(AMToolMaterials.STEEL, 1, -2.8f, AMItems.getSettings()));
	public static final RegistrySupplier<AxeItem> STEEL_AXE = register("steel_axe", () -> new AxeItem(AMToolMaterials.STEEL, 5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<ShovelItem> STEEL_SHOVEL = register("steel_shovel", () -> new ShovelItem(AMToolMaterials.STEEL, 1.5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<HoeItem> STEEL_HOE = register("steel_hoe", () -> new HoeItem(AMToolMaterials.STEEL, -3, 0f, AMItems.getSettings()));
	public static final RegistrySupplier<Item> STEEL_SWORD = register("steel_sword", () -> new SwordItem(AMToolMaterials.STEEL, 3, -2.4f, AMItems.getSettings()));
	
	public static final RegistrySupplier<PickaxeItem> METITE_PICKAXE = register("metite_pickaxe", () -> new PickaxeItem(AMToolMaterials.METITE, 1, -2.8f, AMItems.getSettings()));
	public static final RegistrySupplier<AxeItem> METITE_AXE = register("metite_axe", () -> new AxeItem(AMToolMaterials.METITE, 5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<ShovelItem> METITE_SHOVEL = register("metite_shovel", () -> new ShovelItem(AMToolMaterials.METITE, 1.5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<HoeItem> METITE_HOE = register("metite_hoe", () -> new HoeItem(AMToolMaterials.METITE, -4, 0f, AMItems.getSettings()));
	public static final RegistrySupplier<Item> METITE_SWORD = register("metite_sword", () -> new SwordItem(AMToolMaterials.METITE, 3, -2.4f, AMItems.getSettings()));
	
	public static final RegistrySupplier<PickaxeItem> ASTERITE_PICKAXE = register("asterite_pickaxe", () -> new PickaxeItem(AMToolMaterials.ASTERITE, 1, -2.8f, AMItems.getSettings()));
	public static final RegistrySupplier<AxeItem> ASTERITE_AXE = register("asterite_axe", () -> new AxeItem(AMToolMaterials.ASTERITE, 5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<ShovelItem> ASTERITE_SHOVEL = register("asterite_shovel", () -> new ShovelItem(AMToolMaterials.ASTERITE, 1.5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<HoeItem> ASTERITE_HOE = register("asterite_hoe", () -> new HoeItem(AMToolMaterials.ASTERITE, -5, 0f, AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTERITE_SWORD = register("asterite_sword", () -> new SwordItem(AMToolMaterials.ASTERITE, 3, -2.4f, AMItems.getSettings()));
	
	public static final RegistrySupplier<PickaxeItem> STELLUM_PICKAXE = register("stellum_pickaxe", () -> new PickaxeItem(AMToolMaterials.STELLUM, 1, -2.8f, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<AxeItem> STELLUM_AXE = register("stellum_axe", () -> new AxeItem(AMToolMaterials.STELLUM, 5f, -3.0f, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<ShovelItem> STELLUM_SHOVEL = register("stellum_shovel", () -> new ShovelItem(AMToolMaterials.STELLUM, 1.5f, -3.0f, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<HoeItem> STELLUM_HOE = register("stellum_hoe", () -> new HoeItem(AMToolMaterials.STELLUM, -6, 0f, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> STELLUM_SWORD = register("stellum_sword", () -> new SwordItem(AMToolMaterials.STELLUM, 3, -2.4f, AMItems.getSettings().fireproof()));
	
	public static final RegistrySupplier<PickaxeItem> GALAXIUM_PICKAXE = register("galaxium_pickaxe", () -> new PickaxeItem(AMToolMaterials.GALAXIUM, 1, -2.8f, AMItems.getSettings()));
	public static final RegistrySupplier<AxeItem> GALAXIUM_AXE = register("galaxium_axe", () -> new AxeItem(AMToolMaterials.GALAXIUM, 5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<ShovelItem> GALAXIUM_SHOVEL = register("galaxium_shovel", () -> new ShovelItem(AMToolMaterials.GALAXIUM, 1.5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<HoeItem> GALAXIUM_HOE = register("galaxium_hoe", () -> new HoeItem(AMToolMaterials.GALAXIUM, -5, 0f, AMItems.getSettings()));
	public static final RegistrySupplier<Item> GALAXIUM_SWORD = register("galaxium_sword", () -> new SwordItem(AMToolMaterials.GALAXIUM, 3, -2.4f, AMItems.getSettings()));
	
	public static final RegistrySupplier<PickaxeItem> UNIVITE_PICKAXE = register("univite_pickaxe", () -> new PickaxeItem(AMToolMaterials.UNIVITE, 1, -2.8f, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<AxeItem> UNIVITE_AXE = register("univite_axe", () -> new AxeItem(AMToolMaterials.UNIVITE, 5f, -3.0f, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<ShovelItem> UNIVITE_SHOVEL = register("univite_shovel", () -> new ShovelItem(AMToolMaterials.UNIVITE, 1.5f, -3.0f, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<HoeItem> UNIVITE_HOE = register("univite_hoe", () -> new HoeItem(AMToolMaterials.UNIVITE, -6, 0f, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> UNIVITE_SWORD = register("univite_sword", () -> new SwordItem(AMToolMaterials.UNIVITE, 3, -2.4f, AMItems.getSettings().fireproof()));

	public static final RegistrySupplier<PickaxeItem> LUNUM_PICKAXE = register("lunum_pickaxe", () -> new PickaxeItem(AMToolMaterials.LUNUM, 1, -2.8f, AMItems.getSettings()));
	public static final RegistrySupplier<AxeItem> LUNUM_AXE = register("lunum_axe", () -> new AxeItem(AMToolMaterials.LUNUM, 5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<ShovelItem> LUNUM_SHOVEL = register("lunum_shovel", () -> new ShovelItem(AMToolMaterials.LUNUM, 1.5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<HoeItem> LUNUM_HOE = register("lunum_hoe", () -> new HoeItem(AMToolMaterials.LUNUM, -4, 0f, AMItems.getSettings()));
	public static final RegistrySupplier<Item> LUNUM_SWORD = register("lunum_sword", () -> new SwordItem(AMToolMaterials.LUNUM, 3, -2.4f, AMItems.getSettings()));
	
	public static final RegistrySupplier<PickaxeItem> METEORIC_STEEL_PICKAXE = register("meteoric_steel_pickaxe", () -> new PickaxeItem(AMToolMaterials.METEORIC_STEEL, 1, -2.8f, AMItems.getSettings()));
	public static final RegistrySupplier<AxeItem> METEORIC_STEEL_AXE = register("meteoric_steel_axe", () -> new AxeItem(AMToolMaterials.METEORIC_STEEL, 5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<ShovelItem> METEORIC_STEEL_SHOVEL = register("meteoric_steel_shovel", () -> new ShovelItem(AMToolMaterials.METEORIC_STEEL, 1.5f, -3.0f, AMItems.getSettings()));
	public static final RegistrySupplier<HoeItem> METEORIC_STEEL_HOE = register("meteoric_steel_hoe", () -> new HoeItem(AMToolMaterials.METEORIC_STEEL, -3, 0f, AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_SWORD = register("meteoric_steel_sword", () -> new SwordItem(AMToolMaterials.METEORIC_STEEL, 3, -2.4f, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> BRONZE_HELMET = register("bronze_helmet", () -> new ArmorItem(AMArmorMaterials.BRONZE, ArmorItem.Type.HELMET, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> BRONZE_CHESTPLATE = register("bronze_chestplate", () -> new ArmorItem(AMArmorMaterials.BRONZE, ArmorItem.Type.CHESTPLATE, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> BRONZE_LEGGINGS = register("bronze_leggings", () -> new ArmorItem(AMArmorMaterials.BRONZE, ArmorItem.Type.LEGGINGS, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> BRONZE_BOOTS = register("bronze_boots", () -> new ArmorItem(AMArmorMaterials.BRONZE, ArmorItem.Type.BOOTS, AMItems.getSettings().fireproof()));
	
	public static final RegistrySupplier<Item> STEEL_HELMET = register("steel_helmet", () -> new ArmorItem(AMArmorMaterials.STEEL, ArmorItem.Type.HELMET, AMItems.getSettings()));
	public static final RegistrySupplier<Item> STEEL_CHESTPLATE = register("steel_chestplate", () -> new ArmorItem(AMArmorMaterials.STEEL, ArmorItem.Type.CHESTPLATE, AMItems.getSettings()));
	public static final RegistrySupplier<Item> STEEL_LEGGINGS = register("steel_leggings", () -> new ArmorItem(AMArmorMaterials.STEEL, ArmorItem.Type.LEGGINGS, AMItems.getSettings()));
	public static final RegistrySupplier<Item> STEEL_BOOTS = register("steel_boots", () -> new ArmorItem(AMArmorMaterials.STEEL, ArmorItem.Type.BOOTS, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> FOOLS_GOLD_HELMET = register("fools_gold_helmet", () -> new ArmorItem(AMArmorMaterials.FOOLS_GOLD, ArmorItem.Type.HELMET, AMItems.getSettings()));
	public static final RegistrySupplier<Item> FOOLS_GOLD_CHESTPLATE = register("fools_gold_chestplate", () -> new ArmorItem(AMArmorMaterials.FOOLS_GOLD, ArmorItem.Type.CHESTPLATE, AMItems.getSettings()));
	public static final RegistrySupplier<Item> FOOLS_GOLD_LEGGINGS = register("fools_gold_leggings", () -> new ArmorItem(AMArmorMaterials.FOOLS_GOLD, ArmorItem.Type.LEGGINGS, AMItems.getSettings()));
	public static final RegistrySupplier<Item> FOOLS_GOLD_BOOTS = register("fools_gold_boots", () -> new ArmorItem(AMArmorMaterials.FOOLS_GOLD, ArmorItem.Type.BOOTS, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> METITE_HELMET = register("metite_helmet", () -> new ArmorItem(AMArmorMaterials.METITE, ArmorItem.Type.HELMET, AMItems.getSettings()));
	public static final RegistrySupplier<Item> METITE_CHESTPLATE = register("metite_chestplate", () -> new ArmorItem(AMArmorMaterials.METITE, ArmorItem.Type.CHESTPLATE, AMItems.getSettings()));
	public static final RegistrySupplier<Item> METITE_LEGGINGS = register("metite_leggings", () -> new ArmorItem(AMArmorMaterials.METITE, ArmorItem.Type.LEGGINGS, AMItems.getSettings()));
	public static final RegistrySupplier<Item> METITE_BOOTS = register("metite_boots", () -> new ArmorItem(AMArmorMaterials.METITE, ArmorItem.Type.BOOTS, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> ASTERITE_HELMET = register("asterite_helmet", () -> new ArmorItem(AMArmorMaterials.ASTERITE, ArmorItem.Type.HELMET, AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTERITE_CHESTPLATE = register("asterite_chestplate", () -> new ArmorItem(AMArmorMaterials.ASTERITE, ArmorItem.Type.CHESTPLATE, AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTERITE_LEGGINGS = register("asterite_leggings", () -> new ArmorItem(AMArmorMaterials.ASTERITE, ArmorItem.Type.LEGGINGS, AMItems.getSettings()));
	public static final RegistrySupplier<Item> ASTERITE_BOOTS = register("asterite_boots", () -> new ArmorItem(AMArmorMaterials.ASTERITE, ArmorItem.Type.BOOTS, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> STELLUM_HELMET = register("stellum_helmet", () -> new ArmorItem(AMArmorMaterials.STELLUM, ArmorItem.Type.HELMET, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> STELLUM_CHESTPLATE = register("stellum_chestplate", () -> new ArmorItem(AMArmorMaterials.STELLUM, ArmorItem.Type.CHESTPLATE, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> STELLUM_LEGGINGS = register("stellum_leggings", () -> new ArmorItem(AMArmorMaterials.STELLUM, ArmorItem.Type.LEGGINGS, AMItems.getSettings().fireproof()));
	public static final RegistrySupplier<Item> STELLUM_BOOTS = register("stellum_boots", () -> new ArmorItem(AMArmorMaterials.STELLUM, ArmorItem.Type.BOOTS, AMItems.getSettings().fireproof()));
	
	public static final RegistrySupplier<Item> GALAXIUM_HELMET = register("galaxium_helmet", () -> new ArmorItem(AMArmorMaterials.GALAXIUM, ArmorItem.Type.HELMET, AMItems.getSettings()));
	public static final RegistrySupplier<Item> GALAXIUM_CHESTPLATE = register("galaxium_chestplate", () -> new ArmorItem(AMArmorMaterials.GALAXIUM, ArmorItem.Type.CHESTPLATE, AMItems.getSettings()));
	public static final RegistrySupplier<Item> GALAXIUM_LEGGINGS = register("galaxium_leggings", () -> new ArmorItem(AMArmorMaterials.GALAXIUM, ArmorItem.Type.LEGGINGS, AMItems.getSettings()));
	public static final RegistrySupplier<Item> GALAXIUM_BOOTS = register("galaxium_boots", () -> new ArmorItem(AMArmorMaterials.GALAXIUM, ArmorItem.Type.BOOTS, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> UNIVITE_HELMET = register("univite_helmet", () -> new AnimatedArmorItem(AMArmorMaterials.UNIVITE, ArmorItem.Type.HELMET, AMItems.getSettings().fireproof(), 18));
	public static final RegistrySupplier<Item> UNIVITE_CHESTPLATE = register("univite_chestplate", () -> new AnimatedArmorItem(AMArmorMaterials.UNIVITE, ArmorItem.Type.CHESTPLATE, AMItems.getSettings().fireproof(), 18));
	public static final RegistrySupplier<Item> UNIVITE_LEGGINGS = register("univite_leggings", () -> new AnimatedArmorItem(AMArmorMaterials.UNIVITE, ArmorItem.Type.LEGGINGS, AMItems.getSettings().fireproof(), 18));
	public static final RegistrySupplier<Item> UNIVITE_BOOTS = register("univite_boots", () -> new AnimatedArmorItem(AMArmorMaterials.UNIVITE, ArmorItem.Type.BOOTS, AMItems.getSettings().fireproof(), 18));

	public static final RegistrySupplier<Item> LUNUM_HELMET = register("lunum_helmet", () -> new ArmorItem(AMArmorMaterials.LUNUM, ArmorItem.Type.HELMET, AMItems.getSettings()));
	public static final RegistrySupplier<Item> LUNUM_CHESTPLATE = register("lunum_chestplate", () -> new ArmorItem(AMArmorMaterials.LUNUM, ArmorItem.Type.CHESTPLATE, AMItems.getSettings()));
	public static final RegistrySupplier<Item> LUNUM_LEGGINGS = register("lunum_leggings", () -> new ArmorItem(AMArmorMaterials.LUNUM, ArmorItem.Type.LEGGINGS, AMItems.getSettings()));
	public static final RegistrySupplier<Item> LUNUM_BOOTS = register("lunum_boots", () -> new ArmorItem(AMArmorMaterials.LUNUM, ArmorItem.Type.BOOTS, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> METEORIC_STEEL_HELMET = register("meteoric_steel_helmet", () -> new ArmorItem(AMArmorMaterials.METEORIC_STEEL, ArmorItem.Type.HELMET, AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_CHESTPLATE = register("meteoric_steel_chestplate", () -> new ArmorItem(AMArmorMaterials.METEORIC_STEEL, ArmorItem.Type.CHESTPLATE, AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_LEGGINGS = register("meteoric_steel_leggings", () -> new ArmorItem(AMArmorMaterials.METEORIC_STEEL, ArmorItem.Type.LEGGINGS, AMItems.getSettings()));
	public static final RegistrySupplier<Item> METEORIC_STEEL_BOOTS = register("meteoric_steel_boots", () -> new ArmorItem(AMArmorMaterials.METEORIC_STEEL, ArmorItem.Type.BOOTS, AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> METEOR_METITE_ORE_CLUSTER = register("meteor_metite_ore_cluster", () -> new Item(AMItems.getSettings()));
	
	public static final RegistrySupplier<Item> FIRE_EXTINGUISHER = register("fire_extinguisher", () -> new FireExtinguisherItem(AMItems.getSettings().maxCount(1)));
	
	public static final FoodComponent FOOLS_GOLD_APPLE_COMPONENT = new FoodComponent.Builder().hunger(2).saturationModifier(0.4F).statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 400, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.LUCK, 4800, 0), 1.0F).alwaysEdible().build();
	public static final RegistrySupplier<Item> FOOLS_GOLD_APPLE = register("fools_gold_apple", () -> new Item(AMItems.getSettings().food(FOOLS_GOLD_APPLE_COMPONENT).rarity(Rarity.RARE)));
	public static final FoodComponent LEAD_APPLE_COMPONENT = new FoodComponent.Builder().hunger(2).saturationModifier(0.4F).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 40, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1000, 2), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 500, 1), 1.0F).alwaysEdible().build();
	public static final RegistrySupplier<Item> LEAD_APPLE = register("lead_apple", () -> new Item(AMItems.getSettings().food(LEAD_APPLE_COMPONENT).rarity(Rarity.RARE)));
	
	public static final RegistrySupplier<Item> BLADES = register("blades", () -> new Item(getSettings()));
	
	public static final RegistrySupplier<Item> BIOFUEL = register("biofuel", () -> new Item(getSettings()));
	
	public static final RegistrySupplier<Item> MACHINE_CHASSIS = register("machine_chassis", () -> new Item(getSettings()));
	
	public static final RegistrySupplier<Item> BASIC_MACHINE_UPGRADE_KIT = register("basic_machine_upgrade_kit", () -> new MachineUpgradeKitItem(Tier.BASIC, getSettings()));
	public static final RegistrySupplier<Item> ADVANCED_MACHINE_UPGRADE_KIT = register("advanced_machine_upgrade_kit", () -> new MachineUpgradeKitItem(Tier.ADVANCED, getSettings()));
	public static final RegistrySupplier<Item> ELITE_MACHINE_UPGRADE_KIT = register("elite_machine_upgrade_kit", () -> new MachineUpgradeKitItem(Tier.ELITE, getSettings()));
	
	public static final RegistrySupplier<Item> PRIMITIVE_PLATING = register("primitive_plating", () -> new Item(getSettings()));
	public static final RegistrySupplier<Item> BASIC_PLATING = register("basic_plating", () -> new Item(getSettings()));
	public static final RegistrySupplier<Item> ADVANCED_PLATING = register("advanced_plating", () -> new Item(getSettings()));
	public static final RegistrySupplier<Item> ELITE_PLATING = register("elite_plating", () -> new Item(getSettings()));
	
	public static final RegistrySupplier<Item> PORTABLE_TANK = register("portable_tank", () -> new SimpleFluidStorageItem(getSettings().maxCount(1), AMConfig.get().items.portableTanks.regular));
	public static final RegistrySupplier<Item> LARGE_PORTABLE_TANK = register("large_portable_tank", () -> new SimpleFluidStorageItem(getSettings().maxCount(1), AMConfig.get().items.portableTanks.large));
	
	public static final RegistrySupplier<Item> PRIMITIVE_CIRCUIT = register("primitive_circuit", () -> new Item(getSettings()));
	public static final RegistrySupplier<Item> BASIC_CIRCUIT = register("basic_circuit", () -> new Item(getSettings()));
	public static final RegistrySupplier<Item> ADVANCED_CIRCUIT = register("advanced_circuit", () -> new Item(getSettings()));
	public static final RegistrySupplier<Item> ELITE_CIRCUIT = register("elite_circuit", () -> new Item(getSettings()));
	
	public static final RegistrySupplier<Item> PRIMITIVE_BATTERY = register("primitive_battery", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), AMConfig.get().items.batteries.singleBatteries.primitive));
	public static final RegistrySupplier<Item> BASIC_BATTERY = register("basic_battery", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), AMConfig.get().items.batteries.singleBatteries.basic));
	public static final RegistrySupplier<Item> ADVANCED_BATTERY = register("advanced_battery", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), AMConfig.get().items.batteries.singleBatteries.advanced));
	public static final RegistrySupplier<Item> ELITE_BATTERY = register("elite_battery", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), AMConfig.get().items.batteries.singleBatteries.elite));
	public static final RegistrySupplier<Item> CREATIVE_BATTERY = register("creative_battery", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), Long.MAX_VALUE));
	
	public static final RegistrySupplier<Item> PRIMITIVE_BATTERY_PACK = register("primitive_battery_pack", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), AMConfig.get().items.batteries.batteryPacks.primitive));
	public static final RegistrySupplier<Item> BASIC_BATTERY_PACK = register("basic_battery_pack", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), AMConfig.get().items.batteries.batteryPacks.basic));
	public static final RegistrySupplier<Item> ADVANCED_BATTERY_PACK = register("advanced_battery_pack", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), AMConfig.get().items.batteries.batteryPacks.advanced));
	public static final RegistrySupplier<Item> ELITE_BATTERY_PACK = register("elite_battery_pack", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), AMConfig.get().items.batteries.batteryPacks.elite));
	public static final RegistrySupplier<Item> CREATIVE_BATTERY_PACK = register("creative_battery_pack", () -> new SimpleEnergyStorageItem(getSettings().maxCount(1), Long.MAX_VALUE));
	
	public static final RegistrySupplier<Item> PRIMITIVE_DRILL = register("primitive_drill", () -> new DrillItem(AMToolMaterials.PRIMITIVE_DRILL, 1.0F, -2.8F, AMConfig.get().items.primitiveDrillEnergy, getSettings().maxCount(1)));
	public static final RegistrySupplier<Item> BASIC_DRILL = register("basic_drill", () -> new DrillItem(AMToolMaterials.BASIC_DRILL, 1.0F, -2.8F, AMConfig.get().items.basicDrillEnergy, getSettings().maxCount(1)));
	public static final RegistrySupplier<Item> ADVANCED_DRILL = register("advanced_drill", () -> new DrillItem(AMToolMaterials.ADVANCED_DRILL, 1.0F, -2.8F, AMConfig.get().items.advancedDrillEnergy, getSettings().maxCount(1)));
	public static final RegistrySupplier<Item> ELITE_DRILL = register("elite_drill", () -> new DrillItem(AMToolMaterials.ELITE_DRILL, 1.0F, -2.8F, AMConfig.get().items.eliteDrillEnergy, getSettings().maxCount(1)));
	
	public static final RegistrySupplier<Item> DRILL_HEAD = register("drill_head", () -> new Item(getSettings()));
	
	public static final RegistrySupplier<Item> PRIMITIVE_DRILL_BASE = register("primitive_drill_base", () -> new Item(getSettings()));
	public static final RegistrySupplier<Item> BASIC_DRILL_BASE = register("basic_drill_base", () -> new Item(getSettings()));
	public static final RegistrySupplier<Item> ADVANCED_DRILL_BASE = register("advanced_drill_base", () -> new Item(getSettings()));
	public static final RegistrySupplier<Item> ELITE_DRILL_BASE = register("elite_drill_base", () -> new Item(getSettings()));
	
	public static final RegistrySupplier<Item> HOLOGRAPHIC_CONNECTOR = register("holographic_connector", () -> new HolographicConnectorItem(getSettings().maxCount(1)));
	
	public static final RegistrySupplier<Item> GRAVITY_GAUNTLET = register("gravity_gauntlet", () -> new GravityGauntletItem(getSettings().maxCount(1), AMConfig.get().items.gravityGauntletEnergy));
	
	public static void init() {
	
	}
	
	/**
	 * @param name Name of item instance to be registered
	 * @param item Item instance to be registered
	 *
	 * @return Item instance registered
	 */
	public static <T extends Item> RegistrySupplier<T> register(String name, Supplier<T> item) {
		return register(AMCommon.id(name), item);
	}
	
	/**
	 * @param name Identifier of item instance to be registered
	 * @param item Item instance to be registered
	 *
	 * @return Item instance registered
	 */
	public static <T extends Item> RegistrySupplier<T> register(Identifier name, Supplier<T> item) {
		RegistrySupplier<T> i = AMCommon.registry(RegistryKeys.ITEM).register(name, item);
		AMItemGroups.addToDefault(i.get());
		
		return i;
	}
	
	/**
	 * @param name Name of item instance to be registered
	 * @param item Item instance to be registered
	 *
	 * @return Item instance registered
	 */
	public static <T extends Item> RegistrySupplier<T> registerNoGroup(String name, Supplier<T> item) {
		return registerNoGroup(AMCommon.id(name), item);
	}
	
	/**
	 * @param name Identifier of item instance to be registered
	 * @param item Item instance to be registered
	 *
	 * @return Item instance registered
	 */
	public static <T extends Item> RegistrySupplier<T> registerNoGroup(Identifier name, Supplier<T> item) {
		return AMCommon.registry(RegistryKeys.ITEM).register(name, item);
	}
	
	public static Item.Settings getSettings() {
		return new Item.Settings();
	}
}
