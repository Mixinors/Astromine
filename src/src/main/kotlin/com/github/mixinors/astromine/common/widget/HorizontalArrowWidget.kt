package com.github.mixinors.astromine.common.widget

import com.github.mixinors.astromine.AMCommon
import com.github.mixinors.astromine.common.util.TextUtils
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.ImageTexture
import dev.vini2003.hammer.gui.api.common.widget.bar.TextureBarWidget
import net.minecraft.text.Text

class HorizontalArrowWidget : TextureBarWidget() {
	companion object {
		private val STANDARD_BACKGROUND_TEXTURE: BaseTexture = ImageTexture(AMCommon.id("textures/widget/horizontal_arrow_background.png"))
		private val STANDARD_FOREGROUND_TEXTURE: BaseTexture = ImageTexture(AMCommon.id("textures/widget/horizontal_arrow_foreground.png"))
	}
	
	override var backgroundTexture = STANDARD_BACKGROUND_TEXTURE
	override var foregroundTexture = STANDARD_FOREGROUND_TEXTURE
	
	init {
		horizontal = true
		smooth = false
	}
	
	override fun getTooltip(): List<Text> {
		return listOf(TextUtils.getRatio(current().toInt(), maximum().toInt()))
	}
}