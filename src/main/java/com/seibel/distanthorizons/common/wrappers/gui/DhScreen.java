package com.seibel.distanthorizons.common.wrappers.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

public class DhScreen extends GuiScreen {
    protected String title;

    public DhScreen(String text) {
        title = text;
    }

    protected GuiButton addBtn(GuiButton button) {
        this.buttonList.add(button);
        return button;
    }

    protected void DhDrawCenteredString(String text, int x, int y, int color) {
        if (I18n.hasKey(text)) {
            text = I18n.format(text);
        }
        drawCenteredString(fontRenderer, text, x, y, color);
    }

    protected void DhDrawString(String text, int x, int y, int color) {
        if (I18n.hasKey(text)) {
            text = I18n.format(text);
        }
        drawString(fontRenderer, text, x, y, color);
    }

    protected void DhRenderTooltip(List<String> list, int x, int y) {
        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i);
            if (I18n.hasKey(text)) {
                text = I18n.format(text);
            }
            list.set(i, text);
        }
        drawHoveringText(list, x, y, fontRenderer);
    }

    protected void DhRenderTooltip(String text, int x, int y) {
        if (I18n.hasKey(text)) {
            text = I18n.format(text);
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(text);
        drawHoveringText(list, x, y, fontRenderer);
    }
}
