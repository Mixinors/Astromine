package com.github.chainmailstudios.astromine.registry;

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
import com.mojang.brigadier.tree.LiteralCommandNode;

import com.github.chainmailstudios.astromine.common.screenhandler.CraftingRecipeCreatorScreenHandler;

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
