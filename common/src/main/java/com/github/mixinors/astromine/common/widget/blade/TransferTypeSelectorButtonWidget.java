/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.widget.blade;

import com.github.mixinors.astromine.common.component.block.entity.TransferComponent;
import com.github.mixinors.astromine.registry.common.AMNetworks;
import me.shedaniel.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.client.BaseRenderer;
import com.github.mixinors.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.mixinors.astromine.common.block.transfer.TransferType;
import com.github.mixinors.astromine.common.util.MirrorUtils;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

/**
 * A button for the selection of
 * {@link TransferType}s connected to a machine.
 */
public class TransferTypeSelectorButtonWidget extends AbstractWidget {
	private TransferComponent component;

	private Identifier id;

	private Direction direction;

	private Direction rotation;

	private BlockPos blockPos;

	private boolean wasClicked = false;

	/** Returns the name of this widget's side's {@link TransferType}. */
	private String getSideName() {
		return component.get(id).get(direction).name().toLowerCase(Locale.ROOT);
	}

	/** Returns the {@link Identifier} of this widget's texture, based on its {@link TransferType}. */
	private Identifier getTexture() {
		return component.get(id).get(direction).texture();
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

	/** Retrieve this widget's component {@link Identifier}. */
	public Identifier getId() {
		return id;
	}

	/** Set this widget's component {@link Identifier} to the specified one. */
	public void setId(Identifier type) {
		this.id = type;

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
				var buf = new PacketByteBuf(Unpooled.buffer());

				buf.writeBlockPos(getBlockPos());
				buf.writeIdentifier(ComponentBlockEntity.TRANSFER_UPDATE_PACKET);

				buf.writeIdentifier(id);
				buf.writeEnumConstant(direction);
				buf.writeEnumConstant(component.get(id).get(direction).next());
				
				NetworkManager.sendToServer(AMNetworks.BLOCK_ENTITY_UPDATE_PACKET, buf);
			}
		}
		
		wasClicked = false;

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	/** Returns this widget's tooltip. */
	@Environment(EnvType.CLIENT)
	@Override
	public @NotNull List<Text> getTooltip() {
		return List.of(
				new TranslatableText("text.astromine.siding." + MirrorUtils.rotate(direction, rotation).getName()),
				new TranslatableText("text.astromine.siding." + getSideName())
		);
	}

	/** Renders this widget. */
	@Environment(EnvType.CLIENT)
	@Override
	public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider) {
		if (getHidden())
			return;

		BaseRenderer.drawTexturedQuad(matrices, provider, getPosition().getX(), getPosition().getY(), getSize().getWidth(), getSize().getHeight(), getTexture());
	}
}
