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

package com.github.chainmailstudios.astromine.advancement;

import com.github.chainmailstudios.astromine.registry.AstromineCriteria;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * A {@link CriterionTrigger} for tricking piglins by
 * giving them a gold-like substance, which is not,
 * in fact, gold.
 */
public class TrickedPiglinCriterion extends SimpleCriterionTrigger<TrickedPiglinCriterion.Conditions> {
	public final ResourceLocation id;

	/** Instantiates a {@link TrickedPiglinCriterion}. */
	public TrickedPiglinCriterion(ResourceLocation id) {
		this.id = id;
	}

	/** Reads {@link Conditions} from a {@link JsonObject}. */;
	@Override
	protected TrickedPiglinCriterion.Conditions createInstance(JsonObject obj, EntityPredicate.Composite playerPredicate, DeserializationContext predicateDeserializer) {
		if (obj.has("successful"))
			return new Conditions(this.id, playerPredicate, obj.get("successful").getAsBoolean());
		else return new Conditions(this.id, playerPredicate);
	}

	/** Returns this {@link CriterionTrigger}'s ID. */
	@Override
	public ResourceLocation getId() {
		return id;
	}

	/** Triggers this {@link CriterionTrigger} for the given player with the given parameter. */
	public void trigger(ServerPlayer player, boolean successful) {
		this.trigger(player, conditions -> conditions.matches(successful));
	}

	/**
	 * Conditions for {@link #trigger(ServerPlayer, boolean)}.
	 */
	public static class Conditions extends AbstractCriterionTriggerInstance {
		private final Boolean successful;

		/** Instantiates {@link Conditions}. */
		public Conditions(ResourceLocation id, EntityPredicate.Composite playerPredicate, Boolean successful) {
			super(id, playerPredicate);
			this.successful = successful;
		}

		/** Instantiates {@link Conditions}. */
		public Conditions(ResourceLocation id, EntityPredicate.Composite playerPredicate) {
			super(id, playerPredicate);
			this.successful = null;
		}

		/** Returns whether the Piglin was successfully tricked. */
		public Boolean successful() {
			return successful;
		}

		/** Returns whether this condition should be fulfilled by the given parameter. */
		public boolean matches(boolean successful) {
			return successful() == null || successful == successful();
		}

		/** Instantiates {@link Conditions}. */
		public static Conditions create(boolean successful) {
			return new Conditions(AstromineCriteria.TRICKED_PIGLIN.getId(), EntityPredicate.Composite.ANY, successful);
		}

		/** Instantiates {@link Conditions}. */
		public static Conditions create() {
			return new Conditions(AstromineCriteria.TRICKED_PIGLIN.getId(), EntityPredicate.Composite.ANY);
		}
	}
}
