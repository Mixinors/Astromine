package com.github.chainmailstudios.astromine.common.utilities;

import com.github.vini2003.blade.client.utilities.Instances;
import com.github.vini2003.blade.client.utilities.Texts;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import com.github.vini2003.blade.common.widget.base.PanelWidget;
import com.github.vini2003.blade.common.widget.base.TextWidget;
import me.shedaniel.cloth.api.client.events.v0.ScreenRenderCallback;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ScreenUtilities {
    /**
     *  Unused for now due to a Blade CME.
     */
    public static void showPopup(BaseScreenHandler handler, Text promptText, Runnable ifYes, Runnable ifNo) {
        PanelWidget popup = new PanelWidget();
        popup.setPosition(Position.of(Instances.client().getWindow().getScaledWidth() / 2 - ((18 + Texts.width(promptText) + 18 ) / 2), Instances.client().getWindow().getScaledHeight() / 2 - (42 / 2)));
        popup.setSize(Size.of(18 + Texts.width(promptText) + 18, 42));

        handler.addWidget(popup);

        TextWidget sure = new TextWidget();
        sure.setPosition(Position.of(popup.getX() + 18, popup.getY() + 9));
        sure.setSize(Size.of(Texts.width(promptText), Texts.height()));

        popup.addWidget(sure);

        ButtonWidget buttonYes = new ButtonWidget();

        buttonYes.setPosition(Position.of(popup.getX() + 7, sure.getY() + 7));
        buttonYes.setSize(Size.of(18, 27));
        buttonYes.setLabel(new LiteralText("text.astromine.yes").formatted(Formatting.GREEN));

        popup.addWidget(buttonYes);

        ButtonWidget buttonNo = new ButtonWidget();
        buttonNo.setPosition(Position.of(popup.getX() + popup.getWidth() - 7 - 18, sure.getY() + 7));
        buttonNo.setSize(Size.of(18, 27));
        buttonNo.setLabel(new LiteralText("text.astromine.no").formatted(Formatting.RED));
        
        buttonYes.setClickAction(() -> {
            ifYes.run();

            popup.setPosition(Position.of(Integer.MAX_VALUE, Integer.MAX_VALUE));
            sure.setPosition(Position.of(Integer.MAX_VALUE, Integer.MAX_VALUE));
            buttonYes.setPosition(Position.of(Integer.MAX_VALUE, Integer.MAX_VALUE));
            buttonNo.setPosition(Position.of(Integer.MAX_VALUE, Integer.MAX_VALUE));

            popup.setHidden(true);
            sure.setHidden(true);
            buttonYes.setHidden(true);
            buttonNo.setHidden(true);

            return null;
        });

        buttonNo.setClickAction(() -> {
            ifNo.run();

            popup.setPosition(Position.of(Integer.MAX_VALUE, Integer.MAX_VALUE));
            sure.setPosition(Position.of(Integer.MAX_VALUE, Integer.MAX_VALUE));
            buttonYes.setPosition(Position.of(Integer.MAX_VALUE, Integer.MAX_VALUE));
            buttonNo.setPosition(Position.of(Integer.MAX_VALUE, Integer.MAX_VALUE));

            popup.setHidden(true);
            sure.setHidden(true);
            buttonYes.setHidden(true);
            buttonNo.setHidden(true);

            return null;
        });

        popup.addWidget(buttonNo);
    }
}
