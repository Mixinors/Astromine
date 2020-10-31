package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.registry.AstromineTags;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @ModifyVariable(method = "render", at = @At(value = "STORE", ordinal = 0))
    private static FluidState astromine_renderIndustrialFluidBackground(FluidState submergedFluidState) {
        if (AstromineTags.INDUSTRIAL_FLUID.contains(submergedFluidState.getFluid())) {
            // steal the water background rendering
            return Fluids.WATER.getDefaultState();
        }
        return submergedFluidState;
    }
}
