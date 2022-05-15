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

package com.github.mixinors.astromine.datagen.family.material;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.fluid.SimpleFluid;
import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.Variant;
import com.github.mixinors.astromine.registry.common.AMTags;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MaterialFamily implements Comparable<MaterialFamily> {
	public static final float DEFAULT_ORE_SMELTING_EXPERIENCE = 0.7F;
	public static final int DEFAULT_BASE_MELTING_TIME = 200;
	public static final int DEFAULT_BASE_MELTING_ENERGY = 1600;

	final Map<ItemVariant, Item> itemVariants = Maps.newHashMap();
	final Map<BlockVariant, Block> blockVariants = Maps.newHashMap();
	final Set<AlloyInfo> alloyInfos = Sets.newHashSet();
	private final Item baseItem;
	private final MaterialType type;
	private final String name;
	@Nullable
	MaterialFamily smithingBase;
	boolean piglinLoved = false;
	boolean generateModels = true;
	boolean generateRecipes = true;
	boolean generateTags = true;
	boolean generateHarvestTags = true;
	boolean generateLootTables = true;
	float oreSmeltingExperience = DEFAULT_ORE_SMELTING_EXPERIENCE;
	@Nullable
	String baseTagPathOverride;
	boolean validForBeacon = false;
	@Nullable
	String alias;
	private boolean block2x2;
	private int miningLevel = 0;
	@Nullable
	SimpleFluid moltenFluid;
	int baseMeltingTime = DEFAULT_BASE_MELTING_TIME;
	int baseMeltingEnergy = DEFAULT_BASE_MELTING_ENERGY;
	
	MaterialFamily(String name, Item baseItem, MaterialType type) {
		this.baseItem = baseItem;
		this.type = type;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Item getBaseItem() {
		return this.baseItem;
	}
	
	public MaterialType getType() {
		return type;
	}
	
	public ItemVariant getBaseVariant() {
		return getType().asVariant();
	}
	
	public Map<ItemVariant, Item> getItemVariants() {
		return this.itemVariants;
	}
	
	public Map<BlockVariant, Block> getBlockVariants() {
		return this.blockVariants;
	}
	
	public Map<ItemVariant, TagKey<Item>> getItemTags() {
		return Maps.toMap(this.itemVariants.keySet(), this::getTag);
	}
	
	public Map<BlockVariant, TagKey<Block>> getBlockTags() {
		return Maps.toMap(this.blockVariants.keySet(), this::getTag);
	}
	
	public Map<BlockVariant, TagKey<Item>> getBlockItemTags() {
		return Maps.toMap(this.blockVariants.keySet(), this::getItemTag);
	}
	
	public boolean isBaseAstromine() {
		return isVariantAstromine(getBaseVariant());
	}
	
	public boolean isVariantAstromine(ItemVariant variant) {
		return isVariantOfNamespace(variant, AMCommon.MOD_ID);
	}
	
	public boolean isVariantAstromine(BlockVariant variant) {
		return isVariantOfNamespace(variant, AMCommon.MOD_ID);
	}
	
	public boolean isVariantVanilla(ItemVariant variant) {
		return isVariantOfNamespace(variant, "minecraft");
	}
	
	public boolean isVariantVanilla(BlockVariant variant) {
		return isVariantOfNamespace(variant, "minecraft");
	}
	
	public boolean isVariantOfNamespace(ItemVariant variant, String namespace) {
		return hasVariant(variant) && Registry.ITEM.getId(getVariant(variant)).getNamespace().equals(namespace);
	}
	
	public boolean isVariantOfNamespace(BlockVariant variant, String namespace) {
		return hasVariant(variant) && Registry.BLOCK.getId(getVariant(variant)).getNamespace().equals(namespace);
	}
	
	public boolean areVariantsAstromine(ItemVariant... variants) {
		return Arrays.stream(variants).allMatch(this::isVariantAstromine);
	}
	
	public boolean areVariantsAstromine(BlockVariant... variants) {
		return Arrays.stream(variants).allMatch(this::isVariantAstromine);
	}
	
	public boolean areAnyVariantsAstromine(ItemVariant... variants) {
		return Arrays.stream(variants).anyMatch(this::isVariantAstromine);
	}
	
	public boolean areAnyVariantsAstromine(BlockVariant... variants) {
		return Arrays.stream(variants).anyMatch(this::isVariantAstromine);
	}
	
	public boolean shouldGenerateRecipe(ItemVariant output) {
		return shouldGenerateRecipe(getBaseVariant(), output);
	}
	
	public boolean shouldGenerateRecipe(BlockVariant output) {
		return shouldGenerateRecipe(getBaseVariant(), output);
	}
	
	public boolean shouldGenerateRecipe(ItemVariant input, ItemVariant output) {
		if (input.equals(output)) {
			return false;
		}
		return shouldGenerateRecipes() && hasVariants(input, output) && areAnyVariantsAstromine(input, output);
	}
	
	public boolean shouldGenerateRecipe(BlockVariant input, BlockVariant output) {
		if (input.equals(output)) {
			return false;
		}
		return shouldGenerateRecipes() && hasVariants(input, output) && areAnyVariantsAstromine(input, output);
	}
	
	public boolean shouldGenerateRecipe(ItemVariant input, BlockVariant output) {
		return shouldGenerateRecipes() && hasVariant(input) && hasVariant(output) && (isVariantAstromine(input) || isVariantAstromine(output));
	}
	
	public boolean shouldGenerateRecipe(BlockVariant input, ItemVariant output) {
		return shouldGenerateRecipes() && hasVariant(input) && hasVariant(output) && (isVariantAstromine(input) || isVariantAstromine(output));
	}
	
	public Item getVariant(ItemVariant variant) {
		return this.itemVariants.get(variant);
	}
	
	public Block getVariant(BlockVariant variant) {
		return this.blockVariants.get(variant);
	}
	
	public boolean hasVariant(ItemVariant variant) {
		return this.itemVariants.containsKey(variant);
	}
	
	public boolean hasVariant(BlockVariant variant) {
		return this.blockVariants.containsKey(variant);
	}
	
	public boolean hasVariants(ItemVariant... variants) {
		return Arrays.stream(variants).allMatch(this::hasVariant);
	}
	
	public boolean hasVariants(BlockVariant... variants) {
		return Arrays.stream(variants).allMatch(this::hasVariant);
	}
	
	public boolean hasAnyVariants(ItemVariant... variants) {
		return Arrays.stream(variants).anyMatch(this::hasVariant);
	}
	
	public boolean hasAnyVariants(BlockVariant... variants) {
		return Arrays.stream(variants).anyMatch(this::hasVariant);
	}
	
	public boolean hasAnyItemVariants(Collection<ItemVariant> variants) {
		return variants.stream().anyMatch(this::hasVariant);
	}
	
	public boolean hasAnyBlockVariants(Collection<BlockVariant> variants) {
		return variants.stream().anyMatch(this::hasVariant);
	}
	
	public boolean areAnyItemVariantsVanilla(Collection<ItemVariant> variants) {
		return variants.stream().anyMatch(this::isVariantVanilla);
	}
	
	public boolean areAnyBlockVariantsVanilla(Collection<BlockVariant> variants) {
		return variants.stream().anyMatch(this::isVariantVanilla);
	}
	
	public boolean shouldGenerateModel(ItemVariant variant) {
		return shouldGenerateModels() && hasVariant(variant) && isVariantAstromine(variant);
	}
	
	public boolean shouldGenerateModel(BlockVariant variant) {
		return shouldGenerateModels() && hasVariant(variant) && isVariantAstromine(variant);
	}
	
	public boolean shouldGenerateLootTable(BlockVariant variant) {
		return shouldGenerateLootTables() && hasVariant(variant) && isVariantAstromine(variant);
	}
	
	public boolean shouldGenerateHarvestTags(BlockVariant variant) {
		return shouldGenerateHarvestTags() && hasVariant(variant) && isVariantAstromine(variant);
	}
	
	public boolean shouldGenerateModels() {
		return this.generateModels;
	}
	
	public boolean shouldGenerateRecipes() {
		return this.generateRecipes;
	}
	
	public boolean shouldGenerateTags() {
		return this.generateTags;
	}
	
	public boolean shouldGenerateHarvestTags() {
		return this.generateHarvestTags;
	}
	
	public boolean shouldGenerateLootTables() {
		return this.generateLootTables;
	}
	
	public boolean isPiglinLoved() {
		return this.piglinLoved;
	}
	
	public boolean usesSmithing() {
		return smithingBase != null;
	}
	
	public boolean isAlloy() {
		return !alloyInfos.isEmpty();
	}
	
	public Optional<MaterialFamily> getSmithingBase() {
		return Optional.ofNullable(smithingBase);
	}
	
	public Set<AlloyInfo> getAlloyInfos() {
		return alloyInfos;
	}
	
	public float getOreSmeltingExperience() {
		return oreSmeltingExperience;
	}
	
	public Identifier getTagId(Variant<?> variant) {
		if (getBaseVariant().equals(variant) && baseTagPathOverride != null) {
			return new Identifier("c", baseTagPathOverride);
		} else {
			return variant.getTagId(this);
		}
	}
	
	public Identifier getTagId(String variant) {
		return new Identifier("c", name + "_" + variant);
	}
	
	public Identifier getAliasTagId(Variant<?> variant) {
		return variant.getTagId(alias);
	}
	
	public Identifier getAliasTagId(String variant) {
		return new Identifier("c", alias + "_" + variant);
	}
	
	public <T extends ItemConvertible> TagKey<T> getTag(Variant<T> variant) {
		return variant.getTag(this);
	}
	
	public <T extends ItemConvertible> TagKey<T> getAliasTag(Variant<T> variant) {
		return variant.getTag(alias);
	}
	
	public TagKey<Item> getItemTag(BlockVariant variant) {
		return AMTags.ofItem(getTagId(variant));
	}
	
	public TagKey<Item> getItemTag(String variant) {
		return AMTags.ofItem(getTagId(variant));
	}
	
	public TagKey<Block> getBlockTag(String variant) {
		return AMTags.ofBlock(getTagId(variant));
	}
	
	public TagKey<Item> getAliasItemTag(BlockVariant variant) {
		return AMTags.ofItem(getAliasTagId(variant));
	}
	
	public TagKey<Item> getAliasItemTag(String variant) {
		return AMTags.ofItem(getAliasTagId(variant));
	}
	
	public TagKey<Block> getAliasBlockTag(String variant) {
		return AMTags.ofBlock(getAliasTagId(variant));
	}
	
	public TagKey<Item> getBaseTag() {
		return getTag(getBaseVariant());
	}
	
	public TagKey<Item> getAliasBaseTag() {
		return getAliasTag(getBaseVariant());
	}
	
	public boolean isValidForBeacon() {
		return validForBeacon;
	}
	
	public boolean hasAlias() {
		return alias != null;
	}
	
	public boolean isBlock2x2() {
		return block2x2;
	}
	
	@Override
	public int compareTo(@NotNull MaterialFamily o) {
		return getName().compareTo(o.getName());
	}
	
	public int getMiningLevel() {
		return miningLevel;
	}
	
	public AMDatagen.HarvestData getHarvestData(BlockVariant variant) {
		return variant.getHarvestData(this);
	}

	public Optional<SimpleFluid> getMoltenFluid() {
		return Optional.ofNullable(moltenFluid);
	}

	public int getBaseMeltingTime() {
		return baseMeltingTime;
	}

	public boolean hasMoltenFluid() {
		return moltenFluid != null;
	}

	public float getBaseMeltingEnergy() {
		return baseMeltingEnergy;
	}

	public enum MaterialType {
		INGOT(ItemVariant.INGOT),
		GEM(ItemVariant.GEM),
		MISC(ItemVariant.MISC),
		DUST(ItemVariant.DUST),
		BALL(ItemVariant.BALL);
		
		private final ItemVariant variant;
		
		MaterialType(ItemVariant variant) {
			this.variant = variant;
		}
		
		public ItemVariant asVariant() {
			return this.variant;
		}
	}
	
	public static class Builder {
		private final MaterialFamily family;
		
		public Builder(String name, Item baseItem, MaterialType type) {
			this.family = new MaterialFamily(name, baseItem, type);
			this.family.itemVariants.put(type.asVariant(), baseItem);
		}
		
		public MaterialFamily build() {
			return this.family;
		}
		
		public MaterialFamily.Builder tools(Item pickaxe, Item axe, Item shovel, Item sword, Item hoe) {
			this.family.itemVariants.put(ItemVariant.PICKAXE, pickaxe);
			this.family.itemVariants.put(ItemVariant.AXE, axe);
			this.family.itemVariants.put(ItemVariant.SHOVEL, shovel);
			this.family.itemVariants.put(ItemVariant.SWORD, sword);
			this.family.itemVariants.put(ItemVariant.HOE, hoe);
			return this;
		}
		
		public MaterialFamily.Builder armor(Item helmet, Item chestplate, Item leggings, Item boots) {
			this.family.itemVariants.put(ItemVariant.HELMET, helmet);
			this.family.itemVariants.put(ItemVariant.CHESTPLATE, chestplate);
			this.family.itemVariants.put(ItemVariant.LEGGINGS, leggings);
			this.family.itemVariants.put(ItemVariant.BOOTS, boots);
			return this;
		}
		
		public MaterialFamily.Builder nugget(Item nugget) {
			this.family.itemVariants.put(ItemVariant.NUGGET, nugget);
			return this;
		}
		
		public MaterialFamily.Builder rawOre(Item rawOre, Block rawOreBlock) {
			this.family.itemVariants.put(ItemVariant.RAW_ORE, rawOre);
			this.family.blockVariants.put(BlockVariant.RAW_ORE_BLOCK, rawOreBlock);
			return this;
		}
		
		public MaterialFamily.Builder block(Block block) {
			return block(block, false);
		}
		
		public MaterialFamily.Builder block2x2(Block block) {
			return block(block, true);
		}
		
		public MaterialFamily.Builder block(Block block, boolean is2x2) {
			this.family.blockVariants.put(BlockVariant.BLOCK, block);
			this.family.block2x2 = is2x2;
			return this;
		}
		
		public MaterialFamily.Builder overworldOre(Block stoneOre, Block deepslateOre) {
			this.family.blockVariants.put(BlockVariant.STONE_ORE, stoneOre);
			this.family.blockVariants.put(BlockVariant.DEEPSLATE_ORE, deepslateOre);
			return this;
		}
		
		public MaterialFamily.Builder netherOre(Block netherOre) {
			this.family.blockVariants.put(BlockVariant.NETHER_ORE, netherOre);
			return this;
		}
		
		public MaterialFamily.Builder meteorOre(Block meteorOre, Item meteorOreCluster) {
			this.family.blockVariants.put(BlockVariant.METEOR_ORE, meteorOre);
			this.family.itemVariants.put(ItemVariant.METEOR_ORE_CLUSTER, meteorOreCluster);
			return this;
		}
		
		public MaterialFamily.Builder asteroidOre(Block asteroidOre, Item asteroidOreCluster) {
			this.family.blockVariants.put(BlockVariant.ASTEROID_ORE, asteroidOre);
			this.family.itemVariants.put(ItemVariant.ASTEROID_ORE_CLUSTER, asteroidOreCluster);
			return this;
		}
		
		public MaterialFamily.Builder dust(Item dust) {
			this.family.itemVariants.put(ItemVariant.DUST, dust);
			return this;
		}
		
		public MaterialFamily.Builder dust(Item dust, Item tinyDust) {
			return this.dust(dust).tinyDust(tinyDust);
		}
		
		public MaterialFamily.Builder tinyDust(Item tinyDust) {
			this.family.itemVariants.put(ItemVariant.TINY_DUST, tinyDust);
			return this;
		}
		
		public MaterialFamily.Builder gear(Item gear) {
			this.family.itemVariants.put(ItemVariant.GEAR, gear);
			return this;
		}
		
		public MaterialFamily.Builder plate(Item plate) {
			this.family.itemVariants.put(ItemVariant.PLATE, plate);
			return this;
		}
		
		public MaterialFamily.Builder wire(Item wire) {
			this.family.itemVariants.put(ItemVariant.WIRE, wire);
			return this;
		}
		
		public MaterialFamily.Builder apple(Item apple) {
			this.family.itemVariants.put(ItemVariant.APPLE, apple);
			return this;
		}
		
		public MaterialFamily.Builder horseArmor(Item horseArmor) {
			this.family.itemVariants.put(ItemVariant.HORSE_ARMOR, horseArmor);
			return this;
		}

		public MaterialFamily.Builder moltenFluid(SimpleFluid moltenFluid) {
			this.family.moltenFluid = moltenFluid;
			return this;
		}

		public MaterialFamily.Builder moltenFluid(SimpleFluid moltenFluid, float meltingMultiplier) {
			this.moltenFluid(moltenFluid);
			this.family.baseMeltingTime = (int) (DEFAULT_BASE_MELTING_TIME * meltingMultiplier);
			this.family.baseMeltingEnergy = (int) (DEFAULT_BASE_MELTING_ENERGY * meltingMultiplier);
			return this;
		}

		public MaterialFamily.Builder moltenFluid(SimpleFluid moltenFluid, int baseMeltingTime, int baseMeltingEnergy) {
			this.moltenFluid(moltenFluid);
			this.family.baseMeltingTime = baseMeltingTime;
			this.family.baseMeltingEnergy = baseMeltingEnergy;
			return this;
		}
		
		public MaterialFamily.Builder noGenerateModels() {
			this.family.generateModels = false;
			return this;
		}
		
		public MaterialFamily.Builder noGenerateRecipes() {
			this.family.generateRecipes = false;
			return this;
		}
		
		public MaterialFamily.Builder noGenerateTags() {
			this.family.generateTags = false;
			return this;
		}
		
		public MaterialFamily.Builder noGenerateHarvestTags() {
			this.family.generateHarvestTags = false;
			return this;
		}
		
		public MaterialFamily.Builder noGenerateLootTables() {
			this.family.generateLootTables = false;
			return this;
		}
		
		public MaterialFamily.Builder smithingBase(MaterialFamily smithingBase) {
			this.family.smithingBase = smithingBase;
			return this;
		}
		
		public MaterialFamily.Builder piglinLoved() {
			this.family.piglinLoved = true;
			return this;
		}
		
		public MaterialFamily.Builder alloyIngredients(MaterialFamily first, MaterialFamily second, int time, int energy) {
			return alloyIngredients(first, 1, second, 1, time, energy);
		}
		
		public MaterialFamily.Builder alloyIngredients(MaterialFamily first, int firstCount, MaterialFamily second, int secondCount, int time, int energy) {
			return alloyIngredients(first, firstCount, second, secondCount, firstCount + secondCount, time, energy);
		}
		
		public MaterialFamily.Builder alloyIngredients(MaterialFamily first, MaterialFamily second, int outputCount, int time, int energy) {
			return alloyIngredients(first, 1, second, 1, outputCount, time, energy);
		}
		
		public MaterialFamily.Builder alloyIngredients(MaterialFamily first, int firstCount, MaterialFamily second, int secondCount, int outputCount, int time, int energy) {
			this.family.alloyInfos.add(new AlloyInfo(first, firstCount, second, secondCount, outputCount, time, energy));
			return this;
		}
		
		public MaterialFamily.Builder oreSmeltingExperience(float oreSmeltingExperience) {
			this.family.oreSmeltingExperience = oreSmeltingExperience;
			return this;
		}
		
		public MaterialFamily.Builder baseTagPathOverride(String baseTagPathOverride) {
			this.family.baseTagPathOverride = baseTagPathOverride;
			return this;
		}
		
		public MaterialFamily.Builder validForBeacon() {
			this.family.validForBeacon = true;
			return this;
		}
		
		public MaterialFamily.Builder alias(String alias) {
			this.family.alias = alias;
			return this;
		}
		
		public MaterialFamily.Builder miningLevel(int miningLevel) {
			this.family.miningLevel = miningLevel;
			return this;
		}
	}
	
	public record AlloyInfo(MaterialFamily firstIngredient, int firstCount, MaterialFamily secondIngredient,
			int secondCount, int outputCount, int time, int energy) {
	}
}
