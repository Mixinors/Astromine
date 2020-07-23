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
package com.github.chainmailstudios.astromine.common.utilities;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public class StackUtilities extends spinnery.common.utility.StackUtilities {
	public static boolean areEqual(List<ItemStack> stackListA, List<ItemStack> stackListB) {
		for (ItemStack stackA : stackListA) {
			boolean found = false;
			for (ItemStack stackB : stackListB) {
				if (ItemStack.areEqual(stackA, stackB)) {
					found = true;
				}
				if (found) {
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	public static ItemStack fromPacket(PacketByteBuf buffer) {
		return buffer.readItemStack();
	}

	public static void toPacket(PacketByteBuf buffer, ItemStack stack) {
		buffer.writeItemStack(stack);
	}

	public static ItemStack fromJson(JsonObject jsonObject) {
		return ShapedRecipe.getItemStack(jsonObject);
	}

	public static ItemStack withLore(ItemStack stack, Collection<Text> texts) {
		List<Text> entries = (List<Text>) texts;

		ListTag loreListTag = new ListTag();

		entries.forEach(text -> loreListTag.add(StringTag.of(Text.Serializer.toJson(text))));

		CompoundTag displayTag = stack.getOrCreateTag().getCompound("display");

		displayTag.put("Lore", loreListTag);

		CompoundTag stackTag = stack.getOrCreateTag();

		stackTag.put("display", displayTag);

		stack.setTag(stackTag);

		return stack;
	}
}
