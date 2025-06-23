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

package com.seibel.distanthorizons.forge;

import com.seibel.distanthorizons.common.AbstractModInitializer;
import com.seibel.distanthorizons.common.util.ProxyUtil;
import com.seibel.distanthorizons.common.wrappers.chunk.ChunkWrapper;
import com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import com.seibel.distanthorizons.core.api.internal.ClientApi;
import com.seibel.distanthorizons.core.api.internal.SharedApi;
import com.seibel.distanthorizons.core.dependencyInjection.SingletonInjector;
import com.seibel.distanthorizons.core.logging.DhLoggerBuilder;
import com.seibel.distanthorizons.core.logging.f3.F3Screen;
import com.seibel.distanthorizons.core.util.threading.ThreadPoolUtil;
import com.seibel.distanthorizons.core.wrapperInterfaces.chunk.IChunkWrapper;
import com.seibel.distanthorizons.core.wrapperInterfaces.minecraft.IMinecraftClientWrapper;
import com.seibel.distanthorizons.core.wrapperInterfaces.misc.IPluginPacketSender;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.IClientLevelWrapper;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.ILevelWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.AbstractExecutorService;

/**
 * This handles all events sent to the client,
 * and is the starting point for most of the mod.
 *
 * @author James_Seibel
 * @version 2023-7-27
 */
public class ForgeClientProxy implements AbstractModInitializer.IEventProxy
{
	private static final IMinecraftClientWrapper MC = SingletonInjector.INSTANCE.get(IMinecraftClientWrapper.class);
	private static final ForgePluginPacketSender PACKET_SENDER = (ForgePluginPacketSender) SingletonInjector.INSTANCE.get(IPluginPacketSender.class);
	private static final Logger LOGGER = DhLoggerBuilder.getLogger();

	private static World GetEventLevel(WorldEvent e) { return e.getWorld(); }



	@Override
	public void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(FMLCommonHandler.instance());
		ForgePluginPacketSender.setPacketHandler(ClientApi.INSTANCE::pluginMessageReceived);

		ItemStack stack = new ItemStack(Items.DYE, 1, EnumDyeColor.LIGHT_BLUE.getDyeDamage());
		String s = stack.getDisplayName();
		s = s;
	}



	//=============//
	// tick events //
	//=============//

	@SubscribeEvent
	public void clientTickEvent(TickEvent.ClientTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START)
		{
			ClientApi.INSTANCE.clientTickEvent();
		}
	}



	//==============//
	// world events //
	//==============//

	@SubscribeEvent
	public void clientLevelLoadEvent(WorldEvent.Load event)
	{
		LOGGER.info("level load");

		World level = event.getWorld();
		if (!(level instanceof WorldClient))
		{
			return;
		}

        WorldClient clientLevel = (WorldClient) level;
		IClientLevelWrapper clientLevelWrapper = ClientLevelWrapper.getWrapper(clientLevel, true);
		// TODO this causes a crash due to level being set to null somewhere
		ClientApi.INSTANCE.clientLevelLoadEvent(clientLevelWrapper);
	}
	@SubscribeEvent
	public void clientLevelUnloadEvent(WorldEvent.Unload event)
	{
		LOGGER.info("level unload");

        World level = event.getWorld();
		if (!(level instanceof WorldClient))
		{
			return;
		}

        WorldClient clientLevel = (WorldClient) level;
		IClientLevelWrapper clientLevelWrapper = ClientLevelWrapper.getWrapper(clientLevel);
		ClientApi.INSTANCE.clientLevelUnloadEvent(clientLevelWrapper);
	}



	//==============//
	// chunk events //
	//==============//

	@SubscribeEvent
	public void clickBlockEvent(PlayerInteractEvent event)
	{
		if (MC.clientConnectedToDedicatedServer())
		{
			if (SharedApi.isChunkAtBlockPosAlreadyUpdating(event.getPos().getX(),event.getPos().getZ()))
			{
				return;
			}

			//LOGGER.trace("interact or block place event at blockPos: " + event.getPos());

			World level = event.getWorld();

			AbstractExecutorService executor = ThreadPoolUtil.getFileHandlerExecutor();
			if (executor != null)
			{
				executor.execute(() ->
				{
					Chunk chunk = level.getChunk(event.getPos());
					this.onBlockChangeEvent(level, chunk);
				});
			}
		}
	}

	private void onBlockChangeEvent(World level, Chunk chunk)
	{
		ILevelWrapper wrappedLevel = ProxyUtil.getLevelWrapper(level);
		SharedApi.INSTANCE.chunkBlockChangedEvent(new ChunkWrapper(chunk, wrappedLevel), wrappedLevel);
	}

	@SubscribeEvent
	public void clientChunkLoadEvent(ChunkEvent.Load event)
	{
		if (MC.clientConnectedToDedicatedServer())
		{
			ILevelWrapper wrappedLevel = ProxyUtil.getLevelWrapper(GetEventLevel(event));
			IChunkWrapper chunk = new ChunkWrapper(event.getChunk(), wrappedLevel);
			SharedApi.INSTANCE.chunkLoadEvent(chunk, wrappedLevel);
		}
	}



	//==============//
	// key bindings //
	//==============//

	@SubscribeEvent
	public void registerKeyBindings(InputEvent.KeyInputEvent event)
	{
		if (Minecraft.getMinecraft().player == null)
		{
			return;
		}
		/* TODO if (event.getAction() != GLFW.GLFW_PRESS)
		{
			return;
		}*/

		// TODO ClientApi.INSTANCE.keyPressedEvent(event.getKey());
	}


	//===========//
	// rendering //
	//===========//

	@SubscribeEvent
	public void afterLevelRenderEvent(TickEvent.RenderTickEvent event)
	{
		if (event.type.equals(TickEvent.RenderTickEvent.Type.RENDER))
		{
			try
			{
				// should generally only need to be set once per game session
				// allows DH to render directly to Optifine's level frame buffer,
				// allowing better shader support
				//MinecraftRenderWrapper.INSTANCE.finalLevelFrameBufferId = GL32.glGetInteger(GL32.GL_FRAMEBUFFER_BINDING);
			}
			catch (Exception | Error e)
			{
				LOGGER.error("Unexpected error in afterLevelRenderEvent: "+e.getMessage(), e);
			}
		}
	}

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.isCanceled() || !mc.gameSettings.showDebugInfo) return;

        F3Screen.addStringToDisplay(event.getRight());
    }
	
}
