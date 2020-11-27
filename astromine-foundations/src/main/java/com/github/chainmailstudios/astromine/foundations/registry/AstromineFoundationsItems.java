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

import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;

import com.github.chainmailstudios.astromine.common.item.AnimatedArmorItem;
import com.github.chainmailstudios.astromine.common.item.DynamicToolItemItem;
import com.github.chainmailstudios.astromine.common.item.WrenchItem;
import com.github.chainmailstudios.astromine.foundations.common.item.FireExtinguisherItem;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import draylar.magna.item.ExcavatorItem;
import draylar.magna.item.HammerItem;

public class AstromineFoundationsItems extends AstromineItems {
	public static final Item COPPER_NUGGET = register("copper_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_NUGGET = register("tin_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_NUGGET = register("lead_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_NUGGET = register("steel_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_NUGGET = register("bronze_nugget", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item NETHERITE_NUGGET = register("netherite_nugget", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item DIAMOND_FRAGMENT = register("diamond_fragment", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item EMERALD_FRAGMENT = register("emerald_fragment", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item QUARTZ_FRAGMENT = register("quartz_fragment", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item COPPER_WIRE = register("copper_wire", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_WIRE = register("tin_wire", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item GOLD_WIRE = register("gold_wire", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item COPPER_INGOT = register("copper_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_INGOT = register("tin_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_INGOT = register("lead_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_INGOT = register("bronze_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_INGOT = register("steel_ingot", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item COPPER_DUST = register("copper_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_DUST = register("tin_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_DUST = register("lead_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_DUST = register("steel_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_DUST = register("bronze_dust", new Item(AstromineFoundationsItems.getBasicSettings()));

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

	public static final Item COPPER_TINY_DUST = register("copper_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_TINY_DUST = register("tin_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_TINY_DUST = register("lead_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_TINY_DUST = register("steel_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_TINY_DUST = register("bronze_tiny_dust", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item COPPER_PLATE = register("copper_plate", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_PLATE = register("tin_plate", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_PLATE = register("lead_plate", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_PLATE = register("steel_plate", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_PLATE = register("bronze_plate", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item IRON_PLATE = register("iron_plate", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item GOLD_PLATE = register("gold_plate", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item NETHERITE_PLATE = register("netherite_plate", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item COPPER_GEAR = register("copper_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_GEAR = register("tin_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_GEAR = register("lead_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_GEAR = register("steel_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_GEAR = register("bronze_gear", new Item(AstromineFoundationsItems.getBasicSettings()));

	public static final Item IRON_GEAR = register("iron_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item GOLD_GEAR = register("gold_gear", new Item(AstromineFoundationsItems.getBasicSettings()));
	public static final Item NETHERITE_GEAR = register("netherite_gear", new Item(AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final Item WOODEN_MINING_TOOL = register("wooden_mining_tool", new DynamicToolItemItem((MiningToolItem) Items.WOODEN_SHOVEL, (MiningToolItem) Items.WOODEN_PICKAXE, ToolMaterials.WOOD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item WOODEN_MATTOCK = register("wooden_mattock", new DynamicToolItemItem((MiningToolItem) Items.WOODEN_HOE, (MiningToolItem) Items.WOODEN_AXE, ToolMaterials.WOOD, AstromineFoundationsItems.getBasicSettings()));

	public static final Item STONE_MINING_TOOL = register("stone_mining_tool", new DynamicToolItemItem((MiningToolItem) Items.STONE_SHOVEL, (MiningToolItem) Items.STONE_PICKAXE, ToolMaterials.STONE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STONE_MATTOCK = register("stone_mattock", new DynamicToolItemItem((MiningToolItem) Items.STONE_HOE, (MiningToolItem) Items.STONE_AXE, ToolMaterials.STONE, AstromineFoundationsItems.getBasicSettings()));

	public static final Item IRON_MINING_TOOL = register("iron_mining_tool", new DynamicToolItemItem((MiningToolItem) Items.IRON_SHOVEL, (MiningToolItem) Items.IRON_PICKAXE, ToolMaterials.IRON, AstromineFoundationsItems.getBasicSettings()));
	public static final Item IRON_MATTOCK = register("iron_mattock", new DynamicToolItemItem((MiningToolItem) Items.IRON_HOE, (MiningToolItem) Items.IRON_AXE, ToolMaterials.IRON, AstromineFoundationsItems.getBasicSettings()));

	public static final Item GOLDEN_MINING_TOOL = register("golden_mining_tool", new DynamicToolItemItem((MiningToolItem) Items.GOLDEN_SHOVEL, (MiningToolItem) Items.GOLDEN_PICKAXE, ToolMaterials.GOLD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item GOLDEN_MATTOCK = register("golden_mattock", new DynamicToolItemItem((MiningToolItem) Items.GOLDEN_HOE, (MiningToolItem) Items.GOLDEN_AXE, ToolMaterials.GOLD, AstromineFoundationsItems.getBasicSettings()));

	public static final Item DIAMOND_MINING_TOOL = register("diamond_mining_tool", new DynamicToolItemItem((MiningToolItem) Items.DIAMOND_SHOVEL, (MiningToolItem) Items.DIAMOND_PICKAXE, ToolMaterials.DIAMOND, AstromineFoundationsItems.getBasicSettings()));
	public static final Item DIAMOND_MATTOCK = register("diamond_mattock", new DynamicToolItemItem((MiningToolItem) Items.DIAMOND_HOE, (MiningToolItem) Items.DIAMOND_AXE, ToolMaterials.DIAMOND, AstromineFoundationsItems.getBasicSettings()));

	public static final Item NETHERITE_MINING_TOOL = register("netherite_mining_tool", new DynamicToolItemItem((MiningToolItem) Items.NETHERITE_SHOVEL, (MiningToolItem) Items.NETHERITE_PICKAXE, ToolMaterials.NETHERITE, AstromineFoundationsItems.getBasicSettings().fireproof()));
	public static final Item NETHERITE_MATTOCK = register("netherite_mattock", new DynamicToolItemItem((MiningToolItem) Items.NETHERITE_HOE, (MiningToolItem) Items.NETHERITE_AXE, ToolMaterials.NETHERITE, AstromineFoundationsItems.getBasicSettings().fireproof()));

	public static final PickaxeItem COPPER_PICKAXE = register("copper_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.COPPER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem COPPER_AXE = register("copper_axe", new AxeItem(AstromineFoundationsToolMaterials.COPPER, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem COPPER_SHOVEL = register("copper_shovel", new ShovelItem(AstromineFoundationsToolMaterials.COPPER, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem COPPER_HOE = register("copper_hoe", new HoeItem(AstromineFoundationsToolMaterials.COPPER, -1, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_SWORD = register("copper_sword", new SwordItem(AstromineFoundationsToolMaterials.COPPER, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_MINING_TOOL = register("copper_mining_tool", new DynamicToolItemItem(COPPER_PICKAXE, COPPER_SHOVEL, AstromineFoundationsToolMaterials.COPPER, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_MATTOCK = register("copper_mattock", new DynamicToolItemItem(COPPER_HOE, COPPER_AXE, AstromineFoundationsToolMaterials.COPPER, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_HAMMER = register("copper_hammer", new HammerItem(AstromineFoundationsToolMaterials.COPPER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_EXCAVATOR = register("copper_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.COPPER, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem TIN_PICKAXE = register("tin_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.TIN, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem TIN_AXE = register("tin_axe", new AxeItem(AstromineFoundationsToolMaterials.TIN, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem TIN_SHOVEL = register("tin_shovel", new ShovelItem(AstromineFoundationsToolMaterials.TIN, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem TIN_HOE = register("tin_hoe", new HoeItem(AstromineFoundationsToolMaterials.TIN, -1, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_SWORD = register("tin_sword", new SwordItem(AstromineFoundationsToolMaterials.TIN, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_MINING_TOOL = register("tin_mining_tool", new DynamicToolItemItem(TIN_SHOVEL, TIN_PICKAXE, AstromineFoundationsToolMaterials.TIN, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_MATTOCK = register("tin_mattock", new DynamicToolItemItem(TIN_HOE, TIN_AXE, AstromineFoundationsToolMaterials.TIN, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_HAMMER = register("tin_hammer", new HammerItem(AstromineFoundationsToolMaterials.TIN, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_EXCAVATOR = register("tin_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.TIN, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem LEAD_PICKAXE = register("lead_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.LEAD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem LEAD_AXE = register("lead_axe", new AxeItem(AstromineFoundationsToolMaterials.LEAD, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem LEAD_SHOVEL = register("lead_shovel", new ShovelItem(AstromineFoundationsToolMaterials.LEAD, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem LEAD_HOE = register("lead_hoe", new HoeItem(AstromineFoundationsToolMaterials.LEAD, -2, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_SWORD = register("lead_sword", new SwordItem(AstromineFoundationsToolMaterials.LEAD, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_MINING_TOOL = register("lead_mining_tool", new DynamicToolItemItem(LEAD_PICKAXE, LEAD_SHOVEL, AstromineFoundationsToolMaterials.LEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_MATTOCK = register("lead_mattock", new DynamicToolItemItem(LEAD_HOE, LEAD_AXE, AstromineFoundationsToolMaterials.LEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_HAMMER = register("lead_hammer", new HammerItem(AstromineFoundationsToolMaterials.LEAD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item LEAD_EXCAVATOR = register("lead_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.LEAD, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem BRONZE_PICKAXE = register("bronze_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.BRONZE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem BRONZE_AXE = register("bronze_axe", new AxeItem(AstromineFoundationsToolMaterials.BRONZE, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem BRONZE_SHOVEL = register("bronze_shovel", new ShovelItem(AstromineFoundationsToolMaterials.BRONZE, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem BRONZE_HOE = register("bronze_hoe", new HoeItem(AstromineFoundationsToolMaterials.BRONZE, -2, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_SWORD = register("bronze_sword", new SwordItem(AstromineFoundationsToolMaterials.BRONZE, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_MINING_TOOL = register("bronze_mining_tool", new DynamicToolItemItem(BRONZE_SHOVEL, BRONZE_PICKAXE, AstromineFoundationsToolMaterials.BRONZE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_MATTOCK = register("bronze_mattock", new DynamicToolItemItem(BRONZE_HOE, BRONZE_AXE, AstromineFoundationsToolMaterials.BRONZE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_HAMMER = register("bronze_hammer", new HammerItem(AstromineFoundationsToolMaterials.BRONZE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_EXCAVATOR = register("bronze_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.BRONZE, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final PickaxeItem STEEL_PICKAXE = register("steel_pickaxe", new PickaxeItem(AstromineFoundationsToolMaterials.STEEL, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final AxeItem STEEL_AXE = register("steel_axe", new AxeItem(AstromineFoundationsToolMaterials.STEEL, 5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final ShovelItem STEEL_SHOVEL = register("steel_shovel", new ShovelItem(AstromineFoundationsToolMaterials.STEEL, 1.5f, -3.0f, AstromineFoundationsItems.getBasicSettings()));
	public static final HoeItem STEEL_HOE = register("steel_hoe", new HoeItem(AstromineFoundationsToolMaterials.STEEL, -3, 0f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_SWORD = register("steel_sword", new SwordItem(AstromineFoundationsToolMaterials.STEEL, 3, -2.4f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_MINING_TOOL = register("steel_mining_tool", new DynamicToolItemItem(STEEL_SHOVEL, STEEL_PICKAXE, AstromineFoundationsToolMaterials.STEEL, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_MATTOCK = register("steel_mattock", new DynamicToolItemItem(STEEL_HOE, STEEL_AXE, AstromineFoundationsToolMaterials.STEEL, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_HAMMER = register("steel_hammer", new HammerItem(AstromineFoundationsToolMaterials.STEEL, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_EXCAVATOR = register("steel_excavator", new ExcavatorItem(AstromineFoundationsToolMaterials.STEEL, 1, -2.8f, AstromineFoundationsItems.getBasicSettings()));

	public static final Item COPPER_HELMET = register("copper_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.COPPER, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_CHESTPLATE = register("copper_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.COPPER, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_LEGGINGS = register("copper_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.COPPER, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item COPPER_BOOTS = register("copper_boots", new ArmorItem(AstromineFoundationsArmorMaterials.COPPER, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

	public static final Item TIN_HELMET = register("tin_helmet", new ArmorItem(AstromineFoundationsArmorMaterials.TIN, EquipmentSlot.HEAD, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_CHESTPLATE = register("tin_chestplate", new ArmorItem(AstromineFoundationsArmorMaterials.TIN, EquipmentSlot.CHEST, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_LEGGINGS = register("tin_leggings", new ArmorItem(AstromineFoundationsArmorMaterials.TIN, EquipmentSlot.LEGS, AstromineFoundationsItems.getBasicSettings()));
	public static final Item TIN_BOOTS = register("tin_boots", new ArmorItem(AstromineFoundationsArmorMaterials.TIN, EquipmentSlot.FEET, AstromineFoundationsItems.getBasicSettings()));

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

	
	
	
	

	
	
	
	

	
	
	
	

	
	
	
	

	
	
	
	

	
	
	
	

	
	
	
	

	
	
	
	

	
	
	
	

	
	
	
	

	
	
	
	

	

	public static final Item COPPER_WRENCH = register("copper_wrench", new WrenchItem(AstromineFoundationsToolMaterials.COPPER, AstromineFoundationsItems.getBasicSettings()));
	public static final Item BRONZE_WRENCH = register("bronze_wrench", new WrenchItem(AstromineFoundationsToolMaterials.BRONZE, AstromineFoundationsItems.getBasicSettings()));
	public static final Item STEEL_WRENCH = register("steel_wrench", new WrenchItem(AstromineFoundationsToolMaterials.STEEL, AstromineFoundationsItems.getBasicSettings()));

	public static final Item FIRE_EXTINGUISHER = register("fire_extinguisher", new FireExtinguisherItem(AstromineFoundationsItems.getBasicSettings().maxCount(1)));

	// Apples
	public static final FoodComponent LEAD_APPLE_COMPONENT = new FoodComponent.Builder().hunger(2).saturationModifier(0.4F).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 40, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1000, 2), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 500, 1), 1.0F).alwaysEdible().build();
	public static final Item LEAD_APPLE = register("lead_apple", new Item(AstromineFoundationsItems.getBasicSettings().food(LEAD_APPLE_COMPONENT).rarity(Rarity.RARE)));

	public static final Item BLADES = register("blades", new Item(getBasicSettings()));

	public static Item.Settings getBasicSettings() {
		return AstromineItems.getBasicSettings().group(AstromineFoundationsItemGroups.FOUNDATIONS);
	}

	public static void initialize() {
		FuelRegistryImpl.INSTANCE.add(WOODEN_MATTOCK, 200);
		FuelRegistryImpl.INSTANCE.add(WOODEN_MINING_TOOL, 200);
	}

	public static <T extends Item> T register(String name, T item) {
		return AstromineItems.register(name, item);
	}
}
