package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.OneTimeModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.SetModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.ModelStateData;

import java.util.ArrayList;
import java.util.List;

public abstract class AstromineModelStateGenerators {
	private final List<SetModelStateGenerator> SET_GENERATORS = new ArrayList<>();
	private final List<OneTimeModelStateGenerator> ONE_TIME_GENERATORS = new ArrayList<>();

	public SetModelStateGenerator register(SetModelStateGenerator generator) {
		SET_GENERATORS.add(generator);
		return generator;
	}

	public OneTimeModelStateGenerator register(OneTimeModelStateGenerator generator) {
		ONE_TIME_GENERATORS.add(generator);
		return generator;
	}

	public void generateModelStates(ModelStateData modelStates) {
		AstromineMaterialSets.getMaterialSets().forEach((set) -> generateSetModelStates(modelStates, set));
		generateOneTimeRecipes(modelStates);
	}

	private void generateSetModelStates(ModelStateData modelStates, MaterialSet set) {
		SET_GENERATORS.forEach((generator) -> {
			try {
				if (set.shouldGenerate(generator)) {
					generator.generate(modelStates, set);
					AstromineCommon.LOGGER.info("Model/State generation of " + set.getName() + " succeeded, with generator " + generator.getGeneratorName() + ".");
				}
			} catch (Exception exception) {
				AstromineCommon.LOGGER.error("Model/State generation of " + set.getName() + " failed, with generator " + generator.getGeneratorName() + ".");
				AstromineCommon.LOGGER.error(exception.getMessage());
			}
		});
	}

	private void generateOneTimeRecipes(ModelStateData modelStates) {
		ONE_TIME_GENERATORS.forEach((generator) -> generator.generate(modelStates));
	}
}
