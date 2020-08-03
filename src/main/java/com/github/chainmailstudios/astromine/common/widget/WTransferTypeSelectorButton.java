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

package com.github.chainmailstudios.astromine.common.widget;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.utilities.MirrorUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineCommonPackets;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.ComponentType;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import spinnery.client.render.BaseRenderer;
import spinnery.widget.WButton;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class WTransferTypeSelectorButton extends WButton {
	private BlockEntityTransferComponent component;

	private ComponentType<?> type;

	private Direction direction;

	private Direction rotation;

	private BlockPos blockPos;

	private boolean wasClicked = false;

	private String getSideName() {
		return component.get(type).get(direction).name().toLowerCase(Locale.ROOT);
	}

	private Identifier getTexture() {
		return component.get(type).get(direction).texture();
	}

	public BlockEntityTransferComponent getComponent() {
		return component;
	}

	public <W extends WTransferTypeSelectorButton> W setComponent(BlockEntityTransferComponent component) {
		this.component = component;
		return (W) this;
	}

	public Direction getDirection() {
		return direction;
	}

	public <W extends WTransferTypeSelectorButton> W setDirection(Direction direction) {
		this.direction = direction;
		return (W) this;
	}

	public Direction getRotation() {
		return rotation;
	}

	public <W extends WTransferTypeSelectorButton> W setRotation(Direction rotation) {
		this.rotation = rotation;
		return (W) this;
	}

	public ComponentType<?> getType() {
		return type;
	}

	public <W extends WTransferTypeSelectorButton> W setType(ComponentType<?> type) {
		this.type = type;
		return (W) this;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public <W extends WTransferTypeSelectorButton> W setBlockPos(BlockPos blockPos) {
		this.blockPos = blockPos;
		return (W) this;
	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		if (isFocused()) {
			wasClicked = true;
		}
	}

	@Override
	public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {
		if (isFocused() && wasClicked) {
			component.get(type).set(direction, component.get(type).get(direction).next());

			if (getInterface().getHandler().getWorld().isClient()) {
				PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());

				buffer.writeBlockPos(getBlockPos());
				buffer.writeIdentifier(DefaultedBlockEntity.TRANSFER_UPDATE_PACKET);

				buffer.writeIdentifier(type.getId());
				buffer.writeEnumConstant(direction);
				buffer.writeEnumConstant(component.get(type).get(direction));

				ClientSidePacketRegistry.INSTANCE.sendToServer(AstromineCommonPackets.BLOCK_ENTITY_UPDATE_PACKET, buffer);
			}
		}
		wasClicked = false;

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}

	@Override
	public List<Text> getTooltip() {
		Direction offset = MirrorUtilities.rotate(direction, rotation);
		return Arrays.asList(new TranslatableText("text.astromine.siding." + offset.getName()), new TranslatableText("text.astromine.siding." + getSideName()));
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), getZ(), getWidth(), getHeight(), getTexture());
	}
}
