package com.github.chainmailstudios.astromine.common.widget;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;

import java.util.function.Supplier;

public class WEnergyVolumeFractionalVerticalBar extends WFractionalVerticalBar {
	private Supplier<EnergyVolume> volume;

	private final Identifier ENERGY_BACKGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_background.png");
	private final Identifier ENERGY_FOREGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_foreground.png");


	public WEnergyVolumeFractionalVerticalBar() {
		setUnit(new TranslatableText("text.astromine.energy"));
	}

	@Override
	public Identifier getBackgroundTexture() {
		return ENERGY_BACKGROUND;
	}

	@Override
	public Identifier getForegroundTexture() {
		return ENERGY_FOREGROUND;
	}

	public EnergyVolume getEnergyVolume() {
		return volume.get();
	}

	public <W extends WFractionalVerticalBar> W setEnergyVolume(Supplier<EnergyVolume> volume) {
		setProgressFraction(volume.get()::getFraction);
		setLimitFraction(volume.get()::getSize);

		this.volume = volume;
		return (W) this;
	}
}
