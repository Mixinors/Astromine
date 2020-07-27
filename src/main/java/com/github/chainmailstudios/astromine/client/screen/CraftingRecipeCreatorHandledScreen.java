/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.common.screenhandler.CraftingRecipeCreatorScreenHandler;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import spinnery.client.screen.BaseHandledScreen;
import spinnery.widget.*;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Comparator;
import java.util.List;

public class CraftingRecipeCreatorHandledScreen extends BaseHandledScreen<CraftingRecipeCreatorScreenHandler> {
	private static final int INPUT = 1;
	private static final int OUTPUT = 2;

	public CraftingRecipeCreatorHandledScreen(Text name, CraftingRecipeCreatorScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		WInterface mainInterface = getInterface();

		WPanel mainPanel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(176, 175));
		mainPanel.center();
		mainPanel.setOnAlign(WAbstractWidget::center);

		WSlot.addPlayerInventory(Position.of(mainPanel, 7, 68, 0), Size.of(18, 18), mainPanel);

		List<WSlot> craftingSlots = Lists.newArrayList(WSlot.addArray(Position.of(mainPanel, 7, 7, 0), Size.of(18, 18), mainPanel, 0, INPUT, 3, 3));

		craftingSlots.sort(Comparator.comparingInt(WSlot::getSlotNumber));

		WSlot resultSlot = mainPanel.createChild(WSlot::new, Position.of(mainPanel, 7 + 54 + 27, 7 + 13.5f, 0), Size.of(27, 27)).setSlotNumber(0).setInventoryNumber(OUTPUT);

		mainPanel.createChild(WButton::new, Position.of(mainPanel, mainPanel.getWidth() - 7 - 54, mainPanel.getHeight() - 7 - 18, 0), Size.of(54, 18)).setLabel("Save").setOnMouseClicked(((widget, mouseX, mouseY, mouseButton) -> {
			JsonObject output = new JsonObject();

			String patternString = "0123456789";

			JsonArray pattern = new JsonArray();

			JsonObject key = new JsonObject();

			for (WSlot slot : craftingSlots) {
				if (!slot.getStack().isEmpty()) {
					String item = Registry.ITEM.getId(slot.getStack().getItem()).toString();
					JsonObject stack = new JsonObject();
					stack.addProperty("item", item);

					boolean skip = false;

					for (char c : "012345678".toCharArray()) {
						if (key.has(String.valueOf(c)) && key.get(String.valueOf(c)).getAsJsonObject().get("item").getAsString().equals(item)) {
							patternString = patternString.replace(String.valueOf(slot.getSlotNumber()), String.valueOf(c));
							skip = true;
						}
					}

					if (!skip) {
						key.add(String.valueOf(slot.getSlotNumber()), stack);
					}
				} else {
					patternString = patternString.replace(String.valueOf(slot.getSlotNumber()), " ");
				}
			}

			pattern.add(patternString.substring(0, 3));
			pattern.add(patternString.substring(3, 6));
			pattern.add(patternString.substring(6, 9));

			JsonObject result = new JsonObject();
			result.addProperty("item", Registry.ITEM.getId(resultSlot.getStack().getItem()).toString());
			result.addProperty("count", resultSlot.getStack().getCount());

			output.addProperty("type", "minecraft:crafting_shaped");
			output.add("pattern", pattern);
			output.add("key", key);
			output.add("result", result);

			try {
				File rootDirectory = FabricLoader.getInstance().getGameDirectory();

				File outputDirectory = new File(rootDirectory.getPath().replace("\\.", "") + "/output/");

				outputDirectory.mkdirs();

				File outputFile = new File(outputDirectory.getPath() + "/" + Registry.ITEM.getId(resultSlot.getStack().getItem()).getPath() + ".json");

				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

				writer.write(output.toString());

				writer.close();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}));

	}
}
