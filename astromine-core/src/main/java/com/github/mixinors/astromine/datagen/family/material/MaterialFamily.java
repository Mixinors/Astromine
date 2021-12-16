package com.github.mixinors.astromine.datagen.family.material;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.Variant;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.tag.TagFactory;

public class MaterialFamily implements Comparable<MaterialFamily> {
	final Map<ItemVariant, Item> itemVariants = Maps.newHashMap();
	final Map<BlockVariant, Block> blockVariants = Maps.newHashMap();
	final Set<AlloyIngredients> alloyIngredients = Sets.newHashSet();
	private final Item baseItem;
	private final MaterialType type;
	private final String name;
	@Nullable
	MaterialFamily smithingBase;
	boolean piglinLoved = false;
	boolean generateModels = true;
	boolean generateRecipes = true;
	boolean generateTags = true;
	boolean generateLootTables = true;
	@Nullable
	String group;
	@Nullable
	String unlockCriterionName;
	float oreSmeltingExperience = 0.7f;
	@Nullable
	String baseTagPathOverride;
	boolean validForBeacon = false;
	@Nullable
	String alias;
	private boolean hasEquipment = false;
	private boolean block2x2;
	private int miningLevel = 0;

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

	public Map<ItemVariant, Tag.Identified<Item>> getItemTags() {
		return Maps.toMap(this.itemVariants.keySet(), this::getTag);
	}

	public Map<BlockVariant, Tag.Identified<Block>> getBlockTags() {
		return Maps.toMap(this.blockVariants.keySet(), this::getTag);
	}

	public Map<BlockVariant, Tag.Identified<Item>> getBlockItemTags() {
		return Maps.toMap(this.blockVariants.keySet(), this::getItemTag);
	}

	public Map<Variant<?>, Tag.Identified<Item>> getAllItemTags() {
		return new ImmutableMap.Builder<Variant<?>, Tag.Identified<Item>>()
				.putAll(getItemTags()).putAll(getBlockItemTags()).build();
	}

	public boolean isBaseAstromine() {
		return Registry.ITEM.getId(getBaseItem()).getNamespace().equals(AMCommon.MOD_ID);
	}

	public boolean isVariantAstromine(ItemVariant variant) {
		return hasVariant(variant) && Registry.ITEM.getId(getVariant(variant)).getNamespace().equals(AMCommon.MOD_ID);
	}

	public boolean isVariantAstromine(BlockVariant variant) {
		return hasVariant(variant) && Registry.BLOCK.getId(getVariant(variant)).getNamespace().equals(AMCommon.MOD_ID);
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
		if (input.equals(output)) return false;
		return shouldGenerateRecipes() && hasVariants(input, output) && areAnyVariantsAstromine(input, output);
	}

