package com.github.chainmailstudios.astromine.common.widget;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.google.common.collect.Lists;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class WEnergyVolumeFractionalVerticalBar extends WFloatingPointVerticalBar {
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

	@Override
	public List<Text> getTooltip() {
		return Lists.newArrayList(EnergyUtilities.compoundDisplay(getProgressFraction().get(), getLimitFraction().get()));
	}

	public <W extends WFloatingPointVerticalBar> W setEnergyVolume(Supplier<EnergyVolume> volume) {
		setProgressFraction(volume.get()::getAmount);
		setLimitFraction(volume.get()::getMaxAmount);

		this.volume = volume;
		return (W) this;
	}
}
