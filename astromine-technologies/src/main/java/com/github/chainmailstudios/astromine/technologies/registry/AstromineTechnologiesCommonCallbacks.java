package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.registry.BreathableRegistry;
import com.github.chainmailstudios.astromine.common.registry.FluidEffectRegistry;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineCommonCallbacks;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.technologies.common.entity.RocketEntity;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class AstromineTechnologiesCommonCallbacks extends AstromineCommonCallbacks {
	public static void initialize() {
		ItemComponentCallbackV2.register(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, AstromineTechnologiesItems.SPACE_SUIT_CHESTPLATE, (useless, stack) -> {
			FluidInventoryComponent component = new SimpleFluidInventoryComponent(1);
			component.getVolume(0).setSize(Fraction.ofWhole(64));
			return component;
		});
	}
}
