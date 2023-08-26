package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.github.mixinors.astromine.common.util.MirrorUtils;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import com.google.common.collect.ImmutableList;
import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class StorageSidingWidget extends Widget {
	public static final Texture STANDARD_TEXTURE_INSERT = new ImageTexture(AMCommon.id("textures/widget/insert.png"));
	public static final Texture STANDARD_EXTRACT_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/extract.png"));
	public static final Texture STANDARD_INSERT_EXTRACT_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/insert_extract.png"));
	public static final Texture STANDARD_TEXTURE_NONE = new ImageTexture(AMCommon.id("textures/widget/none.png"));
	
	protected Supplier<Texture> textureNone = () -> STANDARD_TEXTURE_NONE;
	protected Supplier<Texture> textureInsert = () -> STANDARD_TEXTURE_INSERT;
	protected Supplier<Texture> textureExtract = () -> STANDARD_EXTRACT_TEXTURE;
	protected Supplier<Texture> textureInsertExtract = () -> STANDARD_INSERT_EXTRACT_TEXTURE;
	
	private ExtendedBlockEntity blockEntity;
	
	private StorageType type;
	
	private Direction direction;
	private Direction rotation;
	
	public StorageSidingWidget() {
		setTooltipSupplier(() -> {
			var offset = MirrorUtils.rotate(direction, rotation);
			
			var sidings = new StorageSiding[6];
			
			if (type == StorageType.ITEM) {
				var itemStorage = blockEntity.getItemStorage();
				
				if (itemStorage == null) {
					return ImmutableList.of();
				}
				
				sidings = itemStorage.getSidings();
			}
			
			if (type == StorageType.FLUID) {
				var fluidStorage = blockEntity.getFluidStorage();
				
				if (fluidStorage == null) {
					return ImmutableList.of();
				}
				
				sidings = fluidStorage.getSidings();
			}
			
			Text name;
			
			if (sidings[direction.ordinal()] == StorageSiding.INSERT) {
				name = Text.translatable("text.astromine.siding.insert").styled((s) -> s.withColor(0x0078FF));
			} else if (sidings[direction.ordinal()] == StorageSiding.EXTRACT) {
				name = Text.translatable("text.astromine.siding.extract").styled((s) -> s.withColor(0xFF6100));
			} else if (sidings[direction.ordinal()] == StorageSiding.INSERT_EXTRACT) {
				name = Text.translatable("text.astromine.siding.insert").styled((s) -> s.withColor(0x9800FF)).append(Text.literal(" / ").styled((s) -> s.withColor(0x9800FF)).append(Text.translatable("text.astromine.siding.extract").styled((s) -> s.withColor(0x9800FF))));
			} else {
				name = Text.translatable("text.astromine.siding.none").styled((s) -> s.withColor(0x909090));
			}
			
			return ImmutableList.of(Text.translatable("text.astromine.siding." + offset.getName()), name);
		});
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		super.onMouseClicked(event);
		
		if (!isHidden() && isFocused() && getRootCollection().isClient()) {
			var sidings = new StorageSiding[6];
			
			if (type == StorageType.ITEM) {
				var itemStorage = blockEntity.getItemStorage();
				
				if (itemStorage == null) {
					return;
				}
				
				sidings = itemStorage.getSidings();
			}
			
			if (type == StorageType.FLUID) {
				var fluidStorage = blockEntity.getFluidStorage();
				
				if (fluidStorage == null) {
					return;
				}
				
				sidings = fluidStorage.getSidings();
			}
			
			StorageSiding next;
			
			if (event.button() == GLFW.GLFW_MOUSE_BUTTON_1) {
				next = sidings[direction.ordinal()].next();
			} else if (event.button() == GLFW.GLFW_MOUSE_BUTTON_2) {
				next = sidings[direction.ordinal()].previous();
			} else {
				return;
			}
			
			var buf = PacketByteBufs.create();
			
			buf.writeEnumConstant(next);
			buf.writeEnumConstant(type);
			buf.writeEnumConstant(direction);
			
			buf.writeBlockPos(blockEntity.getPos());
			
			NetworkManager.sendToServer(AMNetworking.STORAGE_SIDING_UPDATE, buf);
		}
	}
	
	@Override
	public void draw(DrawContext context, float tickDelta) {
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
		getTexture().draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
	}
	
	public Texture getTexture() {
		if (blockEntity == null) {
			return textureNone.get();
		}
		
		if (direction == null) {
			return textureNone.get();
		}
		
		var sidings = new StorageSiding[6];
		
		if (type == StorageType.ITEM) {
			var itemStorage = blockEntity.getItemStorage();
			
			if (itemStorage == null) {
				return textureNone.get();
			}
			
			sidings = itemStorage.getSidings();
		}
		
		if (type == StorageType.FLUID) {
			var fluidStorage = blockEntity.getFluidStorage();
			
			if (fluidStorage == null) {
				return textureNone.get();
			}
			
			sidings = fluidStorage.getSidings();
		}
		
		if (sidings[direction.ordinal()] == StorageSiding.INSERT) {
			return textureInsert.get();
		} else if (sidings[direction.ordinal()] == StorageSiding.EXTRACT) {
			return textureExtract.get();
		} else if (sidings[direction.ordinal()] == StorageSiding.INSERT_EXTRACT) {
			return textureInsertExtract.get();
		} else {
			return textureNone.get();
		}
	}
	
	public ExtendedBlockEntity getBlockEntity() {
		return blockEntity;
	}
	
	public void setBlockEntity(ExtendedBlockEntity blockEntity) {
		this.blockEntity = blockEntity;
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
