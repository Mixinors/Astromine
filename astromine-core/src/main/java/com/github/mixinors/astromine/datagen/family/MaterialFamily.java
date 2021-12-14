package com.github.mixinors.astromine.datagen.family;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.WordUtils;
import com.github.mixinors.astromine.datagen.provider.AMModelProvider;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.Models;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.tag.TagFactory;

public class MaterialFamily {
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

	public boolean shouldGenerateModel(ItemVariant variant) {
		return shouldGenerateModels() && hasVariant(variant) && isVariantAstromine(variant);
	}

	public boolean shouldGenerateModel(BlockVariant variant) {
		return shouldGenerateModels() && hasVariant(variant) && isVariantAstromine(variant);
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
		if(getBaseVariant().equals(variant) && baseTagPathOverride != null) return new Identifier("c", baseTagPathOverride);
		else return variant.getTagId(name);
	}

	public Identifier getTagId(String variant) {
		return new Identifier("c", name+"_"+variant);
	}

	public Identifier getAliasTagId(Variant<?> variant) {
		return variant.getTagId(alias);
	}

	public Identifier getAliasTagId(String variant) {
		return new Identifier("c", alias+"_"+variant);
	}

	public <T extends ItemConvertible> Tag.Identified<T> getTag(Variant<T> variant) {
		return variant.getTagFactory().create(getTagId(variant));
	}

