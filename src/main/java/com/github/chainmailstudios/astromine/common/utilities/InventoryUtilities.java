package com.github.chainmailstudios.astromine.common.utilities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import spinnery.common.inventory.BaseInventory;

import java.util.*;

public class InventoryUtilities extends spinnery.common.utility.InventoryUtilities {
	@Environment(EnvType.CLIENT)
	public static List<ItemStack> toList(Ingredient ingredient) {
		return new ArrayList<ItemStack>() {{
			for (ItemStack stack : ingredient.getMatchingStacksClient()) {
				add(stack);
			}
		}};
	}

	public static List<ItemStack> toList(Inventory inventory) {
		List<ItemStack> stackSet = new ArrayList();
		for (int i = 0; i < inventory.size(); ++i) {
			stackSet.add(inventory.getStack(i));
		}
		return stackSet;
	}

	public static List<ItemStack> toList(ItemStack[] array) {
		return Arrays.asList(array);
	}

	public static List<ItemStack> toListNonEmpty(Inventory inventory) {
		List<ItemStack> stackSet = new ArrayList<>();
		for (ItemStack stack : toList(inventory)) {
			if (stack.isEmpty()) continue;
			stackSet.add(stack);
		}
		return stackSet;
	}

	public static BaseInventory rangedOf(Inventory origin, int[] slots) {
		BaseInventory recipeInventory = new BaseInventory(slots.length);
		int k = 0;
		for (int i : slots) {
			recipeInventory.setStack(k, origin.getStack(i));
		}
		return recipeInventory;
	}

	public static BaseInventory singleOf(Inventory origin, int slot) {
		return rangedOf(origin, new int[]{slot});
	}

	/**
	 * Sorts an inventory based on stack names and count.
	 *
	 * @param inventory the specified inventory.
	 */
	public static void sort(Inventory inventory) {
		TreeMap<String, ArrayList<ItemStack>> byType = new TreeMap<>();

		for (int i = 0; i < inventory.size(); ++i) {
			ItemStack stack = inventory.getStack(i);

			if (!byType.containsKey(stack.getName().asString()) && !stack.isEmpty()) {
				byType.put(stack.getName().asString(), new ArrayList<>());
			}

			if (!stack.isEmpty()) {
				byType.get(stack.getName().asString()).add(stack.copy());
			}

			inventory.setStack(i, ItemStack.EMPTY);
		}

		int i = 0;
		for (Map.Entry<String, ArrayList<ItemStack>> type : byType.entrySet()) {
			ArrayList<ItemStack> stacks = type.getValue();
			boolean finished = false;
			for (int k = 0; k < 128 && !finished; ++k) {
				for (int l = 0; l < stacks.size(); ++l) {
					boolean moved = false;
					if (l < stacks.size() - 1) {
						ItemStack current = stacks.get(l);
						ItemStack next = stacks.get(l + 1);

						if (current.getCount() < next.getCount()) {
							stacks.set(l + 1, current);
							stacks.set(l, next);
							moved = true;
						}
					}
					if (l == stacks.size() - 1 && !moved) {
						finished = true;
					}
				}
			}

			for (ItemStack stack : stacks) {
				inventory.setStack(i, stack);
				++i;
			}
		}
	}
}
