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

package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.item.DrillItem;
import com.github.chainmailstudios.astromine.common.item.FireExtinguisherItem;
import com.github.chainmailstudios.astromine.common.item.HolographicConnectorItem;
import com.github.chainmailstudios.astromine.common.item.MeteorSpawnerDevItem;
import com.github.chainmailstudios.astromine.common.item.MultitoolItem;
import com.github.chainmailstudios.astromine.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.common.item.SuperSpaceSlimeShooterItem;
import com.github.chainmailstudios.astromine.common.item.UncoloredSpawnEggItem;
import com.github.chainmailstudios.astromine.common.item.WrenchItem;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.item.weapon.AmmunitionItem;
import com.github.chainmailstudios.astromine.common.item.weapon.GravityGauntletItem;
import com.github.chainmailstudios.astromine.common.item.weapon.variant.Weaponry;
import draylar.magna.item.ExcavatorItem;
import draylar.magna.item.HammerItem;

public class AstromineItems {
	// Things
	public static final Item ENERGY = register("energy", new Item(new Item.Settings()));
	public static final Item FLUID = register("fluid", new Item(new Item.Settings()));
	public static final Item ITEM = register("item", new Item(new Item.Settings()));

	// Spawn eggs
	public static final Item SPACE_SLIME_SPAWN_EGG = register("space_slime_spawn_egg", new UncoloredSpawnEggItem(AstromineEntityTypes.SPACE_SLIME, getBasicSettings()));
	public static final Item ROCKET = register("rocket", new UncoloredSpawnEggItem(AstromineEntityTypes.ROCKET, getBasicSettings()));

	// Misc materials
	public static final Item SPACE_SLIME_BALL = register("space_slime_ball", new Item(getBasicSettings()));
	public static final Item YEAST = register("yeast", new Item(getBasicSettings()));
	public static final Item GRAPHITE_SHEET = register("graphite_sheet", new Item(getBasicSettings()));

	// Fantasy weaponry
	public static final Item GRAVITY_GAUNTLET = register("gravity_gauntlet", new GravityGauntletItem(getBasicSettings().maxCount(1), AstromineConfig.get().gravityGauntletEnergy));

	// Realistic tooling
	public static final Item FIRE_EXTINGUISHER = register("fire_extinguisher", new FireExtinguisherItem(getBasicSettings().maxCount(1)));

	// Materials - Fragments
	public static final Item ASTERITE_FRAGMENT = register("asterite_fragment", new Item(getBasicSettings()));
	public static final Item GALAXIUM_FRAGMENT = register("galaxium_fragment", new Item(getBasicSettings()));
	public static final Item DIAMOND_FRAGMENT = register("diamond_fragment", new Item(getBasicSettings()));
	public static final Item EMERALD_FRAGMENT = register("emerald_fragment", new Item(getBasicSettings()));
	public static final Item QUARTZ_FRAGMENT = register("quartz_fragment", new Item(getBasicSettings()));

	// Materials - Gems
	public static final Item ASTERITE = register("asterite", new Item(getBasicSettings()));
	public static final Item GALAXIUM = register("galaxium", new Item(getBasicSettings()));

	// Materials - Nuggets
	public static final Item METITE_NUGGET = register("metite_nugget", new Item(getBasicSettings()));
	public static final Item STELLUM_NUGGET = register("stellum_nugget", new Item(getBasicSettings().fireproof()));
	public static final Item UNIVITE_NUGGET = register("univite_nugget", new Item(getBasicSettings().fireproof()));

	public static final Item COPPER_NUGGET = register("copper_nugget", new Item(getBasicSettings()));
	public static final Item TIN_NUGGET = register("tin_nugget", new Item(getBasicSettings()));
	public static final Item SILVER_NUGGET = register("silver_nugget", new Item(getBasicSettings()));
	public static final Item LEAD_NUGGET = register("lead_nugget", new Item(getBasicSettings()));

	public static final Item STEEL_NUGGET = register("steel_nugget", new Item(getBasicSettings()));
	public static final Item BRONZE_NUGGET = register("bronze_nugget", new Item(getBasicSettings()));
	public static final Item ELECTRUM_NUGGET = register("electrum_nugget", new Item(getBasicSettings()));
	public static final Item ROSE_GOLD_NUGGET = register("rose_gold_nugget", new Item(getBasicSettings()));
	public static final Item STERLING_SILVER_NUGGET = register("sterling_silver_nugget", new Item(getBasicSettings()));
	public static final Item FOOLS_GOLD_NUGGET = register("fools_gold_nugget", new Item(getBasicSettings()));

	public static final Item NETHERITE_NUGGET = register("netherite_nugget", new Item(getBasicSettings().fireproof()));

	// Materials - Ingots
	public static final Item METITE_INGOT = register("metite_ingot", new Item(getBasicSettings()));
	public static final Item STELLUM_INGOT = register("stellum_ingot", new Item(getBasicSettings().fireproof()));
	public static final Item UNIVITE_INGOT = register("univite_ingot", new Item(getBasicSettings().fireproof()));

	public static final Item COPPER_INGOT = register("copper_ingot", new Item(getBasicSettings()));
	public static final Item TIN_INGOT = register("tin_ingot", new Item(getBasicSettings()));
	public static final Item SILVER_INGOT = register("silver_ingot", new Item(getBasicSettings()));
	public static final Item LEAD_INGOT = register("lead_ingot", new Item(getBasicSettings()));

	public static final Item BRONZE_INGOT = register("bronze_ingot", new Item(getBasicSettings()));
	public static final Item STEEL_INGOT = register("steel_ingot", new Item(getBasicSettings()));
	public static final Item ELECTRUM_INGOT = register("electrum_ingot", new Item(getBasicSettings()));
	public static final Item ROSE_GOLD_INGOT = register("rose_gold_ingot", new Item(getBasicSettings()));
	public static final Item STERLING_SILVER_INGOT = register("sterling_silver_ingot", new Item(getBasicSettings()));
	public static final Item FOOLS_GOLD_INGOT = register("fools_gold_ingot", new Item(getBasicSettings()));

	// Materials - Wires
	public static final Item COPPER_WIRE = register("copper_wire", new Item(getBasicSettings()));
	public static final Item TIN_WIRE = register("tin_wire", new Item(getBasicSettings()));
	public static final Item SILVER_WIRE = register("silver_wire", new Item(getBasicSettings()));
	public static final Item LEAD_WIRE = register("lead_wire", new Item(getBasicSettings()));

	public static final Item STEEL_WIRE = register("steel_wire", new Item(getBasicSettings()));
	public static final Item ELECTRUM_WIRE = register("electrum_wire", new Item(getBasicSettings()));

	public static final Item GOLD_WIRE = register("gold_wire", new Item(getBasicSettings()));

