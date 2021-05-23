package com.github.mixinors.astromine.registry.common.fabric;

import com.github.mixinors.astromine.common.item.UncoloredSpawnEggItem;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import com.github.mixinors.astromine.registry.common.AMItems;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;

import static com.github.mixinors.astromine.registry.common.AMItems.*;

public class AMItemsImpl {
	public static void postInit() {
		// TODO: Reimplement this on Fabric & Forge modules!
//		BRONZE_HAMMER = register("bronze_hammer", () -> new HammerItem(AMToolMaterials.BRONZE, 1, -2.8f, AMItems.getBasicSettings()));
//		BRONZE_EXCAVATOR = register("bronze_excavator", () -> new ExcavatorItem(AMToolMaterials.BRONZE, 1, -2.8f, AMItems.getBasicSettings()));
//
//		STEEL_HAMMER = register("steel_hammer", () -> new HammerItem(AMToolMaterials.STEEL, 1, -2.8f, AMItems.getBasicSettings()));
//		STEEL_EXCAVATOR = register("steel_excavator", () -> new ExcavatorItem(AMToolMaterials.STELLUM, 1, -2.8f, AMItems.getBasicSettings()));
//
//		METITE_HAMMER = register("metite_hammer", () -> new HammerItem(AMToolMaterials.METITE, 1, -2.8f, AMItems.getBasicSettings()));
//		METITE_EXCAVATOR = register("metite_excavator", () -> new ExcavatorItem(AMToolMaterials.METITE, 1, -2.8f, AMItems.getBasicSettings()));
//
//		ASTERITE_HAMMER = register("asterite_hammer", () -> new HammerItem(AMToolMaterials.ASTERITE, 1, -2.8f, AMItems.getBasicSettings()));
//		ASTERITE_EXCAVATOR = register("asterite_excavator", () -> new ExcavatorItem(AMToolMaterials.ASTERITE, 1, -2.8f, AMItems.getBasicSettings()));
//
//		STELLUM_HAMMER = register("stellum_hammer", () -> new HammerItem(AMToolMaterials.STELLUM, 1, -2.8f, AMItems.getBasicSettings().fireproof()));
//		STELLUM_EXCAVATOR = register("stellum_excavator", () -> new ExcavatorItem(AMToolMaterials.STELLUM, 1, -2.8f, AMItems.getBasicSettings().fireproof()));
//
//		GALAXIUM_HAMMER = register("galaxium_hammer", () -> new HammerItem(AMToolMaterials.GALAXIUM, 1, -2.8f, AMItems.getBasicSettings()));
//		GALAXIUM_EXCAVATOR = register("galaxium_excavator", () -> new ExcavatorItem(AMToolMaterials.GALAXIUM, 1, -2.8f, AMItems.getBasicSettings()));
//
//		UNIVITE_HAMMER = register("univite_hammer", () -> new HammerItem(AMToolMaterials.UNIVITE, 1, -2.8f, AMItems.getBasicSettings().fireproof()));
//		UNIVITE_EXCAVATOR = register("univite_excavator", () -> new ExcavatorItem(AMToolMaterials.UNIVITE, 1, -2.8f, AMItems.getBasicSettings().fireproof()));
//
//		METEORIC_STEEL_HAMMER = register("meteoric_steel_hammer", () -> new HammerItem(AMToolMaterials.METEORIC_STEEL, 1, -2.8f, AMItems.getBasicSettings()));
//		METEORIC_STEEL_EXCAVATOR = register("meteoric_steel_excavator", () -> new ExcavatorItem(AMToolMaterials.METEORIC_STEEL, 1, -2.8f, AMItems.getBasicSettings()));
		
		SPACE_SLIME_SPAWN_EGG = register("space_slime_spawn_egg", () -> new UncoloredSpawnEggItem(AMEntityTypes.SPACE_SLIME.get(), AMItems.getBasicSettings()));
		
		SPACE_SLIME_BALL = register("space_slime_ball", () -> new Item(AMItems.getBasicSettings()));
		
		PRIMITIVE_ROCKET = register("rocket", () -> new UncoloredSpawnEggItem(AMEntityTypes.PRIMITIVE_ROCKET.get(), AMItems.getBasicSettings()));
		
		// TODO: Reimplement this on Forge module!
		FuelRegistry.INSTANCE.add(WOODEN_MATTOCK.get(), 200);
		FuelRegistry.INSTANCE.add(WOODEN_MINING_TOOL.get(), 200);
	}
}
