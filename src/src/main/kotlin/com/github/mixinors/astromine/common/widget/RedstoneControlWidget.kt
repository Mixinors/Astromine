/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.widget

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity
import com.github.mixinors.astromine.common.transfer.RedstoneControl
import com.github.mixinors.astromine.common.util.NetworkingUtils
import com.github.mixinors.astromine.registry.common.AMNetworking
import dev.architectury.networking.NetworkManager
import dev.vini2003.hammer.core.api.client.util.DrawingUtils.ITEM_RENDERER
import dev.vini2003.hammer.core.api.common.util.extension.green
import dev.vini2003.hammer.core.api.common.util.extension.red
import dev.vini2003.hammer.core.api.common.util.extension.yellow
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import org.lwjgl.glfw.GLFW

class RedstoneControlWidget : ButtonWidget() {
	companion object {
		private val GLOWSTONE = ItemStack(Items.GLOWSTONE_DUST)
		private val REDSTONE = ItemStack(Items.REDSTONE)
		private val GUNPOWDER = ItemStack(Items.GUNPOWDER)
		
		private val STANDARD_TEXTURE = PanelWidget.STANDARD_TEXTURE
	}
	
	init {
		onTexture = STANDARD_TEXTURE
		offTexture = STANDARD_TEXTURE
		focusedTexture = STANDARD_TEXTURE
	}
	
	var blockEntity: ExtendedBlockEntity? = null
	
	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		super.onMouseClicked(x, y, button)
		
		val blockEntity = blockEntity ?: return
		
		val handled = handled ?: return
		
		if (focused && handled.client) {
			val control = blockEntity.redstoneControl
			
			val next: RedstoneControl
			
			when (button) {
				GLFW.GLFW_MOUSE_BUTTON_1 -> next = control.next()
				GLFW.GLFW_MOUSE_BUTTON_2 -> next = control.previous()
				
				else -> return
			}
			
			val buf = NetworkingUtils.ofRedstoneControl(next, blockEntity.getPos())
			
			NetworkManager.sendToServer(AMNetworking.REDSTONE_CONTROL_UPDATE, buf)
		}
	}
	
	override fun getTooltip(): List<Text> {
		val blockEntity = blockEntity ?: return super.getTooltip()
		
		return when (blockEntity.redstoneControl) {
			RedstoneControl.WORK_WHEN_ON -> listOf(TranslatableText("tooltip.astromine.work_when_on").green())
			RedstoneControl.WORK_WHEN_OFF -> listOf(TranslatableText("tooltip.astromine.work_when_off").red())
			RedstoneControl.WORK_ALWAYS -> listOf(TranslatableText("tooltip.astromine.work_always").yellow())
		}
	}
	
	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		val blockEntity = blockEntity ?: return
		
		val itemRenderer = ITEM_RENDERER ?: return
		
		RedstoneControlWidget.STANDARD_TEXTURE.draw(matrices, provider, x, y, width, height)
		
		when (blockEntity.redstoneControl) {
			RedstoneControl.WORK_WHEN_ON -> itemRenderer.renderGuiItemIcon(REDSTONE, x.toInt() + ((width - 16.0F) / 2.0F).coerceAtLeast(0.0F).toInt(), y.toInt() + ((height - 16.0F) / 2.0F).coerceAtLeast(0.0F).toInt())
			RedstoneControl.WORK_WHEN_OFF -> itemRenderer.renderGuiItemIcon(GUNPOWDER, x.toInt() + ((width - 16.0F) / 2.0F).coerceAtLeast(0.0F).toInt(), y.toInt() + ((height - 16.0F) / 2.0F).coerceAtLeast(0.0F).toInt())
			RedstoneControl.WORK_ALWAYS -> itemRenderer.renderGuiItemIcon(GLOWSTONE, x.toInt() + ((width - 16.0F) / 2.0F).coerceAtLeast(0.0F).toInt(), y.toInt() + ((height - 16.0F) / 2.0F).coerceAtLeast(0.0F).toInt())
		}
	}
}