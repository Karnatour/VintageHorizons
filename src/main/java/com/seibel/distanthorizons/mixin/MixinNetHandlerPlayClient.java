package com.seibel.distanthorizons.mixin;

import com.seibel.distanthorizons.core.api.internal.ClientApi;
import com.seibel.distanthorizons.forge.ForgeServerProxy;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient
{
	
	@Inject(method = "handleJoinGame", at = @At("RETURN"))
	private void onHandleJoinGameEnd(SPacketJoinGame packet, CallbackInfo ci)
	{
		ClientApi.INSTANCE.onClientOnlyConnected();
		ForgeServerProxy.connected = true;
	}
	
	@Inject(method = "cleanup", at = @At("HEAD"))
	private void onCleanupStart(CallbackInfo ci)
	{
		ClientApi.INSTANCE.onClientOnlyDisconnected();
		ForgeServerProxy.connected = false;
	}
	
}
