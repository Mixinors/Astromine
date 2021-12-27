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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.github.mixinors.astromine.common.util.NetworkingUtils;
import com.github.mixinors.astromine.registry.common.AMNetworks;
import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.common.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.client.BaseRenderer;
import com.github.mixinors.astromine.common.util.MirrorUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;

public class StorageSidingWidget extends Widget {
	private ExtendedBlockEntity blockEntity;
	
	private StorageSiding siding;
	
	private StorageType type;
	
	private Direction direction;
	
	private Direction rotation;
	
	public static final Identifier TEXTURE_INSERT= AMCommon.id("textures/widget/insert.png");
	public static final Identifier TEXTURE_EXTRACT = AMCommon.id("textures/widget/extract.png");
	public static final Identifier TEXTURE_INSERT_EXTRACT = AMCommon.id("textures/widget/insert_extract.png");
	public static final Identifier TEXTURE_NONE = AMCommon.id("textures/widget/none.png");
	
	private String getSideName() {
		if (type == StorageType.ITEM) {
			var sidings = blockEntity.getItemStorage().getSidings();
			
			return switch (sidings[direction.ordinal()]) {
				case INSERT -> "insert";
				case EXTRACT -> "extract";
				case INSERT_EXTRACT -> "insert_extract";
				case NONE -> "none";
			};
		}
		
		if (type == StorageType.FLUID) {
			var sidings = blockEntity.getFluidStorage().getSidings();
			
			return switch (sidings[direction.ordinal()]) {
				case INSERT -> "insert";
				case EXTRACT -> "extract";
				case INSERT_EXTRACT -> "insert_extract";
				case NONE -> "none";
			};
		}
		
		return null;
	}

	private Identifier getTexture() {
		if (type == StorageType.ITEM) {
			var sidings = blockEntity.getItemStorage().getSidings();
			
			return switch (sidings[direction.ordinal()]) {
				case INSERT -> TEXTURE_INSERT;
				case EXTRACT -> TEXTURE_EXTRACT;
				case INSERT_EXTRACT ->  TEXTURE_INSERT_EXTRACT;
				case NONE -> TEXTURE_NONE;
			};
		}
		
		if (type == StorageType.FLUID) {
			var sidings = blockEntity.getFluidStorage().getSidings();
			
			return switch (sidings[direction.ordinal()]) {
				case INSERT -> TEXTURE_INSERT;
				case EXTRACT -> TEXTURE_EXTRACT;
				case INSERT_EXTRACT ->  TEXTURE_INSERT_EXTRACT;
				case NONE -> TEXTURE_NONE;
			};
		}
		
		return null;
	}
	
	@Override
	public void onMouseClicked(float mouseX, float mouseY, int button) {
		super.onMouseClicked(mouseX, mouseY, button);
		
		if (!getHidden() && getFocused() && getHandled().getClient()) {
			var sidings = (StorageSiding[]) null;
			
			if (type == StorageType.ITEM) {
				sidings = blockEntity.getItemStorage().getSidings();
			}
			
			if (type == StorageType.FLUID) {
				sidings = blockEntity.getFluidStorage().getSidings();
			}

			StorageSiding next;
			
			if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
				next = sidings[direction.ordinal()].next();
			} else if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
				next = sidings[direction.ordinal()].previous();
			} else {
				return;
			}

			var buf = NetworkingUtils.ofStorageSiding(next, type, direction, blockEntity.getPos());
			
			NetworkManager.sendToServer(AMNetworks.STORAGE_SIDING_UPDATE, buf);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public @NotNull List<Text> getTooltip() {
		var offset = MirrorUtils.rotate(direction, rotation);
		
		return Arrays.asList(new TranslatableText("text.astromine.siding." + offset.getName()), new TranslatableText("text.astromine.siding." + getSideName()));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider, float delta) {
		if (getHidden()) {
			return;
		}

		BaseRenderer.drawTexturedQuad(matrices, provider, getPosition().getX(), getPosition().getY(), getSize().getWidth(), getSize().getHeight(), getTexture());
	}
	
	public ExtendedBlockEntity getBlockEntity() {
		return blockEntity;
	}
	
	public void setBlockEntity(ExtendedBlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}
	
	public StorageSiding getSiding() {
		return siding;
	}
	
	public void setSiding(StorageSiding siding) {
		this.siding = siding;
	}
	
	public StorageType getType() {
		return type;
	}
	
	public void setType(StorageType type) {
		this.type = type;
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
}
