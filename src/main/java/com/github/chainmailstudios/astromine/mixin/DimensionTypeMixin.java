package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.world.AstromineDimensionType;
import com.github.chainmailstudios.astromine.world.generation.AstromineBiomeSource;
import com.github.chainmailstudios.astromine.world.generation.AstromineChunkGenerator;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Inject(method = "method_28517",
            at = @At("RETURN"),
            cancellable = true)
    private static void method_28517(long seed, CallbackInfoReturnable<SimpleRegistry<DimensionOptions>> cir) {
        SimpleRegistry<DimensionOptions> registry = cir.getReturnValue();
        registry.add(AstromineDimensionType.OPTIONS, new DimensionOptions(() -> AstromineDimensionType.INSTANCE, new AstromineChunkGenerator(new AstromineBiomeSource(seed), seed)));
        cir.setReturnValue(registry);
    }
}