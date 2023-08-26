package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.RedstoneType;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import com.google.common.collect.ImmutableList;
import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class RedstoneControlWidget extends ButtonWidget {
	private static final ItemStack GLOWSTONE = new ItemStack(Items.GLOWSTONE_DUST);
	private static final ItemStack REDSTONE = new ItemStack(Items.REDSTONE);
	private static final ItemStack GUNPOWDER = new ItemStack(Items.GUNPOWDER);
	
	private static final Texture STANDARD_TEXTURE = PanelWidget.STANDARD_TEXTURE;
	
	protected Supplier<Texture> texture = () -> STANDARD_TEXTURE;
	
	protected ExtendedBlockEntity blockEntity;
	
	public RedstoneControlWidget() {
		super();
		
		setTooltipSupplier(() -> {
			if (blockEntity != null) {
				var type = blockEntity.getRedstoneType();
				
				switch (type) {
					case WORK_WHEN_ON -> {
						return ImmutableList.of(Text.translatable("tooltip.astromine.work_when_on").formatted(Formatting.GREEN));
					}
					
					case WORK_WHEN_OFF -> {
						return ImmutableList.of(Text.translatable("tooltip.astromine.work_when_off").formatted(Formatting.RED));
					}
					
					case WORK_ALWAYS -> {
						return ImmutableList.of(Text.translatable("tooltip.astromine.work_always").formatted(Formatting.YELLOW));
					}
				}
			}
			
			return ImmutableList.of();
		});
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		super.onMouseClicked(event);
		
		if (blockEntity == null) {
			return;
		}
		
		var root = getRootCollection();
		
		if (root == null) {
			return;
		}
		
		if (isFocused() && root.isClient()) {
			var prevType = blockEntity.getRedstoneType();
			
			RedstoneType nextType;
			
			switch (event.button()) {
				case GLFW.GLFW_MOUSE_BUTTON_1 -> nextType = prevType.next();
				case GLFW.GLFW_MOUSE_BUTTON_2 -> nextType = prevType.previous();
				
				default -> {
					return;
				}
			}
			
			var buf = PacketByteBufs.create();
			
			buf.writeEnumConstant(nextType);
			buf.writeBlockPos(blockEntity.getPos());
			
			NetworkManager.sendToServer(AMNetworking.REDSTONE_TYPE_UPDATE, buf);
		}
	}
	
	@Override
	public void draw(DrawContext context, float tickDelta) {
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
		
		if (blockEntity == null) {
			return;
		}
		
		var drawContext = new DrawContext(MinecraftClient.getInstance(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers());
		
		texture.get().draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		
		switch (blockEntity.getRedstoneType()) {
			case WORK_WHEN_ON -> drawContext.drawItem(REDSTONE, (int) Math.max(0.0D, getX() + ((getWidth() - 16.0F) / 2.0F)), (int) Math.max(0.0D, getY() + ((getHeight() - 16.0F) / 2.0F)));
			case WORK_WHEN_OFF -> drawContext.drawItem(GUNPOWDER, (int) Math.max(0.0D, getX() + ((getWidth() - 16.0F) / 2.0F)), (int) Math.max(0.0D, getY() + ((getHeight() - 16.0F) / 2.0F)));
			case WORK_ALWAYS -> drawContext.drawItem(GLOWSTONE, (int) Math.max(0.0D, getX() + ((getWidth() - 16.0F) / 2.0F)), (int) Math.max(0.0D, getY() + ((getHeight() - 16.0F) / 2.0F)));
			
		}
	}
	
	public ExtendedBlockEntity getBlockEntity() {
		return blockEntity;
	}
	
	public void setBlockEntity(ExtendedBlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}
}
