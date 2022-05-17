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
import com.github.mixinors.astromine.common.util.FluidUtils;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

// maybe we could add a way to melt gems and infuse
// machine chassis with them? idk that just came up

// also maybe we add melting quartz into crystals
// and turning that into chips? sounds cool
public class AMFluids {
	public static final ExtendedFluid MOLTEN_AMETHYST = ExtendedFluid.builder().fogColor(0x7EB18CF0).tintColor(0x7EB18CF0).name("molten_amethyst").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_ASTERITE = ExtendedFluid.builder().fogColor(0x7EAD2346).tintColor(0x7EAD2346).name("molten_asterite").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_BRONZE = ExtendedFluid.builder().fogColor(0x7ECEA662).tintColor(0x7ECEA662).name("molten_bronze").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_COPPER = ExtendedFluid.builder().fogColor(0x7EBF5935).tintColor(0x7EBF5935).name("molten_copper").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_DIAMOND = ExtendedFluid.builder().fogColor(0x7E9FF8E5).tintColor(0x7E9FF8E5).name("molten_diamond").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_ELECTRUM = ExtendedFluid.builder().fogColor(0x7EF7ECA0).tintColor(0x7EF7ECA0).name("molten_electrum").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_EMERALD = ExtendedFluid.builder().fogColor(0x7E80F3AB).tintColor(0x7E80F3AB).name("molten_emerald").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_FOOLS_GOLD = ExtendedFluid.builder().fogColor(0x7EC4994E).tintColor(0x7EC4994E).name("molten_fools_gold").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_GALAXIUM = ExtendedFluid.builder().fogColor(0x7E4F2668).tintColor(0x7E4F2668).name("molten_galaxium").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_GOLD = ExtendedFluid.builder().fogColor(0x7EF7D349).tintColor(0x7EF7D349).name("molten_gold").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_IRON = ExtendedFluid.builder().fogColor(0x7EA6A6A6).tintColor(0x7EA6A6A6).name("molten_iron").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_LAPIS = ExtendedFluid.builder().fogColor(0x7E2857B3).tintColor(0x7E2857B3).name("molten_lapis").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_LEAD = ExtendedFluid.builder().fogColor(0x7E4A495C).tintColor(0x7E4A495C).name("molten_lead").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_METEORIC_STEEL = ExtendedFluid.builder().fogColor(0x7E9672AB).tintColor(0x7E9672AB).name("molten_meteoric_steel").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_METITE = ExtendedFluid.builder().fogColor(0x7ED349F7).tintColor(0x7ED349F7).name("molten_metite").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_NETHERITE = ExtendedFluid.builder().fogColor(0x7E4B4042).tintColor(0x7E4B4042).name("molten_netherite").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_QUARTZ = ExtendedFluid.builder().fogColor(0x7EDAD1C4).tintColor(0x7EDAD1C4).name("molten_quartz").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_REDSTONE = ExtendedFluid.builder().fogColor(0x7EA20D00).tintColor(0x7EA20D00).name("molten_redstone").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_SILVER = ExtendedFluid.builder().fogColor(0x7EC0D9D9).tintColor(0x7EC0D9D9).name("molten_silver").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_STEEL = ExtendedFluid.builder().fogColor(0x7E81898A).tintColor(0x7E81898A).name("molten_steel").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_STELLUM = ExtendedFluid.builder().fogColor(0x7EFCC181).tintColor(0x7EFCC181).name("molten_stellum").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_TIN = ExtendedFluid.builder().fogColor(0x7EA4AAAA).tintColor(0x7EA4AAAA).name("molten_tin").group(AMItemGroups.ASTROMINE).build();
	public static final ExtendedFluid MOLTEN_UNIVITE = ExtendedFluid.builder().fogColor(0x7EFF83DB).tintColor(0x7EFF83DB).name("molten_univite").group(AMItemGroups.ASTROMINE).customSprite(true).customHandler(true).build();
	
	public static final ExtendedFluid OIL = ExtendedFluid.builder().fogColor(0x7e121212).tintColor(0x7e121212).damage(0).toxic(false).infinite(false).name("oil").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid FUEL = ExtendedFluid.builder().fogColor(0x7e968048).tintColor(0x7e968048).damage(0).toxic(false).infinite(false).name("fuel").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid BIOMASS = ExtendedFluid.builder().fogColor(0x7e6fda34).tintColor(0x7e6fda34).damage(0).toxic(false).infinite(false).name("biomass").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid OXYGEN = ExtendedFluid.builder().fogColor(0x7e159ef9).tintColor(0xff159ef9).damage(0).toxic(false).infinite(false).name("oxygen").group(AMItemGroups.ASTROMINE).build();
	
	public static final ExtendedFluid HYDROGEN = ExtendedFluid.builder().fogColor(0x7eff0019).tintColor(0xffff0019).damage(0).toxic(false).infinite(false).name("hydrogen").group(AMItemGroups.ASTROMINE).build();
	
	public static void init() {
		if (Platform.getEnv() == EnvType.CLIENT) {
			FluidUtils.registerUniviteFluid();
		}
	}
	
	public static <T extends Fluid> T register(String name, T fluid) {
		return Registry.register(Registry.FLUID, AMCommon.id(name), fluid);
	}
}
