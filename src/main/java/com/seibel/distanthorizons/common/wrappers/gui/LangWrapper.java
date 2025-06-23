package com.seibel.distanthorizons.common.wrappers.gui;

import com.seibel.distanthorizons.core.wrapperInterfaces.config.ILangWrapper;
import net.minecraft.client.resources.I18n;

public class LangWrapper implements ILangWrapper {
    public static final LangWrapper INSTANCE = new LangWrapper();

    @Override
    public boolean langExists(String str) {
        return I18n.hasKey(str);
    }

    @Override
    public String getLang(String str) {
        return I18n.format(str);
    }

}
