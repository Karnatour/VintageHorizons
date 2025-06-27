package com.seibel.distanthorizons.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.renderer.GlStateManager;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
	
	private static final float A_REALLY_REALLY_BIG_VALUE = 420694206942069.F;
	private static final float A_EVEN_LARGER_VALUE = 42069420694206942069.F;
	
	@Inject(method = "setupFog", at = @At("RETURN"))
	private void onSetupFog(int startCoords, float partialTicks, CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		
		if (player == null) return;
		
		// Check if player is blind
		PotionEffect blindness = player.getActivePotionEffect(Potion.getPotionById(15)); // 15 = blindness
		boolean isBlind = blindness != null;
		
		Vec3d eyePos = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		RayTraceResult result = mc.world.rayTraceBlocks(eyePos, eyePos);
		boolean cameraInFluid = result != null && mc.world.getBlockState(result.getBlockPos()).getMaterial().isLiquid();
		
		boolean disableFog = !isBlind && !cameraInFluid;
		
		if (disableFog) {
			GlStateManager.setFogStart(A_REALLY_REALLY_BIG_VALUE);
			GlStateManager.setFogEnd(A_EVEN_LARGER_VALUE);
		}
	}
}
