/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.screen.base;

import com.github.mixinors.astromine.client.gui.widget.FluidFilterWidget;
import com.github.mixinors.astromine.client.gui.widget.WidgetContext;
import com.github.mixinors.astromine.common.block.entity.storage.TankBlockEntity;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.network.PacketDistributor;

public class TankFluidFilterWidget extends FluidFilterWidget {
	private final AbstractContainerMenu menu;
	private final TankBlockEntity tank;
	private final Supplier<BlockPos> blockPos;
	
	public TankFluidFilterWidget(int x, int y, AbstractContainerMenu menu, TankBlockEntity tank, Supplier<BlockPos> blockPos) {
		super(x, y, tank::getFilter, () -> filterTooltip(tank));
		this.menu = menu;
		this.tank = tank;
		this.blockPos = blockPos;
	}
	
	@Override
	public boolean mouseClicked(WidgetContext context, double mouseX, double mouseY, int button) {
		if (!contains(context, mouseX, mouseY)) {
			return false;
		}
		
		var selectedFluid = FluidStack.EMPTY;
		
		if (button != 2) {
			var cursor = menu.getCarried();
			
			if (cursor.isEmpty()) {
				return true;
			}
			
			var itemFluidHandler = FluidUtil.getFluidHandler(cursor).orElse(null);
			
			if (itemFluidHandler == null) {
				return true;
			}
			
			for (var tankIndex = 0; tankIndex < itemFluidHandler.getTanks(); ++tankIndex) {
				var stack = itemFluidHandler.getFluidInTank(tankIndex);
				
				if (!stack.isEmpty()) {
					selectedFluid = stack.copyWithAmount(1);
					break;
				}
			}
			
			if (selectedFluid.isEmpty()) {
				return true;
			}
		}
		
		tank.setFilter(selectedFluid);
		PacketDistributor.sendToServer(new AMNetworking.TankFilterUpdatePayload(selectedFluid, blockPos.get()));
		return true;
	}
	
	private static List<Component> filterTooltip(TankBlockEntity tank) {
		var stack = tank.getFilter();
		var name = stack.isEmpty() ? Component.translatable("text.astromine.empty") : stack.getHoverName();
		return List.of(Component.translatable("text.astromine.filter"), name);
	}
}
