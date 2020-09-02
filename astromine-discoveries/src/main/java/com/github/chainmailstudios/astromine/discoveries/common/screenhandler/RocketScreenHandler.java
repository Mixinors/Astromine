package com.github.chainmailstudios.astromine.discoveries.common.screenhandler;

import com.github.chainmailstudios.astromine.common.screenhandler.base.entity.ComponentEntityFluidScreenHandler;
import com.github.chainmailstudios.astromine.discoveries.common.entity.base.RocketEntity;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesCommonPackets;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesScreenHandlers;
import com.github.vini2003.blade.common.data.Position;
import com.github.vini2003.blade.common.data.Size;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import com.github.vini2003.blade.common.widget.base.TextWidget;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class RocketScreenHandler extends ComponentEntityFluidScreenHandler {
	private TextWidget fuelTextWidget;

	public RocketScreenHandler(int syncId, PlayerEntity player, int entityId) {
		super(AstromineDiscoveriesScreenHandlers.ROCKET, syncId, player, entityId);
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		ButtonWidget launchButtonWidget = new ButtonWidget((widget) -> {
			syncEntity.getDataTracker().set(RocketEntity.IS_RUNNING, true);

			ClientSidePacketRegistry.INSTANCE.sendToServer(AstromineDiscoveriesCommonPackets.ROCKET_LAUNCH, AstromineDiscoveriesCommonPackets.ofRocketLaunch(syncEntity.getEntityId()));

			return null;
		});

		launchButtonWidget.setPosition(Position.of(fluidBar.getX() + fluidBar.getWidth() + 9, fluidBar.getY()));
		launchButtonWidget.setSize(Size.of(72, 18));
		launchButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.launch"));

		fuelTextWidget = new TextWidget();


		mainTab.addWidget(launchButtonWidget);
	}

	public void setFuelText(Text text) {
		this.fuelTextWidget.setText(text);
	}
}
