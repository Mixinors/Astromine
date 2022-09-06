package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.util.DrawingUtil;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.registry.client.AMRenderLayers;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class BodySelectionWidget extends Widget {
	private static final ImageTexture GO_BUTTON_TEXTURE_UP = new ImageTexture(AMCommon.id("widgets/large_square_button_go_up.png"));
	private static final ImageTexture GO_BUTTON_TEXTURE_DOWN = new ImageTexture(AMCommon.id("widgets/large_square_button_go_down.png"));
	
	private final Body body;
	
	private boolean selected = false;
	
	private Consumer<Body> selectListener = ($) -> {};
	
	public BodySelectionWidget(Body body) {
		this.body = body;
	}
	
	public Body getBody() {
		return body;
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
		var client = InstanceUtil.getClient();
		
		var textRenderer = client.textRenderer;
		
		var x = getX();
		var y = getY();
		var z = getZ();
		
		var width = getWidth();
		var height = getHeight();
		var depth = getDepth();
		
		var bodyName = body.name();
		var bodyDescription = body.description();
		var bodyDescriptionLines = textRenderer.wrapLines(bodyDescription, (int) (width * 0.5F));
		
		// Translate to widget's button position.
		matrices.push(); // 5
		matrices.translate(x + width * 0.75F, y, 0.0F);
		
		if (!selected) {
			GO_BUTTON_TEXTURE_UP.draw(matrices, provider, 0.0F, 0.0F, width * 0.25F, height);
		} else {
			GO_BUTTON_TEXTURE_DOWN.draw(matrices, provider, 0.0F, 0.0F, width * 0.25F, height);
		}
		
		matrices.pop(); // 5
		
		// Translate to widget's information position.
		matrices.push(); // 1
		matrices.translate(x + width * 0.25F, y, 100.0F);
		
		// Draw Title.
		matrices.push(); // 2
		matrices.translate(x + width * 0.25F + (width * 0.25F - (TextUtil.getWidth(bodyName)) / 2.0F), 0.0F, 0.0F);
		textRenderer.draw(matrices, bodyName, 0, 0, 0xFFFFFF);
		matrices.pop(); // 2
		
		// Draw Description.
		matrices.push(); // 3
		matrices.translate(x + width + 0.25F, 4.0F + TextUtil.getHeight(bodyName) + 4.0F, 0.0F);
		
		for (var line : bodyDescriptionLines) {
			textRenderer.draw(matrices, line, 0, 0, 0xFFFFFFF);
		}
		
		matrices.pop(); // 3
		
		// Draw Body.
		matrices.push(); // 4
		
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0F));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F));
		
		var texture = body.texture();
		
		DrawingUtil.drawCube(
				matrices,
				provider,
				// 0.1F border per side; 0.2F total border; 1.0F full size.
				x, y, z,
				width * 0.25F, height * 0.25F, depth * 0.25F,
				new Color(0xEFEFEFFFL),
				AMRenderLayers.getBody(texture.up()),
				AMRenderLayers.getBody(texture.down()),
				AMRenderLayers.getBody(texture.north()),
				AMRenderLayers.getBody(texture.south()),
				AMRenderLayers.getBody(texture.west()),
				AMRenderLayers.getBody(texture.east())
		);
		
		matrices.pop(); // 4
		
		matrices.pop(); // 1
	}
	
	public Consumer<Body> getSelectListener() {
		return selectListener;
	}
	
	public void setSelectListener(Consumer<Body> selectListener) {
		this.selectListener = selectListener;
	}
}
