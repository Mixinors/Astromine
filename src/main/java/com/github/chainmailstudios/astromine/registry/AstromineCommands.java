package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.container.CraftingRecipeCreatorContainer;
import com.github.chainmailstudios.astromine.common.container.ElectricSmelterContainer;
import com.google.common.collect.Lists;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static net.minecraft.command.arguments.BlockPosArgumentType.blockPos;
import static net.minecraft.command.arguments.BlockPosArgumentType.getBlockPos;
import static net.minecraft.command.arguments.IdentifierArgumentType.getIdentifier;
import static net.minecraft.command.arguments.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
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
									return new CraftingRecipeCreatorContainer(syncId, playerInventory);
								}
							});

							return 1;
						}).build();

			dispatcher.getRoot().addChild(baseNode);

			baseNode.addChild(teleportNode);
		});
	}
}
