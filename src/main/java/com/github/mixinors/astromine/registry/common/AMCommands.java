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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.common.screen.handler.RecipeCreatorScreenHandler;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class AMCommands {
	public static void init() {
		CommandRegistrationEvent.EVENT.register((dispatcher, environment) -> {
			dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("recipe").then(LiteralArgumentBuilder.<ServerCommandSource>literal("creator").executes((context) -> {
				MenuRegistry.openExtendedMenu(context.getSource().getPlayer(), new ExtendedMenuProvider() {
					@Override
					public void saveExtraData(PacketByteBuf packetByteBuf) {
						
					}
					
					@Override
					public Text getDisplayName() {
						return new LiteralText("Recipe Creator");
					}
					
					@Override
					public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
						return new RecipeCreatorScreenHandler(syncId, player);
					}
				});
				
				return 1;
			})));
		});
	}
}
