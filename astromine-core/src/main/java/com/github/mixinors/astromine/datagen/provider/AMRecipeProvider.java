package com.github.mixinors.astromine.datagen.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.BlockVariant;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily.MaterialType;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import org.apache.logging.log4j.util.TriConsumer;

import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;
import net.minecraft.data.server.recipe.CraftingRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.data.server.recipe.SmithingRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider;

public class AMRecipeProvider extends FabricRecipesProvider {
	public static Map<ItemVariant, TagOfferer> EQUIPMENT_OFFERERS = Map.of(
			ItemVariant.HELMET, AMRecipeProvider::offerHelmetRecipe,
			ItemVariant.CHESTPLATE, AMRecipeProvider::offerChestplateRecipe,
			ItemVariant.LEGGINGS, AMRecipeProvider::offerLeggingsRecipe,
			ItemVariant.BOOTS, AMRecipeProvider::offerBootsRecipe,
			ItemVariant.PICKAXE, AMRecipeProvider::offerPickaxeRecipe,
			ItemVariant.AXE, AMRecipeProvider::offerAxeRecipe,
			ItemVariant.SHOVEL, AMRecipeProvider::offerShovelRecipe,
			ItemVariant.SWORD, AMRecipeProvider::offerSwordRecipe,
			ItemVariant.HOE, AMRecipeProvider::offerHoeRecipe
	);

	public static Map<ItemVariant, TagOfferer> MISC_OFFERERS = Map.of(
			ItemVariant.GEAR, AMRecipeProvider::offerGearRecipe,
			ItemVariant.PLATE, AMRecipeProvider::offerPlateRecipe,
			ItemVariant.APPLE, AMRecipeProvider::offerMaterialAppleRecipe
	);

	private static final Map<BlockFamily.Variant, BasicOfferer> STONECUTTING_OFFERERS = Map.of(
			BlockFamily.Variant.STAIRS, RecipesProvider::offerStonecuttingRecipe,
			BlockFamily.Variant.SLAB, (exporter, output, input) -> offerStonecuttingRecipe(exporter, output, input, 2),
			BlockFamily.Variant.WALL, RecipesProvider::offerStonecuttingRecipe,
			BlockFamily.Variant.POLISHED, RecipesProvider::offerStonecuttingRecipe
	);

	public static final Map<BlockFamily, BlockFamily> VARIANT_FAMILIES = Map.of(
			AMBlockFamilies.POLISHED_ASTEROID_STONE, AMBlockFamilies.ASTEROID_STONE,
			AMBlockFamilies.ASTEROID_STONE_BRICK, AMBlockFamilies.SMOOTH_ASTEROID_STONE,
			AMBlockFamilies.POLISHED_METEOR_STONE, AMBlockFamilies.METEOR_STONE,
			AMBlockFamilies.METEOR_STONE_BRICK, AMBlockFamilies.SMOOTH_METEOR_STONE
	);

	public static final Map<Block, Block> SMOOTH_FROM_REGULAR = Map.of(
			AMBlocks.METEOR_STONE.get(), AMBlocks.SMOOTH_METEOR_STONE.get(),
			AMBlocks.ASTEROID_STONE.get(), AMBlocks.SMOOTH_ASTEROID_STONE.get()
	);

	public static final Map<Block, Block> BRICKS_FROM_SMOOTH = Map.of(
			AMBlocks.SMOOTH_METEOR_STONE.get(), AMBlocks.METEOR_STONE_BRICKS.get(),
			AMBlocks.SMOOTH_ASTEROID_STONE.get(), AMBlocks.ASTEROID_STONE_BRICKS.get()
	);

	public AMRecipeProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	public static String convertBetween(ItemConvertible to, String from) {
		return RecipesProvider.getRecipeName(to) + "_from_" + from;
	}

	public static String convertBetween(ItemConvertible to, Tag.Identified<Item> from) {
		return getRecipeName(to) + "_from_" + getRecipeName(from);
	}

	public static String getRecipeName(Tag.Identified<Item> tag) {
		return tag.getId().getPath();
	}

	public static void offerReversibleCompactingRecipesWithInputItemGroup(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible compacted) {
		offerReversibleCompactingRecipesWithInputItemGroup(exporter, input, compacted, getRecipeName(compacted));
	}

