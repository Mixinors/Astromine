/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.common.inventory.BaseInventory;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.utilities.Slots;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import com.github.vini2003.blade.common.widget.base.PanelWidget;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A {@link BaseScreenHandler}
 * configured as an interface for {@link CraftingRecipe}
 * creation as {@link JsonElement}.
 */
public class RecipeCreatorScreenHandler extends BaseScreenHandler {
	public static final Inventory[] craftingInventories = new Inventory[] { BaseInventory.of(10), BaseInventory.of(10) };

	public Inventory getInventory() {
		return getClient() ? craftingInventories[0] : craftingInventories[1];
	}

	public RecipeCreatorScreenHandler(int syncId, @NotNull PlayerEntity player) {
		super((ScreenHandlerType<? extends ScreenHandler>) AMScreenHandlers.RECIPE_CREATOR.get(), syncId, player);
	}

	@Override
	public void initialize(int width, int height) {
		var types = List.of("nugget", "wire", "ingot", "dust", "tiny_dust", "plate", "gear");

        var tags = Registry.ITEM.getEntries()
		        .stream()
		        .filter(entry -> types.stream().anyMatch(type -> entry.getKey().getValue().getPath().contains(type)))
		        .collect(
				        Collectors.toMap(
				        		entry -> entry.getKey().getValue().toString(),
						        entry -> String.format("c:%ss", entry.getKey().getValue().getPath())
				        )
		        );
        
        var panel = new PanelWidget();
		panel.setPosition(Position.of(width / 2 - 88.5, height / 2 - 92));
		panel.setSize(Size.of(93 + 84, 100 + 84));

		addWidget(panel);

		Slots.addPlayerInventory(Position.of(panel.getX() + 7, panel.getY() + 7 + 9 + 18 + 18 + 18 + 7 + 18 + 7), Size.of(18, 18), this, getPlayer().inventory);

		var inputSlots = Slots.addArray(Position.of(panel.getX() + 7, panel.getY() + 7 + 9), Size.of(18, 18), panel, 0, 3, 3, getInventory());

		var outputSlot = new SlotWidget(9, getInventory());
		outputSlot.setPosition(Position.of(panel.getX() + 7 + 18 * 3 + 7, panel.getY() + 7 + 18 + 9));
		outputSlot.setSize(Size.of(18, 18));

		panel.addWidget(outputSlot);

		var saveButton = new ButtonWidget();
		saveButton.setPosition(Position.of(panel.getX() + 7, panel.getY() + 7 + 14 + 18 * 3));
		saveButton.setSize(Size.of(18 * 3, 18));
		saveButton.setLabel(new LiteralText("Save"));
		saveButton.setClickAction(() -> {
			var table = new HashMap<Integer, String>();
			var inverseTable = new HashMap<String, Integer>();
			var grid = new HashMap<Integer, String>();

			inputSlots.forEach((it) -> {
				var slot = it.getSlot();

				var stack = it.getBackendSlot().getStack();

				if (!stack.isEmpty()) {
					var name = Registry.ITEM.getId(stack.getItem()).toString();

					if (inverseTable.containsKey(name)) {
						grid.put(slot, Integer.toString(inverseTable.get(name)));
					} else {
						grid.put(slot, Integer.toString(slot));
						table.put(slot, name);
						inverseTable.put(name, slot);
					}
				} else {
					grid.put(slot, " ");
				}
			});

			var outputStack = outputSlot.getBackendSlot().getStack();

			var outputName = Registry.ITEM.getId(outputStack.getItem()).toString();

			var recipeJson = new JsonObject();

			recipeJson.addProperty("type", "minecraft:crafting_shaped");

			var patternJson = new JsonArray();

			patternJson.add(grid.get(0) + grid.get(1) + grid.get(2));
			patternJson.add(grid.get(3) + grid.get(4) + grid.get(5));
			patternJson.add(grid.get(6) + grid.get(7) + grid.get(8));

			recipeJson.add("pattern", patternJson);

			var keyJson = new JsonObject();

			table.forEach((slot, name) -> {
				var entry = new JsonObject();

				if (tags.containsKey(name)) {
					entry.addProperty("tag", tags.get(name));
				} else {
					entry.addProperty("item", name);
				}

				keyJson.add(Integer.toString(slot), entry);
			});

			recipeJson.add("key", keyJson);

			var resultJson = new JsonObject();

			resultJson.addProperty("item", outputName);
            resultJson.addProperty("count", outputStack.getCount());

			recipeJson.add("result", resultJson);

			var generatedFile = new File("generated");

			generatedFile.mkdir();

			var outputFile = new File("generated/" + outputName.replace(":", "_").replace("/", "_").replace("astromine_", "") + ".json");

			try {
				outputFile.createNewFile();

				FileUtils.write(outputFile, recipeJson.toString(), Charsets.UTF_8);
			} catch (IOException exception) {
				exception.printStackTrace();
			}

			return null;
		});

		panel.addWidget(saveButton);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
