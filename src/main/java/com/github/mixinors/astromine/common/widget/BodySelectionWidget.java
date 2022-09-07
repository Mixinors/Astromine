package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.util.DrawingUtil;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.registry.client.AMRenderLayers;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class BodySelectionWidget extends Widget {
	private static final ImageTexture GO_BUTTON_TEXTURE_UP = new ImageTexture(AMCommon.id("textures/widget/large_square_button_go_up_clear.png"));
	private static final ImageTexture GO_BUTTON_TEXTURE_DOWN = new ImageTexture(AMCommon.id("textures/widget/large_square_button_go_down_clear.png"));
	
	private Body body;
	
	private boolean selected = false;
	
	private Consumer<Body> selectListener = ($) -> {};
	
	public BodySelectionWidget() {
		this.body = null;
	}
	
	public Body getBody() {
		return body;
	}
	
	public void setBody(Body body) {
		this.body = body;
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		super.onMouseClicked(event);
		
		if (isFocused()) {
			if (event.button() == GLFW.GLFW_MOUSE_BUTTON_1) {
				if (!selected) {
					selected = true;
					
					selectListener.accept(body);
				}
			}
		}
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		if (body == null) return;
		
		var client = InstanceUtil.getClient();
		
		var textRenderer = client.textRenderer;
		
		var window = client.getWindow();
		
		// setPosition to bottom center of window
		setPosition(window.getScaledWidth() / 2.0F - getWidth() / 2.0F, window.getScaledHeight() - getHeight());
		
		var bodyWidth = (getWidth() * 0.15F);
		var bodyHeight = (getWidth() * 0.15F);
		
		var bodyX = getX();
		var bodyY = getY();
		
		var informationWidth = (getWidth() * 0.7F);
		var informationHeight = getHeight();
		
		var informationX = bodyX + bodyWidth;
		var informationY = getY();
		
		var buttonWidth = (getWidth() * 0.1F);
		var buttonHeight = (getWidth() * 0.1F);
		
		var buttonX = informationX + informationWidth;
		var buttonY = getY();
		
		var bodyName = body.name();
		var bodyDescription = body.description();
		var bodyDescriptionLines = textRenderer.wrapLines(bodyDescription, (int) informationWidth);
		
		var textHeight = TextUtil.getHeight(bodyName) + 4.0F + (TextUtil.getHeight(LiteralText.EMPTY) * bodyDescriptionLines.size());
		
		bodyY += bodyHeight / 2.0F;
		bodyX += bodyWidth / 2.0F;
		
		bodyY += (getHeight() - bodyHeight) / 2.0F;
		
		buttonY += (getHeight() - buttonHeight) / 2.0F;
		
		informationY += (getHeight() - textHeight) / 2.0F - 6.0F;
		
		// Translate to widget's button position.
		matrices.push(); // 5
		matrices.translate(buttonX, buttonY, 100.0F);
		
		// Draw button.
		if (!selected) {
			GO_BUTTON_TEXTURE_UP.draw(matrices, provider, 0.0F, 0.0F, buttonWidth, buttonHeight);
		} else {
			GO_BUTTON_TEXTURE_DOWN.draw(matrices, provider, 0.0F, 0.0F, buttonWidth, buttonHeight);
		}
		
		matrices.pop(); // 5
		
		
		// Draw Title.
		matrices.push(); // 2
		
		// Translate to the Information's position.
		matrices.translate(informationX, informationY, 100.0F);
		
		// Translate to the Title's position.
		matrices.translate(informationWidth / 2.0F - TextUtil.getWidth(bodyName) / 2.0F, 0.0F, 0.0F);
		
		textRenderer.draw(matrices, bodyName, 0, 0, Color.WHITE.toRgb());
		
		matrices.pop(); // 2
		
		
		// Draw Description.
		matrices.push(); // 3
		
		// Translate to the Information's position.
		matrices.translate(informationX, informationY, 100.0F);
		
		// Offset the Title's size.
		matrices.translate(0.0F, 4.0F + TextUtil.getHeight(bodyName), 0.0F);
		
		for (var line : bodyDescriptionLines) {
			textRenderer.draw(matrices, line, 0.0F, 0.0F, Color.GRAY.toRgb());
			
			// Offset the line's size.
			matrices.translate(0.0F, TextUtil.getHeight(LiteralText.EMPTY), 0.0F);
		}
		
		matrices.pop(); // 3
		
		
		// Draw Body.
		matrices.push(); // 4
		
		// Translate to the Body's position.
		matrices.translate(bodyX, bodyY, 100.0F);
		
		// Scale the body down to 60% size and translate.
		
		matrices.push();
		
		var texture = body.texture();
		
		DrawingUtil.drawBody(
				provider,
				// 0.1F border per side; 0.2F total border; 1.0F full size.
				bodyX, bodyY, 100.0F,
				bodyWidth * 0.5F, bodyHeight * 0.5F, (bodyWidth + bodyHeight) / 2.0F * 0.5F,
				(float) body.getAngle(),
				false,
				new Color(0xEFEFEFFFL),
				AMRenderLayers.getBody(texture.up()),
				AMRenderLayers.getBody(texture.down()),
				AMRenderLayers.getBody(texture.north()),
				AMRenderLayers.getBody(texture.south()),
				AMRenderLayers.getBody(texture.west()),
				AMRenderLayers.getBody(texture.east())
		);
		
		matrices.pop();
		
		matrices.pop(); // 4
	}
	
	public Consumer<Body> getSelectListener() {
		return selectListener;
	}
	
	public void setSelectListener(Consumer<Body> selectListener) {
		this.selectListener = selectListener;
	}
}
