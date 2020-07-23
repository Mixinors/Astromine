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
package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.screenhandler.CraftingRecipeCreatorScreenHandler;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import static net.minecraft.server.command.CommandManager.literal;

public class AstromineCommands {
	public static void initialize() {
		CommandRegistry.INSTANCE.register(false, dispatcher -> {
			LiteralCommandNode<ServerCommandSource> baseNode =
					literal("create").requires(source -> source.hasPermissionLevel(4)).build();

			LiteralCommandNode<ServerCommandSource> teleportNode =
					literal("crafting_shaped")
							.executes(context -> {
								context.getSource().getPlayer().openHandledScreen(new ExtendedScreenHandlerFactory() {
									@Override
									public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buffer) {
									}

									@Override
									public Text getDisplayName() {
										return new TranslatableText("screen.astromine.crafting_shaped");
									}

									@Override
									public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
										return new CraftingRecipeCreatorScreenHandler(syncId, playerInventory);
									}
								});

								return 1;
							}).build();

			dispatcher.getRoot().addChild(baseNode);

			baseNode.addChild(teleportNode);
		});
	}
}
