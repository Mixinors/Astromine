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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.utilities.MirrorUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineCommonPackets;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TransferTypeSelectorButtonWidget extends AbstractWidget {
	private BlockEntityTransferComponent component;

	private ComponentKey<?> type;

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

	public void setComponent(BlockEntityTransferComponent component) {
		this.component = component;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;

	}

	public Direction getRotation() {
		return rotation;
	}

	public void setRotation(Direction rotation) {
		this.rotation = rotation;

	}

	public ComponentKey<?> getType() {
		return type;
	}

	public void setType(ComponentKey<?> type) {
		this.type = type;

	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public void setBlockPos(BlockPos blockPos) {
		this.blockPos = blockPos;

	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		super.onMouseClicked(mouseX, mouseY, mouseButton);
		if (getFocused() && getHandler().getClient()) {
			wasClicked = true;
		}
	}

	@Override
	public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {
		if (getFocused() && !getHidden() && wasClicked) {
			if (getHandler().getClient()) {
				PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());

				buffer.writeBlockPos(getBlockPos());
				buffer.writeIdentifier(ComponentBlockEntity.TRANSFER_UPDATE_PACKET);

				buffer.writeIdentifier(type.getId());
				buffer.writeEnumConstant(direction);
				buffer.writeEnumConstant(component.get(type).get(direction).next());

				ClientSidePacketRegistry.INSTANCE.sendToServer(AstromineCommonPackets.BLOCK_ENTITY_UPDATE_PACKET, buffer);
			}
		}
		wasClicked = false;

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public @NotNull List<Text> getTooltip() {
		Direction offset = MirrorUtilities.rotate(direction, rotation);
		return Arrays.asList(new TranslatableText("text.astromine.siding." + offset.getName()), new TranslatableText("text.astromine.siding." + getSideName()));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider) {
		if (getHidden())
			return;

		BaseRenderer.drawTexturedQuad(matrices, provider, getPosition().getX(), getPosition().getY(), getSize().getWidth(), getSize().getHeight(), getTexture());
	}
}