	public boolean shouldGenerateRecipe(BlockVariant input, BlockVariant output) {
		if (input.equals(output)) return false;
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

	public boolean shouldGenerateModel(ItemVariant variant) {
		return shouldGenerateModels() && hasVariant(variant) && isVariantAstromine(variant);
	}

	public boolean shouldGenerateModel(BlockVariant variant) {
		return shouldGenerateModels() && hasVariant(variant) && isVariantAstromine(variant);
	}

	public boolean shouldGenerateLootTable(BlockVariant variant) {
		return shouldGenerateLootTables() && hasVariant(variant) && isVariantAstromine(variant);
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

	public boolean shouldGenerateLootTables() {
		return this.generateLootTables;
	}

	public boolean isPiglinLoved() {
		return this.piglinLoved;
	}

	public Optional<String> getGroup() {
		if (StringUtils.isBlank(this.group)) {
			return Optional.empty();
		}
		return Optional.of(this.group);
	}

	public Optional<String> getUnlockCriterionName() {
		if (StringUtils.isBlank(this.unlockCriterionName)) {
			return Optional.empty();
		}
		return Optional.of(this.unlockCriterionName);
	}

	public boolean usesSmithing() {
		return smithingBase != null;
	}

	public boolean isAlloy() {
		return !alloyIngredients.isEmpty();
	}

	public Optional<MaterialFamily> getSmithingBase() {
		return Optional.ofNullable(smithingBase);
	}

	public Set<AlloyIngredients> getAlloyIngredients() {
		return alloyIngredients;
	}

	public float getOreSmeltingExperience() {
		return oreSmeltingExperience;
	}

	public Identifier getTagId(Variant<?> variant) {
		if (getBaseVariant().equals(variant) && baseTagPathOverride != null)
			return new Identifier("c", baseTagPathOverride);
		else return variant.getTagId(this);
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

	public <T extends ItemConvertible> Tag.Identified<T> getTag(Variant<T> variant) {
		return variant.getTag(this);
	}

	public <T extends ItemConvertible> Tag.Identified<T> getAliasTag(Variant<T> variant) {
		return variant.getTag(alias);
	}

	public Tag.Identified<Item> getItemTag(BlockVariant variant) {
		return TagFactory.ITEM.create(getTagId(variant));
	}

	public Tag.Identified<Item> getItemTag(String variant) {
		return TagFactory.ITEM.create(getTagId(variant));
	}

	public Tag.Identified<Block> getBlockTag(String variant) {
		return TagFactory.BLOCK.create(getTagId(variant));
	}

	public Tag.Identified<Item> getAliasItemTag(BlockVariant variant) {
		return TagFactory.ITEM.create(getAliasTagId(variant));
	}

	public Tag.Identified<Item> getAliasItemTag(String variant) {
		return TagFactory.ITEM.create(getAliasTagId(variant));
	}

	public Tag.Identified<Block> getAliasBlockTag(String variant) {
		return TagFactory.BLOCK.create(getAliasTagId(variant));
	}

	public Tag.Identified<Item> getBaseTag() {
		return getTag(getBaseVariant());
	}

	public Tag.Identified<Item> getAliasBaseTag() {
		return getAliasTag(getBaseVariant());
	}

	public boolean isValidForBeacon() {
		return validForBeacon;
	}

	public boolean hasAlias() {
		return alias != null;
	}

	public Optional<String> getAlias() {
		if (StringUtils.isBlank(this.alias)) {
			return Optional.empty();
		}
		return Optional.of(this.alias);
	}

	public boolean hasEquipment() {
		return hasEquipment;
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
			this.family.hasEquipment = true;
			return this;
		}

		public MaterialFamily.Builder armor(Item helmet, Item chestplate, Item leggings, Item boots) {
			this.family.itemVariants.put(ItemVariant.HELMET, helmet);
			this.family.itemVariants.put(ItemVariant.CHESTPLATE, chestplate);
			this.family.itemVariants.put(ItemVariant.LEGGINGS, leggings);
			this.family.itemVariants.put(ItemVariant.BOOTS, boots);
			this.family.hasEquipment = true;
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

		public MaterialFamily.Builder noGenerateLootTables() {
			this.family.generateLootTables = false;
			return this;
		}

		public MaterialFamily.Builder group(String group) {
			this.family.group = group;
			return this;
		}

		public MaterialFamily.Builder unlockCriterionName(String unlockCriterionName) {
			this.family.unlockCriterionName = unlockCriterionName;
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

		public MaterialFamily.Builder alloyIngredients(MaterialFamily first, MaterialFamily second) {
			return alloyIngredients(first, 1, second, 1);
		}

		public MaterialFamily.Builder alloyIngredients(MaterialFamily first, int firstCount, MaterialFamily second, int secondCount) {
			return alloyIngredients(first, firstCount, second, secondCount, firstCount + secondCount);
		}

		public MaterialFamily.Builder alloyIngredients(MaterialFamily first, MaterialFamily second, int outputCount) {
			return alloyIngredients(first, 1, second, 1, outputCount);
		}

		public MaterialFamily.Builder alloyIngredients(MaterialFamily first, int firstCount, MaterialFamily second, int secondCount, int outputCount) {
			this.family.alloyIngredients.add(new AlloyIngredients(first, firstCount, second, secondCount, outputCount));
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

	public record AlloyIngredients(MaterialFamily first, int firstCount, MaterialFamily second, int secondCount,
								   int outputCount) {
	}
}
