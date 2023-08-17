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

package com.github.mixinors.astromine.common.criterion;

import com.github.mixinors.astromine.registry.common.AMCriteria;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class LaunchRocketCriterion extends AbstractCriterion<LaunchRocketCriterion.Conditions> {
	public final Identifier id;
	
	public LaunchRocketCriterion(Identifier id) {
		this.id = id;
	}
	
	@Override
	protected LaunchRocketCriterion.Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new Conditions(this.id, playerPredicate);
	}
	
	@Override
	public Identifier getId() {
		return id;
	}
	
	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, conditions -> true);
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		public Conditions(Identifier id, LootContextPredicate playerPredicate) {
			super(id, playerPredicate);
		}
		
		public static Conditions create() {
			return new Conditions(AMCriteria.LAUNCH_ROCKET.getId(), LootContextPredicate.EMPTY);
		}
	}
}
