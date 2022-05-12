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

import com.github.mixinors.astromine.AMCommon
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.FluidTexture
import dev.vini2003.hammer.core.api.client.texture.ImageTexture
import dev.vini2003.hammer.core.api.client.texture.TiledFluidTexture
import dev.vini2003.hammer.core.api.common.util.FluidTextUtils
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import org.lwjgl.glfw.GLFW

class FluidFilterWidget(
	var fluidVariantSupplier: () -> FluidVariant = { FluidVariant.blank() },
	var fluidVariantConsumer: (FluidVariant) -> Unit = { }
) : ButtonWidget() {
	companion object {
		private val STANDARD_BACKGROUND_TEXTURE: BaseTexture = ImageTexture(AMCommon.id("textures/widget/fluid_filter_background.png"))
		private val STANDARD_FOREGROUND_TEXTURE: BaseTexture = FluidTexture(FluidVariant.blank())
	}
	
	private val backgroundTexture = STANDARD_BACKGROUND_TEXTURE
	private var foregroundTexture = STANDARD_FOREGROUND_TEXTURE
	
	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		super.onMouseClicked(x, y, button)
		
		val handled = handled ?: return
		val handler = handled.handler ?: return
		
		val stack = handler.cursorStack
		
		if (isPointWithin(x, y)) {
			if (!stack.isEmpty) {
				val fluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.ofPlayerCursor(handler.player, handler))
				
				if (fluidStorage != null) {
					val fluidVariant = StorageUtil.findStoredResource(fluidStorage, null) ?: FluidVariant.blank()
					
					fluidVariantSupplier = { fluidVariant }
					fluidVariantConsumer(fluidVariant)
				}
			} else if (button == GLFW.GLFW_MOUSE_BUTTON_3) {
				val fluidVariant = FluidVariant.blank()
				
				fluidVariantSupplier = { fluidVariant }
				fluidVariantConsumer(fluidVariant)
			}
		}
	}
	
	override fun getTooltip(): List<Text> {
		val fluidTooltip = FluidTextUtils.getVariantTooltips(fluidVariantSupplier())
		
		return listOf(TranslatableText("text.astromine.filter")) + fluidTooltip
	}
	
	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		backgroundTexture.draw(matrices, provider, x, y, width, height)
		
		val fluidVariant = fluidVariantSupplier()
		
		if (fluidVariant.isBlank) {
			return
		}
		
		foregroundTexture = TiledFluidTexture(fluidVariant)
		
		foregroundTexture.draw(matrices, provider, x + 1.0F, y + 1.0F, width - 2.0F, height - 2.0F)
	}
}