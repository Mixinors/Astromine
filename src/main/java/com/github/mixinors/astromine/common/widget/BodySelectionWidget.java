package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.util.DrawingUtil;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.registry.client.AMRenderLayers;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.queue.ServerTaskQueue;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

import static com.github.mixinors.astromine.common.screen.handler.body.BodySelectorScreenHandler.SELECT_BUTTON_HEIGHT;
import static com.github.mixinors.astromine.common.screen.handler.body.BodySelectorScreenHandler.SELECT_BUTTON_WIDTH;

public class BodySelectionWidget extends Widget {
	private static final ImageTexture GO_BUTTON_TEXTURE_UP = new ImageTexture(AMCommon.id("textures/widget/small_square_button_go_up_clear.png"));
	private static final ImageTexture GO_BUTTON_TEXTURE_DOWN = new ImageTexture(AMCommon.id("textures/widget/small_square_button_go_down_clear.png"));
	
	private Body body;
	
	private boolean selected = false;
	
	private float prevButtonX = 0.0F;
	private float prevButtonY = 0.0F;
	
	private float prevButtonWidth = 0.0F;
	private float prevButtonHeight = 0.0F;
	
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
	
	public Consumer<Body> getSelectListener() {
		return selectListener;
	}
	
	public void setSelectListener(Consumer<Body> selectListener) {
		this.selectListener = selectListener;
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		super.onMouseClicked(event);
		
		// Check if the mouse is inside the button, and if so,
		// trigger the listener with a delay so the player can
		// see the button being pressed.
		if (isFocused()) {
			if (event.button() == GLFW.GLFW_MOUSE_BUTTON_1) {
				if (event.x() >= prevButtonX && event.x() <= prevButtonX + prevButtonWidth && event.y() >= prevButtonY && event.y() <= prevButtonY + prevButtonHeight) {
					selected = true;
					
					selectListener.accept(body);
					
					if (getParent() instanceof WidgetCollection.Root root) {
						ServerTaskQueue.enqueue(($) -> {
							var handler = root.getScreenHandler();
							handler.onClosed(handler.getPlayer());
						}, 5L);
					}
				}
			}
		}
	}
	
	@Override
	public void draw(DrawContext context, float tickDelta) {
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
		
		if (body == null) return;
		
		var client = InstanceUtil.getClient();
		var window = client.getWindow();
		
		var textRenderer = client.textRenderer;
		
		// setPosition to bottom center of window
		setPosition(window.getScaledWidth() / 2.0F - getWidth() / 2.0F, window.getScaledHeight() - getHeight() + 10.0F);
		
		var bodyWidth = (getWidth() * 0.15F);
		var bodyHeight = (getWidth() * 0.15F);
		
		var bodyX = getX();
		var bodyY = getY();
		
		var informationWidth = (getWidth() * 0.7F);
		
		var informationX = bodyX + bodyWidth;
		var informationY = getY();
		
		var buttonWidth = SELECT_BUTTON_WIDTH;
		var buttonHeight = SELECT_BUTTON_HEIGHT;
		
		var buttonX = informationX + informationWidth;
		var buttonY = getY();
		
		var bodyName = body.name();
		var bodyDescription = body.description();
		var bodyDescriptionLines = textRenderer.wrapLines(bodyDescription, (int) informationWidth);
		
		// Find height of body name, description, and required vertical spacing.
		var textHeight = TextUtil.getHeight(bodyName) + 4.0F + (TextUtil.getHeight("") * bodyDescriptionLines.size());
		
		// Offset bodyY to center body within itself.
		bodyY += bodyHeight / 2.0F;
		
		// Offset bodyX to center body within itself.
		bodyX += bodyWidth / 2.0F;
		
		// Offset bodyY to center body within the widget, vertically.
		bodyY += (getHeight() - bodyHeight) / 2.0F;
		
		// Offset buttonY to center button within the widget, vertically.
		buttonY += (getHeight() - buttonHeight) / 2.0F;
		
		// Offset informationY to center information within the widget, vertically.
		informationY += (getHeight() - textHeight) / 2.0F - 6.0F;
		
		// Translate to widget's button position.
		matrices.push(); // 5
		matrices.translate(buttonX, buttonY, 320.0F);
		
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
		matrices.translate(informationX, informationY, 320.0F);
		
		// Translate to the Title's position.
		matrices.translate(informationWidth / 2.0F - TextUtil.getWidth(bodyName) / 2.0F, 0.0F, 0.0F);
		
		var context = new DrawContext(MinecraftClient.getInstance(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers());
		context.drawText(textRenderer, bodyName, 0, 0, Color.WHITE.toRgb(), false);
		
		matrices.pop(); // 2
		
		
		// Draw Description.
		matrices.push(); // 3
		
		// Translate to the Information's position.
		matrices.translate(informationX, informationY, 320.0F);
		
		// Offset the Title's size.
		matrices.translate(0.0F, 4.0F + TextUtil.getHeight(bodyName), 0.0F);
		
		for (var line : bodyDescriptionLines) {
			context.drawText(textRenderer, line, 0, 0, Color.GRAY.toRgb(), false);
			
			// Offset the line's size.
			// TODO: Remove the ""!
			matrices.translate(0.0F, TextUtil.getHeight(""), 0.0F);
		}
		
		matrices.pop(); // 3
		
		
		// Draw Body.
		matrices.push(); // 4
		
		// Translate to the Body's position.
		matrices.translate(bodyX, bodyY, 320.0F);
		
		// Scale the body down to 60% size and translate.
		
		matrices.push(); // 6
		
		var texture = body.texture();
		
		DrawingUtil.drawBody(
				provider,
				bodyX, bodyY, 600.0F,
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
		
		matrices.pop(); // 6
		
		matrices.pop(); // 4
		
		prevButtonX = buttonX;
		prevButtonY = buttonY;
		
		prevButtonWidth = buttonWidth;
		prevButtonHeight = buttonHeight;
	}
}
