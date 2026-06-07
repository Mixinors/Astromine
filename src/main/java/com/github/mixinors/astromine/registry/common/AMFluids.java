/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.fluid.base.ExtendedFluid;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class AMFluids {
	static final DeferredRegister<FluidType> TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, AMCommon.MOD_ID);
	static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(Registries.FLUID, AMCommon.MOD_ID);
	
	public static final ExtendedFluid.Entry MOLTEN_AMETHYST = ExtendedFluid.builder().fogColor(0x7EB18CF0).tintColor(0x7EB18CF0).name("molten_amethyst").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_ASTERITE = ExtendedFluid.builder().fogColor(0x7EAD2346).tintColor(0x7EAD2346).name("molten_asterite").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_BRONZE = ExtendedFluid.builder().fogColor(0x7ECEA662).tintColor(0x7ECEA662).name("molten_bronze").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_COPPER = ExtendedFluid.builder().fogColor(0x7EBF5935).tintColor(0x7EBF5935).name("molten_copper").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_DIAMOND = ExtendedFluid.builder().fogColor(0x7E9FF8E5).tintColor(0x7E9FF8E5).name("molten_diamond").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_ELECTRUM = ExtendedFluid.builder().fogColor(0x7EF7ECA0).tintColor(0x7EF7ECA0).name("molten_electrum").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_EMERALD = ExtendedFluid.builder().fogColor(0x7E80F3AB).tintColor(0x7E80F3AB).name("molten_emerald").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_FOOLS_GOLD = ExtendedFluid.builder().fogColor(0x7EC4994E).tintColor(0x7EC4994E).name("molten_fools_gold").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_GALAXIUM = ExtendedFluid.builder().fogColor(0x7E4F2668).tintColor(0x7E4F2668).name("molten_galaxium").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_GOLD = ExtendedFluid.builder().fogColor(0x7EF7D349).tintColor(0x7EF7D349).name("molten_gold").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_IRON = ExtendedFluid.builder().fogColor(0x7EA6A6A6).tintColor(0x7EA6A6A6).name("molten_iron").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_LAPIS = ExtendedFluid.builder().fogColor(0x7E2857B3).tintColor(0x7E2857B3).name("molten_lapis").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_LEAD = ExtendedFluid.builder().fogColor(0x7E4A495C).tintColor(0x7E4A495C).name("molten_lead").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_METEORIC_STEEL = ExtendedFluid.builder().fogColor(0x7E9672AB).tintColor(0x7E9672AB).name("molten_meteoric_steel").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_METITE = ExtendedFluid.builder().fogColor(0x7ED349F7).tintColor(0x7ED349F7).name("molten_metite").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_NETHERITE = ExtendedFluid.builder().fogColor(0x7E4B4042).tintColor(0x7E4B4042).name("molten_netherite").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_QUARTZ = ExtendedFluid.builder().fogColor(0x7EDAD1C4).tintColor(0x7EDAD1C4).name("molten_quartz").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_REDSTONE = ExtendedFluid.builder().fogColor(0x7EA20D00).tintColor(0x7EA20D00).name("molten_redstone").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_SILVER = ExtendedFluid.builder().fogColor(0x7EC0D9D9).tintColor(0x7EC0D9D9).name("molten_silver").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_STEEL = ExtendedFluid.builder().fogColor(0x7E81898A).tintColor(0x7E81898A).name("molten_steel").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_STELLUM = ExtendedFluid.builder().fogColor(0x7EFCC181).tintColor(0x7EFCC181).name("molten_stellum").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_TIN = ExtendedFluid.builder().fogColor(0x7EA4AAAA).tintColor(0x7EA4AAAA).name("molten_tin").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid.Entry MOLTEN_UNIVITE = ExtendedFluid.builder().fogColor(0x7EFF83DB).tintColor(0x7EFF83DB).name("molten_univite").group(AMItemGroups.ASTROMINE).customSprite(true).customHandler(true).build();
	public static final ExtendedFluid.Entry MOLTEN_LUNUM = ExtendedFluid.builder().fogColor(0x7E9ECCD3).tintColor(0x7E9ECCD3).name("molten_lunum").group(AMItemGroups.ASTROMINE).build();

	public static final ExtendedFluid.Entry OIL = ExtendedFluid.builder().fogColor(0x7e121212).tintColor(0x7e121212).damage(0).toxic(false).infinite(false).name("oil").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid.Entry FUEL = ExtendedFluid.builder().fogColor(0x7e968048).tintColor(0x7e968048).damage(0).toxic(false).infinite(false).name("fuel").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid.Entry BIOMASS = ExtendedFluid.builder().fogColor(0x7e6fda34).tintColor(0x7e6fda34).damage(0).toxic(false).infinite(false).name("biomass").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid.Entry OXYGEN = ExtendedFluid.builder().fogColor(0x7e159ef9).tintColor(0xff159ef9).damage(0).toxic(false).infinite(false).name("oxygen").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid.Entry HYDROGEN = ExtendedFluid.builder().fogColor(0x7eff0019).tintColor(0xffff0019).damage(0).toxic(false).infinite(false).name("hydrogen").group(AMItemGroups.ASTROMINE).build();
	
	public static void init() {
		TYPES.register(AMCommon.modEventBus());
		REGISTRY.register(AMCommon.modEventBus());
	}
	
	public static List<ExtendedFluid.Entry> entries() {
		return List.of(
				MOLTEN_AMETHYST,
				MOLTEN_ASTERITE,
				MOLTEN_BRONZE,
				MOLTEN_COPPER,
				MOLTEN_DIAMOND,
				MOLTEN_ELECTRUM,
				MOLTEN_EMERALD,
				MOLTEN_FOOLS_GOLD,
				MOLTEN_GALAXIUM,
				MOLTEN_GOLD,
				MOLTEN_IRON,
				MOLTEN_LAPIS,
				MOLTEN_LEAD,
				MOLTEN_METEORIC_STEEL,
				MOLTEN_METITE,
				MOLTEN_NETHERITE,
				MOLTEN_QUARTZ,
				MOLTEN_REDSTONE,
				MOLTEN_SILVER,
				MOLTEN_STEEL,
				MOLTEN_STELLUM,
				MOLTEN_TIN,
				MOLTEN_UNIVITE,
				MOLTEN_LUNUM,
				OIL,
				FUEL,
				BIOMASS,
				OXYGEN,
				HYDROGEN
		);
	}
	
	public static <T extends FluidType> DeferredHolder<FluidType, T> registerType(String name, Supplier<T> type) {
		return TYPES.register(name, type);
	}
	
	public static <T extends Fluid> DeferredHolder<Fluid, T> register(String name, Supplier<T> fluid) {
		return REGISTRY.register(name, fluid);
	}
}
