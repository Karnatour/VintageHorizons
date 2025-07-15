package com.seibel.distanthorizons.mixin.mod.mist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.liahim.mist.handlers.FogRenderer;

@Mixin(FogRenderer.class)
public class MixinFogRenderer {
    @Inject(method = "fogDensity", at = @At("HEAD"), cancellable = true)
    private void cancelFogDensity(EntityViewRenderEvent.FogDensity e, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "fogRender", at = @At("HEAD"), cancellable = true)
    private void cancelFogRender(float partialTicks, WorldClient world, Minecraft mc, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "fogRenderOld", at = @At("HEAD"), cancellable = true)
    private void cancelFogRenderOld(float partialTicks, WorldClient world, Minecraft mc, CallbackInfo ci) {
        ci.cancel();
    }
    

}
