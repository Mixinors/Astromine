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

package com.github.chainmailstudios.astromine.discoveries.common.advancement;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesCriteria;

import com.google.gson.JsonObject;

public class DestroyRocketCriterion extends AbstractCriterion<DestroyRocketCriterion.Conditions> {
	public final Identifier id;

	public DestroyRocketCriterion(Identifier id) {
		this.id = id;
	}

	@Override
	protected DestroyRocketCriterion.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		if (obj.has("intentional"))
			return new Conditions(this.id, playerPredicate, obj.get("intentional").getAsBoolean());
		else return new Conditions(this.id, playerPredicate);
	}

	@Override
	public Identifier getId() {
		return id;
	}

	public void trigger(ServerPlayerEntity player, boolean intentional) {
		this.test(player, conditions -> conditions.matches(intentional));
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final Boolean intentional;

		public Conditions(Identifier id, EntityPredicate.Extended playerPredicate, Boolean intentional) {
			super(id, playerPredicate);
			this.intentional = intentional;
		}

		public Conditions(Identifier id, EntityPredicate.Extended playerPredicate) {
			super(id, playerPredicate);
			this.intentional = null;
		}

		public boolean matches(boolean intentional) {
			return intentional() == null || intentional == intentional();
		}

		public Boolean intentional() {
			return intentional;
		}

		public static Conditions create(boolean intentional) {
			return new Conditions(AstromineDiscoveriesCriteria.DESTROY_ROCKET.getId(), EntityPredicate.Extended.EMPTY, intentional);
		}
	}
}
