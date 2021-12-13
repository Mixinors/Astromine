package com.github.mixinors.astromine.datagen.provider;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.github.mixinors.astromine.datagen.family.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.MaterialFamily.BlockVariant;
import com.github.mixinors.astromine.datagen.family.MaterialFamily.ItemVariant;
import com.github.mixinors.astromine.datagen.family.MaterialFamily.MaterialType;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import org.apache.logging.log4j.util.TriConsumer;

import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider;

public class AMRecipeProvider extends FabricRecipesProvider {
	public static Set<BlockVariant> ORE_VARIANTS = Set.of(
			BlockVariant.ORE,
			BlockVariant.DEEPSLATE_ORE,
			BlockVariant.NETHER_ORE,
			BlockVariant.METEOR_ORE,
			BlockVariant.ASTEROID_ORE
	);

	public static Set<ItemVariant> ORE_ITEM_VARIANTS = Set.of(
			ItemVariant.RAW_ORE,
			ItemVariant.METEOR_CLUSTER,
			ItemVariant.ASTEROID_CLUSTER
	);

	public static Set<ItemVariant> EQUIPMENT = Set.of(
			ItemVariant.HELMET,
			ItemVariant.CHESTPLATE,
			ItemVariant.LEGGINGS,
			ItemVariant.BOOTS,
			ItemVariant.PICKAXE,
			ItemVariant.AXE,
			ItemVariant.SHOVEL,
			ItemVariant.SWORD,
			ItemVariant.HOE
	);

	public static Map<ItemVariant, BasicOfferer> EQUIPMENT_OFFERERS = Map.of(
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

	public static Map<ItemVariant, BasicOfferer> MISC_OFFERERS = Map.of(
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

	public static void offerHelmetRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).pattern("XXX").pattern("X X").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerChestplateRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).pattern("X X").pattern("XXX").pattern("XXX").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerLeggingsRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).pattern("XXX").pattern("X X").pattern("X X").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerBootsRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).pattern("X X").pattern("X X").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerPickaxeRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("XXX").pattern(" S ").pattern(" S ").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerAxeRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("XX").pattern("XS").pattern(" S").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerShovelRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("X").pattern("S").pattern("S").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerSwordRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("X").pattern("X").pattern("S").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerHoeRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).input('S', Items.STICK).pattern("XX").pattern(" S").pattern(" S").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerGearRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output, 2).input('X', input).pattern(" X ").pattern("X X").pattern(" X ").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerPlateRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).pattern("X").pattern("X").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offer2x2CompactingRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('#', input).pattern("##").pattern("##").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerSmoothingRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(input), output, 0.1f, 200).criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
	}

	public static void offerMaterialAppleRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
		ShapedRecipeJsonFactory.create(output).input('X', input).input('A', Items.APPLE).pattern("XXX").pattern("XAX").pattern("XXX").criterion("has_" + getRecipeName(input), RecipesProvider.conditionsFromItem(input)).offerTo(exporter);
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
				offerReversibleCompactingRecipesWithInputItemGroup(exporter, family.getBaseItem(), family.getVariant(BlockVariant.BLOCK));
			}
			if (family.shouldGenerateRecipe(ItemVariant.NUGGET, family.getBaseVariant())) {
				offerReversibleCompactingRecipesWithCompactedItemGroup(exporter, family.getVariant(ItemVariant.NUGGET), family.getBaseItem(), family.getType() == MaterialType.GEM ? "fragments" : "nuggets");
			}
			if (family.shouldGenerateRecipe(ItemVariant.TINY_DUST, ItemVariant.DUST)) {
				offerReversibleCompactingRecipesWithCompactedItemGroup(exporter, family.getVariant(ItemVariant.TINY_DUST), family.getVariant(ItemVariant.DUST), "tiny_dust");
			}
			if (family.shouldGenerateRecipe(ItemVariant.RAW_ORE, BlockVariant.RAW_ORE_BLOCK)) {
				offerReversibleCompactingRecipesWithInputItemGroup(exporter, family.getVariant(ItemVariant.RAW_ORE), family.getVariant(BlockVariant.RAW_ORE_BLOCK));
			}
			ORE_VARIANTS.forEach((variant -> {
				if (family.shouldGenerateRecipe(variant, family.getBaseVariant())) {
					offerSmeltingAndBlasting(exporter, family.getVariant(variant), family.getBaseItem(), family.getOreSmeltingExperience());
				}
			}));
			ORE_ITEM_VARIANTS.forEach((variant -> {
				if (family.shouldGenerateRecipe(variant, family.getBaseVariant())) {
					offerSmeltingAndBlasting(exporter, family.getVariant(variant), family.getBaseItem(), family.getOreSmeltingExperience());
				}
			}));
			if (!family.getType().equals(MaterialType.MISC) && !family.getType().equals(MaterialType.DUST)) {
				if (family.shouldGenerateRecipe(ItemVariant.DUST, family.getBaseVariant())) {
					offerSmeltingAndBlasting(exporter, family.getVariant(ItemVariant.DUST), family.getBaseItem(), 0.0f);
				}
				if (family.shouldGenerateRecipe(ItemVariant.TINY_DUST, ItemVariant.NUGGET)) {
					offerSmeltingAndBlasting(exporter, family.getVariant(ItemVariant.TINY_DUST), family.getVariant(ItemVariant.NUGGET), 0.0f);
				}
			}
			EQUIPMENT.forEach((variant) -> {
				if (family.shouldGenerateRecipe(variant, ItemVariant.NUGGET)) {
					offerSmeltingAndBlasting(exporter, family.getVariant(variant), family.getVariant(ItemVariant.NUGGET), 0.1f);
				}
			});
			EQUIPMENT_OFFERERS.forEach((variant, offerer) -> {
				if (family.shouldGenerateRecipe(variant)) {
					offerer.accept(exporter, family.getBaseItem(), family.getVariant(variant));
				}
			});
			MISC_OFFERERS.forEach((variant, offerer) -> {
				if (family.shouldGenerateRecipe(variant)) {
					offerer.accept(exporter, family.getBaseItem(), family.getVariant(variant));
				}
			});
			if (family.shouldGenerateRecipe(BlockVariant.BLOCK_2x2)) {
				offer2x2CompactingRecipe(exporter, family.getBaseItem(), family.getVariant(BlockVariant.BLOCK_2x2));
			}
		});
	}

	@FunctionalInterface
	public interface BasicOfferer extends TriConsumer<Consumer<RecipeJsonProvider>, ItemConvertible, ItemConvertible> {}
}
