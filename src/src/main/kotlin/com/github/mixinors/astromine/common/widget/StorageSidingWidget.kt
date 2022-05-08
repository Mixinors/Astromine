package com.github.mixinors.astromine.common.widget

import com.github.mixinors.astromine.AMCommon
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity
import com.github.mixinors.astromine.common.transfer.StorageSiding
import com.github.mixinors.astromine.common.transfer.StorageType
import com.github.mixinors.astromine.common.util.MirrorUtils
import com.github.mixinors.astromine.common.util.NetworkingUtils
import com.github.mixinors.astromine.registry.common.AMNetworks
import dev.architectury.networking.NetworkManager
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.ImageTexture
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.Direction
import org.lwjgl.glfw.GLFW
import java.util.*

class StorageSidingWidget : BaseWidget() {
	companion object {
		private val STANDARD_TEXTURE_INSERT: BaseTexture = ImageTexture(AMCommon.id("textures/widget/insert.png"))
		private val STANDARD_TEXTURE_EXTRACT: BaseTexture = ImageTexture(AMCommon.id("textures/widget/extract.png"))
		private val STANDARD_TEXTURE_INSERT_EXTRACT: BaseTexture = ImageTexture(AMCommon.id("textures/widget/insert_extract.png"))
		private val STANDARD_TEXTURE_NONE: BaseTexture = ImageTexture(AMCommon.id("textures/widget/none.png"))
	}
	
	var blockEntity: ExtendedBlockEntity? = null
	
	var type: StorageType? = null
	
	var direction: Direction? = null
	var rotation: Direction? = null
	
	private val texture: BaseTexture
		get() {
			val blockEntity = blockEntity ?: return STANDARD_TEXTURE_NONE
			
			val direction = direction ?: return STANDARD_TEXTURE_NONE
			
			var sidings = Array(6) { StorageSiding.NONE }
			
			if (type == StorageType.ITEM) {
				val itemStorage = blockEntity.itemStorage ?: return STANDARD_TEXTURE_NONE
				
				sidings = itemStorage.sidings
			}
			
			if (type == StorageType.FLUID) {
				val fluidStorage = blockEntity.fluidStorage ?: return STANDARD_TEXTURE_NONE
				
				sidings = fluidStorage.sidings
			}
			
			return when (sidings[direction.ordinal]) {
				StorageSiding.INSERT -> STANDARD_TEXTURE_INSERT
				StorageSiding.EXTRACT -> STANDARD_TEXTURE_EXTRACT
				StorageSiding.INSERT_EXTRACT -> STANDARD_TEXTURE_INSERT_EXTRACT
				StorageSiding.NONE -> STANDARD_TEXTURE_NONE
			}
		}
	
	override fun onMouseClicked(mouseX: Float, mouseY: Float, button: Int) {
		super.onMouseClicked(mouseX, mouseY, button)
		
		val blockEntity = blockEntity ?: return
		
		val handled = handled ?: return
		
		if (!hidden && focused && handled.client) {
			var sidings = Array(6) { StorageSiding.NONE }
			
			if (type == StorageType.ITEM) {
				val itemStorage = blockEntity.itemStorage ?: return
				
				sidings = itemStorage.sidings
			}
			
			if (type == StorageType.FLUID) {
				val fluidStorage = blockEntity.fluidStorage ?: return
				
				sidings = fluidStorage.sidings
			}
			
			val next: StorageSiding
			
			when (button) {
				GLFW.GLFW_MOUSE_BUTTON_1 -> next = sidings[direction!!.ordinal].next()
				GLFW.GLFW_MOUSE_BUTTON_2 -> next = sidings[direction!!.ordinal].previous()
				
				else -> return
			}
			
			val buf = NetworkingUtils.ofStorageSiding(next, type, direction, blockEntity.pos)
			
			NetworkManager.sendToServer(AMNetworks.STORAGE_SIDING_UPDATE, buf)
		}
	}
	
	override fun getTooltip(): List<Text> {
		val blockEntity = blockEntity ?: return super.getTooltip()
		
		val offset = MirrorUtils.rotate(direction, rotation)
		
		var sidings = Array(6) { StorageSiding.NONE }
		
		if (type == StorageType.ITEM) {
			val itemStorage = blockEntity.itemStorage ?: return super.getTooltip()
			
			sidings = itemStorage.sidings
		}
		
		if (type == StorageType.FLUID) {
			val fluidStorage = blockEntity.fluidStorage ?: return super.getTooltip()
			
			sidings = fluidStorage.sidings
		}
		
		val name = when (sidings[direction!!.ordinal]) {
			StorageSiding.INSERT -> TranslatableText("text.astromine.siding.insert").styled { style: Style -> style.withColor(0x0078FF) }
			StorageSiding.EXTRACT -> TranslatableText("text.astromine.siding.extract").styled { style: Style -> style.withColor(0xFF6100) }
			StorageSiding.INSERT_EXTRACT -> TranslatableText("text.astromine.siding.insert").styled { style: Style -> style.withColor(0x9800FF) }.append(LiteralText(" / ").styled { style: Style -> style.withColor(0x9800FF) }).append(TranslatableText("text.astromine.siding.extract").styled { style: Style -> style.withColor(0x9800FF) })
			StorageSiding.NONE -> TranslatableText("text.astromine.siding.none").styled { style: Style -> style.withColor(0x909090) }
		}
		
		return listOf(TranslatableText("text.astromine.siding." + offset.getName()), name)
	}
	
	@Environment(EnvType.CLIENT)
	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		texture.draw(matrices, provider, x, y, width, height)
	}
}