package com.seibel.distanthorizons.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer{
	@Inject(method = "setupFog", at = @At("RETURN"))
	private void disableFog(int startCoords, float partialTicks, CallbackInfo ci) {
		float start = 1024 * 1024 * 15F;
		float end = 1024 * 1024 * 16F;
		
		GL11.glFogf(GL11.GL_FOG_START, start);
		GL11.glFogf(GL11.GL_FOG_END, end);
		
	}
}