	// Materials - Clusters
	public static final Item METEOR_METITE_CLUSTER = register("meteor_metite_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_METITE_CLUSTER = register("asteroid_metite_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_ASTERITE_CLUSTER = register("asteroid_asterite_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_STELLUM_CLUSTER = register("asteroid_stellum_cluster", new Item(getBasicSettings().fireproof()));
	public static final Item ASTEROID_GALAXIUM_CLUSTER = register("asteroid_galaxium_cluster", new Item(getBasicSettings()));

	public static final Item ASTEROID_COPPER_CLUSTER = register("asteroid_copper_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_TIN_CLUSTER = register("asteroid_tin_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_SILVER_CLUSTER = register("asteroid_silver_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_LEAD_CLUSTER = register("asteroid_lead_cluster", new Item(getBasicSettings()));

	public static final Item ASTEROID_COAL_CLUSTER = register("asteroid_coal_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_IRON_CLUSTER = register("asteroid_iron_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_GOLD_CLUSTER = register("asteroid_gold_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_REDSTONE_CLUSTER = register("asteroid_redstone_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_LAPIS_CLUSTER = register("asteroid_lapis_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_DIAMOND_CLUSTER = register("asteroid_diamond_cluster", new Item(getBasicSettings()));
	public static final Item ASTEROID_EMERALD_CLUSTER = register("asteroid_emerald_cluster", new Item(getBasicSettings()));

	// Materials - Dusts
	public static final Item METITE_DUST = register("metite_dust", new Item(getBasicSettings()));
	public static final Item ASTERITE_DUST = register("asterite_dust", new Item(getBasicSettings()));
	public static final Item STELLUM_DUST = register("stellum_dust", new Item(getBasicSettings().fireproof()));
	public static final Item GALAXIUM_DUST = register("galaxium_dust", new Item(getBasicSettings()));
	public static final Item UNIVITE_DUST = register("univite_dust", new Item(getBasicSettings().fireproof()));

	public static final Item COPPER_DUST = register("copper_dust", new Item(getBasicSettings()));
	public static final Item TIN_DUST = register("tin_dust", new Item(getBasicSettings()));
	public static final Item SILVER_DUST = register("silver_dust", new Item(getBasicSettings()));
	public static final Item LEAD_DUST = register("lead_dust", new Item(getBasicSettings()));

	public static final Item STEEL_DUST = register("steel_dust", new Item(getBasicSettings()));
	public static final Item BRONZE_DUST = register("bronze_dust", new Item(getBasicSettings()));
	public static final Item ELECTRUM_DUST = register("electrum_dust", new Item(getBasicSettings()));
	public static final Item ROSE_GOLD_DUST = register("rose_gold_dust", new Item(getBasicSettings()));
	public static final Item STERLING_SILVER_DUST = register("sterling_silver_dust", new Item(getBasicSettings()));
	public static final Item FOOLS_GOLD_DUST = register("fools_gold_dust", new Item(getBasicSettings()));

	public static final Item IRON_DUST = register("iron_dust", new Item(getBasicSettings()));
	public static final Item GOLD_DUST = register("gold_dust", new Item(getBasicSettings()));
	public static final Item LAPIS_DUST = register("lapis_dust", new Item(getBasicSettings()));
	public static final Item DIAMOND_DUST = register("diamond_dust", new Item(getBasicSettings()));
	public static final Item EMERALD_DUST = register("emerald_dust", new Item(getBasicSettings()));
	public static final Item NETHERITE_DUST = register("netherite_dust", new Item(getBasicSettings().fireproof()));
	public static final Item COAL_DUST = register("coal_dust", new Item(getBasicSettings()));
	public static final Item CHARCOAL_DUST = register("charcoal_dust", new Item(getBasicSettings()));
	public static final Item QUARTZ_DUST = register("quartz_dust", new Item(getBasicSettings()));
	public static final Item RAW_NETHERITE_DUST = register("raw_netherite_dust", new Item(getBasicSettings()));

	// Materials - Plates
	public static final Item METITE_PLATES = register("metite_plates", new Item(getBasicSettings()));
	public static final Item STELLUM_PLATES = register("stellum_plates", new Item(getBasicSettings().fireproof()));
	public static final Item UNIVITE_PLATES = register("univite_plates", new Item(getBasicSettings().fireproof()));

	public static final Item COPPER_PLATES = register("copper_plates", new Item(getBasicSettings()));
	public static final Item TIN_PLATES = register("tin_plates", new Item(getBasicSettings()));
	public static final Item SILVER_PLATES = register("silver_plates", new Item(getBasicSettings()));
	public static final Item LEAD_PLATES = register("lead_plates", new Item(getBasicSettings()));

	public static final Item STEEL_PLATES = register("steel_plates", new Item(getBasicSettings()));
	public static final Item BRONZE_PLATES = register("bronze_plates", new Item(getBasicSettings()));
	public static final Item ELECTRUM_PLATES = register("electrum_plates", new Item(getBasicSettings()));
	public static final Item ROSE_GOLD_PLATES = register("rose_gold_plates", new Item(getBasicSettings()));
	public static final Item STERLING_SILVER_PLATES = register("sterling_silver_plates", new Item(getBasicSettings()));
	public static final Item FOOLS_GOLD_PLATES = register("fools_gold_plates", new Item(getBasicSettings()));

	public static final Item IRON_PLATES = register("iron_plates", new Item(getBasicSettings()));
	public static final Item GOLD_PLATES = register("gold_plates", new Item(getBasicSettings()));
	public static final Item NETHERITE_PLATES = register("netherite_plates", new Item(getBasicSettings().fireproof()));

	// Materials - Gears
	public static final Item METITE_GEAR = register("metite_gear", new Item(getBasicSettings()));
	public static final Item STELLUM_GEAR = register("stellum_gear", new Item(getBasicSettings().fireproof()));
	public static final Item UNIVITE_GEAR = register("univite_gear", new Item(getBasicSettings().fireproof()));

	public static final Item COPPER_GEAR = register("copper_gear", new Item(getBasicSettings()));
	public static final Item TIN_GEAR = register("tin_gear", new Item(getBasicSettings()));
	public static final Item SILVER_GEAR = register("silver_gear", new Item(getBasicSettings()));
	public static final Item LEAD_GEAR = register("lead_gear", new Item(getBasicSettings()));

	public static final Item STEEL_GEAR = register("steel_gear", new Item(getBasicSettings()));
	public static final Item BRONZE_GEAR = register("bronze_gear", new Item(getBasicSettings()));
	public static final Item ELECTRUM_GEAR = register("electrum_gear", new Item(getBasicSettings()));
	public static final Item ROSE_GOLD_GEAR = register("rose_gold_gear", new Item(getBasicSettings()));
	public static final Item STERLING_SILVER_GEAR = register("sterling_silver_gear", new Item(getBasicSettings()));
	public static final Item FOOLS_GOLD_GEAR = register("fools_gold_gear", new Item(getBasicSettings()));

	public static final Item IRON_GEAR = register("iron_gear", new Item(getBasicSettings()));
	public static final Item GOLD_GEAR = register("gold_gear", new Item(getBasicSettings()));
	public static final Item NETHERITE_GEAR = register("netherite_gear", new Item(getBasicSettings().fireproof()));

	// Circuits
	public static final Item BASIC_CIRCUIT = register("basic_circuit", new Item(getBasicSettings()));
	public static final Item ADVANCED_CIRCUIT = register("advanced_circuit", new Item(getBasicSettings()));
	public static final Item ELITE_CIRCUIT = register("elite_circuit", new Item(getBasicSettings()));

	// Machine Chassis
	public static final Item PRIMITIVE_MACHINE_CHASSIS = register("primitive_machine_chassis", new Item(getBasicSettings()));
	public static final Item BASIC_MACHINE_CHASSIS = register("basic_machine_chassis", new Item(getBasicSettings()));
	public static final Item ADVANCED_MACHINE_CHASSIS = register("advanced_machine_chassis", new Item(getBasicSettings()));
	public static final Item ELITE_MACHINE_CHASSIS = register("elite_machine_chassis", new Item(getBasicSettings()));

	// Gas Canisters
	public static final Item GAS_CANISTER = register("gas_canister", FluidVolumeItem.of(getBasicSettings(), Fraction.of(8, 1)));
	public static final Item PRESSURIZED_GAS_CANISTER = register("pressurized_gas_canister", FluidVolumeItem.of(getBasicSettings(), Fraction.of(32, 1)));

	// Batteries
	public static final Item BASIC_BATTERY = register("basic_battery", EnergyVolumeItem.of(getBasicSettings(), 9000));
	public static final Item ADVANCED_BATTERY = register("advanced_battery", EnergyVolumeItem.of(getBasicSettings(), 24000));
	public static final Item ELITE_BATTERY = register("elite_battery", EnergyVolumeItem.of(getBasicSettings(), 64000));
	public static final Item CREATIVE_BATTERY = register("creative_battery", EnergyVolumeItem.ofCreative(getBasicSettings()));

	// Misc Tools
	public static final Item HOLOGRAPHIC_CONNECTOR = register("holographic_connector", new HolographicConnectorItem(getBasicSettings().maxCount(1)));
	public static final Item BRONZE_WRENCH = register("bronze_wrench", new WrenchItem(getBasicSettings()));

	// Drills
	public static final Item BASIC_DRILL = register("basic_drill", new DrillItem(AstromineToolMaterials.BASIC_DRILL, 1, -2.8F, 1, 90000, getBasicSettings().maxCount(1)));
	public static final Item ADVANCED_DRILL = register("advanced_drill", new DrillItem(AstromineToolMaterials.ADVANCED_DRILL, 1, -2.8F, 1, 240000, getBasicSettings().maxCount(1)));
	public static final Item ELITE_DRILL = register("elite_drill", new DrillItem(AstromineToolMaterials.ELITE_DRILL, 1, -2.8F, 1, 640000, getBasicSettings().maxCount(1)));

	// Standard Tools
	public static final Item WOODEN_MINING_TOOL = register("wooden_mining_tool", new MultitoolItem((MiningToolItem) Items.WOODEN_SHOVEL, (MiningToolItem) Items.WOODEN_PICKAXE, ToolMaterials.WOOD, getBasicSettings()));
	public static final Item WOODEN_MATTOCK = register("wooden_mattock", new MultitoolItem((MiningToolItem) Items.WOODEN_HOE, (MiningToolItem) Items.WOODEN_AXE, ToolMaterials.WOOD, getBasicSettings()));

	public static final Item STONE_MINING_TOOL = register("stone_mining_tool", new MultitoolItem((MiningToolItem) Items.STONE_SHOVEL, (MiningToolItem) Items.STONE_PICKAXE, ToolMaterials.STONE, getBasicSettings()));
	public static final Item STONE_MATTOCK = register("stone_mattock", new MultitoolItem((MiningToolItem) Items.STONE_HOE, (MiningToolItem) Items.STONE_AXE, ToolMaterials.STONE, getBasicSettings()));

	public static final Item IRON_MINING_TOOL = register("iron_mining_tool", new MultitoolItem((MiningToolItem) Items.IRON_SHOVEL, (MiningToolItem) Items.IRON_PICKAXE, ToolMaterials.IRON, getBasicSettings()));
	public static final Item IRON_MATTOCK = register("iron_mattock", new MultitoolItem((MiningToolItem) Items.IRON_HOE, (MiningToolItem) Items.IRON_AXE, ToolMaterials.IRON, getBasicSettings()));

	public static final Item GOLDEN_MINING_TOOL = register("golden_mining_tool", new MultitoolItem((MiningToolItem) Items.GOLDEN_SHOVEL, (MiningToolItem) Items.GOLDEN_PICKAXE, ToolMaterials.GOLD, getBasicSettings()));
	public static final Item GOLDEN_MATTOCK = register("golden_mattock", new MultitoolItem((MiningToolItem) Items.GOLDEN_HOE, (MiningToolItem) Items.GOLDEN_AXE, ToolMaterials.GOLD, getBasicSettings()));

	public static final Item DIAMOND_MINING_TOOL = register("diamond_mining_tool", new MultitoolItem((MiningToolItem) Items.DIAMOND_SHOVEL, (MiningToolItem) Items.DIAMOND_PICKAXE, ToolMaterials.DIAMOND, getBasicSettings()));
	public static final Item DIAMOND_MATTOCK = register("diamond_mattock", new MultitoolItem((MiningToolItem) Items.DIAMOND_HOE, (MiningToolItem) Items.DIAMOND_AXE, ToolMaterials.DIAMOND, getBasicSettings()));

	public static final Item NETHERITE_MINING_TOOL = register("netherite_mining_tool", new MultitoolItem((MiningToolItem) Items.NETHERITE_SHOVEL, (MiningToolItem) Items.NETHERITE_PICKAXE, ToolMaterials.NETHERITE, getBasicSettings().fireproof()));
	public static final Item NETHERITE_MATTOCK = register("netherite_mattock", new MultitoolItem((MiningToolItem) Items.NETHERITE_HOE, (MiningToolItem) Items.NETHERITE_AXE, ToolMaterials.NETHERITE, getBasicSettings().fireproof()));

	public static final PickaxeItem COPPER_PICKAXE = register("copper_pickaxe", new PickaxeItem(AstromineToolMaterials.COPPER, 1, -2.8f, getBasicSettings()));
	public static final AxeItem COPPER_AXE = register("copper_axe", new AxeItem(AstromineToolMaterials.COPPER, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem COPPER_SHOVEL = register("copper_shovel", new ShovelItem(AstromineToolMaterials.COPPER, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem COPPER_HOE = register("copper_hoe", new HoeItem(AstromineToolMaterials.COPPER, -1, 0f, getBasicSettings()));
	public static final Item COPPER_SWORD = register("copper_sword", new SwordItem(AstromineToolMaterials.COPPER, 3, -2.4f, getBasicSettings()));
	public static final Item COPPER_MINING_TOOL = register("copper_mining_tool", new MultitoolItem(COPPER_PICKAXE, COPPER_SHOVEL, AstromineToolMaterials.COPPER, getBasicSettings()));
	public static final Item COPPER_MATTOCK = register("copper_mattock", new MultitoolItem(COPPER_HOE, COPPER_AXE, AstromineToolMaterials.COPPER, getBasicSettings()));
	public static final Item COPPER_HAMMER = register("copper_hammer", new HammerItem(AstromineToolMaterials.COPPER, 1, -2.8f, getBasicSettings()));
	public static final Item COPPER_EXCAVATOR = register("copper_excavator", new ExcavatorItem(AstromineToolMaterials.COPPER, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem TIN_PICKAXE = register("tin_pickaxe", new PickaxeItem(AstromineToolMaterials.TIN, 1, -2.8f, getBasicSettings()));
	public static final AxeItem TIN_AXE = register("tin_axe", new AxeItem(AstromineToolMaterials.TIN, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem TIN_SHOVEL = register("tin_shovel", new ShovelItem(AstromineToolMaterials.TIN, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem TIN_HOE = register("tin_hoe", new HoeItem(AstromineToolMaterials.TIN, -1, 0f, getBasicSettings()));
	public static final Item TIN_SWORD = register("tin_sword", new SwordItem(AstromineToolMaterials.TIN, 3, -2.4f, getBasicSettings()));
	public static final Item TIN_MINING_TOOL = register("tin_mining_tool", new MultitoolItem(TIN_SHOVEL, TIN_PICKAXE, AstromineToolMaterials.TIN, getBasicSettings()));
	public static final Item TIN_MATTOCK = register("tin_mattock", new MultitoolItem(TIN_HOE, TIN_AXE, AstromineToolMaterials.TIN, getBasicSettings()));
	public static final Item TIN_HAMMER = register("tin_hammer", new HammerItem(AstromineToolMaterials.TIN, 1, -2.8f, getBasicSettings()));
	public static final Item TIN_EXCAVATOR = register("tin_excavator", new ExcavatorItem(AstromineToolMaterials.TIN, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem SILVER_PICKAXE = register("silver_pickaxe", new PickaxeItem(AstromineToolMaterials.SILVER, 1, -2.8f, getBasicSettings()));
	public static final AxeItem SILVER_AXE = register("silver_axe", new AxeItem(AstromineToolMaterials.SILVER, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem SILVER_SHOVEL = register("silver_shovel", new ShovelItem(AstromineToolMaterials.SILVER, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem SILVER_HOE = register("silver_hoe", new HoeItem(AstromineToolMaterials.SILVER, -2, 0f, getBasicSettings()));
	public static final Item SILVER_SWORD = register("silver_sword", new SwordItem(AstromineToolMaterials.SILVER, 3, -2.4f, getBasicSettings()));
	public static final Item SILVER_MINING_TOOL = register("silver_mining_tool", new MultitoolItem(SILVER_PICKAXE, SILVER_SHOVEL, AstromineToolMaterials.SILVER, getBasicSettings()));
	public static final Item SILVER_MATTOCK = register("silver_mattock", new MultitoolItem(SILVER_HOE, SILVER_AXE, AstromineToolMaterials.SILVER, getBasicSettings()));
	public static final Item SILVER_HAMMER = register("silver_hammer", new HammerItem(AstromineToolMaterials.SILVER, 1, -2.8f, getBasicSettings()));
	public static final Item SILVER_EXCAVATOR = register("silver_excavator", new ExcavatorItem(AstromineToolMaterials.SILVER, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem LEAD_PICKAXE = register("lead_pickaxe", new PickaxeItem(AstromineToolMaterials.LEAD, 1, -2.8f, getBasicSettings()));
	public static final AxeItem LEAD_AXE = register("lead_axe", new AxeItem(AstromineToolMaterials.LEAD, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem LEAD_SHOVEL = register("lead_shovel", new ShovelItem(AstromineToolMaterials.LEAD, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem LEAD_HOE = register("lead_hoe", new HoeItem(AstromineToolMaterials.LEAD, -2, 0f, getBasicSettings()));
	public static final Item LEAD_SWORD = register("lead_sword", new SwordItem(AstromineToolMaterials.LEAD, 3, -2.4f, getBasicSettings()));
	public static final Item LEAD_MINING_TOOL = register("lead_mining_tool", new MultitoolItem(LEAD_PICKAXE, LEAD_SHOVEL, AstromineToolMaterials.LEAD, getBasicSettings()));
	public static final Item LEAD_MATTOCK = register("lead_mattock", new MultitoolItem(LEAD_HOE, LEAD_AXE, AstromineToolMaterials.LEAD, getBasicSettings()));
	public static final Item LEAD_HAMMER = register("lead_hammer", new HammerItem(AstromineToolMaterials.LEAD, 1, -2.8f, getBasicSettings()));
	public static final Item LEAD_EXCAVATOR = register("lead_excavator", new ExcavatorItem(AstromineToolMaterials.LEAD, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem BRONZE_PICKAXE = register("bronze_pickaxe", new PickaxeItem(AstromineToolMaterials.BRONZE, 1, -2.8f, getBasicSettings()));
	public static final AxeItem BRONZE_AXE = register("bronze_axe", new AxeItem(AstromineToolMaterials.BRONZE, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem BRONZE_SHOVEL = register("bronze_shovel", new ShovelItem(AstromineToolMaterials.BRONZE, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem BRONZE_HOE = register("bronze_hoe", new HoeItem(AstromineToolMaterials.BRONZE, -2, 0f, getBasicSettings()));
	public static final Item BRONZE_SWORD = register("bronze_sword", new SwordItem(AstromineToolMaterials.BRONZE, 3, -2.4f, getBasicSettings()));
	public static final Item BRONZE_MINING_TOOL = register("bronze_mining_tool", new MultitoolItem(BRONZE_SHOVEL, BRONZE_PICKAXE, AstromineToolMaterials.BRONZE, getBasicSettings()));
	public static final Item BRONZE_MATTOCK = register("bronze_mattock", new MultitoolItem(BRONZE_HOE, BRONZE_AXE, AstromineToolMaterials.BRONZE, getBasicSettings()));
	public static final Item BRONZE_HAMMER = register("bronze_hammer", new HammerItem(AstromineToolMaterials.BRONZE, 1, -2.8f, getBasicSettings()));
	public static final Item BRONZE_EXCAVATOR = register("bronze_excavator", new ExcavatorItem(AstromineToolMaterials.BRONZE, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem STEEL_PICKAXE = register("steel_pickaxe", new PickaxeItem(AstromineToolMaterials.STEEL, 1, -2.8f, getBasicSettings()));
	public static final AxeItem STEEL_AXE = register("steel_axe", new AxeItem(AstromineToolMaterials.STEEL, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem STEEL_SHOVEL = register("steel_shovel", new ShovelItem(AstromineToolMaterials.STEEL, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem STEEL_HOE = register("steel_hoe", new HoeItem(AstromineToolMaterials.STEEL, -3, 0f, getBasicSettings()));
	public static final Item STEEL_SWORD = register("steel_sword", new SwordItem(AstromineToolMaterials.STEEL, 3, -2.4f, getBasicSettings()));
	public static final Item STEEL_MINING_TOOL = register("steel_mining_tool", new MultitoolItem(STEEL_SHOVEL, STEEL_PICKAXE, AstromineToolMaterials.STEEL, getBasicSettings()));
	public static final Item STEEL_MATTOCK = register("steel_mattock", new MultitoolItem(STEEL_HOE, STEEL_AXE, AstromineToolMaterials.STEEL, getBasicSettings()));
	public static final Item STEEL_HAMMER = register("steel_hammer", new HammerItem(AstromineToolMaterials.STEEL, 1, -2.8f, getBasicSettings()));
	public static final Item STEEL_EXCAVATOR = register("steel_excavator", new ExcavatorItem(AstromineToolMaterials.STELLUM, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem ELECTRUM_PICKAXE = register("electrum_pickaxe", new PickaxeItem(AstromineToolMaterials.ELECTRUM, 1, -2.8f, getBasicSettings()));
	public static final AxeItem ELECTRUM_AXE = register("electrum_axe", new AxeItem(AstromineToolMaterials.ELECTRUM, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem ELECTRUM_SHOVEL = register("electrum_shovel", new ShovelItem(AstromineToolMaterials.ELECTRUM, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem ELECTRUM_HOE = register("electrum_hoe", new HoeItem(AstromineToolMaterials.ELECTRUM, -1, 0f, getBasicSettings()));
	public static final Item ELECTRUM_SWORD = register("electrum_sword", new SwordItem(AstromineToolMaterials.ELECTRUM, 3, -2.4f, getBasicSettings()));
	public static final Item ELECTRUM_MINING_TOOL = register("electrum_mining_tool", new MultitoolItem(ELECTRUM_SHOVEL, ELECTRUM_PICKAXE, AstromineToolMaterials.ELECTRUM, getBasicSettings()));
	public static final Item ELECTRUM_MATTOCK = register("electrum_mattock", new MultitoolItem(ELECTRUM_HOE, ELECTRUM_AXE, AstromineToolMaterials.ELECTRUM, getBasicSettings()));
	public static final Item ELECTRUM_HAMMER = register("electrum_hammer", new HammerItem(AstromineToolMaterials.ELECTRUM, 1, -2.8f, getBasicSettings()));
	public static final Item ELECTRUM_EXCAVATOR = register("electrum_excavator", new ExcavatorItem(AstromineToolMaterials.SILVER, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem ROSE_GOLD_PICKAXE = register("rose_gold_pickaxe", new PickaxeItem(AstromineToolMaterials.ROSE_GOLD, 1, -2.8f, getBasicSettings()));
	public static final AxeItem ROSE_GOLD_AXE = register("rose_gold_axe", new AxeItem(AstromineToolMaterials.ROSE_GOLD, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem ROSE_GOLD_SHOVEL = register("rose_gold_shovel", new ShovelItem(AstromineToolMaterials.ROSE_GOLD, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem ROSE_GOLD_HOE = register("rose_gold_hoe", new HoeItem(AstromineToolMaterials.ROSE_GOLD, -1, 0f, getBasicSettings()));
	public static final Item ROSE_GOLD_SWORD = register("rose_gold_sword", new SwordItem(AstromineToolMaterials.ROSE_GOLD, 3, -2.4f, getBasicSettings()));
	public static final Item ROSE_GOLD_MINING_TOOL = register("rose_gold_mining_tool", new MultitoolItem(ROSE_GOLD_SHOVEL, ROSE_GOLD_PICKAXE, AstromineToolMaterials.ROSE_GOLD, getBasicSettings()));
	public static final Item ROSE_GOLD_MATTOCK = register("rose_gold_mattock", new MultitoolItem(ROSE_GOLD_HOE, ROSE_GOLD_AXE, AstromineToolMaterials.ROSE_GOLD, getBasicSettings()));
	public static final Item ROSE_GOLD_HAMMER = register("rose_gold_hammer", new HammerItem(AstromineToolMaterials.ROSE_GOLD, 1, -2.8f, getBasicSettings()));
	public static final Item ROSE_GOLD_EXCAVATOR = register("rose_gold_excavator", new ExcavatorItem(AstromineToolMaterials.ROSE_GOLD, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem STERLING_SILVER_PICKAXE = register("sterling_silver_pickaxe", new PickaxeItem(AstromineToolMaterials.STERLING_SILVER, 1, -2.8f, getBasicSettings()));
	public static final AxeItem STERLING_SILVER_AXE = register("sterling_silver_axe", new AxeItem(AstromineToolMaterials.STERLING_SILVER, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem STERLING_SILVER_SHOVEL = register("sterling_silver_shovel", new ShovelItem(AstromineToolMaterials.STERLING_SILVER, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem STERLING_SILVER_HOE = register("sterling_silver_hoe", new HoeItem(AstromineToolMaterials.STERLING_SILVER, -2, 0f, getBasicSettings()));
	public static final Item STERLING_SILVER_SWORD = register("sterling_silver_sword", new SwordItem(AstromineToolMaterials.STERLING_SILVER, 3, -2.4f, getBasicSettings()));
	public static final Item STERLING_SILVER_MINING_TOOL = register("sterling_silver_mining_tool", new MultitoolItem(STERLING_SILVER_SHOVEL, STERLING_SILVER_PICKAXE, AstromineToolMaterials.STERLING_SILVER, getBasicSettings()));
	public static final Item STERLING_SILVER_MATTOCK = register("sterling_silver_mattock", new MultitoolItem(STERLING_SILVER_HOE, STERLING_SILVER_AXE, AstromineToolMaterials.STERLING_SILVER, getBasicSettings()));
	public static final Item STERLING_SILVER_HAMMER = register("sterling_silver_hammer", new HammerItem(AstromineToolMaterials.STERLING_SILVER, 1, -2.8f, getBasicSettings()));
	public static final Item STERLING_SILVER_EXCAVATOR = register("sterling_silver_excavator", new ExcavatorItem(AstromineToolMaterials.STERLING_SILVER, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem FOOLS_GOLD_PICKAXE = register("fools_gold_pickaxe", new PickaxeItem(AstromineToolMaterials.FOOLS_GOLD, 1, -2.8f, getBasicSettings()));
	public static final AxeItem FOOLS_GOLD_AXE = register("fools_gold_axe", new AxeItem(AstromineToolMaterials.FOOLS_GOLD, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem FOOLS_GOLD_SHOVEL = register("fools_gold_shovel", new ShovelItem(AstromineToolMaterials.FOOLS_GOLD, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem FOOLS_GOLD_HOE = register("fools_gold_hoe", new HoeItem(AstromineToolMaterials.FOOLS_GOLD, -3, 0f, getBasicSettings()));
	public static final Item FOOLS_GOLD_SWORD = register("fools_gold_sword", new SwordItem(AstromineToolMaterials.FOOLS_GOLD, 3, -2.4f, getBasicSettings()));
	public static final Item FOOLS_GOLD_MINING_TOOL = register("fools_gold_mining_tool", new MultitoolItem(FOOLS_GOLD_SHOVEL, FOOLS_GOLD_PICKAXE, AstromineToolMaterials.FOOLS_GOLD, getBasicSettings()));
	public static final Item FOOLS_GOLD_MATTOCK = register("fools_gold_mattock", new MultitoolItem(FOOLS_GOLD_HOE, FOOLS_GOLD_AXE, AstromineToolMaterials.FOOLS_GOLD, getBasicSettings()));
	public static final Item FOOLS_GOLD_HAMMER = register("fools_gold_hammer", new HammerItem(AstromineToolMaterials.FOOLS_GOLD, 1, -2.8f, getBasicSettings()));
	public static final Item FOOLS_GOLD_EXCAVATOR = register("fools_gold_excavator", new ExcavatorItem(AstromineToolMaterials.FOOLS_GOLD, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem METITE_PICKAXE = register("metite_pickaxe", new PickaxeItem(AstromineToolMaterials.METITE, 1, -2.8f, getBasicSettings()));
	public static final AxeItem METITE_AXE = register("metite_axe", new AxeItem(AstromineToolMaterials.METITE, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem METITE_SHOVEL = register("metite_shovel", new ShovelItem(AstromineToolMaterials.METITE, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem METITE_HOE = register("metite_hoe", new HoeItem(AstromineToolMaterials.METITE, -5, 0f, getBasicSettings()));
	public static final Item METITE_SWORD = register("metite_sword", new SwordItem(AstromineToolMaterials.METITE, 3, -2.4f, getBasicSettings()));
	public static final Item METITE_MINING_TOOL = register("metite_mining_tool", new MultitoolItem(METITE_SHOVEL, METITE_PICKAXE, AstromineToolMaterials.METITE, getBasicSettings()));
	public static final Item METITE_MATTOCK = register("metite_mattock", new MultitoolItem(METITE_HOE, METITE_AXE, AstromineToolMaterials.METITE, getBasicSettings()));
	public static final Item METITE_HAMMER = register("metite_hammer", new HammerItem(AstromineToolMaterials.METITE, 1, -2.8f, getBasicSettings()));
	public static final Item METITE_EXCAVATOR = register("metite_excavator", new ExcavatorItem(AstromineToolMaterials.METITE, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem ASTERITE_PICKAXE = register("asterite_pickaxe", new PickaxeItem(AstromineToolMaterials.ASTERITE, 1, -2.8f, getBasicSettings()));
	public static final AxeItem ASTERITE_AXE = register("asterite_axe", new AxeItem(AstromineToolMaterials.ASTERITE, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem ASTERITE_SHOVEL = register("asterite_shovel", new ShovelItem(AstromineToolMaterials.ASTERITE, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem ASTERITE_HOE = register("asterite_hoe", new HoeItem(AstromineToolMaterials.ASTERITE, -5, 0f, getBasicSettings()));
	public static final Item ASTERITE_SWORD = register("asterite_sword", new SwordItem(AstromineToolMaterials.ASTERITE, 3, -2.4f, getBasicSettings()));
	public static final Item ASTERITE_MINING_TOOL = register("asterite_mining_tool", new MultitoolItem(ASTERITE_SHOVEL, ASTERITE_PICKAXE, AstromineToolMaterials.ASTERITE, getBasicSettings()));
	public static final Item ASTERITE_MATTOCK = register("asterite_mattock", new MultitoolItem(ASTERITE_HOE, ASTERITE_AXE, AstromineToolMaterials.ASTERITE, getBasicSettings()));
	public static final Item ASTERITE_HAMMER = register("asterite_hammer", new HammerItem(AstromineToolMaterials.ASTERITE, 1, -2.8f, getBasicSettings()));
	public static final Item ASTERITE_EXCAVATOR = register("asterite_excavator", new ExcavatorItem(AstromineToolMaterials.ASTERITE, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem STELLUM_PICKAXE = register("stellum_pickaxe", new PickaxeItem(AstromineToolMaterials.STELLUM, 1, -2.8f, getBasicSettings().fireproof()));
	public static final AxeItem STELLUM_AXE = register("stellum_axe", new AxeItem(AstromineToolMaterials.STELLUM, 5f, -3.0f, getBasicSettings().fireproof()));
	public static final ShovelItem STELLUM_SHOVEL = register("stellum_shovel", new ShovelItem(AstromineToolMaterials.STELLUM, 1.5f, -3.0f, getBasicSettings().fireproof()));
	public static final HoeItem STELLUM_HOE = register("stellum_hoe", new HoeItem(AstromineToolMaterials.STELLUM, -6, 0f, getBasicSettings().fireproof()));
	public static final Item STELLUM_SWORD = register("stellum_sword", new SwordItem(AstromineToolMaterials.STELLUM, 3, -2.4f, getBasicSettings().fireproof()));
	public static final Item STELLUM_MINING_TOOL = register("stellum_mining_tool", new MultitoolItem(STELLUM_SHOVEL, STELLUM_PICKAXE, AstromineToolMaterials.STELLUM, getBasicSettings().fireproof()));
	public static final Item STELLUM_MATTOCK = register("stellum_mattock", new MultitoolItem(STELLUM_HOE, STELLUM_AXE, AstromineToolMaterials.STELLUM, getBasicSettings().fireproof()));
	public static final Item STELLUM_HAMMER = register("stellum_hammer", new HammerItem(AstromineToolMaterials.STELLUM, 1, -2.8f, getBasicSettings()));
	public static final Item STELLUM_EXCAVATOR = register("stellum_excavator", new ExcavatorItem(AstromineToolMaterials.STELLUM, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem GALAXIUM_PICKAXE = register("galaxium_pickaxe", new PickaxeItem(AstromineToolMaterials.GALAXIUM, 1, -2.8f, getBasicSettings()));
	public static final AxeItem GALAXIUM_AXE = register("galaxium_axe", new AxeItem(AstromineToolMaterials.GALAXIUM, 5f, -3.0f, getBasicSettings()));
	public static final ShovelItem GALAXIUM_SHOVEL = register("galaxium_shovel", new ShovelItem(AstromineToolMaterials.GALAXIUM, 1.5f, -3.0f, getBasicSettings()));
	public static final HoeItem GALAXIUM_HOE = register("galaxium_hoe", new HoeItem(AstromineToolMaterials.GALAXIUM, -5, 0f, getBasicSettings()));
	public static final Item GALAXIUM_SWORD = register("galaxium_sword", new SwordItem(AstromineToolMaterials.GALAXIUM, 3, -2.4f, getBasicSettings()));
	public static final Item GALAXIUM_MINING_TOOL = register("galaxium_mining_tool", new MultitoolItem(GALAXIUM_SHOVEL, GALAXIUM_PICKAXE, AstromineToolMaterials.GALAXIUM, getBasicSettings()));
	public static final Item GALAXIUM_MATTOCK = register("galaxium_mattock", new MultitoolItem(GALAXIUM_HOE, GALAXIUM_AXE, AstromineToolMaterials.GALAXIUM, getBasicSettings()));
	public static final Item GALAXIUM_HAMMER = register("galaxium_hammer", new HammerItem(AstromineToolMaterials.GALAXIUM, 1, -2.8f, getBasicSettings()));
	public static final Item GALAXIUM_EXCAVATOR = register("galaxium_excavator", new ExcavatorItem(AstromineToolMaterials.GALAXIUM, 1, -2.8f, getBasicSettings()));

	public static final PickaxeItem UNIVITE_PICKAXE = register("univite_pickaxe", new PickaxeItem(AstromineToolMaterials.UNIVITE, 1, -2.8f, getBasicSettings().fireproof()));
	public static final AxeItem UNIVITE_AXE = register("univite_axe", new AxeItem(AstromineToolMaterials.UNIVITE, 5f, -3.0f, getBasicSettings().fireproof()));
	public static final ShovelItem UNIVITE_SHOVEL = register("univite_shovel", new ShovelItem(AstromineToolMaterials.UNIVITE, 1.5f, -3.0f, getBasicSettings().fireproof()));
	public static final HoeItem UNIVITE_HOE = register("univite_hoe", new HoeItem(AstromineToolMaterials.UNIVITE, -6, 0f, getBasicSettings().fireproof()));
	public static final Item UNIVITE_SWORD = register("univite_sword", new SwordItem(AstromineToolMaterials.UNIVITE, 3, -2.4f, getBasicSettings().fireproof()));
	public static final Item UNIVITE_MINING_TOOL = register("univite_mining_tool", new MultitoolItem(UNIVITE_SHOVEL, UNIVITE_PICKAXE, AstromineToolMaterials.UNIVITE, getBasicSettings().fireproof()));
	public static final Item UNIVITE_MATTOCK = register("univite_mattock", new MultitoolItem(UNIVITE_HOE, UNIVITE_AXE, AstromineToolMaterials.UNIVITE, getBasicSettings().fireproof()));
	public static final Item UNIVITE_HAMMER = register("univite_hammer", new HammerItem(AstromineToolMaterials.UNIVITE, 1, -2.8f, getBasicSettings()));
	public static final Item UNIVITE_EXCAVATOR = register("univite_excavator", new ExcavatorItem(AstromineToolMaterials.UNIVITE, 1, -2.8f, getBasicSettings()));

	// Armor
	public static final Item COPPER_HELMET = register("copper_helmet", new ArmorItem(AstromineArmorMaterials.COPPER, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item COPPER_CHESTPLATE = register("copper_chestplate", new ArmorItem(AstromineArmorMaterials.COPPER, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item COPPER_LEGGINGS = register("copper_leggings", new ArmorItem(AstromineArmorMaterials.COPPER, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item COPPER_BOOTS = register("copper_boots", new ArmorItem(AstromineArmorMaterials.COPPER, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item TIN_HELMET = register("tin_helmet", new ArmorItem(AstromineArmorMaterials.TIN, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item TIN_CHESTPLATE = register("tin_chestplate", new ArmorItem(AstromineArmorMaterials.TIN, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item TIN_LEGGINGS = register("tin_leggings", new ArmorItem(AstromineArmorMaterials.TIN, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item TIN_BOOTS = register("tin_boots", new ArmorItem(AstromineArmorMaterials.TIN, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item SILVER_HELMET = register("silver_helmet", new ArmorItem(AstromineArmorMaterials.SILVER, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item SILVER_CHESTPLATE = register("silver_chestplate", new ArmorItem(AstromineArmorMaterials.SILVER, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item SILVER_LEGGINGS = register("silver_leggings", new ArmorItem(AstromineArmorMaterials.SILVER, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item SILVER_BOOTS = register("silver_boots", new ArmorItem(AstromineArmorMaterials.SILVER, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item LEAD_HELMET = register("lead_helmet", new ArmorItem(AstromineArmorMaterials.LEAD, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item LEAD_CHESTPLATE = register("lead_chestplate", new ArmorItem(AstromineArmorMaterials.LEAD, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item LEAD_LEGGINGS = register("lead_leggings", new ArmorItem(AstromineArmorMaterials.LEAD, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item LEAD_BOOTS = register("lead_boots", new ArmorItem(AstromineArmorMaterials.LEAD, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item BRONZE_HELMET = register("bronze_helmet", new ArmorItem(AstromineArmorMaterials.BRONZE, EquipmentSlot.HEAD, getBasicSettings().fireproof()));
	public static final Item BRONZE_CHESTPLATE = register("bronze_chestplate", new ArmorItem(AstromineArmorMaterials.BRONZE, EquipmentSlot.CHEST, getBasicSettings().fireproof()));
	public static final Item BRONZE_LEGGINGS = register("bronze_leggings", new ArmorItem(AstromineArmorMaterials.BRONZE, EquipmentSlot.LEGS, getBasicSettings().fireproof()));
	public static final Item BRONZE_BOOTS = register("bronze_boots", new ArmorItem(AstromineArmorMaterials.BRONZE, EquipmentSlot.FEET, getBasicSettings().fireproof()));

	public static final Item STEEL_HELMET = register("steel_helmet", new ArmorItem(AstromineArmorMaterials.STEEL, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item STEEL_CHESTPLATE = register("steel_chestplate", new ArmorItem(AstromineArmorMaterials.STEEL, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item STEEL_LEGGINGS = register("steel_leggings", new ArmorItem(AstromineArmorMaterials.STEEL, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item STEEL_BOOTS = register("steel_boots", new ArmorItem(AstromineArmorMaterials.STEEL, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item ELECTRUM_HELMET = register("electrum_helmet", new ArmorItem(AstromineArmorMaterials.ELECTRUM, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item ELECTRUM_CHESTPLATE = register("electrum_chestplate", new ArmorItem(AstromineArmorMaterials.ELECTRUM, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item ELECTRUM_LEGGINGS = register("electrum_leggings", new ArmorItem(AstromineArmorMaterials.ELECTRUM, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item ELECTRUM_BOOTS = register("electrum_boots", new ArmorItem(AstromineArmorMaterials.ELECTRUM, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item ROSE_GOLD_HELMET = register("rose_gold_helmet", new ArmorItem(AstromineArmorMaterials.ROSE_GOLD, EquipmentSlot.HEAD, getBasicSettings().fireproof()));
	public static final Item ROSE_GOLD_CHESTPLATE = register("rose_gold_chestplate", new ArmorItem(AstromineArmorMaterials.ROSE_GOLD, EquipmentSlot.CHEST, getBasicSettings().fireproof()));
	public static final Item ROSE_GOLD_LEGGINGS = register("rose_gold_leggings", new ArmorItem(AstromineArmorMaterials.ROSE_GOLD, EquipmentSlot.LEGS, getBasicSettings().fireproof()));
	public static final Item ROSE_GOLD_BOOTS = register("rose_gold_boots", new ArmorItem(AstromineArmorMaterials.ROSE_GOLD, EquipmentSlot.FEET, getBasicSettings().fireproof()));

	public static final Item STERLING_SILVER_HELMET = register("sterling_silver_helmet", new ArmorItem(AstromineArmorMaterials.STERLING_SILVER, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item STERLING_SILVER_CHESTPLATE = register("sterling_silver_chestplate", new ArmorItem(AstromineArmorMaterials.STERLING_SILVER, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item STERLING_SILVER_LEGGINGS = register("sterling_silver_leggings", new ArmorItem(AstromineArmorMaterials.STERLING_SILVER, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item STERLING_SILVER_BOOTS = register("sterling_silver_boots", new ArmorItem(AstromineArmorMaterials.STERLING_SILVER, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item FOOLS_GOLD_HELMET = register("fools_gold_helmet", new ArmorItem(AstromineArmorMaterials.FOOLS_GOLD, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item FOOLS_GOLD_CHESTPLATE = register("fools_gold_chestplate", new ArmorItem(AstromineArmorMaterials.FOOLS_GOLD, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item FOOLS_GOLD_LEGGINGS = register("fools_gold_leggings", new ArmorItem(AstromineArmorMaterials.FOOLS_GOLD, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item FOOLS_GOLD_BOOTS = register("fools_gold_boots", new ArmorItem(AstromineArmorMaterials.FOOLS_GOLD, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item METITE_HELMET = register("metite_helmet", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item METITE_CHESTPLATE = register("metite_chestplate", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item METITE_LEGGINGS = register("metite_leggings", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item METITE_BOOTS = register("metite_boots", new ArmorItem(AstromineArmorMaterials.METITE, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item ASTERITE_HELMET = register("asterite_helmet", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item ASTERITE_CHESTPLATE = register("asterite_chestplate", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item ASTERITE_LEGGINGS = register("asterite_leggings", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item ASTERITE_BOOTS = register("asterite_boots", new ArmorItem(AstromineArmorMaterials.ASTERITE, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item STELLUM_HELMET = register("stellum_helmet", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.HEAD, getBasicSettings().fireproof()));
	public static final Item STELLUM_CHESTPLATE = register("stellum_chestplate", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.CHEST, getBasicSettings().fireproof()));
	public static final Item STELLUM_LEGGINGS = register("stellum_leggings", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.LEGS, getBasicSettings().fireproof()));
	public static final Item STELLUM_BOOTS = register("stellum_boots", new ArmorItem(AstromineArmorMaterials.STELLUM, EquipmentSlot.FEET, getBasicSettings().fireproof()));

	public static final Item GALAXIUM_HELMET = register("galaxium_helmet", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item GALAXIUM_CHESTPLATE = register("galaxium_chestplate", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item GALAXIUM_LEGGINGS = register("galaxium_leggings", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item GALAXIUM_BOOTS = register("galaxium_boots", new ArmorItem(AstromineArmorMaterials.GALAXIUM, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item UNIVITE_HELMET = register("univite_helmet", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.HEAD, getBasicSettings().fireproof()));
	public static final Item UNIVITE_CHESTPLATE = register("univite_chestplate", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.CHEST, getBasicSettings().fireproof()));
	public static final Item UNIVITE_LEGGINGS = register("univite_leggings", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.LEGS, getBasicSettings().fireproof()));
	public static final Item UNIVITE_BOOTS = register("univite_boots", new ArmorItem(AstromineArmorMaterials.UNIVITE, EquipmentSlot.FEET, getBasicSettings().fireproof()));

	// Space Suit
	public static final Item SPACE_SUIT_HELMET = register("space_suit_helmet", new SpaceSuitItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item SPACE_SUIT_CHESTPLATE = register("space_suit_chestplate", new SpaceSuitItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item SPACE_SUIT_LEGGINGS = register("space_suit_leggings", new SpaceSuitItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item SPACE_SUIT_BOOTS = register("space_suit_boots", new SpaceSuitItem(AstromineArmorMaterials.SPACE_SUIT, EquipmentSlot.FEET, getBasicSettings()));

	public static void initialize() {
		for (UncoloredSpawnEggItem spawnEggItem : UncoloredSpawnEggItem.getAll()) {
			DispenserBlock.registerBehavior(spawnEggItem, new ItemDispenserBehavior() {
				@Override
				public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
					Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
					EntityType<?> entityType = ((UncoloredSpawnEggItem) stack.getItem()).getEntityType(stack.getTag());
					entityType.spawnFromItemStack(pointer.getWorld(), stack, null, pointer.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
					stack.decrement(1);
					return stack;
				}
			});
		}

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Registry.register(Registry.ITEM, AstromineCommon.identifier("meteor_spawner"), new MeteorSpawnerDevItem(new Item.Settings()));
		}
	}

	/**
	 * @param name
	 *        Name of item instance to be registered
	 * @param item
	 *        Item instance to be registered
	 * @return Item instanced registered
	 */
	public static <T extends Item> T register(String name, T item) {
		return register(AstromineCommon.identifier(name), item);
	}

	/**
	 * @param name
	 *        Identifier of item instance to be registered
	 * @param item
	 *        Item instance to be registered
	 * @return Item instance registered
	 */
	public static <T extends Item> T register(Identifier name, T item) {
		return Registry.register(Registry.ITEM, name, item);
	}

	public static Item.Settings getBasicSettings() {
		return new Item.Settings().group(AstromineItemGroups.ASTROMINE);
	}
}
