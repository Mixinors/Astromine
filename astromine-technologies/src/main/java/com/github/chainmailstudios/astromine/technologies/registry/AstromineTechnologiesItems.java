package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.item.DrillItem;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.registry.AstromineItemGroups;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.technologies.common.item.HolographicConnectorItem;
import net.minecraft.item.Item;

public class AstromineTechnologiesItems extends AstromineItems {
	public static final Item PRIMITIVE_MACHINE_CHASSIS = register("primitive_machine_chassis", new Item(getBasicSettings()));
	public static final Item BASIC_MACHINE_CHASSIS = register("basic_machine_chassis", new Item(getBasicSettings()));
	public static final Item ADVANCED_MACHINE_CHASSIS = register("advanced_machine_chassis", new Item(getBasicSettings()));
	public static final Item ELITE_MACHINE_CHASSIS = register("elite_machine_chassis", new Item(getBasicSettings()));

	public static final Item GAS_CANISTER = register("gas_canister", FluidVolumeItem.of(getBasicSettings().maxCount(1), Fraction.of(8, 1)));
	public static final Item PRESSURIZED_GAS_CANISTER = register("pressurized_gas_canister", FluidVolumeItem.of(getBasicSettings().maxCount(1), Fraction.of(32, 1)));

	public static final Item BASIC_CIRCUIT = register("basic_circuit", new Item(getBasicSettings()));
	public static final Item ADVANCED_CIRCUIT = register("advanced_circuit", new Item(getBasicSettings()));
	public static final Item ELITE_CIRCUIT = register("elite_circuit", new Item(getBasicSettings()));

	public static final Item BASIC_BATTERY = register("basic_battery", EnergyVolumeItem.of(getBasicSettings().maxCount(1), 9000));
	public static final Item ADVANCED_BATTERY = register("advanced_battery", EnergyVolumeItem.of(getBasicSettings().maxCount(1), 24000));
	public static final Item ELITE_BATTERY = register("elite_battery", EnergyVolumeItem.of(getBasicSettings().maxCount(1), 64000));
	public static final Item CREATIVE_BATTERY = register("creative_battery", EnergyVolumeItem.ofCreative(getBasicSettings().maxCount(1)));

	public static final Item BASIC_DRILL = register("basic_drill", new DrillItem(AstromineTechnologiesToolMaterials.BASIC_DRILL, 1, -2.8F, 1, 90000, getBasicSettings().maxCount(1)));
	public static final Item ADVANCED_DRILL = register("advanced_drill", new DrillItem(AstromineTechnologiesToolMaterials.ADVANCED_DRILL, 1, -2.8F, 1, 240000, getBasicSettings().maxCount(1)));
	public static final Item ELITE_DRILL = register("elite_drill", new DrillItem(AstromineTechnologiesToolMaterials.ELITE_DRILL, 1, -2.8F, 1, 640000, getBasicSettings().maxCount(1)));

	public static final Item HOLOGRAPHIC_CONNECTOR = register("holographic_connector", new HolographicConnectorItem(getBasicSettings().maxCount(1)));

	public static void initialize() {

	}

	public static Item.Settings getBasicSettings() {
		return new Item.Settings().group(AstromineTechnologiesItemGroups.TECHNOLOGIES);
	}
}
