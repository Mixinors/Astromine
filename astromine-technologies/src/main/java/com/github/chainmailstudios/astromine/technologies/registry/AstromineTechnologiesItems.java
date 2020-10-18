/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.technologies.registry;

import net.minecraft.item.Item;

import com.github.chainmailstudios.astromine.common.item.DrillItem;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.technologies.common.item.GravityGauntletItem;
import com.github.chainmailstudios.astromine.technologies.common.item.HolographicConnectorItem;

public class AstromineTechnologiesItems extends AstromineItems {
	public static final Item PRIMITIVE_MACHINE_CHASSIS = register("primitive_machine_chassis", new Item(getBasicSettings()));
	public static final Item BASIC_MACHINE_CHASSIS = register("basic_machine_chassis", new Item(getBasicSettings()));
	public static final Item ADVANCED_MACHINE_CHASSIS = register("advanced_machine_chassis", new Item(getBasicSettings()));
	public static final Item ELITE_MACHINE_CHASSIS = register("elite_machine_chassis", new Item(getBasicSettings()));

	public static final Item BASIC_MACHINE_UPGRADE_KIT = register("basic_machine_upgrade_kit", new Item(getBasicSettings()));
	public static final Item ADVANCED_MACHINE_UPGRADE_KIT = register("advanced_machine_upgrade_kit", new Item(getBasicSettings()));
	public static final Item ELITE_MACHINE_UPGRADE_KIT = register("elite_machine_upgrade_kit", new Item(getBasicSettings()));

	public static final Item GAS_CANISTER = register("gas_canister", FluidVolumeItem.of(getBasicSettings().maxCount(1), Fraction.of(AstromineConfig.get().gasCanisterFluid, 1)));
	public static final Item PRESSURIZED_GAS_CANISTER = register("pressurized_gas_canister", FluidVolumeItem.of(getBasicSettings().maxCount(1), Fraction.of(AstromineConfig.get().pressurizedGasCanisterFluid, 1)));

	public static final Item BASIC_CIRCUIT = register("basic_circuit", new Item(getBasicSettings()));
	public static final Item ADVANCED_CIRCUIT = register("advanced_circuit", new Item(getBasicSettings()));
	public static final Item ELITE_CIRCUIT = register("elite_circuit", new Item(getBasicSettings()));

	public static final Item BASIC_BATTERY = register("basic_battery", EnergyVolumeItem.of(getBasicSettings().maxCount(1), AstromineConfig.get().basicBatteryEnergy));
	public static final Item ADVANCED_BATTERY = register("advanced_battery", EnergyVolumeItem.of(getBasicSettings().maxCount(1), AstromineConfig.get().advancedBatteryEnergy));
	public static final Item ELITE_BATTERY = register("elite_battery", EnergyVolumeItem.of(getBasicSettings().maxCount(1), AstromineConfig.get().eliteBatteryEnergy));
	public static final Item CREATIVE_BATTERY = register("creative_battery", EnergyVolumeItem.ofCreative(getBasicSettings().maxCount(1)));

	public static final Item BASIC_DRILL = register("basic_drill", new DrillItem(AstromineTechnologiesToolMaterials.BASIC_DRILL, 1, -2.8F, 1, AstromineConfig.get().basicDrillEnergy, getBasicSettings().maxCount(1)));
	public static final Item ADVANCED_DRILL = register("advanced_drill", new DrillItem(AstromineTechnologiesToolMaterials.ADVANCED_DRILL, 1, -2.8F, 1, AstromineConfig.get().advancedDrillEnergy, getBasicSettings().maxCount(1)));
	public static final Item ELITE_DRILL = register("elite_drill", new DrillItem(AstromineTechnologiesToolMaterials.ELITE_DRILL, 1, -2.8F, 1, AstromineConfig.get().eliteDrillEnergy, getBasicSettings().maxCount(1)));

	public static final Item HOLOGRAPHIC_CONNECTOR = register("holographic_connector", new HolographicConnectorItem(getBasicSettings().maxCount(1)));

	public static final Item GRAVITY_GAUNTLET = register("gravity_gauntlet", new GravityGauntletItem(getBasicSettings().maxCount(1), AstromineConfig.get().gravityGauntletEnergy));

	public static void initialize() {

	}

	public static Item.Settings getBasicSettings() {
		return new Item.Settings().group(AstromineTechnologiesItemGroups.TECHNOLOGIES);
	}
}
