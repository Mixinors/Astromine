package com.github.chainmailstudios.astromine.advancement;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.registry.AstromineCriteria;

import com.google.gson.JsonObject;

public class LaunchRocketCriterion extends AbstractCriterion<LaunchRocketCriterion.Conditions> {
	public final Identifier id;

	public LaunchRocketCriterion(Identifier id) {
		this.id = id;
	}

	@Override
	protected LaunchRocketCriterion.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new Conditions(this.id, playerPredicate);
	}

	@Override
	public Identifier getId() {
		return id;
	}

	public void trigger(ServerPlayerEntity player) {
		this.test(player, conditions -> true);
	}

	public static class Conditions extends AbstractCriterionConditions {
		public Conditions(Identifier id, EntityPredicate.Extended playerPredicate) {
			super(id, playerPredicate);
		}

		public static Conditions create() {
			return new Conditions(AstromineCriteria.LAUNCH_ROCKET.getId(), EntityPredicate.Extended.EMPTY);
		}
	}
}
