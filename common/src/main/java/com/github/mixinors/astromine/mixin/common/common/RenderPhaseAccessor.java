package com.github.mixinors.astromine.mixin.common.common;

import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPhase.class)
public interface RenderPhaseAccessor {
	@Accessor
	void setBeginAction(Runnable beginAction);
	
	@Accessor
	void setEndAction(Runnable endAction);
}
