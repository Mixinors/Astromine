package com.github.chainmailstudios.astromine.common.widget;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.registry.AstromineCommonPackets;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.ComponentType;
import spinnery.client.render.BaseRenderer;
import spinnery.widget.WButton;

import java.util.Collections;
import java.util.List;

public class WTransferTypeSelectorButton extends WButton {
	private BlockEntityTransferComponent component;

	private ComponentType<?> type;

	private Direction direction;

	private BlockPos blockPos;

	private Identifier texture = null;

	private Vec3i mouse = Vec3i.ZERO;

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

	public ComponentType<?> getType() {
		return type;
	}

	public <W extends WTransferTypeSelectorButton> W setType(ComponentType<?> type) {
		this.type = type;
		return (W) this;
	}

	public Identifier getTexture() {
		return texture;
	}

	public <W extends WTransferTypeSelectorButton> W setTexture(Identifier texture) {
		this.texture = texture;
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
	public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {
		if (isFocused()) {
			component.get(type).set(direction, component.get(type).get(direction, Direction.NORTH).next());

			setTexture(component.get(type).get(direction, Direction.NORTH).texture());

			if (getInterface().getContainer().getWorld().isClient()) {
				PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());

				buffer.writeBlockPos(getBlockPos());
				buffer.writeIdentifier(DefaultedBlockEntity.TRANSFER_UPDATE_PACKET);

				buffer.writeIdentifier(type.getId());
				buffer.writeEnumConstant(direction);
				buffer.writeEnumConstant(component.get(type).get(direction, Direction.NORTH));

				ClientSidePacketRegistry.INSTANCE.sendToServer(AstromineCommonPackets.BLOCK_ENTITY_UPDATE_PACKET, buffer);
			}
		}

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseMoved(float mouseX, float mouseY) {
		mouse = new Vec3i(mouseX, mouseY, 0);
		super.onMouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}

	@Override
	public List<Text> getTooltip() {
		return Collections.singletonList(
				getDirection() == Direction.NORTH ? new TranslatableText("text.astromine.front")
						: getDirection() == Direction.SOUTH ? new TranslatableText("text.astromine.back")
								: getDirection() == Direction.WEST ? new TranslatableText("text.astromine.right")
										: getDirection() == Direction.EAST ? new TranslatableText("text.astromine.left")
												: new TranslatableText("text.astromine." + getDirection().getName()));
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (texture == null) setTexture(component.get(type).get(direction, Direction.NORTH).texture());

		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), getZ(), getWidth(), getHeight(), getTexture());
	}
}
