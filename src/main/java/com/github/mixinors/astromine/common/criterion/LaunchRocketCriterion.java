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

public class LaunchRocketCriterion extends SimpleCriterionTrigger<LaunchRocketCriterion.Conditions> implements IdentifiedCriterion {
	public final ResourceLocation id;
	
	public LaunchRocketCriterion(ResourceLocation id) {
		this.id = id;
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public ResourceLocation getId() {
		return id;
	}
	
	public void trigger(ServerPlayer player) {
		this.trigger(player, conditions -> true);
	}
	
	public record Conditions(Optional<ContextAwarePredicate> player) implements SimpleInstance {
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player)
		).apply(instance, Conditions::new));
		
		public static Criterion<Conditions> create() {
			return AMCriteria.LAUNCH_ROCKET.createCriterion(new Conditions(Optional.empty()));
		}
	}
}
