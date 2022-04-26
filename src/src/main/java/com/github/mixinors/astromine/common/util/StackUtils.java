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

package com.github.mixinors.astromine.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class StackUtils {
	public static boolean fits(ItemStack source, ItemStack target) {
		return target.getMaxCount() - target.getCount() >= source.getCount();
	}
	
	/** Attempts to merge two {@link ItemStack}s, returning a {@link Pair}
	 * with the results.
	 *
	 * The amount transferred is the {@link Math#min(int, int)} between
	 * their available space, our amount, and the specified amount.
	 * */
	public static Pair<ItemStack, ItemStack> merge(ItemStack source, ItemStack target) {
		var targetMax = target.getMaxCount();

		if (ItemStack.areItemsEqual(source, target) && ItemStack.areNbtEqual(source, target)) {
			var sourceCount = source.getCount();
			var targetCount = target.getCount();
			
			var targetAvailable = Math.max(0, targetMax - targetCount);

			target.increment(Math.min(sourceCount, targetAvailable));
			source.setCount(Math.max(sourceCount - targetAvailable, 0));
		} else {
			if (target.isEmpty() && !source.isEmpty()) {
				var targetCount = target.getCount();
				var targetAvailable = targetMax - targetCount;
				
				var sourceCount = source.getCount();

				target = source.copy();
				target.setCount(Math.min(sourceCount, targetAvailable));
				source.decrement(Math.min(sourceCount, targetAvailable));
			}
		}

		return new Pair<>(source, target);
	}

	/** Asserts whether the source {@link ItemStack} matches the second,
	 * and whether the target can fit the source, or not.
	 */
	public static boolean equalsAndFits(ItemStack source, ItemStack target) {
		return target.isEmpty() || ItemStack.areItemsEqual(source, target) && ItemStack.areNbtEqual(source, target) && target.getMaxCount() - target.getCount() >= source.getCount();
	}

	/** Weakly merges an {@link ItemStack} into another, returning the resulting target. */
	public static ItemStack into(ItemStack source, ItemStack target) {
		if (target.isEmpty()) {
			return source.copy();
		} else {
			target = target.copy();
			target.increment(source.getCount());
			return target;
		}
	}

	/** Asserts equality of {@link ItemStack} {@link NbtCompound} and {@link Item}. */
	public static boolean areItemsAndTagsEqual(ItemStack left, ItemStack right) {
		return ItemStack.areItemsEqual(left, right) && ItemStack.areNbtEqual(left, right);
	}

	/** Deserializes an {@link ItemStack} from a {@link JsonElement}. */
	public static ItemStack fromJson(JsonElement jsonElement) {
		if (!jsonElement.isJsonObject()) {
			if (jsonElement.isJsonPrimitive()) {
				var primitive = jsonElement.getAsJsonPrimitive();

				if (primitive.isString()) {
					return new ItemStack(Registry.ITEM.get(new Identifier(primitive.getAsString())));
				} else {
					return ItemStack.EMPTY;
				}
			} else {
				return ItemStack.EMPTY;
			}
		} else {
			return ShapedRecipe.outputFromJson(jsonElement.getAsJsonObject());
		}
	}

	/** Serializes the given {@link ItemStack} to a {@link JsonElement}. */
	public static JsonElement toJson(ItemStack stack) {
		var object = new JsonObject();
		object.addProperty("item", Registry.ITEM.getId(stack.getItem()).toString());
		object.addProperty("count", stack.getCount());
		return object;
	}

	/** Deserializes an {@link ItemStack} from a {@link ByteBuf}. */
	public static ItemStack fromPacket(PacketByteBuf buffer) {
		return buffer.readItemStack();
	}

	/** Serializes the given {@link ItemStack} to a {@link ByteBuf}. */
	public static void toPacket(PacketByteBuf buffer, ItemStack stack) {
		buffer.writeItemStack(stack);
	}
}
