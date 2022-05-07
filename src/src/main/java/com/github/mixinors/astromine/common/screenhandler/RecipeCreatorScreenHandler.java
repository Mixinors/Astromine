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
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.util.SlotUtils;
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
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
	public boolean isClient() {
		return getClient();
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
		panel.setPosition(new Position(width / 2.0F - 88.5F, height / 2.0F - 92F));
		panel.setSize(new Size(93.0F + 84.0F, 100.0F + 84.0F));

		add(panel);
		
		var player = getPlayer();

		SlotUtils.addPlayerInventory(new Position(panel.getX() + 7.0F, panel.getY() + 7.0F + 9.0F + 18.0F + 18.0F + 18.0F + 7.0F + 18.0F + 7.0F), new Size(18.0F, 18.0F), (BaseWidgetCollection) this, player.getInventory());
		
		var inputSlots = Lists.newArrayList(SlotUtils.addArray(new Position(panel.getX() + 7.0F, panel.getY() + 7.0F + 9.0F), new Size(18.0F, 18.0F), panel, getInventory(), 3, 3, 0));
		
		var outputSlot = new SlotWidget(9, getInventory());
		outputSlot.setPosition(new Position(panel.getX() + 7.0F + 18.0F * 3.0F + 7.0F, panel.getY() + 7.0F + 18.0F + 9.0F));
		outputSlot.setSize(new Size(18.0F, 18.0F));

		panel.add(outputSlot);
		
		var saveButton = new ButtonWidget();
		saveButton.setPosition(new Position(panel.getX() + 7.0F, panel.getY() + 7.0F + 14.0F + 18.0F * 3.0F));
		saveButton.setSize(new Size(18.0F * 3.0F, 18.0F));
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
