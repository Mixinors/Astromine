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

package com.github.mixinors.astromine.client.screen;

import com.github.mixinors.astromine.client.gui.widget.ButtonWidget;
import com.github.mixinors.astromine.client.gui.widget.PanelWidget;
import com.github.mixinors.astromine.client.gui.widget.SlotBackgroundsWidget;
import com.github.mixinors.astromine.client.gui.widget.WidgetGroup;
import com.github.mixinors.astromine.client.screen.base.CustomForegroundBaseHandledScreen;
import com.github.mixinors.astromine.common.screen.handler.RecipeCreatorScreenHandler;
import com.github.mixinors.astromine.common.util.WordUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;

/**
 * A container screen used by {@link RecipeCreatorScreenHandler}.
 */
public class RecipeCreatorHandledScreen extends CustomForegroundBaseHandledScreen<RecipeCreatorScreenHandler> {
	/** Instantiates a {@link RecipeCreatorHandledScreen}. */
	public RecipeCreatorHandledScreen(@NotNull RecipeCreatorScreenHandler handler, @NotNull Inventory inventory, @NotNull Component title) {
		super(handler, inventory, title);
		imageWidth = 177;
		imageHeight = 184;
		inventoryLabelX = -Integer.MAX_VALUE;
		inventoryLabelY = -Integer.MAX_VALUE;
		titleLabelX = -Integer.MAX_VALUE;
		titleLabelY = -Integer.MAX_VALUE;
	}
	
	@Override
	protected void rebuildWidgets() {
		root = new WidgetGroup(0, 0, imageWidth, imageHeight);
		root.add(new PanelWidget(0, 0, imageWidth, imageHeight));
		root.add(new SlotBackgroundsWidget(menu, ($) -> true));
		root.add(new ButtonWidget(7, 75, 54, 18, () -> Component.literal("Save"), () -> true, this::saveRecipe));
	}
	
	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		if (root != null) {
			root.render(graphics, widgetContext(mouseX, mouseY));
		}
	}
	
	private void saveRecipe() {
		var inventory = menu.getInventory();
		var table = new HashMap<Integer, String>();
		var inverseTable = new HashMap<String, Integer>();
		var grid = new HashMap<Integer, String>();
		var tags = recipeTags();
		
		for (var slot = 0; slot < 9; ++slot) {
			var stack = inventory.getItem(slot);
			
			if (stack.isEmpty()) {
				grid.put(slot, " ");
				continue;
			}
			
			var name = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
			
			if (inverseTable.containsKey(name)) {
				grid.put(slot, Integer.toString(inverseTable.get(name)));
			} else {
				grid.put(slot, Integer.toString(slot));
				table.put(slot, name);
				inverseTable.put(name, slot);
			}
		}
		
		var outputStack = inventory.getItem(9);
		
		if (outputStack.isEmpty()) {
			return;
		}
		
		var outputName = BuiltInRegistries.ITEM.getKey(outputStack.getItem()).toString();
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
		resultJson.addProperty("id", outputName);
		resultJson.addProperty("count", outputStack.getCount());
		recipeJson.add("result", resultJson);
		
		try {
			Files.createDirectories(Path.of("generated"));
			Files.writeString(Path.of("generated", outputName.replace(":", "_").replace("/", "_").replace("am_", "") + ".json"), recipeJson.toString(), StandardCharsets.UTF_8);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private static HashMap<String, String> recipeTags() {
		var types = Set.of("nugget", "wire", "ingot", "dust", "tiny_dust", "plate", "gear");
		var tags = new HashMap<String, String>();
		
		BuiltInRegistries.ITEM.forEach((item) -> {
			var id = BuiltInRegistries.ITEM.getKey(item);
			
			for (var type : types) {
				if (id.getPath().contains(type)) {
					tags.put(id.toString(), "c:" + WordUtils.pluralize(id.getPath()));
				}
			}
		});
		
		return tags;
	}
}
