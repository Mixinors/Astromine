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

import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesCriteria;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class DestroyRocketCriterion extends SimpleCriterionTrigger<DestroyRocketCriterion.Conditions> {
	public final ResourceLocation id;

	public DestroyRocketCriterion(ResourceLocation id) {
		this.id = id;
	}

	@Override
	protected DestroyRocketCriterion.Conditions createInstance(JsonObject obj, EntityPredicate.Composite playerPredicate, DeserializationContext predicateDeserializer) {
		if (obj.has("intentional"))
			return new Conditions(this.id, playerPredicate, obj.get("intentional").getAsBoolean());
		else return new Conditions(this.id, playerPredicate);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	public void trigger(ServerPlayer player, boolean intentional) {
		this.trigger(player, conditions -> conditions.matches(intentional));
	}

	public static class Conditions extends AbstractCriterionTriggerInstance {
		private final Boolean intentional;

		public Conditions(ResourceLocation id, EntityPredicate.Composite playerPredicate, Boolean intentional) {
			super(id, playerPredicate);
			this.intentional = intentional;
		}

		public Conditions(ResourceLocation id, EntityPredicate.Composite playerPredicate) {
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
			return new Conditions(AstromineDiscoveriesCriteria.DESTROY_ROCKET.getId(), EntityPredicate.Composite.ANY, intentional);
		}

		public static Conditions create() {
			return new Conditions(AstromineDiscoveriesCriteria.DESTROY_ROCKET.getId(), EntityPredicate.Composite.ANY);
		}
	}
}
