/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.recipe.ingredient;

import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidVariantStorage;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

public final class FluidIngredient {
	private static final String FLUID_KEY = "fluid";
	private static final String AMOUNT_KEY = "amount";
	private static final String TAG_KEY = "tag";
	
	private final Entry entry;
	
	@Nullable
	private FluidStack[] matchingStacks;
	
	public FluidIngredient(Entry entry) {
		this.entry = entry;
	}
	
	public FluidIngredient(FluidStack stack, long amount) {
		this.entry = new StackEntry(stack, amount);
	}
	
	public FluidIngredient(Fluid fluid, long amount) {
		this(new FluidStack(fluid, clampAmount(amount)), amount);
	}
	
	public boolean test(SimpleFluidVariantStorage testStorage) {
		if (testStorage.isResourceBlank()) {
			return false;
		}
		
		return test(testStorage.getResource(), testStorage.getAmount());
	}
	
	public boolean test(FluidStack testStack, Long testAmount) {
		return entry.test(testStack, testAmount);
	}
	
	public boolean testStack(FluidStack testStack) {
		return entry.testStack(testStack);
	}
	
	public Entry getEntry() {
		return entry;
	}
	
	public long getAmount() {
		return entry.getAmount();
	}
	
	public FluidStack[] getMatchingStacks() {
		this.cacheMatchingStacks();
		
		return this.matchingStacks;
	}
	
	private void cacheMatchingStacks() {
		if (this.matchingStacks == null) {
			this.matchingStacks = entry.getStacks().stream().distinct().toArray(FluidStack[]::new);
		}
	}
	
	public static FluidIngredient fromJson(JsonElement json) {
		if (json.isJsonPrimitive()) {
			var fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(json.getAsString()));
			
			return new FluidIngredient(new StackEntry(new FluidStack(fluid, FluidType.BUCKET_VOLUME)));
		}
		
		if (json.isJsonObject()) {
			var jsonObject = json.getAsJsonObject();
			
			if (jsonObject.has(FLUID_KEY)) {
				var fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(jsonObject.get(FLUID_KEY).getAsString()));
				var amount = jsonObject.has(AMOUNT_KEY) ? jsonObject.get(AMOUNT_KEY).getAsLong() : FluidType.BUCKET_VOLUME;
				
				return new FluidIngredient(new StackEntry(new FluidStack(fluid, clampAmount(amount)), amount));
			}
			
			if (jsonObject.has(TAG_KEY)) {
				var tag = AMTagKeys.createFluidTag(ResourceLocation.parse(jsonObject.get(TAG_KEY).getAsString()));
				var amount = jsonObject.has(AMOUNT_KEY) ? jsonObject.get(AMOUNT_KEY).getAsLong() : FluidType.BUCKET_VOLUME;
				
				return new FluidIngredient(new TagEntry(tag, amount));
			}
		}
		
		return null;
	}
	
	public static JsonObject toJson(FluidIngredient ingredient) {
		var jsonObject = new JsonObject();
		
		if (ingredient.entry instanceof StackEntry stackEntry) {
			jsonObject.addProperty(FLUID_KEY, BuiltInRegistries.FLUID.getKey(stackEntry.requiredStack.getFluid()).toString());
			jsonObject.addProperty(AMOUNT_KEY, stackEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			jsonObject.addProperty(TAG_KEY, tagEntry.requiredTag.location().toString());
			jsonObject.addProperty(AMOUNT_KEY, tagEntry.requiredAmount);
		}
		
		return jsonObject;
	}
	
	public static FluidIngredient fromPacket(FriendlyByteBuf buf) {
		var entryType = buf.readUtf();
		var entryTypeId = ResourceLocation.parse(buf.readUtf());
		var entryAmount = buf.readLong();
		
		if (entryType.equals(FLUID_KEY)) {
			var fluid = BuiltInRegistries.FLUID.get(entryTypeId);
			
			return new FluidIngredient(new StackEntry(new FluidStack(fluid, clampAmount(entryAmount)), entryAmount));
		}
		
		if (entryType.equals(TAG_KEY)) {
			return new FluidIngredient(new TagEntry(AMTagKeys.createFluidTag(entryTypeId), entryAmount));
		}
		
		return null;
	}
	
	public static void toPacket(FriendlyByteBuf buf, FluidIngredient ingredient) {
		if (ingredient.entry instanceof StackEntry stackEntry) {
			buf.writeUtf(FLUID_KEY);
			buf.writeUtf(BuiltInRegistries.FLUID.getKey(stackEntry.requiredStack.getFluid()).toString());
			buf.writeLong(stackEntry.requiredAmount);
		}
		
		if (ingredient.entry instanceof TagEntry tagEntry) {
			buf.writeUtf(TAG_KEY);
			buf.writeUtf(tagEntry.requiredTag.location().toString());
			buf.writeLong(tagEntry.requiredAmount);
		}
	}
	
	private static int clampAmount(long amount) {
		return (int) Math.max(0L, Math.min(amount, Integer.MAX_VALUE));
	}
	
	public static abstract class Entry implements BiPredicate<FluidStack, Long> {
		public abstract long getAmount();
		
		public abstract Collection<FluidStack> getStacks();
		
		public abstract boolean testStack(FluidStack testStack);
	}
	
	public static class StackEntry extends Entry {
		private final FluidStack requiredStack;
		private final long requiredAmount;
		
		public StackEntry(FluidStack stack) {
			this(stack, stack.isEmpty() ? 0L : stack.getAmount());
		}
		
		public StackEntry(FluidStack stack, long amount) {
			this.requiredStack = stack.isEmpty() ? FluidStack.EMPTY : stack.copyWithAmount(1);
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(FluidStack testStack, Long testAmount) {
			return testStack(testStack) && testAmount >= requiredAmount;
		}
		
		@Override
		public boolean testStack(FluidStack testStack) {
			return FluidStack.isSameFluidSameComponents(testStack, requiredStack);
		}
		
		@Override
		public long getAmount() {
			return requiredAmount;
		}
		
		@Override
		public Collection<FluidStack> getStacks() {
			return ImmutableList.of(requiredStack.copyWithAmount(clampAmount(requiredAmount)));
		}
	}
	
	public static class TagEntry extends Entry {
		private List<FluidStack> requiredStacks;
		
		private final TagKey<Fluid> requiredTag;
		private final long requiredAmount;
		
		public TagEntry(TagKey<Fluid> tag) {
			this(tag, FluidType.BUCKET_VOLUME);
		}
		
		public TagEntry(TagKey<Fluid> tag, long amount) {
			this.requiredTag = tag;
			this.requiredAmount = amount;
		}
		
		@Override
		public boolean test(FluidStack testStack, Long testAmount) {
			return testStack(testStack) && testAmount >= requiredAmount;
		}
		
		@Override
		public boolean testStack(FluidStack testStack) {
			return !testStack.isEmpty() && testStack.getFluid().is(requiredTag);
		}
		
		@Override
		public long getAmount() {
			return requiredAmount;
		}
		
		@Override
		public Collection<FluidStack> getStacks() {
			if (requiredStacks == null) {
				var builder = ImmutableList.<FluidStack>builder();
				
				for (var entry : BuiltInRegistries.FLUID.getTagOrEmpty(requiredTag)) {
					var fluid = entry.value();
					
					if (fluid != Fluids.EMPTY && fluid.isSource(fluid.defaultFluidState())) {
						builder.add(new FluidStack(fluid, clampAmount(requiredAmount)));
					}
				}
				
				requiredStacks = builder.build();
			}
			
			return requiredStacks;
		}
	}
}
