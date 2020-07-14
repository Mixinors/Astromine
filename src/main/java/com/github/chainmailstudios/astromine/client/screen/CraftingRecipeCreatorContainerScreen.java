package com.github.chainmailstudios.astromine.client.screen;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.common.container.CraftingRecipeCreatorContainer;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WButton;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Comparator;
import java.util.List;

public class CraftingRecipeCreatorContainerScreen extends BaseContainerScreen<CraftingRecipeCreatorContainer> {
	private static final int INPUT = 1;
	private static final int OUTPUT = 2;

	public CraftingRecipeCreatorContainerScreen(Text name, CraftingRecipeCreatorContainer linkedContainer, PlayerEntity player) {
		super(name, linkedContainer, player);

		WInterface mainInterface = getInterface();

		WPanel mainPanel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(176, 175));
		mainPanel.center();
		mainPanel.setOnAlign(WAbstractWidget::center);

		WSlot.addPlayerInventory(Position.of(mainPanel, 7, 68, 0), Size.of(18, 18), mainPanel);

		List<WSlot> craftingSlots = Lists.newArrayList(WSlot.addArray(Position.of(mainPanel, 7, 7, 0), Size.of(18, 18), mainPanel, 0, INPUT, 3, 3));

		craftingSlots.sort(Comparator.comparingInt(WSlot::getSlotNumber));

		WSlot resultSlot = mainPanel.createChild(WSlot::new, Position.of(mainPanel, 7 + 54 + 27, 7 + 13.5f, 0), Size.of(27, 27)).setSlotNumber(0).setInventoryNumber(OUTPUT);

		mainPanel.createChild(WButton::new,
			Position.of(
				mainPanel,
				mainPanel.getWidth() - 7 - 54,
				mainPanel.getHeight() - 7 - 18,
				0
			), Size.of(54, 18))
			.setLabel("Save")
			.setOnMouseClicked(((widget, mouseX, mouseY, mouseButton) -> {
				JsonObject output = new JsonObject();

				JsonArray pattern = new JsonArray();
				pattern.add("012");

				pattern.add("345");

				pattern.add("678");

				JsonObject key = new JsonObject();

				int[] i = { 0 };

				craftingSlots.forEach(slot -> {
					JsonObject stack = new JsonObject();
					stack.addProperty("item", Registry.ITEM.getId(slot.getStack().getItem()).toString());

					key.add(String.valueOf(i[0]), stack);

					++i[0];
				});

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
