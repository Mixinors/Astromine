package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.techreborn.common.component.provider.TREnergyComponentProvider;
import com.github.mixinors.astromine.techreborn.common.component.general.provider.EnergyComponentProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnergyComponentProvider.class)
public interface EnergyComponentProviderMixin extends TREnergyComponentProvider {}
