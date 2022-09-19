package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.AMCommon;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.TiledFluidTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.util.FluidTextUtil;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidFilterWidget extends Widget {
	public static final Texture STANDARD_BACKGROUND_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/fluid_filter_background.png"));
	
	protected Supplier<FluidVariant> fluidVariant = FluidVariant::blank;
	
	protected Consumer<FluidVariant> action = ($) -> {};
	
	protected Supplier<Texture> foregroundTexture = () -> new TiledFluidTexture(fluidVariant.get());
	protected Supplier<Texture> backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
	
	public FluidFilterWidget() {
		super();
		
		setTooltipSupplier(() -> {
			var tooltips = new ArrayList<Text>();
			tooltips.add(Text.translatable("text.astromine.filter"));
			tooltips.addAll(FluidTextUtil.getVariantTooltips(fluidVariant.get()));
			
			return tooltips;
		});
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		super.onMouseClicked(event);
		
		var root = getRootCollection();
		
		if (root == null) {
			return;
		}
		
		var screenHandler = root.getScreenHandler();
		
		if (screenHandler == null) {
			return;
		}
		
		var cursorStack = screenHandler.getCursorStack();
		
		if (isPointWithin(event.x(), event.y())) {
			if (!cursorStack.isEmpty()) {
				var fluidStorage = FluidStorage.ITEM.find(cursorStack, ContainerItemContext.ofPlayerCursor(screenHandler.getPlayer(), screenHandler));
				
				if (fluidStorage != null) {
					var fluidVariant = StorageUtil.findStoredResource(fluidStorage, null);
					
					if (fluidVariant == null) {
						this.fluidVariant = FluidVariant::blank;
					} else {
						this.fluidVariant = () -> fluidVariant;
					}
					
					action.accept(this.fluidVariant.get());
				}
			} else if (event.button() == GLFW.GLFW_MOUSE_BUTTON_3) {
				this.fluidVariant = FluidVariant::blank;
				
				action.accept(this.fluidVariant.get());
			}
		}
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		backgroundTexture.get().draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		
		if (fluidVariant.get().isBlank()) {
			return;
		}
		
		foregroundTexture.get().draw(matrices, provider, getX() + 1.0F, getY() + 1.0F, getWidth() - 2.0F, getHeight() - 2.0F);
	}
	
	public Supplier<FluidVariant> getFluidVariant() {
		return fluidVariant;
	}
	
	public void setFluidVariant(Supplier<FluidVariant> fluidVariant) {
		this.fluidVariant = fluidVariant;
	}
	
	public Consumer<FluidVariant> getAction() {
		return action;
	}
	
	public void setAction(Consumer<FluidVariant> action) {
		this.action = action;
	}
}
