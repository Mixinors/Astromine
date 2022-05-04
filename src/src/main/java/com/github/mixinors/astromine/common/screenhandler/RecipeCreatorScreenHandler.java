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

package com.github.mixinors.astromine.common.screenhandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.github.mixinors.astromine.common.inventory.BaseInventory;
import com.github.mixinors.astromine.common.util.WordUtils;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;
import dev.vini2003.hammer.gui.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.common.util.SlotUtils;
import dev.vini2003.hammer.gui.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.gui.common.widget.panel.PanelWidget;
import dev.vini2003.hammer.gui.common.widget.slot.SlotWidget;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

public class RecipeCreatorScreenHandler extends BaseScreenHandler {
	public static final Inventory[] craftingInventories = new Inventory[] { BaseInventory.of(10), BaseInventory.of(10) };

	public Inventory getInventory() {
		return getClient() ? craftingInventories[0] : craftingInventories[1];
	}

	public RecipeCreatorScreenHandler(int syncId, @NotNull PlayerEntity player) {
		super(AMScreenHandlers.RECIPE_CREATOR.get(), syncId, player);
	}

	@Override
	public void initialize(int width, int height) {
		final List<String> TYPES = new ArrayList<>() {
			{
				add("nugget");
				add("wire");
				add("ingot");
				add("dust");
				add("tiny_dust");
				add("plate");
				add("gear");
			}
		};

        final Map<String, String> TAGS = new HashMap<>() {
			{
				Registry.ITEM.forEach((item) -> {
					var id = Registry.ITEM.getId(item);
			
					TYPES.forEach((type) -> {
						if (id.getPath().contains(type)) {
							put(id.toString(), "c:" + WordUtils.pluralize(id.getPath()));
						}
					});
				});
			}
		};
		var panel = new PanelWidget();
		panel.setPosition( Position.of(width / 2 - 88.5, height / 2 - 92));
		panel.setSize( Size.of(93 + 84, 100 + 84));

		add(panel);

		SlotUtils.addPlayerInventory(Position.of(panel.getX() + 7, panel.getY() + 7 + 9 + 18 + 18 + 18 + 7 + 18 + 7), Size.of(18, 18), (WidgetCollection) this, getPlayer().getInventory());
		
		var inputSlots = Lists.newArrayList(SlotUtils.addArray(Position.of(panel.getX() + 7, panel.getY() + 7 + 9), Size.of(18, 18), panel, 0, 3, 3, getInventory()));
		
		var outputSlot = new SlotWidget(9, getInventory());
		outputSlot.setPosition(Position.of(panel.getX() + 7 + 18 * 3 + 7, panel.getY() + 7 + 18 + 9));
		outputSlot.setSize(Size.of(18, 18));

		panel.add(outputSlot);
		
		var saveButton = new ButtonWidget();
		saveButton.setPosition(Position.of(panel.getX() + 7, panel.getY() + 7 + 14 + 18 * 3));
		saveButton.setSize(Size.of(18 * 3, 18));
		saveButton.setLabel(new LiteralText("Save"));
		saveButton.setClickAction(() -> {
			Map<Integer, String> table = new HashMap<>();
			Map<String, Integer> inverseTable = new HashMap<>();
			Map<Integer, String> grid = new HashMap<>();

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

				if (TAGS.containsKey(name)) {
					entry.addProperty("tag", TAGS.get(name));
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
			
			var outputFile = new File("generated/" + outputName.replace(":", "_").replace("/", "_").replace("am_", "") + ".json");

			try {
				outputFile.createNewFile();

				FileUtils.write(outputFile, recipeJson.toString(), Charsets.UTF_8);
			} catch (IOException exception) {
				exception.printStackTrace();
			}

			return null;
		});

		panel.add(saveButton);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
