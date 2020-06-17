package com.github.chainmailstudios.astromine.client.model;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;

public class SuperSpaceSlimeEntityModel extends SlimeEntityModel<SuperSpaceSlimeEntity> {

	private static final ItemStack GLASS = new ItemStack(Items.GLASS);

	public SuperSpaceSlimeEntityModel(int size) {
		super(size);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.getParts().forEach((modelPart) -> modelPart.render(matrices, vertices, light, overlay, red, green, blue, alpha));

		// translate & scale for glass outline
		matrices.translate(0, 1.25, 0);
		matrices.scale(1.25f, 1.25f, 1.25f);

		// render glass block
		MinecraftClient.getInstance().getItemRenderer().renderItem(GLASS, ModelTransformation.Mode.FIXED, light, overlay, matrices, MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers());

		// undo translation & scale
		matrices.scale(.75f, .75f, .75f);
		matrices.translate(0, -1.25, 0);
	}
}
