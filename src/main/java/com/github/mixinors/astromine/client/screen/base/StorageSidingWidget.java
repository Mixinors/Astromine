/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.screen.base;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.gui.widget.Widget;
import com.github.mixinors.astromine.client.gui.widget.WidgetContext;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.github.mixinors.astromine.common.util.MirrorUtils;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class StorageSidingWidget extends Widget {
	private static final int SIZE = 18;
	private static final ResourceLocation SIDING_INSERT = AMCommon.id("textures/widget/insert.png");
	private static final ResourceLocation SIDING_EXTRACT = AMCommon.id("textures/widget/extract.png");
	private static final ResourceLocation SIDING_INSERT_EXTRACT = AMCommon.id("textures/widget/insert_extract.png");
	private static final ResourceLocation SIDING_NONE = AMCommon.id("textures/widget/none.png");
	
	private final ExtendedBlockEntity blockEntity;
	private final StorageType storageType;
	private final Direction direction;
	private final Direction facing;
	private final Supplier<BlockPos> blockPos;
	
	public StorageSidingWidget(int x, int y, ExtendedBlockEntity blockEntity, StorageType storageType, Direction direction, Direction facing, Supplier<BlockPos> blockPos) {
		super(x, y, SIZE, SIZE);
		this.blockEntity = blockEntity;
		this.storageType = storageType;
		this.direction = direction;
		this.facing = facing;
		this.blockPos = blockPos;
	}
	
	@Override
	public void render(GuiGraphics graphics, WidgetContext context) {
		var siding = siding();
		
		if (siding != null) {
			graphics.blit(sidingTexture(siding), context.left() + x, context.top() + y, 0.0F, 0.0F, width, height, width, height);
		}
	}
	
	@Override
	public void renderTooltip(GuiGraphics graphics, WidgetContext context) {
		if (!contains(context, context.mouseX(), context.mouseY())) {
			return;
		}
		
		var siding = siding();
		
		if (siding != null) {
			graphics.renderComponentTooltip(context.font(), List.of(directionName(MirrorUtils.rotate(direction, facing)), sidingName(siding)), context.mouseX(), context.mouseY());
		}
	}
	
	@Override
	public boolean mouseClicked(WidgetContext context, double mouseX, double mouseY, int button) {
		if (!contains(context, mouseX, mouseY)) {
			return false;
		}
		
		var sidings = sidings();
		
		if (sidings == null) {
			return true;
		}
		
		var next = switch (button) {
			case 0 -> sidings[direction.ordinal()].next();
			case 1 -> sidings[direction.ordinal()].previous();
			default -> null;
		};
		
		if (next != null) {
			sidings[direction.ordinal()] = next;
			PacketDistributor.sendToServer(new AMNetworking.StorageSidingUpdatePayload(next, storageType, direction, blockPos.get()));
		}
		
		return true;
	}
	
	private StorageSiding siding() {
		var sidings = sidings();
		return sidings == null ? null : sidings[direction.ordinal()];
	}
	
	private StorageSiding[] sidings() {
		return switch (storageType) {
			case ITEM -> blockEntity.getItemStorage() == null ? null : blockEntity.getItemStorage().getSidings();
			case FLUID -> blockEntity.getFluidStorage() == null ? null : blockEntity.getFluidStorage().getSidings();
			case ENERGY -> null;
		};
	}
	
	private static ResourceLocation sidingTexture(StorageSiding siding) {
		return switch (siding) {
			case INSERT -> SIDING_INSERT;
			case EXTRACT -> SIDING_EXTRACT;
			case INSERT_EXTRACT -> SIDING_INSERT_EXTRACT;
			case NONE -> SIDING_NONE;
		};
	}
	
	private static Component directionName(Direction direction) {
		return Component.translatable("text.astromine.siding." + direction.getName());
	}
	
	private static Component sidingName(StorageSiding siding) {
		return switch (siding) {
			case INSERT -> Component.translatable("text.astromine.siding.insert");
			case EXTRACT -> Component.translatable("text.astromine.siding.extract");
			case INSERT_EXTRACT -> Component.translatable("text.astromine.siding.insert_extract");
			case NONE -> Component.translatable("text.astromine.siding.none");
		};
	}
}
