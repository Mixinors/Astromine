package com.github.mixinors.astromine.common.criterion;

import com.github.mixinors.astromine.registry.common.AMCriteria;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * A {@link CriterionTrigger} for tricking piglins by giving them a gold-like substance, which is not, in fact, gold.
 */
public class TrickedPiglinCriterion extends SimpleCriterionTrigger<TrickedPiglinCriterion.Conditions> implements IdentifiedCriterion {
	private final ResourceLocation id;
	
	public TrickedPiglinCriterion(ResourceLocation id) {
		this.id = id;
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public ResourceLocation getId() {
		return id;
	}
	
	public void trigger(ServerPlayer player, boolean successful) {
		this.trigger(player, conditions -> conditions.matches(successful));
	}
	
	public record Conditions(Optional<ContextAwarePredicate> player, Optional<Boolean> successful) implements SimpleInstance {
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				Codec.BOOL.optionalFieldOf("successful").forGetter(Conditions::successful)
		).apply(instance, Conditions::new));
		
		public boolean matches(boolean successful) {
			return this.successful.isEmpty() || this.successful.get() == successful;
		}
		
		public static Criterion<Conditions> create(boolean successful) {
			return AMCriteria.TRICKED_PIGLIN.createCriterion(new Conditions(Optional.empty(), Optional.of(successful)));
		}
		
		public static Criterion<Conditions> create() {
			return AMCriteria.TRICKED_PIGLIN.createCriterion(new Conditions(Optional.empty(), Optional.empty()));
		}
	}
}
