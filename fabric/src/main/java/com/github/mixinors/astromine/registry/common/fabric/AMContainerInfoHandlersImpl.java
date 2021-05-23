package com.github.mixinors.astromine.registry.common.fabric;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.screenhandler.*;
import com.github.mixinors.astromine.common.screenhandler.base.block.ComponentBlockEntityScreenHandler;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.rei.server.*;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Collectors;

public class AMContainerInfoHandlersImpl implements Runnable{
	public static final Identifier TRITURATING = AMCommon.id("triturating");
	public static final Identifier ELECTRIC_SMELTING = AMCommon.id("electric_smelting");
	public static final Identifier SOLID_GENERATING = AMCommon.id("solid_generating");
	public static final Identifier PRESSING = AMCommon.id("pressing");
	public static final Identifier ALLOY_SMELTING = AMCommon.id("alloy_smelting");
	
	public static void postInit() {}
	
	@Override
	public void run() {
		ContainerInfoHandler.registerContainerInfo(TRITURATING, new SimpleContainerInfo<>(TrituratorScreenHandler.class, 1, 1, 1));
		ContainerInfoHandler.registerContainerInfo(ELECTRIC_SMELTING, new SimpleContainerInfo<>(ElectricFurnaceScreenHandler.class, 1, 1, 1));
		ContainerInfoHandler.registerContainerInfo(SOLID_GENERATING, new SimpleContainerInfo<>(SolidGeneratorScreenHandler.class, 1, 1, 0));
		ContainerInfoHandler.registerContainerInfo(PRESSING, new SimpleContainerInfo<>(PressScreenHandler.class, 1, 1, 1));
		ContainerInfoHandler.registerContainerInfo(ALLOY_SMELTING, new SimpleContainerInfo<>(AlloySmelterScreenHandler.class, 1, 2, 0, 1));
	}
	
	private static class SimpleContainerInfo<T extends ComponentBlockEntityScreenHandler> implements ContainerInfo<T> {
		private final Class<T> clazz;
		private final int width;
		private final int height;
		private final IntList gridStacks;
		
		public SimpleContainerInfo(Class<T> clazz, int width, int height, int... gridStacks) {
			this.clazz = clazz;
			this.width = width;
			this.height = height;
			this.gridStacks = new IntArrayList(gridStacks);
		}
		
		@Override
		public Class<? extends ScreenHandler> getContainerClass() {
			return clazz;
		}
		
		@Override
		public List<StackAccessor> getGridStacks(ContainerContext<T> context) {
			return gridStacks.stream().map(slotIndex -> new InventoryStackAccessor((Inventory) context.getContainer().getBlockEntity(), slotIndex)).collect(Collectors.toList());
		}
		
		@Override
		public int getCraftingResultSlotIndex(T t) {
			return Integer.MAX_VALUE;
		}
		
		@Override
		public int getCraftingWidth(T t) {
			return width;
		}
		
		@Override
		public int getCraftingHeight(T t) {
			return height;
		}
	}
}
