package com.github.chainmailstudios.astromine.widget;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import net.minecraft.text.TranslatableText;

public class WBaseVolumeFractionalVerticalBar extends WFractionalVerticalBar {
	private BaseVolume volume;

	public WBaseVolumeFractionalVerticalBar() {
		super();

		setUnit(new TranslatableText("text.astromine.base"));
	}

	public BaseVolume getFluidVolume() {
		return volume;
	}

	public <W extends WFractionalVerticalBar> W setProgressFraction(BaseVolume volume) {
		setProgressFraction(volume::getFraction);
		setLimitFraction(volume::getSize);

		this.volume = volume;
		return (W) this;
	}
}
