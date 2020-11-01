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

package com.github.chainmailstudios.astromine.technologies;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.chainmailstudios.astromine.technologies.common.recipe.TrituratingRecipe;
import com.github.chainmailstudios.astromine.technologies.registry.*;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kotlin.text.Charsets;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.FileUtils;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.fluid.RebornFluid;
import techreborn.init.ModRecipes;
import techreborn.items.DynamicCellItem;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class AstromineTechnologiesCommon extends AstromineCommon {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, ignored) -> {
			dispatcher.register(
					LiteralArgumentBuilder.<ServerCommandSource>literal("dump").executes((context) -> {
						PlayerEntity source = context.getSource().getPlayer();

						source.getEntityWorld()
								.getRecipeManager()
								.getAllOfType(ModRecipes.GRINDER)
								.values()
								.stream()
								.forEach(it -> {
									RebornRecipe recipe = ((RebornRecipe) it);

									JsonObject trituratorRecipe = new JsonObject();

									trituratorRecipe.addProperty("type", "astromine:triturating");

									JsonObject trituratorInput = new JsonObject();

									List<RebornIngredient> inputList = Lists.newArrayList(recipe.getRebornIngredients());

									TagManager manager = source.getEntityWorld().getTagManager();

									Tag<Item>[] inputTag = new Tag[]{ null };

									inputList.get(0).getPreviewStacks().forEach((stack) -> {
										if (inputTag[0] == null) {
											Collection<Identifier> tags = manager.getItems().getTagsFor(stack.getItem());

											if (!tags.isEmpty()) {
												inputTag[0] = manager.getItems().getTag(tags.iterator().next());
											}
										}
									});

									boolean isTag = inputTag[0] != null;

									if (isTag) {
										trituratorInput.addProperty("tag", manager.getItems().getTagId(inputTag[0]).toString());
									} else {
										trituratorInput.addProperty("item", Registry.ITEM.getId(inputList.get(0).getPreviewStacks().iterator().next().getItem()).toString());
										trituratorInput.addProperty("count", inputList.get(0).getPreviewStacks().iterator().next().getCount());
									}

									trituratorRecipe.add("input", trituratorInput);

									JsonObject trituratorOutput = new JsonObject();

									ItemStack output = recipe.getOutputs().iterator().next();

									trituratorOutput.addProperty("item", Registry.ITEM.getId(output.getItem()).toString());
									trituratorOutput.addProperty("count", output.getCount());

									trituratorRecipe.add("output", trituratorOutput);

									trituratorRecipe.addProperty("energy", 340);
									trituratorRecipe.addProperty("time", 90);

									File dumpFile = new File("dump");

									dumpFile.mkdir();

									File file = new File("dump/" + recipe.getId().toString().replace(":", "_").replace("/", "_").replace("techreborn_grinder_", "") + "_from_triturating.json");

									JsonObject trituratorObject = new JsonObject();

									JsonObject trituratorCondition = new JsonObject();

									trituratorCondition.addProperty("modid", "techreborn");

									trituratorObject.add("recipe", trituratorRecipe);


									trituratorObject.add("condition", trituratorCondition);


									try {
										file.createNewFile();

										FileUtils.write(file, trituratorObject.toString(), Charsets.UTF_8);
									} catch (IOException e) {
										e.printStackTrace();
									}
								});

						source.getEntityWorld()
								.getRecipeManager()
								.getAllOfType(ModRecipes.COMPRESSOR)
								.values()
								.stream()
								.forEach(it -> {
									RebornRecipe recipe = ((RebornRecipe) it);

									JsonObject presserRecipe = new JsonObject();

									presserRecipe.addProperty("type", "astromine:pressing");

									JsonObject presserInput = new JsonObject();

									List<RebornIngredient> inputList = Lists.newArrayList(recipe.getRebornIngredients());

									TagManager manager = source.getEntityWorld().getTagManager();

									Tag<Item>[] inputTag = new Tag[]{ null };

									inputList.get(0).getPreviewStacks().forEach((stack) -> {
										if (inputTag[0] == null) {
											Collection<Identifier> tags = manager.getItems().getTagsFor(stack.getItem());

											if (!tags.isEmpty()) {
												inputTag[0] = manager.getItems().getTag(tags.iterator().next());
											}
										}
									});

									boolean isTag = inputTag[0] != null;

									if (isTag) {
										presserInput.addProperty("tag", manager.getItems().getTagId(inputTag[0]).toString());
									} else {
										presserInput.addProperty("item", Registry.ITEM.getId(inputList.get(0).getPreviewStacks().iterator().next().getItem()).toString());
										presserInput.addProperty("count", inputList.get(0).getPreviewStacks().iterator().next().getCount());
									}

									presserRecipe.add("input", presserInput);

									JsonObject presserOutput = new JsonObject();

									ItemStack output = recipe.getOutputs().iterator().next();

									presserOutput.addProperty("item", Registry.ITEM.getId(output.getItem()).toString());
									presserOutput.addProperty("count", output.getCount());

									presserRecipe.add("output", presserOutput);

									presserRecipe.addProperty("energy", 340);
									presserRecipe.addProperty("time", 60);

									File dumpFile = new File("dump");

									dumpFile.mkdir();

									File file = new File("dump/" + recipe.getId().toString().replace(":", "_").replace("/", "_").replace("techreborn_compressor_", "") + "_from_pressing.json");

									JsonObject presserObject = new JsonObject();

									JsonObject presserCondition = new JsonObject();

									presserCondition.addProperty("modid", "techreborn");

									presserObject.add("recipe", presserRecipe);


									presserObject.add("condition", presserCondition);


									try {
										file.createNewFile();

										FileUtils.write(file, presserObject.toString(), Charsets.UTF_8);
									} catch (IOException e) {
										e.printStackTrace();
									}
								});


						Registry.FLUID.forEach((it) -> {
							if (it instanceof RebornFluid && ((RebornFluid) it).getFlowing() != it) {

								File dumpFile = new File("dumptags");

								dumpFile.mkdir();

								File file = new File("dumptags/" + Registry.FLUID.getId(it).toString().replace(":", "_").replace("/", "_").replace("techreborn_", "") + ".json");

								JsonObject tag = new JsonObject();

								tag.addProperty("replace", false);

								JsonArray values = new JsonArray();

								JsonObject data = new JsonObject();

								data.addProperty("id", Registry.FLUID.getId(it).toString());
								data.addProperty("required", false);

								values.add(data);

								tag.add("values", values);

								try {
									file.createNewFile();

									FileUtils.write(file, tag.toString(), Charsets.UTF_8);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});

						return 1;
					})
			);
		});

		AstromineTechnologiesBlockEntityTypes.initialize();
		AstromineTechnologiesBlocks.initialize();
		AstromineTechnologiesItems.initialize();
		AstromineTechnologiesItemGroups.initialize();
		AstromineTechnologiesRecipeSerializers.initialize();
		AstromineTechnologiesScreenHandlers.initialize();
		AstromineTechnologiesToolMaterials.initialize();
		AstromineTechnologiesCommonCallbacks.initialize();
		AstromineTechnologiesEntityTypes.initialize();
		AstromineTechnologiesRecipeSerializers.initialize();
		AstromineTechnologiesCommonPackets.initialize();
	}
}
