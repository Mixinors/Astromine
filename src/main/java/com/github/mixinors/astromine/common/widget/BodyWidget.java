package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.body.Body;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.function.Supplier;

public class BodyWidget extends Widget {
	private static final double ABSOLUTE_CANVAS_WIDTH = 384_000_000;
	private static final double ABSOLUTE_CANVAS_HEIGHT = 384_000_000;
	
	private static final double DESIRED_CANVAS_WIDTH = 768;
	private static final double DESIRED_CANVAS_HEIGHT = 768;
	
	public static final Texture STANDARD_FOREGROUND_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/circle.png"));
	
	protected Supplier<Texture> foregroundTexture = () -> STANDARD_FOREGROUND_TEXTURE;
	
	private final Body body;
	
	public BodyWidget(Body body) {
		this.body = body;
	}
	
	public Position getBodyPosition() {
		return new Position((float) (body.getPos().getX() / 384_000 * InstanceUtil.getClient().getWindow().getScaledWidth()), (float) (body.getPos().getZ() / 384_000 * InstanceUtil.getClient().getWindow().getScaledHeight())).plus(getPosition());
	}
	
	public Size getBodySize() {
		return new Size(32, 32);
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var df = new DecimalFormat("###.###");
		
		var bP = getBodyPosition();
		var bS = getBodySize();
		
		DrawingUtil.getTextRenderer().draw(matrices, "Mass (kg): " + df.format(body.getMass()), bP.getX() + 18, bP.getY() + 4.5F, 0xFFFFFF);
		DrawingUtil.getTextRenderer().draw(matrices, "Velocity (km/t): " + df.format(body.getVelocity().getX()) + ", " +  df.format(body.getVelocity().getY()) + ", " + df.format(body.getVelocity().getZ()), bP.getX() + 18, bP.getY() + 4.5F + 9.0F + 4.5F, 0xFFFFFF);
		
		new ImageTexture(AMCommon.id("textures/widget/circle.png")).draw(matrices, provider, bP.getX() - (bS.getWidth() / 2 / 2), bP.getY() - (bS.getHeight() / 2 / 2), bS.getWidth() / 2, bS.getHeight() / 2);
	}
}
