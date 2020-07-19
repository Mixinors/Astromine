package com.github.chainmailstudios.astromine.common.widget;

import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import net.minecraft.text.TranslatableText;

import java.util.function.Supplier;

public class WBaseVolumeFractionalVerticalBar extends WFractionalVerticalBar {
	private Supplier<BaseVolume> volume;

	public WBaseVolumeFractionalVerticalBar() {
		super();

		setUnit(new TranslatableText("text.astromine.base"));
	}

	public BaseVolume getFluidVolume() {
		return volume.get();
	}

	public <W extends WFractionalVerticalBar> W setBaseVolume(Supplier<BaseVolume> volume) {
		setProgressFraction(volume.get()::getFraction);
		setLimitFraction(volume.get()::getSize);

		this.volume = volume;
		return (W) this;
	}
}
