/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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
import com.github.mixinors.astromine.common.criterion.DestroyRocketCriterion;
import com.github.mixinors.astromine.common.criterion.LaunchRocketCriterion;
import com.github.mixinors.astromine.common.criterion.MetiteOreUnderestimationCriterion;
import com.github.mixinors.astromine.common.criterion.ProperlyUseFireExtinguisherCriterion;
import com.github.mixinors.astromine.common.criterion.UseFireExtinguisherCriterion;
import me.shedaniel.architectury.registry.CriteriaTriggersRegistry;

import net.minecraft.advancement.criterion.Criterion;

import com.github.mixinors.astromine.common.criterion.TrickedPiglinCriterion;

public class AMCriteria {
	public static final TrickedPiglinCriterion TRICKED_PIGLIN = register(new TrickedPiglinCriterion(AMCommon.id("tricked_piglin")));
	
	public static final LaunchRocketCriterion LAUNCH_ROCKET = register(new LaunchRocketCriterion(AMCommon.id("launch_rocket")));
	public static final DestroyRocketCriterion DESTROY_ROCKET = register(new DestroyRocketCriterion(AMCommon.id("destroy_rocket")));
	
	public static final MetiteOreUnderestimationCriterion UNDERESTIMATE_METITE = register(new MetiteOreUnderestimationCriterion(AMCommon.id("underestimate_metite")));
	public static final UseFireExtinguisherCriterion USE_FIRE_EXTINGUISHER = register(new UseFireExtinguisherCriterion(AMCommon.id("use_fire_extinguisher")));
	public static final ProperlyUseFireExtinguisherCriterion PROPERLY_USE_FIRE_EXTINGUISHER = register(new ProperlyUseFireExtinguisherCriterion(AMCommon.id("properly_use_fire_extinguisher")));
	
	public static void init() {

	}

	public static <T extends Criterion<?>> T register(T criterion) {
		return CriteriaTriggersRegistry.register(criterion);
	}
}
