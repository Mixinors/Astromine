package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.text.Text;

import com.google.gson.JsonObject;
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