	public static void offerReversibleCompactingRecipesWithCompactedItemGroup(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible compacted) {
		offerReversibleCompactingRecipesWithInputItemGroup(exporter, input, compacted, getRecipeName(compacted));
	}

	public static void offerReversibleCompactingRecipesWithInputItemGroup(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible compacted, String from) {
		offerReversibleCompactingRecipesWithInputItemGroup(exporter, input, compacted, convertBetween(input, from), getRecipeName(input));
	}

	public static void offerReversibleCompactingRecipesWithCompactedItemGroup(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible compacted, String from) {
		offerReversibleCompactingRecipesWithCompactedItemGroup(exporter, input, compacted, convertBetween(compacted, from), getRecipeName(compacted));
	}

	public static void offerSmeltingAndBlasting(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output, float experience) {
		offerSmeltingAndBlasting(exporter, List.of(input), output, experience);
	}

	public static void offerSmeltingAndBlasting(Consumer<RecipeJsonProvider> exporter, List<ItemConvertible> inputs, ItemConvertible output, float experience) {
		offerSmelting(exporter, inputs, output, experience, 200, getRecipeName(output));
		offerBlasting(exporter, inputs, output, experience, 100, getRecipeName(output));
	}

	public static void offerSmeltingAndBlasting(Consumer<RecipeJsonProvider> exporter, Tag<Item> inputs, ItemConvertible output, float experience) {
		offerSmelting(exporter, new ArrayList<>(inputs.values()), output, experience, 200, getRecipeName(output));
		offerBlasting(exporter, new ArrayList<>(inputs.values()), output, experience, 100, getRecipeName(output));
	}

