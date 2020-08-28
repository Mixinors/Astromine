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

import com.github.chainmailstudios.astromine.common.item.*;
import com.github.chainmailstudios.astromine.common.utilities.ToolUtilities;
import com.github.chainmailstudios.astromine.common.utilities.data.position.WorldPos;
import com.github.chainmailstudios.astromine.foundations.common.item.FireExtinguisherItem;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import draylar.magna.item.ExcavatorItem;
import draylar.magna.item.HammerItem;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;

public class AstromineFoundationsItems extends AstromineItems {
	// Materials - Nuggets & Fragments
	public static final Item METITE_NUGGET = register("metite_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STELLUM_NUGGET = register("stellum_nugget", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_NUGGET = register("univite_nugget", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item COPPER_NUGGET = register("copper_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_NUGGET = register("tin_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_NUGGET = register("silver_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_NUGGET = register("lead_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item STEEL_NUGGET = register("steel_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_NUGGET = register("bronze_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_NUGGET = register("electrum_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_NUGGET = register("rose_gold_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_NUGGET = register("sterling_silver_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_NUGGET = register("fools_gold_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item NETHERITE_NUGGET = register("netherite_nugget", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item ASTERITE_FRAGMENT = register("asterite_fragment", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM_FRAGMENT = register("galaxium_fragment", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item DIAMOND_FRAGMENT = register("diamond_fragment", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item EMERALD_FRAGMENT = register("emerald_fragment", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item QUARTZ_FRAGMENT = register("quartz_fragment", new Item(AstromineFoundationsItems.getBasicSettings()));

	// Materials - Wires
	public static final Item COPPER_WIRE = register("copper_wire", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_WIRE = register("tin_wire", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_WIRE = register("silver_wire", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_WIRE = register("lead_wire", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item STEEL_WIRE = register("steel_wire", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_WIRE = register("electrum_wire", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item GOLD_WIRE = register("gold_wire", new Item(AstromineFoundationsItems.getBasicSettings()));

	// Materials - Gems
	public static final Item ASTERITE = register("asterite", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM = register("galaxium", new Item(AstromineFoundationsItems.getBasicSettings()));

	// Materials - Ingots
	public static final Item METITE_INGOT = register("metite_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STELLUM_INGOT = register("stellum_ingot", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_INGOT = register("univite_ingot", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item COPPER_INGOT = register("copper_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_INGOT = register("tin_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_INGOT = register("silver_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_INGOT = register("lead_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item BRONZE_INGOT = register("bronze_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_INGOT = register("steel_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_INGOT = register("electrum_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_INGOT = register("rose_gold_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_INGOT = register("sterling_silver_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_INGOT = register("fools_gold_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));

	// Materials - Dusts
	public static final Item METITE_DUST = register("metite_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_DUST = register("asterite_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STELLUM_DUST = register("stellum_dust", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item GALAXIUM_DUST = register("galaxium_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item UNIVITE_DUST = register("univite_dust", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item COPPER_DUST = register("copper_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_DUST = register("tin_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_DUST = register("silver_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_DUST = register("lead_dust", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item STEEL_DUST = register("steel_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_DUST = register("bronze_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_DUST = register("electrum_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_DUST = register("rose_gold_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_DUST = register("sterling_silver_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_DUST = register("fools_gold_dust", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item IRON_DUST = register("iron_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item GOLD_DUST = register("gold_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LAPIS_DUST = register("lapis_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item DIAMOND_DUST = register("diamond_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item EMERALD_DUST = register("emerald_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item NETHERITE_DUST = register("netherite_dust", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item COAL_DUST = register("coal_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item CHARCOAL_DUST = register("charcoal_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item QUARTZ_DUST = register("quartz_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item RAW_NETHERITE_DUST = register("raw_netherite_dust", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	// Materials - Tiny Dusts
	public static final Item METITE_TINY_DUST = register("metite_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_TINY_DUST = register("asterite_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STELLUM_TINY_DUST = register("stellum_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item GALAXIUM_TINY_DUST = register("galaxium_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item UNIVITE_TINY_DUST = register("univite_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item COPPER_TINY_DUST = register("copper_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_TINY_DUST = register("tin_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_TINY_DUST = register("silver_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_TINY_DUST = register("lead_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item STEEL_TINY_DUST = register("steel_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_TINY_DUST = register("bronze_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_TINY_DUST = register("electrum_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_TINY_DUST = register("rose_gold_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_TINY_DUST = register("sterling_silver_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_TINY_DUST = register("fools_gold_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item REDSTONE_TINY_DUST = register("redstone_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item IRON_TINY_DUST = register("iron_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item GOLD_TINY_DUST = register("gold_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LAPIS_TINY_DUST = register("lapis_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item DIAMOND_TINY_DUST = register("diamond_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item EMERALD_TINY_DUST = register("emerald_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item NETHERITE_TINY_DUST = register("netherite_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item COAL_TINY_DUST = register("coal_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item CHARCOAL_TINY_DUST = register("charcoal_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item QUARTZ_TINY_DUST = register("quartz_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item RAW_NETHERITE_TINY_DUST = register("raw_netherite_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	// Materials - Plates
	public static final Item METITE_PLATES = register("metite_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STELLUM_PLATES = register("stellum_plates", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_PLATES = register("univite_plates", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item COPPER_PLATES = register("copper_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_PLATES = register("tin_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_PLATES = register("silver_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_PLATES = register("lead_plates", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item STEEL_PLATES = register("steel_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_PLATES = register("bronze_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_PLATES = register("electrum_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_PLATES = register("rose_gold_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_PLATES = register("sterling_silver_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_PLATES = register("fools_gold_plates", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item IRON_PLATES = register("iron_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item GOLD_PLATES = register("gold_plates", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item NETHERITE_PLATES = register("netherite_plates", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	// Materials - Gears
	public static final Item METITE_GEAR = register("metite_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STELLUM_GEAR = register("stellum_gear", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_GEAR = register("univite_gear", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item COPPER_GEAR = register("copper_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_GEAR = register("tin_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_GEAR = register("silver_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_GEAR = register("lead_gear", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item STEEL_GEAR = register("steel_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_GEAR = register("bronze_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_GEAR = register("electrum_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_GEAR = register("rose_gold_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_GEAR = register("sterling_silver_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_GEAR = register("fools_gold_gear", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item IRON_GEAR = register("iron_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item GOLD_GEAR = register("gold_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item NETHERITE_GEAR = register("netherite_gear", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	// Tools
	public static final Item WOODEN_MINING_TOOL = register("wooden_mining_tool", new DynamicToolItem((MiningToolItem) Items.WOODEN_SHOVEL, (MiningToolItem) Items.WOODEN_PICKAXE, ToolMaterials.WOOD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item WOODEN_MATTOCK = register("wooden_mattock", new DynamicToolItem((MiningToolItem) Items.WOODEN_HOE, (MiningToolItem) Items.WOODEN_AXE, ToolMaterials.WOOD, AstromineFoundationsItems.getBasicSettings()));

	public static final Item STONE_MINING_TOOL = register("stone_mining_tool", new DynamicToolItem((MiningToolItem) Items.STONE_SHOVEL, (MiningToolItem) Items.STONE_PICKAXE, ToolMaterials.STONE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STONE_MATTOCK = register("stone_mattock", new DynamicToolItem((MiningToolItem) Items.STONE_HOE, (MiningToolItem) Items.STONE_AXE, ToolMaterials.STONE, AstromineFoundationsItems.getBasicSettings()));

	public static final Item IRON_MINING_TOOL = register("iron_mining_tool", new DynamicToolItem((MiningToolItem) Items.IRON_SHOVEL, (MiningToolItem) Items.IRON_PICKAXE, ToolMaterials.IRON, AstromineFoundationsItems.getBasicSettings()));
	public static final Item IRON_MATTOCK = register("iron_mattock", new DynamicToolItem((MiningToolItem) Items.IRON_HOE, (MiningToolItem) Items.IRON_AXE, ToolMaterials.IRON, AstromineFoundationsItems.getBasicSettings()));

	public static final Item GOLDEN_MINING_TOOL = register("golden_mining_tool", new DynamicToolItem((MiningToolItem) Items.GOLDEN_SHOVEL, (MiningToolItem) Items.GOLDEN_PICKAXE, ToolMaterials.GOLD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GOLDEN_MATTOCK = register("golden_mattock", new DynamicToolItem((MiningToolItem) Items.GOLDEN_HOE, (MiningToolItem) Items.GOLDEN_AXE, ToolMaterials.GOLD, AstromineFoundationsItems.getBasicSettings()));

	public static final Item DIAMOND_MINING_TOOL = register("diamond_mining_tool", new DynamicToolItem((MiningToolItem) Items.DIAMOND_SHOVEL, (MiningToolItem) Items.DIAMOND_PICKAXE, ToolMaterials.DIAMOND, AstromineFoundationsItems.getBasicSettings()));
	public static final Item DIAMOND_MATTOCK = register("diamond_mattock", new DynamicToolItem((MiningToolItem) Items.DIAMOND_HOE, (MiningToolItem) Items.DIAMOND_AXE, ToolMaterials.DIAMOND, AstromineFoundationsItems.getBasicSettings()));

	public static final Item NETHERITE_MINING_TOOL = register("netherite_mining_tool", new DynamicToolItem((MiningToolItem) Items.NETHERITE_SHOVEL, (MiningToolItem) Items.NETHERITE_PICKAXE, ToolMaterials.NETHERITE, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item NETHERITE_MATTOCK = register("netherite_mattock", new DynamicToolItem((MiningToolItem) Items.NETHERITE_HOE, (MiningToolItem) Items.NETHERITE_AXE, ToolMaterials.NETHERITE, AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final PickaxeItem COPPER_PICKAXE = register("copper_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.COPPER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem COPPER_AXE = register("copper_axe", new AxeItem(AstromineFoundationsToolMaterials.COPPER, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem COPPER_SHOVEL = register("copper_shovel", new ShovelItem(AstromineFoundationsToolMaterials.COPPER, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem COPPER_HOE = register("copper_hoe", new HoeItem(AstromineFoundationsToolMaterials.COPPER, -1, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_SWORD = register("copper_sword", new SwordItem(AstromineFoundationsToolMaterials.COPPER, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_MINING_TOOL = register("copper_mining_tool", new DynamicToolItem(COPPER_PICKAXE, COPPER_SHOVEL, AstromineFoundationsToolMaterials.COPPER, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_MATTOCK = register("copper_mattock", new DynamicToolItem(COPPER_HOE, COPPER_AXE, AstromineFoundationsToolMaterials.COPPER, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_HAMMER = register("copper_hammer", new HammerItem(AstromineFoundationsToolMaterials.COPPER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_EXCAVATOR = register("copper_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.COPPER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem TIN_PICKAXE = register("tin_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.TIN, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem TIN_AXE = register("tin_axe", new AxeItem(AstromineFoundationsToolMaterials.TIN, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem TIN_SHOVEL = register("tin_shovel", new ShovelItem(AstromineFoundationsToolMaterials.TIN, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem TIN_HOE = register("tin_hoe", new HoeItem(AstromineFoundationsToolMaterials.TIN, -1, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_SWORD = register("tin_sword", new SwordItem(AstromineFoundationsToolMaterials.TIN, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_MINING_TOOL = register("tin_mining_tool", new DynamicToolItem(TIN_SHOVEL, TIN_PICKAXE, AstromineFoundationsToolMaterials.TIN, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_MATTOCK = register("tin_mattock", new DynamicToolItem(TIN_HOE, TIN_AXE, AstromineFoundationsToolMaterials.TIN, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_HAMMER = register("tin_hammer", new HammerItem(AstromineFoundationsToolMaterials.TIN, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_EXCAVATOR = register("tin_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.TIN, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem SILVER_PICKAXE = register("silver_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.SILVER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem SILVER_AXE = register("silver_axe", new AxeItem(AstromineFoundationsToolMaterials.SILVER, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem SILVER_SHOVEL = register("silver_shovel", new ShovelItem(AstromineFoundationsToolMaterials.SILVER, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem SILVER_HOE = register("silver_hoe", new HoeItem(AstromineFoundationsToolMaterials.SILVER, -2, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_SWORD = register("silver_sword", new SwordItem(AstromineFoundationsToolMaterials.SILVER, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_MINING_TOOL = register("silver_mining_tool", new DynamicToolItem(SILVER_PICKAXE, SILVER_SHOVEL, AstromineFoundationsToolMaterials.SILVER, AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_MATTOCK = register("silver_mattock", new DynamicToolItem(SILVER_HOE, SILVER_AXE, AstromineFoundationsToolMaterials.SILVER, AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_HAMMER = register("silver_hammer", new HammerItem(AstromineFoundationsToolMaterials.SILVER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_EXCAVATOR = register("silver_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.SILVER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem LEAD_PICKAXE = register("lead_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.LEAD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem LEAD_AXE = register("lead_axe", new AxeItem(AstromineFoundationsToolMaterials.LEAD, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem LEAD_SHOVEL = register("lead_shovel", new ShovelItem(AstromineFoundationsToolMaterials.LEAD, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem LEAD_HOE = register("lead_hoe", new HoeItem(AstromineFoundationsToolMaterials.LEAD, -2, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_SWORD = register("lead_sword", new SwordItem(AstromineFoundationsToolMaterials.LEAD, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_MINING_TOOL = register("lead_mining_tool", new DynamicToolItem(LEAD_PICKAXE, LEAD_SHOVEL, AstromineFoundationsToolMaterials.LEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_MATTOCK = register("lead_mattock", new DynamicToolItem(LEAD_HOE, LEAD_AXE, AstromineFoundationsToolMaterials.LEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_HAMMER = register("lead_hammer", new HammerItem(AstromineFoundationsToolMaterials.LEAD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_EXCAVATOR = register("lead_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.LEAD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem BRONZE_PICKAXE = register("bronze_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.BRONZE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem BRONZE_AXE = register("bronze_axe", new AxeItem(AstromineFoundationsToolMaterials.BRONZE, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem BRONZE_SHOVEL = register("bronze_shovel", new ShovelItem(AstromineFoundationsToolMaterials.BRONZE, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem BRONZE_HOE = register("bronze_hoe", new HoeItem(AstromineFoundationsToolMaterials.BRONZE, -2, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_SWORD = register("bronze_sword", new SwordItem(AstromineFoundationsToolMaterials.BRONZE, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_MINING_TOOL = register("bronze_mining_tool", new DynamicToolItem(BRONZE_SHOVEL, BRONZE_PICKAXE, AstromineFoundationsToolMaterials.BRONZE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_MATTOCK = register("bronze_mattock", new DynamicToolItem(BRONZE_HOE, BRONZE_AXE, AstromineFoundationsToolMaterials.BRONZE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_HAMMER = register("bronze_hammer", new HammerItem(AstromineFoundationsToolMaterials.BRONZE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_EXCAVATOR = register("bronze_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.BRONZE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem STEEL_PICKAXE = register("steel_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.STEEL, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem STEEL_AXE = register("steel_axe", new AxeItem(AstromineFoundationsToolMaterials.STEEL, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem STEEL_SHOVEL = register("steel_shovel", new ShovelItem(AstromineFoundationsToolMaterials.STEEL, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem STEEL_HOE = register("steel_hoe", new HoeItem(AstromineFoundationsToolMaterials.STEEL, -3, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_SWORD = register("steel_sword", new SwordItem(AstromineFoundationsToolMaterials.STEEL, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_MINING_TOOL = register("steel_mining_tool", new DynamicToolItem(STEEL_SHOVEL, STEEL_PICKAXE, AstromineFoundationsToolMaterials.STEEL, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_MATTOCK = register("steel_mattock", new DynamicToolItem(STEEL_HOE, STEEL_AXE, AstromineFoundationsToolMaterials.STEEL, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_HAMMER = register("steel_hammer", new HammerItem(AstromineFoundationsToolMaterials.STEEL, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_EXCAVATOR = register("steel_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.STELLUM, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem ELECTRUM_PICKAXE = register("electrum_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.ELECTRUM, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem ELECTRUM_AXE = register("electrum_axe", new AxeItem(AstromineFoundationsToolMaterials.ELECTRUM, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem ELECTRUM_SHOVEL = register("electrum_shovel", new ShovelItem(AstromineFoundationsToolMaterials.ELECTRUM, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem ELECTRUM_HOE = register("electrum_hoe", new HoeItem(AstromineFoundationsToolMaterials.ELECTRUM, -1, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_SWORD = register("electrum_sword", new SwordItem(AstromineFoundationsToolMaterials.ELECTRUM, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_MINING_TOOL = register("electrum_mining_tool", new DynamicToolItem(ELECTRUM_SHOVEL, ELECTRUM_PICKAXE, AstromineFoundationsToolMaterials.ELECTRUM, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_MATTOCK = register("electrum_mattock", new DynamicToolItem(ELECTRUM_HOE, ELECTRUM_AXE, AstromineFoundationsToolMaterials.ELECTRUM, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_HAMMER = register("electrum_hammer", new HammerItem(AstromineFoundationsToolMaterials.ELECTRUM, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_EXCAVATOR = register("electrum_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.SILVER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem ROSE_GOLD_PICKAXE = register("rose_gold_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.ROSE_GOLD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem ROSE_GOLD_AXE = register("rose_gold_axe", new AxeItem(AstromineFoundationsToolMaterials.ROSE_GOLD, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem ROSE_GOLD_SHOVEL = register("rose_gold_shovel", new ShovelItem(AstromineFoundationsToolMaterials.ROSE_GOLD, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem ROSE_GOLD_HOE = register("rose_gold_hoe", new HoeItem(AstromineFoundationsToolMaterials.ROSE_GOLD, -1, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_SWORD = register("rose_gold_sword", new SwordItem(AstromineFoundationsToolMaterials.ROSE_GOLD, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_MINING_TOOL = register("rose_gold_mining_tool", new DynamicToolItem(ROSE_GOLD_SHOVEL, ROSE_GOLD_PICKAXE, AstromineFoundationsToolMaterials.ROSE_GOLD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_MATTOCK = register("rose_gold_mattock", new DynamicToolItem(ROSE_GOLD_HOE, ROSE_GOLD_AXE, AstromineFoundationsToolMaterials.ROSE_GOLD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_HAMMER = register("rose_gold_hammer", new HammerItem(AstromineFoundationsToolMaterials.ROSE_GOLD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ROSE_GOLD_EXCAVATOR = register("rose_gold_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.ROSE_GOLD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem STERLING_SILVER_PICKAXE = register("sterling_silver_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.STERLING_SILVER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem STERLING_SILVER_AXE = register("sterling_silver_axe", new AxeItem(AstromineFoundationsToolMaterials.STERLING_SILVER, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem STERLING_SILVER_SHOVEL = register("sterling_silver_shovel", new ShovelItem(AstromineFoundationsToolMaterials.STERLING_SILVER, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem STERLING_SILVER_HOE = register("sterling_silver_hoe", new HoeItem(AstromineFoundationsToolMaterials.STERLING_SILVER, -2, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_SWORD = register("sterling_silver_sword", new SwordItem(AstromineFoundationsToolMaterials.STERLING_SILVER, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_MINING_TOOL = register("sterling_silver_mining_tool", new DynamicToolItem(STERLING_SILVER_SHOVEL, STERLING_SILVER_PICKAXE, AstromineFoundationsToolMaterials.STERLING_SILVER, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_MATTOCK = register("sterling_silver_mattock", new DynamicToolItem(STERLING_SILVER_HOE, STERLING_SILVER_AXE, AstromineFoundationsToolMaterials.STERLING_SILVER, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_HAMMER = register("sterling_silver_hammer", new HammerItem(AstromineFoundationsToolMaterials.STERLING_SILVER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_EXCAVATOR = register("sterling_silver_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.STERLING_SILVER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem FOOLS_GOLD_PICKAXE = register("fools_gold_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.FOOLS_GOLD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem FOOLS_GOLD_AXE = register("fools_gold_axe", new AxeItem(AstromineFoundationsToolMaterials.FOOLS_GOLD, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem FOOLS_GOLD_SHOVEL = register("fools_gold_shovel", new ShovelItem(AstromineFoundationsToolMaterials.FOOLS_GOLD, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem FOOLS_GOLD_HOE = register("fools_gold_hoe", new HoeItem(AstromineFoundationsToolMaterials.FOOLS_GOLD, -3, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_SWORD = register("fools_gold_sword", new SwordItem(AstromineFoundationsToolMaterials.FOOLS_GOLD, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_MINING_TOOL = register("fools_gold_mining_tool", new DynamicToolItem(FOOLS_GOLD_SHOVEL, FOOLS_GOLD_PICKAXE, AstromineFoundationsToolMaterials.FOOLS_GOLD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_MATTOCK = register("fools_gold_mattock", new DynamicToolItem(FOOLS_GOLD_HOE, FOOLS_GOLD_AXE, AstromineFoundationsToolMaterials.FOOLS_GOLD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_HAMMER = register("fools_gold_hammer", new HammerItem(AstromineFoundationsToolMaterials.FOOLS_GOLD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_EXCAVATOR = register("fools_gold_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.FOOLS_GOLD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem METITE_PICKAXE = register("metite_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.METITE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem METITE_AXE = register("metite_axe", new AxeItem(AstromineFoundationsToolMaterials.METITE, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem METITE_SHOVEL = register("metite_shovel", new ShovelItem(AstromineFoundationsToolMaterials.METITE, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem METITE_HOE = register("metite_hoe", new HoeItem(AstromineFoundationsToolMaterials.METITE, -4, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item METITE_SWORD = register("metite_sword", new SwordItem(AstromineFoundationsToolMaterials.METITE, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item METITE_MINING_TOOL = register("metite_mining_tool", new DynamicToolItem(METITE_SHOVEL, METITE_PICKAXE, AstromineFoundationsToolMaterials.METITE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item METITE_MATTOCK = register("metite_mattock", new DynamicToolItem(METITE_HOE, METITE_AXE, AstromineFoundationsToolMaterials.METITE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item METITE_HAMMER = register("metite_hammer", new HammerItem(AstromineFoundationsToolMaterials.METITE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item METITE_EXCAVATOR = register("metite_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.METITE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem ASTERITE_PICKAXE = register("asterite_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.ASTERITE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem ASTERITE_AXE = register("asterite_axe", new AxeItem(AstromineFoundationsToolMaterials.ASTERITE, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem ASTERITE_SHOVEL = register("asterite_shovel", new ShovelItem(AstromineFoundationsToolMaterials.ASTERITE, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem ASTERITE_HOE = register("asterite_hoe", new HoeItem(AstromineFoundationsToolMaterials.ASTERITE, -5, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_SWORD = register("asterite_sword", new SwordItem(AstromineFoundationsToolMaterials.ASTERITE, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_MINING_TOOL = register("asterite_mining_tool", new DynamicToolItem(ASTERITE_SHOVEL, ASTERITE_PICKAXE, AstromineFoundationsToolMaterials.ASTERITE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_MATTOCK = register("asterite_mattock", new DynamicToolItem(ASTERITE_HOE, ASTERITE_AXE, AstromineFoundationsToolMaterials.ASTERITE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_HAMMER = register("asterite_hammer", new HammerItem(AstromineFoundationsToolMaterials.ASTERITE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_EXCAVATOR = register("asterite_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.ASTERITE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem STELLUM_PICKAXE = register("stellum_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.STELLUM, 1, -2.8f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final AxeItem STELLUM_AXE = register("stellum_axe", new AxeItem(AstromineFoundationsToolMaterials.STELLUM, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final ShovelItem STELLUM_SHOVEL = register("stellum_shovel", new ShovelItem(AstromineFoundationsToolMaterials.STELLUM, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final HoeItem STELLUM_HOE = register("stellum_hoe", new HoeItem(AstromineFoundationsToolMaterials.STELLUM, -6, 0f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item STELLUM_SWORD = register("stellum_sword", new SwordItem(AstromineFoundationsToolMaterials.STELLUM, 3, -2.4f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item STELLUM_MINING_TOOL = register("stellum_mining_tool", new DynamicToolItem(STELLUM_SHOVEL, STELLUM_PICKAXE, AstromineFoundationsToolMaterials.STELLUM, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item STELLUM_MATTOCK = register("stellum_mattock", new DynamicToolItem(STELLUM_HOE, STELLUM_AXE, AstromineFoundationsToolMaterials.STELLUM, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item STELLUM_HAMMER = register("stellum_hammer", new HammerItem(AstromineFoundationsToolMaterials.STELLUM, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STELLUM_EXCAVATOR = register("stellum_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.STELLUM, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem GALAXIUM_PICKAXE = register("galaxium_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.GALAXIUM, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem GALAXIUM_AXE = register("galaxium_axe", new AxeItem(AstromineFoundationsToolMaterials.GALAXIUM, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem GALAXIUM_SHOVEL = register("galaxium_shovel", new ShovelItem(AstromineFoundationsToolMaterials.GALAXIUM, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem GALAXIUM_HOE = register("galaxium_hoe", new HoeItem(AstromineFoundationsToolMaterials.GALAXIUM, -5, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM_SWORD = register("galaxium_sword", new SwordItem(AstromineFoundationsToolMaterials.GALAXIUM, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM_MINING_TOOL = register("galaxium_mining_tool", new DynamicToolItem(GALAXIUM_SHOVEL, GALAXIUM_PICKAXE, AstromineFoundationsToolMaterials.GALAXIUM, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM_MATTOCK = register("galaxium_mattock", new DynamicToolItem(GALAXIUM_HOE, GALAXIUM_AXE, AstromineFoundationsToolMaterials.GALAXIUM, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM_HAMMER = register("galaxium_hammer", new HammerItem(AstromineFoundationsToolMaterials.GALAXIUM, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM_EXCAVATOR = register("galaxium_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.GALAXIUM, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem UNIVITE_PICKAXE = register("univite_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.UNIVITE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final AxeItem UNIVITE_AXE = register("univite_axe", new AxeItem(AstromineFoundationsToolMaterials.UNIVITE, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final ShovelItem UNIVITE_SHOVEL = register("univite_shovel", new ShovelItem(AstromineFoundationsToolMaterials.UNIVITE, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final HoeItem UNIVITE_HOE = register("univite_hoe", new HoeItem(AstromineFoundationsToolMaterials.UNIVITE, -6, 0f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_SWORD = register("univite_sword", new SwordItem(AstromineFoundationsToolMaterials.UNIVITE, 3, -2.4f, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_MINING_TOOL = register("univite_mining_tool", new DynamicToolItem(UNIVITE_SHOVEL, UNIVITE_PICKAXE, AstromineFoundationsToolMaterials.UNIVITE, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_MATTOCK = register("univite_mattock", new DynamicToolItem(UNIVITE_HOE, UNIVITE_AXE, AstromineFoundationsToolMaterials.UNIVITE, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_HAMMER = register("univite_hammer", new HammerItem(AstromineFoundationsToolMaterials.UNIVITE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item UNIVITE_EXCAVATOR = register("univite_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.UNIVITE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	// Armors
	public static final Item COPPER_HELMET = register("copper_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.COPPER, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_CHESTPLATE = register("copper_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.COPPER, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_LEGGINGS = register("copper_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.COPPER, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_BOOTS = register("copper_boots", new ArmorItem(AstromineFoundationsArmorMaterials.COPPER, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item TIN_HELMET = register("tin_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.TIN, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_CHESTPLATE = register("tin_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.TIN, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_LEGGINGS = register("tin_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.TIN, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_BOOTS = register("tin_boots", new ArmorItem(AstromineFoundationsArmorMaterials.TIN, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item SILVER_HELMET = register("silver_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.SILVER, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_CHESTPLATE = register("silver_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.SILVER, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_LEGGINGS = register("silver_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.SILVER, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item SILVER_BOOTS = register("silver_boots", new ArmorItem(AstromineFoundationsArmorMaterials.SILVER, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item LEAD_HELMET = register("lead_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.LEAD, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_CHESTPLATE = register("lead_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.LEAD, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_LEGGINGS = register("lead_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.LEAD, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_BOOTS = register("lead_boots", new ArmorItem(AstromineFoundationsArmorMaterials.LEAD, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item BRONZE_HELMET = register("bronze_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.BRONZE, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item BRONZE_CHESTPLATE = register("bronze_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.BRONZE, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item BRONZE_LEGGINGS = register("bronze_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.BRONZE, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item BRONZE_BOOTS = register("bronze_boots", new ArmorItem(AstromineFoundationsArmorMaterials.BRONZE, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item STEEL_HELMET = register("steel_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.STEEL, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_CHESTPLATE = register("steel_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.STEEL, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_LEGGINGS = register("steel_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.STEEL, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_BOOTS = register("steel_boots", new ArmorItem(AstromineFoundationsArmorMaterials.STEEL, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item ELECTRUM_HELMET = register("electrum_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.ELECTRUM, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_CHESTPLATE = register("electrum_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.ELECTRUM, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_LEGGINGS = register("electrum_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.ELECTRUM, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ELECTRUM_BOOTS = register("electrum_boots", new ArmorItem(AstromineFoundationsArmorMaterials.ELECTRUM, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item ROSE_GOLD_HELMET = register("rose_gold_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.ROSE_GOLD, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item ROSE_GOLD_CHESTPLATE = register("rose_gold_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.ROSE_GOLD, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item ROSE_GOLD_LEGGINGS = register("rose_gold_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.ROSE_GOLD, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item ROSE_GOLD_BOOTS = register("rose_gold_boots", new ArmorItem(AstromineFoundationsArmorMaterials.ROSE_GOLD, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item STERLING_SILVER_HELMET = register("sterling_silver_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.STERLING_SILVER, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_CHESTPLATE = register("sterling_silver_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.STERLING_SILVER, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_LEGGINGS = register("sterling_silver_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.STERLING_SILVER, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STERLING_SILVER_BOOTS = register("sterling_silver_boots", new ArmorItem(AstromineFoundationsArmorMaterials.STERLING_SILVER, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item FOOLS_GOLD_HELMET = register("fools_gold_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.FOOLS_GOLD, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_CHESTPLATE = register("fools_gold_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.FOOLS_GOLD, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_LEGGINGS = register("fools_gold_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.FOOLS_GOLD, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item FOOLS_GOLD_BOOTS = register("fools_gold_boots", new ArmorItem(AstromineFoundationsArmorMaterials.FOOLS_GOLD, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item METITE_HELMET = register("metite_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.METITE, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item METITE_CHESTPLATE = register("metite_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.METITE, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item METITE_LEGGINGS = register("metite_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.METITE, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item METITE_BOOTS = register("metite_boots", new ArmorItem(AstromineFoundationsArmorMaterials.METITE, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item ASTERITE_HELMET = register("asterite_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.ASTERITE, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_CHESTPLATE = register("asterite_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.ASTERITE, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_LEGGINGS = register("asterite_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.ASTERITE, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item ASTERITE_BOOTS = register("asterite_boots", new ArmorItem(AstromineFoundationsArmorMaterials.ASTERITE, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item STELLUM_HELMET = register("stellum_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.STELLUM, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item STELLUM_CHESTPLATE = register("stellum_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.STELLUM, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item STELLUM_LEGGINGS = register("stellum_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.STELLUM, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item STELLUM_BOOTS = register("stellum_boots", new ArmorItem(AstromineFoundationsArmorMaterials.STELLUM, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item GALAXIUM_HELMET = register("galaxium_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.GALAXIUM, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM_CHESTPLATE = register("galaxium_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.GALAXIUM, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM_LEGGINGS = register("galaxium_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.GALAXIUM, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GALAXIUM_BOOTS = register("galaxium_boots", new ArmorItem(AstromineFoundationsArmorMaterials.GALAXIUM, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item UNIVITE_HELMET = register("univite_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.UNIVITE, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_CHESTPLATE = register("univite_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.UNIVITE, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_LEGGINGS = register("univite_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.UNIVITE, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item UNIVITE_BOOTS = register("univite_boots", new ArmorItem(AstromineFoundationsArmorMaterials.UNIVITE, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item BRONZE_WRENCH = register("bronze_wrench", new WrenchItem(AstromineFoundationsItems.getBasicSettings()));

	public static final Item FIRE_EXTINGUISHER = register("fire_extinguisher", new FireExtinguisherItem(AstromineFoundationsItems.getBasicSettings().maxCount(1)));

	public static Item.Settings getBasicSettings() {
		return AstromineItems.getBasicSettings().group(AstromineFoundationsItemGroups.FOUNDATIONS);
	}

	public static void initialize() {
		UseBlockCallback.EVENT.register((player, world, hand, blockHitResult) -> {
			if (!world.isClient()) {
				WorldPos worldPos = WorldPos.of(world, blockHitResult.getBlockPos());
				if (worldPos.getBlock() == Blocks.BOOKSHELF) {
					if (player.getStackInHand(hand).getItem() == METITE_AXE) {
						ItemScatterer.spawn(world, worldPos.getX(), worldPos.getY(), worldPos.getZ(), ToolUtilities.getAstromineBook());
						world.breakBlock(worldPos.getBlockPos(), false);
						return ActionResult.SUCCESS;
					}
				}
			}
			return ActionResult.PASS;
		});
	}

	public static <T extends Item> T register(String name, T item) {
		return AstromineItems.register(name, item);
	}
}
