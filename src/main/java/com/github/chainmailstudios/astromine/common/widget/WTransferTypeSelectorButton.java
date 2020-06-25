package com.github.chainmailstudios.astromine.common.widget;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import spinnery.client.render.BaseRenderer;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WButton;

public class WTransferTypeSelectorButton extends WButton {
	private BlockEntityTransferComponent component;

	private ComponentType<?> type;

	private Direction direction;

	private Identifier texture = null;

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

	@Override
	public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {
		if (isFocused()) {
			component.get(type).set(direction, component.get(type).get(direction).next());

			setTexture(component.get(type).get(direction).texture());
		}

		super.onMouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (texture == null) setTexture(component.get(type).get(direction).texture());
		BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), getZ(), getWidth(), getHeight(), getTexture());
	}
}
