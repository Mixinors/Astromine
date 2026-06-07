/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.screen.base;

import com.github.mixinors.astromine.client.gui.GuiRenderers;
import com.github.mixinors.astromine.client.gui.widget.Widget;
import com.github.mixinors.astromine.client.gui.widget.WidgetContext;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityMenuLayout;
import com.github.mixinors.astromine.common.transfer.RedstoneType;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;

public class RedstoneControlWidget extends Widget {
	private final ExtendedBlockEntity blockEntity;
	private final Supplier<BlockPos> blockPos;
	
	public RedstoneControlWidget(int x, int y, ExtendedBlockEntity blockEntity, Supplier<BlockPos> blockPos) {
		super(x, y, (int) ExtendedBlockEntityMenuLayout.REDSTONE_WIDTH, (int) ExtendedBlockEntityMenuLayout.REDSTONE_HEIGHT);
		this.blockEntity = blockEntity;
		this.blockPos = blockPos;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		GuiRenderers.panel(graphics, context.left() + x, context.top() + y, width, height);
		graphics.renderItem(redstoneIcon(blockEntity.getRedstoneType()), context.left() + x + 2, context.top() + y + 2);
	}
	
	@Override
	public void renderTooltip(GuiGraphics graphics, WidgetContext context) {
		if (contains(context, context.mouseX(), context.mouseY())) {
			graphics.renderComponentTooltip(context.font(), List.of(redstoneTooltip(blockEntity.getRedstoneType())), context.mouseX(), context.mouseY());
		}
	}
	
	@Override
	public boolean mouseClicked(WidgetContext context, double mouseX, double mouseY, int button) {
		if (!contains(context, mouseX, mouseY)) {
			return false;
		}
		
		var next = switch (button) {
			case 0 -> blockEntity.getRedstoneType().next();
			case 1 -> blockEntity.getRedstoneType().previous();
			default -> null;
		};
		
		if (next == null) {
			return true;
		}
		
		blockEntity.setRedstoneControl(next);
		PacketDistributor.sendToServer(new AMNetworking.RedstoneTypeUpdatePayload(next, blockPos.get()));
		return true;
	}
	
	private static ItemStack redstoneIcon(RedstoneType type) {
		return switch (type) {
			case WORK_WHEN_ON -> Items.REDSTONE.getDefaultInstance();
			case WORK_WHEN_OFF -> Items.GUNPOWDER.getDefaultInstance();
			case WORK_ALWAYS -> Items.GLOWSTONE_DUST.getDefaultInstance();
		};
	}
	
	private static Component redstoneTooltip(RedstoneType type) {
		return switch (type) {
			case WORK_WHEN_ON -> Component.translatable("tooltip.astromine.work_when_on");
			case WORK_WHEN_OFF -> Component.translatable("tooltip.astromine.work_when_off");
			case WORK_ALWAYS -> Component.translatable("tooltip.astromine.work_always");
		};
	}
}
