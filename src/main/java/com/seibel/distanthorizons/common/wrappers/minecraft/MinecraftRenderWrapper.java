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

package com.seibel.distanthorizons.common.wrappers.minecraft;

import java.awt.Color;
import java.lang.invoke.MethodHandles;

import com.seibel.distanthorizons.common.wrappers.WrapperFactory;

import com.seibel.distanthorizons.core.logging.DhLoggerBuilder;
import com.seibel.distanthorizons.core.wrapperInterfaces.misc.ILightMapWrapper;

import com.seibel.distanthorizons.core.wrapperInterfaces.world.IClientLevelWrapper;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.ILevelWrapper;
import com.seibel.distanthorizons.core.util.math.Vec3d;
import com.seibel.distanthorizons.core.util.math.Vec3f;
import com.seibel.distanthorizons.core.wrapperInterfaces.IWrapperFactory;
import com.seibel.distanthorizons.core.wrapperInterfaces.minecraft.IMinecraftRenderWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL15;


/**
 * A singleton that contains everything
 * related to rendering in Minecraft.
 *
 * @author James Seibel
 * @version 12-12-2021
 */
//@Environment(EnvType.CLIENT)
public class MinecraftRenderWrapper implements IMinecraftRenderWrapper
{
    public static final MinecraftRenderWrapper INSTANCE = new MinecraftRenderWrapper();

    private static final Logger LOGGER = DhLoggerBuilder.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static final IWrapperFactory FACTORY = WrapperFactory.INSTANCE;

    /**
     * In the case of immersive portals multiple levels may be active at once, causing conflicting lightmaps. <br>
     * Requiring the use of multiple LightMapWrapper.
     */
   // public ConcurrentHashMap<IDimensionTypeWrapper, LightMapWrapper> lightmapByDimensionType = new ConcurrentHashMap<>();

    /**
     * Holds the render buffer that should be used when displaying levels to the screen.
     * This is used for Optifine shader support so we can render directly to Optifine's level frame buffer.
     */
    public int finalLevelFrameBufferId = -1;



    @Override
    public Vec3f getLookAtVector()
    {
        net.minecraft.util.math.Vec3d look = MC.getRenderViewEntity().getLookVec();
        return new Vec3f((float)look.x, (float)look.y, (float)look.z);
    }

    @Override
    /** Unless you really need to know if the player is blind, use {@link MinecraftRenderWrapper#isFogStateSpecial()}/{@link IMinecraftRenderWrapper#isFogStateSpecial()} instead */
    public boolean playerHasBlindingEffect()
    {
        return MC.player.getActivePotionEffect(MobEffects.BLINDNESS) != null;
    }

    @Override
    public Vec3d getCameraExactPosition()
    {
        float partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;
	    Entity entity = MC.getRenderViewEntity();
	    if (entity == null) {
		    return new Vec3d(0, 0, 0);
	    }
	    
	    double x = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
	    double y = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + entity.getEyeHeight();
	    double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
	    
	    return new Vec3d(x, y, z);
    }

    @Override
    public Color getFogColor(float partialTicks)
    {
        float[] colorValues = new float[4];
        GL15.glGetFloatv(GL15.GL_FOG_COLOR, colorValues);
        return new Color(
            Math.max(0f, Math.min(colorValues[0], 1f)), // r
            Math.max(0f, Math.min(colorValues[1], 1f)), // g
            Math.max(0f, Math.min(colorValues[2], 1f)), // b
            Math.max(0f, Math.min(colorValues[3], 1f))  // a
        );
        // TODO ?
    }
    // getSpecialFogColor() is the same as getFogColor()

    @Override
    public Color getSkyColor()
    {
        if (!MC.world.provider.hasSkyLight())
        {
	        float partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;
            net.minecraft.util.math.Vec3d color = MC.world.provider.getSkyColor(MC.getRenderViewEntity(), partialTicks);

            return new Color((float) color.x, (float) color.y, (float) color.z);
        }
        else
        {
            return new Color(0, 0, 0);
        }
    }

