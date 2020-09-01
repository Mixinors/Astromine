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

package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.common.criterion.MetiteOreUnderestimationCriterion;
import com.github.chainmailstudios.astromine.foundations.common.criterion.ProperlyUseFireExtinguisherCriterion;
import com.github.chainmailstudios.astromine.foundations.common.criterion.UseFireExtinguisherCriterion;
import com.github.chainmailstudios.astromine.registry.AstromineCriteria;

public class AstromineFoundationsCriteria extends AstromineCriteria {
	public static final MetiteOreUnderestimationCriterion UNDERESTIMATE_METITE = register(new MetiteOreUnderestimationCriterion(AstromineCommon.identifier("underestimate_metite")));
	public static final UseFireExtinguisherCriterion USE_FIRE_EXTINGUISHER = register(new UseFireExtinguisherCriterion(AstromineCommon.identifier("use_fire_extinguisher")));
	public static final ProperlyUseFireExtinguisherCriterion PROPERLY_USE_FIRE_EXTINGUISHER = register(new ProperlyUseFireExtinguisherCriterion(AstromineCommon.identifier("properly_use_fire_extinguisher")));

	public static void initialize() {

	}
}
