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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class AMItemGroups {
	private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AMCommon.MOD_ID);
	
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ASTROMINE = register("astromine", AMItems.ITEM);
	
	public static void init() {
		REGISTRY.register(AMCommon.modEventBus());
		AMCommon.modEventBus().addListener(AMItemGroups::buildContents);
	}
	
	public static DeferredHolder<CreativeModeTab, CreativeModeTab> register(String id, Supplier<? extends ItemLike> icon) {
		return REGISTRY.register(id, () -> CreativeModeTab.builder()
				.title(Component.translatable("itemGroup." + AMCommon.MOD_ID + "." + id))
				.icon(() -> new ItemStack(icon.get()))
				.build());
	}
	
	private static void buildContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() != ASTROMINE.getKey()) {
			return;
		}
		
		AMBlocks.BLOCKS.getEntries().forEach(block -> acceptIfPresent(event, new ItemStack(block.get().asItem())));
		AMItems.REGISTRY.getEntries().forEach(item -> acceptIfPresent(event, new ItemStack(item.get())));
	}
	
	private static void acceptIfPresent(BuildCreativeModeTabContentsEvent event, ItemStack stack) {
		if (isDisplayable(stack)) {
			event.accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}
	
	public static boolean isDisplayable(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() != Items.AIR && stack.getCount() == 1;
	}
}
