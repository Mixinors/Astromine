package com.github.chainmailstudios.astromine.client.rei.sorting;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.recipe.SortingRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.utils.CollectionUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SortingDisplay implements RecipeDisplay {
    private final List<List<EntryStack>> inputs;
    private final List<EntryStack> outputs;
    private final int timeRequired;
    private final Fraction energyRequired;
    
    public SortingDisplay(SortingRecipe recipe) {
        this(
                Collections.singletonList(CollectionUtils.map(recipe.getInput().getMatchingStacksClient(), EntryStack::create)),
                Collections.singletonList(EntryStack.create(recipe.getOutput())),
                recipe.getTimeTotal(),
                recipe.getEnergyTotal().copy()
        );
    }
    
    public SortingDisplay(List<List<EntryStack>> inputs, List<EntryStack> outputs, int timeRequired, Fraction energyRequired) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.timeRequired = timeRequired;
        this.energyRequired = energyRequired;
    }
    
    @Override
    public List<List<EntryStack>> getInputEntries() {
        return inputs;
    }
    
    @Override
    public List<EntryStack> getOutputEntries() {
        return outputs;
    }
    
    public int getTimeRequired() {
        return timeRequired;
    }
    
    public Fraction getEnergyRequired() {
        return energyRequired;
    }
    
    @Override
    public Identifier getRecipeCategory() {
        return AstromineREIPlugin.SORTING;
    }
}
