package com.github.chainmailstudios.astromine.foundations.common.criterion;

import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsCriteria;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MetiteOreUnderestimationCriterion extends AbstractCriterion<MetiteOreUnderestimationCriterion.Conditions> {
	private final Identifier id;

	public MetiteOreUnderestimationCriterion(Identifier id) {
		this.id = id;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new Conditions(this.id, extended);
	}

	public void trigger(ServerPlayerEntity player) {
		this.test(player, conditions -> true);
	}

	public static class Conditions extends AbstractCriterionConditions {
		public Conditions(Identifier id, EntityPredicate.Extended playerPredicate) {
			super(id, playerPredicate);
		}

		public static Conditions create() {
			return new Conditions(AstromineFoundationsCriteria.METITE_ORE_UNDERESTIMATION.id, EntityPredicate.Extended.EMPTY);
		}
	}
}
