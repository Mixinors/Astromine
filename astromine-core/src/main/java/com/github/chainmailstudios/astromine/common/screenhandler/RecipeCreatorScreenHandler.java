package com.github.chainmailstudios.astromine.common.screenhandler;

import com.github.chainmailstudios.astromine.common.inventory.BaseInventory;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.utilities.Slots;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import com.github.vini2003.blade.common.widget.base.PanelWidget;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link BaseScreenHandler}
 * configured as an interface for {@link CraftingRecipe}
 * creation as {@link JsonElement}.
 */
public class RecipeCreatorScreenHandler extends BaseScreenHandler {
    public RecipeCreatorScreenHandler(int syncId, @NotNull PlayerEntity player) {
        super(AstromineScreenHandlers.RECIPE_CREATOR, syncId, player);
    }

    @Override
    public void initialize(int width, int height) {
        final List<String> TYPES = new ArrayList<String>() {
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

        final Map<String, String> TAGS = new HashMap<String, String>() {
            {
                Registry.ITEM.forEach((item) -> {
                    Identifier id = Registry.ITEM.getId(item);

                    TYPES.forEach((type) -> {
                        if (id.getPath().contains(type)) {
                            put(id.toString(), "c:" + id.getPath() + "s");
                        }
                    });
                });
            }
        };
        PanelWidget panel = new PanelWidget();
        panel.setPosition(Position.of(width / 2 - 40, height / 2 - 40));
        panel.setSize(Size.of(93, 100));

        addWidget(panel);

        BaseInventory inventory = BaseInventory.of(10);

        List<SlotWidget> inputSlots = Lists.newArrayList(Slots.addArray(Position.of(panel.getX() + 7, panel.getY() + 7 + 9), Size.of(18, 18), panel, 0, 3, 3, inventory));

        SlotWidget outputSlot = new SlotWidget(9, inventory);
        outputSlot.setPosition(Position.of(panel.getX() + 7 + 18 * 3 + 7, panel.getY() + 7 + 18 + 9));
        outputSlot.setSize(Size.of(18, 18));

        panel.addWidget(outputSlot);

        ButtonWidget saveButton = new ButtonWidget();
        saveButton.setPosition(Position.of(panel.getX() + 7, panel.getY() + 7 + 14 + 18 * 3));
        saveButton.setSize(Size.of(18 * 3, 18));
        saveButton.setLabel(new LiteralText("Save"));
        saveButton.setClickAction(() -> {
            Map<Integer, String> table = new HashMap<>();
            Map<String, Integer> inverseTable = new HashMap<>();
            Map<Integer, String> grid = new HashMap<>();

            inputSlots.forEach((it) -> {
                int slot = it.getSlot();

                ItemStack stack = it.getBackendSlot().getStack();

                if (!stack.isEmpty()) {
                    String name = Registry.ITEM.getId(stack.getItem()).toString();

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

            ItemStack outputStack = outputSlot.getBackendSlot().getStack();

            String outputName = Registry.ITEM.getId(outputStack.getItem()).toString();

            JsonObject recipeJson = new JsonObject();

            recipeJson.addProperty("type", "minecraft:crafting_shaped");

            JsonArray patternJson = new JsonArray();

            patternJson.add(grid.get(0) + grid.get(1) + grid.get(2));
            patternJson.add(grid.get(3) + grid.get(4) + grid.get(5));
            patternJson.add(grid.get(6) + grid.get(7) + grid.get(8));

            recipeJson.add("pattern", patternJson);

            JsonObject keyJson = new JsonObject();

            table.forEach((slot, name) -> {
                JsonObject entry = new JsonObject();

                TagManager tagManager = getPlayer().getEntityWorld().getTagManager();

                if (TAGS.containsKey(name)) {
                    entry.addProperty("tag", TAGS.get(name));
                } else {
                    entry.addProperty("item", name);
                }

                keyJson.add(Integer.toString(slot), entry);
            });

            recipeJson.add("key", keyJson);

            JsonObject resultJson = new JsonObject();

            resultJson.addProperty("item", outputName);
            resultJson.addProperty("count", outputStack.getCount());

            recipeJson.add("result", resultJson);

            File generatedFile = new File("generated");

            generatedFile.mkdir();

            File outputFile = new File("generated/" + outputName.replace(":", "_").replace("/", "_").replace("astromine_", "") + ".json");

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