    @Override
    public double getFov(float partialTicks)
    {
        return MC.gameSettings.fovSetting;
    }

    /** Measured in chunks */
    @Override
    public int getRenderDistance()
    {
       return MC.gameSettings.renderDistanceChunks - 2;
    }

    @Override
    public int getScreenWidth()
    {
        // alternate ways of getting the window's resolution,
        // using one of these methods may fix the optifine render resolution bug
        // TODO: test these once we can run with Optifine again
//		int[] heightArray = new int[1];
//		int[] widthArray = new int[1];
//
//		long window = GLProxy.getInstance().minecraftGlContext;
//		GLFW.glfwGetWindowSize(window, widthArray, heightArray); // option 1
//		GLFW.glfwGetFramebufferSize(window, widthArray, heightArray); // option 2



        int width = MC.displayWidth;
        return width;
    }
    @Override
    public int getScreenHeight()
    {
        int height = MC.displayWidth;
        return height;
    }


    @Override
    public int getTargetFrameBuffer()
    {
        return Minecraft.getMinecraft().getFramebuffer().framebufferObject;
    }

    @Override
    public void clearTargetFrameBuffer() { this.finalLevelFrameBufferId = -1; }

    @Override
    public int getDepthTextureId() {
        final Framebuffer framebuffer = Minecraft.getMinecraft().getFramebuffer();

        //if (AngelicaConfig.enableIris)
        {
         //   return ((IRenderTargetExt)framebuffer).getIris$depthTextureId();
        }

        return framebuffer.depthBuffer;
    }

    @Override
    public int getColorTextureId() {
        int texture = Minecraft.getMinecraft().getFramebuffer().framebufferTexture;
        return texture;
    }

    @Override
    public int getTargetFrameBufferViewportWidth()
    {
        return Minecraft.getMinecraft().getFramebuffer().framebufferWidth;
    }

    @Override
    public int getTargetFrameBufferViewportHeight()
    {
        return  Minecraft.getMinecraft().getFramebuffer().framebufferHeight;
    }

    @Override
    public ILightMapWrapper getLightmapWrapper(ILevelWrapper level) { return new LightMapWrapper(); }

    @Override
    public boolean isFogStateSpecial()
    {
        boolean isBlind = this.playerHasBlindingEffect();
        //isBlind |= fluidState.is(FluidTags.WATER);
        //isBlind |= fluidState.is(FluidTags.LAVA);
        /* TODO */
        return isBlind;
    }

    /**
     * It's better to use {@link MinecraftRenderWrapper#setLightmapId(int, IClientLevelWrapper)} if possible,
     * however old MC versions don't support it.
     */
    /*
    public void updateLightmap(NativeImage lightPixels, IClientLevelWrapper level)
    {
        // Using ClientLevelWrapper as the key would be better, but we don't have a consistent way to create the same
        // object for the same MC level and/or the same hash,
        // so this will have to do for now
        IDimensionTypeWrapper dimensionType = level.getDimensionType();

        LightMapWrapper wrapper = this.lightmapByDimensionType.computeIfAbsent(dimensionType, (dimType) -> new LightMapWrapper());
        wrapper.uploadLightmap(lightPixels);
    }
    public void setLightmapId(int tetxureId, IClientLevelWrapper level)
    {
        // Using ClientLevelWrapper as the key would be better, but we don't have a consistent way to create the same
        // object for the same MC level and/or the same hash,
        // so this will have to do for now
        IDimensionTypeWrapper dimensionType = level.getDimensionType();

        LightMapWrapper wrapper = this.lightmapByDimensionType.computeIfAbsent(dimensionType, (dimType) -> new LightMapWrapper());
        wrapper.setLightmapId(tetxureId);
    }*/

}