	public <T extends ItemConvertible> Tag.Identified<T> getAliasTag(Variant<T> variant) {
		return variant.getTagFactory().create(getAliasTagId(variant));
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

	public enum ItemVariant implements Variant<Item> {
		INGOT("ingot"),
		GEM("gem"),
		MISC("misc"),
		NUGGET("nugget"),
		RAW_ORE("raw_ore"),
		METEOR_CLUSTER("meteor_cluster"),
		ASTEROID_CLUSTER("asteroid_cluster"),
		DUST("dust"),
		TINY_DUST("tiny_dust"),
		GEAR("gear"),
		PLATE("plate"),
		WIRE("wire"),
		PICKAXE("pickaxe"),
		AXE("axe"),
		SHOVEL("shovel"),
		SWORD("sword"),
		HOE("hoe"),
		HELMET("helmet"),
		CHESTPLATE("chestplate"),
		LEGGINGS("leggings"),
		BOOTS("boots"),
		APPLE("apple"),
		HORSE_ARMOR("horse_armor");

		private final String name;

		ItemVariant(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public BiConsumer<ItemModelGenerator, Item> getModelRegistrar() {
			return switch (this) {
				case PICKAXE, AXE, SHOVEL, SWORD, HOE -> (itemModelGenerator, item) -> itemModelGenerator.register(item, Models.HANDHELD);
				default -> (itemModelGenerator, item) -> itemModelGenerator.register(item, Models.GENERATED);
			};
		}

		@Override
		public String getTagPath() {
			return switch (this) {
				case LEGGINGS, BOOTS -> getName();
				case APPLE -> "metal_apples";
				default -> Variant.super.getTagPath();
			};
		}

		@Override
		public String getTagPrefix() {
			return switch (this) {
				case RAW_ORE -> "raw_";
				case METEOR_CLUSTER -> "meteor_";
				case ASTEROID_CLUSTER -> "asteroid_";
				default -> Variant.super.getTagPrefix();
			};
		}

		@Override
		public String getTagSuffix() {
			return switch (this) {
				case GEM, MISC, RAW_ORE -> "";
				case METEOR_CLUSTER, ASTEROID_CLUSTER -> "_clusters";
				case APPLE -> "_apples";
				default -> Variant.super.getTagSuffix();
			};
		}

		@Override
		public String getTagCentre(String material) {
			return switch(this) {
				case GEM, MISC, RAW_ORE -> WordUtils.pluralize(material);
				default -> Variant.super.getTagCentre(material);
			};
		}

		@Override
		public boolean hasVariantTag() {
			return switch(this) {
				case MISC -> false;
				default -> Variant.super.hasVariantTag();
			};
		}

		@Override
		public TagFactory<Item> getTagFactory() {
			return TagFactory.ITEM;
		}
	}

	public enum BlockVariant implements Variant<Block> {
		BLOCK("block"),
		BLOCK_2x2("block_2x2"),
		ORE("ore"),
		DEEPSLATE_ORE("deepslate_ore"),
		NETHER_ORE("nether_ore"),
		METEOR_ORE("meteor_ore"),
		ASTEROID_ORE("asteroid_ore"),
		RAW_ORE_BLOCK("raw_ore_block");

		private final String name;

		BlockVariant(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public BiConsumer<BlockStateModelGenerator, Block> getModelRegistrar() {
			return switch (this) {
				case METEOR_ORE -> AMModelProvider::registerMeteorOre;
				case ASTEROID_ORE -> AMModelProvider::registerAsteroidOre;
				default -> BlockStateModelGenerator::registerSimpleCubeAll;
			};
		}

		@Override
		public String getTagPath() {
			return switch (this) {
				case BLOCK_2x2 -> "blocks";
				default -> Variant.super.getTagPath();
			};
		}

		@Override
		public String getTagPrefix() {
			return switch (this) {
				case NETHER_ORE -> "nether_";
				case METEOR_ORE -> "meteor_";
				case ASTEROID_ORE -> "asteroid_";
				case DEEPSLATE_ORE -> "deepslate_";
				case ORE -> "stone_";
				case RAW_ORE_BLOCK -> "raw_";
				default -> Variant.super.getTagPrefix();
			};
		}

		@Override
		public String getTagSuffix() {
			return switch (this) {
				case RAW_ORE_BLOCK -> "_blocks";
				case NETHER_ORE, METEOR_ORE, ASTEROID_ORE, DEEPSLATE_ORE -> "_ores";
				default -> Variant.super.getTagSuffix();
			};
		}

		@Override
		public TagFactory<Block> getTagFactory() {
			return TagFactory.BLOCK;
		}

		public Tag.Identified<Item> getItemTag() {
			return TagFactory.ITEM.create(new Identifier("c", getTagPath()));
		}
	}

	public enum MaterialType {
		INGOT(ItemVariant.INGOT),
		GEM(ItemVariant.GEM),
		MISC(ItemVariant.MISC),
		DUST(ItemVariant.DUST);

		private final ItemVariant variant;

		MaterialType(ItemVariant variant) {
			this.variant = variant;
		}

		public ItemVariant asVariant() {
			return this.variant;
		}
	}

	public interface Variant<T extends ItemConvertible> {
		String getName();

		BiConsumer<?, T> getModelRegistrar();

		default String getTagPath() {
			return WordUtils.pluralize(getName());
		}

		default String getTagPrefix() {
			return "";
		}

		default String getTagSuffix() {
			return "_" + getTagPath();
		}

		default String getTagCentre(String material) {
			return material;
		}

		default String getTagPath(String material) {
			return getTagPrefix() + getTagCentre(material) + getTagSuffix();
		}

		default Identifier getTagId() {
			return new Identifier("c", getTagPath());
		}

		default Identifier getTagId(String material) {
			return new Identifier("c", getTagPath(material));
		}

		default Tag.Identified<T> getTag() {
			return getTagFactory().create(getTagId());
		}

		default Tag.Identified<T> getTag(String material) {
			return getTagFactory().create(getTagId(material));
		}

		default boolean hasVariantTag() {
			return true;
		}

		TagFactory<T> getTagFactory();
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
			this.family.blockVariants.put(BlockVariant.BLOCK, block);
			return this;
		}

		public MaterialFamily.Builder block2x2(Block block2x2) {
			this.family.blockVariants.put(BlockVariant.BLOCK_2x2, block2x2);
			return this;
		}

		public MaterialFamily.Builder ore(Block ore, Block deepslateOre) {
			this.family.blockVariants.put(BlockVariant.ORE, ore);
			this.family.blockVariants.put(BlockVariant.DEEPSLATE_ORE, deepslateOre);
			return this;
		}

		public MaterialFamily.Builder netherOre(Block netherOre) {
			this.family.blockVariants.put(BlockVariant.NETHER_ORE, netherOre);
			return this;
		}

		public MaterialFamily.Builder meteorOre(Block meteorOre, Item meteorCluster) {
			this.family.blockVariants.put(BlockVariant.METEOR_ORE, meteorOre);
			this.family.itemVariants.put(ItemVariant.METEOR_CLUSTER, meteorCluster);
			return this;
		}

		public MaterialFamily.Builder asteroidOre(Block asteroidOre, Item asteroidCluster) {
			this.family.blockVariants.put(BlockVariant.ASTEROID_ORE, asteroidOre);
			this.family.itemVariants.put(ItemVariant.ASTEROID_CLUSTER, asteroidCluster);
			return this;
		}

		public MaterialFamily.Builder dust(Item dust, Item tinyDust) {
			this.family.itemVariants.put(ItemVariant.DUST, dust);
			return this.tinyDust(tinyDust);
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
	}

	public record AlloyIngredients(MaterialFamily first, int firstCount, MaterialFamily second, int secondCount,
								   int outputCount) {
	}
}