	public static void offerHelmetRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).pattern("XXX").pattern("X X"), input).offerTo(exporter);
	}

	public static void offerChestplateRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).pattern("X X").pattern("XXX").pattern("XXX"), input).offerTo(exporter);
	}

	public static void offerLeggingsRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).pattern("XXX").pattern("X X").pattern("X X"), input).offerTo(exporter);
	}

	public static void offerBootsRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).pattern("X X").pattern("X X"), input).offerTo(exporter);
	}

	public static void offerPickaxeRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("XXX").pattern(" S ").pattern(" S "), input).offerTo(exporter);
	}

	public static void offerAxeRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("XX").pattern("XS").pattern(" S"), input).offerTo(exporter);
	}

	public static void offerShovelRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("X").pattern("S").pattern("S"), input).offerTo(exporter);
	}

	public static void offerSwordRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("X").pattern("X").pattern("S"), input).offerTo(exporter);
	}

	public static void offerHoeRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("XX").pattern(" S").pattern(" S"), input).offerTo(exporter);
	}

	public static void offerGearRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output, 2).input('X', input).pattern(" X ").pattern("X X").pattern(" X "), input).offerTo(exporter);
	}

	public static void offerPlateRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).pattern("X").pattern("X"), input).offerTo(exporter);
	}

	public static void offerMaterialAppleRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapedRecipeJsonFactory.create(output).input('X', input).input('A', Items.APPLE).pattern("XXX").pattern("XAX").pattern("XXX"), input).offerTo(exporter);
	}

	public static void offer2x2CompactingRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		withCriterion(create2x2CompactingRecipe(input, output), input).offerTo(exporter);
	}

	public static void offer2x2CompactingRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(create2x2CompactingRecipe(input, output), input).offerTo(exporter);
	}
	
	public static ShapedRecipeJsonFactory create2x2CompactingRecipe(ItemConvertible input, ItemConvertible output) {
		return ShapedRecipeJsonFactory.create(output).input('#', input).pattern("##").pattern("##");
	}

	public static ShapedRecipeJsonFactory create2x2CompactingRecipe(Tag.Identified<Item> input, ItemConvertible output) {
		return ShapedRecipeJsonFactory.create(output).input('#', input).pattern("##").pattern("##");
	}

	public static void offerCompactingRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(createCompactingRecipe(input, output), input).offerTo(exporter);
	}

	public static ShapedRecipeJsonFactory createCompactingRecipe(ItemConvertible input, ItemConvertible output) {
		return ShapedRecipeJsonFactory.create(output).input('#', input).pattern("###").pattern("###").pattern("###");
	}

	public static ShapedRecipeJsonFactory createCompactingRecipe(Tag.Identified<Item> input, ItemConvertible output) {
		return ShapedRecipeJsonFactory.create(output).input('#', input).pattern("###").pattern("###").pattern("###");
	}
	
	public static void offerReverseCompactingRecipe(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		withCriterion(ShapelessRecipeJsonFactory.create(output, 9).input(input), input).offerTo(exporter);
	}

	public static void offerCompactingRecipeWithFullName(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		offerWithName(exporter, withCriterion(ShapedRecipeJsonFactory.create(output).input('#', input).pattern("###").pattern("###").pattern("###"), input), convertBetween(output, input));
	}

	public static void offerReverseCompactingRecipeWithFullName(Consumer<RecipeJsonProvider> exporter, Tag.Identified<Item> input, ItemConvertible output) {
		offerWithName(exporter, withCriterion(ShapelessRecipeJsonFactory.create(output, 9).input(input), input), convertBetween(output, input));
	}

	public static void offerSmoothingRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		withCriterion(CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(input), output, 0.1f, 200), input).offerTo(exporter);
	}

	public static void offerSmithingRecipe(Consumer<RecipeJsonProvider> exporter, Item input, Item addition, Item output) {
		SmithingRecipeJsonFactory.create(Ingredient.ofItems(input), Ingredient.ofItems(addition), output).criterion("has_" + getRecipeName(addition), RecipesProvider.conditionsFromItem(addition)).offerTo(exporter, RecipesProvider.getItemPath(output) + "_smithing");
	}
	
	public static CraftingRecipeJsonFactory withCriterion(CraftingRecipeJsonFactory factory, ItemConvertible input) {
		return factory.criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input));
	}

	public static CraftingRecipeJsonFactory withCriterion(CraftingRecipeJsonFactory factory, Tag.Identified<Item> input) {
		return factory.criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromTag(input));
	}

	public static void offerWithName(Consumer<RecipeJsonProvider> exporter, CraftingRecipeJsonFactory factory, String name) {
		factory.offerTo(exporter, AMCommon.id(name));
	}

	public static void generateFamily(Consumer<RecipeJsonProvider> exporter, BlockFamily family) {
		RecipesProvider.generateFamily(exporter, family);
		STONECUTTING_OFFERERS.forEach((variant, offerer) -> {
			if (family.getVariants().containsKey(variant))
				offerer.accept(exporter, family.getVariant(variant), family.getBaseBlock());
		});
	}

	@Override
	protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
		BlockFamilies.getFamilies().filter(AMBlockFamilies::isAstromineFamily).filter(BlockFamily::shouldGenerateRecipes).forEach(family -> generateFamily(exporter, family));

		VARIANT_FAMILIES.forEach((variantFamily, baseFamily) -> {
			STONECUTTING_OFFERERS.forEach((variant, offerer) -> {
				if (variantFamily.getVariants().containsKey(variant))
					offerer.accept(exporter, variantFamily.getVariant(variant), baseFamily.getBaseBlock());
			});
			Arrays.stream(BlockFamily.Variant.values()).forEach((variant) -> {
				if (variantFamily.getVariants().containsKey(variant) && baseFamily.getVariants().containsKey(variant)) {
					offerStonecuttingRecipe(exporter, variantFamily.getVariant(variant), baseFamily.getVariant(variant));
				}
			});
		});

		SMOOTH_FROM_REGULAR.forEach((regular, smooth) -> {
			offerSmoothingRecipe(exporter, regular, smooth);
		});

		BRICKS_FROM_SMOOTH.forEach((smooth, bricks) -> {
			offer2x2CompactingRecipe(exporter, smooth, bricks);
		});

		MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateRecipes).forEach((family) -> {
			if (family.shouldGenerateRecipe(BlockVariant.BLOCK)) {
				if(family.isBlock2x2()) {
					offer2x2CompactingRecipe(exporter, family.getBaseTag(), family.getVariant(BlockVariant.BLOCK));
				} else {
					offerCompactingRecipe(exporter, family.getBaseTag(), family.getVariant(BlockVariant.BLOCK));
					offerReverseCompactingRecipeWithFullName(exporter, family.getItemTag(BlockVariant.BLOCK), family.getBaseItem());
				}
			}
			if (family.shouldGenerateRecipe(ItemVariant.NUGGET, family.getBaseVariant())) {
				offerCompactingRecipeWithFullName(exporter, family.getTag(ItemVariant.NUGGET), family.getBaseItem());
				offerReverseCompactingRecipe(exporter, family.getBaseTag(), family.getVariant(ItemVariant.NUGGET));
			}
			if (family.shouldGenerateRecipe(ItemVariant.TINY_DUST, ItemVariant.DUST)) {
				offerCompactingRecipeWithFullName(exporter, family.getTag(ItemVariant.TINY_DUST), family.getVariant(ItemVariant.DUST));
				offerReverseCompactingRecipe(exporter, family.getTag(ItemVariant.DUST), family.getVariant(ItemVariant.TINY_DUST));
			}
			if (family.shouldGenerateRecipe(ItemVariant.RAW_ORE, BlockVariant.RAW_ORE_BLOCK)) {
				offerCompactingRecipe(exporter, family.getTag(ItemVariant.RAW_ORE), family.getVariant(BlockVariant.RAW_ORE_BLOCK));
				offerReverseCompactingRecipeWithFullName(exporter, family.getItemTag(BlockVariant.RAW_ORE_BLOCK), family.getVariant(ItemVariant.RAW_ORE));
			}
			if (family.hasAnyBlockVariants(AMDatagen.ORE_VARIANTS)) {
				offerSmeltingAndBlasting(exporter, family.getItemTag("ores"), family.getBaseItem(), family.getOreSmeltingExperience());
			}
			if (family.hasAnyItemVariants(AMDatagen.CLUSTER_VARIANTS)) {
				offerSmeltingAndBlasting(exporter, family.getItemTag("clusters"), family.getBaseItem(), family.getOreSmeltingExperience());
			}
			if (family.shouldGenerateRecipe(ItemVariant.RAW_ORE, family.getBaseVariant())) {
				offerSmeltingAndBlasting(exporter, family.getTag(ItemVariant.RAW_ORE), family.getBaseItem(), family.getOreSmeltingExperience());
			}
			if (!family.getType().equals(MaterialType.MISC) && !family.getType().equals(MaterialType.DUST)) {
				if (family.shouldGenerateRecipe(ItemVariant.DUST, family.getBaseVariant())) {
					offerSmeltingAndBlasting(exporter, family.getTag(ItemVariant.DUST), family.getBaseItem(), 0.0f);
				}
				if (family.shouldGenerateRecipe(ItemVariant.TINY_DUST, ItemVariant.NUGGET)) {
					offerSmeltingAndBlasting(exporter, family.getTag(ItemVariant.TINY_DUST), family.getVariant(ItemVariant.NUGGET), 0.0f);
				}
			}
			if (family.hasVariant(ItemVariant.NUGGET) && family.hasAnyItemVariants(AMDatagen.EQUIPMENT_VARIANTS)) {
				offerSmeltingAndBlasting(exporter, family.getItemTag("salvageables"), family.getVariant(ItemVariant.NUGGET), 0.1f);
			}
			if(family.usesSmithing()) {
				MaterialFamily smithingBase = family.getSmithingBase().orElse(MaterialFamilies.DIAMOND);
				AMDatagen.EQUIPMENT_VARIANTS.forEach((variant) -> {
					if (family.shouldGenerateRecipe(variant)) {
						offerSmithingRecipe(exporter, smithingBase.getVariant(variant), family.getBaseItem(), family.getVariant(variant));
					}
				});
			} else {
				EQUIPMENT_OFFERERS.forEach((variant, offerer) -> {
					if (family.shouldGenerateRecipe(variant)) {
						offerer.accept(exporter, family.getBaseTag(), family.getVariant(variant));
					}
				});
			}
			MISC_OFFERERS.forEach((variant, offerer) -> {
				if (family.shouldGenerateRecipe(variant)) {
					offerer.accept(exporter, family.getBaseTag(), family.getVariant(variant));
				}
			});
		});
	}

	@FunctionalInterface
	public interface BasicOfferer extends TriConsumer<Consumer<RecipeJsonProvider>, ItemConvertible, ItemConvertible> {
	}

	@FunctionalInterface
	public interface TagOfferer extends TriConsumer<Consumer<RecipeJsonProvider>, Tag.Identified<Item>, ItemConvertible> {
	}
}
