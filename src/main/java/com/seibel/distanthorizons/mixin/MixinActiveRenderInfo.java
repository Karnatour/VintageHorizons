package com.seibel.distanthorizons.mixin;

import com.seibel.distanthorizons.RenderHelper;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.FloatBuffer;

@Mixin(ActiveRenderInfo.class)
public class MixinActiveRenderInfo
{
	@Shadow private static FloatBuffer MODELVIEW;
	@Shadow private static FloatBuffer PROJECTION;
	
	@Inject(method = "updateRenderInfo", at = @At("TAIL"))
	private static void asini$onUpdateRenderInfo(Entity entityplayerIn, boolean p_74583_1_, CallbackInfo ci)
	{
		RenderHelper.setProjectionMatrix(PROJECTION);
		RenderHelper.setModelViewMatrix(MODELVIEW);
	}
	
}
