package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.client.accessor.BakedQuadAccessor;
import net.minecraft.client.render.model.BakedQuad;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BakedQuad.class)
public class BakedQuadMixin implements BakedQuadAccessor {
	@Mutable
	@Shadow
	@Final
	protected int colorIndex;
	
	@Override
	public void setColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
	}
}
