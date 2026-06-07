package com.github.mixinors.astromine.common.criterion;

import com.github.mixinors.astromine.registry.common.AMCriteria;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class DestroyRocketCriterion extends SimpleCriterionTrigger<DestroyRocketCriterion.Conditions> implements IdentifiedCriterion {
	private final ResourceLocation id;
	
	public DestroyRocketCriterion(ResourceLocation id) {
		this.id = id;
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public ResourceLocation getId() {
		return id;
	}
	
	public void trigger(ServerPlayer player, boolean intentional) {
		this.trigger(player, conditions -> conditions.matches(intentional));
	}
	
	public record Conditions(Optional<ContextAwarePredicate> player, Optional<Boolean> intentional) implements SimpleInstance {
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				Codec.BOOL.optionalFieldOf("intentional").forGetter(Conditions::intentional)
		).apply(instance, Conditions::new));
		
		public boolean matches(boolean intentional) {
			return this.intentional.isEmpty() || this.intentional.get() == intentional;
		}
		
		public static Criterion<Conditions> create(boolean intentional) {
			return AMCriteria.DESTROY_ROCKET.createCriterion(new Conditions(Optional.empty(), Optional.of(intentional)));
		}
		
		public static Criterion<Conditions> create() {
			return AMCriteria.DESTROY_ROCKET.createCriterion(new Conditions(Optional.empty(), Optional.empty()));
		}
	}
}
