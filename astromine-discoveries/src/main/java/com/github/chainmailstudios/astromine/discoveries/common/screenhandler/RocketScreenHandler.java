package com.github.chainmailstudios.astromine.discoveries.common.screenhandler;

import com.github.chainmailstudios.astromine.common.screenhandler.base.entity.ComponentEntityFluidInventoryScreenHandler;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.discoveries.common.entity.base.RocketEntity;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesScreenHandlers;
import com.github.vini2003.blade.common.data.Position;
import com.github.vini2003.blade.common.data.Size;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import com.github.vini2003.blade.common.widget.base.TextWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class RocketScreenHandler extends ComponentEntityFluidInventoryScreenHandler {
	private TextWidget fuelTextWidget;

	public RocketScreenHandler(int syncId, PlayerEntity player, int entityId) {
		super(AstromineDiscoveriesScreenHandlers.ROCKET, syncId, player, entityId);
	}

	@Override
	public ItemStack getSymbol() {
		return new ItemStack(AstromineDiscoveriesItems.PRIMITIVE_ROCKET);
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		ButtonWidget launchButtonWidget = new ButtonWidget((widget) -> {
			if (entity.getFluidComponent().getVolume(0).biggerThan(Fraction.empty()))
				entity.getDataTracker().set(RocketEntity.IS_RUNNING, true);

			return null;
		});

		launchButtonWidget.setPosition(Position.of(mainTab, 3 + 4, 11));
		launchButtonWidget.setSize(Size.of(48, 18));
		launchButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.launch"));
		launchButtonWidget.setDisabled(() -> entity.getDataTracker().get(RocketEntity.IS_RUNNING) || entity.getFluidComponent().getVolume(0).smallerOrEqualThan(Fraction.empty()));

		ButtonWidget abortButtonWidget = new ButtonWidget((widget) -> {
			((RocketEntity) entity).tryDisassemble();

			return null;
		});

		abortButtonWidget.setPosition(Position.of(mainTab, 3 + 4, 11 + fluidBar.getHeight() - 18));
		abortButtonWidget.setSize(Size.of(48, 18));
		abortButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.destroy"));

		fluidBar.setPosition(Position.of(width / 2F - fluidBar.getWidth() / 2F + 2, fluidBar.getY()));

		SlotWidget input = new SlotWidget(0, entity);
		input.setPosition(Position.of(fluidBar, -18 - 3, 0));
		input.setSize(Size.of(18, 18));

		SlotWidget output = new SlotWidget(1, entity);
		output.setPosition(Position.of(fluidBar, -18 - 3, fluidBar.getHeight() - 18));
		output.setSize(Size.of(18, 18));

		fuelTextWidget = new TextWidget();

		mainTab.addWidget(launchButtonWidget);
		mainTab.addWidget(abortButtonWidget);

		mainTab.addWidget(input);
		mainTab.addWidget(output);
	}

	public void setFuelText(Text text) {
		this.fuelTextWidget.setText(text);
	}
}
