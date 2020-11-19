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

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.registry.AstromineCriteria;

import com.google.gson.JsonObject;

/**
 * A {@link Criterion} for tricking piglins by
 * giving them a gold-like substance, which is not,
 * in fact, gold.
 */
public class TrickedPiglinCriterion extends AbstractCriterion<TrickedPiglinCriterion.Conditions> {
	public final Identifier id;

	/** Instantiates a {@link TrickedPiglinCriterion}. */
	public TrickedPiglinCriterion(Identifier id) {
		this.id = id;
	}

	/** Reads {@link Conditions} from a {@link JsonObject}. */;
	@Override
	protected TrickedPiglinCriterion.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new Conditions(this.id, playerPredicate);
	}

	/** Returns this {@link Criterion}'s ID. */
	@Override
	public Identifier getId() {
		return id;
	}

	/** Triggers this {@link Criterion} for the given player. */
	public void trigger(ServerPlayerEntity player) {
		this.test(player, conditions -> true);
	}

	/**
	 * Conditions for {@link #trigger(ServerPlayerEntity)}.
	 */
	public static class Conditions extends AbstractCriterionConditions {
		/** Instantiates {@link Conditions}. */
		public Conditions(Identifier id, EntityPredicate.Extended playerPredicate) {
			super(id, playerPredicate);
		}

		/** Instantiates {@link Conditions}. */
		public static Conditions create() {
			return new Conditions(AstromineCriteria.TRICKED_PIGLIN.getId(), EntityPredicate.Extended.EMPTY);
		}
	}
}
