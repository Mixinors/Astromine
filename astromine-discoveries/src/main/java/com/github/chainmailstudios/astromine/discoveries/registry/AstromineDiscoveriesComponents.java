package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import net.minecraft.fluid.Fluids;

public class AstromineDiscoveriesComponents implements ItemComponentInitializer {
    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.registerFor(
            item -> item == AstromineDiscoveriesItems.SPACE_SUIT_CHESTPLATE,
            AstromineComponents.FLUID_INVENTORY_COMPONENT,
            stack -> SimpleFluidComponent.of(FluidVolume.of(Fraction.of(AstromineConfig.get().spaceSuitFluid), Fluids.EMPTY))
        );
    }
}
