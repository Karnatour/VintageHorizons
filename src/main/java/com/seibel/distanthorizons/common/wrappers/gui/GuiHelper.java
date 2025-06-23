package com.seibel.distanthorizons.common.wrappers.gui;

import net.minecraft.client.resources.I18n;

public class GuiHelper {
    /**
     * Helper static methods for versional compat
     */

    public static String TextOrLiteral(String text) {
        return text;
    }

    public static String TextOrTranslatable(String text) {
        if (I18n.hasKey(text)) {
            text = I18n.format(text);
        }
        return text;
    }

    public static String Translatable(String text, Object... args) {
        return I18n.format(text, args);
    }

}
