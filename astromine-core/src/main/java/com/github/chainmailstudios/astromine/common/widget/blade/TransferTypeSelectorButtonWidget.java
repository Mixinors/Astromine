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

package com.github.chainmailstudios.astromine.common.widget.blade;

import com.github.chainmailstudios.astromine.common.component.block.entity.TransferComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.utilities.MirrorUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineCommonPackets;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A button for the selection of
 * {@link TransferType}s connected to a machine.
 */
public class TransferTypeSelectorButtonWidget extends AbstractWidget {
	private TransferComponent component;

	private ComponentKey<?> type;

	private Direction direction;

	private Direction rotation;

	private BlockPos blockPos;

	private boolean wasClicked = false;

	/** Returns the name of this widget's side's {@link TransferType}. */
	private String getSideName() {
		return component.get(type).get(direction).name().toLowerCase(Locale.ROOT);
	}

	/** Returns the {@link ResourceLocation} of this widget's texture, based on its {@link TransferType}. */
	private ResourceLocation getTexture() {
		return component.get(type).get(direction).texture();
	}

	/** Returns this widget's {@link TransferComponent}. */
	public TransferComponent getComponent() {
		return component;
	}

	/** Sets this widget's {@link TransferComponent} to the specified one. */
	public void setComponent(TransferComponent component) {
		this.component = component;
	}

	/** Retrieve this widget's {@link Direction}. */
	public Direction getDirection() {
		return direction;
	}

	/** Sets this widget's {@link Direction} to the specified one. */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/** Retrieve this widget's rotation's {@link Direction}. */
	public Direction getRotation() {
		return rotation;
	}

	/** Set this widget's rotation's {@link Direction} to the specified one. */
	public void setRotation(Direction rotation) {
		this.rotation = rotation;

	}

	/** Retrieve this widget's {@link ComponentKey}. */
	public ComponentKey<?> getType() {
		return type;
	}

	/** Set this widget's {@link ComponentKey} to the specified one. */
	public void setType(ComponentKey<?> type) {
		this.type = type;

	}

	/** Retrieve this widget's {@link BlockPos}. */
	 public BlockPos getBlockPos() {
		return blockPos;
	}

	/** Set this widget's {@link BlockPos} to the specified one. */
	public void setBlockPos(BlockPos blockPos) {
		this.blockPos = blockPos;

	}

	/** Override mouse click behavior to... what was I doing? It works, I'm not touching it. */
	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		if (getFocused() && getHandler().getClient()) {
			wasClicked = true;
		}
	}

	/** Override mouse click behavior to update this widget's
	 * {@link TransferType} on the server.
	 */
	@Override
	public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {
		if (getFocused() && !getHidden() && wasClicked) {
			if (getHandler().getClient()) {
				FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

				buffer.writeBlockPos(getBlockPos());
				buffer.writeResourceLocation(ComponentBlockEntity.TRANSFER_UPDATE_PACKET);

				buffer.writeResourceLocation(type.getId());
				buffer.writeEnum(direction);
				buffer.writeEnum(component.get(type).get(direction).next());

				ClientSidePacketRegistry.INSTANCE.sendToServer(AstromineCommonPackets.BLOCK_ENTITY_UPDATE_PACKET, buffer);
			}
		}
		wasClicked = false;

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	/** Returns this widget's tooltip. */
	@Environment(EnvType.CLIENT)
	@Override
	public @NotNull List<Component> getTooltip() {
		Direction offset = MirrorUtilities.rotate(direction, rotation);
		return Arrays.asList(new TranslatableComponent("text.astromine.siding." + offset.getName()), new TranslatableComponent("text.astromine.siding." + getSideName()));
	}

	/** Renders this widget. */
	@Environment(EnvType.CLIENT)
	@Override
	public void drawWidget(@NotNull PoseStack matrices, @NotNull MultiBufferSource provider) {
		if (getHidden())
			return;

		BaseRenderer.drawTexturedQuad(matrices, provider, getPosition().getX(), getPosition().getY(), getSize().getWidth(), getSize().getHeight(), getTexture());
	}
}
