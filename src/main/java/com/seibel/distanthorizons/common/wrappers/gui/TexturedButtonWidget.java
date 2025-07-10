/*
 *    This file is part of the Distant Horizons mod
 *    licensed under the GNU LGPL v3 License.
 *
 *    Copyright (C) 2020 James Seibel
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, version 3.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.seibel.distanthorizons.common.wrappers.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Creates a button with a texture on it (and a background) that works with all mc versions
 *
 * @author coolGi
 * @version 2023-10-03
 */
public class TexturedButtonWidget extends GuiButton {
    public final boolean renderBackground;

    private final int u;
    private final int v;
    private final int hoveredVOffset;

    private final ResourceLocation textureResourceLocation;

    private final int textureWidth;
    private final int textureHeight;


    public TexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation textureResourceLocation, int textureWidth, int textureHeight, int id, String text) {
        this(x, y, width, height, u, v, hoveredVOffset, textureResourceLocation, textureWidth, textureHeight, id, text, true);
    }

    public TexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation textureResourceLocation, int textureWidth, int textureHeight, int id, String text, boolean renderBackground) {
        super(id, x, y, width, height, text);

        this.u = u;
        this.v = v;
        this.hoveredVOffset = hoveredVOffset;

        this.textureResourceLocation = textureResourceLocation;

        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;

        this.renderBackground = renderBackground;
    }
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			//Render vanilla background
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
			
			//Render DH texture
			mc.getTextureManager().bindTexture(textureResourceLocation);
			drawModalRectWithCustomSizedTexture(this.x, this.y, this.u, (hoveredVOffset * (i - 1)), this.width, this.height, this.textureWidth, this.textureHeight);
		}
	}
}
